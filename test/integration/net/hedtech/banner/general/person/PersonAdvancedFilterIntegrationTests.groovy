/*********************************************************************************
Copyright 2013 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
/**
 Banner Automator Version: 1.24
 Generated: Thu Aug 04 14:06:16 EDT 2011 
 */
package net.hedtech.banner.general.person

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

import net.hedtech.banner.general.person.view.PersonAdvancedSearchView
import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import net.hedtech.banner.general.person.view.PersonAdvancedFilterView
import net.hedtech.banner.general.person.view.PersonPersonView

class PersonAdvancedFilterIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['SOAIDEN'] // Since we are not testing a controller, we need to explicitly set this (removing GEAPART because of GUOBOBS_UI_VERSION = B)
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
