/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.general.system.*
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class PriorCollegeDegreeIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidPriorCollegeDegree() {
        def priorCollegeDegree = newValidForCreatePriorCollegeDegree()
        priorCollegeDegree.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull priorCollegeDegree.id
    }


    void testCreateInvalidPriorCollegeDegree() {
        def priorCollegeDegree = newInvalidForCreatePriorCollegeDegree()
        shouldFail(ValidationException) {
            priorCollegeDegree.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidPriorCollegeDegree() {
        def priorCollegeDegree = newValidForCreatePriorCollegeDegree()
        priorCollegeDegree.save(failOnError: true, flush: true)
        assertNotNull priorCollegeDegree.id
        assertEquals 0L, priorCollegeDegree.version
        assertEquals PersonIdentificationName.findByBannerId("HOR000001").pidm, priorCollegeDegree.pidm
        assertEquals 1, priorCollegeDegree.degreeSequenceNumber
        assertEquals 200.00, priorCollegeDegree.hoursTransferred
        assertEquals 1000.00, priorCollegeDegree.gpaTransferred
        assertEquals "2014", priorCollegeDegree.degreeYear
        assertEquals "Y", priorCollegeDegree.termDegree
        assertEquals "Y", priorCollegeDegree.primaryIndicator
        assertEquals "999999", priorCollegeDegree.sourceAndBackgroundInstitution.code
        assertEquals "PHD", priorCollegeDegree.degree.code
        assertEquals "AH", priorCollegeDegree.college.code
        assertEquals "TTTTTT", priorCollegeDegree.institutionalHonor.code
        assertEquals "MA", priorCollegeDegree.educationGoal

        //Update the entity
        def institutionalHonorNew = new InstitutionalHonor(
                code: "TTTTT2",
                description: "123456789012345678901234567890",
                transcPrintIndicator: "Y",
                commencePrintIndicator: "Y",
                electronicDataInterchangeEquivalent: "TTT",
        )
        institutionalHonorNew.save(failOnError: true, flush: true)

        priorCollegeDegree.degreeSequenceNumber = 2
        priorCollegeDegree.hoursTransferred = 201.00
        priorCollegeDegree.gpaTransferred = 1001.00
        priorCollegeDegree.degreeYear = "2013"
        priorCollegeDegree.termDegree = "N"
        priorCollegeDegree.primaryIndicator = "N"
        priorCollegeDegree.degree = Degree.findByCode("MA")
        priorCollegeDegree.college = College.findByCode("BU")
        priorCollegeDegree.institutionalHonor = institutionalHonorNew
        priorCollegeDegree.educationGoal = "PH"
        priorCollegeDegree.save(failOnError: true, flush: true)

        //Assert for sucessful update
        priorCollegeDegree = PriorCollegeDegree.get(priorCollegeDegree.id)
        assertEquals 1L, priorCollegeDegree?.version
        assertEquals 2, priorCollegeDegree.degreeSequenceNumber
        assertEquals 201.00, priorCollegeDegree.hoursTransferred
        assertEquals 1001.00, priorCollegeDegree.gpaTransferred
        assertEquals "2013", priorCollegeDegree.degreeYear
        assertEquals "N", priorCollegeDegree.termDegree
        assertEquals "N", priorCollegeDegree.primaryIndicator
        assertEquals "MA", priorCollegeDegree.degree.code
        assertEquals "BU", priorCollegeDegree.college.code
        assertEquals "TTTTT2", priorCollegeDegree.institutionalHonor.code
        assertEquals "PH", priorCollegeDegree.educationGoal
    }


    void testUpdateInvalidPriorCollegeDegree() {
        def priorCollegeDegree = newValidForCreatePriorCollegeDegree()
        priorCollegeDegree.save(failOnError: true, flush: true)
        assertNotNull priorCollegeDegree.id
        assertEquals 0L, priorCollegeDegree.version
        assertEquals PersonIdentificationName.findByBannerId("HOR000001").pidm, priorCollegeDegree.pidm
        assertEquals 1, priorCollegeDegree.degreeSequenceNumber
        assertEquals 200.00, priorCollegeDegree.hoursTransferred
        assertEquals 1000.00, priorCollegeDegree.gpaTransferred
        assertEquals "2014", priorCollegeDegree.degreeYear
        assertEquals "Y", priorCollegeDegree.termDegree
        assertEquals "Y", priorCollegeDegree.primaryIndicator
        assertEquals "999999", priorCollegeDegree.sourceAndBackgroundInstitution.code
        assertEquals "PHD", priorCollegeDegree.degree.code
        assertEquals "AH", priorCollegeDegree.college.code
        assertEquals "TTTTTT", priorCollegeDegree.institutionalHonor.code
        assertEquals "MA", priorCollegeDegree.educationGoal

        //Update the entity with invalid values
        priorCollegeDegree.degreeSequenceNumber = 100
        shouldFail(ValidationException) {
            priorCollegeDegree.save(failOnError: true, flush: true)
        }
    }


    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def priorCollegeDegree = newValidForCreatePriorCollegeDegree()

        priorCollegeDegree.attendenceFrom = new Date()
        priorCollegeDegree.attendenceTo = new Date()
        priorCollegeDegree.degreeDate = new Date()

        priorCollegeDegree.save(flush: true, failOnError: true)
        priorCollegeDegree.refresh()
        assertNotNull "PriorCollegeDegree should have been saved", priorCollegeDegree.id

        // test date values -
        assertEquals date.format(today), date.format(priorCollegeDegree.lastModified)
        assertEquals hour.format(today), hour.format(priorCollegeDegree.lastModified)

        assertEquals time.format(priorCollegeDegree.attendenceFrom), "000000"
        assertEquals time.format(priorCollegeDegree.attendenceTo), "000000"
        assertEquals time.format(priorCollegeDegree.degreeDate), "000000"
    }


    void testOptimisticLock() {
        def priorCollegeDegree = newValidForCreatePriorCollegeDegree()
        priorCollegeDegree.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SV_SORDEGR set SORDEGR_VERSION = 999 where SORDEGR_SURROGATE_ID = ?", [priorCollegeDegree.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        priorCollegeDegree.degreeSequenceNumber = 2
        shouldFail(HibernateOptimisticLockingFailureException) {
            priorCollegeDegree.save(failOnError: true, flush: true)
        }
    }


    void testDeletePriorCollegeDegree() {
        def priorCollegeDegree = newValidForCreatePriorCollegeDegree()
        priorCollegeDegree.save(failOnError: true, flush: true)
        def id = priorCollegeDegree.id
        assertNotNull id
        priorCollegeDegree.delete()
        assertNull PriorCollegeDegree.get(id)
    }


    void testValidation() {
        def priorCollegeDegree = newInvalidForCreatePriorCollegeDegree()
        assertFalse "PriorCollegeDegree could not be validated as expected due to ${priorCollegeDegree.errors}", priorCollegeDegree.validate()
    }


    void testNullValidationFailure() {
        def priorCollegeDegree = new PriorCollegeDegree()
        assertFalse "PriorCollegeDegree should have failed validation", priorCollegeDegree.validate()
        assertErrorsFor priorCollegeDegree, 'nullable',
                [
                        'pidm',
                        'degreeSequenceNumber',
                        'sourceAndBackgroundInstitution',
                ]
        assertNoErrorsFor priorCollegeDegree,
                [
                        'attendenceFrom',
                        'attendenceTo',
                        'hoursTransferred',
                        'gpaTransferred',
                        'degreeDate',
                        'degreeYear',
                        'termDegree',
                        'primaryIndicator',
                        'degree',
                        'college',
                        'institutionalHonor',
                        'educationGoal'
                ]
    }


    void testMaxSizeValidationFailures() {
        def priorCollegeDegree = new PriorCollegeDegree(
                degreeYear: 'XXXXXX',
                termDegree: 'XXX',
                primaryIndicator: 'XXX')
        assertFalse "PriorCollegeDegree should have failed validation", priorCollegeDegree.validate()
        assertErrorsFor priorCollegeDegree, 'maxSize', ['degreeYear', 'termDegree', 'primaryIndicator']
    }


    private def newValidForCreatePriorCollegeDegree() {
        def priorCollege = newValidForCreatePriorCollege()
        priorCollege.save(failOnError: true, flush: true)

        def institutionalHonor = newValidForCreateInstitutionalHonor()
        institutionalHonor.save(failOnError: true, flush: true)

        def priorCollegeDegree = new PriorCollegeDegree(
                pidm: PersonIdentificationName.findByBannerId("HOR000001").pidm,
                degreeSequenceNumber: 1,
                attendenceFrom: new Date(),
                attendenceTo: new Date(),
                hoursTransferred: 200.00,
                gpaTransferred: 1000.00,
                degreeDate: new Date(),
                degreeYear: "2014",
                termDegree: "Y",
                primaryIndicator: "Y",
                sourceAndBackgroundInstitution: SourceAndBackgroundInstitution.findWhere(code: "999999"),
                degree: Degree.findByCode("PHD"),
                college: College.findByCode("AH"),
                institutionalHonor: institutionalHonor,
                educationGoal: "MA",
        )
        return priorCollegeDegree
    }


    private def newInvalidForCreatePriorCollegeDegree() {
        def priorCollegeDegree = new PriorCollegeDegree(
                pidm: null,
                degreeSequenceNumber: 100,
                sourceAndBackgroundInstitution: null,
        )
        return priorCollegeDegree
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


    private def newValidForCreateInstitutionalHonor() {
        def institutionalHonor = new InstitutionalHonor(
                code: "TTTTTT",
                description: "123456789012345678901234567890",
                transcPrintIndicator: "Y",
                commencePrintIndicator: "Y",
                electronicDataInterchangeEquivalent: "TTT",
        )
        return institutionalHonor
    }
}
