/*********************************************************************************
Copyright 2012-2018 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
package net.hedtech.banner.general.person.search

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import grails.converters.JSON


@Integration
@Rollback
class PersonSearchControllerIntegrationTests extends BaseIntegrationTestCase {
    def controller
    def personSearchService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] //(removing GEAPART because of GUOBOBS_UI_VERSION = B)
        controller = new PersonSearchController()
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * Test controller by Id.
     */
    @Test
    void testSearchById() {
        controller.request.contentType = "text/json"
        controller.params.searchFilter = "HOS00001"
        controller.searchById()
        String actualJSON = controller.response.contentAsString
        def data = JSON.parse(actualJSON)
        assertNotNull data
        assertTrue data.people.size() >= 1
        //assertEquals "105, Student", data.people[0].formattedName
    }

    /**
     * Test controller by Name.
     */
    @Test
    void testSearchByName() {
        controller.request.contentType = "text/json"
        controller.params.searchFilter = "Bates"
        controller.searchByName()
        String actualJSON = controller.response.contentAsString
        def data = JSON.parse(actualJSON)
        assertNotNull data
        assertTrue data.people.size() >= 1
        //assertEquals "Lindblom, Atlas", data.people[0].formattedName
    }

}
