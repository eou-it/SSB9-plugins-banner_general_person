/** *******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonIdentificationNameCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def personIdentificationNameCompositeService
    def personIdentificationNameService


    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }

    // *************************************************************************************************************
    //  Perform current ID tests first.  Alternate Id tests follow these tests.
    // *************************************************************************************************************

    void testCreateCurrentPersonIdentificationName() {
        def bannerId = "ID-T00001"
        def personList = []

        def person = newPersonIdentificationName(bannerId)
        personList << person
        personIdentificationNameCompositeService.createOrUpdate([currentPersonIdentificationNames: personList])

        def currentPerson = PersonIdentificationName.fetchBannerPerson(bannerId)
        assertNotNull currentPerson.pidm
        assertEquals "Adams", currentPerson.lastName
        assertEquals "P", currentPerson.entityIndicator
        assertEquals 0L, currentPerson.version

        // Make sure no alternate person records were created.
        def alternatePersons = PersonIdentificationName.fetchAllPersonByPidmAndChangeIndicatorNotNull(currentPerson.pidm)
        assertEquals 0, alternatePersons.size()
    }


    void testCreateCurrentNonPersonIdentificationName() {
        def bannerId = "ID-T00001"
        def personList = []

        def person = newNonPersonIdentificationName(bannerId)
        personList << person
        personIdentificationNameCompositeService.createOrUpdate([currentPersonIdentificationNames: personList])

        def currentPerson = PersonIdentificationName.fetchBannerNonPerson(bannerId)
        assertNotNull currentPerson.pidm
        assertEquals "Acme Rocket and Anvils", currentPerson.lastName
        assertNull currentPerson.firstName
        assertEquals "C", currentPerson.entityIndicator
        assertEquals "CORP", currentPerson.nameType.code
        assertEquals 0L, currentPerson.version

        // Make sure no alternate person records were created.
        def alternatePersons = PersonIdentificationName.fetchAllPersonByPidmAndChangeIndicatorNotNull(currentPerson.pidm)
        assertEquals 0, alternatePersons.size()
    }


    void testCreateCurrentPersonIdentificationNameWithGeneratedId() {
//        def personList = []
//
//        def person = newPersonIdentificationName("GENERATED")
//        personList << person
//        personIdentificationNameCompositeService.createOrUpdate([currentPersonIdentificationNames: personList])
//
//        def persons = PersonIdentificationName.findAllByBannerId("ID-T00001")
//        assertEquals 1, persons.size()
//
//        def currentPerson = persons.get(0)
//        assertNotNull currentPerson.pidm
//        assertEquals "Adams", currentPerson.lastName
//        assertEquals 0L, currentPerson.version
    }


    void testCreateCurrentWhereBannerIdExists() {
        def personList = []
        def person1 = newPersonIdentificationName("ID-T00001")
        def person2 = newPersonIdentificationName("ID-T00001")
        personList << person1
        personList << person2

        try {
            personIdentificationNameCompositeService.createOrUpdate([currentPersonIdentificationNames: personList])
            fail "should have failed because you cannot create two person with the same banner id"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot create new identification record, since the ID provided is in use by another person/non-person."
        }
    }


    void testUpdateCurrentBannerId() {
        def origBannerId = "ID-T00001"
        def updateBannerId = "ID-U00001"

        def person = setupNewPersonIdentificationName(origBannerId)

        // Update the person's banner id
        person.bannerId = updateBannerId

        def updatedPersonList = []
        updatedPersonList << person
        personIdentificationNameCompositeService.createOrUpdate([currentPersonIdentificationNames: updatedPersonList])

        def currentPerson = PersonIdentificationName.fetchBannerPerson(updateBannerId)
        assertNotNull currentPerson
        assertEquals person.pidm, currentPerson.pidm
        assertEquals updateBannerId, currentPerson.bannerId
        assertEquals 1L, currentPerson.version

        def alternatePersons = PersonIdentificationName.fetchAllPersonByPidmAndChangeIndicatorNotNull(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        // Check the alternate id
        def alternatePerson = alternatePersons.get(0)
        assertEquals currentPerson.pidm, alternatePerson?.pidm
        assertEquals origBannerId, alternatePerson.bannerId
        assertEquals "I", alternatePerson.changeIndicator
        assertEquals 0L, alternatePerson.version
    }


    void testUpdateCurrentNonPersonBannerId() {
        def origBannerId = "ID-T00001"
        def updateBannerId = "ID-U00001"

        def person = setupNewNonPersonIdentificationName(origBannerId)

        // Update the non-person's banner id
        person.bannerId = updateBannerId

        def updatedPersonList = []
        updatedPersonList << person
        personIdentificationNameCompositeService.createOrUpdate([currentPersonIdentificationNames: updatedPersonList])

        def currentPerson = PersonIdentificationName.fetchBannerNonPerson(updateBannerId)
        assertNotNull currentPerson
        assertEquals person.pidm, currentPerson.pidm
        assertEquals updateBannerId, currentPerson.bannerId
        assertEquals 1L, currentPerson.version

        // Check the alternate id
        def alternatePersons = PersonIdentificationName.fetchAllPersonByPidmAndChangeIndicatorNotNull(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        def alternatePerson = alternatePersons.get(0)
        assertEquals currentPerson.pidm, alternatePerson?.pidm
        assertEquals origBannerId, alternatePerson.bannerId
        assertEquals "I", alternatePerson.changeIndicator
        assertEquals 0L, alternatePerson.version
    }


    void testUpdateBannerIdToExistingBannerId() {
        def person1 = setupNewPersonIdentificationName("ID-T00001")
        def person2 = setupNewPersonIdentificationName("ID-T00002")

        // Update the person1 banner id to one that already exists
        person1.bannerId = "ID-T00002"

        def updatedPersonList = []
        updatedPersonList << person1

        try {
            personIdentificationNameCompositeService.createOrUpdate([currentPersonIdentificationNames: updatedPersonList])
            fail "should have failed because you cannot create two person with the same banner id"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot update identification record, since the new ID is in use by another person/non-person."
        }
    }


    void testUpdateCurrentName() {
        def origBannerId = "ID-T00001"
        def updateLastName = "UPDATED_LAST_NAME"
        def updateFirstName = "UPDATED_FIRST_NAME"
        def updateMiddleName = "UPDATED_MIDDLE_NAME"

        def person = setupNewPersonIdentificationName(origBannerId)
        def origLastName = person.lastName
        def origFirstName = person.firstName
        def origMiddleName = person.middleName

        // Update the person's name
        person.lastName = updateLastName
        person.firstName = updateFirstName
        person.middleName = updateMiddleName

        def updatedPersonList = []
        updatedPersonList << person
        personIdentificationNameCompositeService.createOrUpdate([currentPersonIdentificationNames: updatedPersonList])

        // Check the current id
        def currentPerson = PersonIdentificationName.fetchBannerPerson(origBannerId)
        assertNotNull currentPerson?.pidm
        assertEquals updateLastName, currentPerson.lastName
        assertEquals updateFirstName, currentPerson.firstName
        assertEquals updateMiddleName, currentPerson.middleName
        assertEquals 1L, currentPerson.version

        // Check the alternate id
        def alternatePersons = PersonIdentificationName.fetchAllPersonByPidmAndChangeIndicatorNotNull(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        def alternatePerson = alternatePersons.get(0)
        assertNotNull alternatePerson?.pidm
        assertEquals origLastName, alternatePerson.lastName
        assertEquals origFirstName, alternatePerson.firstName
        assertEquals origMiddleName, alternatePerson.middleName
        assertEquals 0L, alternatePerson.version
    }


    void testUpdateCurrentNonPersonName() {
        def origBannerId = "ID-T00001"
        def updateLastName = "UPDATED_LAST_NAME"

        def person = setupNewNonPersonIdentificationName(origBannerId)
        def origLastName = person.lastName

        // Update the person's name
        person.lastName = updateLastName

        def updatedPersonList = []
        updatedPersonList << person
        personIdentificationNameCompositeService.createOrUpdate([currentPersonIdentificationNames: updatedPersonList])

        // Check the current id
        def currentPerson = PersonIdentificationName.fetchBannerNonPerson(origBannerId)
        assertNotNull currentPerson?.pidm
        assertEquals updateLastName, currentPerson.lastName
        assertEquals 1L, currentPerson.version

        // Check the alternate id
        def alternatePersons = PersonIdentificationName.fetchAllPersonByPidmAndChangeIndicatorNotNull(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        def alternatePerson = alternatePersons.get(0)
        assertNotNull alternatePerson?.pidm
        assertEquals origLastName, alternatePerson.lastName
        assertEquals 0L, alternatePerson.version
    }


    void testUpdateCurrentBannerIdAndName() {
        def person = setupNewPersonIdentificationName("ID-T00001")

        // Update the person's name
        person.bannerId = "ID-U00001"
        person.lastName = "UPDATED_LAST_NAME"
        person.firstName = "UPDATED_FIRST_NAME"
        person.middleName = "UPDATED_MIDDLE_NAME"

        def updatedPersonList = []
        updatedPersonList << person

        try {
            personIdentificationNameCompositeService.createOrUpdate([currentPersonIdentificationNames: updatedPersonList])
            fail "should have failed because you cannot update both the banner id and name"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "ID and Name cannot be changed at the same time."
        }
    }


    void testUpdateCurrentNameType() {
        def bannerId = "ID-T00001"

        def person = setupNewPersonIdentificationName(bannerId)

        // Update the person's name
        person.nameType = NameType.findWhere(code: "BRTH")

        def updatedPersonList = []
        updatedPersonList << person
        personIdentificationNameCompositeService.createOrUpdate([currentPersonIdentificationNames: updatedPersonList])

        // Check the current id
        def currentPerson = PersonIdentificationName.fetchBannerPerson(bannerId)
        assertNotNull currentPerson
        assertEquals person.pidm, currentPerson.pidm
        assertEquals "BRTH", currentPerson.nameType.code
        assertEquals 1L, currentPerson.version

        // Make sure no alternate person records were created.
        def alternatePersons = PersonIdentificationName.fetchAllPersonByPidmAndChangeIndicatorNotNull(currentPerson.pidm)
        assertEquals 0, alternatePersons.size()
    }


    void testUpdateCurrentEntityIndicator() {
        def person = setupNewPersonIdentificationName("ID-T00001")

        // Change from person entity to non-person entity
        person.entityIndicator = "C"
        person.firstName = null

        def updatedPersonList = []
        updatedPersonList << person

        try {
            personIdentificationNameCompositeService.createOrUpdate([currentPersonIdentificationNames: updatedPersonList])
            fail "should have failed because you cannot update the entity indicator"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot update because the Entity Indicator cannot be changed."
        }
    }


    void testUpdateCurrentNoChanges() {
        def person = setupNewPersonIdentificationName("ID-T00001")

        def updatedPersonList = []
        updatedPersonList << person

        try {
            personIdentificationNameCompositeService.createOrUpdate([currentPersonIdentificationNames: updatedPersonList])
            fail "should have failed because you cannot update both the banner id and name"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Must update at least ID, Name, or Name Type."
        }
    }


    void testUpdateReadOnlyFields() {
        def person = setupNewPersonIdentificationName("ID-T00001")
        def alternatePerson = setupNewAlternateBannerId(person, "ID-A00001")

        alternatePerson.changeIndicator = "N"
        def alternateList = []
        alternateList << alternatePerson

        try {
            personIdentificationNameCompositeService.createOrUpdate([alternatePersonIdentificationNames: alternateList])
            fail "This should have failed with @@r1:readonlyFieldsCannotBeModified"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }


    void testDeleteCurrent() {
        def person = setupNewPersonIdentificationName("ID-T00001")

        def deletePersonList = []
        deletePersonList << person

        try {
            personIdentificationNameCompositeService.createOrUpdate([deleteAlternatePersonIdentificationNames: deletePersonList])
            fail "should have failed because you cannot delete the current id"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot delete current record."
        }
    }

    // *************************************************************************************************************
    //  Perform alternate Id tests.
    // *************************************************************************************************************

    void testCreateAlternateBannerId() {
        def origBannerId = "ID-T00001"
        def altBannerId = "ID-U00001"

        def person = setupNewPersonIdentificationName(origBannerId)

        // Instantiate an alternate id change
        def alternatePerson = new PersonIdentificationName(
                pidm: person.pidm,
                bannerId: altBannerId,
                lastName: person.lastName,
                firstName: person.firstName,
                middleName: person.middleName,
                changeIndicator: "I",
                entityIndicator: "P",
                nameType: NameType.findByCode("BRTH")
        )

        def alternatePersonList = []
        alternatePersonList << alternatePerson
        personIdentificationNameCompositeService.createOrUpdate([alternatePersonIdentificationNames: alternatePersonList])

        // Check the current id
        def currentPerson = PersonIdentificationName.fetchBannerPerson(origBannerId)
        assertNotNull currentPerson
        assertEquals person.pidm, currentPerson.pidm
        assertEquals origBannerId, currentPerson.bannerId
        assertEquals 0L, currentPerson.version

        // Check the alternate id
        def alternatePersons = PersonIdentificationName.fetchAllPersonByPidmAndChangeIndicatorNotNull(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        alternatePerson = alternatePersons.get(0)
        assertNotNull alternatePerson?.pidm
        assertEquals altBannerId, alternatePerson.bannerId
        assertEquals "I", alternatePerson.changeIndicator
        assertEquals "BRTH", alternatePerson.nameType?.code
        assertEquals 0L, alternatePerson.version
    }


    void testCreateAlternateName() {
        def origBannerId = "ID-T00001"
        def altLastName = "ALTERNATE_LAST_NAME"
        def altFirstName = "ALTERNATE_FIRST_NAME"
        def altMiddleName = "ALTERNATE_MIDDLE_NAME"

        def person = setupNewPersonIdentificationName(origBannerId)

        // Instantiate an alternate id change
        def alternatePerson = new PersonIdentificationName(
                pidm: person.pidm,
                bannerId: person.bannerId,
                lastName: altLastName,
                firstName: altFirstName,
                middleName: altMiddleName,
                changeIndicator: "N",
                entityIndicator: person.entityIndicator,
                nameType: person.nameType
        )

        def alternatePersonList = []
        alternatePersonList << alternatePerson
        personIdentificationNameCompositeService.createOrUpdate([alternatePersonIdentificationNames: alternatePersonList])

        // Check the current id
        def currentPerson = PersonIdentificationName.fetchBannerPerson(origBannerId)
        assertNotNull currentPerson?.pidm
        assertEquals origBannerId, currentPerson.bannerId
        assertEquals "Adams", currentPerson.lastName
        assertEquals 0L, currentPerson.version

        // Check the alternate id
        def alternatePersons = PersonIdentificationName.fetchAllPersonByPidmAndChangeIndicatorNotNull(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        alternatePerson = alternatePersons.get(0)
        assertNotNull alternatePerson?.pidm
        assertEquals origBannerId, alternatePerson.bannerId
        assertEquals altLastName, alternatePerson.lastName
        assertEquals altFirstName, alternatePerson.firstName
        assertEquals altMiddleName, alternatePerson.middleName
        assertEquals 0L, alternatePerson.version
    }


    void testUpdateAlternateNameType() {
        def origBannerId = "ID-T00001"
        def altBannerId = "ID-A00001"

        def person = setupNewPersonIdentificationName(origBannerId)
        def alternatePerson = setupNewAlternateBannerId(person, altBannerId)

        alternatePerson.nameType = NameType.findByCode("BRTH")
        def alternateList = []
        alternateList << alternatePerson

        alternatePerson = PersonIdentificationName.get(alternatePerson.id)
        assertNotNull alternatePerson
        assertEquals "BRTH", alternatePerson.nameType.code
    }


    void testUpdateAlternateOtherThanNameType() {
        def origBannerId = "ID-T00001"
        def altBannerId = "ID-A00001"
        def updateBannerId = "ID-U00001"

        def person = setupNewPersonIdentificationName(origBannerId)
        def alternatePerson = setupNewAlternateBannerId(person, altBannerId)

        alternatePerson.bannerId = updateBannerId
        def alternateList = []
        alternateList << alternatePerson

        try {
            personIdentificationNameCompositeService.createOrUpdate([alternatePersonIdentificationNames: alternateList])
            fail "should have failed because you can only update name type on alternate id records"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "The only change to history records allowed is Name Type."
        }
    }


    void testDeleteAlternatePersonIdentificationName() {
        def origBannerId = "ID-T00001"
        def altBannerId = "ID-A00001"
        def updateBannerId = "ID-U00001"
        def altLastName = "ALTERNATE_LAST_NAME"
        def altFirstName = "ALTERNATE_FIRST_NAME"

        def person = setupNewPersonIdentificationName(origBannerId)

        // Create some alternate id records
        def alternatePerson1 = setupNewAlternateBannerId(person, altBannerId)
        def alternatePerson2 = setupNewAlternateName(person, [lastName: altLastName, firstName: altFirstName])

        // Update the person's id  thus creating another alternate id record
        person.bannerId = updateBannerId
        def updatedPersonList = []
        updatedPersonList << person
        personIdentificationNameCompositeService.createOrUpdate([currentPersonIdentificationNames: updatedPersonList])

        // We should have 3 alternate id records
        def updatedPersons = PersonIdentificationName.fetchAllPersonByPidmAndChangeIndicatorNotNull(person.pidm)
        assertEquals 3, updatedPersons.size()

        def deleteList = []
        deleteList << alternatePerson1
        deleteList << alternatePerson2
        personIdentificationNameCompositeService.createOrUpdate([deleteAlternatePersonIdentificationNames: deleteList])

        def personList = PersonIdentificationName.fetchAllPersonByPidmAndChangeIndicatorNotNull(person.pidm)
        assertEquals 1, personList.size()
    }

    // *************************************************************************************************************
    //  End tests.
    // *************************************************************************************************************

    /**
     * Creates a new person current id record.
     */
    private def setupNewPersonIdentificationName(String bannerId) {

        def person = newPersonIdentificationName(bannerId)
        person = personIdentificationNameService.create([domainModel: person])
        assertNotNull person?.id

        person
    }

    /**
     * Creates a new non-person current id record.
     */
    private def setupNewNonPersonIdentificationName(String bannerId) {

        def person = newNonPersonIdentificationName(bannerId)
        person = personIdentificationNameService.create([domainModel: person])
        assertNotNull person.id

        person
    }

    /**
     * Creates a new alternate id record with a banner id change.
     */
    private def setupNewAlternateBannerId(currentPerson, String altBannerId) {

        def alternatePerson = new PersonIdentificationName(
                pidm: currentPerson.pidm,
                bannerId: altBannerId,
                lastName: currentPerson.lastName,
                firstName: currentPerson.firstName,
                middleName: currentPerson.middleName,
                changeIndicator: "I",
                entityIndicator: currentPerson.entityIndicator,
                nameType: currentPerson.nameType
        )

        alternatePerson = personIdentificationNameService.create([domainModel: alternatePerson])
        assertNotNull alternatePerson?.id
        assertEquals altBannerId, alternatePerson.bannerId
        assertEquals 0L, alternatePerson.version

        alternatePerson
    }

    /**
     * Creates a new alternate id record with a name change.
     */
    private def setupNewAlternateName(currentPerson, names) {

        def alternatePerson = new PersonIdentificationName(
                pidm: currentPerson.pidm,
                bannerId: currentPerson.bannerId,
                lastName: names["lastName"],
                firstName: names["firstName"],
                middleName: names["middleName"],
                changeIndicator: "N",
                entityIndicator: currentPerson.entityIndicator,
                nameType: currentPerson.nameType
        )

        alternatePerson = personIdentificationNameService.create([domainModel: alternatePerson])
        assertNotNull alternatePerson?.id
        assertEquals names["lastName"], alternatePerson.lastName
        assertEquals 0L, alternatePerson.version

        alternatePerson
    }


    private def newPersonIdentificationName(String bannerId) {
        def personIdentificationName = new PersonIdentificationName(
                pidm: null,
                bannerId: bannerId,
                lastName: "Adams",
                firstName: "Troy",
                middleName: "W",
                changeIndicator: null,
                entityIndicator: "P",
                nameType: NameType.findByCode("PROF")
        )

        return personIdentificationName
    }



    private def newNonPersonIdentificationName(String bannerId) {
        def personIdentificationName = new PersonIdentificationName(
                pidm: null,
                bannerId: bannerId,
                lastName: "Acme Rocket and Anvils",
                middleName: "W",
                changeIndicator: null,
                entityIndicator: "C",
                nameType: NameType.findByCode("CORP")
        )

        return personIdentificationName
    }

}
