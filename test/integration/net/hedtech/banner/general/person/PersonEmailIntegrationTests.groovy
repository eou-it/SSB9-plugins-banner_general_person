/** *******************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 ********************************************************************************* */
/**
 Banner Automator Version: 1.24
 Generated: Thu Aug 04 14:06:16 EDT 2011 
 */
package net.hedtech.banner.general.person

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.general.system.EmailType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class PersonEmailIntegrationTests extends BaseIntegrationTestCase {

    /*PROTECTED REGION ID(personemail_domain_integration_test_data) ENABLED START*/
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
    /*PROTECTED REGION END*/


    protected void setUp() {
        formContext = ['SOAIDEN'] // Since we are not testing a controller, we need to explicitly set this (removing GEAPART because of GUOBOBS_UI_VERSION = B)
        super.setUp()
        initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction
    void initializeTestDataForReferences() {
        //Valid test data (For success tests)
        i_success_emailType = EmailType.findByCode("CAMP")
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidPersonEmail() {
        def personEmail = newValidForCreatePersonEmail()
        personEmail.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull personEmail.id
    }


    void testCreateInvalidPersonEmail() {
        def personEmail = newInvalidForCreatePersonEmail()
        shouldFail(ValidationException) {
            personEmail.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidPersonEmail() {
        def personEmail = newValidForCreatePersonEmail()
        personEmail.save(failOnError: true, flush: true)
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
        personEmail.save(failOnError: true, flush: true)
        //Assert for sucessful update
        personEmail = PersonEmail.get(personEmail.id)
        assertEquals 1L, personEmail?.version
        assertEquals u_success_statusIndicator, personEmail.statusIndicator
        assertEquals u_success_preferredIndicator, personEmail.preferredIndicator
        assertEquals u_success_commentData, personEmail.commentData
        assertEquals u_success_displayWebIndicator, personEmail.displayWebIndicator
    }


    void testUpdateInvalidPersonEmail() {
        def personEmail = newValidForCreatePersonEmail()
        personEmail.save(failOnError: true, flush: true)
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

        shouldFail(ValidationException) {
            personEmail.save(failOnError: true, flush: true)
        }
    }


    void testDeletePersonEmail() {
        def personEmail = newValidForCreatePersonEmail()
        personEmail.save(failOnError: true, flush: true)
        def id = personEmail.id
        assertNotNull id
        personEmail.delete()
        assertNull PersonEmail.get(id)
    }


    void testValidation() {
        def personEmail = newInvalidForCreatePersonEmail()
        assertFalse "PersonEmail could not be validated as expected due to ${personEmail.errors}", personEmail.validate()
    }


    void testNullValidationFailure() {
        def personEmail = new PersonEmail()
        assertFalse "PersonEmail should have failed validation", personEmail.validate()
        assertErrorsFor personEmail, 'nullable',
                [
                        'pidm',
                        'emailAddress',
                        'statusIndicator',
                        'preferredIndicator',
                        'displayWebIndicator',
                        'emailType'
                ]
        assertNoErrorsFor personEmail,
                [
                        'commentData'
                ]
    }


    void testMaxSizeValidationFailures() {
        def personEmail = new PersonEmail(
                commentData: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "PersonEmail should have failed validation", personEmail.validate()
        assertErrorsFor personEmail, 'maxSize', ['commentData']
    }


    void testOptimisticLock() {
        def personEmail = newValidForCreatePersonEmail()
        personEmail.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GOREMAL set GOREMAL_VERSION = 999 where GOREMAL_SURROGATE_ID = ?", [personEmail.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        personEmail.preferredIndicator = u_success_preferredIndicator
        personEmail.commentData = u_success_commentData
        personEmail.displayWebIndicator = u_success_displayWebIndicator
        shouldFail(HibernateOptimisticLockingFailureException) {
            personEmail.save(failOnError: true, flush: true)
        }
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


    def testFetchByPidmAndStatusAndWebDisplayAndPreferredIndicator() {
        def results = PersonEmail.fetchByPidmAndStatusAndWebDisplayAndPreferredIndicator(PersonUtility.getPerson("966049236").pidm, 'A', 'Y', 'Y')

        assertTrue results.size() == 1

        def res = results.get(0)


        assertEquals res.version, 0
        assertEquals res.pidm, 33784
        assertEquals res.emailAddress, "einstein2be@verizon.net"
        assertEquals res.statusIndicator, "A"
        assertEquals res.preferredIndicator, true
        assertEquals res.commentData, null
        assertEquals res.displayWebIndicator, true
        assertEquals res.dataOrigin, "Banner"

    }


    def testFetchFirstByPidmAndStatusAndWebDisplayAndPreferredIndicator() {
        def results = PersonEmail.fetchByPidmAndStatusAndWebDisplayAndPreferredIndicator(PersonUtility.getPerson("966049236").pidm, 'A', 'Y', 'Y')

        assertTrue results.size() == 1

        def res = results.get(0)

        def email = PersonEmail.fetchFirstByPidmAndStatusAndWebDisplayAndPreferredIndicator(PersonUtility.getPerson("966049236").pidm, 'A', 'Y', 'Y')

        assertEquals res.emailAddress, email

    }


    def testFetchFirstByPidmAndStatus() {
        def emails = PersonEmail.findAllByPidm(PersonUtility.getPerson("HOS00003").pidm)
        assertEquals 2, emails.size()
        emails.each {
            assertTrue it.statusIndicator == "A"
        }
        assertNotNull emails.find { it.displayWebIndicator }
        assertNotNull emails.find { !it.displayWebIndicator }
        def results = PersonEmail.fetchByPidmAndStatus(PersonUtility.getPerson("HOS00003").pidm, 'A')

        assertEquals 2, results.size()
    }


    def testFetchFirstByPidmAndStatusAndWebDisplayAndPreferredIndicatorWhenNoneExists() {
        def pidm = PersonUtility.getPerson("HOSL0003").pidm
        assertNotNull pidm
        def results = PersonEmail.fetchByPidmAndStatusAndWebDisplayAndPreferredIndicator(PersonUtility.getPerson("HOSL0003").pidm, 'A', 'Y', 'Y')

        assertTrue results.size() == 0

        def email = PersonEmail.fetchFirstByPidmAndStatusAndWebDisplayAndPreferredIndicator(PersonUtility.getPerson("HOSL0003").pidm, 'A', 'Y', 'Y')

        assertNull email

    }

}
