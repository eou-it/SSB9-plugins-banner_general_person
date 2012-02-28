/*********************************************************************************
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

package com.sungardhe.banner.general.person

import com.sungardhe.banner.general.person.view.PersonPersonView
import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.general.system.NameType

class PersonSearchIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GEAPART'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    /**
     * Tests the Person Search.
     */
    def testPersonSearch() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def id
        def lastName = "Duck"
        def firstName
        def midName
        def soundexLastName
        def soundexFirstName
        def changeIndicator
        def nameType = ""

        //search by last name
        def results = PersonPersonView.fetchPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)
        assert results.size() == 4

        lastName = "Duc" //Ducey

        results = PersonPersonView.fetchPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)
        assert results.size() == 5

        //search by soundex   [Duck,Dog,Ducey,Diaz,Dawg]
        soundexLastName = "Duck"
        results = PersonPersonView.fetchPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)
        assertTrue results.size() == 8

        //search by change indicator
        soundexLastName = "Duck"
        changeIndicator = "N"
        results = PersonPersonView.fetchPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)
        assertTrue results.size() == 3

        //search by name type
        nameType = NameType.findWhere(code: "LEGL").code
        lastName = "Sam"
        soundexLastName = ""
        changeIndicator = ""
        results = PersonPersonView.fetchPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)
        println results
        assertTrue results.size() == 1

        //fetch all defined by page size with no parameters
        results = PersonPersonView.fetchPerson("", "", "", "", "", "", "", "", pagingAndSortParams)
        println results
        assertTrue results.size() == 8
    }

    /**
     * Tests the list of persons.
     */
    def testPersonSearchByFilterAndPagination() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def id
        def lastName = "Duck"
        def firstName
        def midName
        def soundexLastName
        def soundexFirstName
        def changeIndicator
        def nameType = ""

        def results = PersonPersonView.fetchPersonList(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)

        assertNotNull results.list

        def res = results?.list[0]

        assertNotNull res.bannerId
        assertNotNull res.lastName
        assertNotNull res.firstName
        assertNotNull res.birthDate
    }


     /**
     * Tests the list of persons for inquiry page.
     * Search by lastName
     */
    def testDynamicFinder1() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."id" = ""
        param."lastName" = "%duck%"
        param."firstName" = ""
        param."midName" = ""
        param."soundexLastName" = ""
        param."soundexFirstName" = ""
        param."changeIndicator" = ""
        param."nameType" = ""

        filterData.params = param

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() == 4
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by lastName, firstName
     */
    def testDynamicFinder2() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."id" = ""
        param."lastName" = "%duck%"
        param."firstName" = "%don%"
        param."midName" = ""
        param."soundexLastName" = ""
        param."soundexFirstName" = ""
        param."changeIndicator" = ""
        param."nameType" = ""

        filterData.params = param

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() == 1
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by Id
     */
    def testDynamicFinder3() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."id" = "%A00000654%"
        param."lastName" = ""
        param."firstName" = ""
        param."midName" = ""
        param."soundexLastName" = ""
        param."soundexFirstName" = ""
        param."changeIndicator" = ""
        param."nameType" = ""

        filterData.params = param

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() == 1
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by soundexLastName
     */
    def testDynamicFinder4() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."id" = ""
        param."lastName" = ""
        param."firstName" = ""
        param."midName" = ""
        param."soundexLastName" = "duck"
        param."soundexFirstName" = ""
        param."changeIndicator" = ""
        param."nameType" = ""

        filterData.params = param

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() == 8
    }

     /**
     * Tests the list of persons for inquiry page.
     * Search by soundexLastName, nameType
     */
    def testDynamicFinder5() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."id" = ""
        param."lastName" = ""
        param."firstName" = ""
        param."midName" = ""
        param."soundexLastName" = "McAnderson"
        param."soundexFirstName" = ""
        param."changeIndicator" = ""
        param."nameType" = "LEGL"

        filterData.params = param

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() == 1
        assertEquals "LEGL", result[0].nameType
    }
}
