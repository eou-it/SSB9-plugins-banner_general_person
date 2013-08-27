/*********************************************************************************
  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import groovy.sql.Sql
import net.hedtech.banner.general.person.view.PersonAdvancedFilterView
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonAdvancedFilterIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this (removing GEAPART because of GUOBOBS_UI_VERSION = B)
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    /**
     * Tests the list of additional ids for inquire page.
     */
    def testDynamicFinder() {
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
