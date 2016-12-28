/*********************************************************************************
 Copyright 2012-2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.person

import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.AddressType
import net.hedtech.banner.general.system.TelephoneType
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonTelephoneServiceIntegrationTests extends BaseIntegrationTestCase {

    def personTelephoneService

    //Test data for creating new domain instance
    //Valid test data (For success tests)
    def i_success_telephoneType
    def i_success_addressType
    def i_success_pidm = 1
    def i_success_sequenceNumber = 1
    def i_success_phoneArea = "TTTTT"
    def i_success_phoneNumber = "TTTTT"
    def i_success_phoneExtension = "TTTTT"
    def i_success_statusIndicator = "I"
    def i_success_addressSequenceNumber = 1
    def i_success_primaryIndicator = ""
    def i_success_unlistIndicator = "Y"
    def i_success_commentData = "TTTTT"
    def i_success_internationalAccess = "TTTTT"
    def i_success_countryPhone = "TTTT"

    //Invalid test data (For failure tests)
    def i_failure_telephoneType
    def i_failure_addressType
    def i_failure_pidm = 1
    def i_failure_sequenceNumber = 1
    def i_failure_phoneArea = "777"
    def i_failure_phoneNumber = "TTTTT"
    def i_failure_phoneExtension = "TTTTT"
    def i_failure_statusIndicator = "I"
    def i_failure_addressSequenceNumber = 1
    def i_failure_primaryIndicator = "Y"
    def i_failure_unlistIndicator = "Y"
    def i_failure_commentData = "TTTTT"
    def i_failure_internationalAccess = "TTTTT"
    def i_failure_countryPhone = "TTTT"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)
    def u_success_telephoneType
    def u_success_addressType
    def u_success_pidm = 1
    def u_success_sequenceNumber = 1
    def u_success_phoneArea = "TTTTT"
    def u_success_phoneNumber = "TTTTT"
    def u_success_phoneExtension = "TTTTT"
    def u_success_statusIndicator = "I"
    def u_success_addressSequenceNumber = 1
    def u_success_primaryIndicator = ""
    def u_success_unlistIndicator = "Y"
    def u_success_commentData = "TTTTT"
    def u_success_internationalAccess = "TTTTT"
    def u_success_countryPhone = "TTTT"

    //Valid test data (For failure tests)
    def u_failure_telephoneType
    def u_failure_addressType
    def u_failure_pidm = 1
    def u_failure_sequenceNumber = 1
    def u_failure_phoneArea = "TTTTT"
    def u_failure_phoneNumber = "TTTTT"
    def u_failure_phoneExtension = "TTTTT"
    def u_failure_statusIndicator = "I"
    def u_failure_addressSequenceNumber = 1
    def u_failure_primaryIndicator = "Y"
    def u_failure_unlistIndicator = "Y"
    def u_failure_commentData = "TTTTT"
    def u_failure_internationalAccess = "TTTTT"
    def u_failure_countryPhone = "TTTT"


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction
    void initializeTestDataForReferences() {
        //Valid test data (For success tests)
        i_success_telephoneType = TelephoneType.findByCode("TE")
        i_success_addressType = AddressType.findByCode("MA")

        //Invalid test data (For failure tests)
        i_failure_telephoneType = TelephoneType.findByCode("PR")
        i_failure_addressType = AddressType.findByCode("MA")

        //Valid test data (For success tests)
        u_success_telephoneType = TelephoneType.findByCode("PR")
        u_success_addressType = AddressType.findByCode("MA")

        //Valid test data (For failure tests)
        u_failure_telephoneType = TelephoneType.findByCode("PR")
        u_failure_addressType = AddressType.findByCode("MA")

        //Test data for references for custom tests

    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testPersonTelephoneValidCreate() {
        def personTelephone = newValidForCreatePersonTelephone()
        def map = [domainModel: personTelephone]
        personTelephone = personTelephoneService.create(map)
        assertNotNull "PersonTelephone ID is null in PersonTelephone Service Tests Create", personTelephone.id
        assertNotNull "PersonTelephone telephoneType is null in PersonTelephone Service Tests", personTelephone.telephoneType
        assertNotNull "PersonTelephone addressType is null in PersonTelephone Service Tests", personTelephone.addressType
        assertNotNull personTelephone.version
        assertNotNull personTelephone.dataOrigin
        assertNotNull personTelephone.lastModifiedBy
        assertNotNull personTelephone.lastModified
    }


    @Test
    void testPersonTelephoneInvalidCreate() {
        def personTelephone = newInvalidForCreatePersonTelephone()
        def map = [domainModel: personTelephone]
        shouldFail(ApplicationException) {
            personTelephone = personTelephoneService.create(map)
        }
    }


    @Test
    void testPersonTelephoneValidUpdate() {
        def personTelephone = newValidForCreatePersonTelephone()
        def map = [domainModel: personTelephone]
        personTelephone = personTelephoneService.create(map)
        assertNotNull "PersonTelephone ID is null in PersonTelephone Service Tests Create", personTelephone.id
        assertNotNull "PersonTelephone telephoneType is null in PersonTelephone Service Tests", personTelephone.telephoneType
        assertNotNull "PersonTelephone addressType is null in PersonTelephone Service Tests", personTelephone.addressType
        assertNotNull personTelephone.version
        assertNotNull personTelephone.dataOrigin
        assertNotNull personTelephone.lastModifiedBy
        assertNotNull personTelephone.lastModified
        //Update the entity with new values
        personTelephone.addressType = u_success_addressType
        personTelephone.phoneArea = u_success_phoneArea
        personTelephone.phoneNumber = u_success_phoneNumber
        personTelephone.phoneExtension = u_success_phoneExtension
        personTelephone.statusIndicator = u_success_statusIndicator
        personTelephone.addressSequenceNumber = u_success_addressSequenceNumber
        personTelephone.primaryIndicator = u_success_primaryIndicator
        personTelephone.unlistIndicator = u_success_unlistIndicator
        personTelephone.commentData = u_success_commentData
        personTelephone.internationalAccess = u_success_internationalAccess
        personTelephone.countryPhone = u_success_countryPhone

        map.domainModel = personTelephone
        personTelephone = personTelephoneService.update(map)
        // test the values
        assertEquals u_success_phoneArea, personTelephone.phoneArea
        assertEquals u_success_phoneNumber, personTelephone.phoneNumber
        assertEquals u_success_phoneExtension, personTelephone.phoneExtension
        assertEquals u_success_statusIndicator, personTelephone.statusIndicator
        assertEquals u_success_addressSequenceNumber, personTelephone.addressSequenceNumber
//		assertEquals u_success_primaryIndicator, personTelephone.primaryIndicator
        assertEquals u_success_unlistIndicator, personTelephone.unlistIndicator
        assertEquals u_success_commentData, personTelephone.commentData
        assertEquals u_success_internationalAccess, personTelephone.internationalAccess
        assertEquals u_success_countryPhone, personTelephone.countryPhone
        assertEquals u_success_addressType, personTelephone.addressType
    }


    @Test
    void testPersonTelephoneInvalidUpdate() {
        def personTelephone = newValidForCreatePersonTelephone()
        def map = [domainModel: personTelephone]
        personTelephone = personTelephoneService.create(map)
        assertNotNull "PersonTelephone ID is null in PersonTelephone Service Tests Create", personTelephone.id
        assertNotNull "PersonTelephone telephoneType is null in PersonTelephone Service Tests", personTelephone.telephoneType
        assertNotNull "PersonTelephone addressType is null in PersonTelephone Service Tests", personTelephone.addressType
        assertNotNull personTelephone.version
        assertNotNull personTelephone.dataOrigin
        assertNotNull personTelephone.lastModifiedBy
        assertNotNull personTelephone.lastModified
        //Update the entity with new invalid values
        personTelephone.addressType = u_failure_addressType
        personTelephone.phoneArea = u_failure_phoneArea
        personTelephone.phoneNumber = u_failure_phoneNumber
        personTelephone.phoneExtension = u_failure_phoneExtension
        personTelephone.statusIndicator = u_failure_statusIndicator
        personTelephone.addressSequenceNumber = u_failure_addressSequenceNumber
        personTelephone.primaryIndicator = u_failure_primaryIndicator
        personTelephone.unlistIndicator = u_failure_unlistIndicator
        personTelephone.commentData = u_failure_commentData
        personTelephone.internationalAccess = u_failure_internationalAccess
        personTelephone.countryPhone = u_failure_countryPhone

        map.domainModel = personTelephone
        shouldFail(ApplicationException) {
            personTelephone = personTelephoneService.update(map)
        }
    }


    @Test
    void testPersonTelephoneDelete() {
        def personTelephone = newValidForCreatePersonTelephone()
        def map = [domainModel: personTelephone]
        personTelephone = personTelephoneService.create(map)
        assertNotNull "PersonTelephone ID is null in PersonTelephone Service Tests Create", personTelephone.id
        def id = personTelephone.id
        map = [domainModel: personTelephone]
        personTelephoneService.delete(map)
        assertNull "PersonTelephone should have been deleted", personTelephone.get(id)
    }


    @Test
    void testReadOnly() {
        def personTelephone = newValidForCreatePersonTelephone()
        def map = [domainModel: personTelephone]
        personTelephone = personTelephoneService.create(map)
        assertNotNull "PersonTelephone ID is null in PersonTelephone Service Tests Create", personTelephone.id
        map.domainModel.telephoneType = u_success_telephoneType

        personTelephone.pidm = u_success_pidm
        personTelephone.sequenceNumber = u_success_sequenceNumber
        try {
            personTelephoneService.update([domainModel: personTelephone])
            fail("This should have failed with @@r1:readonlyFieldsCannotBeModified")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }


    @Test
    void testFetchAllActiveByPidmInListAndTelephoneTypeCodeInList() {
        def pidmList = [PersonUtility.getPerson("210009710").pidm]
        def results = personTelephoneService.fetchAllActiveByPidmInListAndTelephoneTypeCodeInList(pidmList, [TelephoneType.findByCode("MA").code])
        assertTrue results.size() > 0
        assertTrue results[0] instanceof PersonTelephone
    }


    @Test
    void testFetchAllByIdInListAndTelephoneTypeCodeInList() {
        def pidmList = [PersonUtility.getPerson("210009710").pidm]
        List<PersonTelephone> personTelephones = PersonTelephone.findAllByPidmInList(pidmList)
        def idList = personTelephones.collect { it.id }
        assertTrue idList.size() > 0
        def results = personTelephoneService.fetchAllByIdInListAndTelephoneTypeCodeInList(idList, [TelephoneType.findByCode("MA").code])
        assertTrue results.size() > 0
        assertTrue results[0] instanceof PersonTelephone
    }


	@Test
	void testFetchActiveTelephonesByPidmWithUnlisted(){
		def pidm = PersonUtility.getPerson("510000001").pidm
		def sequenceConfig = [processCode: 'PERSONAL_INFORMATION_SSB', settingName: 'PERS.INFO.OVERVIEW.PHONEX']

		def phoneNumbers = personTelephoneService.fetchActiveTelephonesByPidm(pidm, sequenceConfig, true)

		assertEquals 4, phoneNumbers.size()
		assertEquals '5555000', phoneNumbers[0].phoneNumber
		assertEquals  1, phoneNumbers[0].displayPriority
		assertEquals '301 5555000 51', phoneNumbers[0].displayPhoneNumber
	}

	@Test
	void testFetchActiveTelephonesByPidmWithoutUnlisted(){
		def pidm = PersonUtility.getPerson("510000001").pidm
		def sequenceConfig = [processCode: 'PERSONAL_INFORMATION_SSB', settingName: 'PERS.INFO.OVERVIEW.PHONEX']

		def phoneNumbers = personTelephoneService.fetchActiveTelephonesByPidm(pidm, sequenceConfig)

		assertEquals 3, phoneNumbers.size()
	}

	@Test
	void testFetchActiveTelephonesByPidmWithoutSequenceConfigAndWithoutUnlisted(){
		def pidm = PersonUtility.getPerson("510000001").pidm

		def phoneNumbers = personTelephoneService.fetchActiveTelephonesByPidm(pidm)

		assertEquals 3, phoneNumbers.size()
		assertEquals '5555000', phoneNumbers[0].phoneNumber
		assertNull phoneNumbers[0].displayPriority
		assertEquals '301 5555000 51', phoneNumbers[0].displayPhoneNumber
	}

	@Test
    void testInactivatePhone() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def phones = personTelephoneService.fetchActiveTelephonesByPidm(pidm)

        assertLength 1, phones
        assertEquals '2083094', phones[0].phoneNumber

        phones[0].phoneNumber = '57557571'
        personTelephoneService.inactivatePhone(phones[0]);

        def inactivePhone = PersonTelephone.get(phones[0].id);

        assertEquals 'I', inactivePhone.statusIndicator
        assertEquals '2083094', inactivePhone.phoneNumber
    }

    @Test
    void testInactivateAndCreate() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def phones = personTelephoneService.fetchActiveTelephonesByPidm(pidm)

        assertLength 1, phones
        assertEquals '2083094', phones[0].phoneNumber

        def id = phones[0].id

        phones[0].pidm = pidm
        phones[0].phoneNumber = '57557571'
        personTelephoneService.inactivateAndCreate(phones[0]);

        def inactivePhone = PersonTelephone.get(id);

        assertEquals 'I', inactivePhone.statusIndicator
        assertEquals '2083094', inactivePhone.phoneNumber

        phones = personTelephoneService.fetchActiveTelephonesByPidm(pidm)

        assertLength 1, phones
        assertEquals '57557571', phones[0].phoneNumber
    }


    private def newValidForCreatePersonTelephone() {
//        def sql = new Sql(sessionFactory.getCurrentSession().connection())
//        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
//        def bannerValues = sql.firstRow(idSql)
//        def ibannerId = bannerValues.bannerId
//        def ipidm = bannerValues.pidm
//        def person = new PersonIdentificationName(
//                pidm: ipidm,
//                bannerId: ibannerId,
//                lastName: "TTTTT",
//                firstName: "TTTTT",
//                middleName: "TTTTT",
//                changeIndicator: null,
//                entityIndicator: "P"
//        )
//        person.save(flush: true, failOnError: true)
//        assert person.id
        def pidm = PersonUtility.getPerson("210009710").pidm
        def personTelephone = new PersonTelephone(
                pidm: pidm,
                sequenceNumber: i_success_sequenceNumber,
                phoneArea: i_success_phoneArea,
                phoneNumber: i_success_phoneNumber,
                phoneExtension: i_success_phoneExtension,
                statusIndicator: i_success_statusIndicator,
                addressSequenceNumber: i_success_addressSequenceNumber,
                primaryIndicator: i_success_primaryIndicator,
                unlistIndicator: i_success_unlistIndicator,
                commentData: i_success_commentData,
                internationalAccess: i_success_internationalAccess,
                countryPhone: i_success_countryPhone,
                telephoneType: i_success_telephoneType,
                addressType: i_success_addressType
        )
        return personTelephone
    }

    private def newInvalidForCreatePersonTelephone() {
//        def sql = new Sql(sessionFactory.getCurrentSession().connection())
//        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
//        def bannerValues = sql.firstRow(idSql)
//        def ibannerId = bannerValues.bannerId
//        def ipidm = bannerValues.pidm
//        def person = new PersonIdentificationName(
//                pidm: ipidm,
//                bannerId: ibannerId,
//                lastName: "TTTTT",
//                firstName: "TTTTT",
//                middleName: "TTTTT",
//                changeIndicator: null,
//                entityIndicator: "P"
//        )
//        person.save(flush: true, failOnError: true)
//        assert person.id
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def personTelephone = new PersonTelephone(
                pidm: pidm,
                sequenceNumber: i_failure_sequenceNumber,
                phoneArea: i_failure_phoneArea,
                phoneNumber: i_failure_phoneNumber,
                phoneExtension: i_failure_phoneExtension,
                statusIndicator: i_failure_statusIndicator,
                addressSequenceNumber: i_failure_addressSequenceNumber,
                primaryIndicator: i_failure_primaryIndicator,
                unlistIndicator: i_failure_unlistIndicator,
                commentData: i_failure_commentData,
                internationalAccess: i_failure_internationalAccess,
                countryPhone: i_failure_countryPhone,

                telephoneType: i_failure_telephoneType,
                addressType: i_failure_addressType
        )
        return personTelephone
    }
}
