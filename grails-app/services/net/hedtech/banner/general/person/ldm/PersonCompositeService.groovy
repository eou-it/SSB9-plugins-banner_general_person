/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.person.PersonAddress
import net.hedtech.banner.general.person.PersonBasicPersonBase
import net.hedtech.banner.general.person.PersonEmail
import net.hedtech.banner.general.person.PersonIdentificationNameCurrent
import net.hedtech.banner.general.person.PersonTelephone
import net.hedtech.banner.general.person.ldm.v1.Address
import net.hedtech.banner.general.person.ldm.v1.Credential
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.person.ldm.v1.Email
import net.hedtech.banner.general.person.ldm.v1.Name
import net.hedtech.banner.general.person.ldm.v1.Person
import net.hedtech.banner.general.person.ldm.v1.Phone
import net.hedtech.banner.general.system.AddressType
import net.hedtech.banner.general.system.County
import net.hedtech.banner.general.system.EmailType
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.Nation
import net.hedtech.banner.general.system.State
import net.hedtech.banner.general.system.TelephoneType
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusParentCategory
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Transactional
class PersonCompositeService extends LdmService {

    def personIdentificationNameCurrentService
    def personBasicPersonBaseService
    def personAddressService
    def personTelephoneService
    def personEmailService
    def globalUniqueIdentifierService
    def maritalStatusCompositeService
    def static ldmName = 'persons'
    static final String PERSON_ADDRESS_TYPE = "person.addresses.addressType"
    static final String PERSON_PHONE_TYPE = "person.phones.phoneType"
    static final String PERSON_EMAIL_TYPE ="person.emails.emailType"
    static final String PROCESS_CODE ="LDM"


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def list(params) {
        def pidms = []
            // This'll be populated by the pidm(s) returned from common matching, or the list we query from matching results.


        def persons = buildLdmPersonObjects(pidms)
        def resultList
        try {  // Avoid restful-api plugin dependencies.
            resultList = this.class.classLoader.loadClass('net.hedtech.restfulapi.PagedResultArrayList').newInstance(persons.values(), persons.values()?.size())
        }
        catch (ClassNotFoundException e) {
            resultList = persons.values()
        }
        resultList
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def get(id) {
        def entity = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(ldmName, id)
        if( !entity ) {
            throw new ApplicationException("Person","@@r1:not.found.message:BusinessLogicValidationException@@")
        }
        def resultList = buildLdmPersonObjects([entity.domainKey?.toInteger()])
        resultList.get(entity.domainKey?.toInteger())
    }

    // TODO: Break into more functions.
    def create(Map person) {
        def newPersonIdentification
        person?.names?.each { it ->
            if( it.nameType == 'Primary' ) {
                newPersonIdentification = it
            }
        }
        newPersonIdentification.put('bannerId','GENERATED')
        newPersonIdentification.put('entityIndicator', 'P')
        newPersonIdentification.put('changeIndicator', null)
        newPersonIdentification.remove('nameType') // ID won't generate if this is set.
        //Create the new PersonIdentification record
        PersonIdentificationNameCurrent newPersonIdentificationName =
                personIdentificationNameCurrentService.create(newPersonIdentification)
        //Fix the GUID if provided as DB will assign one
        if( person.guid ) {
            updateGuidValue(newPersonIdentificationName.id, person.guid)
        }
        else {
            def entity = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(ldmName, newPersonIdentificationName.id)
            person.put('guid', entity)
        }

        //Copy personBase attributes into person map from Primary names object.
        person?.credentials?.each { it ->
            if( it.credentialType == 'Social Security Number' ) {
                newPersonIdentification.put('ssn',it?.credentialId)
            }
        }
        person.put('namePrefix', newPersonIdentification.get('namePrefix'))
        person.put('nameSuffix', newPersonIdentification.get('nameSuffix'))
        person.put('preferenceFirstName', newPersonIdentification.get('preferenceFirstName'))
        //Translate enumerations and defaults
        person.put('sex', person?.sex == 'Male' ? 'M':(person?.sex == 'Female' ? 'F' : 'N'))
        def maritalStatus = person.maritalStatus?.guid ? maritalStatusCompositeService.get(person.maritalStatus?.guid) : null
        person.put('maritalStatus', maritalStatus ? MaritalStatus.findByCode(maritalStatus?.code) : null)
        person.put('ethnic', person.ethnic == "Non-Hispanic" ? '1' :(person.ethnic == "Hispanic" ? '2' : null))
        person.put('deadIndicator', person.get('dateDeceased') != null ? 'Y' : null)
        person.put('pidm', newPersonIdentificationName?.pidm)
        person.put('armedServiceMedalVetIndicator', false)
        PersonBasicPersonBase newPersonBase = personBasicPersonBaseService.create(person)
        def names = []
        def name = new Name(newPersonIdentificationName, newPersonBase)
        name.setNameType("Primary")
        names << name
        //Store the credential we already have
        def credentials = []
        if( newPersonBase.ssn ) {
            credentials << new Credential("Social Security Number",
                    newPersonBase.ssn,
                    null,
                    null)
        }
        def addresses = createAddresses(newPersonIdentificationName.pidm,
                person.addresses instanceof List ? person.addresses : [])
        def phones = createPhones(newPersonIdentificationName.pidm,
                person.phones instanceof List ? person.phones : [])
        def emails = createEmails(newPersonIdentificationName.pidm,
                person.emails instanceof List ? person.emails : [])
        //Build decorator to return LDM response.
        def newPerson = new Person( newPersonBase, person.guid, credentials, addresses, phones, emails, names, maritalStatus)
        newPerson
    }

    // TODO: validate guid format.
    private void updateGuidValue(def id, def guid) {
        // Update the GUID to the one we received.
        GlobalUniqueIdentifier newEntity = GlobalUniqueIdentifier.findByLdmNameAndDomainId(ldmName, id)
        if( !newEntity ) {
            throw new ApplicationException("Person","@@r1:guid.record.not.found.message:Person@@")
        }
        if( !newEntity ) {
            throw new ApplicationException("Person","@@r1:guid.not.found.message:Person:BusinessLogicValidationException@@")
        }
        newEntity.guid = guid
        globalUniqueIdentifierService.update(newEntity)
    }

    def createAddresses (def pidm, List<Map> newAddresses) {
        def addresses = []
        newAddresses?.each { activeAddress ->
            IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_ADDRESS_TYPE,activeAddress.addressType)
            if (rule.translationValue == activeAddress.addressType && !addresses.contains { activeAddress.addressType == rule.value }) {
                activeAddress.addressType = AddressType.findByCode(rule.value)
                activeAddress.state = State.findByDescription(activeAddress.state)
                if( activeAddress?.country?.code ) {
                    activeAddress.nation = Nation.findByNation(activeAddress?.country?.code)
                    if( !activeAddress.nation ) { log.warn "Nation not found for code: ${activeAddress?.country?.code}" }
                }
                if( activeAddress.county ) {
                    activeAddress.county = County.findByDescription(activeAddress.county)
                    if( !activeAddress.county ) { log.warn "County not found for code: ${activeAddress.county}" }
                }
                activeAddress.put('pidm', pidm)
                validateAddressRequiredFields(activeAddress)
                def address = personAddressService.create(activeAddress)
                def addressDecorator = new Address(address)
                addressDecorator.addressType = rule.translationValue
                addresses << addressDecorator
            }

        }
        addresses
    }

