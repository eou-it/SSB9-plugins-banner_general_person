/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
package net.hedtech.banner.general.person

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.general.system.AdditionalIdentificationType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class AdditionalIDIntegrationTests extends BaseIntegrationTestCase {

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


    void testCreateValidAdditionalID() {
        def additionalID = newValidForCreateAdditionalID()
        additionalID.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull additionalID.id
    }


    void testCreateInvalidAdditionalID() {
        def additionalID = newInvalidForCreateAdditionalID()
        shouldFail(ValidationException) {
            additionalID.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidAdditionalID() {
        def additionalID = newValidForCreateAdditionalID()
        additionalID.save(failOnError: true, flush: true)
        assertNotNull additionalID.id
        assertEquals 0L, additionalID.version
        assertEquals i_success_additionalId, additionalID.additionalId

        //Update the entity
        additionalID.additionalId = u_success_additionalId
        additionalID.save(failOnError: true, flush: true)
        //Assert for sucessful update
        additionalID = AdditionalID.get(additionalID.id)
        assertEquals 1L, additionalID?.version

    }


    void testUpdateInvalidAdditionalID() {
        def additionalID = newValidForCreateAdditionalID()
        additionalID.save(failOnError: true, flush: true)
        assertNotNull additionalID.id
        assertEquals 0L, additionalID.version
        assertEquals i_success_additionalId, additionalID.additionalId

        //Update the entity with invalid values
        additionalID.additionalIdentificationType = u_failure_additionalIdentificationType

        shouldFail(ValidationException) {
            additionalID.save(failOnError: true, flush: true)
        }
    }


    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def additionalID = newValidForCreateAdditionalID()

        additionalID.save(flush: true, failOnError: true)
        additionalID.refresh()
        assertNotNull "AdditionalID should have been saved", additionalID.id

        // test date values -
        assertEquals date.format(today), date.format(additionalID.lastModified)
        assertEquals hour.format(today), hour.format(additionalID.lastModified)


    }


    void testOptimisticLock() {
        def additionalID = newValidForCreateAdditionalID()
        additionalID.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GV_GORADID set GORADID_VERSION = 999 where GORADID_SURROGATE_ID = ?", [additionalID.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        additionalID.additionalId = u_success_additionalId
        shouldFail(HibernateOptimisticLockingFailureException) {
            additionalID.save(failOnError: true, flush: true)
        }
    }


    void testDeleteAdditionalID() {
        def additionalID = newValidForCreateAdditionalID()
        additionalID.save(failOnError: true, flush: true)
        def id = additionalID.id
        assertNotNull id
        additionalID.delete()
        assertNull AdditionalID.get(id)
    }


    void testValidation() {
        def additionalID = newInvalidForCreateAdditionalID()
        assertFalse "AdditionalID could not be validated as expected due to ${additionalID.errors}", additionalID.validate()
    }


    void testNullValidationFailure() {
        def additionalID = new AdditionalID()
        assertFalse "AdditionalID should have failed validation", additionalID.validate()
        assertErrorsFor additionalID, 'nullable',
                [
                        'pidm',
                        'additionalId',
                        'additionalIdentificationType'
                ]
    }


    private def newValidForCreateAdditionalID() {
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def additionalID = new AdditionalID(
                pidm: pidm,
                additionalId: i_success_additionalId,
                additionalIdentificationType: i_success_additionalIdentificationType,
        )
        return additionalID
    }


    private def newInvalidForCreateAdditionalID() {
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def additionalID = new AdditionalID(
                pidm: pidm,
                additionalId: i_failure_additionalId,
                additionalIdentificationType: i_failure_additionalIdentificationType,
        )
        return additionalID
    }

}
