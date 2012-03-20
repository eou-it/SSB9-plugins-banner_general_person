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
/**
 Banner Automator Version: 1.24
 Generated: Thu Aug 04 14:06:16 EDT 2011 
 */
package com.sungardhe.banner.general.person

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.general.person.view.PersonAdvancedSearchView

class PersonAdvancedSearchIntegrationTests extends BaseIntegrationTestCase {

    def  personSearchService

    protected void setUp() {
        formContext = ['GEAPART'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }


    /**
     * Tests the list of additional ids for inquire page.
     */
    def testDynamicFinder() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."city" = "%seattle%"

        def pidmList = []
        pidmList.add(new Integer("2415"))
        pidmList.add(new Integer("2416"))

        param."pidms" = pidmList

        filterData.params = param

        def m1 = [:]
        m1."key" = "city"
        m1."binding" = "city"
        m1."operator" = "contains"

        def x = []
        x.add(m1)
        filterData.criteria = x

        def result = PersonAdvancedSearchView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertTrue result.size() == 4

    }


     /**
     * Tests the list of additional ids for inquire page.
     */
    def testAdvancedSearch() {
        // Define text search based on LastName, FirstName, and Id
        def persons = personSearchService.fetchTextSearch("33|STUDENT|104|S104")
        assertNotNull persons

        // Step 1: returns a list of pidms
        assertNotNull (persons.collect { it.pidm } as HashSet)

        def pidmSet = (persons.collect { new Integer (it.pidm.toString()) } as HashSet)

        List<Integer> pidmList = new ArrayList<Integer>(pidmSet);

        // Step 2: returns a list of persons based on list of pidms
        // search criteria: City, Zip, Birth Date
        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."city" = "%seattle%"
        param."zip" = "98199"
        param."birthDate" = Date.parse("yyyy-MM-dd", "1970-12-31")

        param."pidms" = pidmList

        filterData.params = param

        def m1 = [:]
        m1."key" = "city"
        m1."binding" = "city"
        m1."operator" = "contains"

        def m2 = [:]
        m2."key" = "birthDate"
        m2."binding" = "birthDate"
        m2."operator" = "lessthan"

        def m3 = [:]
        m3."key" = "zip"
        m3."binding" = "zip"
        m3."operator" = "equals"

        def x = []
        x.add(m1)
        x.add(m2)
        x.add(m3)
        filterData.criteria = x

        def result = PersonAdvancedSearchView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertTrue result.size() == 1
        assertEquals "M",result[0].sex
    }
}
