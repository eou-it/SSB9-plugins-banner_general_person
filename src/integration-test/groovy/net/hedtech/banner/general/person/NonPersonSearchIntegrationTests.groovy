/*********************************************************************************
 Copyright 2009-2018 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import net.hedtech.banner.general.person.view.NonPersonPersonView
import org.junit.Ignore

class NonPersonSearchIntegrationTests extends BaseIntegrationTestCase {

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
     * Tests the Non-Person Search.
     */
    @Test
    void testNonPersonSearch() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def id
        def lastName = "First"
        def soundexLastName
        def changeIndicator
        def nameType = ""

        //search by last name
        def results = NonPersonPersonView.fetchPerson(id, lastName, soundexLastName, changeIndicator, nameType, pagingAndSortParams)
        assert results.size() >= 4

        //search by change indicator
        soundexLastName = "First"
        nameType = NameType.findWhere(code: "LEGL").code
        results = NonPersonPersonView.fetchPerson(id, lastName, soundexLastName, changeIndicator, nameType, pagingAndSortParams)
        assertTrue results.size() == 1
    }

    /**
     * Tests the list of Non-Persons.
     */
    @Test
    void testNonPersonSearchByFilterAndPagination() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def id
        def lastName = "First"
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
    @Test
    void testDynamicFinder1() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."searchLastName" = "%First%"
        filterData.params = param

        def m = [:]
        m."key" = "searchLastName"
        m."binding" = "searchLastName"
        m."operator" = "contains"

        filterData.params = param

        def x = []
        x.add(m)
        filterData.criteria = x

        def result = NonPersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() >= 4
    }

    /**
     * Tests the list of Non-Persons for inquiry page.
     * Search by Id
     */
    @Test
    void testDynamicFinder3() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."bannerId" = "%NP00NP001%"

        filterData.params = param

        def m = [:]
        m."key" = "bannerId"
        m."binding" = "bannerId"
        m."operator" = "contains"

        filterData.params = param

        def x = []
        x.add(m)
        filterData.criteria = x

        def result = NonPersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() == 1
    }

    /**
     * Tests the list of non persons for inquiry page.
     * Search by lastName
     */
    @Ignore
    //TODO grails 221 merge with master- may require seed-data ( see the failure bellow)
    //| Failure:  testDynamicFinder4(net.hedtech.banner.general.person.NonPersonSearchIntegrationTests)
    //|  Assertion failed:
    //assert result.size() == 3
    //|      |      |
    //[]     0      false
    @Test
    void testDynamicFinder4() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."searchLastName" = "%thompson e%"

        filterData.params = param

        def m = [:]
        m."key" = "searchLastName"
        m."binding" = "searchLastName"
        m."operator" = "contains"

        filterData.params = param

        def x = []
        x.add(m)
        filterData.criteria = x

        def result = NonPersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() == 3
    }

    /**
     * Tests no data found.
     */
    @Test
    void testDynamicFinder5() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."searchLastName" = "%thxompson e%"

        filterData.params = param

        def m = [:]
        m."key" = "searchLastName"
        m."binding" = "searchLastName"
        m."operator" = "contains"

        filterData.params = param

        def x = []
        x.add(m)
        filterData.criteria = x

        def result = NonPersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertTrue result.size() == 0
    }

    /**
     * Tests no parameters on query - fetch all.
     */
    @Test
    void testQueryAllWithNoParameters() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]

        filterData.params = param

        def result = NonPersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertTrue result.size() == 8
    }

    /**
     * Tests the list of non persons for inquiry page.
     * Search by soundexLastName
     */
    @Test
    void testDynamicFinderSoundex() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."soundexLastName" = "First" //Thompson is the actual name
        filterData.params = param

        def result = NonPersonPersonView.fetchSearchSoundexEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() >= 4
    }
}
