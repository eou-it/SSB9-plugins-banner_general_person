/*********************************************************************************
Copyright 2012-2016 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
package net.hedtech.banner.general.person
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.EmailType
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonEmailServiceIntegrationTests extends BaseIntegrationTestCase {

    def personEmailService

    //Test data for creating new domain instance
    //Valid test data (For success tests)
    def i_success_emailType
    def i_success_pidm = 1
    def i_success_emailAddress = "TTTTT@msn.com"
    def i_success_statusIndicator = "A"
    def i_success_preferredIndicator = true
    def i_success_commentData = "TTTTT"
    def i_success_displayWebIndicator = true

    //Invalid test data (For failure tests)
    def i_failure_emailType
    def i_failure_pidm = 1
    def i_failure_emailAddress = "TTTTT"
    def i_failure_statusIndicator = "I"
    def i_failure_preferredIndicator = true
    def i_failure_commentData = "TTTTT"
    def i_failure_displayWebIndicator = true

    //Test data for creating updating domain instance
    //Valid test data (For success tests)
    def u_success_emailType
    def u_success_pidm = 1
    def u_success_emailAddress = "TTTTT@yahoo.com"
    def u_success_statusIndicator = "A"
    def u_success_preferredIndicator = true
    def u_success_commentData = "TTTTT"
    def u_success_displayWebIndicator = true

    //Valid test data (For failure tests)
    def u_failure_emailType
    def u_failure_pidm = 1
    def u_failure_emailAddress = "TTTTT"
    def u_failure_statusIndicator = "I"
    def u_failure_preferredIndicator = true
    def u_failure_commentData = "TTTTT"
    def u_failure_displayWebIndicator = true


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
        i_success_emailType = EmailType.findByCode("CAMP")

    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testPersonEmailValidCreate() {
        def personEmail = newValidForCreatePersonEmail()
        def map = [domainModel: personEmail]
        personEmail = personEmailService.create(map)
        assertNotNull "PersonEmail ID is null in PersonEmail Service Tests Create", personEmail.id
        assertNotNull "PersonEmail emailType is null in PersonEmail Service Tests", personEmail.emailType
        assertNotNull personEmail.version
        assertNotNull personEmail.dataOrigin
        assertNotNull personEmail.lastModifiedBy
        assertNotNull personEmail.lastModified
    }


    @Test
    void testPersonEmailInvalidCreate() {
        def personEmail = newInvalidForCreatePersonEmail()
        def map = [domainModel: personEmail]
        shouldFail(ApplicationException) {
            personEmail = personEmailService.create(map)
        }
    }


    @Test
    void testPersonEmailValidUpdate() {
        def personEmail = newValidForCreatePersonEmail()
        def map = [domainModel: personEmail]
        personEmail = personEmailService.create(map)
        assertNotNull "PersonEmail ID is null in PersonEmail Service Tests Create", personEmail.id
        assertNotNull "PersonEmail emailType is null in PersonEmail Service Tests", personEmail.emailType
        assertNotNull personEmail.version
        assertNotNull personEmail.dataOrigin
        assertNotNull personEmail.lastModifiedBy
        assertNotNull personEmail.lastModified
        //Update the entity with new values
        personEmail.statusIndicator = u_success_statusIndicator
        personEmail.preferredIndicator = u_success_preferredIndicator
        personEmail.commentData = u_success_commentData
        personEmail.displayWebIndicator = u_success_displayWebIndicator

        map.domainModel = personEmail
        personEmail = personEmailService.update(map)
        // test the values
        assertEquals u_success_statusIndicator, personEmail.statusIndicator
        assertEquals u_success_preferredIndicator, personEmail.preferredIndicator
        assertEquals u_success_commentData, personEmail.commentData
        assertEquals u_success_displayWebIndicator, personEmail.displayWebIndicator
    }


    @Test
    void testPersonEmailInvalidUpdate() {
        def personEmail = newValidForCreatePersonEmail()
        def map = [domainModel: personEmail]
        personEmail = personEmailService.create(map)
        assertNotNull "PersonEmail ID is null in PersonEmail Service Tests Create", personEmail.id
        assertNotNull "PersonEmail emailType is null in PersonEmail Service Tests", personEmail.emailType
        assertNotNull personEmail.version
        assertNotNull personEmail.dataOrigin
        assertNotNull personEmail.lastModifiedBy
        assertNotNull personEmail.lastModified
        //Update the entity with new invalid values
        personEmail.statusIndicator = u_failure_statusIndicator
        personEmail.preferredIndicator = u_failure_preferredIndicator
        personEmail.commentData = u_failure_commentData
        personEmail.displayWebIndicator = u_failure_displayWebIndicator

        map.domainModel = personEmail
        shouldFail(ApplicationException) {
            personEmail = personEmailService.update(map)
        }
    }


    @Test
    void testPersonEmailPrimaryKeyUpdate() {
        def personEmail = newValidForCreatePersonEmail()
        def map = [domainModel: personEmail]
        personEmail = personEmailService.create(map)
        assertNotNull "PersonEmail ID is null in PersonEmail Service Tests Create", personEmail.id
        assertNotNull "PersonEmail emailType is null in PersonEmail Service Tests", personEmail.emailType
        assertNotNull personEmail.version
        assertNotNull personEmail.dataOrigin
        assertNotNull personEmail.lastModifiedBy
        assertNotNull personEmail.lastModified

        personEmail.emailType = EmailType.findByCode("HOME")
        try {
            personEmailService.update([domainModel: personEmail])
            fail("This should have failed with @@r1:primaryKeyFieldsCannotBeModified")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "primaryKeyFieldsCannotBeModified"
        }
    }


    @Test
    void testPersonEmailDelete() {
        def personEmail = newValidForCreatePersonEmail()
        def map = [domainModel: personEmail]
        personEmail = personEmailService.create(map)
        assertNotNull "PersonEmail ID is null in PersonEmail Service Tests Create", personEmail.id
        def id = personEmail.id
        map = [domainModel: personEmail]
        personEmailService.delete(map)
        assertNull "PersonEmail should have been deleted", personEmail.get(id)
    }


    @Test
    void testReadOnly() {
        def personEmail = newValidForCreatePersonEmail()
        def map = [domainModel: personEmail]
        personEmail = personEmailService.create(map)
        assertNotNull "PersonEmail ID is null in PersonEmail Service Tests Create", personEmail.id
        personEmail.emailType = u_success_emailType
        personEmail.pidm = PersonUtility.getPerson("HOS00001").pidm
        personEmail.emailAddress = u_success_emailAddress
        try {
            personEmailService.update([domainModel: personEmail])
            fail("This should have failed with @@r1:readonlyFieldsCannotBeModified")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }

    @Test
    void testFetchListByPidmAndCodes() {
        def pidmList = [PersonUtility.getPerson("966049236").pidm, PersonUtility.getPerson("HOS00003").pidm]
        def results = personEmailService.fetchAllActiveEmails(pidmList, ['PERS', 'CAMP', 'MA'] as Set)

        assertEquals 3, results.size()
        assertTrue results[0] instanceof PersonEmail
        assertTrue results[1] instanceof PersonEmail
        assertTrue results[2] instanceof PersonEmail

        def foundCount = 0
        results.each {
            if (it.emailType.code == "PERS") {
                foundCount++
                assertEquals "einstein2be@verizon.net", it.emailAddress
                assertTrue it.displayWebIndicator
            } else if (it.emailType.code == "MA") {
                foundCount++
                assertEquals "pamix@charter.net", it.emailAddress
                assertTrue it.displayWebIndicator
            } else if (it.emailType.code == "CAMP") {
                foundCount++
                assertEquals "pauline.amyx@charter.net", it.emailAddress
                assertFalse it.displayWebIndicator
            }
        }
        assertEquals 3, foundCount

    }


    private def newValidForCreatePersonEmail() {
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

        def personEmail = new PersonEmail(
                pidm: ipidm,
                emailAddress: i_success_emailAddress,
                statusIndicator: i_success_statusIndicator,
                preferredIndicator: i_success_preferredIndicator,
                commentData: i_success_commentData,
                displayWebIndicator: i_success_displayWebIndicator,
                emailType: i_success_emailType
        )
        return personEmail
    }


    private def newInvalidForCreatePersonEmail() {
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

        def personEmail = new PersonEmail(
                pidm: ipidm,
                emailAddress: i_failure_emailAddress,
                statusIndicator: i_failure_statusIndicator,
                preferredIndicator: i_failure_preferredIndicator,
                commentData: i_failure_commentData,
                displayWebIndicator: i_failure_displayWebIndicator,

                emailType: i_failure_emailType
        )
        return personEmail
    }
}
