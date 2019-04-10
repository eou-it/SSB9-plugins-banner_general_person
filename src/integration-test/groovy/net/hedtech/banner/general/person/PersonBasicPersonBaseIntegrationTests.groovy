/*******************************************************************************
 Copyright 2012-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.junit.Before
import org.junit.Test
import org.junit.After

import grails.validation.ValidationException
import groovy.sql.Sql
import static groovy.test.GroovyAssert.*
import net.hedtech.banner.general.system.*
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

@Integration
@Rollback
class PersonBasicPersonBaseIntegrationTests extends BaseIntegrationTestCase {

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
    def i_success_unitOfMeasureHeight
    def i_success_unitOfMeasureWeight

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
    def i_success_itin = 1
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
    def i_failure_unitOfMeasureHeight
    def i_failure_unitOfMeasureWeight

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
    def i_failure_itin = 1
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

    def u_success_ssn = "TTTTT"
    def u_success_birthDate = new Date()
    def u_success_sex = "F"
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
    def u_success_itin = 1
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

    def u_failure_ssn = "TTTTT"
    def u_failure_birthDate = new Date()
    def u_failure_sex = "F"
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
    def u_failure_itin = 1
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
    void testCreateValidPersonBasicPersonBase() {
        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        personBasicPersonBase.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull personBasicPersonBase.id
    }


    @Test
    void testCalculateAge() {
        def df = new SimpleDateFormat("MM/dd/yyyy");
        def birthDate = new java.sql.Date(df.parse("10/17/1966").getTime());
        def deceasedDate = new java.sql.Date(df.parse("07/15/2013").getTime());

        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        personBasicPersonBase.birthDate = birthDate
        personBasicPersonBase.deadIndicator = "Y"
        personBasicPersonBase.deadDate = deceasedDate
        personBasicPersonBase.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull personBasicPersonBase.id
        assertEquals 46, personBasicPersonBase.calculateAge()

        personBasicPersonBase.birthDate = null
        personBasicPersonBase.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull personBasicPersonBase.id
        assertNull personBasicPersonBase.calculateAge()
    }


    @Test
    void testCreateInvalidPersonBasicPersonBase() {
        def personBasicPersonBase = newInvalidForCreatePersonBasicPersonBase()
        shouldFail(ValidationException) {
            personBasicPersonBase.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidPersonBasicPersonBase() {
        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        personBasicPersonBase.save(failOnError: true, flush: true)
        assertNotNull personBasicPersonBase.id
        assertEquals 0L, personBasicPersonBase.version
        assertEquals i_success_ssn, personBasicPersonBase.ssn
        assertEquals i_success_birthDate, personBasicPersonBase.birthDate
        assertEquals i_success_sex, personBasicPersonBase.sex
        assertEquals i_success_confidIndicator, personBasicPersonBase.confidIndicator
        assertEquals i_success_deadIndicator, personBasicPersonBase.deadIndicator
        assertEquals i_success_vetcFileNumber, personBasicPersonBase.vetcFileNumber
        assertEquals i_success_legalName, personBasicPersonBase.legalName
        assertEquals i_success_preferenceFirstName, personBasicPersonBase.preferenceFirstName
        assertEquals i_success_namePrefix, personBasicPersonBase.namePrefix
        assertEquals i_success_nameSuffix, personBasicPersonBase.nameSuffix
        assertEquals i_success_veraIndicator, personBasicPersonBase.veraIndicator
        assertEquals i_success_citizenshipIndicator, personBasicPersonBase.citizenshipIndicator
        assertEquals i_success_deadDate, personBasicPersonBase.deadDate
        assertEquals i_success_hair, personBasicPersonBase.hair
        assertEquals i_success_eyeColor, personBasicPersonBase.eyeColor
        assertEquals i_success_cityBirth, personBasicPersonBase.cityBirth
        assertEquals i_success_driverLicense, personBasicPersonBase.driverLicense
        assertEquals i_success_height, personBasicPersonBase.height
        assertEquals i_success_weight, personBasicPersonBase.weight
        assertEquals i_success_sdvetIndicator, personBasicPersonBase.sdvetIndicator
        assertEquals i_success_licenseIssuedDate, personBasicPersonBase.licenseIssuedDate
        assertEquals i_success_licenseExpiresDate, personBasicPersonBase.licenseExpiresDate
        assertEquals i_success_incarcerationIndicator, personBasicPersonBase.incarcerationIndicator
        assertEquals i_success_itin, personBasicPersonBase.itin
        assertEquals i_success_activeDutySeprDate, personBasicPersonBase.activeDutySeprDate
        assertEquals i_success_ethnic, personBasicPersonBase.ethnic
        assertEquals i_success_confirmedRe, personBasicPersonBase.confirmedRe
        assertEquals i_success_confirmedReDate, personBasicPersonBase.confirmedReDate
        assertEquals i_success_armedServiceMedalVetIndicator, personBasicPersonBase.armedServiceMedalVetIndicator

        //Update the entity
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
        personBasicPersonBase.save(failOnError: true, flush: true)
        //Assert for sucessful update
        personBasicPersonBase = PersonBasicPersonBase.get(personBasicPersonBase.id)
        assertEquals 1L, personBasicPersonBase?.version
        assertEquals u_success_ssn, personBasicPersonBase.ssn
        assertEquals u_success_birthDate, personBasicPersonBase.birthDate
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
        assertEquals u_success_deadDate, personBasicPersonBase.deadDate
        assertEquals u_success_hair, personBasicPersonBase.hair
        assertEquals u_success_eyeColor, personBasicPersonBase.eyeColor
        assertEquals u_success_cityBirth, personBasicPersonBase.cityBirth
        assertEquals u_success_driverLicense, personBasicPersonBase.driverLicense
        assertEquals u_success_height, personBasicPersonBase.height
        assertEquals u_success_weight, personBasicPersonBase.weight
        assertEquals u_success_sdvetIndicator, personBasicPersonBase.sdvetIndicator
        assertEquals u_success_licenseIssuedDate, personBasicPersonBase.licenseIssuedDate
        assertEquals u_success_licenseExpiresDate, personBasicPersonBase.licenseExpiresDate
        assertEquals u_success_incarcerationIndicator, personBasicPersonBase.incarcerationIndicator
        assertEquals u_success_itin, personBasicPersonBase.itin
        assertEquals u_success_activeDutySeprDate, personBasicPersonBase.activeDutySeprDate
        assertEquals u_success_ethnic, personBasicPersonBase.ethnic
        assertEquals u_success_confirmedRe, personBasicPersonBase.confirmedRe
        assertEquals u_success_confirmedReDate, personBasicPersonBase.confirmedReDate
        assertEquals u_success_armedServiceMedalVetIndicator, personBasicPersonBase.armedServiceMedalVetIndicator
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
    }


    @Test
    void testUpdateInvalidPersonBasicPersonBase() {
        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        personBasicPersonBase.save(failOnError: true, flush: true)
        assertNotNull personBasicPersonBase.id
        assertEquals 0L, personBasicPersonBase.version
        assertEquals i_success_ssn, personBasicPersonBase.ssn
        assertEquals i_success_birthDate, personBasicPersonBase.birthDate
        assertEquals i_success_sex, personBasicPersonBase.sex
        assertEquals i_success_confidIndicator, personBasicPersonBase.confidIndicator
        assertEquals i_success_deadIndicator, personBasicPersonBase.deadIndicator
        assertEquals i_success_vetcFileNumber, personBasicPersonBase.vetcFileNumber
        assertEquals i_success_legalName, personBasicPersonBase.legalName
        assertEquals i_success_preferenceFirstName, personBasicPersonBase.preferenceFirstName
        assertEquals i_success_namePrefix, personBasicPersonBase.namePrefix
        assertEquals i_success_nameSuffix, personBasicPersonBase.nameSuffix
        assertEquals i_success_veraIndicator, personBasicPersonBase.veraIndicator
        assertEquals i_success_citizenshipIndicator, personBasicPersonBase.citizenshipIndicator
        assertEquals i_success_deadDate, personBasicPersonBase.deadDate
        assertEquals i_success_hair, personBasicPersonBase.hair
        assertEquals i_success_eyeColor, personBasicPersonBase.eyeColor
        assertEquals i_success_cityBirth, personBasicPersonBase.cityBirth
        assertEquals i_success_driverLicense, personBasicPersonBase.driverLicense
        assertEquals i_success_height, personBasicPersonBase.height
        assertEquals i_success_weight, personBasicPersonBase.weight
        assertEquals i_success_sdvetIndicator, personBasicPersonBase.sdvetIndicator
        assertEquals i_success_licenseIssuedDate, personBasicPersonBase.licenseIssuedDate
        assertEquals i_success_licenseExpiresDate, personBasicPersonBase.licenseExpiresDate
        assertEquals i_success_incarcerationIndicator, personBasicPersonBase.incarcerationIndicator
        assertEquals i_success_itin, personBasicPersonBase.itin
        assertEquals i_success_activeDutySeprDate, personBasicPersonBase.activeDutySeprDate
        assertEquals i_success_ethnic, personBasicPersonBase.ethnic
        assertEquals i_success_confirmedRe, personBasicPersonBase.confirmedRe
        assertEquals i_success_confirmedReDate, personBasicPersonBase.confirmedReDate
        assertEquals i_success_armedServiceMedalVetIndicator, personBasicPersonBase.armedServiceMedalVetIndicator

        //Update the entity with invalid values
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
        shouldFail(ValidationException) {
            personBasicPersonBase.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()

        personBasicPersonBase.birthDate = new Date()
        personBasicPersonBase.deadDate = new Date()
        personBasicPersonBase.licenseIssuedDate = new Date()
        personBasicPersonBase.licenseExpiresDate = new Date()
        personBasicPersonBase.activeDutySeprDate = new Date()
        personBasicPersonBase.confirmedReDate = new Date()
        personBasicPersonBase.lastModified = new Date();

        personBasicPersonBase.save(flush: true, failOnError: true)
        personBasicPersonBase.refresh()
        assertNotNull "PersonBasicPersonBase should have been saved", personBasicPersonBase.id

        // test date values -
        assertEquals date.format(today), date.format(personBasicPersonBase.lastModified)
       // assertEquals hour.format(today), hour.format(personBasicPersonBase.lastModified)

        assertEquals time.format(personBasicPersonBase.birthDate), "000000"
        assertEquals time.format(personBasicPersonBase.deadDate), "000000"
//    	assertEquals time.format(personBasicPersonBase.licenseIssuedDate), "000000"
//    	assertEquals time.format(personBasicPersonBase.licenseExpiresDate), "000000"
        assertEquals time.format(personBasicPersonBase.activeDutySeprDate), "000000"
        assertEquals time.format(personBasicPersonBase.confirmedReDate), "000000"

    }


    @Test
    void testOptimisticLock() {
        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        personBasicPersonBase.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SV_SPBPERS set SPBPERS_VERSION = 999 where SPBPERS_SURROGATE_ID = ?", [personBasicPersonBase.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
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
        shouldFail(HibernateOptimisticLockingFailureException) {
            personBasicPersonBase.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeletePersonBasicPersonBase() {
        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        personBasicPersonBase.save(failOnError: true, flush: true)
        def id = personBasicPersonBase.id
        assertNotNull id
        personBasicPersonBase.delete()
        assertNull PersonBasicPersonBase.get(id)
    }


    @Test
    void testValidation() {
        def personBasicPersonBase = newInvalidForCreatePersonBasicPersonBase()
        assertFalse "PersonBasicPersonBase could not be validated as expected due to ${personBasicPersonBase.errors}", personBasicPersonBase.validate()
    }


    @Test
    void testNullValidationFailure() {
        def personBasicPersonBase = new PersonBasicPersonBase()
        assertFalse "PersonBasicPersonBase should have failed validation", personBasicPersonBase.validate()
        assertErrorsFor personBasicPersonBase, 'nullable',
                [
                        'pidm',
                        'armedServiceMedalVetIndicator'
                ]
        assertNoErrorsFor personBasicPersonBase,
                [
                        'ssn',
                        'birthDate',
                        'sex',
                        'confidIndicator',
                        'deadIndicator',
                        'vetcFileNumber',
                        'legalName',
                        'preferenceFirstName',
                        'namePrefix',
                        'nameSuffix',
                        'veraIndicator',
                        'citizenshipIndicator',
                        'deadDate',
                        'hair',
                        'eyeColor',
                        'cityBirth',
                        'driverLicense',
                        'height',
                        'weight',
                        'sdvetIndicator',
                        'licenseIssuedDate',
                        'licenseExpiresDate',
                        'incarcerationIndicator',
                        'itin',
                        'activeDutySeprDate',
                        'ethnic',
                        'confirmedRe',
                        'confirmedReDate',
                        'legacy',
                        'ethnicity',
                        'maritalStatus',
                        'religion',
                        'citizenType',
                        'stateBirth',
                        'stateDriver',
                        'nationDriver',
                        'unitOfMeasureHeight',
                        'unitOfMeasureWeight'
                ]
    }


    @Test
    void testMaxSizeValidationFailures() {
        def personBasicPersonBase = new PersonBasicPersonBase(
                ssn: 'XXXXXXXXXXXXXXXXX',
                sex: 'XXX',
                confidIndicator: 'XXX',
                deadIndicator: 'XXX',
                vetcFileNumber: 'XXXXXXXXXXXX',
                legalName: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                preferenceFirstName: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                namePrefix: 'XXXXXXXXXXXXXXXXXXXXXX',
                nameSuffix: 'XXXXXXXXXXXXXXXXXXXXXX',
                veraIndicator: 'XXX',
                citizenshipIndicator: 'XXX',
                hair: 'XXXX',
                eyeColor: 'XXXX',
                cityBirth: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                driverLicense: 'XXXXXXXXXXXXXXXXXXXXXX',
                sdvetIndicator: 'XXX',
                incarcerationIndicator: 'XXX',
                ethnic: 'XXX',
                confirmedRe: 'XXX')
        assertFalse "PersonBasicPersonBase should have failed validation", personBasicPersonBase.validate()
        assertErrorsFor personBasicPersonBase, 'maxSize', ['ssn', 'sex', 'confidIndicator', 'deadIndicator', 'vetcFileNumber', 'legalName', 'preferenceFirstName', 'namePrefix', 'nameSuffix', 'veraIndicator', 'citizenshipIndicator', 'hair', 'eyeColor', 'cityBirth', 'driverLicense', 'sdvetIndicator', 'incarcerationIndicator', 'ethnic', 'confirmedRe']
    }


    @Test
    void testFindByPidm() {
        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        personBasicPersonBase.save(failOnError: true, flush: true)
        assertNotNull personBasicPersonBase.id

        def newPersonBasicPersonBase = PersonBasicPersonBase.fetchByPidm(personBasicPersonBase.pidm)
        //Test if the generated entity now has an id assigned
        assertNotNull newPersonBasicPersonBase.id
    }


    @Test
    void testFetchByPidmList() {
        def personList = []
        (0..5).each {
            def person = newValidForCreatePersonBasicPersonBase()
            person.save(failOnError: true, flush: true)
            personList.add(person)
        }
        def result = PersonBasicPersonBase.fetchByPidmList(personList.pidm)

        assertFalse result.empty
        assertTrue result.size() > 1
        assertTrue result[0] instanceof PersonBasicPersonBase
    }


    @Test
    void testFetchBySsn() {
        def personBasicPersonBase1 = newValidForCreatePersonBasicPersonBase()
        personBasicPersonBase1.save(failOnError: true, flush: true)
        assertNotNull personBasicPersonBase1.id

        def personBasicPersonBase2 = newValidForCreatePersonBasicPersonBase()
        personBasicPersonBase2.save(failOnError: true, flush: true)
        assertNotNull personBasicPersonBase2.id

        def bioList = PersonBasicPersonBase.fetchBySsn(personBasicPersonBase1.ssn)

        assertEquals 2, bioList.size()
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

        def unitOfMeasure = UnitOfMeasure.findByCode("LB")
        if (!unitOfMeasure?.id) {
            unitOfMeasure = newUnitOfMeasure()
            unitOfMeasure.save(failOnError: true, flush: true)
        }

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

    @Test
    void testFetchSurveyConfirmedFlagByPidm() {
        def personBasicPersonBase = newValidForCreatePersonBasicPersonBase()
        personBasicPersonBase.save(failOnError: true, flush: true)
        assertNotNull personBasicPersonBase.id

        String confirmedFlag = PersonBasicPersonBase.fetchSurveyConfirmedFlagByPidm(personBasicPersonBase.pidm)
        assertNotNull confirmedFlag
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
