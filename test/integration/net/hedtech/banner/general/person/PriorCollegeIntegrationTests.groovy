/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.general.system.AdmissionRequest
import net.hedtech.banner.general.system.SourceAndBackgroundInstitution
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class PriorCollegeIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidPriorCollege() {
        def priorCollege = newValidForCreatePriorCollege()
        priorCollege.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull priorCollege.id
    }


    void testCreateInvalidPriorCollege() {
        def priorCollege = newInvalidForCreatePriorCollege()
        shouldFail(ValidationException) {
            priorCollege.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidPriorCollege() {
        def priorCollege = newValidForCreatePriorCollege()
        priorCollege.save(failOnError: true, flush: true)
        assertNotNull priorCollege.id
        assertEquals 0L, priorCollege.version
        assertEquals PersonIdentificationName.findByBannerId("HOR000001").pidm, priorCollege.pidm
        assertEquals "Y", priorCollege.officialTransaction
        assertEquals "999999", priorCollege.sourceAndBackgroundInstitution.code
        assertEquals "VISA", priorCollege.admissionRequest.code

        //Update the entity
        priorCollege.officialTransaction = null
        priorCollege.admissionRequest = AdmissionRequest.findByCode("TUTD")
        priorCollege.save(failOnError: true, flush: true)

        //Assert for sucessful update
        priorCollege = PriorCollege.get(priorCollege.id)
        assertEquals 1L, priorCollege?.version
        assertNull priorCollege.officialTransaction
        assertEquals "TUTD", priorCollege.admissionRequest.code
    }


    void testUpdateInvalidPriorCollege() {
        def priorCollege = newValidForCreatePriorCollege()
        priorCollege.save(failOnError: true, flush: true)
        assertNotNull priorCollege.id
        assertEquals 0L, priorCollege.version
        assertEquals PersonIdentificationName.findByBannerId("HOR000001").pidm, priorCollege.pidm
        assertEquals "Y", priorCollege.officialTransaction
        assertEquals "999999", priorCollege.sourceAndBackgroundInstitution.code
        assertEquals "VISA", priorCollege.admissionRequest.code

        //Update the entity with invalid values
        priorCollege.officialTransaction = "ZZ"
        shouldFail(ValidationException) {
            priorCollege.save(failOnError: true, flush: true)
        }
    }


    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def priorCollege = newValidForCreatePriorCollege()

        priorCollege.transactionRecvDate = new Date()
        priorCollege.transactionRevDate = new Date()

        priorCollege.save(flush: true, failOnError: true)
        priorCollege.refresh()
        assertNotNull "PriorCollege should have been saved", priorCollege.id

        // test date values -
        assertEquals date.format(today), date.format(priorCollege.lastModified)
        assertEquals hour.format(today), hour.format(priorCollege.lastModified)

        assertEquals time.format(priorCollege.transactionRecvDate), "000000"
        assertEquals time.format(priorCollege.transactionRevDate), "000000"
    }


    void testOptimisticLock() {
        def priorCollege = newValidForCreatePriorCollege()
        priorCollege.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SV_SORPCOL set SORPCOL_VERSION = 999 where SORPCOL_SURROGATE_ID = ?", [priorCollege.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        priorCollege.officialTransaction = null
        shouldFail(HibernateOptimisticLockingFailureException) {
            priorCollege.save(failOnError: true, flush: true)
        }
    }


    void testDeletePriorCollege() {
        def priorCollege = newValidForCreatePriorCollege()
        priorCollege.save(failOnError: true, flush: true)
        def id = priorCollege.id
        assertNotNull id
        priorCollege.delete()
        assertNull PriorCollege.get(id)
    }


    void testValidation() {
        def priorCollege = newInvalidForCreatePriorCollege()
        assertFalse "PriorCollege could not be validated as expected due to ${priorCollege.errors}", priorCollege.validate()
    }


    void testNullValidationFailure() {
        def priorCollege = new PriorCollege()
        assertFalse "PriorCollege should have failed validation", priorCollege.validate()
        assertErrorsFor priorCollege, 'nullable',
                [
                        'pidm',
                        'sourceAndBackgroundInstitution'
                ]
        assertNoErrorsFor priorCollege,
                [
                        'transactionRecvDate',
                        'transactionRevDate',
                        'officialTransaction',
                        'admissionRequest'
                ]
    }


    void testMaxSizeValidationFailures() {
        def priorCollege = new PriorCollege(
                officialTransaction: 'XXX')
        assertFalse "PriorCollege should have failed validation", priorCollege.validate()
        assertErrorsFor priorCollege, 'maxSize', ['officialTransaction']
    }


    private def newValidForCreatePriorCollege() {
        def priorCollege = new PriorCollege(
                pidm: PersonIdentificationName.findByBannerId("HOR000001").pidm,
                transactionRecvDate: new Date(),
                transactionRevDate: new Date(),
                officialTransaction: "Y",
                sourceAndBackgroundInstitution: SourceAndBackgroundInstitution.findWhere(code: "999999"),
                admissionRequest: AdmissionRequest.findByCode("VISA"),
        )
        return priorCollege
    }


    private def newInvalidForCreatePriorCollege() {
        def priorCollege = new PriorCollege(
                pidm: null,
                transactionRecvDate: new Date(),
                transactionRevDate: new Date(),
                officialTransaction: "ZZ",
                sourceAndBackgroundInstitution: null,
                admissionRequest: null,
        )
        return priorCollege
    }
}
