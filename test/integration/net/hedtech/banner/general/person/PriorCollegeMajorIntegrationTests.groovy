/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import grails.validation.ValidationException
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.*
import net.hedtech.banner.testing.BaseIntegrationTestCase

import java.text.SimpleDateFormat

class PriorCollegeMajorIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidPriorCollegeMajor() {
        def priorCollegeMajor = newValidForCreatePriorCollegeMajor()
        priorCollegeMajor.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull priorCollegeMajor.id
    }


    void testCreateInvalidPriorCollegeMajor() {
        def priorCollegeMajor = newInvalidForCreatePriorCollegeMajor()
        shouldFail(ValidationException) {
            priorCollegeMajor.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidPriorCollegeMajor() {
        def priorCollegeMajor = newValidForCreatePriorCollegeMajor()
        priorCollegeMajor.save(failOnError: true, flush: true)
        assertNotNull priorCollegeMajor.id
        assertEquals 0L, priorCollegeMajor.version
        assertEquals PersonIdentificationName.findByBannerId("HOR000001").pidm, priorCollegeMajor.pidm
        assertEquals 1, priorCollegeMajor.degreeSequenceNumber
        assertEquals "999999", priorCollegeMajor.sourceAndBackgroundInstitution.code
        assertEquals "PHD", priorCollegeMajor.degree.code
        assertEquals "EDUC", priorCollegeMajor.majorMinorConcentrationMajor.code

        priorCollegeMajor.majorMinorConcentrationMajor = MajorMinorConcentration.findByCode("ECNO")
        try {
            priorCollegeMajor.save(flush: true, failOnError: true)
            fail("this should have failed, update not allowed")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "unsupported.operation"

        }
    }


    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def priorCollegeMajor = newValidForCreatePriorCollegeMajor()

        priorCollegeMajor.save(flush: true, failOnError: true)
        priorCollegeMajor.refresh()
        assertNotNull "PriorCollegeMajor should have been saved", priorCollegeMajor.id

        // test date values -
        assertEquals date.format(today), date.format(priorCollegeMajor.lastModified)
        assertEquals hour.format(today), hour.format(priorCollegeMajor.lastModified)
    }


    void testDeletePriorCollegeMajor() {
        def priorCollegeMajor = newValidForCreatePriorCollegeMajor()
        priorCollegeMajor.save(failOnError: true, flush: true)
        def id = priorCollegeMajor.id
        assertNotNull id
        priorCollegeMajor.delete()
        assertNull PriorCollegeMajor.get(id)
    }


    void testValidation() {
        def priorCollegeMajor = newInvalidForCreatePriorCollegeMajor()
        assertFalse "PriorCollegeMajor could not be validated as expected due to ${priorCollegeMajor.errors}", priorCollegeMajor.validate()
    }


    void testNullValidationFailure() {
        def priorCollegeMajor = new PriorCollegeMajor()
        assertFalse "PriorCollegeMajor should have failed validation", priorCollegeMajor.validate()
        assertErrorsFor priorCollegeMajor, 'nullable',
                [
                        'pidm',
                        'degreeSequenceNumber',
                        'sourceAndBackgroundInstitution',
                        'majorMinorConcentrationMajor'
                ]
        assertNoErrorsFor priorCollegeMajor,
                [
                        'degree'
                ]
    }


    private def newValidForCreatePriorCollegeMajor() {
        def priorCollegeDegree = newValidForCreatePriorCollegeDegree()
        priorCollegeDegree.save(failOnError: true, flush: true)

        def priorCollegeMajor = new PriorCollegeMajor(
                pidm: priorCollegeDegree.pidm,
                degreeSequenceNumber: priorCollegeDegree.degreeSequenceNumber,
                sourceAndBackgroundInstitution: priorCollegeDegree.sourceAndBackgroundInstitution,
                degree: priorCollegeDegree.degree,
                majorMinorConcentrationMajor: MajorMinorConcentration.findByCode("EDUC"),
        )
        return priorCollegeMajor
    }


    private def newInvalidForCreatePriorCollegeMajor() {
        def priorCollegeMajor = new PriorCollegeMajor(
                pidm: null,
                degreeSequenceNumber: 100,
                sourceAndBackgroundInstitution: null,
                degree: null,
                majorMinorConcentrationMajor: null,
        )
        return priorCollegeMajor
    }


    private def newValidForCreatePriorCollegeDegree() {
        def priorCollege = newValidForCreatePriorCollege()
        priorCollege.save(failOnError: true, flush: true)

        def priorCollegeDegree = new PriorCollegeDegree(
                pidm: priorCollege.pidm,
                degreeSequenceNumber: 1,
                attendenceFrom: new Date(),
                attendenceTo: new Date(),
                hoursTransferred: 200.00,
                gpaTransferred: 1000.00,
                degreeDate: new Date(),
                degreeYear: 2014,
                termDegree: "Y",
                primaryIndicator: "Y",
                sourceAndBackgroundInstitution: priorCollege.sourceAndBackgroundInstitution,
                degree: Degree.findByCode("PHD"),
                college: College.findByCode("AH"),
                institutionalHonor: null,
                educationGoal: "MA",
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
}
