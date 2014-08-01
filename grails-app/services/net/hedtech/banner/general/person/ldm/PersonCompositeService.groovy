/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

import net.hedtech.banner.exceptions.ApplicationException
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
import org.springframework.transaction.annotation.Transactional

@Transactional
class PersonCompositeService extends LdmService {

    def personIdentificationNameCurrentService
    def personBasicPersonBaseService
    def personAddressService
    def personTelephoneService
    def personEmailService
    def globalUniqueIdentifierService
    def static ldmName = 'persons'

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    def get(id) {
        def entity = globalUniqueIdentifierService.fetchByLdmNameAndGuid(ldmName, id)
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
        // Handle when last name required field is null using firstName required field.
        if ( newPersonIdentification.get('lastName') == null ) {
            newPersonIdentification.put('lastName', newPersonIdentification.get('firstName'))
        }
        person?.credentials?.each { it ->
            if( it.credentialType == 'Person Id' ) {
                newPersonIdentification.put('bannerId',it.credentialId)
            }
            // TODO: Create other credential types (not PIDM!) after saving of PersonIdentificationNameCurrent?
        }
        //Create the new PersonIdentification record
        PersonIdentificationNameCurrent newPersonIdentificationName =
                personIdentificationNameCurrentService.create(newPersonIdentification)
        //Fix the GUID if provided as DB will assign one
        if( person.guid ) {
            updateGuidValue(newPersonIdentificationName.id, person.guid)
        }
        else {
            def entity = globalUniqueIdentifierService.fetchByLdmNameAndDomainId('person', newPersonIdentificationName.id)
            person.put('guid', entity)
        }

        //Copy personBase attributes into person map from Primary names object.
        person.put('namePrefix', newPersonIdentification.get('namePrefix'))
        person.put('nameSuffix', newPersonIdentification.get('nameSuffix'))
        person.put('preferenceFirstName', newPersonIdentification.get('preferenceFirstName'))
        //Translate enumerations and defaults
        person.put('sex', person?.sex == 'Male' ? 'M':(person?.sex == 'Female' ? 'F' : 'N'))
        person.put('maritalStatus', getBannerMaritalStatus(person.maritalStatus))
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
        credentials << new Credential("Person ID",
                newPersonIdentificationName.bannerId,
                null,
                null)
        // TODO: Add other credentials?  (Not PIDM!)
        def addresses = createAddresses(newPersonIdentificationName.pidm,
                person.addresses instanceof List ? person.addresses : [])
        def phones = createPhones(newPersonIdentificationName.pidm,
                person.phones instanceof List ? person.phones : [])
        def emails = createEmails(newPersonIdentificationName.pidm,
                person.emails instanceof List ? person.emails : [])
        //Build decorator to return LDM response.
        def newPerson = new Person( newPersonBase, person.guid, credentials, addresses, phones, emails, names)
        newPerson.setMaritalStatus(getLdmMaritalStatus(newPersonBase.maritalStatus))
        newPerson
    }

    // TODO: validate guid format.
    private void updateGuidValue(def id, def guid) {
        // Update the GUID to the one we received.
        GlobalUniqueIdentifier newEntity = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId('persons', id)
        if( !newEntity ) {
            throw new ApplicationException("Person","@@r1:guid.record.not.found.message:Person@@")
        }
        if( !newEntity ) {
            throw new ApplicationException("Person","@@r1:guid.not.found.message:Person:BusinessLogicValidationException@@")
        }
        newEntity.guid = guid
        globalUniqueIdentifierService.update(newEntity)
    }

    def createAddresses (def pidm, List newAddresses) {
        def addresses = []
        def addressRules = rules.grep {
            it.settingName?.equals('person.addresses.addressType')
        }
        PersonCompositeService.log.debug "Address mapping rules: ${addressRules.toString()}"
        if( addressRules.size() > 0 ) {
            newAddresses?.each { activeAddress ->
                addressRules?.each { rule ->
                    if (rule.translationValue == activeAddress.addressType &&
                            !addresses.contains { activeAddress.addressType == rule.value }) {
                        activeAddress.addressType = AddressType.findByCode(rule.value)
                        activeAddress.state = State.findByDescription(activeAddress.state) // TODO: This may change
                        activeAddress.nation = Nation.findByNation(activeAddress.nation) // TODO: This has changed.
                        activeAddress.county = County.findByDescription(activeAddress.county)
                        activeAddress.put('pidm', pidm)
                        def address = personAddressService.create(activeAddress)
                        def addressDecorator = new Address(address)
                        addressDecorator.addressType = rule.translationValue
                        addresses << addressDecorator
                    }

                }
            }
        }
        addresses
    }

