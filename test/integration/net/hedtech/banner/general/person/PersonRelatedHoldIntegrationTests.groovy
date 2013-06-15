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

package net.hedtech.banner.general.person

import net.hedtech.banner.general.system.HoldType
import net.hedtech.banner.general.system.Originator
import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import java.text.SimpleDateFormat
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import org.junit.Ignore

class PersonRelatedHoldIntegrationTests extends BaseIntegrationTestCase {

    //def personRelatedHoldService

    protected void setUp() {
        formContext = ['SOAIDEN'] // Since we are not testing a controller, we need to explicitly set this  (removing SOAHOLD because of GUOBOBS_UI_VERSION = B)
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreatePersonRelatedHold() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold
        //Test if the generated entity now has an id assigned
        assertNotNull personRelatedHold.id
    }


    void testUpdatePersonRelatedHold() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold

        assertNotNull personRelatedHold.id
        assertEquals 0L, personRelatedHold.version
        assertEquals PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("HOS00001").pidm, personRelatedHold.pidm
        assertNotNull personRelatedHold.fromDate
        assertNotNull personRelatedHold.toDate
        assertEquals false, personRelatedHold.releaseIndicator
        assertEquals "TTTTT", personRelatedHold.reason
        assertEquals 1, personRelatedHold.amountOwed
        assertEquals "grails_user", personRelatedHold.lastModifiedBy

        //Update the entity
        def testDate = new Date()
        personRelatedHold.pidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("HOS00001").pidm
        personRelatedHold.fromDate = testDate
        personRelatedHold.toDate = testDate
        personRelatedHold.releaseIndicator = false
        personRelatedHold.reason = "UUUUU"
        personRelatedHold.amountOwed = 0
        personRelatedHold.lastModified = testDate
        personRelatedHold.lastModifiedBy = "test"
        personRelatedHold.dataOrigin = "Banner"
        save personRelatedHold

