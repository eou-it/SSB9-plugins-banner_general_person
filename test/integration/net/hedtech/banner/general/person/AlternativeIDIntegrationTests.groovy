/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.general.system.AdditionalIdentificationType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class AlternativeIDIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)
    def i_success_additionalIdentificationType

    def i_success_pidm = 1
    def i_success_additionalId = "TTTTT"
    //Invalid test data (For failure tests)
    def i_failure_additionalIdentificationType

    def i_failure_pidm = 1
    def i_failure_additionalId = "TTTTT"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)
    def u_success_additionalIdentificationType

    def u_success_pidm = 1
    def u_success_additionalId = "TTTTTs"
    //Valid test data (For failure tests)
    def u_failure_additionalIdentificationType

    def u_failure_pidm = 1
    def u_failure_additionalId = "TTTTT"


    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction
    void initializeTestDataForReferences() {
        //Valid test data (For success tests)
        i_success_additionalIdentificationType = AdditionalIdentificationType.findByCode("PROD")

        //Invalid test data (For failure tests)
        i_failure_additionalIdentificationType = AdditionalIdentificationType.findByCode("x")

        //Valid test data (For success tests)
        u_success_additionalIdentificationType = AdditionalIdentificationType.findByCode("PROD")

        //Valid test data (For failure tests)
        u_failure_additionalIdentificationType = AdditionalIdentificationType.findByCode("x")
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidAlternativeID() {
        def alternativeID = newValidForCreateAlternativeID()
        alternativeID.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull alternativeID.id
    }


    void testCreateInvalidAlternativeID() {
        def alternativeID = newInvalidForCreateAlternativeID()
        shouldFail(ValidationException) {
            alternativeID.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidAlternativeID() {
        def alternativeID = newValidForCreateAlternativeID()
        alternativeID.save(failOnError: true, flush: true)
        assertNotNull alternativeID.id
        assertEquals 0L, alternativeID.version
        assertEquals i_success_additionalId, alternativeID.additionalId

        //Update the entity
        alternativeID.additionalId = u_success_additionalId
        alternativeID.save(failOnError: true, flush: true)
        //Assert for sucessful update
        alternativeID = AlternativeID.get(alternativeID.id)
        assertEquals 1L, alternativeID?.version

    }


    void testUpdateInvalidAlternativeID() {
        def alternativeID = newValidForCreateAlternativeID()
        alternativeID.save(failOnError: true, flush: true)
        assertNotNull alternativeID.id
        assertEquals 0L, alternativeID.version
        assertEquals i_success_additionalId, alternativeID.additionalId

        //Update the entity with invalid values
        alternativeID.additionalIdentificationType = u_failure_additionalIdentificationType

        shouldFail(ValidationException) {
            alternativeID.save(failOnError: true, flush: true)
        }
    }


    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def alternativeID = newValidForCreateAlternativeID()

        alternativeID.save(flush: true, failOnError: true)
        alternativeID.refresh()
        assertNotNull "AlternativeID should have been saved", alternativeID.id

        // test date values -
        assertEquals date.format(today), date.format(alternativeID.lastModified)
        assertEquals hour.format(today), hour.format(alternativeID.lastModified)


    }


    void testOptimisticLock() {
        def alternativeID = newValidForCreateAlternativeID()
        alternativeID.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GV_GORADID set GORADID_VERSION = 999 where GORADID_SURROGATE_ID = ?", [alternativeID.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        alternativeID.additionalId = u_success_additionalId
        shouldFail(HibernateOptimisticLockingFailureException) {
            alternativeID.save(failOnError: true, flush: true)
        }
    }


    void testDeleteAlternativeID() {
        def alternativeID = newValidForCreateAlternativeID()
        alternativeID.save(failOnError: true, flush: true)
        def id = alternativeID.id
        assertNotNull id
        alternativeID.delete()
        assertNull AlternativeID.get(id)
    }


    void testValidation() {
        def alternativeID = newInvalidForCreateAlternativeID()
        assertFalse "AlternativeID could not be validated as expected due to ${alternativeID.errors}", alternativeID.validate()
    }


    void testNullValidationFailure() {
        def alternativeID = new AlternativeID()
        assertFalse "AlternativeID should have failed validation", alternativeID.validate()
        assertErrorsFor alternativeID, 'nullable',
                [
                        'pidm',
                        'additionalId',
                        'additionalIdentificationType'
                ]
    }


    private def newValidForCreateAlternativeID() {
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def alternativeID = new AlternativeID(
                pidm: pidm,
                additionalId: i_success_additionalId,
                additionalIdentificationType: i_success_additionalIdentificationType,
        )
        return alternativeID
    }


    private def newInvalidForCreateAlternativeID() {
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def alternativeID = new AlternativeID(
                pidm: pidm,
                additionalId: i_failure_additionalId,
                additionalIdentificationType: i_failure_additionalIdentificationType,
        )
        return alternativeID
    }

}