    def createPhones(def pidm, List newPhones) {
        def phones = []
        def phoneRules = rules.grep {
            it.settingName?.equals('person.phones.phoneType')
        }
        PersonCompositeService.log.debug "Phone mapping rules: ${phoneRules.toString()}"
        if( phoneRules.size() > 0 ) {
            newPhones?.each { activePhone ->
                phoneRules?.each { rule ->
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
            }
        }
        phones
    }

    def createEmails(def pidm, List newEmails) {
        def emails = []
        def emailRules = rules.grep {
            it.settingName?.equals('person.emails.emailType')
        }
        PersonCompositeService.log.debug "Email mapping rules: ${emailRules.toString()}"
        if( emailRules.size() > 0 ) {
            newEmails?.each { activeEmail ->
                emailRules?.each { rule ->
                    if (rule.translationValue == activeEmail.emailType &&
                            !emails.contains { activeEmail.emailType == rule.value }) {
                        activeEmail.emailType = EmailType.findByCode(rule.value)
                        activeEmail.put('pidm', pidm)
                        def email = personEmailService.create(activeEmail)
                        def emailDecorator = new Email(email)
                        emailDecorator.emailType = rule.translationValue
                        emails << emailDecorator
                    }
                }
            }
        }
        emails
    }

    // Return LDM enumeration value for this marital status code.
    def getLdmMaritalStatus(def maritalStatus) {
        if( maritalStatus != null  ) {
            switch (maritalStatus) {
                case "S":
                    return "Single"
                case "M":
                    return "Married"
                case "D":
                    return "Divorced"
                case "W":
                    return "Widowed"
                case "P":
                    return "Separated"
                case "R":
                    return "Married"
                default:
                    PersonCompositeService.log.warn "Banner marital status code ${maritalStatus} not found."
            }
        }
        return null
    }

    // Return validation record for this marital status.
    def getBannerMaritalStatus(def maritalStatus) {
        if( maritalStatus != null ) {
            def validationRecord
            switch (maritalStatus) {
                case "Single":
                    validationRecord = MaritalStatus.findByCode("S")
                    break
                case "Married":
                    validationRecord = MaritalStatus.findByCode("M")
                    break
                case "Divorced":
                    validationRecord = MaritalStatus.findByCode("D")
                    break
                case "Widowed":
                    validationRecord = MaritalStatus.findByCode("W")
                    break
                case "Separated":
                    validationRecord = MaritalStatus.findByCode("P")
                    break
                default:
                    throw new ApplicationException("Person", "@@r1:maritalStatus.code.invalid:BusinessLogicValidationException@@")
                    break

            }
            if( validationRecord == null ) {
                throw new ApplicationException("Person", "@@r1:maritalStatus.code.missing:BusinessLogicValidationException@@")
            }
            return validationRecord
        }
        return null
    }

    @Transactional(readOnly = true)
    def buildLdmPersonObjects(def pidms) {
        def persons = [:]
        List<PersonBasicPersonBase> personBaseList = PersonBasicPersonBase.findAllByPidmInList(pidms)
        List<PersonIdentificationNameCurrent> personIdentificationList = PersonIdentificationNameCurrent.findAllByPidmInList(pidms)
        List<PersonAddress> personAddressList = PersonAddress.fetchActiveAddressesByPidmInList(pidms)
        List<PersonTelephone> personTelephoneList = PersonTelephone.fetchActiveTelephoneByPidmInList(pidms)
        List<PersonEmail> personEmailList = PersonEmail.findAllByStatusIndicatorAndPidmInList('A', pidms)
        personBaseList.each { personBase ->
            Person currentRecord = new Person(personBase)
            currentRecord.setMaritalStatus(getLdmMaritalStatus(personBase?.maritalStatus?.code))
            persons.put(currentRecord.pidm, currentRecord)
        }
        def domainIds = []
        personIdentificationList.each { identification ->
            Person currentRecord = persons.get(identification.pidm) ?: new Person(null)
            def name = new Name(identification, currentRecord)
            name.setNameType("Primary")
            domainIds << identification.id
            currentRecord.names << name
            currentRecord.credentials << new Credential("Person ID",
                    identification.bannerId,
                    null,
                    null)
            persons.put(identification.pidm, currentRecord)
        }
        GlobalUniqueIdentifier.findAllByLdmNameAndDomainIdInList(ldmName, domainIds).each { guid ->
            Person currentRecord = persons.get(guid.domainKey.toInteger())
            currentRecord.guid = guid.guid
            persons.put(guid.domainKey.toInteger(), currentRecord)
        }
        def addressRules = rules.grep {
            it.settingName?.equals('person.addresses.addressType')
        }
        PersonCompositeService.log.debug "Address mapping rules: ${addressRules.toString()}"
        personAddressList.each { activeAddress ->
            Person currentRecord = persons.get(activeAddress.pidm)
            addressRules?.each { rule ->
                if (rule.value == activeAddress.addressType?.code &&
                        !currentRecord.addresses.contains { it.addressType == rule.translationValue }) {
                    def address = new Address(activeAddress)
                    address.addressType = rule.translationValue
                    currentRecord.addresses << address
                }

            }
        }
        def phoneRules = rules.grep {
            it.settingName?.equals('person.phones.phoneType')
        }
        PersonCompositeService.log.debug "Phone mapping rules: ${phoneRules.toString()}"
        personTelephoneList.each { activePhone ->
            Person currentRecord = persons.get(activePhone.pidm)
            phoneRules?.each { rule ->
                if (rule.value == activePhone?.telephoneType?.code &&
                        !(currentRecord.phones.contains { it.phoneType == rule.translationValue })) {
                    def phone = new Phone(activePhone)
                    phone.phoneType = rule.translationValue
                    currentRecord.phones << phone
                }
            }
            persons.put(activePhone.pidm, currentRecord)
        }

        def emailRules = rules.grep {
            it.settingName?.equals('person.emails.emailType')
        }
        PersonCompositeService.log.debug "Email mapping rules: ${emailRules.toString()}"
        personEmailList.each { PersonEmail activeEmail ->
            Person currentRecord = persons.get(activeEmail.pidm)
            emailRules?.each { rule ->
                if (rule.value == activeEmail?.emailType?.code &&
                        !currentRecord.emails.contains { it.emailType == rule.translationValue }) {
                    def email = new Email(activeEmail)
                    email.emailType = rule.translationValue
                    currentRecord.emails << email
                }
            }
            persons.put(activeEmail.pidm, currentRecord)
        }
        persons // Map of person objects with pidm as index.
    }
}