        personRelatedHold = PersonRelatedHold.get(personRelatedHold.id)
        assertEquals 1L, personRelatedHold?.version
        assertEquals PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("HOS00001").pidm, personRelatedHold.pidm
        assertEquals testDate, personRelatedHold.fromDate
        assertEquals testDate, personRelatedHold.toDate
        assertEquals false, personRelatedHold.releaseIndicator
        assertEquals "UUUUU", personRelatedHold.reason
        assertEquals 0, personRelatedHold.amountOwed
        //Make sure that last modified by user is not being updated
        assertEquals "grails_user", personRelatedHold.lastModifiedBy

    }


    void testOptimisticLock() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SV_SPRHOLD set SPRHOLD_VERSION = 999 where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        personRelatedHold.pidm = 1
        personRelatedHold.fromDate = new Date()
        personRelatedHold.toDate = new Date()
        personRelatedHold.releaseIndicator = false
        personRelatedHold.reason = "UUUUU"
        personRelatedHold.amountOwed = 0
        personRelatedHold.lastModified = new Date()
        personRelatedHold.lastModifiedBy = "test"
        personRelatedHold.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            personRelatedHold.save(flush: true)
        }
    }


    void testDeletePersonRelatedHold() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold
        def id = personRelatedHold.id
        assertNotNull id
        personRelatedHold.delete()
        assertNull PersonRelatedHold.get(id)
    }


    void testValidation() {
        def personRelatedHold = newPersonRelatedHold()
        assertTrue "PersonRelatedHold could not be validated as expected due to ${personRelatedHold.errors}", personRelatedHold.validate()
    }


    void testNullValidationFailure() {
        def personRelatedHold = new PersonRelatedHold()
        assertFalse "PersonRelatedHold should have failed validation", personRelatedHold.validate()
        assertErrorsFor personRelatedHold, 'nullable',
                [
                        'pidm',
                        'fromDate',
                        'toDate',
                        'holdType'
                ]
        assertNoErrorsFor personRelatedHold,
                [
                        'reason',
                        'amountOwed',
                        'originator',
                        'releaseIndicator'
                ]
    }


    void testMaxSizeValidationFailures() {
        def personRelatedHold = new PersonRelatedHold(
                reason: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "PersonRelatedHold should have failed validation", personRelatedHold.validate()
        assertErrorsFor personRelatedHold, 'maxSize', ['reason']
    }


    @Ignore //validation error test fails due to presence of scale constraint in the domain
    void testDates() {
        String fromDate = "2010-10-01"
        String toDate = "2010-09-01"
        def personRelatedHold = newPersonRelatedHold()

        def df1 = new SimpleDateFormat("yyyy-MM-dd")

        if (fromDate) personRelatedHold.fromDate = df1.parse(fromDate)
        if (toDate) personRelatedHold.toDate = df1.parse(toDate)

        assertFalse "Should fail validation", personRelatedHold.validate()
        assertErrorsFor personRelatedHold, 'validator', ['fromDate']
    }


    void testFetchByPidm() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold
        def holdPidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("HOS00001").pidm

        def holdList = PersonRelatedHold.fetchByPidm(holdPidm)
        assertNotNull(holdList)
    }


    void testFetchByPidmAndDateBetween() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def holdDate = df1.parse("2010-12-01")
        def holdPidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("HOS00001").pidm

        def holdList = PersonRelatedHold.fetchByPidmAndDateBetween(holdPidm, holdDate)
        assertNotNull(holdList)
    }


    void testFetchByPidmDateAndHoldType() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def holdDate = df1.parse("2010-12-01")
        def holdPidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("HOS00001").pidm
        def holdType = HoldType.findByCode("AS")

        def holdList = PersonRelatedHold.fetchByPidmDateAndHoldType(holdPidm, holdDate, holdType.code)
        assertNotNull(holdList)
    }



    void testFetchByPidmAndDateCompare() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def holdDate = df1.parse("2010-12-01")
        def holdPidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("HOS00001").pidm

        def holdList = PersonRelatedHold.fetchByPidmAndDateCompare(holdPidm, holdDate)
        assertNotNull(holdList)
    }


    void testFetchRegistrationHoldsExist() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def holdDate = df1.parse("2010-08-01")
        def holdPidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("HOS00001").pidm

        def registrationHoldsExist = PersonRelatedHold.registrationHoldsExist(holdPidm, holdDate)
        assertTrue(registrationHoldsExist)
    }


    void testAdvanceFilter() {
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def personRelatedHold1 = newPersonRelatedHold()
        save personRelatedHold1

        def personRelatedHold2 = newPersonRelatedHold()
        personRelatedHold2.holdType = HoldType.findByCode("AD")
        save personRelatedHold2

        def personRelatedHold3 = newPersonRelatedHold()
        personRelatedHold3.holdType = HoldType.findByCode("PF")
        save personRelatedHold3

        def personRelatedHold4 = newPersonRelatedHold()
        personRelatedHold4.holdType = HoldType.findByCode("FH")
        save personRelatedHold4

        def personRelatedHold5 = newPersonRelatedHold()
        personRelatedHold5.holdType = HoldType.findByCode("WC")
        save personRelatedHold5

        Map pagingAndSortParams = [:]
        Map filterData = [params: [pidm: pidm]]

        pagingAndSortParams = [sortColumn: "holdType.code", sortDirection: "desc"]
        def personRelatedHolds = PersonRelatedHold.fetchSearch(filterData, pagingAndSortParams)
        assertNotNull personRelatedHolds
        assertEquals personRelatedHolds[0].holdType.code, "WC"

        Map paramsMap = [originator: "ACCT", pidm: pidm]
        def criteriaMap = [[key: "originator", binding: "originator.code", operator: "equals"]]
        filterData = [params: paramsMap, criteria: criteriaMap]
        personRelatedHolds = PersonRelatedHold.fetchSearch(filterData, pagingAndSortParams)
        personRelatedHolds.each { personRelatedHold ->
            assertTrue personRelatedHold.originator.code == "ACCT"
            assertTrue personRelatedHold.pidm == pidm
        }

        def count = PersonRelatedHold.countAll(filterData)
        assertEquals count, personRelatedHolds.size()

    }


    private def newPersonRelatedHold() {

        def iholdType = HoldType.findWhere(code: "RG")
        def ioriginator = Originator.findWhere(code: "ACCT")
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def fromDate = df1.parse("2010-07-01")
        def toDate = df1.parse("2010-09-01")
        def pidm = PersonUtility.getPerson("HOS00001").pidm


        def personRelatedHold = new PersonRelatedHold(
                pidm: pidm,
                fromDate: fromDate,
                toDate: toDate,
                releaseIndicator: false,
                reason: "TTTTT",
                amountOwed: 1,
                holdType: iholdType,
                originator: ioriginator,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return personRelatedHold
    }
}
