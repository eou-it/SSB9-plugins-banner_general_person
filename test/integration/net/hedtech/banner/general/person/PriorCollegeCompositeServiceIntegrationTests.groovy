/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.general.system.*
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PriorCollegeCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def priorCollegeCompositeService

    // parent records
    def priorCollege
    def priorCollegeDegree

    def concentrationMajorMinor

    // stored mapping of the primary keys for the child records
    def mapPK


    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    def testPriorCollegeCompositeServiceCreate() {
        // create and test new records
        def map = createAll()
    }


    def testPriorCollegeCompositeServiceDeleteAndCreate() {
        // create/delete and test ONLY new child records; these are the non-updateable domains
        def deleteMap = createAll()      // create the initial records for the deletion tests
        def createMap = createAll("LAW") // create similiar records with a different concentration/major/minor for the creation test

        // delete them
        priorCollegeCompositeService.createOrUpdate(
                [
                        deletePriorCollegeConcentrationAreas: [deleteMap.priorCollegeConcentrationArea],
                        deletePriorCollegeMajors: [deleteMap.priorCollegeMajor],
                        deletePriorCollegeMinors: [deleteMap.priorCollegeMinor],

                        priorCollegeConcentrationAreas: [createMap.priorCollegeConcentrationArea],
                        priorCollegeMajors: [createMap.priorCollegeMajor],
                        priorCollegeMinors: [createMap.priorCollegeMinor],
                ])

        // test the deleted records does not exists
        def recMap = fetchAll()
        assertNull recMap.priorCollegeConcentrationArea
        assertNull recMap.priorCollegeMajor
        assertNull recMap.priorCollegeMinor

        // test the created record exists
        recMap = fetchAll("LAW")
        assertNotNull recMap.priorCollegeConcentrationArea.version
        assertNotNull recMap.priorCollegeMajor.version
        assertNotNull recMap.priorCollegeMinor.version
    }


    def testPriorCollegeCompositeServiceDelete() {
        // create and test new records
        def deleteMap = createAll()

        // delete them
        priorCollegeCompositeService.createOrUpdate(
                [
                        deletePriorColleges: [deleteMap.priorCollege],
                        deletePriorCollegeDegrees: [deleteMap.priorCollegeDegree],
                        deletePriorCollegeConcentrationAreas: [deleteMap.priorCollegeConcentrationArea],
                        deletePriorCollegeMajors: [deleteMap.priorCollegeMajor],
                        deletePriorCollegeMinors: [deleteMap.priorCollegeMinor],
                ])

        // test the deleted records does not exists
        def recMap = fetchAll()
        assertNull recMap.priorCollege
        assertNull recMap.priorCollegeDegree
        assertNull recMap.priorCollegeConcentrationArea
        assertNull recMap.priorCollegeMajor
        assertNull recMap.priorCollegeMinor
    }


    def testPriorCollegeCompositeServiceUpdate() {
        // create and test new records
        def map = createAll()

        // update the records
        map.priorCollege.admissionRequest = AdmissionRequest.findByCode("TUTD")
        map.priorCollegeDegree.hoursTransferred = 201.00
        priorCollegeCompositeService.createOrUpdate(
                [
                        priorColleges: [map.priorCollege],
                        priorCollegeDegrees: [map.priorCollegeDegree],
                ])

        concentrationMajorMinor = MajorMinorConcentration.findByCode("LAW")

        map.priorCollegeConcentrationArea.concentration = concentrationMajorMinor
        priorCollegeCompositeService.createOrUpdate([priorCollegeConcentrationAreas: [map.priorCollegeConcentrationArea]])

        map.priorCollegeMajor.majorMinorConcentrationMajor = concentrationMajorMinor
        priorCollegeCompositeService.createOrUpdate([priorCollegeMajors: [map.priorCollegeMajor]])

        map.priorCollegeMinor.majorMinorConcentrationMinor = concentrationMajorMinor
        priorCollegeCompositeService.createOrUpdate([priorCollegeMinors: [map.priorCollegeMinor]])

        // test the updates worked
        def recMap = fetchAll(concentrationMajorMinor.code)

        assertEquals 1L, recMap.priorCollege.version
        assertEquals "TUTD", recMap.priorCollege.admissionRequest.code

        assertEquals 1L, recMap.priorCollegeDegree.version
        assertEquals 201.00, recMap.priorCollegeDegree.hoursTransferred

        assertEquals 0L, recMap.priorCollegeConcentrationArea.version
        assertEquals concentrationMajorMinor.code, recMap.priorCollegeConcentrationArea.concentration.code

        assertEquals 0L, recMap.priorCollegeMajor.version
        assertEquals concentrationMajorMinor.code, recMap.priorCollegeMajor.majorMinorConcentrationMajor.code

        assertEquals 0L, recMap.priorCollegeMinor.version
        assertEquals concentrationMajorMinor.code, recMap.priorCollegeMinor.majorMinorConcentrationMinor.code
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setup
    // -----------------------------------------------------------------------------------------------------------------

    // create test records with concentration/major/minor value
    private def createAll(major) {
        // create parent domains; these have to be done individually before creations of any of the child domains
        if (!priorCollege) {
            priorCollege = newValidForCreatePriorCollege()
            priorCollegeCompositeService.createOrUpdate([priorColleges: [priorCollege]])
        }

        if (!priorCollegeDegree) {
            priorCollegeDegree = newValidForCreatePriorCollegeDegree()
            priorCollegeCompositeService.createOrUpdate([priorCollegeDegrees: [priorCollegeDegree]])
        }

        // create child domains
        def priorCollegeConcentrationArea = newValidForCreatePriorCollegeConcentrationArea(major)
        def priorCollegeMajor = newValidForCreatePriorCollegeMajor()
        def priorCollegeMinor = newValidForCreatePriorCollegeMinor()

        priorCollegeCompositeService.createOrUpdate(
                [
                        priorCollegeConcentrationAreas: [priorCollegeConcentrationArea],
                        priorCollegeMajors: [priorCollegeMajor],
                        priorCollegeMinors: [priorCollegeMinor],
                ])

        // confirm these records exists
        def map = fetchAll(major)
        assertNotNull map.priorCollege.version
        assertNotNull map.priorCollegeDegree.version
        assertNotNull map.priorCollegeConcentrationArea.version
        assertNotNull map.priorCollegeMajor.version
        assertNotNull map.priorCollegeMinor.version

        return map
    }


    private def createAll() {
        return createAll("EDUC")
    }

    // fetch test records with concentration/major/minor value
    private def fetchAll(major) {
        def priorCollege = PriorCollege.findWhere(
                pidm: mapPK.pidm,
                sourceAndBackgroundInstitution: mapPK.sourceAndBackgroundInstitution,
        )

        def priorCollegeDegree = PriorCollegeDegree.findWhere(
                pidm: mapPK.pidm,
                sourceAndBackgroundInstitution: mapPK.sourceAndBackgroundInstitution,
                degreeSequenceNumber: mapPK.degreeSequenceNumber,
        )

        concentrationMajorMinor = MajorMinorConcentration.findByCode(major)

        def priorCollegeConcentrationArea = PriorCollegeConcentrationArea.findWhere(
                pidm: mapPK.pidm,
                sourceAndBackgroundInstitution: mapPK.sourceAndBackgroundInstitution,
                degree: mapPK.degree,
                degreeSequenceNumber: mapPK.degreeSequenceNumber,
                concentration: concentrationMajorMinor,
        )

        def priorCollegeMajor = PriorCollegeMajor.findWhere(
                pidm: mapPK.pidm,
                sourceAndBackgroundInstitution: mapPK.sourceAndBackgroundInstitution,
                degree: mapPK.degree,
                degreeSequenceNumber: mapPK.degreeSequenceNumber,
                majorMinorConcentrationMajor: concentrationMajorMinor,
        )

        def priorCollegeMinor = PriorCollegeMinor.findWhere(
                pidm: mapPK.pidm,
                sourceAndBackgroundInstitution: mapPK.sourceAndBackgroundInstitution,
                degree: mapPK.degree,
                degreeSequenceNumber: mapPK.degreeSequenceNumber,
                majorMinorConcentrationMinor: concentrationMajorMinor,
        )

        def map =
            [
                    priorCollege: priorCollege,
                    priorCollegeDegree: priorCollegeDegree,
                    priorCollegeConcentrationArea: priorCollegeConcentrationArea,
                    priorCollegeMajor: priorCollegeMajor,
                    priorCollegeMinor: priorCollegeMinor,
            ]

        return map
    }


    private def fetchAll() {
        return fetchAll("EDUC")
    }


    private def newValidForCreatePriorCollegeConcentrationArea(major) {
        concentrationMajorMinor = MajorMinorConcentration.findByCode(major)

        def priorCollegeConcentrationArea = new PriorCollegeConcentrationArea(
                pidm: mapPK.pidm,
                degreeSequenceNumber: mapPK.degreeSequenceNumber,
                sourceAndBackgroundInstitution: mapPK.sourceAndBackgroundInstitution,
                degree: mapPK.degree,
                concentration: concentrationMajorMinor,
        )
        return priorCollegeConcentrationArea
    }


    private def newValidForCreatePriorCollegeConcentrationArea() {
        return newValidForCreatePriorCollegeConcentrationArea("EDUC")
    }


    private def newValidForCreatePriorCollegeMajor() {
        def priorCollegeMajor = new PriorCollegeMajor(
                pidm: mapPK.pidm,
                degreeSequenceNumber: mapPK.degreeSequenceNumber,
                sourceAndBackgroundInstitution: mapPK.sourceAndBackgroundInstitution,
                degree: mapPK.degree,
                majorMinorConcentrationMajor: concentrationMajorMinor,
        )
        return priorCollegeMajor
    }


    private def newValidForCreatePriorCollegeMinor() {
        def priorCollegeMinor = new PriorCollegeMinor(
                pidm: mapPK.pidm,
                degreeSequenceNumber: mapPK.degreeSequenceNumber,
                sourceAndBackgroundInstitution: mapPK.sourceAndBackgroundInstitution,
                degree: mapPK.degree,
                majorMinorConcentrationMinor: concentrationMajorMinor,
        )
        return priorCollegeMinor
    }


    private def newValidForCreatePriorCollegeDegree() {
        mapPK =
            [
                    pidm: priorCollege.pidm,
                    degreeSequenceNumber: 1,
                    sourceAndBackgroundInstitution: priorCollege.sourceAndBackgroundInstitution,
                    degree: Degree.findByCode("PHD"),
            ]

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
