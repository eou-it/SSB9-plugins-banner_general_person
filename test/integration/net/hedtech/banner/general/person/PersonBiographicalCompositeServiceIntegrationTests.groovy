/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
/** *******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.general.system.*
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonBiographicalCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def personBiographicalCompositeService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCreateBiographical() {
        def personIdentificationNameCurrent = setupNewPersonIdentificationNameCurrent()

        def personBasicPersonBase = newPersonBasicPersonBase(personIdentificationNameCurrent)

        def personRaces = []

        personRaces << new PersonRace(
                pidm: personIdentificationNameCurrent.pidm,
                race: "MOA")

        personRaces << new PersonRace(
                pidm: personIdentificationNameCurrent.pidm,
                race: "ASI")


        personBiographicalCompositeService.createOrUpdate([personBasicPersonBase: personBasicPersonBase,
                personRaces: personRaces])

        personBasicPersonBase = PersonBasicPersonBase.fetchByPidm(personIdentificationNameCurrent.pidm)
        assertNotNull personBasicPersonBase
        assertEquals "TTTTT", personBasicPersonBase.ssn
        assertEquals "BR", personBasicPersonBase.hair

        personRaces = PersonRace.fetchByPidm(personIdentificationNameCurrent.pidm)
        assertEquals 2, personRaces.size()
    }


    @Test
    void testUpdateBiographical() {
        def personIdentificationNameCurrent = setupNewPersonIdentificationNameCurrent()

        def personBasicPersonBase = newPersonBasicPersonBase(personIdentificationNameCurrent)

        def personRaces = []

        personRaces << new PersonRace(
                pidm: personIdentificationNameCurrent.pidm,
                race: "MOA")

        personRaces << new PersonRace(
                pidm: personIdentificationNameCurrent.pidm,
                race: "ASI")


        personBiographicalCompositeService.createOrUpdate([personBasicPersonBase: personBasicPersonBase,
                personRaces: personRaces])

        personBasicPersonBase = PersonBasicPersonBase.fetchByPidm(personIdentificationNameCurrent.pidm)
        assertNotNull personBasicPersonBase
        assertEquals "TTTTT", personBasicPersonBase.ssn
        assertEquals "BR", personBasicPersonBase.hair

        personRaces = PersonRace.fetchByPidm(personIdentificationNameCurrent.pidm)
        assertEquals 2, personRaces.size()

        // Update values
        personBasicPersonBase.hair = "BL"
        personBasicPersonBase.eyeColor = "BL"
        personBasicPersonBase.cityBirth = "Bristol"
        personBasicPersonBase.driverLicense = "UUUUU"
        personBasicPersonBase.height = 2
        personBasicPersonBase.weight = 2

        personRaces << new PersonRace(
                pidm: personIdentificationNameCurrent.pidm,
                race: "IND")

        personBiographicalCompositeService.createOrUpdate([personBasicPersonBase: personBasicPersonBase,
                personRaces: personRaces])

        personBasicPersonBase = PersonBasicPersonBase.fetchByPidm(personIdentificationNameCurrent.pidm)
        assertNotNull personBasicPersonBase
        assertEquals "UUUUU", personBasicPersonBase.driverLicense
        assertEquals "BL", personBasicPersonBase.hair
        assertEquals "BL", personBasicPersonBase.eyeColor
        assertEquals "Bristol", personBasicPersonBase.cityBirth
        assertEquals 2, personBasicPersonBase.height
        assertEquals 2, personBasicPersonBase.weight

        personRaces = PersonRace.fetchByPidm(personIdentificationNameCurrent.pidm)
        assertEquals 3, personRaces.size()
    }


    @Test
    void testDeleteBiographical() {
        def personIdentificationNameCurrent = setupNewPersonIdentificationNameCurrent()

        def personBasicPersonBase = newPersonBasicPersonBase(personIdentificationNameCurrent)

        def personRaces = []

        personRaces << new PersonRace(
                pidm: personIdentificationNameCurrent.pidm,
                race: "MOA")

        personRaces << new PersonRace(
                pidm: personIdentificationNameCurrent.pidm,
                race: "ASI")


        personBiographicalCompositeService.createOrUpdate([personBasicPersonBase: personBasicPersonBase,
                personRaces: personRaces])

        personBasicPersonBase = PersonBasicPersonBase.fetchByPidm(personIdentificationNameCurrent.pidm)
        assertNotNull personBasicPersonBase
        assertEquals "TTTTT", personBasicPersonBase.ssn
        assertEquals "BR", personBasicPersonBase.hair

        personRaces = PersonRace.fetchByPidm(personIdentificationNameCurrent.pidm)
        assertEquals 2, personRaces.size()

        personBiographicalCompositeService.createOrUpdate([deletePersonBasicPersonBase: personBasicPersonBase,
                deletePersonRaces: personRaces])

        personBasicPersonBase = PersonBasicPersonBase.fetchByPidm(personIdentificationNameCurrent.pidm)
        assertNull personBasicPersonBase

        personRaces = PersonRace.fetchByPidm(personIdentificationNameCurrent.pidm)
        assertEquals 0, personRaces.size()
    }

    /**
     * Creates a new person current id record.
     */
    private def setupNewPersonIdentificationNameCurrent() {

        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
        def bannerValues = sql.firstRow(idSql)
        def bannerId = bannerValues.bannerId
        def pidm = bannerValues.pidm

        def person = new PersonIdentificationNameCurrent(
                pidm: pidm,
                bannerId: bannerId,
                lastName: "Adams",
                firstName: "Troy",
                middleName: "W",
                changeIndicator: null,
                entityIndicator: "P",
                nameType: NameType.findByCode("BRTH"))

        person.save(failOnError: true, flush: true)
        assertNotNull person?.id

        return person
    }


    private def newPersonBasicPersonBase(personIdentificationCurrent) {
        def unitOfMeasure = newUnitOfMeasure()
        unitOfMeasure.save(failOnError: true, flush: true)

        def personBasicPersonBase = new PersonBasicPersonBase(
                pidm: personIdentificationCurrent.pidm,
                ssn: "TTTTT",
                birthDate: new Date(),
                sex: "M",
                confidIndicator: "N",
                deadIndicator: null,
                vetcFileNumber: "TTTTT",
                legalName: "TTTTT",
                preferenceFirstName: "TTTTTTTTTT",
                namePrefix: "TT",
                nameSuffix: "TT",
                veraIndicator: "V",
                citizenshiopIndicator: "U",
                deadDate: null,
                hair: "BR",
                eyeColor: "BR",
                cityBirth: "Milton",
                driverLicense: "TTTTTT",
                height: 1,
                weight: 1,
                sdvetIndicator: null,
                licenseIssuedDate: new Date(),
                licenseExpiresDate: null,
                incarcerationIndicator: "N",
                itin: 1,
                activeDutySeprDate: new Date(),
                ethnic: "1",
                confirmedRe: "Y",
                confirmedReDate: new Date(),
                armedServiceMedalVetIndicator: true,
                legacy: Legacy.findByCode("M"),
                ethnicity: Ethnicity.findByCode("1"),
                maritalStatus: MaritalStatus.findByCode("S"),
                religion: Religion.findByCode("JE"),
                citizenType: CitizenType.findByCode("Y"),
                stateBirth: State.findByCode("DE"),
                stateDriver: State.findByCode("PA"),
                nationDriver: Nation.findByCode("157"),
                unitOfMeasureHeight: UnitOfMeasure.findByCode("LB"),
                unitOfMeasureWeight: UnitOfMeasure.findByCode("LB")
        )

        return personBasicPersonBase
    }


    private def newUnitOfMeasure() {
        def unitOfMeasure = new UnitOfMeasure(
                code: "LB",
                description: "Pounds"
        )

        return unitOfMeasure
    }

}
