/*********************************************************************************
 Copyright 2012 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
/*********************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonIdentificationNameAlternateServiceIntegrationTests extends BaseIntegrationTestCase {

    def personIdentificationNameAlternateService

    final def GENERATED = "GENERATED"


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
    //  Perform person alternate ID tests first.  Non-person alternate ID tests follow these tests.
    // *************************************************************************************************************

    @Test
    void testCreatePersonIdentificationNameAlternateId() {
        def person = setupNewPersonIdentificationNameCurrent()
        def origBannerId = person.bannerId

        def alternatePerson = newPersonIdentificationNameAlternate(person)
        alternatePerson.bannerId = "ID-ALT001"
        alternatePerson.changeIndicator = "I"
        personIdentificationNameAlternateService.create([domainModel: alternatePerson])

        // Check the current id
        def currentPerson = PersonIdentificationNameCurrent.get(person.id)
        assertNotNull currentPerson
        assertNull currentPerson.changeIndicator
        assertEquals origBannerId, currentPerson.bannerId
        assertEquals 0L, currentPerson.version

        // Check the alternate id
        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        alternatePerson = alternatePersons.get(0)
        assertNotNull alternatePerson.pidm
        assertEquals "ID-ALT001", alternatePerson.bannerId
        assertEquals "I", alternatePerson.changeIndicator
        assertEquals "BRTH", alternatePerson.nameType?.code
        assertEquals 0L, alternatePerson.version
    }


    @Test
    void testCreatePersonIdentificationNameAlternateName() {
        def person = setupNewPersonIdentificationNameCurrent()
        def origBannerId = person.bannerId

        def altLastName = "ALTERNATE_LAST_NAME"
        def altFirstName = "ALTERNATE_FIRST_NAME"
        def altMiddleName = "ALTERNATE_MIDDLE_NAME"

        def alternatePerson = newPersonIdentificationNameAlternate(person)
        alternatePerson.lastName = altLastName
        alternatePerson.firstName = altFirstName
        alternatePerson.middleName = altMiddleName
        alternatePerson.changeIndicator = "N"
        personIdentificationNameAlternateService.create([domainModel: alternatePerson])

        // Check the current id
        def currentPerson = PersonIdentificationNameCurrent.get(person.id)
        assertNotNull currentPerson
        assertEquals origBannerId, currentPerson.bannerId
        assertEquals "Adams", currentPerson.lastName
        assertEquals 0L, currentPerson.version

        // Check the alternate id
        def alternatePersons = PersonIdentificationNameAlternate.fetchAllByPidm(currentPerson.pidm)
        assertEquals 1, alternatePersons.size()

        alternatePerson = alternatePersons.get(0)
        assertNotNull alternatePerson?.pidm
        assertEquals "N", alternatePerson.changeIndicator
        assertEquals origBannerId, alternatePerson.bannerId
        assertEquals altLastName, alternatePerson.lastName
        assertEquals altFirstName, alternatePerson.firstName
        assertEquals altMiddleName, alternatePerson.middleName
        assertEquals 0L, alternatePerson.version
    }


    @Test
    void testCreateAlternatePersonIdentificationNameWithNullChangeIndicator() {
        def person = setupNewPersonIdentificationNameCurrent()

        def alternatePerson = newPersonIdentificationNameAlternate(person)
        alternatePerson.bannerId = 'ID-ALT001'
        alternatePerson.changeIndicator = null

        try {
            personIdentificationNameAlternateService.create([domainModel: alternatePerson])
            fail "Should have failed because change indicator must be null."
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "changeIndicatorCannotBeNull"
        }
    }


    @Test
    void testUpdateAlternateNameType() {
        def altId = "ID-ALT001"
        def person = setupNewPersonIdentificationNameCurrent()

        // Create id change
        def alternatePerson = setupNewPersonIdentificationNameAlternateId(person, altId)
        assertNotNull alternatePerson
        def alternatePersonId = alternatePerson.id

        alternatePerson.nameType = NameType.findByCode("PROF")
        alternatePerson = personIdentificationNameAlternateService.update([domainModel: alternatePerson])

        alternatePerson = PersonIdentificationNameAlternate.get(alternatePersonId)
        assertNotNull alternatePerson
        assertEquals "PROF", alternatePerson.nameType.code
    }


    @Test
    void testUpdateAlternateOtherThanNameType() {
        def person = setupNewPersonIdentificationNameCurrent()

        // Create id change
        def alternatePerson = setupNewPersonIdentificationNameAlternateId(person, "ID-ALT001")
        assertNotNull alternatePerson
        def alternatePersonId = alternatePerson.id

        alternatePerson.lastName = "Update Name"

        try {
            personIdentificationNameAlternateService.update([domainModel: alternatePerson])
            fail "should have failed because you can only update name type on alternate id records"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "The only change to history records allowed is Name Type."
        }
    }


    @Test
    void testDeleteAlternatePersonIdentificationName() {
        def person = setupNewPersonIdentificationNameCurrent()

        def altBannerId = "ID-A00001"
        def altLastName = "ALTERNATE_LAST_NAME"
        def altFirstName = "ALTERNATE_FIRST_NAME"

        // Create some alternate id records
        def alternatePerson1 = setupNewPersonIdentificationNameAlternateId(person, altBannerId)
        def alternatePerson2 = setupNewPersonIdentificationNameAlternateName(person, [lastName: altLastName, firstName: altFirstName])

        def personList = PersonIdentificationNameAlternate.fetchAllByPidm(person.pidm)
        assertEquals 2, personList.size()

        personIdentificationNameAlternateService.delete([domainModel: alternatePerson1])
        personIdentificationNameAlternateService.delete([domainModel: alternatePerson2])

        personList = PersonIdentificationNameAlternate.fetchAllByPidm(person.pidm)
        assertEquals 0, personList.size()
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
     * Creates a new alternate id record with a banner id change.
     */
    private def setupNewPersonIdentificationNameAlternateId(currentPerson, altBannerId) {
        def alternatePerson = newPersonIdentificationNameAlternate(currentPerson)
        alternatePerson.bannerId = altBannerId
        alternatePerson.changeIndicator = "I"

        alternatePerson = personIdentificationNameAlternateService.create([domainModel: alternatePerson])
        assertNotNull alternatePerson
        assertNotNull alternatePerson.id
        assertEquals altBannerId, alternatePerson.bannerId
        assertEquals 0L, alternatePerson.version

        alternatePerson
    }

    /**
     * Creates a new alternate id record with a name change.
     */
    private def setupNewPersonIdentificationNameAlternateName(currentPerson, names) {

        def alternatePerson = newPersonIdentificationNameAlternate(currentPerson)
        alternatePerson.changeIndicator = "N"
        alternatePerson.lastName = names["lastName"]
        alternatePerson.firstName = names["firstName"]
        alternatePerson.middleName = names["middleName"]

        alternatePerson = personIdentificationNameAlternateService.create([domainModel: alternatePerson])
        assertNotNull alternatePerson?.id
        assertEquals names["lastName"], alternatePerson.lastName
        assertEquals 0L, alternatePerson.version

        alternatePerson
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


    private def newPersonIdentificationNameAlternate(currentPerson) {
        def alternatePerson = new PersonIdentificationNameAlternate(
                pidm: currentPerson.pidm,
                bannerId: currentPerson.bannerId,
                lastName: currentPerson.lastName,
                firstName: currentPerson.firstName,
                middleName: currentPerson.middleName,
                changeIndicator: currentPerson.changeIndicator,
                entityIndicator: currentPerson.entityIndicator,
                nameType: currentPerson.nameType
        )

        return alternatePerson
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

}
