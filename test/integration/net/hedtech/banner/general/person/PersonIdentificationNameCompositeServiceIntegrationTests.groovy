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

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonIdentificationNameCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def personIdentificationNameCompositeService
    def personIdentificationNameService
    def personIdentificationNameCurrentService
    def personIdentificationNameAlternateService


	@Before
	public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


	@After
	public void tearDown() {
        super.tearDown()
    }

    // *************************************************************************************************************
    //  Perform current ID tests first.  Alternate Id tests follow these tests.
    // *************************************************************************************************************

    // TODO test with GENERATED in id
	@Test
    void testCreatePersonIdentificationNameCurrent() {
        def bannerId = "ID-T00001"
        def personList = []

        def person = newPersonIdentificationNameCurrent(bannerId)
        personList << person
        personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: personList])

        def currentPerson = PersonIdentificationNameCurrent.fetchByBannerId(bannerId)
        assertNotNull currentPerson.pidm
        assertEquals "Adams", currentPerson.lastName
        assertEquals "P", currentPerson.entityIndicator
        assertEquals 0L, currentPerson.version

        // Make sure no alternate person records were created.
        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 0, alternatePersons.size()
    }


	@Test
    void testCreateCurrentNonPersonIdentificationName() {
        def bannerId = "ID-T00001"
        def personList = []

        def person = newNonPersonIdentificationNameCurrent(bannerId)
        personList << person
        personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: personList])

        def currentPerson = PersonIdentificationNameCurrent.fetchByBannerId(bannerId)
        assertNotNull currentPerson.pidm
        assertEquals "Acme Rocket and Anvils", currentPerson.lastName
        assertNull currentPerson.firstName
        assertEquals "C", currentPerson.entityIndicator
        assertEquals "CORP", currentPerson.nameType.code
        assertEquals 0L, currentPerson.version

        // Make sure no alternate person records were created.
        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 0, alternatePersons.size()
    }


	@Test
    void testCreateCurrentPersonIdentificationNameWithInvalidChangeIndicator() {
        def bannerId = "ID-T00001"
        def personList = []

        def person = newPersonIdentificationNameCurrent(bannerId)
        person.changeIndicator = "I"
        personList << person

        try {
            personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: personList])
            fail "Should have failed because change indicator must be null."
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "changeIndicatorMustBeNull"
        }
    }


	@Test
    void testCreateCurrentWhereBannerIdExists() {
        def personList = []
        def person1 = newPersonIdentificationNameCurrent("ID-T00001")
        def person2 = newPersonIdentificationNameCurrent("ID-T00001")
        personList << person1
        personList << person2

        try {
            personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: personList])
            fail "Should have failed because you cannot create two persons with the same banner id."
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot create new identification record, since the ID provided is in use by another person/non-person."
        }
    }


	@Test
    void testUpdateCurrentBannerId() {
        def origBannerId = "ID-T00001"
        def updateBannerId = "ID-U00001"

        def person = setupNewPersonIdentificationNameCurrent(origBannerId)

        // Update the person's banner id
        person.bannerId = updateBannerId

        def updatedPersonList = []
        updatedPersonList << person
        personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: updatedPersonList])

        def currentPerson = PersonIdentificationNameCurrent.fetchByBannerId(updateBannerId)
        assertNotNull currentPerson
        assertEquals person.pidm, currentPerson.pidm
        assertEquals updateBannerId, currentPerson.bannerId
        assertEquals 1L, currentPerson.version

        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        // Check the alternate id
        def alternatePerson = alternatePersons.get(0)
        assertEquals currentPerson.pidm, alternatePerson?.pidm
        assertEquals origBannerId, alternatePerson.bannerId
        assertEquals "I", alternatePerson.changeIndicator
        assertEquals 0L, alternatePerson.version
    }


	@Test
    void testUpdateCurrentNonPersonBannerId() {
        def origBannerId = "ID-T00001"
        def updateBannerId = "ID-U00001"

        def person = setupNewNonPersonIdentificationNameCurrent(origBannerId)

        // Update the non-person's banner id
        person.bannerId = updateBannerId

        def updatedPersonList = []
        updatedPersonList << person
        personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: updatedPersonList])

        def currentPerson = PersonIdentificationNameCurrent.fetchByBannerId(updateBannerId)
        assertNotNull currentPerson
        assertEquals person.pidm, currentPerson.pidm
        assertEquals updateBannerId, currentPerson.bannerId
        assertEquals 1L, currentPerson.version

        // Check the alternate id
        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        def alternatePerson = alternatePersons.get(0)
        assertEquals currentPerson.pidm, alternatePerson?.pidm
        assertEquals origBannerId, alternatePerson.bannerId
        assertEquals "I", alternatePerson.changeIndicator
        assertEquals 0L, alternatePerson.version
    }


	@Test
    void testUpdateBannerIdToExistingBannerId() {
        def person1 = setupNewPersonIdentificationNameCurrent("ID-T00001")
        def person2 = setupNewPersonIdentificationNameCurrent("ID-T00002")

        // Update the person1 banner id to one that already exists
        person1.bannerId = "ID-T00002"

        def updatedPersonList = []
        updatedPersonList << person1

        try {
            personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: updatedPersonList])
            fail "should have failed because you cannot create two person with the same banner id"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot update identification record, since the new ID is in use by another person/non-person."
        }
    }


	@Test
    void testUpdateCurrentName() {
        def origBannerId = "ID-T00001"
        def updateLastName = "UPDATED_LAST_NAME"
        def updateFirstName = "UPDATED_FIRST_NAME"
        def updateMiddleName = "UPDATED_MIDDLE_NAME"

        def person = setupNewPersonIdentificationNameCurrent(origBannerId)
        def origLastName = person.lastName
        def origFirstName = person.firstName
        def origMiddleName = person.middleName

        // Update the person's name
        person.lastName = updateLastName
        person.firstName = updateFirstName
        person.middleName = updateMiddleName

        def updatedPersonList = []
        updatedPersonList << person
        personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: updatedPersonList])

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


	@Test
    void testUpdateCurrentNonPersonName() {
        def origBannerId = "ID-T00001"
        def updateLastName = "UPDATED_LAST_NAME"

        def person = setupNewNonPersonIdentificationNameCurrent(origBannerId)
        def origLastName = person.lastName

        // Update the person's name
        person.lastName = updateLastName

        def updatedPersonList = []
        updatedPersonList << person
        personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: updatedPersonList])

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


	@Test
    void testUpdateCurrentBannerIdAndName() {
        def person = setupNewPersonIdentificationNameCurrent("ID-T00001")

        // Update the person's name
        person.bannerId = "ID-U00001"
        person.lastName = "UPDATED_LAST_NAME"
        person.firstName = "UPDATED_FIRST_NAME"
        person.middleName = "UPDATED_MIDDLE_NAME"

        def updatedPersonList = []
        updatedPersonList << person

        try {
            personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: updatedPersonList])
            fail "should have failed because you cannot update both the banner id and name"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "ID and Name cannot be changed at the same time."
        }
    }


	@Test
    void testUpdateCurrentNameType() {
        def bannerId = "ID-T00001"

        def person = setupNewPersonIdentificationNameCurrent(bannerId)

        // Update the person's name
        person.nameType = NameType.findWhere(code: "PROF")

        def updatedPersonList = []
        updatedPersonList << person
        personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: updatedPersonList])

        // Check the current id
        def currentPerson = PersonIdentificationNameCurrent.fetchByBannerId(bannerId)
        assertNotNull currentPerson
        assertEquals person.pidm, currentPerson.pidm
        assertEquals "PROF", currentPerson.nameType.code
        assertEquals 1L, currentPerson.version

        // Make sure no alternate person records were created.
        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 0, alternatePersons.size()
    }


	@Test
    void testUpdateCurrentEntityIndicator() {
        def person = setupNewPersonIdentificationNameCurrent("ID-T00001")

        // Change from person entity to non-person entity
        person.entityIndicator = "C"
        person.firstName = null

        def updatedPersonList = []
        updatedPersonList << person

        try {
            personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: updatedPersonList])
            fail "should have failed because you cannot update the entity indicator"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot update because the Entity Indicator cannot be changed."
        }
    }


	@Test
    void testUpdateCurrentNoChanges() {
        def person = setupNewPersonIdentificationNameCurrent("ID-T00001")

        person.dataOrigin = "TEST"

        def updatedPersonList = []
        updatedPersonList << person

        try {
            personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: updatedPersonList])
            fail "should have failed because you cannot update both the banner id and name"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Must update at least ID, Name, or Name Type."
        }
    }


	@Test
    void testUpdateReadOnlyFields() {
        def person = setupNewPersonIdentificationNameCurrent("ID-T00001")
        def alternatePerson = setupNewAlternateBannerId(person, "ID-A00001")

        alternatePerson.changeIndicator = "N"
        def alternateList = []
        alternateList << alternatePerson

        try {
            personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameAlternates: alternateList])
            fail "This should have failed with @@r1:readonlyFieldsCannotBeModified"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }


	@Test
    void testDeleteCurrent() {
        def person = setupNewPersonIdentificationNameCurrent("ID-T00001")

        def deletePersonList = []
        deletePersonList << person

        try {
            personIdentificationNameCompositeService.createOrUpdate([deletePersonIdentificationNameCurrents: deletePersonList])
            fail "should have failed because you cannot delete the current id"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot delete current record."
        }
    }

    // *************************************************************************************************************
    //  Perform alternate Id tests.
    // *************************************************************************************************************

	@Test
    void testCreateAlternateBannerId() {
        def origBannerId = "ID-T00001"
        def altBannerId = "ID-U00001"

        def person = setupNewPersonIdentificationNameCurrent(origBannerId)

        // Instantiate an alternate id change
        def alternatePerson = new PersonIdentificationNameAlternate(
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
        personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameAlternates: alternatePersonList])

        // Check the current id
        def currentPerson = PersonIdentificationNameCurrent.fetchByBannerId(origBannerId)
        assertNotNull currentPerson
        assertEquals person.pidm, currentPerson.pidm
        assertEquals origBannerId, currentPerson.bannerId
        assertEquals 0L, currentPerson.version

        // Check the alternate id
        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        alternatePerson = alternatePersons.get(0)
        assertNotNull alternatePerson?.pidm
        assertEquals altBannerId, alternatePerson.bannerId
        assertEquals "I", alternatePerson.changeIndicator
        assertEquals "BRTH", alternatePerson.nameType?.code
        assertEquals 0L, alternatePerson.version
    }


	@Test
    void testCreateAlternateName() {
        def origBannerId = "ID-T00001"
        def altLastName = "ALTERNATE_LAST_NAME"
        def altFirstName = "ALTERNATE_FIRST_NAME"
        def altMiddleName = "ALTERNATE_MIDDLE_NAME"

        def person = setupNewPersonIdentificationNameCurrent(origBannerId)

        // Instantiate an alternate id change
        def alternatePerson = new PersonIdentificationNameAlternate(
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
        personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameAlternates: alternatePersonList])

        // Check the current id
        def currentPerson = PersonIdentificationNameCurrent.fetchByBannerId(origBannerId)
        assertNotNull currentPerson?.pidm
        assertEquals origBannerId, currentPerson.bannerId
        assertEquals "Adams", currentPerson.lastName
        assertEquals 0L, currentPerson.version

        // Check the alternate id
        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        alternatePerson = alternatePersons.get(0)
        assertNotNull alternatePerson?.pidm
        assertEquals origBannerId, alternatePerson.bannerId
        assertEquals altLastName, alternatePerson.lastName
        assertEquals altFirstName, alternatePerson.firstName
        assertEquals altMiddleName, alternatePerson.middleName
        assertEquals 0L, alternatePerson.version
    }

	@Test
    void testCreateAlternatePersonIdentificationNameWithNullChangeIndicator() {
        def origBannerId = "ID-T00001"
        def altBannerId = "ID-U00001"

        def person = setupNewPersonIdentificationNameCurrent(origBannerId)

        // Instantiate an alternate id change
        def alternatePerson = new PersonIdentificationNameAlternate(
                pidm: person.pidm,
                bannerId: altBannerId,
                lastName: person.lastName,
                firstName: person.firstName,
                middleName: person.middleName,
                changeIndicator: null,
                entityIndicator: "P",
                nameType: NameType.findByCode("BRTH")
        )

        def alternatePersonList = []
        alternatePersonList << alternatePerson

        try {
            personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameAlternates: alternatePersonList])
            fail "Should have failed because change indicator must be null."
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "changeIndicatorCannotBeNull"
        }
    }


	@Test
    void testUpdateAlternateNameType() {
        def origBannerId = "ID-T00001"
        def altBannerId = "ID-A00001"

        def person = setupNewPersonIdentificationNameCurrent(origBannerId)
        def alternatePerson = setupNewAlternateBannerId(person, altBannerId)

        alternatePerson.nameType = NameType.findByCode("PROF")
        def alternateList = []
        alternateList << alternatePerson
        personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameAlternates: alternateList])

        alternatePerson = PersonIdentificationNameAlternate.get(alternatePerson.id)
        assertNotNull alternatePerson
        assertEquals "PROF", alternatePerson.nameType.code
    }


	@Test
    void testUpdateAlternateOtherThanNameType() {
        def origBannerId = "ID-T00001"
        def altBannerId = "ID-A00001"
        def updateBannerId = "ID-U00001"

        def person = setupNewPersonIdentificationNameCurrent(origBannerId)
        def alternatePerson = setupNewAlternateBannerId(person, altBannerId)

        alternatePerson.bannerId = updateBannerId
        def alternateList = []
        alternateList << alternatePerson

        try {
            personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameAlternates: alternateList])
            fail "should have failed because you can only update name type on alternate id records"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "The only change to history records allowed is Name Type."
        }
    }


	@Test
    void testDeleteAlternatePersonIdentificationName() {
        def origBannerId = "ID-T00001"
        def altBannerId = "ID-A00001"
        def updateBannerId = "ID-U00001"
        def altLastName = "ALTERNATE_LAST_NAME"
        def altFirstName = "ALTERNATE_FIRST_NAME"

        def person = setupNewPersonIdentificationNameCurrent(origBannerId)

        // Create some alternate id records
        def alternatePerson1 = setupNewAlternateBannerId(person, altBannerId)
        def alternatePerson2 = setupNewAlternateName(person, [lastName: altLastName, firstName: altFirstName])

        // Update the person's id  thus creating another alternate id record
        person.bannerId = updateBannerId
        def updatedPersonList = []
        updatedPersonList << person
        personIdentificationNameCompositeService.createOrUpdate([personIdentificationNameCurrents: updatedPersonList])

        // We should have 3 alternate id records
        def updatedPersons = PersonIdentificationNameAlternate.fetchAllByPidm(person.pidm)
        assertEquals 3, updatedPersons.size()

        def deleteList = []
        deleteList << alternatePerson1
        deleteList << alternatePerson2
        personIdentificationNameCompositeService.createOrUpdate([deletePersonIdentificationNameAlternates: deleteList])

        def personList = PersonIdentificationNameAlternate.fetchAllByPidm(person.pidm)
        assertEquals 1, personList.size()
    }

    // *************************************************************************************************************
    //  End tests.
    // *************************************************************************************************************

    /**
     * Creates a new person current id record.
     */
    private def setupNewPersonIdentificationNameCurrent(bannerId) {

        def person = newPersonIdentificationNameCurrent(bannerId)
        person = personIdentificationNameCurrentService.create([domainModel: person])
        assertNotNull person?.id

        person
    }

    /**
     * Creates a new non-person current id record.
     */
    private def setupNewNonPersonIdentificationNameCurrent(String bannerId) {

        def person = newNonPersonIdentificationNameCurrent(bannerId)
        person = personIdentificationNameCurrentService.create([domainModel: person])
        assertNotNull person.id

        person
    }

    /**
     * Creates a new alternate id record with a banner id change.
     */
    private def setupNewAlternateBannerId(currentPerson, String altBannerId) {

        def alternatePerson = new PersonIdentificationNameAlternate(
                pidm: currentPerson.pidm,
                bannerId: altBannerId,
                lastName: currentPerson.lastName,
                firstName: currentPerson.firstName,
                middleName: currentPerson.middleName,
                changeIndicator: "I",
                entityIndicator: currentPerson.entityIndicator,
                nameType: currentPerson.nameType
        )

        alternatePerson = personIdentificationNameAlternateService.create([domainModel: alternatePerson])
        assertNotNull alternatePerson?.id
        assertEquals altBannerId, alternatePerson.bannerId
        assertEquals 0L, alternatePerson.version

        alternatePerson
    }

    /**
     * Creates a new alternate id record with a name change.
     */
    private def setupNewAlternateName(currentPerson, names) {

        def alternatePerson = new PersonIdentificationNameAlternate(
                pidm: currentPerson.pidm,
                bannerId: currentPerson.bannerId,
                lastName: names["lastName"],
                firstName: names["firstName"],
                middleName: names["middleName"],
                changeIndicator: "N",
                entityIndicator: currentPerson.entityIndicator,
                nameType: currentPerson.nameType
        )

        alternatePerson = personIdentificationNameAlternateService.create([domainModel: alternatePerson])
        assertNotNull alternatePerson?.id
        assertEquals names["lastName"], alternatePerson.lastName
        assertEquals 0L, alternatePerson.version

        alternatePerson
    }


    private def newPersonIdentificationNameCurrent(String bannerId) {
        def personIdentificationNameCurrent = new PersonIdentificationNameCurrent(
                pidm: null,
                bannerId: bannerId,
                lastName: "Adams",
                firstName: "Troy",
                middleName: "W",
                changeIndicator: null,
                entityIndicator: "P",
                nameType: NameType.findByCode("BRTH")
        )

        return personIdentificationNameCurrent
    }


    private def newNonPersonIdentificationNameCurrent(String bannerId) {
        def personIdentificationNameCurrent = new PersonIdentificationNameCurrent(
                pidm: null,
                bannerId: bannerId,
                lastName: "Acme Rocket and Anvils",
                middleName: "W",
                changeIndicator: null,
                entityIndicator: "C",
                nameType: NameType.findByCode("CORP")
        )

        return personIdentificationNameCurrent
    }

}
