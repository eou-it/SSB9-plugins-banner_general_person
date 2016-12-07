/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import org.codehaus.groovy.runtime.InvokerHelper
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.*
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonBasicPersonBaseServiceIntegrationTests extends BaseIntegrationTestCase {

    def personBasicPersonBaseService

    //Test data for creating new domain instance
    //Valid test data (For success tests)
    def i_success_legacy
    def i_success_ethnicity
    def i_success_maritalStatus
    def i_success_religion
    def i_success_citizenType
    def i_success_stateBirth
    def i_success_stateDriver
    def i_success_nationDriver
    def i_success_pidm
    def i_success_bannerId
    def i_success_ssn = "TTTTT"
    def i_success_birthDate = new Date()
    def i_success_sex = "M"
    def i_success_confidIndicator = "Y"
    def i_success_deadIndicator = "Y"
    def i_success_vetcFileNumber = "TTTTT"
    def i_success_legalName = "Levitch"
    def i_success_preferenceFirstName = "Happy"
    def i_success_namePrefix = "TTTTT"
    def i_success_nameSuffix = "TTTTT"
    def i_success_veraIndicator = "V"
    def i_success_citizenshipIndicator = "U"
    def i_success_deadDate = new Date()
    def i_success_hair = "TT"
    def i_success_eyeColor = "TT"
    def i_success_cityBirth = "TTTTT"
    def i_success_driverLicense = "TTTTT"
    def i_success_height = 1
    def i_success_weight = 1
    def i_success_sdvetIndicator = "Y"
    def i_success_licenseIssuedDate = new Date()
    def i_success_licenseExpiresDate = new Date()
    def i_success_incarcerationIndicator = "#"
    def i_success_itin = 1L
    def i_success_activeDutySeprDate = new Date()
    def i_success_ethnic = "1"
    def i_success_confirmedRe = "Y"
    def i_success_confirmedReDate = new Date()
    def i_success_armedServiceMedalVetIndicator = true

    //Invalid test data (For failure tests)
    def i_failure_legacy
    def i_failure_ethnicity
    def i_failure_maritalStatus
    def i_failure_religion
    def i_failure_citizenType
    def i_failure_stateBirth
    def i_failure_stateDriver
    def i_failure_nationDriver
    def i_failure_pidm = 1
    def i_failure_ssn = "TTTTT"
    def i_failure_birthDate = new Date()
    def i_failure_sex = "F"
    def i_failure_confidIndicator = "Y"
    def i_failure_deadIndicator = "Y"
    def i_failure_vetcFileNumber = "TTTTT"
    def i_failure_legalName = "TTTTT"
    def i_failure_preferenceFirstName = "TTTTT"
    def i_failure_namePrefix = "TTTTT"
    def i_failure_nameSuffix = "TTTTT"
    def i_failure_veraIndicator = "V"
    def i_failure_citizenshipIndicator = "U"
    def i_failure_deadDate = new Date()
    def i_failure_hair = "TTT"
    def i_failure_eyeColor = "TT"
    def i_failure_cityBirth = "TTTTT"
    def i_failure_driverLicense = "TTTTT"
    def i_failure_height = 1
    def i_failure_weight = 1
    def i_failure_sdvetIndicator = "Y"
    def i_failure_licenseIssuedDate = new Date()
    def i_failure_licenseExpiresDate = new Date()
    def i_failure_incarcerationIndicator = "#"
    def i_failure_itin = 1L
    def i_failure_activeDutySeprDate = new Date()
    def i_failure_ethnic = "1"
    def i_failure_confirmedRe = "Y"
    def i_failure_confirmedReDate = new Date()
    def i_failure_armedServiceMedalVetIndicator = true

    //Test data for creating updating domain instance
    //Valid test data (For success tests)
    def u_success_legacy
    def u_success_ethnicity
    def u_success_maritalStatus
    def u_success_religion
    def u_success_citizenType
    def u_success_stateBirth
    def u_success_stateDriver
    def u_success_nationDriver
    def u_success_unitOfMeasureHeight
    def u_success_unitOfMeasureWeight
    def u_success_pidm = 1
    def u_success_ssn = "TTTTT"
    def u_success_birthDate = new Date()
    def u_success_sex = "M"
    def u_success_confidIndicator = "Y"
    def u_success_deadIndicator = "Y"
    def u_success_vetcFileNumber = "TTTTT"
    def u_success_legalName = "TTTTT"
    def u_success_preferenceFirstName = "TTTTT"
    def u_success_namePrefix = "TTTTT"
    def u_success_nameSuffix = "TTTTT"
    def u_success_veraIndicator = "V"
    def u_success_citizenshipIndicator = "U"
    def u_success_deadDate = new Date()
    def u_success_hair = "TT"
    def u_success_eyeColor = "TT"
    def u_success_cityBirth = "TTTTT"
    def u_success_driverLicense = "TTTTT"
    def u_success_height = 1
    def u_success_weight = 1
    def u_success_sdvetIndicator = "Y"
    def u_success_licenseIssuedDate = new Date()
    def u_success_licenseExpiresDate = new Date()
    def u_success_incarcerationIndicator = "#"
    def u_success_itin = 1L
    def u_success_activeDutySeprDate = new Date()
    def u_success_ethnic = "1"
    def u_success_confirmedRe = "Y"
    def u_success_confirmedReDate = new Date()
    def u_success_armedServiceMedalVetIndicator = true

    //Valid test data (For failure tests)
    def u_failure_legacy
    def u_failure_ethnicity
    def u_failure_maritalStatus
    def u_failure_religion
    def u_failure_citizenType
    def u_failure_stateBirth
    def u_failure_stateDriver
    def u_failure_nationDriver
    def u_failure_unitOfMeasureHeight
    def u_failure_unitOfMeasureWeight
    def u_failure_pidm = 1
    def u_failure_ssn = "TTTTT"
    def u_failure_birthDate = new Date()
    def u_failure_sex = "M"
    def u_failure_confidIndicator = "Y"
    def u_failure_deadIndicator = "Y"
    def u_failure_vetcFileNumber = "TTTTT"
    def u_failure_legalName = "TTTTT"
    def u_failure_preferenceFirstName = "TTTTT"
    def u_failure_namePrefix = "TTTTT"
    def u_failure_nameSuffix = "TTTTT"
    def u_failure_veraIndicator = "V"
    def u_failure_citizenshipIndicator = "U"
    def u_failure_deadDate = new Date()
    def u_failure_hair = "TTT"
    def u_failure_eyeColor = "TT"
    def u_failure_cityBirth = "TTTTT"
    def u_failure_driverLicense = "TTTTT"
    def u_failure_height = 1
    def u_failure_weight = 1
    def u_failure_sdvetIndicator = "Y"
    def u_failure_licenseIssuedDate = new Date()
    def u_failure_licenseExpiresDate = new Date()
    def u_failure_incarcerationIndicator = "#"
    def u_failure_itin = 1L
    def u_failure_activeDutySeprDate = new Date()
    def u_failure_ethnic = "1"
    def u_failure_confirmedRe = "Y"
    def u_failure_confirmedReDate = new Date()
    def u_failure_armedServiceMedalVetIndicator = true


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
        i_success_legacy = Legacy.findByCode("M")
        i_success_ethnicity = Ethnicity.findByCode("1")
        i_success_maritalStatus = MaritalStatus.findByCode("S")
        i_success_religion = Religion.findByCode("JE")
        i_success_citizenType = CitizenType.findByCode("Y")
        i_success_stateBirth = State.findByCode("DE")
        i_success_stateDriver = State.findByCode("PA")
        i_success_nationDriver = Nation.findByCode("157")

        //Invalid test data (For failure tests)
        i_failure_legacy = Legacy.findByCode("M")
        i_failure_ethnicity = Ethnicity.findByCode("1")
        i_failure_maritalStatus = MaritalStatus.findByCode("S")
        i_failure_religion = Religion.findByCode("JE")
        i_failure_citizenType = CitizenType.findByCode("Y")
        i_failure_stateBirth = State.findByCode("DE")
        i_failure_stateDriver = State.findByCode("DE")
        i_failure_nationDriver = Nation.findByCode("157")

        //Valid test data (For success tests)
        u_success_legacy = Legacy.findByCode("M")
        u_success_ethnicity = Ethnicity.findByCode("1")
        u_success_maritalStatus = MaritalStatus.findByCode("S")
        u_success_religion = Religion.findByCode("JE")
        u_success_citizenType = CitizenType.findByCode("Y")
        u_success_stateBirth = State.findByCode("DE")
        u_success_stateDriver = State.findByCode("DE")
        u_success_nationDriver = Nation.findByCode("157")

        //Valid test data (For failure tests)
        u_failure_legacy = Legacy.findByCode("M")
        u_failure_ethnicity = Ethnicity.findByCode("1")
        u_failure_maritalStatus = MaritalStatus.findByCode("S")
        u_failure_religion = Religion.findByCode("JE")
        u_failure_citizenType = CitizenType.findByCode("Y")
        u_failure_stateBirth = State.findByCode("DE")
        u_failure_stateDriver = State.findByCode("DE")
        u_failure_nationDriver = Nation.findByCode("157")
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testPersonBasicPersonBaseValidCreate() {
        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        def map = [domainModel: personBasicPersonBase]
        personBasicPersonBase = personBasicPersonBaseService.create(map)
        assertNotNull "PersonBasicPersonBase ID is null in PersonBasicPersonBase Service Tests Create", personBasicPersonBase.id
        assertNotNull "PersonBasicPersonBase legacy is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.legacy
        assertNotNull "PersonBasicPersonBase ethnicity is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.ethnicity
        assertNotNull "PersonBasicPersonBase maritalStatus is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.maritalStatus
        assertNotNull "PersonBasicPersonBase religion is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.religion
        assertNotNull "PersonBasicPersonBase citizenType is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.citizenType
        assertNotNull "PersonBasicPersonBase stateBirth is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.stateBirth
        assertNotNull "PersonBasicPersonBase stateDriver is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.stateDriver
        assertNotNull "PersonBasicPersonBase nationDriver is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.nationDriver
        assertNotNull "PersonBasicPersonBase unitOfMeasureHeight is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.unitOfMeasureHeight
        assertNotNull "PersonBasicPersonBase unitOfMeasureWeight is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.unitOfMeasureWeight
        assertNotNull personBasicPersonBase.version
        assertNotNull personBasicPersonBase.dataOrigin
        assertNotNull personBasicPersonBase.lastModifiedBy
        assertNotNull personBasicPersonBase.lastModified
    }


    @Test
    void testPersonBasicPersonBaseInvalidCreate() {
        def personBasicPersonBase = newInvalidForCreatePersonBasicPersonBase()
        def map = [domainModel: personBasicPersonBase]
        shouldFail(ApplicationException) {
            personBasicPersonBase = personBasicPersonBaseService.create(map)
        }
    }


    @Test
    void testPersonBasicPersonBaseValidUpdate() {
        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        def map = [domainModel: personBasicPersonBase]
        personBasicPersonBase = personBasicPersonBaseService.create(map)
        assertNotNull "PersonBasicPersonBase ID is null in PersonBasicPersonBase Service Tests Create", personBasicPersonBase.id
        assertNotNull "PersonBasicPersonBase legacy is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.legacy
        assertNotNull "PersonBasicPersonBase ethnicity is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.ethnicity
        assertNotNull "PersonBasicPersonBase maritalStatus is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.maritalStatus
        assertNotNull "PersonBasicPersonBase religion is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.religion
        assertNotNull "PersonBasicPersonBase citizenType is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.citizenType
        assertNotNull "PersonBasicPersonBase stateBirth is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.stateBirth
        assertNotNull "PersonBasicPersonBase stateDriver is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.stateDriver
        assertNotNull "PersonBasicPersonBase nationDriver is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.nationDriver
        assertNotNull "PersonBasicPersonBase unitOfMeasureHeight is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.unitOfMeasureHeight
        assertNotNull "PersonBasicPersonBase unitOfMeasureWeight is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.unitOfMeasureWeight
        assertNotNull personBasicPersonBase.version
        assertNotNull personBasicPersonBase.dataOrigin
        assertNotNull personBasicPersonBase.lastModifiedBy
        assertNotNull personBasicPersonBase.lastModified
        //Update the entity with new values
        personBasicPersonBase.legacy = u_success_legacy
        personBasicPersonBase.ethnicity = u_success_ethnicity
        personBasicPersonBase.maritalStatus = u_success_maritalStatus
        personBasicPersonBase.religion = u_success_religion
        personBasicPersonBase.citizenType = u_success_citizenType
        personBasicPersonBase.stateBirth = u_success_stateBirth
        personBasicPersonBase.stateDriver = u_success_stateDriver
        personBasicPersonBase.nationDriver = u_success_nationDriver
        personBasicPersonBase.unitOfMeasureHeight = u_success_unitOfMeasureHeight
        personBasicPersonBase.unitOfMeasureWeight = u_success_unitOfMeasureWeight
        personBasicPersonBase.ssn = u_success_ssn
        personBasicPersonBase.birthDate = u_success_birthDate
        personBasicPersonBase.sex = u_success_sex
        personBasicPersonBase.confidIndicator = u_success_confidIndicator
        personBasicPersonBase.deadIndicator = u_success_deadIndicator
        personBasicPersonBase.vetcFileNumber = u_success_vetcFileNumber
        personBasicPersonBase.legalName = u_success_legalName
        personBasicPersonBase.preferenceFirstName = u_success_preferenceFirstName
        personBasicPersonBase.namePrefix = u_success_namePrefix
        personBasicPersonBase.nameSuffix = u_success_nameSuffix
        personBasicPersonBase.veraIndicator = u_success_veraIndicator
        personBasicPersonBase.citizenshipIndicator = u_success_citizenshipIndicator
        personBasicPersonBase.deadDate = u_success_deadDate
        personBasicPersonBase.hair = u_success_hair
        personBasicPersonBase.eyeColor = u_success_eyeColor
        personBasicPersonBase.cityBirth = u_success_cityBirth
        personBasicPersonBase.driverLicense = u_success_driverLicense
        personBasicPersonBase.height = u_success_height
        personBasicPersonBase.weight = u_success_weight
        personBasicPersonBase.sdvetIndicator = u_success_sdvetIndicator
        personBasicPersonBase.licenseIssuedDate = u_success_licenseIssuedDate
        personBasicPersonBase.licenseExpiresDate = u_success_licenseExpiresDate
        personBasicPersonBase.incarcerationIndicator = u_success_incarcerationIndicator
        personBasicPersonBase.itin = u_success_itin
        personBasicPersonBase.activeDutySeprDate = u_success_activeDutySeprDate
        personBasicPersonBase.ethnic = u_success_ethnic
        personBasicPersonBase.confirmedRe = u_success_confirmedRe
        personBasicPersonBase.confirmedReDate = u_success_confirmedReDate
        personBasicPersonBase.armedServiceMedalVetIndicator = u_success_armedServiceMedalVetIndicator
        map.domainModel = personBasicPersonBase

        personBasicPersonBase = personBasicPersonBaseService.update(map)

        // test the values
        assertEquals u_success_ssn, personBasicPersonBase.ssn
//        assertEquals u_success_birthDate, personBasicPersonBase.birthDate
        assertEquals u_success_sex, personBasicPersonBase.sex
        assertEquals u_success_confidIndicator, personBasicPersonBase.confidIndicator
        assertEquals u_success_deadIndicator, personBasicPersonBase.deadIndicator
        assertEquals u_success_vetcFileNumber, personBasicPersonBase.vetcFileNumber
        assertEquals u_success_legalName, personBasicPersonBase.legalName
        assertEquals u_success_preferenceFirstName, personBasicPersonBase.preferenceFirstName
        assertEquals u_success_namePrefix, personBasicPersonBase.namePrefix
        assertEquals u_success_nameSuffix, personBasicPersonBase.nameSuffix
        assertEquals u_success_veraIndicator, personBasicPersonBase.veraIndicator
        assertEquals u_success_citizenshipIndicator, personBasicPersonBase.citizenshipIndicator
//        assertEquals u_success_deadDate, personBasicPersonBase.deadDate
        assertEquals u_success_hair, personBasicPersonBase.hair
        assertEquals u_success_eyeColor, personBasicPersonBase.eyeColor
        assertEquals u_success_cityBirth, personBasicPersonBase.cityBirth
        assertEquals u_success_driverLicense, personBasicPersonBase.driverLicense
        assertEquals u_success_height, personBasicPersonBase.height
        assertEquals u_success_weight, personBasicPersonBase.weight
        assertEquals u_success_sdvetIndicator, personBasicPersonBase.sdvetIndicator
//        assertEquals u_success_licenseIssuedDate, personBasicPersonBase.licenseIssuedDate
//        assertEquals u_success_licenseExpiresDate, personBasicPersonBase.licenseExpiresDate
        assertEquals u_success_incarcerationIndicator, personBasicPersonBase.incarcerationIndicator
        assertEquals u_success_itin, personBasicPersonBase.itin
//        assertEquals u_success_activeDutySeprDate, personBasicPersonBase.activeDutySeprDate
        assertEquals u_success_ethnic, personBasicPersonBase.ethnic
        assertEquals u_success_confirmedRe, personBasicPersonBase.confirmedRe
//        assertEquals u_success_confirmedReDate, personBasicPersonBase.confirmedReDate
        assertEquals u_success_armedServiceMedalVetIndicator, personBasicPersonBase.armedServiceMedalVetIndicator
        assertEquals u_success_legacy, personBasicPersonBase.legacy
        assertEquals u_success_ethnicity, personBasicPersonBase.ethnicity
        assertEquals u_success_maritalStatus, personBasicPersonBase.maritalStatus
        assertEquals u_success_religion, personBasicPersonBase.religion
        assertEquals u_success_citizenType, personBasicPersonBase.citizenType
        assertEquals u_success_stateBirth, personBasicPersonBase.stateBirth
        assertEquals u_success_stateDriver, personBasicPersonBase.stateDriver
        assertEquals u_success_nationDriver, personBasicPersonBase.nationDriver
        assertEquals u_success_unitOfMeasureHeight, personBasicPersonBase.unitOfMeasureHeight
        assertEquals u_success_unitOfMeasureWeight, personBasicPersonBase.unitOfMeasureWeight
    }


    @Test
    void testPersonBasicPersonBaseInvalidUpdate() {
        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        def map = [domainModel: personBasicPersonBase]
        personBasicPersonBase = personBasicPersonBaseService.create(map)
        assertNotNull "PersonBasicPersonBase ID is null in PersonBasicPersonBase Service Tests Create", personBasicPersonBase.id
        assertNotNull "PersonBasicPersonBase legacy is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.legacy
        assertNotNull "PersonBasicPersonBase ethnicity is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.ethnicity
        assertNotNull "PersonBasicPersonBase maritalStatus is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.maritalStatus
        assertNotNull "PersonBasicPersonBase religion is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.religion
        assertNotNull "PersonBasicPersonBase citizenType is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.citizenType
        assertNotNull "PersonBasicPersonBase stateBirth is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.stateBirth
        assertNotNull "PersonBasicPersonBase stateDriver is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.stateDriver
        assertNotNull "PersonBasicPersonBase nationDriver is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.nationDriver
        assertNotNull "PersonBasicPersonBase unitOfMeasureHeight is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.unitOfMeasureHeight
        assertNotNull "PersonBasicPersonBase unitOfMeasureWeight is null in PersonBasicPersonBase Service Tests", personBasicPersonBase.unitOfMeasureWeight
        assertNotNull personBasicPersonBase.version
        assertNotNull personBasicPersonBase.dataOrigin
        assertNotNull personBasicPersonBase.lastModifiedBy
        assertNotNull personBasicPersonBase.lastModified
        //Update the entity with new invalid values
        personBasicPersonBase.legacy = u_failure_legacy
        personBasicPersonBase.ethnicity = u_failure_ethnicity
        personBasicPersonBase.maritalStatus = u_failure_maritalStatus
        personBasicPersonBase.religion = u_failure_religion
        personBasicPersonBase.citizenType = u_failure_citizenType
        personBasicPersonBase.stateBirth = u_failure_stateBirth
        personBasicPersonBase.stateDriver = u_failure_stateDriver
        personBasicPersonBase.nationDriver = u_failure_nationDriver
        personBasicPersonBase.unitOfMeasureHeight = u_failure_unitOfMeasureHeight
        personBasicPersonBase.unitOfMeasureWeight = u_failure_unitOfMeasureWeight
        personBasicPersonBase.ssn = u_failure_ssn
        personBasicPersonBase.birthDate = u_failure_birthDate
        personBasicPersonBase.sex = u_failure_sex
        personBasicPersonBase.confidIndicator = u_failure_confidIndicator
        personBasicPersonBase.deadIndicator = u_failure_deadIndicator
        personBasicPersonBase.vetcFileNumber = u_failure_vetcFileNumber
        personBasicPersonBase.legalName = u_failure_legalName
        personBasicPersonBase.preferenceFirstName = u_failure_preferenceFirstName
        personBasicPersonBase.namePrefix = u_failure_namePrefix
        personBasicPersonBase.nameSuffix = u_failure_nameSuffix
        personBasicPersonBase.veraIndicator = u_failure_veraIndicator
        personBasicPersonBase.citizenshipIndicator = u_failure_citizenshipIndicator
        personBasicPersonBase.deadDate = u_failure_deadDate
        personBasicPersonBase.hair = u_failure_hair
        personBasicPersonBase.eyeColor = u_failure_eyeColor
        personBasicPersonBase.cityBirth = u_failure_cityBirth
        personBasicPersonBase.driverLicense = u_failure_driverLicense
        personBasicPersonBase.height = u_failure_height
        personBasicPersonBase.weight = u_failure_weight
        personBasicPersonBase.sdvetIndicator = u_failure_sdvetIndicator
        personBasicPersonBase.licenseIssuedDate = u_failure_licenseIssuedDate
        personBasicPersonBase.licenseExpiresDate = u_failure_licenseExpiresDate
        personBasicPersonBase.incarcerationIndicator = u_failure_incarcerationIndicator
        personBasicPersonBase.itin = u_failure_itin
        personBasicPersonBase.activeDutySeprDate = u_failure_activeDutySeprDate
        personBasicPersonBase.ethnic = u_failure_ethnic
        personBasicPersonBase.confirmedRe = u_failure_confirmedRe
        personBasicPersonBase.confirmedReDate = u_failure_confirmedReDate
        personBasicPersonBase.armedServiceMedalVetIndicator = u_failure_armedServiceMedalVetIndicator

        map.domainModel = personBasicPersonBase
        shouldFail(ApplicationException) {
            personBasicPersonBase = personBasicPersonBaseService.update(map)
        }
    }


    @Test
    void testPersonBasicPersonBaseDelete() {
        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        def map = [domainModel: personBasicPersonBase]
        personBasicPersonBase = personBasicPersonBaseService.create(map)
        assertNotNull "PersonBasicPersonBase ID is null in PersonBasicPersonBase Service Tests Create", personBasicPersonBase.id
        def id = personBasicPersonBase.id
        map = [domainModel: personBasicPersonBase]
        personBasicPersonBaseService.delete(map)
        assertNull "PersonBasicPersonBase should have been deleted", personBasicPersonBase.get(id)
    }


    @Test
    void testReadOnly() {
        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        def map = [domainModel: personBasicPersonBase]
        personBasicPersonBase = personBasicPersonBaseService.create(map)
        assertNotNull "PersonBasicPersonBase ID is null in PersonBasicPersonBase Service Tests Create", personBasicPersonBase.id
        personBasicPersonBase.pidm = PersonUtility.getPerson("HOS00001").pidm
        try {
            personBasicPersonBaseService.update([domainModel: personBasicPersonBase])
            fail("This should have failed with @@r1:readonlyFieldsCannotBeModified")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }


    @Test
    void testSsnTooLong() {
        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        personBasicPersonBase.ssn = "TTTTTTTTTTTTTTTTTTTT"

        try {
            personBasicPersonBaseService.create([domainModel: personBasicPersonBase])
            fail("This should have failed with @@r1:ssnMaximumLengthExceeded")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "ssnMaximumLengthExceeded"
        }
    }

    @Test
    void testGetPersonalDetails() {
        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        def map = [domainModel: personBasicPersonBase]
        personBasicPersonBase = personBasicPersonBaseService.create(map)
        assertNotNull "PersonBasicPersonBase ID is null in PersonBasicPersonBase Service Tests Create", personBasicPersonBase.id

        def details = personBasicPersonBaseService.getPersonalDetails(personBasicPersonBase.pidm)
        assertEquals i_success_birthDate.day, details.birthDate.day
        assertEquals i_success_birthDate.month, details.birthDate.month
        assertEquals i_success_birthDate.year, details.birthDate.year
        assertEquals i_success_sex, details.gender
        assertEquals i_success_preferenceFirstName, details.preferenceFirstName
        assertEquals i_success_maritalStatus, details.maritalStatus
        assertEquals '1', details.ethnic
    }


    private def newValidForCreatePersonBasicPersonBase() {
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
        def bannerValues = sql.firstRow(idSql)
        def ibannerId = bannerValues.bannerId
        def ipidm = bannerValues.pidm
        def person = new PersonIdentificationName(
                pidm: ipidm,
                bannerId: ibannerId,
                lastName: "TTTTT",
                firstName: "TTTTT",
                middleName: "TTTTT",
                changeIndicator: null,
                entityIndicator: "P"
        )
        person.save(flush: true, failOnError: true)
        assert person.id

        def unitOfMeasure = newUnitOfMeasure()
        unitOfMeasure.save(failOnError: true, flush: true)

        def personBasicPersonBase = new PersonBasicPersonBase(
                pidm: ipidm,
                ssn: i_success_ssn,
                birthDate: i_success_birthDate,
                sex: i_success_sex,
                confidIndicator: i_success_confidIndicator,
                deadIndicator: i_success_deadIndicator,
                vetcFileNumber: i_success_vetcFileNumber,
                legalName: i_success_legalName,
                preferenceFirstName: i_success_preferenceFirstName,
                namePrefix: i_success_namePrefix,
                nameSuffix: i_success_nameSuffix,
                veraIndicator: i_success_veraIndicator,
                citizenshipIndicator: i_success_citizenshipIndicator,
                deadDate: i_success_deadDate,
                hair: i_success_hair,
                eyeColor: i_success_eyeColor,
                cityBirth: i_success_cityBirth,
                driverLicense: i_success_driverLicense,
                height: i_success_height,
                weight: i_success_weight,
                sdvetIndicator: i_success_sdvetIndicator,
                licenseIssuedDate: i_success_licenseIssuedDate,
                licenseExpiresDate: i_success_licenseExpiresDate,
                incarcerationIndicator: i_success_incarcerationIndicator,
                itin: i_success_itin,
                activeDutySeprDate: i_success_activeDutySeprDate,
                ethnic: i_success_ethnic,
                confirmedRe: i_success_confirmedRe,
                confirmedReDate: i_success_confirmedReDate,
                armedServiceMedalVetIndicator: i_success_armedServiceMedalVetIndicator,
                legacy: i_success_legacy,
                ethnicity: i_success_ethnicity,
                maritalStatus: i_success_maritalStatus,
                religion: i_success_religion,
                citizenType: i_success_citizenType,
                stateBirth: i_success_stateBirth,
                stateDriver: i_success_stateDriver,
                nationDriver: i_success_nationDriver,
                unitOfMeasureHeight: UnitOfMeasure.findByCode("LB"),
                unitOfMeasureWeight: UnitOfMeasure.findByCode("LB")
        )
        return personBasicPersonBase
    }


    private def newInvalidForCreatePersonBasicPersonBase() {
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
        def bannerValues = sql.firstRow(idSql)
        def ibannerId = bannerValues.bannerId
        def ipidm = bannerValues.pidm
        def person = new PersonIdentificationName(
                pidm: ipidm,
                bannerId: ibannerId,
                lastName: "TTTTT",
                firstName: "TTTTT",
                middleName: "TTTTT",
                changeIndicator: null,
                entityIndicator: "P"
        )
        person.save(flush: true, failOnError: true)
        assert person.id

        def unitOfMeasure = newUnitOfMeasure()
        unitOfMeasure.save(failOnError: true, flush: true)

        def personBasicPersonBase = new PersonBasicPersonBase(
                pidm: ipidm,
                ssn: i_failure_ssn,
                birthDate: i_failure_birthDate,
                sex: i_failure_sex,
                confidIndicator: i_failure_confidIndicator,
                deadIndicator: i_failure_deadIndicator,
                vetcFileNumber: i_failure_vetcFileNumber,
                legalName: i_failure_legalName,
                preferenceFirstName: i_failure_preferenceFirstName,
                namePrefix: i_failure_namePrefix,
                nameSuffix: i_failure_nameSuffix,
                veraIndicator: i_failure_veraIndicator,
                citizenshipIndicator: i_failure_citizenshipIndicator,
                deadDate: i_failure_deadDate,
                hair: i_failure_hair,
                eyeColor: i_failure_eyeColor,
                cityBirth: i_failure_cityBirth,
                driverLicense: i_failure_driverLicense,
                height: i_failure_height,
                weight: i_failure_weight,
                sdvetIndicator: i_failure_sdvetIndicator,
                licenseIssuedDate: i_failure_licenseIssuedDate,
                licenseExpiresDate: i_failure_licenseExpiresDate,
                incarcerationIndicator: i_failure_incarcerationIndicator,
                itin: i_failure_itin,
                activeDutySeprDate: i_failure_activeDutySeprDate,
                ethnic: i_failure_ethnic,
                confirmedRe: i_failure_confirmedRe,
                confirmedReDate: i_failure_confirmedReDate,
                armedServiceMedalVetIndicator: i_failure_armedServiceMedalVetIndicator,
                legacy: i_failure_legacy,
                ethnicity: i_failure_ethnicity,
                maritalStatus: i_failure_maritalStatus,
                religion: i_failure_religion,
                citizenType: i_failure_citizenType,
                stateBirth: i_failure_stateBirth,
                stateDriver: i_failure_stateDriver,
                nationDriver: i_failure_nationDriver,
                unitOfMeasureHeight: UnitOfMeasure.findByCode("LB"),
                unitOfMeasureWeight: UnitOfMeasure.findByCode("LB")
        )
        return personBasicPersonBase
    }


    private def newUnitOfMeasure() {
        def unitOfMeasure = new UnitOfMeasure(
                code: "LB",
                description: "Pounds",
                lastModified: new Date(),
                lastModifiedBy: "test"
        )
        return unitOfMeasure
    }
}
