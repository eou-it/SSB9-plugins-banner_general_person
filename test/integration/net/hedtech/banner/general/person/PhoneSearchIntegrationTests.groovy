/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import net.hedtech.banner.general.person.view.PersonPhoneView
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PhoneSearchIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['SOAIDEN'] // Since we are not testing a controller, we need to explicitly set this (removing GEAPART because of GUOBOBS_UI_VERSION = B)
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    /**
     * Tests the list of phones by filter and pagination with.
     */
    def testPhoneSearchByFilterAndPagination() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def filter = "1235000"
        def results = PersonPhoneView.fetchPersonPhone(filter, pagingAndSortParams)

        assertNotNull results?.list

        def res = results?.list[0]

        assertNotNull res?.bannerId
        assertNotNull res?.lastName
        assertNotNull res?.firstName
        assertNotNull res?.birthDate
    }

    /**
     * Tests the list of phones by filter and pagination with the phone format.
     */
    def testPhoneSearchWithFormatByFilterAndPagination() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def filter = "(610) 111 -"   //610) 111 -
        def results = PersonPhoneView.fetchPersonPhone(filter, pagingAndSortParams)

        assertNotNull results?.list

        def res = results?.list[0]

        assertNotNull res?.bannerId
        assertNotNull res?.lastName
    }

    /**
     * Tests the list of phones by filter.
     */
    def testPhoneSearchByFilter() {

        def filter = "1235000"
        def results = PersonPhoneView.fetchByPhone(filter)

        assertNotNull results

        def res = results[0]

        assertNotNull res.bannerId
        assertNotNull res.lastName
        assertNotNull res.firstName
        assertNotNull res.birthDate

    }

    /**
     * Tests the list of phones for inquire page.
     */
    def testDynamicFinder() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."phoneSearch" = "%1235000%"

        filterData.params = param

        def m = [:]
        m."key" = "phoneSearch"
        m."binding" = "phoneSearch"
        m."operator" = "contains"

        def x = []
        x.add(m)
        filterData.criteria = x

        def result = PersonPhoneView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result
    }

    /**
     * Tests the list of phones for the pidm list
     */
    def testPhoneSearchByFilterAndPidm() {
        def pidmList = []
        pidmList.add(new Integer("35540"))

        def filter = "1234567"

        def resultsMany = PersonPhoneView.fetchByPhone(filter)

        assertTrue resultsMany.size() > 0


        def results = PersonPhoneView.fetchByPhoneAndPidm(pidmList, filter)

        assertEquals 1, results.size()
    }


    /**
     * Tests the list of phones for inquire page.
     */
    def testDynamicFinder1() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."phoneSearch" = "%6102332323%"

        filterData.params = param

        def m = [:]
        m."key" = "phoneSearch"
        m."binding" = "phoneSearch"
        m."operator" = "contains"

        def x = []
        x.add(m)
        filterData.criteria = x

        def result = PersonPhoneView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result
    }
}
