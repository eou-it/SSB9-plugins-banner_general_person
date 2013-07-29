/*********************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonIdentificationNameCurrentServiceIntegrationTests extends BaseIntegrationTestCase {

    def personIdentificationNameCurrentService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreatePersonIdentificationNameCurrent() {
        def personIdentificationNameCurrent = newPersonIdentificationNameCurrent("ID-T00001")
        personIdentificationNameCurrent = personIdentificationNameCurrentService.create([domainModel: personIdentificationNameCurrent])

        assertNotNull personIdentificationNameCurrent?.id
        assertNotNull personIdentificationNameCurrent.pidm
        assertEquals "ID-T00001", personIdentificationNameCurrent.bannerId
        assertEquals "Adams", personIdentificationNameCurrent.lastName
        assertEquals "Troy", personIdentificationNameCurrent.firstName
        assertEquals "W", personIdentificationNameCurrent.middleName
        assertEquals "P", personIdentificationNameCurrent.entityIndicator
        assertNull personIdentificationNameCurrent.changeIndicator
        assertEquals "BRTH", personIdentificationNameCurrent.nameType.code
        assertEquals 0L, personIdentificationNameCurrent.version
    }


    void testCreateNonPersonIdentificationNameCurrent() {
        def personIdentificationNameCurrent = newNonPersonIdentificationNameCurrent("ID-T00001")
        personIdentificationNameCurrent = personIdentificationNameCurrentService.create([domainModel: personIdentificationNameCurrent])

        assertNotNull personIdentificationNameCurrent?.id
        assertNotNull personIdentificationNameCurrent.pidm
        assertEquals "ID-T00001", personIdentificationNameCurrent.bannerId
        assertEquals "Acme Rockets and Anvils", personIdentificationNameCurrent.lastName
        assertNull personIdentificationNameCurrent.firstName
        assertNull personIdentificationNameCurrent.middleName
        assertEquals "C", personIdentificationNameCurrent.entityIndicator
        assertNull personIdentificationNameCurrent.changeIndicator
        assertEquals "CORP", personIdentificationNameCurrent.nameType.code
        assertEquals 0L, personIdentificationNameCurrent.version
    }


    void testCreateWithInvalidChangeIndicator() {
        def personIdentificationNameCurrent = newPersonIdentificationNameCurrent("ID-T00001")
        personIdentificationNameCurrent.changeIndicator = "I"

        try {
            personIdentificationNameCurrent = personIdentificationNameCurrentService.create([domainModel: personIdentificationNameCurrent]) fail "Should have failed because change indicator must be null."
            fail "Should have failed because you cannot create a current identification record with a non-null change indicator."
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "changeIndicatorMustBeNull"
        }
    }


    void testCreateWhereBannerIdExists() {
        def personList = []
        def person1 = newPersonIdentificationNameCurrent("ID-T00001")
        def person2 = newPersonIdentificationNameCurrent("ID-T00001")
        personList << [domainModel:  person1]
        personList << [domainModel:  person2]

        try {
            personIdentificationNameCurrentService.create(personList)
            fail "Should have failed because you cannot create two persons with the same banner id."
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot create new identification record, since the ID provided is in use by another person/non-person."
        }
    }


    void testUpdatePersonIdentificationNameCurrent() {
        def personIdentificationNameCurrent = setupNewPersonIdentificationNameCurrent("P")

        personIdentificationNameCurrent.bannerId = "UPD-001"

        try {
            personIdentificationNameCurrentService.update([domainModel: personIdentificationNameCurrent])
            fail("This should have failed with @@r1:unsupported.operation")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "unsupported.operation"
        }
    }


    void testDeletePersonIdentificationNameCurrent() {
        def personIdentificationNameCurrent = setupNewPersonIdentificationNameCurrent("P")
        def id = personIdentificationNameCurrent.id

        try {
            personIdentificationNameCurrentService.delete([domainModel: personIdentificationNameCurrent])
            fail "Should have failed because you cannot delete the current spriden record."
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot delete current record."
        }
    }

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
                lastName: "Acme Rockets and Anvils",
                changeIndicator: null,
                entityIndicator: "C",
                nameType: NameType.findByCode("CORP")
        )

        return personIdentificationNameCurrent
    }

}
