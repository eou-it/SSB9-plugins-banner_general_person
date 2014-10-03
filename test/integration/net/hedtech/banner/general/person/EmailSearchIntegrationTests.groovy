/*********************************************************************************
  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.general.person.view.PersonEmailView
import net.hedtech.banner.testing.BaseIntegrationTestCase

class EmailSearchIntegrationTests extends BaseIntegrationTestCase {


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
     * Tests the list of emails by email filter search and pagination.
     */
	@Test
    void testEmailSearchByFilterAndPagination() {

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
	@Test
    void testEmailSearchByFilter() {

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
	@Test
    void testDynamicFinder() {

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
	@Test
    void testEmailSearchByFilterAndPidm() {
        def pidmList = []
        pidmList.add(new Integer("33775"))

        def filter = "einstein"
        def results = PersonEmailView.fetchByEmailAddressAndPidm(pidmList, filter)

        assertNotNull results
        assertEquals 1, results.size()
    }
}
