/** *******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person.search

import groovy.sql.Sql
import net.hedtech.banner.general.person.PersonBasicPersonBase
import net.hedtech.banner.general.person.PersonIdentificationNameCurrent
import net.hedtech.banner.general.person.PersonRace
import net.hedtech.banner.general.system.*
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonBiographicalCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def personBiographicalCompositeService
    def personBasicPersonBaseService
    def personRaceService


    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


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
        assertEquals "TTTTT",  personBasicPersonBase.ssn
        assertEquals "BR",  personBasicPersonBase.hair

        personRaces = PersonRace.fetchByPidm(personIdentificationNameCurrent.pidm)
        assertEquals 2, personRaces.size()
    }

//    void testUpdateCurrentBannerId() {
//        def origBannerId = "ID-T00001"
//        def updateBannerId = "ID-U00001"
//
//        def person = setupNewPersonIdentificationNameCurrent(origBannerId)
//
//        // Update the person's banner id
//        person.bannerId = updateBannerId
//
//        def updatedPersonList = []
//        updatedPersonList << person
//        personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: updatedPersonList])
//
//        def currentPerson = PersonIdentificationNameCurrent.fetchByBannerId(updateBannerId)
//        assertNotNull currentPerson
//        assertEquals person.pidm, currentPerson.pidm
//        assertEquals updateBannerId, currentPerson.bannerId
//        assertEquals 1L, currentPerson.version
//
//        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
//        assertEquals 1, alternatePersons.size()
//
//        // Check the alternate id
//        def alternatePerson = alternatePersons.get(0)
//        assertEquals currentPerson.pidm, alternatePerson?.pidm
//        assertEquals origBannerId, alternatePerson.bannerId
//        assertEquals "I", alternatePerson.changeIndicator
//        assertEquals 0L, alternatePerson.version
//    }
//
//
//    void testDeleteCurrent() {
//        def person = setupNewPersonIdentificationNameCurrent("ID-T00001")
//
//        def deletePersonList = []
//        deletePersonList << person
//
//        try {
//            personIdentificationNameCompositeService.createOrUpdate([deletePersonIdentificationNameCurrents: deletePersonList])
//            fail "should have failed because you cannot delete the current id"
//        }
//        catch (ApplicationException ae) {
//            assertApplicationException ae, "Cannot delete current record."
//        }
//    }

    // *************************************************************************************************************
    //  End tests.
    // *************************************************************************************************************

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
