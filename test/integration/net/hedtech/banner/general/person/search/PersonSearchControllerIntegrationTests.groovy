/*********************************************************************************
Copyright 2013 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
package net.hedtech.banner.general.person.search

import net.hedtech.banner.testing.BaseIntegrationTestCase
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
        formContext = ['SOAIDEN'] //(removing GEAPART because of GUOBOBS_UI_VERSION = B)
        controller = new PersonSearchController()
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    /**
     * Test controller by Id.
     */
    def testSearchById() {
        controller.request.contentType = "text/json"
        controller.params.searchFilter = "HOS00001"
        controller.searchById()
        String actualJSON = controller.response.contentAsString
        def data = JSON.parse(actualJSON)
        assertNotNull data
        assertTrue data.people.size() > 1
        //assertEquals "105, Student", data.people[0].formattedName
    }

    /**
     * Test controller by Name.
     */
    def testSearchByName() {
        controller.request.contentType = "text/json"
        controller.params.searchFilter = "Lindblom"
        controller.searchByName()
        String actualJSON = controller.response.contentAsString
        def data = JSON.parse(actualJSON)
        assertNotNull data
        assertTrue data.people.size() > 1
        //assertEquals "Lindblom, Atlas", data.people[0].formattedName
    }

}
