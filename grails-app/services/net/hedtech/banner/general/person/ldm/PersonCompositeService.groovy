/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
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
import net.hedtech.banner.general.system.Ethnicity
import net.hedtech.banner.general.system.InstitutionalDescription
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.Nation
import net.hedtech.banner.general.system.State
import net.hedtech.banner.general.system.TelephoneType
import net.hedtech.banner.general.system.ldm.v1.Metadata
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
    def ethnicityCompositeService
    def static ldmName = 'persons'
    static final String PERSON_ADDRESS_TYPE = "person.addresses.addressType"
    static final String PERSON_PHONE_TYPE = "person.phones.phoneType"
    static final String PERSON_EMAIL_TYPE = "person.emails.emailType"
    static final String PROCESS_CODE = "LDM"
    static final String PERSON_ADDRESS_NATION = "person.addresses.country.code"


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
        Map<Integer,Person> persons = [:]
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

        //Copy ssn attribute from credential to person map.
        person?.credentials?.each { it ->
            if( it.credentialType == 'Social Security Number' ) {
                person.put('ssn',it?.credentialId)
            }
            else if( it.credentialType == 'Social Insurance Number' ) {
                person.put('ssn',it?.credentialId)
            }
        }
        //Copy personBase attributes into person map from Primary names object.
        log.error person.birthDate
        person.put('namePrefix', newPersonIdentification.get('namePrefix'))
        person.put('nameSuffix', newPersonIdentification.get('nameSuffix'))
        person.put('preferenceFirstName', newPersonIdentification.get('preferenceFirstName'))
        //Translate enumerations and defaults
        person.put('sex', person?.sex == 'Male' ? 'M':(person?.sex == 'Female' ? 'F' :(person?.sex == 'Unknown' ? 'N' : null)))
        def maritalStatus = person.maritalStatusDetail?.guid ? maritalStatusCompositeService.get(person.maritalStatusDetail?.guid) : null
        person.put('maritalStatus', maritalStatus ? MaritalStatus.findByCode(maritalStatus?.code) : null)
        def ethnicity = person.ethnicityDetail?.guid ? ethnicityCompositeService.get(person.ethnicityDetail?.guid) : null
        person.put('ethnicity', ethnicity ? Ethnicity.findByCode(ethnicity?.code) : null)
        person.put('ethnic', ethnicity ? ethnicity.ethnic : null)
        person.put('deadIndicator', person.get('dateDeceased') != null ? 'Y' : null)
        person.put('pidm', newPersonIdentificationName?.pidm)
        person.put('armedServiceMedalVetIndicator', false)
        PersonBasicPersonBase newPersonBase = personBasicPersonBaseService.create(person)
        def currentRecord = new Person(newPersonBase)
        currentRecord.guid = person.guid
        currentRecord.maritalStatusDetail = maritalStatus
        currentRecord.ethnicityDetail = ethnicity
        def name = new Name(newPersonIdentificationName, newPersonBase)
        name.setNameType("Primary")
        currentRecord.names << name

        //Store the credential we already have
        currentRecord.credentials = []
        currentRecord.credentials << new Credential("Banner ID", newPersonIdentificationName.bannerId, null, null)
        if( newPersonBase.ssn ) {
            currentRecord.credentials << new Credential("Social Security Number",
                    newPersonBase.ssn,
                    null,
                    null)
        }
        persons.put(newPersonIdentificationName.pidm, currentRecord)
        def addresses = createAddresses(newPersonIdentificationName.pidm,
                person.addresses instanceof List ? person.addresses : [])
        persons = buildPersonAddresses(addresses, persons )
        def phones = createPhones(newPersonIdentificationName.pidm,
                person.phones instanceof List ? person.phones : [])
        persons = buildPersonTelephones(phones, persons)
        def emails = createEmails(newPersonIdentificationName.pidm,
                person.emails instanceof List ? person.emails : [])
        persons = buildPersonEmails(emails, persons)
        persons.get(newPersonIdentificationName.pidm)
    }

    private void updateGuidValue(def id, def guid) {
        // Update the GUID to the one we received.
        GlobalUniqueIdentifier newEntity = GlobalUniqueIdentifier.findByLdmNameAndDomainId(ldmName, id)
        if( !newEntity ) {
            throw new ApplicationException("Person","@@r1:guid.record.not.found.message:Person@@") // TODO: 404 this.
        }
        if( !newEntity ) {
            throw new ApplicationException("Person","@@r1:guid.not.found.message:Person:BusinessLogicValidationException@@")
        }
        newEntity.guid = guid
        globalUniqueIdentifierService.update(newEntity)
    }

    List<PersonAddress> createAddresses (def pidm, List<Map> newAddresses) {
        def addresses = []
        newAddresses?.each { activeAddress ->
            IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_ADDRESS_TYPE, activeAddress.addressType)
            if (rule.translationValue == activeAddress.addressType && !addresses.contains {
                activeAddress.addressType == rule.value
            }) {
                activeAddress.put('addressType', AddressType.findByCode(rule.value))
                activeAddress.put('state', State.findByDescription(activeAddress.state))
                if (activeAddress?.nation?.containsKey('code')) {
                    activeAddress.put('nation', Nation.findByScodIso(activeAddress?.nation?.code))
                    if (!activeAddress.nation) {
                        log.error "Nation not found for code: ${activeAddress?.nation?.code}"
                        throw new RestfulApiValidationException('PersonCompositeService.country.invalid')
                    }
                }
                if (activeAddress.containsKey('county')) {
                    activeAddress.put('county', County.findByDescription(activeAddress.county))
                    if (!activeAddress.county) {
                        log.error "County not found for code: ${activeAddress.county}"
                        throw new RestfulApiValidationException('PersonCompositeService.county.invalid')
                    }
                }
                activeAddress.put('pidm', pidm)
                validateAddressRequiredFields(activeAddress)
                addresses << personAddressService.create(activeAddress)
            }
        }
        addresses
    }

    def validateAddressRequiredFields(address) {
        if( !address.addressType ) { throw new RestfulApiValidationException('PersonCompositeService.addressType.invalid')}
        if( !address.state ) { throw new RestfulApiValidationException('PersonCompositeService.region.invalid')}
        if( !address.streetLine1 ) { throw new RestfulApiValidationException('PersonCompositeService.streetAddress.invalid')}
        if( !address.city ) { throw new RestfulApiValidationException('PersonCompositeService.city.invalid')}
        if( !address.zip ) { throw new RestfulApiValidationException('PersonCompositeService.postalCode.invalid')}
    }

    def createPhones(def pidm, List<Map> newPhones) {
        def phones = []
        newPhones?.each { activePhone ->
            IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_ADDRESS_TYPE, activePhone.phoneType)
            if (rule.translationValue == activePhone.phoneType &&
                    !phones.contains { activePhone.phoneType == rule.value }) {
                activePhone.put('telephoneType', TelephoneType.findByCode(rule.value) )
                activePhone.put('pidm', pidm)
                validatePhoneRequiredFields(activePhone)
                Map phoneNumber = parsePhoneNumber(activePhone.phoneNumber)
                phoneNumber.keySet().each { key ->
                    activePhone.put(key, phoneNumber.get(key))
                }
                phones << personTelephoneService.create(activePhone)
            }

        }
        phones
    }

    def validatePhoneRequiredFields(phone) {
        if( !phone.telephoneType ) { throw new RestfulApiValidationException('PersonCompositeService.phoneType.invalid')}
        if( !phone.phoneNumber ) { throw new RestfulApiValidationException('PersonCompositeService.phoneNumber.invalid')}
    }

    def createEmails(def pidm, List<Map> newEmails) {
        def emails = []

        newEmails?.each { activeEmail ->
            IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_EMAIL_TYPE, activeEmail.emailType)
            if (rule?.translationValue == activeEmail.emailType &&
                    !emails.contains { activeEmail.emailType == rule?.value }) {
                activeEmail.emailType = EmailType.findByCode(rule.value)
                activeEmail.put('pidm', pidm)
                validateEmailRequiredFields(activeEmail)
                emails << personEmailService.create(activeEmail)
            }
        }
        emails
    }

    def validateEmailRequiredFields(email) {
        if( !email.emailType ) { throw new RestfulApiValidationException('PersonCompositeService.emailType.invalid')}
        if( !email.emailAddress ) { throw new RestfulApiValidationException('PersonCompositeService.emailAddress.invalid')}
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def buildLdmPersonObjects(def pidms) {
        def persons = [:]
        if( pidms.size() < 1 ) {
            return persons
        }
        else if( pidms.size() > 500 ) {
            throw new RestfulApiValidationException('PersonCompositeService.max.results.exceeded')
        }
        List<PersonBasicPersonBase> personBaseList = PersonBasicPersonBase.findAllByPidmInList(pidms)
        List<PersonIdentificationNameCurrent> personIdentificationList = PersonIdentificationNameCurrent.findAllByPidmInList(pidms)
        List<PersonAddress> personAddressList = PersonAddress.fetchActiveAddressesByPidmInList(pidms)
        List<PersonTelephone> personTelephoneList = PersonTelephone.fetchActiveTelephoneByPidmInList(pidms)
        List<PersonEmail> personEmailList = PersonEmail.findAllByStatusIndicatorAndPidmInList('A', pidms)
        personBaseList.each { personBase ->
            Person currentRecord = new Person(personBase)
            currentRecord.maritalStatusDetail = maritalStatusCompositeService.fetchByMaritalStatusCode(personBase.maritalStatus?.code)
            currentRecord.ethnicityDetail = personBase.ethnicity?.code ? ethnicityCompositeService.fetchByEthnicityCode(personBase.ethnicity?.code) : null
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
            currentRecord.metadata = new Metadata(identification.dataOrigin)
            currentRecord.names << name
            currentRecord.credentials << new Credential ("Banner ID", identification.bannerId, null, null)
            persons.put(identification.pidm, currentRecord)
        }
        persons = buildPersonGuids(domainIds, persons)
        persons = buildPersonAddresses(personAddressList, persons)
        persons = buildPersonTelephones(personTelephoneList, persons)
        persons = buildPersonEmails(personEmailList, persons)
        persons // Map of person objects with pidm as index.
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def buildPersonEmails( personEmailList, persons) {
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
        persons
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def buildPersonTelephones( List<PersonTelephone> personTelephoneList, Map persons) {
        personTelephoneList.each { activePhone ->
            Person currentRecord = persons.get(activePhone.pidm)
            IntegrationConfiguration rule = findAllByProcessCodeAndSettingNameAndValue(PROCESS_CODE, PERSON_PHONE_TYPE, activePhone?.telephoneType.code)
            if (rule.value == activePhone?.telephoneType?.code &&
                    !(currentRecord.phones.contains { it.phoneType == rule.translationValue })) {
                def phone = new Phone(activePhone)
                phone.phoneType = rule.translationValue
                currentRecord.phones << phone
            }
            persons.put(activePhone.pidm, currentRecord)
        }
        persons
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def buildPersonAddresses( List<PersonAddress> personAddressList, Map persons) {
        personAddressList.each { activeAddress ->
            Person currentRecord = persons.get(activeAddress.pidm)
            IntegrationConfiguration rule = findAllByProcessCodeAndSettingNameAndValue(PROCESS_CODE, PERSON_ADDRESS_TYPE, activeAddress.addressType.code)
            if (rule.value == activeAddress.addressType?.code &&
                    !currentRecord.addresses.contains { it.addressType == rule.translationValue }) {
                def address = new Address(activeAddress)
                address.addressType = rule.translationValue
                currentRecord.addresses << address
            }
            persons.put(activeAddress.pidm, currentRecord)
        }
        persons
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def buildPersonGuids(List domainIds, Map persons) {
        GlobalUniqueIdentifier.findAllByLdmNameAndDomainIdInList(ldmName, domainIds).each { guid ->
            Person currentRecord = persons.get(guid.domainKey.toInteger())
            currentRecord.guid = guid.guid
            persons.put(guid.domainKey.toInteger(), currentRecord)
        }
        persons
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


    private  updateAddresses(def pidm, List<PersonAddress> newAddresses) {
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
                        log.error "Nation not found for code: ${activeAddress?.country?.code}"
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

    private  updatePhones(def pidm, List<PersonTelephone> newPhones) {
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


    private updateEmails(def pidm, List<PersonEmail> newEmails) {
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

    Map parsePhoneNumber(String phoneNumber) {
        Map parsedNumber = [:]
        List<InstitutionalDescription> institutions = InstitutionalDescription.list()
        def institution = institutions.size() > 0 ? institutions[0]: null
        IntegrationConfiguration countryLdmCode
        if( institution?.natnCode ) {
            countryLdmCode =
                    IntegrationConfiguration.fetchByProcessCodeAndSettingNameAndValue(PROCESS_CODE, PERSON_ADDRESS_NATION, institution.natnCode)
        }
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance()
        Phonenumber.PhoneNumber parsedResult = phoneUtil.parse(phoneNumber, countryLdmCode?.translationValue ?: 'US')
        if ( phoneUtil.isNANPACountry(countryLdmCode?.translationValue ?: 'US') ) {
            String nationalNumber = parsedResult.getNationalNumber()
            if( nationalNumber.length() == 10 ) {
                parsedNumber.put('phoneArea', nationalNumber[0..2])
                parsedNumber.put('phoneNumber', nationalNumber[3..-1])
            }
            else {
                parsedNumber.put('phoneNumber', nationalNumber)
            }

        }
        else {
            parsedNumber.put('internationalAccess', parsedResult.getCountryCode() + parsedResult.getNationalNumber())
        }
        if( parsedResult.getExtension() ) {
            parsedNumber.put('phoneExtension', parsedResult.getExtension())
        }
        parsedNumber
    }

}