    def validateAddressRequiredFields(address) {
        if( !address.addressType ) { throw new RestfulApiValidationException('addressType.not.found')}
        if( !address.state ) { throw new RestfulApiValidationException('region.not.found')}
        if( !address.streetLine1 ) { throw new RestfulApiValidationException('streetAddress.not.provided')}
        if( !address.city ) { throw new RestfulApiValidationException('city.not.provided')}
        if( !address.zip ) { throw new RestfulApiValidationException('postalCode.not.provided')}
    }

    def createPhones(def pidm, List newPhones) {
        def phones = []
        newPhones?.each { activePhone ->
            IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_ADDRESS_TYPE, activePhone.phoneType)
            if (rule.translationValue == activePhone.phoneType &&
                    !phones.contains { activePhone.phoneType == rule.value }) {
                activePhone.put('telephoneType', TelephoneType.findByCode(rule.value) )
                activePhone.put('pidm', pidm)
                def phone = personTelephoneService.create(activePhone)
                def phoneDecorator = new Phone(phone)
                phoneDecorator.phoneType = rule.translationValue
                phones << phoneDecorator
            }

        }
        phones
    }

    def createEmails(def pidm, List newEmails) {
        def emails = []

        newEmails?.each { activeEmail ->
            IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_EMAIL_TYPE, activeEmail.emailType)
            if (rule?.translationValue == activeEmail.emailType &&
                    !emails.contains { activeEmail.emailType == rule?.value }) {
                activeEmail.emailType = EmailType.findByCode(rule.value)
                activeEmail.put('pidm', pidm)
                def email = personEmailService.create(activeEmail)
                def emailDecorator = new Email(email)
                emailDecorator.emailType = rule.translationValue
                emails << emailDecorator
            }
        }
        emails
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def buildLdmPersonObjects(def pidms) {
        def persons = [:]
        if( pidms.size() < 1 ) {
            return persons
        }
        List<PersonBasicPersonBase> personBaseList = PersonBasicPersonBase.findAllByPidmInList(pidms)
        List<PersonIdentificationNameCurrent> personIdentificationList = PersonIdentificationNameCurrent.findAllByPidmInList(pidms)
        def personAddressList = PersonAddress.fetchActiveAddressesByPidmInList(pidms)
        def personTelephoneList = PersonTelephone.fetchActiveTelephoneByPidmInList(pidms)
        List<PersonEmail> personEmailList = PersonEmail.findAllByStatusIndicatorAndPidmInList('A', pidms)
        personBaseList.each { personBase ->
            Person currentRecord = new Person(personBase)
            currentRecord.maritalStatusDetail = maritalStatusCompositeService.fetchByMaritalStatusCode(personBase.maritalStatus?.code)
            if( personBase.ssn ) {
                currentRecord.credentials << new Credential("Social Security Number",
                        personBase.ssn,
                        null,
                        null)
            }
            persons.put(currentRecord.pidm, currentRecord)
        }
        def domainIds = []
        personIdentificationList.each { identification ->
            Person currentRecord = persons.get(identification.pidm) ?: new Person(null)
            def name = new Name(identification, currentRecord)
            name.setNameType("Primary")
            domainIds << identification.id
            currentRecord.names << name
            persons.put(identification.pidm, currentRecord)
        }
        GlobalUniqueIdentifier.findAllByLdmNameAndDomainIdInList(ldmName, domainIds).each { guid ->
            Person currentRecord = persons.get(guid.domainKey.toInteger())
            currentRecord.guid = guid.guid
            persons.put(guid.domainKey.toInteger(), currentRecord)
        }
        personAddressList.each { activeAddress ->
            Person currentRecord = persons.get(activeAddress.pidm)
            IntegrationConfiguration rule = findAllByProcessCodeAndSettingNameAndValue(PROCESS_CODE, PERSON_ADDRESS_TYPE,activeAddress.addressType.code)
            if (rule.value == activeAddress.addressType?.code &&
                    !currentRecord.addresses.contains { it.addressType == rule.translationValue }) {
                def address = new Address(activeAddress)
                address.addressType = rule.translationValue
                currentRecord.addresses << address
            }
        }
        personTelephoneList.each { activePhone ->
            Person currentRecord = persons.get(activePhone.pidm)
            IntegrationConfiguration rule = findAllByProcessCodeAndSettingNameAndValue(PROCESS_CODE, PERSON_PHONE_TYPE,activePhone?.telephoneType.code)
                if (rule.value == activePhone?.telephoneType?.code &&
                        !(currentRecord.phones.contains { it.phoneType == rule.translationValue })) {
                    def phone = new Phone(activePhone)
                    phone.phoneType = rule.translationValue
                    currentRecord.phones << phone
                }
            persons.put(activePhone.pidm, currentRecord)
        }
        personEmailList.each { PersonEmail activeEmail ->
            Person currentRecord = persons.get(activeEmail.pidm)
            IntegrationConfiguration rule = findAllByProcessCodeAndSettingNameAndValue(PROCESS_CODE, PERSON_EMAIL_TYPE,activeEmail?.emailType.code)
                if (rule.value == activeEmail?.emailType?.code &&
                        !currentRecord.emails.contains { it.emailType == rule.translationValue }) {
                    def email = new Email(activeEmail)
                    email.emailType = rule.translationValue
                    currentRecord.emails << email
                }

            persons.put(activeEmail.pidm, currentRecord)
        }
        persons // Map of person objects with pidm as index.
    }

    // @Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
    /**
     * Updates the Person Information like PersonIdentificationNameCurrent, PersonBasicPersonBase, Address
     * Telephones and Emails
     * @param person - Map containing the changes person details
     * @return erson
     */
    def update(Map person) {

        def changedPersonIdentification
        person?.names?.each { it ->
            if (it.nameType == 'Primary') {
                changedPersonIdentification = it
            }
        }
        def entity = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(ldmName, person.guid)
        if (!entity) {
            throw new ApplicationException("Person", "@@r1:guid.record.not.found.message:Person@@")
        }

        List<PersonIdentificationNameCurrent> personIdentificationList = PersonIdentificationNameCurrent.findAllByPidmInList([entity.domainKey?.toInteger()])

        def personIdentification
        personIdentificationList.each { identification ->
            if (identification.changeIndicator == null) {
                personIdentification = identification
            }
        }
        //update PersonIdentificationNameCurrent

        personIdentification.firstName = changedPersonIdentification.firstName
        personIdentification.lastName = changedPersonIdentification.lastName
        personIdentification.middleName = changedPersonIdentification.middleName
        PersonIdentificationNameCurrent newPersonIdentificationName =personIdentificationNameCurrentService.update(personIdentification)

        //update PersonBasicPersonBase
        PersonBasicPersonBase newPersonBase = updatePersonBasicPersonBase(entity, person, changedPersonIdentification)
        def names = []
        def name = new Name(newPersonIdentificationName, newPersonBase)
        name.setNameType("Primary")
        names << name
        //Store the credential we already have
        def credentials = person?.credentials


        //update Address
        def addresses = updateAddresses(entity.domainKey?.toInteger(), person.addresses instanceof List ? person.addresses : [])

        //update Telephones
        def phones = updatePhones(entity.domainKey?.toInteger(), person.phones instanceof List ? person.phones : [])

        //update Emails
        def emails =  updateEmails(entity.domainKey?.toInteger(), person.emails instanceof List ? person.emails : [])
        //Build decorator to return LDM response.
        def newPerson = new Person(newPersonBase, person.guid, credentials, addresses, phones, emails, names, newPersonBase.maritalStatus)
        newPerson


    }

    private PersonBasicPersonBase updatePersonBasicPersonBase(entity, person, changedPersonIdentification) {
        List<PersonBasicPersonBase> personBaseList = PersonBasicPersonBase.findAllByPidmInList([entity.domainKey?.toInteger()])
        PersonBasicPersonBase newPersonBase
        personBaseList.each { personBase ->
            //Copy personBase attributes into person map from Primary names object.
            person?.credentials?.each { it ->
                if (it.credentialType == 'Social Security Number') {
                    personBase.ssn = it?.credentialId
                }
            }
            personBase.namePrefix = changedPersonIdentification.get('namePrefix')
            personBase.nameSuffix = changedPersonIdentification.get('nameSuffix')
            personBase.preferenceFirstName = changedPersonIdentification.get('preferenceFirstName')
            //Translate enumerations and defaults
            personBase.sex = person?.sex == 'Male' ? 'M' : (person?.sex == 'Female' ? 'F' : 'N')
            def maritalStatus = person.maritalStatus?.guid ? maritalStatusCompositeService.get(person.maritalStatus?.guid) : null
            personBase.maritalStatus = maritalStatus ? MaritalStatus.findByCode(maritalStatus?.code) : null
            personBase.ethnic = person.ethnic == "Non-Hispanic" ? '1' : (person.ethnic == "Hispanic" ? '2' : null)
            personBase.deadIndicator = person.get('dateDeceased') != null ? 'Y' : null
            personBase.armedServiceMedalVetIndicator = false
            newPersonBase = personBasicPersonBaseService.update(personBase)

        }
        return newPersonBase
    }


    private  updateAddresses(def pidm, List<Map> newAddresses) {
        def addresses = []
        newAddresses?.each { activeAddress ->
            IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_ADDRESS_TYPE, activeAddress.addressType)
            if (rule.translationValue == activeAddress.addressType && !addresses.contains {
                activeAddress.addressType == rule.value
            }) {
                def currAddress = PersonAddress.fetchListActiveAddressByPidmAndAddressType([pidm], [AddressType.findByCode(rule.value)]).get(0)
                currAddress.state = State.findByDescription(activeAddress.state)
                if (activeAddress?.country?.code) {
                    currAddress.nation = Nation.findByNation(activeAddress?.country?.code)
                    if (!currAddress.nation) {
                        log.warn "Nation not found for code: ${activeAddress?.country?.code}"
                    }
                }
                if (activeAddress.county) {
                    currAddress.county = County.findByDescription(activeAddress.county)
                    if (!currAddress.county) {
                        log.warn "County not found for code: ${activeAddress.county}"
                    }
                }
                def address = personAddressService.update(currAddress)
                def addressDecorator = new Address(address)
                addressDecorator.addressType = rule.translationValue
                addresses << addressDecorator
            }
        }
        addresses

    }

    private  updatePhones(def pidm, List newPhones) {
        def phones = []
        newPhones?.each { activePhone ->
            IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_ADDRESS_TYPE, activePhone.phoneType)
            if (rule.translationValue == activePhone.phoneType &&
                    !phones.contains { activePhone.phoneType == rule.value }) {
                def telephoneType =  TelephoneType.findByCode(rule.value)
                def personTelephoneList = PersonTelephone.fetchActiveTelephoneByPidmInList([pidm])
                personTelephoneList.each { curPhones ->
                    if(curPhones.telephoneType.code ==rule.value){
                        // curPhones.phoneArea
                        curPhones.phoneExtension = activePhone.phoneExtension
                        curPhones.phoneNumber = activePhone.phoneNumber
                        def phone = personTelephoneService.update(curPhones)
                        def phoneDecorator = new Phone(phone)
                        phoneDecorator.phoneType = rule.translationValue
                        phones << phoneDecorator
                    }
                }
            }
        }
        phones

    }


    private updateEmails(def pidm, List newEmails) {
        def emails = []
        newEmails?.each { activeEmail ->
            IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_EMAIL_TYPE, activeEmail.emailType)
            if (rule?.translationValue == activeEmail.emailType &&
                    !emails.contains { activeEmail.emailType == rule?.value }) {
                def emailList =  PersonEmail.findAllByStatusIndicatorAndPidmInList('A', [pidm])
                emailList.each { curEmails ->
                    if(curEmails.emailType.code ==rule.value){
                        curEmails.emailAddress = activeEmail.emailAddress
                        def email = personEmailService.update(curEmails)
                        def emailDecorator = new Email(email)
                        emailDecorator.emailType = rule.translationValue
                        emails << emailDecorator
                    }
                }
            }
        }
        emails
    }
}
