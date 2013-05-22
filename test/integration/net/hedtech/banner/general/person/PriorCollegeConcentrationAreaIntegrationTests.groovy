/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.*
import net.hedtech.banner.student.system.EducationGoal
import net.hedtech.banner.testing.BaseIntegrationTestCase

import java.text.SimpleDateFormat

class PriorCollegeConcentrationAreaIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidPriorCollegeConcentrationArea() {
        def priorCollegeConcentrationArea = newValidForCreatePriorCollegeConcentrationArea()
        priorCollegeConcentrationArea.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull priorCollegeConcentrationArea.id
    }


    void testUpdateValidPriorCollegeConcentrationArea() {
        def priorCollegeConcentrationArea = newValidForCreatePriorCollegeConcentrationArea()
        priorCollegeConcentrationArea.save(failOnError: true, flush: true)
        assertNotNull priorCollegeConcentrationArea.id
        assertEquals 0L, priorCollegeConcentrationArea.version
        assertEquals PersonIdentificationName.findByBannerId("HOR000001").pidm, priorCollegeConcentrationArea.pidm
        assertEquals 1, priorCollegeConcentrationArea.degreeSequenceNumber
        assertEquals "999999", priorCollegeConcentrationArea.sourceAndBackgroundInstitution.code
        assertEquals "PHD", priorCollegeConcentrationArea.degree.code
        assertEquals "EDUC", priorCollegeConcentrationArea.concentration.code

        priorCollegeConcentrationArea.concentration = MajorMinorConcentration.findByCode("ECNO")
        try {
            priorCollegeConcentrationArea.save(flush: true, failOnError: true)
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

        def priorCollegeConcentrationArea = newValidForCreatePriorCollegeConcentrationArea()

        priorCollegeConcentrationArea.save(flush: true, failOnError: true)
        priorCollegeConcentrationArea.refresh()
        assertNotNull "PriorCollegeConcentrationArea should have been saved", priorCollegeConcentrationArea.id

        // test date values -
        assertEquals date.format(today), date.format(priorCollegeConcentrationArea.lastModified)
        assertEquals hour.format(today), hour.format(priorCollegeConcentrationArea.lastModified)
    }


    void testDeletePriorCollegeConcentrationArea() {
        def priorCollegeConcentrationArea = newValidForCreatePriorCollegeConcentrationArea()
        priorCollegeConcentrationArea.save(failOnError: true, flush: true)
        def id = priorCollegeConcentrationArea.id
        assertNotNull id
        priorCollegeConcentrationArea.delete()
        assertNull PriorCollegeConcentrationArea.get(id)
    }


    void testValidation() {
        def priorCollegeConcentrationArea = newInvalidForCreatePriorCollegeConcentrationArea()
        assertFalse "PriorCollegeConcentrationArea could not be validated as expected due to ${priorCollegeConcentrationArea.errors}", priorCollegeConcentrationArea.validate()
    }


    void testNullValidationFailure() {
        def priorCollegeConcentrationArea = new PriorCollegeConcentrationArea()
        assertFalse "PriorCollegeConcentrationArea should have failed validation", priorCollegeConcentrationArea.validate()
        assertErrorsFor priorCollegeConcentrationArea, 'nullable',
                [
                        'pidm',
                        'degreeSequenceNumber',
                        'sourceAndBackgroundInstitution',
                        'concentration'
                ]
        assertNoErrorsFor priorCollegeConcentrationArea,
                [
                        'degree'
                ]
    }


    private def newValidForCreatePriorCollegeConcentrationArea() {
        def priorCollegeDegree = newValidForCreatePriorCollegeDegree()
        priorCollegeDegree.save(failOnError: true, flush: true)

        def priorCollegeConcentrationArea = new PriorCollegeConcentrationArea(
                pidm: priorCollegeDegree.pidm,
                degreeSequenceNumber: priorCollegeDegree.degreeSequenceNumber,
                sourceAndBackgroundInstitution: priorCollegeDegree.sourceAndBackgroundInstitution,
                degree: priorCollegeDegree.degree,
                concentration: MajorMinorConcentration.findByCode("EDUC"),
        )
        return priorCollegeConcentrationArea
    }


    private def newInvalidForCreatePriorCollegeConcentrationArea() {
        def priorCollegeConcentrationArea = new PriorCollegeConcentrationArea(
                pidm: null,
                degreeSequenceNumber: 100,
                sourceAndBackgroundInstitution: null,
                degree: null,
                concentration: null,
        )
        return priorCollegeConcentrationArea
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
                educationGoal: EducationGoal.findByCode("MA"),
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
