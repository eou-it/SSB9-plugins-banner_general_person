/*********************************************************************************
 Copyright 2009-2012 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 ********************************************************************************* */

package com.sungardhe.banner.general.person


import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.general.person.view.PersonAlternateIdView

class PersonAlternateIdSearchIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GEAPART'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    /**
     * Tests the Person Search by SSN.
     */
    def testPersonSearchBySSN() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def id
        def lastName = "Duck"
        def firstName
        def midName
        def ssn = ""
        def changeIndicator

        //search by last name
        def results = PersonAlternateIdView.fetchPerson(id, lastName, firstName, midName, changeIndicator, ssn, pagingAndSortParams)
        assert results.size() == 4

        lastName = "Duc" //Ducey

        results = PersonAlternateIdView.fetchPerson(id, lastName, firstName, midName, changeIndicator, ssn, pagingAndSortParams)
        assert results.size() == 5

        //search by ssn
        ssn = "439590585"
        lastName = ""
        results = PersonAlternateIdView.fetchPerson(id, lastName, firstName, midName, changeIndicator, ssn, pagingAndSortParams)
        assertNotNull results

    }


    /**
     * Tests the list of persons for inquiry page.
     * Search by preferredFirstName.
     */
    def testDynamicFinder1() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]

        param."preferredFirstName" = "Trouble"

        filterData.params = param

        def m0 = [:]
        m0."key" = "preferredFirstName"
        m0."binding" = "preferredFirstName"
        m0."operator" = "contains"

        def x = []
        x.add(m0)

        filterData.criteria = x

        def result = PersonAlternateIdView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() > 0
        assertNotNull result[0].ssn
    }

}
