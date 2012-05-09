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

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

import com.sungardhe.banner.general.person.view.PersonAdvancedSearchView
import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import com.sungardhe.banner.general.person.view.PersonAdvancedFilterView
import com.sungardhe.banner.general.person.view.PersonPersonView

class PersonAdvancedFilterIntegrationTests extends BaseIntegrationTestCase {

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
