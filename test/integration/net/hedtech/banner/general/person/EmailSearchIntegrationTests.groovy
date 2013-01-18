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
package net.hedtech.banner.general.person

import net.hedtech.banner.general.system.EmailType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import net.hedtech.banner.general.person.view.PersonEmailView

class EmailSearchIntegrationTests extends BaseIntegrationTestCase {


    protected void setUp() {
        formContext = ['SOAIDEN'] // Since we are not testing a controller, we need to explicitly set this (removing GEAPART because of GUOBOBS_UI_VERSION = B)
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    /**
     * Tests the list of emails by email filter search and pagination.
     */
    def testEmailSearchByFilterAndPagination() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def filter = "einstein"
        def results = PersonEmailView.fetchPersonEmailAddress(filter, pagingAndSortParams)

        assertNotNull results.list

        def res = results?.list[0]

        assertNotNull res.bannerId
        assertNotNull res.lastName
        assertNotNull res.firstName
        assertNotNull res.birthDate
    }

    /**
     * Tests the list of emails by email filter search.
     */
    def testEmailSearchByFilter() {

        def filter = "einstein"
        def results = PersonEmailView.fetchByEmailAddress(filter)

        assertNotNull results

        def res = results[0]

        assertNotNull res.bannerId
        assertNotNull res.lastName
        assertNotNull res.firstName
        assertNotNull res.birthDate

    }

    /**
     * Tests the list of emails for inquire page.
     */
    def testDynamicFinder() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."emailAddress" = "%einstein%"

        filterData.params = param

        def m = [:]
        m."key" = "emailAddress"
        m."binding" = "emailAddress"
        m."operator" = "contains"

        def x = []
        x.add(m)
        filterData.criteria = x

        def result = PersonEmailView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result
    }

    /**
     * Tests the list of emails for the pidm list
     */
    def testEmailSearchByFilterAndPidm() {
        def pidmList = []
        pidmList.add(new Integer("33775"))

        def filter = "einstein"
        def results = PersonEmailView.fetchByEmailAddressAndPidm(pidmList, filter)

        assertNotNull results
        assertEquals 1, results.size()
    }
}
