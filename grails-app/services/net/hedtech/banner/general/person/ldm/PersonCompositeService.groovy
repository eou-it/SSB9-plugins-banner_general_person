/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.person.PersonAddress
import net.hedtech.banner.general.person.PersonBasicPersonBase
import net.hedtech.banner.general.person.PersonEmail
import net.hedtech.banner.general.person.PersonIdentificationNameCurrent
import net.hedtech.banner.general.person.PersonRace
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
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.apache.log4j.Logger
import org.springframework.transaction.annotation.Propagation
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.transaction.annotation.Transactional
import java.sql.CallableStatement
import java.sql.SQLException

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
    def raceCompositeService
    def personRaceService
    def userRoleCompositeService

    def static ldmName = 'persons'
    static final String PERSON_ADDRESS_TYPE = "person.addresses.addressType"
    static final String PERSON_PHONE_TYPE = "person.phones.phoneType"
    static final String PERSON_EMAIL_TYPE = "person.emails.emailType"
    static final String PROCESS_CODE = "LDM"
    static final String PERSON_MATCH_RULE = "persons.matchRule"


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def list(params) {
        def pidms = []
        def resultList

        def sortAndPagingParams = [:]
        def allowedSortFields = ["firstName", "lastName"]
        if( params.containsKey('max') ) sortAndPagingParams.put('max',params.max)
        if( params.containsKey('offset') ) sortAndPagingParams.put('offset',params.offset)
        if( params.containsKey('sort') ) sortAndPagingParams.put( 'sort', params.sort )
        if( params.containsKey('order') ) sortAndPagingParams.put( 'order', params.order )
        RestfulApiValidationUtility.correctMaxAndOffset(sortAndPagingParams, 500, 0)

        if (params.sort) {
            RestfulApiValidationUtility.validateSortField(sortAndPagingParams.sort, allowedSortFields)
        } else {
            sortAndPagingParams.put( 'sort', allowedSortFields[1] )
        }

        if (sortAndPagingParams.order) {
            RestfulApiValidationUtility.validateSortOrder(sortAndPagingParams.order)
        } else {
            sortAndPagingParams.put( 'order', "asc" )
        }

        // Check if it is qapi request, if so do matching
        if (RestfulApiValidationUtility.isQApiRequest( params )) {
            log.info "Person Duplicate service:"
            log.debug "Request parameters: ${params}"

            def primaryName = params.names.find {primaryNameType ->
                primaryNameType.nameType == "Primary"
            }

            if (primaryName?.firstName && primaryName?.lastName) {
                pidms = searchPerson( params )
            } else {
                throw new ApplicationException( "Person", "@@r1:missing.first.last.name:BusinessLogicValidationException@@" )
            }
        } else {
            if( params.role ) {
                def roles = userRoleCompositeService.fetchAllByRole(params.role)
                roles.each { key, value ->
                    pidms << key
                }
            }
        }
        if( pidms.size() > 1000 ) {
            throw new RestfulApiValidationException('PersonCompositeService.max.results.exceeded')
        }
        List<PersonIdentificationNameCurrent> personIdentificationList =
                PersonIdentificationNameCurrent.findAllByPidmInList(pidms, sortAndPagingParams)
        def persons = buildLdmPersonObjects( personIdentificationList )

        /*sorting start
        if(persons?.size() > 0) {
            if (sortAndPagingParams.sort == "firstName") {
                if (sortAndPagingParams.order == "desc") {
                    log.trace "Persons - sort: firstName - order: desc"
                    persons = persons.sort {a, b -> b.value.names[0].firstName<=> a.value.names[0].firstName}
                } else {
                    log.trace "Persons - sort: firstName - order: asc"
                    persons = persons.sort {it.value.names[0].firstName}
                }

            } else if (sortAndPagingParams.sort == "lastName") {
                if (sortAndPagingParams.order == "desc") {
                    log.trace "Persons - sort: lastName - order: desc"
                    persons = persons.sort {a, b -> b.value.names[0].lastName <=> a.value.names[0].lastName}
                } else {
                    log.trace "Persons - sort: lastName - order: asc"
                    persons = persons.sort {it.value.names[0].lastName}
                }
            }
        }
        //sorting end */

        try {  // Avoid restful-api plugin dependencies.
            resultList = this.class.classLoader.loadClass( 'net.hedtech.restfulapi.PagedResultArrayList' ).newInstance( persons.values(), pidms?.size() )
        }
        catch (ClassNotFoundException e) {
            resultList = persons.values()
        }
        resultList
    }


    private List<Integer> searchPerson( Map params ) {

        def ctx = ServletContextHolder.servletContext.getAttribute( GrailsApplicationAttributes.APPLICATION_CONTEXT )
        def sessionFactory = ctx.sessionFactory

        List<Integer> personList = []
        IntegrationConfiguration personMatchRule = IntegrationConfiguration.findByProcessCodeAndSettingName( PROCESS_CODE, PERSON_MATCH_RULE )

        def primaryName = params.names.find {primaryNameType ->
            primaryNameType.nameType == "Primary"
        }
        def ssnCredentials = params.credentials.find {credential ->
            credential.credentialType == "Social Security Number"
        }
        def bannerIdCredentials = params.credentials.find {credential ->
            credential.credentialType == "Banner ID"
        }
        def emailInstitution = params.emails.find {email ->
            email.emailType == "Institution"
        }
        def emailInstitutionRuleValue
        if(emailInstitution?.emailType){
            emailInstitutionRuleValue = IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_EMAIL_TYPE, emailInstitution['emailType'])[0]?.value
        }

        def emailPersonal = params.emails.find {email ->
            email.emailType == "Personal"
        }
        def emailPersonalRuleValue
        if(emailPersonal?.emailType){
            emailPersonalRuleValue = IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_EMAIL_TYPE, emailPersonal['emailType'])[0]?.value
        }

        def emailWork = params.emails.find {email ->
            email.emailType == "Work"
        }
        def emailWorkRuleValue
        if(emailWork?.emailType){
            emailWorkRuleValue = IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_EMAIL_TYPE, emailWork['emailType'])[0]?.value
        }

        CallableStatement sqlCall
        try {
            def connection = sessionFactory.currentSession.connection()
            String matchPersonQuery = "{ call spkcmth.p_common_mtch(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }"
            sqlCall = connection.prepareCall( matchPersonQuery )

            sqlCall.setString( 1, personMatchRule?.value )
            sqlCall.setString( 2, primaryName?.firstName )
            sqlCall.setString( 3, primaryName?.lastName )
            sqlCall.setString( 4, primaryName?.middleName )
            sqlCall.setString( 5, params?.dateOfBirth )
            sqlCall.setString( 6, params?.gender )
            sqlCall.setString( 7, ssnCredentials?.credentialType )
            sqlCall.setString( 8, ssnCredentials?.credentialId )
            sqlCall.setString( 9, bannerIdCredentials?.credentialType?: null )
            sqlCall.setString( 10, bannerIdCredentials?.credentialId?: null )
            sqlCall.setString( 11, emailInstitutionRuleValue?: null )
            sqlCall.setString( 12, emailInstitution?.emailAddress )
            sqlCall.setString( 13, emailPersonalRuleValue?: null )
            sqlCall.setString( 14, emailPersonal?.emailAddress )
            sqlCall.setString( 15, emailWorkRuleValue?: null )
            sqlCall.setString( 16, emailWork?.emailAddress )

            sqlCall.registerOutParameter( 17, java.sql.Types.VARCHAR )
            sqlCall.executeQuery()

            String errorCode = sqlCall.getString( 17 )
            if (!errorCode) {
                personList = getCommonMatchingResults()
            } else {
                throw new ApplicationException( this.class.name, errorCode )
            }
        }
        catch (SQLException sqlEx) {
            log.error "Error executing spkcmth.p_common_mtch: " + sqlEx.stackTrace
            throw new ApplicationException(this.class.name, sqlEx)
        }
        catch (Exception ex) {
            log.error "Exception while searching person ${ex}" + ex.stackTrace
            throw new ApplicationException(this.class.name, ex)
        }
        finally {
            try {
                sqlCall?.close()
            } catch (SQLException sqlEx) {
                log.trace "Sql Statement is already closed, no need to close it."
            }
        }

        return personList
    }


    private def getCommonMatchingResults(){
        List<Integer> personPidmList = []
        def ctx = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        def log = Logger.getLogger(this.getClass())
        def sessionFactory = ctx.sessionFactory
        def session = sessionFactory.currentSession
        def sql
        try {
            sql = new Sql(session.connection())
            def commonMatchSql = """SELECT govcmrt_pidm pidm, govcmrt_id
                                      FROM govcmrt
                                     WHERE govcmrt_result_ind = 'M'"""

            sql.eachRow(commonMatchSql) { commonMatchPerson ->
                personPidmList << commonMatchPerson.pidm.intValue()
            }
        }
        catch (SQLException ae) {
            log.error "SqlException while fetching person details from govcmrt ${ae}"
            throw ae
        }
        catch (Exception ae) {
            log.error "Exception while fetching person details ${ae} "
            throw ae
        }
        finally {
            sql?.close()
        }
        return personPidmList
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def get(id) {
        def entity = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(ldmName, id)
        if( !entity ) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Person.class.simpleName))
        }
        List<PersonIdentificationNameCurrent> personIdentificationList =
                PersonIdentificationNameCurrent.findAllByPidmInList([entity.domainKey?.toInteger()])
        def resultList = buildLdmPersonObjects(personIdentificationList)
        resultList.get(entity.domainKey?.toInteger())
    }

    def create(Map person) {
        Map<Integer,Person> persons = [:]
        def newPersonIdentification
        if( person.names instanceof List) {
            person?.names?.each { it ->
                if( it instanceof Map ) {
                    if (it.nameType == 'Primary') {
                        newPersonIdentification = it
                    }
                }
            }
        }
        newPersonIdentification.put('bannerId','GENERATED')
        newPersonIdentification.put('entityIndicator', 'P')
        newPersonIdentification.put('changeIndicator', null)
        newPersonIdentification.put('dataOrigin', person?.metadata?.dataOrigin)
        newPersonIdentification.remove('nameType') // ID won't generate if this is set.
        //Create the new PersonIdentification record
        PersonIdentificationNameCurrent newPersonIdentificationName =
                personIdentificationNameCurrentService.create(newPersonIdentification)
        //Fix the GUID if provided as DB will assign one
        if( person.guid ) {
            updateGuidValue(newPersonIdentificationName.id, person.guid)

        }
        else {
            def entity = GlobalUniqueIdentifier.findByLdmNameAndDomainId(ldmName, newPersonIdentificationName.id)
            person.put('guid', entity?.guid)
        }

        //Copy ssn attribute from credential to person map.
        if( person?.credentials instanceof List) {
            person?.credentials?.each { it ->
                if( it instanceof Map ) {
                    if (it.credentialType == 'Social Security Number') {
                        if( it?.credentialId.length() > 9) {throw new ApplicationException("PersonCompositeService",
                                "@@r1:credentialId.invalid:BusinessLogicValidationException@@")}
                        person.put('ssn', it?.credentialId)
                    } else if (it.credentialType == 'Social Insurance Number') {
                        if( it?.credentialId.length() > 9) {throw new ApplicationException("PersonCompositeService",
                                "@@r1:credentialId.invalid:BusinessLogicValidationException@@")}
                        person.put('ssn', it?.credentialId)
                    }
                }
            }
        }
        //Copy personBase attributes into person map from Primary names object.
        person.put('dataOrigin', person?.metadata?.dataOrigin)
        person.put('namePrefix', newPersonIdentification.get('namePrefix'))
        person.put('nameSuffix', newPersonIdentification.get('nameSuffix'))
        person.put('preferenceFirstName', newPersonIdentification.get('preferenceFirstName'))
        //Translate enumerations and defaults
        person.put('sex', person?.sex == 'Male' ? 'M':(person?.sex == 'Female' ? 'F' :(person?.sex == 'Unknown' ? 'N' : null)))
        def maritalStatus
        try {
            maritalStatus = person.maritalStatusDetail instanceof Map && person.maritalStatusDetail?.guid ? maritalStatusCompositeService.get(person.maritalStatusDetail?.guid) : null
        }catch (ApplicationException e) {
                throw new ApplicationException("PersonCompositeService", "@@r1:maritalStatus.invalid:BusinessLogicValidationException@@")
        }
        person.put('maritalStatus', maritalStatus ? MaritalStatus.findByCode(maritalStatus?.code) : null)
        def ethnicity
        try {
            if( person.ethnicityDetail instanceof Map ) {
                ethnicity = person.ethnicityDetail?.guid ? ethnicityCompositeService.get(person.ethnicityDetail?.guid) : null
            }
        }
        catch (ApplicationException e) {
            if( e.wrappedException instanceof NotFoundException ) {
                throw new ApplicationException("PersonCompositeService", "@@r1:ethnicity.invalid:BusinessLogicValidationException@@")
                ethnicity = null
            }
            else {
                throw e
            }
        }
        person.put('ethnicity', ethnicity ? Ethnicity.findByCode(ethnicity?.code) : null)
        person.put('ethnic', ethnicity ? ethnicity.ethnic : null)
        person.put('deadIndicator', person.get('deadDate') ? 'Y' : null)
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
        def addresses = createAddresses(newPersonIdentificationName.pidm, person?.metadata,
                person.addresses instanceof List ? person.addresses : [])
        persons = buildPersonAddresses(addresses, persons )
        def phones = createPhones(newPersonIdentificationName.pidm, person?.metadata,
                person.phones instanceof List ? person.phones : [])
        persons = buildPersonTelephones(phones, persons)
        def emails = createEmails(newPersonIdentificationName.pidm, person?.metadata,
                person.emails instanceof List ? person.emails : [])
        persons = buildPersonEmails(emails, persons)
        def races = createRaces(newPersonIdentificationName.pidm, person?.metadata,
                person.races instanceof List ? person.races : [] )
        persons = buildPersonRaces(races, persons)
        persons = buildPersonRoles(persons)
        persons.get(newPersonIdentificationName.pidm)
    }

    private void updateGuidValue(def id, def guid) {
        // Update the GUID to the one we received.
        GlobalUniqueIdentifier newEntity = GlobalUniqueIdentifier.findByLdmNameAndDomainId(ldmName, id)
        if( !newEntity ) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Person.class.simpleName))
        }
        if( !newEntity ) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Person.class.simpleName))
        }
        newEntity.guid = guid
        globalUniqueIdentifierService.update(newEntity)
    }

    List<PersonAddress> createAddresses (def pidm, Map metadata, List<Map> newAddresses) {
        def addresses = []
        newAddresses?.each { activeAddress ->
            if(activeAddress instanceof Map) {
                IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_ADDRESS_TYPE, activeAddress.addressType)
                if (rule?.translationValue == activeAddress.addressType && !addresses.contains {
                    activeAddress.addressType == rule?.value
                }) {
                    activeAddress.put('addressType', AddressType.findByCode(rule?.value))
                    activeAddress.put('state', State.findByDescription(activeAddress.state))
                    if (activeAddress?.nation?.containsKey('code')) {
                        Nation nation = Nation.findByScodIso(activeAddress?.nation?.code)
                        if (nation) {
                            activeAddress.put('nation', nation)
                        } else {
                            log.error "Nation not found for code: ${activeAddress?.nation?.code}"
                            throw new RestfulApiValidationException('PersonCompositeService.country.invalid')
                        }
                    }
                    if (activeAddress.containsKey('county')) {
                        County country = County.findByDescription(activeAddress.county)
                        if (country) {
                            activeAddress.put('county', country)
                        } else {
                            log.error "County not found for code: ${activeAddress.county}"
                            throw new RestfulApiValidationException('PersonCompositeService.county.invalid')
                        }
                    }
                    activeAddress.put('pidm', pidm)
                    activeAddress.put('dataOrigin', metadata?.dataOrigin)
                    validateAddressRequiredFields(activeAddress)
                    addresses << personAddressService.create(activeAddress)
                }
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

    List<PersonRace> createRaces (def pidm, Map metadata, List<Map> newRaces) {
        def races = []
        newRaces?.each { activeRace ->
            if( activeRace instanceof Map && activeRace?.guid ) {
                def race
                try {
                    race = raceCompositeService.get(activeRace?.guid)
                }
                catch (ApplicationException e) {
                    if( e.wrappedException instanceof NotFoundException ) {
                        throw new ApplicationException("PersonCompositeService", "@@r1:race.invalid:BusinessLogicValidationException@@")
                    }
                    else {
                        throw e
                    }
                }
                def newRace = new PersonRace()
                newRace.pidm = pidm
                newRace.race = race.race
                newRace.dataOrigin = metadata?.dataOrigin
                races << personRaceService.create(newRace)
            }
            else {
                throw new ApplicationException('PersonCompositeService', "@@r1:race.invalid:BusinessLogicValidationException@@")
            }
        }
        races
    }



    def createPhones(def pidm, Map metadata, List<Map> newPhones) {
        def phones = []
        newPhones?.each { activePhone ->
            if( activePhone instanceof Map ) {
                IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_PHONE_TYPE, activePhone.phoneType)
                if (!rule) {
                    log.error activePhone.toString()
                }
                if (rule?.translationValue == activePhone.phoneType &&
                        !phones.contains { activePhone.phoneType == rule?.value }) {
                    activePhone.put('telephoneType', TelephoneType.findByCode(rule?.value))
                    activePhone.put('pidm', pidm)
                    activePhone.put('dataOrigin', metadata?.dataOrigin)
                    validatePhoneRequiredFields(activePhone)
                    Map phoneNumber = parsePhoneNumber(activePhone.phoneNumber)
                    phoneNumber.keySet().each { key ->
                        activePhone.put(key, phoneNumber.get(key))
                    }
                    phones << personTelephoneService.create(activePhone)
                }
            }
        }
        phones
    }

    def validatePhoneRequiredFields(phone) {
        if( !phone.telephoneType ) { throw new RestfulApiValidationException('PersonCompositeService.phoneType.invalid')}
        if( !phone.phoneNumber ) { throw new RestfulApiValidationException('PersonCompositeService.phoneNumber.invalid')}
    }

    def createEmails(def pidm, Map metadata, List<Map> newEmails) {
        def emails = []
        newEmails?.each { activeEmail ->
            if( activeEmail instanceof Map) {
                IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_EMAIL_TYPE, activeEmail.emailType)
                if (rule?.translationValue == activeEmail.emailType &&
                        !emails.contains { activeEmail.emailType == rule?.value }) {
                    activeEmail.emailType = EmailType.findByCode(rule?.value)
                    activeEmail.put('pidm', pidm)
                    activeEmail.put('dataOrigin', metadata?.dataOrigin)
                    validateEmailRequiredFields(activeEmail)
                    emails << personEmailService.create(activeEmail)
                }
            }
        }
        emails
    }

    def validateEmailRequiredFields(email) {
        if( !email.emailType ) { throw new RestfulApiValidationException('PersonCompositeService.emailType.invalid')}
        if( !email.emailAddress ) { throw new RestfulApiValidationException('PersonCompositeService.emailAddress.invalid')}
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def buildLdmPersonObjects(List<PersonIdentificationNameCurrent> personIdentificationList) {
        def persons = [:]
        def pidms = []
        personIdentificationList.each { personIdentification ->
            pidms << personIdentification.pidm
            persons.put(personIdentification.pidm, null) //Preserve list order.
        }
        if( pidms.size() < 1 ) {
            return persons
        }
        else if( pidms.size() > 1000 ) {
            throw new RestfulApiValidationException('PersonCompositeService.max.results.exceeded')
        }
        List<PersonBasicPersonBase> personBaseList = PersonBasicPersonBase.findAllByPidmInList(pidms)
        List<PersonAddress> personAddressList = PersonAddress.fetchActiveAddressesByPidmInList(pidms)
        List<PersonTelephone> personTelephoneList = PersonTelephone.fetchActiveTelephoneByPidmInList(pidms)
        List<PersonEmail> personEmailList = PersonEmail.findAllByStatusIndicatorAndPidmInList('A', pidms)
        List<PersonRace> personRaceList = PersonRace.findAllByPidmInList(pidms)
        personBaseList.each { personBase ->
            Person currentRecord = new Person(personBase)
            currentRecord.maritalStatusDetail = maritalStatusCompositeService.fetchByMaritalStatusCode(personBase.maritalStatus?.code)
            currentRecord.ethnicityDetail = personBase.ethnicity?.code ? ethnicityCompositeService.fetchByEthnicityCode(personBase.ethnicity?.code) : null
          /*  if( personBase.ssn ) {
                currentRecord.credentials << new Credential("Social Security Number",
                        personBase.ssn,
                        null,
                        null)
            } Not spitting out SSNs at this time*/

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
        persons = buildPersonRaces(personRaceList,persons)
        persons = buildPersonRoles(persons)
        persons // Map of person objects with pidm as index.
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def buildPersonEmails( personEmailList, persons) {
        personEmailList.each { PersonEmail activeEmail ->
            Person currentRecord = persons.get(activeEmail.pidm)
            IntegrationConfiguration rule = findAllByProcessCodeAndSettingNameAndValue(PROCESS_CODE, PERSON_EMAIL_TYPE,activeEmail?.emailType.code)
            if (rule?.value == activeEmail?.emailType?.code &&
                    !currentRecord.emails.contains { it.emailType == rule?.translationValue }) {
                def email = new Email(activeEmail)
                email.emailType = rule?.translationValue
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
            if (rule?.value == activePhone?.telephoneType?.code &&
                    !(currentRecord.phones.contains { it.phoneType == rule?.translationValue })) {
                def phone = new Phone(activePhone)
                phone.phoneType = rule?.translationValue
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
            if (rule?.value == activeAddress.addressType?.code &&
                    !currentRecord.addresses.contains { it.addressType == rule?.translationValue }) {
                def address = new Address(activeAddress)
                address.addressType = rule?.translationValue
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

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def buildPersonRaces( List<PersonRace> personRacesList, Map persons) {
        personRacesList.each { activeRace ->
            Person currentRecord = persons.get(activeRace.pidm)
            def race = raceCompositeService.fetchByRaceCode(activeRace.race)
            race.dataOrigin = activeRace.dataOrigin
            currentRecord.races << race
            persons.put(activeRace.pidm, currentRecord)
        }
        persons
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def buildPersonRoles( Map persons) {
        def pidms = []
        persons.each { key, value ->
            pidms << key
        }
        userRoleCompositeService.fetchAllRolesByPidmInList(pidms).each { role ->
            Person currentRecord = persons.get(role.key)
            currentRecord.roles = role.value
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
    def update(Map content) {

        def primaryName
        content?.names?.each { it ->
            if (it.nameType == 'Primary') {
                primaryName = it
            }
        }
        def entity = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(ldmName, content.guid)
        if (!entity) {
            throw new ApplicationException("Person", "@@r1:guid.record.not.found.message:Person@@")
        }
        def pidmToUpdate = entity.domainKey?.toInteger()
        List<PersonIdentificationNameCurrent> personIdentificationList = PersonIdentificationNameCurrent.findAllByPidmInList([pidmToUpdate])

        def personIdentification
        personIdentificationList.each { identification ->
            if (identification.changeIndicator == null) {
                personIdentification = identification
            }
        }
        //update PersonIdentificationNameCurrent
        PersonIdentificationNameCurrent newPersonIdentificationName
        if(primaryName) {
            personIdentification.firstName = primaryName.firstName
            personIdentification.lastName = primaryName.lastName
            personIdentification.middleName = primaryName.middleName
            newPersonIdentificationName = personIdentificationNameCurrentService.update(personIdentification)

        }
        def credentials

        content?.credentials?.each { it ->
            if (it.credentialType == 'Banner ID') {
                personIdentification.bannerId  = it?.credentialId
                newPersonIdentificationName = personIdentificationNameCurrentService.update(personIdentification)
            }
        }


        //update PersonBasicPersonBase
        PersonBasicPersonBase newPersonBase = updatePersonBasicPersonBase(pidmToUpdate, newPersonIdentificationName, content, primaryName)
        def names = []
        def name = new Name(newPersonIdentificationName, newPersonBase)
        if(primaryName)
            name.setNameType("Primary")
        names << name


        def ethnicity = content.ethnicity?.guid ? ethnicityCompositeService.get(content.ethnicity?.guid) : null

        //update Address
         def addresses

        if (content.containsKey('addresses') && content.addresses instanceof List && content.addresses.size > 0)
            addresses = updateAddresses(pidmToUpdate, content.metadata, content.addresses)

        //update Telephones
        def phones
        if (content.containsKey('phones') && content.phones instanceof List && content.phones.size > 0)
            phones = updatePhones(pidmToUpdate, content.metadata, content.phones)

        //update Emails
        def emails
        if (content.containsKey('emails') && content.emails instanceof List && content.emails.size > 0)
            emails = updateEmails(pidmToUpdate, content.metadata, content.emails)

        //update races
        def races
        if (content.containsKey('races') && content.races instanceof List && content.races.size > 0)
            updateRaces(pidmToUpdate, content.metadata, content.races)
        //Build decorator to return LDM response.
        new Person(newPersonBase, content.guid, credentials, addresses, phones, emails, names, newPersonBase?.maritalStatus,ethnicity,races)

    }


    private PersonBasicPersonBase updatePersonBasicPersonBase(pidmToUpdate, newPersonIdentificationName, person, changedPersonIdentification) {
        List<PersonBasicPersonBase> personBaseList = PersonBasicPersonBase.findAllByPidmInList([pidmToUpdate])
        PersonBasicPersonBase newPersonBase

        if (personBaseList.size() == 0) {
            //if there is no person base then create new PersonBase
            newPersonBase = createPersonBasicPersonBase(person, newPersonIdentificationName, changedPersonIdentification)
        } else {
            personBaseList.each { personBase ->
                //Copy personBase attributes into person map from Primary names object.
                person?.credentials?.each { it ->
                    if (it.credentialType == 'Social Security Number') {
                        personBase.ssn = it?.credentialId
                    }
                }
                if (changedPersonIdentification.containsKey('namePrefix')) {
                    personBase.namePrefix = changedPersonIdentification.get('namePrefix')
                }
                if (changedPersonIdentification.containsKey('nameSuffix')) {
                    personBase.nameSuffix = changedPersonIdentification.get('nameSuffix')
                }
                if (changedPersonIdentification.containsKey('preferenceFirstName')) {
                    personBase.preferenceFirstName = changedPersonIdentification.get('preferenceFirstName')
                }
                //Translate enumerations and defaults
                personBase.sex = person?.sex == 'Male' ? 'M' : (person?.sex == 'Female' ? 'F' : 'N')
                def maritalStatus = person.maritalStatus?.guid ? maritalStatusCompositeService.get(person.maritalStatus?.guid) : null
                personBase.maritalStatus = maritalStatus ? MaritalStatus.findByCode(maritalStatus?.code) : null
                if (person.containsKey('ethnicity')) {
                    def ethnicity = person.ethnicity?.guid ? ethnicityCompositeService.get(person.ethnicity?.guid) : null
                    personBase.ethnicity = ethnicity ? Ethnicity.findByCode(ethnicity?.code) : null
                    personBase.ethnic = person.ethnic == "Non-Hispanic" ? '1' : (person.ethnic == "Hispanic" ? '2' : null)
                }

                if (person.containsKey('dateDeceased'))
                    personBase.deadIndicator = person.get('dateDeceased') != null ? 'Y' : null

                newPersonBase = personBasicPersonBaseService.update(personBase)
            }
        }
        return newPersonBase
    }

    private PersonBasicPersonBase createPersonBasicPersonBase(person, newPersonIdentificationName, newPersonIdentification) {
        PersonBasicPersonBase newPersonBase
        if (person.guid) {
            updateGuidValue(newPersonIdentificationName.id, person.guid)

        } else {
            def entity = GlobalUniqueIdentifier.findByLdmNameAndDomainId(ldmName, newPersonIdentificationName.id)
            person.put('guid', entity)
        }

        //Copy ssn attribute from credential to person map.
        if (person?.credentials instanceof List) {
            person?.credentials?.each { it ->
                if (it instanceof Map) {
                    if (it.credentialType == 'Social Security Number') {
                        person.put('ssn', it?.credentialId)
                    } else if (it.credentialType == 'Social Insurance Number') {
                        person.put('ssn', it?.credentialId)
                    }
                }
            }
        }
        //Copy personBase attributes into person map from Primary names object.
        person.put('dataOrigin', person?.metadata?.dataOrigin)
        person.put('namePrefix', newPersonIdentification.get('namePrefix'))
        person.put('nameSuffix', newPersonIdentification.get('nameSuffix'))
        person.put('preferenceFirstName', newPersonIdentification.get('preferenceFirstName'))
        //Translate enumerations and defaults
        person.put('sex', person?.sex == 'Male' ? 'M' : (person?.sex == 'Female' ? 'F' : (person?.sex == 'Unknown' ? 'N' : null)))
        def maritalStatus
        try {
            maritalStatus = person.maritalStatusDetail instanceof Map && person.maritalStatusDetail?.guid ? maritalStatusCompositeService.get(person.maritalStatusDetail?.guid) : null
        } catch (ApplicationException e) {
            throw new ApplicationException("PersonCompositeService", "@@r1:maritalStatus.invalid:BusinessLogicValidationException@@")
        }
        person.put('maritalStatus', maritalStatus ? MaritalStatus.findByCode(maritalStatus?.code) : null)
        def ethnicity
        try {
            if (person.ethnicityDetail instanceof Map) {
                ethnicity = person.ethnicityDetail?.guid ? ethnicityCompositeService.get(person.ethnicityDetail?.guid) : null
            }
        }
        catch (ApplicationException e) {
            if (e.wrappedException instanceof NotFoundException) {
                throw new ApplicationException("PersonCompositeService", "@@r1:ethnicity.invalid:BusinessLogicValidationException@@")
                ethnicity = null
            } else {
                throw e
            }
        }
        person.put('ethnicity', ethnicity ? Ethnicity.findByCode(ethnicity?.code) : null)
        person.put('ethnic', ethnicity ? ethnicity.ethnic : null)
        person.put('deadIndicator', person.get('deadDate') ? 'Y' : null)
        person.put('pidm', newPersonIdentificationName?.pidm)
        person.put('armedServiceMedalVetIndicator', false)
        newPersonBase = personBasicPersonBaseService.create(person)
        newPersonBase
    }


    private updateAddresses(def pidm, Map metadata, List<PersonAddress> newAddresses) {
        def addresses = []
        newAddresses?.each { activeAddress ->
            IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_ADDRESS_TYPE, activeAddress.addressType)
            if (rule?.translationValue == activeAddress.addressType && !addresses.contains { activeAddress.addressType == rule?.value }) {
                def currAddr = PersonAddress.fetchListActiveAddressByPidmAndAddressType([pidm], [AddressType.findByCode(rule.value)])
                def address
                //address exists in DB
                if (currAddr.size > 0) {
                    currAddr.each { currAddress ->
                        if (activeAddress.containsKey('state'))
                            currAddress.state = State.findByDescription(activeAddress.state)
                        if (activeAddress?.nation?.containsKey('code')) {
                            def nation = Nation.findByNation(activeAddress?.country?.code)
                            if (nation) {
                                currAddress.nation = nation
                            } else {
                                log.error "Nation not found for code: ${activeAddress?.country?.code}"
                            }
                        }
                        if (activeAddress.containsKey('county')) {
                            def country = County.findByDescription(activeAddress.county)
                            if (country) {
                                currAddress.county = country
                            } else {
                                log.warn "County not found for code: ${activeAddress.county}"
                            }
                        }
                        if (activeAddress.containsKey('streetLine1'))
                            currAddress.streetLine1 = activeAddress.streetLine1
                        if (activeAddress.containsKey('streetLine2'))
                            currAddress.streetLine2 = activeAddress.streetLine2
                        if (activeAddress.containsKey('streetLine3'))
                            currAddress.streetLine3 = activeAddress.streetLine3
                        if (activeAddress.containsKey('zip'))
                            currAddress.zip = activeAddress.zip
                        if (activeAddress.containsKey('county'))
                            currAddress.county = activeAddress.county
                        address = personAddressService.update(currAddress)
                    }

                } else {
                    address = createAddresses(pidm, metadata, [activeAddress]).get(0)
                }

                def addressDecorator = new Address(address)
                addressDecorator.addressType = rule?.translationValue
                addresses << addressDecorator
            }
        }
        addresses

    }

    private updatePhones(def pidm, Map metadata, List<PersonTelephone> newPhones) {
        def phones = []
        newPhones?.each { activePhone ->
            IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_ADDRESS_TYPE, activePhone.phoneType)
            if (rule?.translationValue == activePhone.phoneType &&
                    !phones.contains { activePhone.phoneType == rule?.value }) {
                def telephoneType = TelephoneType.findByCode(rule?.value)
                def personTelephoneList = PersonTelephone.fetchActiveTelephoneByPidmInList([pidm])
                def phone
                if (personTelephoneList) {
                    personTelephoneList.each { curPhones ->
                        if (curPhones.telephoneType.code == rule.value) {
                            // curPhones.phoneArea
                            curPhones.phoneExtension = activePhone.phoneExtension
                            curPhones.phoneNumber = activePhone.phoneNumber
                            phone = personTelephoneService.update(curPhones)

                        }
                    }

                } else {
                    //Create New Telephones if it dosen't exists
                    phone = createPhones(pidm, metadata, [activePhone]).get(0)
                }
                def phoneDecorator = new Phone(phone)
                phoneDecorator.phoneType = rule.translationValue
                phones << phoneDecorator

            }
        }
        phones

    }


    private updateEmails(def pidm, Map metadata, List<PersonEmail> newEmails) {
        def emails = []
        newEmails?.each { activeEmail ->
            IntegrationConfiguration rule = fetchAllByProcessCodeAndSettingNameAndTranslationValue(PROCESS_CODE, PERSON_EMAIL_TYPE, activeEmail.emailType)
            if (rule?.translationValue == activeEmail.emailType &&
                    !emails.contains { activeEmail.emailType == rule?.value }) {
                def emailList = PersonEmail.findAllByStatusIndicatorAndPidmInList('A', [pidm])
                def email
                if (emailList.size > 0) {
                    PersonEmail curEmails = emailList.get(0)
                    if (curEmails.emailType.code == rule.value) {
                        curEmails.emailAddress = activeEmail.emailAddress
                        def map = [domainModel: curEmails]
                        email = personEmailService.update(map)
                    }

                } else {
                    //Create New emails if it dosen't exists
                    email = createEmails(pidm, metadata, [activeEmail]).get(0)
                }
                def emailDecorator = new Email(email)
                emailDecorator.emailType = rule.translationValue
                emails << emailDecorator
            }
        }
        emails
    }

    List<PersonRace> updateRaces(def pidm, Map metadata, List<Map> newRaces) {
        def races = []
        newRaces?.each { activeRace ->
            if (activeRace?.guid) {
                def race = raceCompositeService.get(activeRace?.guid)
                if (!race) {
                    throw new ApplicationException(PersonCompositeService, "@@r1:race.invalid:BusinessLogicValidationException@@")
                }
                def raceList = PersonRace.fetchByPidm(pidm)
                def personRace
                if (raceList) {
                    raceList.each { personsRace ->
                        personsRace.race = race.race
                        personRace = personRaceService.update(personsRace)
                    }

                } else {
                    //Create New race
                    personRace = createRaces(pidm, metadata, [activeRace]).get(0)
                }
                races << personRace

            } else {
                throw new ApplicationException(PersonCompositeService, "@@r1:race.invalid:BusinessLogicValidationException@@")
            }
        }
        races
    }


    Map parsePhoneNumber(String phoneNumber) {
        Map parsedNumber = [:]
        List<InstitutionalDescription> institutions = InstitutionalDescription.list()
        def institution = institutions.size() > 0 ? institutions[0]: null
        Nation countryLdmCode
        if( institution?.natnCode ) {
            countryLdmCode = Nation.findByCode(institution.natnCode)
        }
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance()
        Phonenumber.PhoneNumber parsedResult = phoneUtil.parse(phoneNumber, countryLdmCode?.scodIso ?: 'US')
        if ( phoneUtil.isNANPACountry(countryLdmCode?.scodIso ?: 'US') ) {
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