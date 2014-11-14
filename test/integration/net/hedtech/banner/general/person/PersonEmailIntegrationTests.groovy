/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person
import org.junit.Before
import org.junit.Test
import org.junit.After

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.general.system.EmailType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class PersonEmailIntegrationTests extends BaseIntegrationTestCase {

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
    def u_success_commentData = "TTTTTest"
    def u_success_displayWebIndicator = true
    //Valid test data (For failure tests)
    def u_failure_emailType

    def u_failure_pidm = 1
    def u_failure_emailAddress = "TTTTT@msn.com"
    def u_failure_statusIndicator = "A"
    def u_failure_preferredIndicator = null
    def u_failure_commentData = "TTTTT"
    def u_failure_displayWebIndicator = true


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this (removing GEAPART because of GUOBOBS_UI_VERSION = B)
        super.setUp()
        initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction
    void initializeTestDataForReferences( ) {
        //Valid test data (For success tests)
        i_success_emailType = EmailType.findByCode( "CAMP" )
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCreateValidPersonEmail( ) {
        def personEmail = newValidForCreatePersonEmail()
        personEmail.save( failOnError: true, flush: true )
        //Test if the generated entity now has an id assigned
        assertNotNull personEmail.id
    }


    @Test
    void testCreateInvalidPersonEmail( ) {
        def personEmail = newInvalidForCreatePersonEmail()
        shouldFail( ValidationException ) {
            personEmail.save( failOnError: true, flush: true )
        }
    }


    @Test
    void testUpdateValidPersonEmail( ) {
        def personEmail = newValidForCreatePersonEmail()
        personEmail.save( failOnError: true, flush: true )
        assertNotNull personEmail.id
        assertEquals 0L, personEmail.version
        assertEquals i_success_emailAddress, personEmail.emailAddress
        assertEquals i_success_statusIndicator, personEmail.statusIndicator
        assertEquals i_success_preferredIndicator, personEmail.preferredIndicator
        assertEquals i_success_commentData, personEmail.commentData
        assertEquals i_success_displayWebIndicator, personEmail.displayWebIndicator

        //Update the entity
        personEmail.preferredIndicator = u_success_preferredIndicator
        personEmail.commentData = u_success_commentData
        personEmail.displayWebIndicator = u_success_displayWebIndicator
        personEmail.save( failOnError: true, flush: true )
        //Assert for sucessful update
        personEmail = PersonEmail.get( personEmail.id )
        assertEquals 1L, personEmail?.version
        assertEquals u_success_statusIndicator, personEmail.statusIndicator
        assertEquals u_success_preferredIndicator, personEmail.preferredIndicator
        assertEquals u_success_commentData, personEmail.commentData
        assertEquals u_success_displayWebIndicator, personEmail.displayWebIndicator
    }


    @Test
    void testUpdateInvalidPersonEmail( ) {
        def personEmail = newValidForCreatePersonEmail()
        personEmail.save( failOnError: true, flush: true )
        assertNotNull personEmail.id
        assertEquals 0L, personEmail.version
        assertEquals i_success_emailAddress, personEmail.emailAddress
        assertEquals i_success_statusIndicator, personEmail.statusIndicator
        assertEquals i_success_preferredIndicator, personEmail.preferredIndicator
        assertEquals i_success_commentData, personEmail.commentData
        assertEquals i_success_displayWebIndicator, personEmail.displayWebIndicator

        //Update the entity with invalid values
        personEmail.preferredIndicator = u_failure_preferredIndicator
        personEmail.commentData = u_failure_commentData
        personEmail.displayWebIndicator = u_failure_displayWebIndicator

        shouldFail( ValidationException ) {
            personEmail.save( failOnError: true, flush: true )
        }
    }


    @Test
    void testDeletePersonEmail( ) {
        def personEmail = newValidForCreatePersonEmail()
        personEmail.save( failOnError: true, flush: true )
        def id = personEmail.id
        assertNotNull id
        personEmail.delete()
        assertNull PersonEmail.get( id )
    }


    @Test
    void testValidation( ) {
        def personEmail = newInvalidForCreatePersonEmail()
        assertFalse "PersonEmail could not be validated as expected due to ${personEmail.errors}", personEmail.validate()
    }


    @Test
    void testNullValidationFailure( ) {
        def personEmail = new PersonEmail()
        personEmail.statusIndicator = null
        assertFalse "PersonEmail should have failed validation", personEmail.validate()
        assertErrorsFor personEmail, 'nullable',
                [
                        'pidm',
                        'emailAddress',
                        'statusIndicator',
                        'emailType'
                ]
        assertNoErrorsFor personEmail,
                [
                        'commentData'
                ]
    }


    @Test
    void testMaxSizeValidationFailures( ) {
        def personEmail = new PersonEmail(
                commentData: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' )
        assertFalse "PersonEmail should have failed validation", personEmail.validate()
        assertErrorsFor personEmail, 'maxSize', ['commentData']
    }


    @Test
    void testOptimisticLock( ) {
        def personEmail = newValidForCreatePersonEmail()
        personEmail.save( failOnError: true, flush: true )

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update GOREMAL set GOREMAL_VERSION = 999 where GOREMAL_SURROGATE_ID = ?", [personEmail.id] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        personEmail.preferredIndicator = u_success_preferredIndicator
        personEmail.commentData = u_success_commentData
        personEmail.displayWebIndicator = u_success_displayWebIndicator
        shouldFail( HibernateOptimisticLockingFailureException ) {
            personEmail.save( failOnError: true, flush: true )
        }
    }


    @Test
    void testFetchByPidmAndStatusAndWebDisplayAndPreferredIndicator( ) {
        def results = PersonEmail.fetchByPidmAndStatusAndWebDisplayAndPreferredIndicator( PersonUtility.getPerson( "966049236" ).pidm, 'A', 'Y', 'Y' )

        assertTrue results.size() == 1

        def res = results.get( 0 )


        assertEquals res.version, 0
        assertEquals res.pidm, 33784
        assertEquals res.emailAddress, "einstein2be@verizon.net"
        assertEquals res.statusIndicator, "A"
        assertEquals res.preferredIndicator, true
        assertEquals res.commentData, null
        assertEquals res.displayWebIndicator, true
        assertEquals res.dataOrigin, "Banner"

    }


    @Test
    void testFetchFirstByPidmAndStatusAndWebDisplayAndPreferredIndicator( ) {
        def results = PersonEmail.fetchByPidmAndStatusAndWebDisplayAndPreferredIndicator( PersonUtility.getPerson( "966049236" ).pidm, 'A', 'Y', 'Y' )

        assertTrue results.size() == 1

        def res = results.get( 0 )

        def email = PersonEmail.fetchFirstByPidmAndStatusAndWebDisplayAndPreferredIndicator( PersonUtility.getPerson( "966049236" ).pidm, 'A', 'Y', 'Y' )

        assertEquals res.emailAddress, email

    }


    @Test
    void testFetchFirstByPidmAndStatus( ) {
        def emails = PersonEmail.findAllByPidm( PersonUtility.getPerson( "HOS00003" ).pidm )
        assertEquals 2, emails.size()
        emails.each {
            assertTrue it.statusIndicator == "A"
        }
        assertNotNull emails.find { it.displayWebIndicator }
        assertNotNull emails.find { !it.displayWebIndicator }
        def results = PersonEmail.fetchByPidmAndStatus( PersonUtility.getPerson( "HOS00003" ).pidm, 'A' )

        assertEquals 2, results.size()
    }


    @Test
    void testFetchFirstByPidmAndStatusAndWebDisplayAndPreferredIndicatorWhenNoneExists( ) {
        def pidm = PersonUtility.getPerson( "HOSL0003" ).pidm
        assertNotNull pidm
        def results = PersonEmail.fetchByPidmAndStatusAndWebDisplayAndPreferredIndicator( PersonUtility.getPerson( "HOSL0003" ).pidm, 'A', 'Y', 'Y' )

        assertTrue results.size() == 0

        def email = PersonEmail.fetchFirstByPidmAndStatusAndWebDisplayAndPreferredIndicator( PersonUtility.getPerson( "HOSL0003" ).pidm, 'A', 'Y', 'Y' )

        assertNull email

    }


    @Test
    void testFetchListByPidmAndStatusAndWebDisplay( ) {
        def pidmList = [PersonUtility.getPerson( "966049236" ).pidm, PersonUtility.getPerson( "HOS00003" ).pidm]
        def results = PersonEmail.fetchListByPidmAndStatusAndWebDisplay( pidmList, 'A', 'Y' )

        assertEquals 2, results.size()
        assertTrue results[0] instanceof PersonEmail
        assertTrue results[1] instanceof PersonEmail
        
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
            }
        }
        assertEquals 2, foundCount
    }


    @Test
    void testFetchListByPidmAndStatus() {
        def pidmList = [PersonUtility.getPerson("966049236").pidm, PersonUtility.getPerson("HOS00003").pidm]
        def results = PersonEmail.fetchListByPidmAndStatus(pidmList, 'A')

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


    @Test
    void testFetchByEmailAddressAndActiveStatus_ActivePreferred( ) {
        PersonEmail result = PersonEmail.fetchByEmailAddressAndActiveStatus( "Ben29322@Ellucian.edu" )
        assertEquals( 29322, result.pidm )
    }


    @Test
    void testFetchByEmailAddressCaseInsensitive( ) {
        PersonEmail result = PersonEmail.fetchByEmailAddressAndActiveStatus( "beN29322@Ellucian.edu" )
        assertEquals( 29322, result.pidm )
    }


    @Test
    void testFetchByEmailAddressAndActiveStatus_ActiveNotPreferred( ) {
        def result = PersonEmail.fetchByEmailAddressAndActiveStatus( "Neil29311@Ellucian.edu" )
        assertTrue result instanceof PersonEmail
        assertEquals( 29311, result.pidm )
    }


    @Test
    void testFetchByEmailAddressAndActiveStatus_NoSuchAddress( ) {
        def result = PersonEmail.fetchByEmailAddressAndActiveStatus( "jksdhfiwjencvwe@Ellucian.edu" )
        assertNull(result)
    }


    @Test
    void testFetchByEmailAddressAndActiveStatus_Inactive( ) {
        def result = PersonEmail.fetchByEmailAddressAndActiveStatus( "Stuart29321@Ellucian.edu" )
        assertNull(result)
    }


    @Test
    void testFetchByPidmAndEmailTypeAndStatusAndWebDisplayAndPreferredIndicator() {
        def res = PersonEmail.fetchByPidmAndEmailTypeAndStatusAndWebDisplayAndPreferredIndicator(PersonUtility.getPerson("966049236").pidm, 'PERS','A', 'Y', 'Y')
        assertEquals res.version, 0
        assertEquals res.pidm, 33784
        assertEquals res.emailAddress, "einstein2be@verizon.net"
        assertEquals res.statusIndicator, "A"
        assertEquals res.preferredIndicator, true
        assertEquals res.commentData, null
        assertEquals res.displayWebIndicator, true
        assertEquals res.dataOrigin, "Banner"

    }


    private def newValidForCreatePersonEmail( ) {
        def sql = new Sql( sessionFactory.getCurrentSession().connection() )
        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
        def bannerValues = sql.firstRow( idSql )
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
        person.save( flush: true, failOnError: true )
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


    private def newInvalidForCreatePersonEmail( ) {
        def sql = new Sql( sessionFactory.getCurrentSession().connection() )
        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
        def bannerValues = sql.firstRow( idSql )
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
        person.save( flush: true, failOnError: true )
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

