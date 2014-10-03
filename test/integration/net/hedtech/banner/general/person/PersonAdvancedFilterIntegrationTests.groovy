/*********************************************************************************
  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.general.person.view.PersonAdvancedFilterView
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonAdvancedFilterIntegrationTests extends BaseIntegrationTestCase {

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
     * Tests the list of additional ids for inquire page.
     */
	@Test
    void testDynamicFinder() {
        def searchFilter = 'A00000706'

        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())

        sql.call("{call soknsut.p_set_search_filter(${searchFilter})}")

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]

        filterData.params = param
        def result = PersonAdvancedFilterView.fetchSearchEntityList(filterData, pagingAndSortParams)
        assertNotNull result
    }
}
