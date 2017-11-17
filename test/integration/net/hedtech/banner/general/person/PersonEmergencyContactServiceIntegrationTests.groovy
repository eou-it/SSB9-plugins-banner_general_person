/*********************************************************************************
Copyright 2012-2017 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
package net.hedtech.banner.general.person
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.AddressType
import net.hedtech.banner.general.system.Nation
import net.hedtech.banner.general.system.Relationship
import net.hedtech.banner.general.system.State
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonEmergencyContactServiceIntegrationTests extends BaseIntegrationTestCase {

    def personEmergencyContactService

    //Test data for creating new domain instance
    //Valid test data (For success tests)
    def i_success_state
    def i_success_nation
    def i_success_relationship
    def i_success_addressType
    def i_success_priority = "1"
    def i_success_lastName = "TTTTT"
    def i_success_firstName = "TTTTT"
    def i_success_middleInitial = "TTTTT"
    def i_success_streetLine1 = "TTTTT"
    def i_success_streetLine2 = "TTTTT"
    def i_success_streetLine3 = "TTTTT"
    def i_success_city = "TTTTT"
    def i_success_zip = "TTTTT"
    def i_success_phoneArea = "TTTTT"
    def i_success_phoneNumber = "TTTTT"
    def i_success_phoneExtension = "TTTTT"
    def i_success_surnamePrefix = "TTTTT"
    def i_success_countryPhone = "TTTT"
    def i_success_houseNumber = "TTTTT"
    def i_success_streetLine4 = "TTTTT"

    //Invalid test data (For failure tests)
    def i_failure_state
    def i_failure_nation
    def i_failure_relationship
    def i_failure_addressType
    def i_failure_priority = "1"
    def i_failure_lastName = "TTTTT"
    def i_failure_firstName = "TTTTT"
    def i_failure_middleInitial = "TTTTT"
    def i_failure_streetLine1 = "TTTTT"
    def i_failure_streetLine2 = "TTTTT"
    def i_failure_streetLine3 = "TTTTT"
    def i_failure_city = "TTTTT"
    def i_failure_zip = "TTTTT"
    def i_failure_phoneArea = "TTTTTTT"
    def i_failure_phoneNumber = "TTTTT"
    def i_failure_phoneExtension = "TTTTT"
    def i_failure_surnamePrefix = "TTTTT"
    def i_failure_countryPhone = "TTTT"
    def i_failure_houseNumber = "TTTTT"
    def i_failure_streetLine4 = "TTTTT"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)
    def u_success_state
    def u_success_nation
    def u_success_relationship
    def u_success_addressType
    def u_success_priority = "1"
    def u_success_lastName = "UTTTT"
    def u_success_firstName = "TTTTT"
    def u_success_middleInitial = "TTTTT"
    def u_success_streetLine1 = "TTTTT"
    def u_success_streetLine2 = "TTTTT"
    def u_success_streetLine3 = "TTTTT"
    def u_success_city = "TTTTT"
    def u_success_zip = "TTTTT"
    def u_success_phoneArea = "TTTTT"
    def u_success_phoneNumber = "TTTTT"
    def u_success_phoneExtension = "TTTTT"
    def u_success_surnamePrefix = "TTTTT"
    def u_success_countryPhone = "TTTT"
    def u_success_houseNumber = "TTTTT"
    def u_success_streetLine4 = "TTTTT"

    //Valid test data (For failure tests)
    def u_failure_state
    def u_failure_nation
    def u_failure_relationship
    def u_failure_addressType
    def u_failure_priority = "1"
    def u_failure_lastName = "TTTTT"
    def u_failure_firstName = "TTTTT"
    def u_failure_middleInitial = "TTTTT"
    def u_failure_streetLine1 = "TTTTT"
    def u_failure_streetLine2 = "TTTTT"
    def u_failure_streetLine3 = "TTTTT"
    def u_failure_city = "TTTTT"
    def u_failure_zip = "TTTTT"
    def u_failure_phoneArea = "TTTTTTT"
    def u_failure_phoneNumber = "TTTTT"
    def u_failure_phoneExtension = "TTTTT"
    def u_failure_surnamePrefix = "TTTTT"
    def u_failure_countryPhone = "TTTT"
    def u_failure_houseNumber = "TTTTT"
    def u_failure_streetLine4 = "TTTTT"


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
        i_success_state = State.findByCode("MER")
        i_success_nation = Nation.findByCode("157")
        i_success_relationship = Relationship.findByCode("M")
        i_success_addressType = AddressType.findByCode("PO")

        //Invalid test data (For failure tests)
        i_failure_state = State.findByCode("GLO")
        i_failure_nation = Nation.findByCode("40")
        i_failure_relationship = Relationship.findByCode("Y")
        i_failure_addressType = AddressType.findByCode("GR")

        //Valid test data (For success tests)
        u_success_state = State.findByCode("MER")
        u_success_nation = Nation.findByCode("157")
        u_success_relationship = Relationship.findByCode("M")
        u_success_addressType = AddressType.findByCode("PO")

        //Valid test data (For failure tests)
        u_failure_state = State.findByCode("GLO")
        u_failure_nation = Nation.findByCode("40")
        u_failure_relationship = Relationship.findByCode("Y")
        u_failure_addressType = AddressType.findByCode("GR")
    }


    @After
    public void tearDown() {
        logout()
        super.tearDown()
    }


    @Test
    void testPersonEmergencyContactValidCreate() {
        def personEmergencyContact = newValidForCreatePersonEmergencyContact()
        def map = [domainModel: personEmergencyContact]
        personEmergencyContact = personEmergencyContactService.create(map)
        assertNotNull "PersonEmergencyContact ID is null in PersonEmergencyContact Service Tests Create", personEmergencyContact.id
        assertNotNull "PersonEmergencyContact state is null in PersonEmergencyContact Service Tests", personEmergencyContact.state
        assertNotNull "PersonEmergencyContact nation is null in PersonEmergencyContact Service Tests", personEmergencyContact.nation
        assertNotNull "PersonEmergencyContact relationship is null in PersonEmergencyContact Service Tests", personEmergencyContact.relationship
        assertNotNull "PersonEmergencyContact addressType is null in PersonEmergencyContact Service Tests", personEmergencyContact.addressType
        assertNotNull personEmergencyContact.version
        assertNotNull personEmergencyContact.dataOrigin
        assertNotNull personEmergencyContact.lastModifiedBy
        assertNotNull personEmergencyContact.lastModified
    }


    @Test
    void testPersonEmergencyContactInvalidCreate() {
        def personEmergencyContact = newInvalidForCreatePersonEmergencyContact()
        def map = [domainModel: personEmergencyContact]
        shouldFail(ApplicationException) {
            personEmergencyContact = personEmergencyContactService.create(map)
        }
    }


    @Test
    void testPersonEmergencyContactValidUpdate() {
        def personEmergencyContact = newValidForCreatePersonEmergencyContact()
        def map = [domainModel: personEmergencyContact]
        personEmergencyContact = personEmergencyContactService.create(map)
        assertNotNull "PersonEmergencyContact ID is null in PersonEmergencyContact Service Tests Create", personEmergencyContact.id
        assertNotNull "PersonEmergencyContact state is null in PersonEmergencyContact Service Tests", personEmergencyContact.state
        assertNotNull "PersonEmergencyContact nation is null in PersonEmergencyContact Service Tests", personEmergencyContact.nation
        assertNotNull "PersonEmergencyContact relationship is null in PersonEmergencyContact Service Tests", personEmergencyContact.relationship
        assertNotNull "PersonEmergencyContact addressType is null in PersonEmergencyContact Service Tests", personEmergencyContact.addressType
        assertNotNull personEmergencyContact.version
        assertNotNull personEmergencyContact.dataOrigin
        assertNotNull personEmergencyContact.lastModifiedBy
        assertNotNull personEmergencyContact.lastModified
        //Update the entity with new values
        personEmergencyContact.state = u_success_state
        personEmergencyContact.nation = u_success_nation
        personEmergencyContact.relationship = u_success_relationship
        personEmergencyContact.addressType = u_success_addressType
        personEmergencyContact.lastName = u_success_lastName
        personEmergencyContact.firstName = u_success_firstName
        personEmergencyContact.middleInitial = u_success_middleInitial
        personEmergencyContact.streetLine1 = u_success_streetLine1
        personEmergencyContact.streetLine2 = u_success_streetLine2
        personEmergencyContact.streetLine3 = u_success_streetLine3
        personEmergencyContact.city = u_success_city
        personEmergencyContact.zip = u_success_zip
        personEmergencyContact.phoneArea = u_success_phoneArea
        personEmergencyContact.phoneNumber = u_success_phoneNumber
        personEmergencyContact.phoneExtension = u_success_phoneExtension
        personEmergencyContact.surnamePrefix = u_success_surnamePrefix
        personEmergencyContact.countryPhone = u_success_countryPhone
        personEmergencyContact.houseNumber = u_success_houseNumber
        personEmergencyContact.streetLine4 = u_success_streetLine4

        map.domainModel = personEmergencyContact
        personEmergencyContact = personEmergencyContactService.update(map)
        // test the values
        assertEquals u_success_lastName, personEmergencyContact.lastName
        assertEquals u_success_firstName, personEmergencyContact.firstName
        assertEquals u_success_middleInitial, personEmergencyContact.middleInitial
        assertEquals u_success_streetLine1, personEmergencyContact.streetLine1
        assertEquals u_success_streetLine2, personEmergencyContact.streetLine2
        assertEquals u_success_streetLine3, personEmergencyContact.streetLine3
        assertEquals u_success_city, personEmergencyContact.city
        assertEquals u_success_zip, personEmergencyContact.zip
        assertEquals u_success_phoneArea, personEmergencyContact.phoneArea
        assertEquals u_success_phoneNumber, personEmergencyContact.phoneNumber
        assertEquals u_success_phoneExtension, personEmergencyContact.phoneExtension
        assertEquals u_success_surnamePrefix, personEmergencyContact.surnamePrefix
        assertEquals u_success_countryPhone, personEmergencyContact.countryPhone
        assertEquals u_success_houseNumber, personEmergencyContact.houseNumber
        assertEquals u_success_streetLine4, personEmergencyContact.streetLine4
        assertEquals u_success_state, personEmergencyContact.state
        assertEquals u_success_nation, personEmergencyContact.nation
        assertEquals u_success_relationship, personEmergencyContact.relationship
        assertEquals u_success_addressType, personEmergencyContact.addressType
    }


    @Test
    void testPersonEmergencyContactInvalidUpdate() {
        def personEmergencyContact = newValidForCreatePersonEmergencyContact()
        def map = [domainModel: personEmergencyContact]
        personEmergencyContact = personEmergencyContactService.create(map)
        assertNotNull "PersonEmergencyContact ID is null in PersonEmergencyContact Service Tests Create", personEmergencyContact.id
        assertNotNull "PersonEmergencyContact state is null in PersonEmergencyContact Service Tests", personEmergencyContact.state
        assertNotNull "PersonEmergencyContact nation is null in PersonEmergencyContact Service Tests", personEmergencyContact.nation
        assertNotNull "PersonEmergencyContact relationship is null in PersonEmergencyContact Service Tests", personEmergencyContact.relationship
        assertNotNull "PersonEmergencyContact addressType is null in PersonEmergencyContact Service Tests", personEmergencyContact.addressType
        assertNotNull personEmergencyContact.version
        assertNotNull personEmergencyContact.dataOrigin
        assertNotNull personEmergencyContact.lastModifiedBy
        assertNotNull personEmergencyContact.lastModified
        //Update the entity with new invalid values
        personEmergencyContact.state = u_failure_state
        personEmergencyContact.nation = u_failure_nation
        personEmergencyContact.relationship = u_failure_relationship
        personEmergencyContact.addressType = u_failure_addressType
        personEmergencyContact.lastName = u_failure_lastName
        personEmergencyContact.firstName = u_failure_firstName
        personEmergencyContact.middleInitial = u_failure_middleInitial
        personEmergencyContact.streetLine1 = u_failure_streetLine1
        personEmergencyContact.streetLine2 = u_failure_streetLine2
        personEmergencyContact.streetLine3 = u_failure_streetLine3
        personEmergencyContact.city = u_failure_city
        personEmergencyContact.zip = u_failure_zip
        personEmergencyContact.phoneArea = u_failure_phoneArea
        personEmergencyContact.phoneNumber = u_failure_phoneNumber
        personEmergencyContact.phoneExtension = u_failure_phoneExtension
        personEmergencyContact.surnamePrefix = u_failure_surnamePrefix
        personEmergencyContact.countryPhone = u_failure_countryPhone
        personEmergencyContact.houseNumber = u_failure_houseNumber
        personEmergencyContact.streetLine4 = u_failure_streetLine4

        map.domainModel = personEmergencyContact
        shouldFail(ApplicationException) {
            personEmergencyContact = personEmergencyContactService.update(map)
        }
    }


    @Test
    void testPersonEmergencyContactDelete() {
        def personEmergencyContact = newValidForCreatePersonEmergencyContact()
        def map = [domainModel: personEmergencyContact]
        personEmergencyContact = personEmergencyContactService.create(map)
        assertNotNull "PersonEmergencyContact ID is null in PersonEmergencyContact Service Tests Create", personEmergencyContact.id
        def id = personEmergencyContact.id
        map = [domainModel: personEmergencyContact]
        personEmergencyContactService.delete(map)
        assertNull "PersonEmergencyContact should have been deleted", personEmergencyContact.get(id)
    }


    @Test
    void testReadOnly() {
        def personEmergencyContact = newValidForCreatePersonEmergencyContact()
        def map = [domainModel: personEmergencyContact]
        personEmergencyContact = personEmergencyContactService.create(map)
        assertNotNull "PersonEmergencyContact ID is null in PersonEmergencyContact Service Tests Create", personEmergencyContact.id
        personEmergencyContact.pidm = PersonUtility.getPerson("HOS00002").pidm
        personEmergencyContact.priority = u_success_priority
        try {
            personEmergencyContactService.update([domainModel: personEmergencyContact])
            fail("This should have failed with @@r1:readonlyFieldsCannotBeModified")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }

    @Test
    void testFetchActiveEmergencyContactsByPidm(){
        def personEmergencyContact = newValidForCreatePersonEmergencyContact()
        def map = [domainModel: personEmergencyContact]
        personEmergencyContactService.create(map)

        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def contacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        assertEquals 1, contacts.size()
        assertEquals 'M', contacts[0].relationship.code
    }

    @Test
    void testCheckEmergencyContactFieldsValidFirstNameFail() {
        def contact = [firstName: null, lastName: "Doe"]
        try {
            personEmergencyContactService.checkEmergencyContactFieldsValid(contact)
            fail("First Name failed to be flagged as invalid.") // This line should not be reached
        } catch(ApplicationException ae) {
            assertApplicationException ae, "firstNameRequired"
        }
    }

    @Test
    void testCheckEmergencyContactFieldsValidLastNameFail() {
        def contact = [firstName: "Jane", lastName: null]
        try {
            personEmergencyContactService.checkEmergencyContactFieldsValid(contact)
            fail("Last Name failed to be flagged as invalid.") // This line should not be reached
        } catch(ApplicationException ae) {
            assertApplicationException ae, "lastNameRequired"
        }
    }

    @Test
    void testCreateEmergencyContactWithPriorityShuffle() {
        def map = newValidForCreatePersonEmergencyContact()
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertNotNull "PersonEmergencyContact ID is null in PersonEmergencyContact Service Tests Create", personEmergencyContact.id
        assertNotNull "PersonEmergencyContact state is null in PersonEmergencyContact Service Tests", personEmergencyContact.state
        assertNotNull "PersonEmergencyContact nation is null in PersonEmergencyContact Service Tests", personEmergencyContact.nation
        assertNotNull "PersonEmergencyContact relationship is null in PersonEmergencyContact Service Tests", personEmergencyContact.relationship
        assertNotNull "PersonEmergencyContact addressType is null in PersonEmergencyContact Service Tests", personEmergencyContact.addressType
        assertNotNull personEmergencyContact.version
        assertNotNull personEmergencyContact.dataOrigin
        assertNotNull personEmergencyContact.lastModifiedBy
        assertNotNull personEmergencyContact.lastModified

    }

    @Test
    void testUpdateEmergencyContactWithPriorityShuffleForNonPriorityUpdate() {
        def map = newValidForCreatePersonEmergencyContact()

        // Create new entity
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertNotNull "PersonEmergencyContact ID is null in PersonEmergencyContact Service Tests Create", personEmergencyContact.id
        assertNotNull "PersonEmergencyContact state is null in PersonEmergencyContact Service Tests", personEmergencyContact.state
        assertNotNull "PersonEmergencyContact nation is null in PersonEmergencyContact Service Tests", personEmergencyContact.nation
        assertNotNull "PersonEmergencyContact relationship is null in PersonEmergencyContact Service Tests", personEmergencyContact.relationship
        assertNotNull "PersonEmergencyContact addressType is null in PersonEmergencyContact Service Tests", personEmergencyContact.addressType
        assertNotNull personEmergencyContact.version
        assertNotNull personEmergencyContact.dataOrigin
        assertNotNull personEmergencyContact.lastModifiedBy
        assertNotNull personEmergencyContact.lastModified

        // Update the entity with new values.  For this test, priority is NOT changed.
        map = personEmergencyContactService.populateEmergencyContact(personEmergencyContact)
        map.id = personEmergencyContact.id
        map.version = personEmergencyContact.version
        map.state = u_success_state
        map.nation = u_success_nation
        map.relationship = u_success_relationship
        map.addressType = u_success_addressType
        map.lastName = u_success_lastName
        map.firstName = u_success_firstName
        map.middleInitial = u_success_middleInitial
        map.streetLine1 = u_success_streetLine1
        map.streetLine2 = u_success_streetLine2
        map.streetLine3 = u_success_streetLine3
        map.city = u_success_city
        map.zip = u_success_zip
        map.phoneArea = u_success_phoneArea
        map.phoneNumber = u_success_phoneNumber
        map.phoneExtension = u_success_phoneExtension
        map.surnamePrefix = u_success_surnamePrefix
        map.countryPhone = u_success_countryPhone
        map.houseNumber = u_success_houseNumber
        map.streetLine4 = u_success_streetLine4

        personEmergencyContactService.updateEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        // Test the values
        assertEquals u_success_lastName, personEmergencyContact.lastName
        assertEquals u_success_firstName, personEmergencyContact.firstName
        assertEquals u_success_middleInitial, personEmergencyContact.middleInitial
        assertEquals u_success_streetLine1, personEmergencyContact.streetLine1
        assertEquals u_success_streetLine2, personEmergencyContact.streetLine2
        assertEquals u_success_streetLine3, personEmergencyContact.streetLine3
        assertEquals u_success_city, personEmergencyContact.city
        assertEquals u_success_zip, personEmergencyContact.zip
        assertEquals u_success_phoneArea, personEmergencyContact.phoneArea
        assertEquals u_success_phoneNumber, personEmergencyContact.phoneNumber
        assertEquals u_success_phoneExtension, personEmergencyContact.phoneExtension
        assertEquals u_success_surnamePrefix, personEmergencyContact.surnamePrefix
        assertEquals u_success_countryPhone, personEmergencyContact.countryPhone
        assertEquals u_success_houseNumber, personEmergencyContact.houseNumber
        assertEquals u_success_streetLine4, personEmergencyContact.streetLine4
        assertEquals u_success_state, personEmergencyContact.state
        assertEquals u_success_nation, personEmergencyContact.nation
        assertEquals u_success_relationship, personEmergencyContact.relationship
        assertEquals u_success_addressType, personEmergencyContact.addressType
        assertEquals u_success_priority, personEmergencyContact.priority
    }

    @Test
    void testCreateEmergencyContactWithPriorityShuffleAddMultipleContacts() {
        // Check all existing emergency contacts for the same user
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Confirm 3 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(3, existingContacts.size())

        // Confirm each has the priority we expect
        assertEquals("TTTTT", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT2", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT3", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)
    }

    @Test
    void testCreateEmergencyContactWithPriorityShuffleDoInsertAtBeginning() {
        // Check all existing emergency contacts for the same user
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Insert entity #4 AT BEGINNING
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 1
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Confirm each has the priority we expect
        assertEquals("TTTTT4", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT2", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)

        assertEquals("TTTTT3", existingContacts[3].firstName)
        assertEquals("4", existingContacts[3].priority)
    }

    @Test
    void testCreateEmergencyContactWithPriorityShuffleDoInsertInMiddle() {
        // Check all existing emergency contacts for the same user
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Insert entity #4 IN MIDDLE
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Confirm each has the priority we expect
        assertEquals("TTTTT", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT4", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT2", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)

        assertEquals("TTTTT3", existingContacts[3].firstName)
        assertEquals("4", existingContacts[3].priority)
    }

    @Test
    void testUpdateEmergencyContactWithPriorityShuffleDoMoveFromMiddleToFirst() {
        // Check all existing emergency contacts for the same user
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Create new entity #4
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 4
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[3]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("4", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Confirm each has the priority we expect
        assertEquals("TTTTT", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT2", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT3", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)

        assertEquals("TTTTT4", existingContacts[3].firstName)
        assertEquals("4", existingContacts[3].priority)

        // Now MOVE the third one to the beginning
        map = personEmergencyContactService.populateEmergencyContact(existingContacts[2])
        map.id = existingContacts[2].id
        map.priority = 1

        personEmergencyContactService.updateEmergencyContactWithPriorityShuffle(map)

        // Confirm each has the priority we expect
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        assertEquals("TTTTT3", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT2", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)

        assertEquals("TTTTT4", existingContacts[3].firstName)
        assertEquals("4", existingContacts[3].priority)
    }

    @Test
    void testUpdateEmergencyContactWithPriorityShuffleDoMoveFromMiddleToLast() {
        // Check all existing emergency contacts for the same user
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Create new entity #4
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 4
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[3]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("4", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Confirm each has the priority we expect
        assertEquals("TTTTT", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT2", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT3", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)

        assertEquals("TTTTT4", existingContacts[3].firstName)
        assertEquals("4", existingContacts[3].priority)

        // Now MOVE the third one to the end
        map = personEmergencyContactService.populateEmergencyContact(existingContacts[2])
        map.id = existingContacts[2].id
        map.priority = 4

        personEmergencyContactService.updateEmergencyContactWithPriorityShuffle(map)

        // Confirm each has the priority we expect
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        assertEquals("TTTTT", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT2", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT4", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)

        assertEquals("TTTTT3", existingContacts[3].firstName)
        assertEquals("4", existingContacts[3].priority)
    }

    @Test
    void testUpdateEmergencyContactWithPriorityShuffleDoMoveFromFirstToLast() {
        // Check all existing emergency contacts for the same user
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Create new entity #4
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 4
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[3]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("4", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Confirm each has the priority we expect
        assertEquals("TTTTT", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT2", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT3", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)

        assertEquals("TTTTT4", existingContacts[3].firstName)
        assertEquals("4", existingContacts[3].priority)

        // Now MOVE the first one to the end
        map = personEmergencyContactService.populateEmergencyContact(existingContacts[0])
        map.id = existingContacts[0].id
        map.priority = 4

        personEmergencyContactService.updateEmergencyContactWithPriorityShuffle(map)

        // Confirm each has the priority we expect
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        assertEquals("TTTTT2", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT3", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT4", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)

        assertEquals("TTTTT", existingContacts[3].firstName)
        assertEquals("4", existingContacts[3].priority)
    }

    @Test
    void testUpdateEmergencyContactWithPriorityShuffleDoMoveFromLastToFirst() {
        // Check all existing emergency contacts for the same user
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Create new entity #4
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 4
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[3]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("4", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Confirm each has the priority we expect
        assertEquals("TTTTT", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT2", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT3", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)

        assertEquals("TTTTT4", existingContacts[3].firstName)
        assertEquals("4", existingContacts[3].priority)

        // Now MOVE the last one to the beginning
        map = personEmergencyContactService.populateEmergencyContact(existingContacts[3])
        map.id = existingContacts[3].id
        map.priority = 1

        personEmergencyContactService.updateEmergencyContactWithPriorityShuffle(map)

        // Confirm each has the priority we expect
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        assertEquals("TTTTT4", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT2", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)

        assertEquals("TTTTT3", existingContacts[3].firstName)
        assertEquals("4", existingContacts[3].priority)
    }

    @Test
    void testDeleteEmergencyContactWithPriorityShuffleDoDeleteFirst() {
        // Check all existing emergency contacts for the same user
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Create new entity #4
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 4
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[3]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("4", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Confirm each has the priority we expect
        assertEquals("TTTTT", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT2", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT3", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)

        assertEquals("TTTTT4", existingContacts[3].firstName)
        assertEquals("4", existingContacts[3].priority)

        // Now DELETE the first one
        map = personEmergencyContactService.populateEmergencyContact(existingContacts[0])
        map.id = existingContacts[0].id

        personEmergencyContactService.deleteEmergencyContactWithPriorityShuffle(map)

        // Confirm each has the priority we expect
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        assertEquals("TTTTT2", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT3", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT4", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)
    }

    @Test
    void testDeleteEmergencyContactWithPriorityShuffleDoDeleteFromMiddle() {
        // Check all existing emergency contacts for the same user
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Create new entity #4
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 4
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[3]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("4", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Confirm each has the priority we expect
        assertEquals("TTTTT", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT2", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT3", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)

        assertEquals("TTTTT4", existingContacts[3].firstName)
        assertEquals("4", existingContacts[3].priority)

        // Now DELETE the second one
        map = personEmergencyContactService.populateEmergencyContact(existingContacts[1])
        map.id = existingContacts[1].id

        personEmergencyContactService.deleteEmergencyContactWithPriorityShuffle(map)

        // Confirm each has the priority we expect
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        assertEquals("TTTTT", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT3", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT4", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)
    }

    @Test
    void testDeleteEmergencyContactWithPriorityShuffleDoDeleteLast() {
        // Check all existing emergency contacts for the same user
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Create new entity #4
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 4
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[3]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("4", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Confirm each has the priority we expect
        assertEquals("TTTTT", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT2", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT3", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)

        assertEquals("TTTTT4", existingContacts[3].firstName)
        assertEquals("4", existingContacts[3].priority)

        // Now DELETE the last one
        map = personEmergencyContactService.populateEmergencyContact(existingContacts[3])
        map.id = existingContacts[3].id

        personEmergencyContactService.deleteEmergencyContactWithPriorityShuffle(map)

        // Confirm each has the priority we expect
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        assertEquals("TTTTT", existingContacts[0].firstName)
        assertEquals("1", existingContacts[0].priority)

        assertEquals("TTTTT2", existingContacts[1].firstName)
        assertEquals("2", existingContacts[1].priority)

        assertEquals("TTTTT3", existingContacts[2].firstName)
        assertEquals("3", existingContacts[2].priority)
    }

    // BEGIN Test that records which should be unaffected by CRUD and reprioritization operations have
    // the same modifiedBy and modifiedByDate values after said operations.
    @Test
    void testCreateEmergencyContactWithPriorityShuffleAddMultipleContactsAndCheckLastModified() {
        def GRAILS_USER = 'GRAILS_USER'
        def SAISUSR = 'SAISUSR'
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        sleep(2000) // Cause different lastModified date than the records above
        login(SAISUSR, 'u_pick_it')

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def contacts = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)

        // The first and second contacts' "last modified" times should be quite close, as they were created at
        // about the same time.
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[1].lastModified.getTime()) < 1800)

        // The second and third contacts' "last modified" times should differ by about 1000 ms, as the third one
        // was inserted later.
        assertTrue('Emergency contact "modified by" times are similar.', Math.abs(contacts[1].lastModified.getTime() - contacts[2].lastModified.getTime()) > 1800)

        // The first and second contacts' lastModifiedBy should be one value, while the third should be another (see the login above in this test)
        assertEquals GRAILS_USER, contacts[0].lastModifiedBy.toUpperCase()
        assertEquals GRAILS_USER, contacts[1].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[2].lastModifiedBy.toUpperCase()
    }

    @Test
    void testCreateEmergencyContactWithPriorityShuffleDoInsertAtBeginningAndCheckLastModified() {
        def SAISUSR = 'SAISUSR'
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Insert entity #4 AT BEGINNING
        sleep(2000) // Cause different lastModified date than the records above
        login(SAISUSR, 'u_pick_it')

        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 1
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def contacts = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)

        // All contacts' "last modified" times should be quite close as, although the one now at priority 1 was
        // inserted one second later, all the others had to be updated to new priorities.
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[1].lastModified.getTime()) < 1800)
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[2].lastModified.getTime()) < 1800)
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[3].lastModified.getTime()) < 1800)

        // All contacts' lastModifiedBy should be the same value, as they were updated by SAISUSR (see the login above
        // in this test) at the same time as the insert was done and the existing ones were reprioritized.
        assertEquals SAISUSR, contacts[0].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[1].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[2].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[3].lastModifiedBy.toUpperCase()
    }

    @Test
    void testCreateEmergencyContactWithPriorityShuffleDoInsertInMiddleAndCheckLastModified() {
        def GRAILS_USER = 'GRAILS_USER'
        def SAISUSR = 'SAISUSR'
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Insert entity #4 IN MIDDLE
        sleep(2000) // Cause different lastModified date than the records above
        login(SAISUSR, 'u_pick_it')

        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def contacts = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)

        // The first and second contacts' "last modified" times should differ by about 1000 ms, as the second one
        // was inserted later.
        assertTrue('Emergency contact "modified by" times are similar.', Math.abs(contacts[0].lastModified.getTime() - contacts[1].lastModified.getTime()) > 1800)

        // The second, third, and fourth "last modified" times should be quite close as the second was inserted, and
        // the priority of the third and fourth had to be updated to new priorities.
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[1].lastModified.getTime() - contacts[2].lastModified.getTime()) < 1800)
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[1].lastModified.getTime() - contacts[3].lastModified.getTime()) < 1800)

        // The first contact's lastModifiedBy should be one value, while the second, third, and fourth should be
        // another (see the login above in this test).
        assertEquals GRAILS_USER, contacts[0].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[1].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[2].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[3].lastModifiedBy.toUpperCase()
    }

    @Test
    void testUpdateEmergencyContactWithPriorityShuffleDoMoveFromMiddleToFirstAndCheckLastModified() {
        def GRAILS_USER = 'GRAILS_USER'
        def SAISUSR = 'SAISUSR'
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Create new entity #4
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 4
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[3]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("4", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Now MOVE the third one to the beginning
        sleep(2000) // Cause different lastModified date than the records above
        login(SAISUSR, 'u_pick_it')

        map = personEmergencyContactService.populateEmergencyContact(existingContacts[2])
        map.id = existingContacts[2].id
        map.priority = 1

        personEmergencyContactService.updateEmergencyContactWithPriorityShuffle(map)
        def contacts = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)

        // The first, second, and third contacts' "last modified" times should be quite close, as they were created at
        // about the same time.
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[1].lastModified.getTime()) < 1800)
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[2].lastModified.getTime()) < 1800)

        // The third and fourth contacts' "last modified" times should differ by about 1000 ms, as the third one
        // was inserted later.
        assertTrue('Emergency contact "modified by" times are similar.', Math.abs(contacts[2].lastModified.getTime() - contacts[3].lastModified.getTime()) > 1800)

        // The first, second, and third contacts' lastModifiedBy should be one value, while the fourth should be
        // another (see the login above in this test)
        assertEquals SAISUSR, contacts[0].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[1].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[2].lastModifiedBy.toUpperCase()
        assertEquals GRAILS_USER, contacts[3].lastModifiedBy.toUpperCase()
    }

    @Test
    void testUpdateEmergencyContactWithPriorityShuffleDoMoveFromMiddleToLastAndCheckLastModified() {
        def GRAILS_USER = 'GRAILS_USER'
        def SAISUSR = 'SAISUSR'
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Create new entity #4
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 4
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[3]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("4", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Now MOVE the third one to the end
        sleep(2000) // Cause different lastModified date than the records above
        login(SAISUSR, 'u_pick_it')

        map = personEmergencyContactService.populateEmergencyContact(existingContacts[2])
        map.id = existingContacts[2].id
        map.priority = 4

        personEmergencyContactService.updateEmergencyContactWithPriorityShuffle(map)
        def contacts = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)

        // The first and second contacts' "last modified" times should be quite close, as they were created at
        // about the same time.
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[1].lastModified.getTime()) < 1800)

        // The third and fourth contacts' "last modified" times should be quite close, as they were modified at
        // about the same time.
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[2].lastModified.getTime() - contacts[3].lastModified.getTime()) < 1800)

        // The second and third contacts' "last modified" times should differ by about 1000 ms, as the third one
        // was updated later.
        assertTrue('Emergency contact "modified by" times are similar.', Math.abs(contacts[1].lastModified.getTime() - contacts[2].lastModified.getTime()) > 1800)

        // The first and second contacts' lastModifiedBy should be one value, while the third and fourth should be
        // another (see the login above in this test)
        assertEquals GRAILS_USER, contacts[0].lastModifiedBy.toUpperCase()
        assertEquals GRAILS_USER, contacts[1].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[2].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[3].lastModifiedBy.toUpperCase()
    }

    @Test
    void testUpdateEmergencyContactWithPriorityShuffleDoMoveFromFirstToLastAndCheckLastModified() {
        def SAISUSR = 'SAISUSR'
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Create new entity #4
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 4
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[3]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("4", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Now MOVE the first one to the end
        sleep(2000) // Cause different lastModified date than the records above
        login(SAISUSR, 'u_pick_it')

        map = personEmergencyContactService.populateEmergencyContact(existingContacts[0])
        map.id = existingContacts[0].id
        map.priority = 4

        personEmergencyContactService.updateEmergencyContactWithPriorityShuffle(map)
        def contacts = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)

        // All contacts' "last modified" times should be quite close as, although the one now at priority 4 was
        // moved there one second later, all the others had to be updated to new priorities.
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[1].lastModified.getTime()) < 1800)
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[2].lastModified.getTime()) < 1800)
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[3].lastModified.getTime()) < 1800)

        // All contacts' lastModifiedBy should be the same value, as they were updated by SAISUSR (see the login above
        // in this test) at the same time as the update was done and the existing ones were reprioritized.
        assertEquals SAISUSR, contacts[0].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[1].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[2].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[3].lastModifiedBy.toUpperCase()
    }

    @Test
    void testUpdateEmergencyContactWithPriorityShuffleDoMoveFromLastToFirstAndCheckLastModified() {
        def SAISUSR = 'SAISUSR'
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Create new entity #4
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 4
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[3]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("4", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Now MOVE the last one to the beginning
        sleep(2000) // Cause different lastModified date than the records above
        login(SAISUSR, 'u_pick_it')

        map = personEmergencyContactService.populateEmergencyContact(existingContacts[3])
        map.id = existingContacts[3].id
        map.priority = 1

        personEmergencyContactService.updateEmergencyContactWithPriorityShuffle(map)
        def contacts = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)

        // All contacts' "last modified" times should be quite close as, although the one now at priority 1 was
        // moved there one second later, all the others had to be updated to new priorities.
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[1].lastModified.getTime()) < 1800)
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[2].lastModified.getTime()) < 1800)
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[3].lastModified.getTime()) < 1800)

        // All contacts' lastModifiedBy should be the same value, as they were updated by SAISUSR (see the login above
        // in this test) at the same time as the update was done and the existing ones were reprioritized.
        assertEquals SAISUSR, contacts[0].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[1].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[2].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[3].lastModifiedBy.toUpperCase()
    }

    @Test
    void testDeleteEmergencyContactWithPriorityShuffleDoDeleteFirstAndCheckLastModified() {
        def SAISUSR = 'SAISUSR'
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Create new entity #4
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 4
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[3]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("4", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Now DELETE the first one
        sleep(2000) // Cause different lastModified date than the records above
        login(SAISUSR, 'u_pick_it')

        map = personEmergencyContactService.populateEmergencyContact(existingContacts[0])
        map.id = existingContacts[0].id

        personEmergencyContactService.deleteEmergencyContactWithPriorityShuffle(map)
        def contacts = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)

        // All contacts' "last modified" times should be quite close as, although the one originally at priority 1 was
        // deleted there one second later, all the others had to be updated to new priorities.
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[1].lastModified.getTime()) < 1800)
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[2].lastModified.getTime()) < 1800)

        // All contacts' lastModifiedBy should be the same value, as they were updated by SAISUSR (see the login above
        // in this test) at the same time as the delete was done and the existing ones were reprioritized.
        assertEquals SAISUSR, contacts[0].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[1].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[2].lastModifiedBy.toUpperCase()
    }

    @Test
    void testDeleteEmergencyContactWithPriorityShuffleDoDeleteFromMiddleAndCheckLastModified() {
        def GRAILS_USER = 'GRAILS_USER'
        def SAISUSR = 'SAISUSR'
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Create new entity #4
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 4
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[3]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("4", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Now DELETE the second one
        sleep(2000) // Cause different lastModified date than the records above
        login(SAISUSR, 'u_pick_it')

        map = personEmergencyContactService.populateEmergencyContact(existingContacts[1])
        map.id = existingContacts[1].id

        personEmergencyContactService.deleteEmergencyContactWithPriorityShuffle(map)
        def contacts = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)

        // The first and second contacts' "last modified" times should differ by about 1000 ms, as the one originally
        // in the second position was deleted later and the ones below it were moved up in priority.
        assertTrue('Emergency contact "modified by" times are similar.', Math.abs(contacts[0].lastModified.getTime() - contacts[1].lastModified.getTime()) > 1800)

        // The second and third contacts' "last modified" times should be quite close, as they were updated at
        // about the same time.
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[1].lastModified.getTime() - contacts[2].lastModified.getTime()) < 1800)

        // The first contact's lastModifiedBy should be one value, while the second and third should be
        // another (see the login above in this test)
        assertEquals GRAILS_USER, contacts[0].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[1].lastModifiedBy.toUpperCase()
        assertEquals SAISUSR, contacts[2].lastModifiedBy.toUpperCase()
    }

    @Test
    void testDeleteEmergencyContactWithPriorityShuffleDoDeleteLastAndCheckLastModified() {
        def GRAILS_USER = 'GRAILS_USER'
        def SAISUSR = 'SAISUSR'
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)

        // No contacts exist yet
        assertEquals(0, existingContacts.size())

        // Create new entity #1
        def map = newValidForCreatePersonEmergencyContact()

        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        def personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[0]

        assertEquals "TTTTT", personEmergencyContact.firstName
        assertEquals("1", personEmergencyContact.priority)

        // Create new entity #2
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT2"
        map.priority = 2
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[1]

        assertEquals "TTTTT2", personEmergencyContact.firstName
        assertEquals("2", personEmergencyContact.priority)

        // Create new entity #3
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT3"
        map.priority = 3
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[2]

        assertEquals "TTTTT3", personEmergencyContact.firstName
        assertEquals("3", personEmergencyContact.priority)

        // Create new entity #4
        map = newValidForCreatePersonEmergencyContact()
        map.firstName = "TTTTT4"
        map.priority = 4
        personEmergencyContactService.createEmergencyContactWithPriorityShuffle(map)
        personEmergencyContact = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)[3]

        assertEquals "TTTTT4", personEmergencyContact.firstName
        assertEquals("4", personEmergencyContact.priority)

        // Confirm 4 contacts exist
        existingContacts = personEmergencyContactService.getEmergencyContactsByPidm(pidm)
        assertEquals(4, existingContacts.size())

        // Now DELETE the last one
        sleep(2000) // Cause different lastModified date than the records above
        login(SAISUSR, 'u_pick_it')

        map = personEmergencyContactService.populateEmergencyContact(existingContacts[3])
        map.id = existingContacts[3].id

        personEmergencyContactService.deleteEmergencyContactWithPriorityShuffle(map)
        def contacts = personEmergencyContactService.getEmergencyContactsByPidm(map.pidm)

        // The first, second, and third contacts' "last modified" times should be quite close, as they were created at
        // about the same time, and they were not affected by the deleted one.
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[1].lastModified.getTime()) < 1800)
        assertTrue('Emergency contact "modified by" times differ.', Math.abs(contacts[0].lastModified.getTime() - contacts[2].lastModified.getTime()) < 1800)

        // All contacts' lastModifiedBy should be the same value, as they were created at
        // about the same time, and they were not affected by the deleted one.
        assertEquals GRAILS_USER, contacts[0].lastModifiedBy.toUpperCase()
        assertEquals GRAILS_USER, contacts[1].lastModifiedBy.toUpperCase()
        assertEquals GRAILS_USER, contacts[2].lastModifiedBy.toUpperCase()
    }
    // END Test that records which should be unaffected by CRUD and reprioritization operations have
    // the same modifiedBy and modifiedByDate values after said operations.

    @Test
    void testPopulateEmergencyContact() {
        def personEmergencyContact = newValidForCreatePersonEmergencyContact()
        def map = [domainModel: personEmergencyContact]
        personEmergencyContact = personEmergencyContactService.create(map)

        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def out = personEmergencyContactService.populateEmergencyContact(personEmergencyContact)

        assertEquals pidm, out.pidm
        assertEquals i_success_priority, out.priority
        assertEquals i_success_relationship.code, out.relationship.code
        assertEquals i_success_relationship.description, out.relationship.description
        assertEquals i_success_firstName, out.firstName
        assertEquals i_success_middleInitial, out.middleInitial
        assertEquals i_success_lastName, out.lastName
        assertEquals i_success_surnamePrefix, out.surnamePrefix
        assertEquals i_success_addressType.code, out.addressType.code
        assertEquals i_success_addressType.description, out.addressType.description
        assertEquals i_success_houseNumber, out.houseNumber
        assertEquals i_success_streetLine1, out.streetLine1
        assertEquals i_success_streetLine2, out.streetLine2
        assertEquals i_success_streetLine3, out.streetLine3
        assertEquals i_success_streetLine4, out.streetLine4
        assertEquals i_success_city, out.city
        assertEquals i_success_zip, out.zip
        assertEquals i_success_state.code, out.state.code
        assertEquals i_success_state.description, out.state.description
        assertEquals i_success_nation.code, out.nation.code
        assertEquals i_success_nation.nation, out.nation.nation
        assertEquals i_success_phoneArea, out.phoneArea
        assertEquals i_success_phoneNumber, out.phoneNumber
        assertEquals i_success_phoneExtension, out.phoneExtension
        assertEquals i_success_countryPhone, out.countryPhone
    }


    private def newValidForCreatePersonEmergencyContact() {
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def personEmergencyContact = new PersonEmergencyContact(
                pidm: pidm,
                priority: i_success_priority,
                lastName: i_success_lastName,
                firstName: i_success_firstName,
                middleInitial: i_success_middleInitial,
                streetLine1: i_success_streetLine1,
                streetLine2: i_success_streetLine2,
                streetLine3: i_success_streetLine3,
                city: i_success_city,
                zip: i_success_zip,
                phoneArea: i_success_phoneArea,
                phoneNumber: i_success_phoneNumber,
                phoneExtension: i_success_phoneExtension,
                surnamePrefix: i_success_surnamePrefix,
                countryPhone: i_success_countryPhone,
                houseNumber: i_success_houseNumber,
                streetLine4: i_success_streetLine4,

                state: i_success_state,
                nation: i_success_nation,
                relationship: i_success_relationship,
                addressType: i_success_addressType
        )
        return personEmergencyContact
    }


    private def newInvalidForCreatePersonEmergencyContact() {
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def personEmergencyContact = new PersonEmergencyContact(
                pidm: pidm,
                priority: i_failure_priority,
                lastName: i_failure_lastName,
                firstName: i_failure_firstName,
                middleInitial: i_failure_middleInitial,
                streetLine1: i_failure_streetLine1,
                streetLine2: i_failure_streetLine2,
                streetLine3: i_failure_streetLine3,
                city: i_failure_city,
                zip: i_failure_zip,
                phoneArea: i_failure_phoneArea,
                phoneNumber: i_failure_phoneNumber,
                phoneExtension: i_failure_phoneExtension,
                surnamePrefix: i_failure_surnamePrefix,
                countryPhone: i_failure_countryPhone,
                houseNumber: i_failure_houseNumber,
                streetLine4: i_failure_streetLine4,

                state: i_failure_state,
                nation: i_failure_nation,
                relationship: i_failure_relationship,
                addressType: i_failure_addressType
        )
        return personEmergencyContact
    }
}
