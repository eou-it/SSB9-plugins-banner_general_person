/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
/*********************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonIdentificationNameAlternateServiceIntegrationTests extends BaseIntegrationTestCase {

    def personIdentificationNameAlternateService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreatePersonIdentificationNameAlternateBannerIdChange() {
        def personIdentificationNameCurrent = setupNewPersonIdentificationNameCurrent()
        def personIdentificationNameAlternate = newAlternateBannerId(personIdentificationNameCurrent, "ID-A00001")
        personIdentificationNameAlternate = personIdentificationNameAlternateService.create([domainModel: personIdentificationNameAlternate])

        def personIdentificationNameAlternates = PersonIdentificationNameAlternate.fetchAllByPidm(personIdentificationNameAlternate.pidm)
        assertEquals 1, personIdentificationNameAlternates?.size()

        personIdentificationNameAlternate = personIdentificationNameAlternates[0]
        assertNotNull personIdentificationNameAlternate?.id
        assertNotNull personIdentificationNameAlternate.pidm
        assertEquals "ID-A00001", personIdentificationNameAlternate.bannerId
        assertEquals "Adams", personIdentificationNameAlternate.lastName
        assertEquals "Troy", personIdentificationNameAlternate.firstName
        assertEquals "W", personIdentificationNameAlternate.middleName
        assertEquals "P", personIdentificationNameAlternate.entityIndicator
        assertEquals "I", personIdentificationNameAlternate.changeIndicator
        assertEquals "BRTH", personIdentificationNameAlternate.nameType.code
        assertEquals 0L, personIdentificationNameAlternate.version
    }


    void testCreatePersonIdentificationNameAlternateNameChange() {
        def personIdentificationNameCurrent = setupNewPersonIdentificationNameCurrent()
        def personIdentificationNameAlternate = newAlternateName(personIdentificationNameCurrent, [lastName: "Higgins", firstName: "Walter"])
        personIdentificationNameAlternate = personIdentificationNameAlternateService.create([domainModel: personIdentificationNameAlternate])
        def bannerId = personIdentificationNameAlternate.bannerId

        def personIdentificationNameAlternates = PersonIdentificationNameAlternate.fetchAllByPidm(personIdentificationNameAlternate.pidm)
        assertEquals 1, personIdentificationNameAlternates?.size()

        personIdentificationNameAlternate = personIdentificationNameAlternates[0]
        assertNotNull personIdentificationNameAlternate?.id
        assertNotNull personIdentificationNameAlternate.pidm
        assertEquals bannerId, personIdentificationNameAlternate.bannerId
        assertEquals "Higgins", personIdentificationNameAlternate.lastName
        assertEquals "Walter", personIdentificationNameAlternate.firstName
        assertNull personIdentificationNameAlternate.middleName
        assertEquals "P", personIdentificationNameAlternate.entityIndicator
        assertEquals "N", personIdentificationNameAlternate.changeIndicator
        assertEquals "BRTH", personIdentificationNameAlternate.nameType.code
        assertEquals 0L, personIdentificationNameAlternate.version
    }


    void testCreateNonPersonIdentificationNameAlternateBannerIdChange() {
        def personIdentificationNameCurrent = setupNewNonPersonIdentificationNameCurrent()
        def personIdentificationNameAlternate = newAlternateBannerId(personIdentificationNameCurrent, "ID-A00001")
        personIdentificationNameAlternate = personIdentificationNameAlternateService.create([domainModel: personIdentificationNameAlternate])

        def personIdentificationNameAlternates = PersonIdentificationNameAlternate.fetchAllByPidm(personIdentificationNameAlternate.pidm)
        assertEquals 1, personIdentificationNameAlternates?.size()

        personIdentificationNameAlternate = personIdentificationNameAlternates[0]

        assertNotNull personIdentificationNameAlternate?.id
        assertNotNull personIdentificationNameAlternate.pidm
        assertEquals "ID-A00001", personIdentificationNameAlternate.bannerId
        assertEquals "Acme Rockets and Anvils", personIdentificationNameAlternate.lastName
        assertNull personIdentificationNameAlternate.firstName
        assertNull personIdentificationNameAlternate.middleName
        assertEquals "C", personIdentificationNameAlternate.entityIndicator
        assertEquals "I",  personIdentificationNameAlternate.changeIndicator
        assertEquals "CORP", personIdentificationNameAlternate.nameType.code
        assertEquals 0L, personIdentificationNameAlternate.version
    }


    void testCreateNonPersonIdentificationNameAlternateNameChange() {
        def personIdentificationNameCurrent = setupNewNonPersonIdentificationNameCurrent()
        def personIdentificationNameAlternate = newAlternateName(personIdentificationNameCurrent, [lastName: "Ellucian"])
        personIdentificationNameAlternate = personIdentificationNameAlternateService.create([domainModel: personIdentificationNameAlternate])
        def bannerId = personIdentificationNameAlternate.bannerId

        def personIdentificationNameAlternates = PersonIdentificationNameAlternate.fetchAllByPidm(personIdentificationNameAlternate.pidm)
        assertEquals 1, personIdentificationNameAlternates?.size()

        personIdentificationNameAlternate = personIdentificationNameAlternates[0]
        assertNotNull personIdentificationNameAlternate?.id
        assertNotNull personIdentificationNameAlternate.pidm
        assertEquals bannerId, personIdentificationNameAlternate.bannerId
        assertEquals "Ellucian", personIdentificationNameAlternate.lastName
        assertNull personIdentificationNameAlternate.firstName
        assertNull personIdentificationNameAlternate.middleName
        assertEquals "C", personIdentificationNameAlternate.entityIndicator
        assertEquals "N", personIdentificationNameAlternate.changeIndicator
        assertEquals "CORP", personIdentificationNameAlternate.nameType.code
        assertEquals 0L, personIdentificationNameAlternate.version
    }


    void testCreateWithInvalidChangeIndicator() {
        def personIdentificationNameCurrent = setupNewPersonIdentificationNameCurrent()
        def personIdentificationNameAlternate = newAlternateBannerId(personIdentificationNameCurrent, "ID-A00001")
        personIdentificationNameAlternate.changeIndicator = null

        try {
            personIdentificationNameAlternate = personIdentificationNameAlternateService.create([domainModel: personIdentificationNameAlternate]) fail "Should have failed because change indicator must be null."
            fail "Should have failed because you cannot create an alternate identification record with a null change indicator."
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "changeIndicatorCannotBeNull"
        }
    }


    void testUpdatePersonIdentificationNameAlternate() {
        def personIdentificationNameCurrent = setupNewPersonIdentificationNameCurrent()
        def personIdentificationNameAlternate = newAlternateBannerId(personIdentificationNameCurrent, "ID-A00001")
        personIdentificationNameAlternate = personIdentificationNameAlternateService.create([domainModel: personIdentificationNameAlternate])

        personIdentificationNameAlternate.bannerId = "ID-U00001"

        try {
            personIdentificationNameAlternateService.update([domainModel: personIdentificationNameAlternate])
            fail("This should have failed with @@r1:unsupported.operation")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "unsupported.operation"
        }
    }


    void testDeletePersonIdentificationNameAlternate() {
        def personIdentificationNameCurrent = setupNewPersonIdentificationNameCurrent()
        def personIdentificationNameAlternate = newAlternateBannerId(personIdentificationNameCurrent, "ID-A00001")
        personIdentificationNameAlternate = personIdentificationNameAlternateService.create([domainModel: personIdentificationNameAlternate])
        assertNotNull personIdentificationNameAlternate?.id
        def id = personIdentificationNameAlternate.id

        personIdentificationNameAlternate = personIdentificationNameAlternate.get(id)
        assertNotNull personIdentificationNameAlternate?.id

        personIdentificationNameAlternateService.delete([domainModel: personIdentificationNameAlternate])
        assertNull "Alternate identification should have been deleted", PersonIdentificationNameAlternate.get(id)
    }

    /**
     * Creates a new person current id record.
     */
    private def setupNewPersonIdentificationNameCurrent() {
        // Generate the banner id and pidm
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
        def bannerValues = sql.firstRow(idSql)
        def bannerId = bannerValues.bannerId
        def pidm = bannerValues.pidm

        def personIdentificationNameCurrent = new PersonIdentificationNameCurrent(
                pidm: pidm,
                bannerId: bannerId,
                lastName: "Adams",
                firstName: "Troy",
                middleName: "W",
                changeIndicator: null,
                entityIndicator: "P",
                nameType: NameType.findByCode("BRTH")
        )

        personIdentificationNameCurrent.save(failOnError: true, flush: true)
        assertNotNull personIdentificationNameCurrent?.id

        return personIdentificationNameCurrent
    }

    /**
     * Creates a new non-person current id record.
     */
    private def setupNewNonPersonIdentificationNameCurrent() {
        // Generate the banner id and pidm
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
        def bannerValues = sql.firstRow(idSql)
        def bannerId = bannerValues.bannerId
        def pidm = bannerValues.pidm

        def personIdentificationNameCurrent = new PersonIdentificationNameCurrent(
                pidm: pidm,
                bannerId: bannerId,
                lastName: "Acme Rockets and Anvils",
                changeIndicator: null,
                entityIndicator: "C",
                nameType: NameType.findByCode("CORP")
        )

        personIdentificationNameCurrent.save(failOnError: true, flush: true)
        assertNotNull personIdentificationNameCurrent?.id
        assertNotNull personIdentificationNameCurrent?.pidm

        return personIdentificationNameCurrent
    }

    /**
     * Creates a new alternate id record with a banner id change.
     */
    private def newAlternateBannerId(currentPerson, String newBannerId) {
        def alternatePerson = new PersonIdentificationNameAlternate(
                pidm: currentPerson.pidm,
                bannerId: newBannerId,
                lastName: currentPerson.lastName,
                firstName: currentPerson.firstName,
                middleName: currentPerson.middleName,
                changeIndicator: "I",
                entityIndicator: currentPerson.entityIndicator,
                nameType: currentPerson.nameType
        )

        return alternatePerson
    }

    /**
     * Creates a new alternate id record with a name change.
     */
    private def newAlternateName(currentPerson, names) {
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

        return alternatePerson
    }
}
