/*********************************************************************************
  Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import groovy.sql.Sql
import static groovy.test.GroovyAssert.*
import net.hedtech.banner.general.system.HoldType
import net.hedtech.banner.general.system.Originator
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

@Integration
@Rollback
class PersonRelatedHoldIntegrationTests extends BaseIntegrationTestCase {

    //def personRelatedHoldService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this  (removing SOAHOLD because of GUOBOBS_UI_VERSION = B)
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCreatePersonRelatedHold() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold
        //Test if the generated entity now has an id assigned
        assertNotNull personRelatedHold.id
    }


    @Test
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
        assertTrue new BigDecimal(1) == personRelatedHold.amountOwed
        assertEquals "grails_user", personRelatedHold.lastModifiedBy.toLowerCase()

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
        personRelatedHold.createdBy = "test"
        personRelatedHold.dataOrigin = "Banner"
        save personRelatedHold

        personRelatedHold = PersonRelatedHold.get(personRelatedHold.id)
        assertEquals 1L, personRelatedHold?.version
        assertEquals PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("HOS00001").pidm, personRelatedHold.pidm
        assertEquals testDate, personRelatedHold.fromDate
        assertEquals testDate, personRelatedHold.toDate
        assertEquals false, personRelatedHold.releaseIndicator
        assertEquals "UUUUU", personRelatedHold.reason
        assertTrue new BigDecimal(0) == personRelatedHold.amountOwed
        //Make sure that last modified by user is not being updated
        assertEquals "grails_user", personRelatedHold.lastModifiedBy.toLowerCase()

    }


    @Test
    void testOptimisticLock() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SV_SPRHOLD set SPRHOLD_VERSION = 999 where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            //TODO grails3   sql?.close() // note that the test will close the connection, since it's our current session's connection
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
        personRelatedHold.createdBy = "test"
        personRelatedHold.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            personRelatedHold.save(flush: true)
        }
    }


    @Test
    void testDeletePersonRelatedHold() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold
        def id = personRelatedHold.id
        assertNotNull id
        personRelatedHold.delete()
        assertNull PersonRelatedHold.get(id)
    }


    @Test
    void testValidation() {
        def personRelatedHold = newPersonRelatedHold()
        assertTrue "PersonRelatedHold could not be validated as expected due to ${personRelatedHold.errors}", personRelatedHold.validate()
    }


    @Test
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


    @Test
    void testMaxSizeValidationFailures() {
        def personRelatedHold = new PersonRelatedHold(
                reason: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "PersonRelatedHold should have failed validation", personRelatedHold.validate()
        assertErrorsFor personRelatedHold, 'maxSize', ['reason']
    }


    @Ignore //validation error test fails due to presence of scale constraint in the domain
    @Test
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


    @Test
    void testFetchByPidm() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold
        def holdPidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("HOS00001").pidm

        def holdList = PersonRelatedHold.fetchByPidm(holdPidm)
        assertNotNull(holdList)
    }


    @Test
    void testFetchByPidmAndDateBetween() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def holdDate = df1.parse("2010-12-01")
        def holdPidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("HOS00001").pidm

        def holdList = PersonRelatedHold.fetchByPidmAndDateBetween(holdPidm, holdDate)
        assertNotNull(holdList)
    }


    @Test
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



    @Test
    void testFetchByPidmAndDateCompare() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def holdDate = df1.parse("2010-12-01")
        def holdPidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("HOS00001").pidm

        def holdList = PersonRelatedHold.fetchByPidmAndDateCompare(holdPidm, holdDate)
        assertNotNull(holdList)
    }

    @Test
    void testFetchByPidmListAndDateCompare() {
        def holdList = [newPersonRelatedHold()]
        def personRelatedHold = newPersonRelatedHold()
        personRelatedHold.pidm  = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("HOS00002").pidm
        holdList.add(personRelatedHold)
        holdList.each{it.save(failOnError: true, flush: true)}

        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def holdDate = df1.parse("2010-08-01")

        def result = PersonRelatedHold.fetchByPidmListAndDateCompare(holdList.pidm, holdDate)
        assertNotNull(result)
        assertTrue result.size() > 1
        assertTrue result.pidm.size() > 1
        assertTrue result[0] instanceof PersonRelatedHold
    }


    @Test
    void testFetchRegistrationHoldsExist() {
        def personRelatedHold = newPersonRelatedHold()
        save personRelatedHold
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def holdDate = df1.parse("2010-08-01")
        def holdPidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("HOS00001").pidm

        def registrationHoldsExist = PersonRelatedHold.registrationHoldsExist(holdPidm, holdDate)
        assertTrue(registrationHoldsExist)
    }


    @Test
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
                createdBy: "test",
                dataOrigin: "Banner"
        )
        return personRelatedHold
    }



    /**
     * Test to validate the count
     * */
    @Test
    void testCount() {
        def count = PersonRelatedHold.countRecord(new Date())
        assertNotNull count
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def result = sql.firstRow("select count(*) as cnt from SPRHOLD where SPRHOLD_TO_DATE > TO_DATE(SYSDATE, 'DD-MM-YY') ")
        Long expectCount = result.cnt
        assertNotNull expectCount
        assertEquals expectCount,count
    }



    /**
     * <p> Test to get the count from AcademicHonorView and cumulative count of DepartmentalHonor and InstitutionalHonor</p>
     * */
    @Test
    public void testCountWithFilter() {
        def params = personHoldFilterMap()
        def count = PersonRelatedHold.countRecordWithFilter(params, new Date())
        assertNotNull count
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def result = sql.firstRow("select  count(*) as cnt from SPRHOLD,STVHLDD,GORGUID g1,GORGUID g2,GORGUID g3 where SPRHOLD_TO_DATE > TO_DATE(SYSDATE, 'DD-MM-YY') and STVHLDD_CODE = SPRHOLD_HLDD_CODE and g1.GORGUID_LDM_NAME = 'person-holds' AND g1.GORGUID_DOMAIN_SURROGATE_ID = SPRHOLD.SPRHOLD_SURROGATE_ID and g2.GORGUID_LDM_NAME = 'persons' AND g2.GORGUID_DOMAIN_KEY= SPRHOLD_PIDM AND g2.GORGUID_GUID = '"+params.person+"' and g3.GORGUID_LDM_NAME = 'restriction-types' AND g3.GORGUID_DOMAIN_KEY= SPRHOLD_HLDD_CODE")
        Long expectCount = result.cnt
        assertNotNull expectCount
        assertEquals expectCount, count

    }

    /*
      * Tests validate for list
      * */
    @Test
    void testFetchLists() {
        def params = [max: '10', offset: '0']
        def personHoldsList = PersonRelatedHold.fetchAll(params,new Date())
        assertNotNull personHoldsList
    }

    /*
   * Tests validate for filter
   **/
    @Test
    void fetchByPersonGuid() {
        def params = personHoldFilterMap()
        def personHoldsList = PersonRelatedHold.fetchByPersonsGuid(params,new Date())
        assertNotNull personHoldsList
    }


    private Map personHoldFilterMap() {
        def params = [max: '10', offset: '0']
        def personHoldsList =  PersonRelatedHold.fetchAll(params,new Date())
        def personHoldsGuid = personHoldsList.get(0).getAt(2).guid
        params.person = personHoldsGuid
        return params
    }



    /** Tests validate for filter*/
    @Test
    void fetchPersonHoldByGuid() {
        def params = [max: '10', offset: '0']
        def personHoldsList = PersonRelatedHold.fetchAll(params,new Date())
        def personHoldGuid = personHoldsList.get(0).getAt(1).guid
        def personHold = PersonRelatedHold.fetchByPersonHoldGuid(personHoldGuid,new Date())
        assertNotNull personHold
    }







}

