package com.sungardhe.banner.general.person.search

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import grails.converters.JSON

/**
 * Created by IntelliJ IDEA.
 * User: mhitrik
 * Date: 4/25/12
 * Time: 9:56 AM
 * To change this template use File | Settings | File Templates.
 */
class PersonSearchControllerIntegrationTests extends BaseIntegrationTestCase {
    def controller
    def personSearchService

    protected void setUp() {
        formContext = ['GEAPART']
        controller = new PersonSearchController()
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    /**
     * Test controller by lastName search.
     */
    def testSearch() {
        controller.request.contentType = "text/json"
        controller.params.lastName = "Duck"
        controller.search()
        String actualJSON = controller.response.contentAsString
        def data = JSON.parse(actualJSON)
        assertNotNull data
        assertTrue data.people.size() == 4
        assertEquals "Duck", data.people[0].lastName
    }

    /**
     * Test controller by lastName search.
     */
    def testSearch_1() {
        controller.request.contentType = "text/json"
        controller.params.lastName = "Duc"
        controller.search()
        String actualJSON = controller.response.contentAsString
        def data = JSON.parse(actualJSON)
        assertNotNull data
        assertTrue data.people.size() == 5
    }

    /**
     * Test controller by soundexLastName, changeIndicator search.
     */
    def testSearch_2() {
        controller.request.contentType = "text/json"
        controller.params.soundexLastName = "Duck"
        controller.params.changeIndicator = "N"
        controller.search()
        String actualJSON = controller.response.contentAsString
        def data = JSON.parse(actualJSON)
        assertNotNull data
        assertTrue data.people.size() == 3
    }

    /**
     * Test controller with pagination support.
     */
    def testSearch_4() {
        controller.request.contentType = "text/json"
        controller.params.soundexLastName = "Duck"
        controller.params.max = 8
        controller.params.offset = 0
        controller.search()
        String actualJSON = controller.response.contentAsString
        def data = JSON.parse(actualJSON)
        assertNotNull data
        assertTrue data.people.size() == 8
    }

    /**
     * Test controller by lastName and pidms search.
     */
    def testSearch_5() {
        controller.request.contentType = "text/json"
        def pidmList = []
        pidmList.add(new Integer("1358"))
        pidmList.add(new Integer("1355"))

        controller.params.lastName = "Duck"
        controller.params.pidms = pidmList
        controller.params.pagingAndSortParams = ["max": 8, "offset": 0]
        controller.searchPidms()
        String actualJSON = controller.response.contentAsString
        def data = JSON.parse(actualJSON)
        assertNotNull data
        assertTrue data.people.size() == 2
        assertEquals "Duck", data.people[0].lastName
        assertEquals "Duck", data.people[1].lastName
    }

     /**
     * Test controller by text search.
     */
    def testSearch_6() {

        controller.request.contentType = "text/json"

        controller.params.searchFilter = "LINDBLOM GANNON"
        controller.params.pagingAndSortParams = ["max": 8, "offset": 0]

        controller.searchText()
        String actualJSON = controller.response.contentAsString
        def data = JSON.parse(actualJSON)
        assertNotNull data
        assertTrue data.people.size() > 10
    }
}
