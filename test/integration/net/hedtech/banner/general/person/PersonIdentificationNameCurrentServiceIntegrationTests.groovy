/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
/*********************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.CitizenType
import net.hedtech.banner.general.system.Ethnicity
import net.hedtech.banner.general.system.Legacy
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.general.system.Nation
import net.hedtech.banner.general.system.Religion
import net.hedtech.banner.general.system.State
import net.hedtech.banner.general.system.UnitOfMeasure
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonIdentificationNameCurrentServiceIntegrationTests extends BaseIntegrationTestCase {

    def personIdentificationNameCurrentService

    final def GENERATED = "GENERATED"

    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }

    // *************************************************************************************************************
    //  Start person tests.  Non-person tests follow these tests.
    // *************************************************************************************************************

    void testCreatePersonIdentificationNameCurrent() {
        def bannerId = generateBannerId()
        def person = newPersonIdentificationNameCurrent(bannerId, null)
        person = personIdentificationNameCurrentService.create([domainModel: person])

        def currentPerson = PersonIdentificationNameCurrent.get(person.id)
        assertNotNull currentPerson.pidm
        assertEquals bannerId, currentPerson.bannerId
        assertEquals "Adams", currentPerson.lastName
        assertEquals "P", currentPerson.entityIndicator
        assertEquals 0L, currentPerson.version

        // Make sure no alternate person records were created.
        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 0, alternatePersons.size()
    }

    void testCreatePersonIdentificationNameCurrentWithGeneratedId() {
        def bannerId = GENERATED
        def person = newPersonIdentificationNameCurrent(bannerId, null)
        person = personIdentificationNameCurrentService.create([domainModel: person])

        def currentPerson = PersonIdentificationNameCurrent.get(person.id)
        assertNotNull currentPerson
        assertNotNull currentPerson.pidm
        assertNotNull currentPerson.bannerId
        assertFalse GENERATED == currentPerson.bannerId
        assertEquals "P", currentPerson.entityIndicator
        assertEquals 0L, currentPerson.version

        // Make sure no alternate person records were created.
        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 0, alternatePersons.size()
    }


    void testCreateWithInvalidChangeIndicator() {
        def bannerId = generateBannerId()
        def person = newPersonIdentificationNameCurrent(bannerId, null)
        person.changeIndicator = "I"

        try {
            personIdentificationNameCurrentService.create([domainModel: person])
            fail "Should have failed because you cannot create a current identification record with a non-null change indicator."
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "changeIndicatorMustBeNull"
        }
    }


    void testCreateCurrentWhereBannerIdExists() {
        def person = setupNewPersonIdentificationNameCurrent()
        def bannerId = person.bannerId

        // Create another person with the same banner id.
        try {
            def person2 = newPersonIdentificationNameCurrent(bannerId, null)
            personIdentificationNameCurrentService.create([domainModel: person2])
            fail "Should have failed because you cannot create two persons with the same banner id."
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot create new identification record, since the ID provided is in use by another person/non-person."
        }
    }


    void testUpdateCurrentBannerId() {
        def person = setupNewPersonIdentificationNameCurrent()
        def origBannerId = person.bannerId

        // Update the person's banner id
        person.bannerId = "ID-U00001"
        person = personIdentificationNameCurrentService.update([domainModel: person])

        def currentPerson = PersonIdentificationNameCurrent.get(person.id)
        assertNotNull currentPerson
        assertEquals "ID-U00001", currentPerson.bannerId
        assertEquals 1L, currentPerson.version

        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        // Check the alternate id
        def alternatePerson = alternatePersons.get(0)
        assertEquals currentPerson.pidm, alternatePerson.pidm
        assertEquals origBannerId, alternatePerson.bannerId
        assertEquals "I", alternatePerson.changeIndicator
        assertEquals 0L, alternatePerson.version
    }


    void testUpdateCurrentBannerIdToExistingBannerId() {
        def person1 = setupNewPersonIdentificationNameCurrent()
        def person2 = setupNewPersonIdentificationNameCurrent()

        try {
            person2.bannerId = person1.bannerId
            personIdentificationNameCurrentService.update([domainModel: person2])
            fail "Should have failed because you cannot update a banner id with an existing banner id."
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot update identification record, since the new ID is in use by another person/non-person."
        }
    }


    void testUpdateCurrentBannerIdToExistingSsn() {
        def person1 = setupNewPersonIdentificationNameCurrent()
        def person2 = setupNewPersonIdentificationNameCurrent()

        def bio = setupNewPersonBasicPersonBase(person1)

        try {
            person2.bannerId = bio.ssn
            personIdentificationNameCurrentService.update([domainModel: person2])
            fail "Should have failed because you cannot update a banner id with an existing ssn."
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "bannerIdExistsAsSsn"
        }
    }


    void testUpdateCurrentName() {
        def updateLastName = "UPDATED_LAST_NAME"
        def updateFirstName = "UPDATED_FIRST_NAME"
        def updateMiddleName = "UPDATED_MIDDLE_NAME"

        def person = setupNewPersonIdentificationNameCurrent()
        def origBannerId = person.bannerId
        def origLastName = person.lastName
        def origFirstName = person.firstName
        def origMiddleName = person.middleName

        // Update the person's name
        person.lastName = updateLastName
        person.firstName = updateFirstName
        person.middleName = updateMiddleName
        personIdentificationNameCurrentService.update([domainModel: person])

        // Check the current id
        def currentPerson = PersonIdentificationNameCurrent.fetchByBannerId(origBannerId)
        assertNotNull currentPerson?.pidm
        assertEquals updateLastName, currentPerson.lastName
        assertEquals updateFirstName, currentPerson.firstName
        assertEquals updateMiddleName, currentPerson.middleName
        assertEquals 1L, currentPerson.version

        // Check the alternate id
        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        def alternatePerson = alternatePersons.get(0)
        assertNotNull alternatePerson?.pidm
        assertEquals origLastName, alternatePerson.lastName
        assertEquals origFirstName, alternatePerson.firstName
        assertEquals origMiddleName, alternatePerson.middleName
        assertEquals 0L, alternatePerson.version
    }



    void testUpdateCurrentBannerIdAndName() {
        def person = setupNewPersonIdentificationNameCurrent()

        // Update the person's id and name
        person.bannerId = "ID-U00001"
        person.lastName = "UPDATED_LAST_NAME"
        person.firstName = "UPDATED_FIRST_NAME"
        person.middleName = "UPDATED_MIDDLE_NAME"

        try {
            personIdentificationNameCurrentService.update([domainModel: person])
            fail "should have failed because you cannot update both the banner id and name"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "ID and Name cannot be changed at the same time."
        }
    }


    void testUpdateCurrentNameType() {
        def person = setupNewPersonIdentificationNameCurrent()

        // Update the person's name
        person.nameType = NameType.findWhere(code: "PROF")
        personIdentificationNameCurrentService.update([domainModel: person])

        // Check the current id
        def currentPerson = PersonIdentificationNameCurrent.fetchByBannerId(person.bannerId)
        assertNotNull currentPerson
        assertEquals person.pidm, currentPerson.pidm
        assertEquals "PROF", currentPerson.nameType.code
        assertEquals 1L, currentPerson.version

        // Make sure no alternate person records were created.
        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 0, alternatePersons.size()
    }


    void testUpdateCurrentEntityIndicator() {
        def person = setupNewPersonIdentificationNameCurrent()

        // Change from person entity to non-person entity
        person.entityIndicator = "C"
        person.firstName = null

        try {
            personIdentificationNameCurrentService.update([domainModel: person])
            fail "should have failed because you cannot update the entity indicator"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot update because the Entity Indicator cannot be changed."
        }
    }


    void testUpdateCurrentNoChanges() {
        def person = setupNewPersonIdentificationNameCurrent()

        person.dataOrigin = "TEST"

        try {
            personIdentificationNameCurrentService.update([domainModel: person])
            fail "should have failed because you cannot update both the banner id and name"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Must update at least ID, Name, or Name Type."
        }
    }


    void testUpdateReadOnlyFields() {
        def person = setupNewPersonIdentificationNameCurrent()

        person.changeIndicator = "N"

        try {
            personIdentificationNameCurrentService.update([domainModel: person])
            fail "This should have failed with @@r1:readonlyFieldsCannotBeModified"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }


    void testDeleteCurrent() {
        def person = setupNewPersonIdentificationNameCurrent()

        try {
            personIdentificationNameCurrentService.delete([domainModel: person])
            fail "should have failed because you cannot delete the current id"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot delete current record."
        }
    }
    // *************************************************************************************************************
    //  Start non-person tests.
    // *************************************************************************************************************

    void testCreateCurrentNonPersonIdentificationName() {
        def bannerId = generateBannerId()
        def nonPerson = newNonPersonIdentificationNameCurrent(bannerId, null)
        personIdentificationNameCurrentService.create([domainModel: nonPerson])

        def currentNonPerson = PersonIdentificationNameCurrent.fetchByBannerId(bannerId)
        assertNotNull currentNonPerson.pidm
        assertEquals bannerId, currentNonPerson.bannerId
        assertEquals "Acme Rocket and Anvils", currentNonPerson.lastName
        assertNull currentNonPerson.firstName
        assertEquals "C", currentNonPerson.entityIndicator
        assertEquals "CORP", currentNonPerson.nameType.code
        assertEquals 0L, currentNonPerson.version

        // Make sure no alternate person records were created.
        def alternateNonPersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentNonPerson.pidm)
        assertEquals 0, alternateNonPersons.size()
    }


    void testUpdateCurrentNonPersonBannerId() {
        def nonPerson = setupNewNonPersonIdentificationNameCurrent()
        def origBannerId = nonPerson.bannerId

        // Update the non-person's banner id
        nonPerson.bannerId = "ID-U00001"
        nonPerson = personIdentificationNameCurrentService.update([domainModel: nonPerson])

        def currentNonPerson = PersonIdentificationNameCurrent.get(nonPerson.id)
        assertNotNull currentNonPerson
        assertEquals nonPerson.pidm, currentNonPerson.pidm
        assertEquals "ID-U00001", currentNonPerson.bannerId
        assertEquals 1L, currentNonPerson.version

        // Check the alternate id
        def alternateNonPersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentNonPerson.pidm)
        assertEquals 1, alternateNonPersons.size()

        def alternateNonPerson = alternateNonPersons.get(0)
        assertEquals currentNonPerson.pidm, alternateNonPerson.pidm
        assertEquals origBannerId, alternateNonPerson.bannerId
        assertEquals "I", alternateNonPerson.changeIndicator
        assertEquals 0L, alternateNonPerson.version
    }


    void testUpdateCurrentNonPersonName() {
        def updateLastName = "UPDATED_LAST_NAME"

        def person = setupNewNonPersonIdentificationNameCurrent()
        def origBannerId = person.bannerId
        def origLastName = person.lastName

        // Update the person's name
        person.lastName = updateLastName
        personIdentificationNameCurrentService.update([domainModel: person])

        // Check the current id
        def currentPerson = PersonIdentificationNameCurrent.fetchByBannerId(origBannerId)
        assertNotNull currentPerson?.pidm
        assertEquals updateLastName, currentPerson.lastName
        assertEquals 1L, currentPerson.version

        // Check the alternate id
        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        def alternatePerson = alternatePersons.get(0)
        assertNotNull alternatePerson?.pidm
        assertEquals origLastName, alternatePerson.lastName
        assertEquals 0L, alternatePerson.version
    }


    // *************************************************************************************************************
    //  End tests.
    // *************************************************************************************************************

    /**
     * Setup a new PersonIdentificationNameCurrent for testing.
     */
    private def setupNewPersonIdentificationNameCurrent() {
        def personIdentificationNameCurrent = newPersonIdentificationNameCurrent(generateBannerId(), generatePidm())

        save personIdentificationNameCurrent
        assertNotNull personIdentificationNameCurrent.id
        assertNotNull personIdentificationNameCurrent.bannerId
        assertNotNull personIdentificationNameCurrent.pidm
        assertEquals 0L, personIdentificationNameCurrent.version
        return personIdentificationNameCurrent
    }

    /**
     * Setup a new PersonIdentificationNameCurrent for testing.
     */
    private def setupNewNonPersonIdentificationNameCurrent() {
        def personIdentificationNameCurrent = newNonPersonIdentificationNameCurrent(generateBannerId(), generatePidm())

        save personIdentificationNameCurrent
        assertNotNull personIdentificationNameCurrent.id
        assertNotNull personIdentificationNameCurrent.bannerId
        assertNotNull personIdentificationNameCurrent.pidm
        assertEquals 0L, personIdentificationNameCurrent.version
        return personIdentificationNameCurrent
    }


    private def newPersonIdentificationNameCurrent(bannerId, pidm) {
        def personIdentificationNameCurrent = new PersonIdentificationNameCurrent(
                bannerId: bannerId,
                pidm: pidm,
                lastName: "Adams",
                firstName: "Troy",
                middleName: "W",
                changeIndicator: null,
                entityIndicator: "P",
                nameType: NameType.findByCode("BRTH")
        )

        return personIdentificationNameCurrent
    }


    private def newNonPersonIdentificationNameCurrent(bannerId, pidm) {
        def personIdentificationNameCurrent = new PersonIdentificationNameCurrent(
                bannerId: bannerId,
                pidm: pidm,
                lastName: "Acme Rocket and Anvils",
                middleName: "W",
                changeIndicator: null,
                entityIndicator: "C",
                nameType: NameType.findByCode("CORP")
        )

        return personIdentificationNameCurrent
    }


    private def generateBannerId() {
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        String idSql = """select gb_common.f_generate_id bannerId from dual """
        def bannerValues = sql.firstRow(idSql)

        return bannerValues.bannerId
    }


    private def generatePidm() {
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        String idSql = """select gb_common.f_generate_pidm pidm from dual """
        def bannerValues = sql.firstRow(idSql)

        return bannerValues.pidm
    }

    private def setupNewPersonBasicPersonBase(personIdentificationCurrent) {
        def unitOfMeasure = UnitOfMeasure.findByCode("LB")
        if (!unitOfMeasure?.id) {
            unitOfMeasure = newUnitOfMeasure()
            unitOfMeasure.save(failOnError: true, flush: true)
        }

        def personBasicPersonBase = new PersonBasicPersonBase(
                pidm: personIdentificationCurrent.pidm,
                ssn: "SSN-0001",
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

        save personBasicPersonBase
        assertNotNull personBasicPersonBase?.id

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
