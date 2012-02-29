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

package com.sungardhe.banner.general.person

import com.sungardhe.banner.general.person.view.PersonPersonView
import com.sungardhe.banner.general.system.NameType
import com.sungardhe.banner.testing.BaseIntegrationTestCase
import org.junit.Ignore
import com.sungardhe.banner.general.person.view.NonPersonPersonView

class NonPersonSearchIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GEAPART'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    /**
     * Tests the Non-Person Search.
     */
    def testNonPersonSearch() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def id
        def lastName = "Thompson"
        def soundexLastName
        def changeIndicator
        def nameType = ""

        //search by last name
        def results = NonPersonPersonView.fetchPerson(id, lastName, soundexLastName, changeIndicator, nameType, pagingAndSortParams)
        assert results.size() == 6

        //search by change indicator
        soundexLastName = "Tompson"
        nameType = NameType.findWhere(code: "LEGL").code
        results = NonPersonPersonView.fetchPerson(id, lastName, soundexLastName,  changeIndicator, nameType, pagingAndSortParams)
        assertTrue results.size() == 1
    }

    /**
     * Tests the list of Non-Persons.
     */
    def testNonPersonSearchByFilterAndPagination() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def id
        def lastName = "Thompson"
        def soundexLastName
        def changeIndicator
        def nameType = ""

        def results = NonPersonPersonView.fetchPersonList(id, lastName, soundexLastName, changeIndicator, nameType, pagingAndSortParams)

        assertNotNull results.list

        def res = results?.list[0]

        assertNotNull res.bannerId
        assertNotNull res.lastName

    }

    /**
     * Tests the list of Non-Persons for inquiry page.
     * Search by lastName
     */
    def testDynamicFinder1() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."id" = ""
        param."lastName" = "%thompson%"
        param."soundexLastName" = ""
        param."changeIndicator" = ""
        param."nameType" = ""

        filterData.params = param

        def result = NonPersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() == 6
    }


    /**
     * Tests the list of Non-Persons for inquiry page.
     * Search by Id
     */
    def testDynamicFinder3() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."id" = "%A00010216%"
        param."lastName" = ""
        param."soundexLastName" = ""
        param."changeIndicator" = ""
        param."nameType" = ""

        filterData.params = param

        def result = NonPersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() == 1
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by lastName
     */
    def testDynamicFinder4() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."id" = ""
        param."lastName" = "%thompson e%"
        param."soundexLastName" = ""
        param."changeIndicator" = ""
        param."nameType" = ""

        filterData.params = param

        def result = NonPersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() == 3
    }


     /**
     * Tests no data found.
     */
    def testDynamicFinder5() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."id" = ""
        param."lastName" = "%thxompson e%"
        param."soundexLastName" = ""
        param."changeIndicator" = ""
        param."nameType" = ""

        filterData.params = param

        def result = NonPersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertTrue result.size()==0
    }


}
