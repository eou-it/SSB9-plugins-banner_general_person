/*********************************************************************************
  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.general.person.view.PersonAdditionalIdView
import net.hedtech.banner.testing.BaseIntegrationTestCase

@Integration
@Rollback
class AdditionalIdSearchIntegrationTests extends BaseIntegrationTestCase {


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
     * Tests the list of additional ids by filter and pagination.
     */
    @Test
    void testAdditionalIdSearchByFilterAndPagination() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def filter = "hor"   //DEANNE1
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
    @Test
    void testAdditionalIdSearchByFilter() {

        def filter = "hor"  //DEANNE1
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
    @Test
    void testAdditionalIdSearchByFilterBirthday() {

        def filter = "hor"  //Wears glasses
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
    @Test
    void testDynamicFinder() {

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
    @Test
    void testAdditionalIdSearchByFilterAndPidm() {
        def pidmList = []
        pidmList.add(new Integer("49435"))

        def filter = "hor"
        def resultsMany = PersonAdditionalIdView.fetchByAdditionalId(filter)

        assertTrue resultsMany.size() > 1

        def results = PersonAdditionalIdView.fetchByAdditionalIdAndPidm(pidmList, filter)

        assertNotNull results
        assertEquals 1, results.size()
    }
}
