/*********************************************************************************
  Copyright 2009-2018 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
 package net.hedtech.banner.general.person

import net.hedtech.banner.general.person.view.PersonPhoneView
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test
import org.junit.After

class PhoneSearchIntegrationTests extends BaseIntegrationTestCase {

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this (removing GEAPART because of GUOBOBS_UI_VERSION = B)
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * Tests the list of phones by filter and pagination with.
     */
    @Test
    void testPhoneSearchByFilterAndPagination() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def filter = "2083094"
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
    @Test
    void testPhoneSearchWithFormatByFilterAndPagination() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def filter = "(215) 208 - " //"(215) 372 -"   //610) 111 -
        def results = PersonPhoneView.fetchPersonPhone(filter, pagingAndSortParams)

        assertNotNull results?.list

        def res = results?.list[0]

        assertNotNull res?.bannerId
        assertNotNull res?.lastName
    }

    /**
     * Tests the list of phones by filter.
     */
    @Test
    void testPhoneSearchByFilter() {

        def filter = "2083094"
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
    @Test
    void testDynamicFinder() {

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
    @Test
    void testPhoneSearchByFilterAndPidm() {
        def pidmList = []
        pidmList.add(new Integer(PersonUtility.getPerson("GDP000005").pidm))

        def filter = "2083094"

        def resultsMany = PersonPhoneView.fetchByPhone(filter)

        assertTrue resultsMany.size() > 0


        def results = PersonPhoneView.fetchByPhoneAndPidm(pidmList, filter)

        assertEquals 1, results.size()
    }


    /**
     * Tests the list of phones for inquire page.
     */
    @Test
    void testDynamicFinder1() {

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
