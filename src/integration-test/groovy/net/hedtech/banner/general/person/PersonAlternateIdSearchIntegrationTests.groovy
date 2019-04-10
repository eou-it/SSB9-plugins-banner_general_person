/*********************************************************************************
  Copyright 2009-2018 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.junit.Before
import org.junit.Test
import org.junit.After


import net.hedtech.banner.testing.BaseIntegrationTestCase
import net.hedtech.banner.general.person.view.PersonAlternateIdView

@Integration
@Rollback
class PersonAlternateIdSearchIntegrationTests extends BaseIntegrationTestCase {

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
     * Tests the Person Search by SSN.
     */
    @Test
    void testPersonSearchBySSN() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def id
        def lastName = "Bates"
        def firstName
        def midName
        def ssn = ""
        def changeIndicator

        //search by last name
        def results = PersonAlternateIdView.fetchPerson(id, lastName, firstName, midName, changeIndicator, ssn, pagingAndSortParams)
        assert results.size() >= 4

        lastName = "Mcelroy" //Ducey

        results = PersonAlternateIdView.fetchPerson(id, lastName, firstName, midName, changeIndicator, ssn, pagingAndSortParams)
        assert results.size() == 2

        //search by ssn
        ssn = "000345703"
        lastName = ""
        results = PersonAlternateIdView.fetchPerson(id, lastName, firstName, midName, changeIndicator, ssn, pagingAndSortParams)
        assertNotNull results

    }


    /**
     * Tests the list of persons for inquiry page.
     * Search by preferredFirstName.
     */
    @Test
    void testDynamicFinder1() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]

        param."preferredFirstName" = "Ant"

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
