/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import net.hedtech.banner.general.person.view.PersonAdditionalIdView
import net.hedtech.banner.testing.BaseIntegrationTestCase

class AdditionalIdSearchIntegrationTests extends BaseIntegrationTestCase {


    protected void setUp() {
        formContext = ['SOAIDEN'] // Since we are not testing a controller, we need to explicitly set this (removing GEAPART because of GUOBOBS_UI_VERSION = B)
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    /**
     * Tests the list of additional ids by filter and pagination.
     */
    def testAdditionalIdSearchByFilterAndPagination() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def filter = "nne1"   //DEANNE1
        def results = PersonAdditionalIdView.fetchPersonAdditionalId(filter, pagingAndSortParams)

        assertNotNull results?.list

        def res = results?.list[0]

        assertNotNull res?.bannerId
        assertNotNull res?.lastName
        assertNotNull res?.firstName
    }

    /**
     * Tests the list of additional ids by filter.
     */
    def testAdditionalIdSearchByFilter() {

        def filter = "nne1"  //DEANNE1
        def results = PersonAdditionalIdView.fetchByAdditionalId(filter)

        assertNotNull results

        def res = results[0]

        assertNotNull res.bannerId
        assertNotNull res.lastName
        assertNotNull res.firstName
    }

    /**
     * Tests the list of additional ids by filter with birthday
     */
    def testAdditionalIdSearchByFilterBirthday() {

        def filter = "glasses"  //Wears glasses
        def results = PersonAdditionalIdView.fetchByAdditionalId(filter)

        assertNotNull results

        def res = results[0]

        assertNotNull res.bannerId
        assertNotNull res.lastName
        assertNotNull res.firstName
        assertNotNull res.birthDate
    }

    /**
     * Tests the list of additional ids for inquire page.
     */
    def testDynamicFinder() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."additionalId" = "%nne1%"

        filterData.params = param

        def m = [:]
        m."key" = "additionalId"
        m."binding" = "additionalId"
        m."operator" = "contains"

        def x = []
        x.add(m)
        filterData.criteria = x

        def result = PersonAdditionalIdView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result
    }

    /**
     * Tests the list of additional ids for the pidm list
     */
    def testAdditionalIdSearchByFilterAndPidm() {
        def pidmList = []
        pidmList.add(new Integer("36336"))

        def filter = "nne1"
        def resultsMany = PersonAdditionalIdView.fetchByAdditionalId(filter)

        assertTrue resultsMany.size() > 1

        def results = PersonAdditionalIdView.fetchByAdditionalIdAndPidm(pidmList, filter)

        assertNotNull results
        assertEquals 5, results.size()
    }
}
