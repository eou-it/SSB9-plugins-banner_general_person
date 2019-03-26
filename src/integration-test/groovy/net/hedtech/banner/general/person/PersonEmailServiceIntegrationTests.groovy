/*********************************************************************************
Copyright 2012-2019 Ellucian Company L.P. and its affiliates.
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
        def pidmList = [PersonUtility.getPerson("HOS00003").pidm, PersonUtility.getPerson("STUAFR004").pidm]
        def results = personEmailService.fetchAllActiveEmails(pidmList, ['PERS', 'CAMP', 'MA'] as Set)

        assertEquals 3, results.size()
        assertTrue results[0] instanceof PersonEmail
        assertTrue results[1] instanceof PersonEmail
        assertTrue results[2] instanceof PersonEmail

        def foundCount = 0
        results.each {
            if (it.emailType.code == "PERS") {
                foundCount++
                assertEquals "Hank4@college.edu", it.emailAddress
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

    @Test
    void testGetDisplayableEmails(){
        def pidm = PersonUtility.getPerson("GDP000001").pidm

        def emails = personEmailService.getDisplayableEmails(pidm)

        assertEquals 2, emails.size()
        assertEquals 'ansbates@telstra.com', emails[0].emailAddress
        assertEquals null, emails[0].lastModified
    }

    @Test
    void testCastEmailForUpdate(){
        def emailMap = [:]
        emailMap.id = 9899
        emailMap.version = 0
        emailMap.emailAddress = 'test@123.com'

        def email = personEmailService.castEmailForUpdate(emailMap)

        assertEquals 9899, email.id
        assertEquals 0, email.version
        assertEquals 'test@123.com', email.emailAddress
        assertEquals PersonEmail, email.getClass()
    }

    @Test
    void testUpdatePreferredEmail(){
        def pidm = PersonUtility.getPerson("GDP000001").pidm

        def email = [:]
        email.pidm = pidm
        email.emailAddress = 'myemail@somesite.org'
        email.preferredIndicator = true

        personEmailService.updatePreferredEmail(email)

        def emails = personEmailService.getDisplayableEmails(pidm)

        assertEquals 2, emails.size()
        assertEquals 'ansbates@telstra.com', emails[0].emailAddress
        assertEquals false, emails[0].preferredIndicator
    }

    @Test
    void testInactivateEmail(){
        def pidm = PersonUtility.getPerson("GDP000001").pidm

        def email = PersonEmail.get(personEmailService.getDisplayableEmails(pidm)[0].id)
        personEmailService.inactivateEmail(email)

        def result = PersonEmail.get(email.id)

        assertEquals 1, personEmailService.getDisplayableEmails(pidm).size()
        assertEquals 'I', result.statusIndicator
        assertEquals false, result.preferredIndicator
    }

    @Test
    void testUpdateIfExistingEmailNoExist() {
        def personEmail = newValidForCreatePersonEmail()
        def emailMap = [
                pidm: personEmail.pidm,
                emailAddress: personEmail.emailAddress,
                preferredIndicator: personEmail.preferredIndicator,
                commentData: personEmail.commentData,
                displayWebIndicator: personEmail.displayWebIndicator,
                emailType: personEmail.emailType
        ]
        emailMap = personEmailService.updateIfExistingEmail(emailMap)

        assertNull emailMap.id
        assertNull emailMap.version
    }

    @Test
    void testUpdateIfExistingEmail() {
        def personEmail = newValidForCreatePersonEmail()
        personEmail.statusIndicator = 'I'
        personEmail.preferredIndicator = false
        personEmail.save()
        def emailMap = [
                pidm: personEmail.pidm,
                emailAddress: personEmail.emailAddress,
                preferredIndicator: personEmail.preferredIndicator,
                commentData: personEmail.commentData,
                emailType: personEmail.emailType
        ]
        emailMap = personEmailService.updateIfExistingEmail(emailMap)

        assertEquals personEmail.id, emailMap.id
        assertEquals personEmail.version, emailMap.version
        assertEquals 'A', emailMap.statusIndicator
    }

    @Test
    void testFetchByPidmAndTypeAndAddress() {
        def personEmail = newValidForCreatePersonEmail()
        personEmail.statusIndicator = 'I'
        personEmail.preferredIndicator = false
        personEmail.save()
        def result = personEmailService.fetchByPidmAndTypeAndAddress(personEmail.pidm, personEmail.emailType.code, personEmail.emailAddress)

        assertEquals 1, result.size()
        assertEquals i_success_emailType.code, result[0].emailType.code
        assertEquals i_success_emailAddress, result[0].emailAddress
        assertEquals 'I', result[0].statusIndicator
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

    @Test
    void testEmailAddressByVendorPidm( ) {
        def result = personEmailService.findPreferredEmailAddress( 30450 )
        assertNotNull  result
        assertNotNull result.emailAddress
    }

    @Test
    void testEmailAddressByVendorPidmAndActiveEmails( ) {
        def result = personEmailService.findPersonEmailAddressList( 30450 , [searchParam:''],  [max: 10, offset: 0] )
        assertNotNull result
        assertEquals 30450, result[0].pidm
    }

    @Test
    void testFetchByEmailAddressByVendorPidmAndSearchOption( ) {
        def pidm = PersonUtility.getPerson("VENDEMAL1").pidm
        def result = personEmailService.findPersonEmailAddressList( pidm , [searchParam:'dhareppa'],  [max: 10, offset: 0] )
        assertTrue  result.size() > 0
    }

    @Test
    void testFetchByEmailAddressByVendorPidmAndInvalidSearchOption( ) {
        def result = personEmailService.findPersonEmailAddressList( 30450 , [searchParam:'INVALID'],  [max: 10, offset: 0] )
        assertTrue result.size() == 0
    }
}
