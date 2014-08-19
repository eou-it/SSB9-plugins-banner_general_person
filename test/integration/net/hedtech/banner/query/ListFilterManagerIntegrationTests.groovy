/*******************************************************************************
 Copyright 2009-2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.query

import net.hedtech.banner.exceptions.ApplicationException
import org.hibernate.Criteria
import net.hedtech.banner.general.person.view.PersonPersonView
import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 *
 */
class ListFilterManagerIntegrationTests extends BaseIntegrationTestCase {
    def filterDefinition

    protected void setUp( ) {
        formContext = ['GUAGMNU']
        super.setUp()
        filterDefinition = [ [field: "searchLastName", preProcessor: ListFilterManager.capitalize],
            [field: "searchFirstName", preProcessor: ListFilterManager.capitalize],
            [field: "searchMiddleName", preProcessor: ListFilterManager.capitalize]]
    }


    protected void tearDown( ) {
        super.tearDown()
    }


    void testSaveInvalidFilter() {
        // bad operator
        // Setup a filter for lastName = "Smith"
        def lastNameFilter =
            ["filter[0][field]" : "searchLastName",
                    "filter[0][value]" : "SmitH",
                    "filter[0][operator]" : "nope"
            ]

        def lfm = new ListFilterManager(PersonPersonView, filterDefinition)
        shouldFail(ApplicationException) {
            lfm.saveFilter(lastNameFilter)
        }
    }


    void testGetCriteriaLastNameOnly( ) {
        // Setup a filter for lastName = "Smith"
        def lastNameFilter =
                ["filter[0][field]" : "searchLastName",
                 "filter[0][value]" : "SmitH",
                 "filter[0][operator]" : "eq"
                ]

        def lfm = new ListFilterManager(PersonPersonView, filterDefinition)
        lfm.saveFilter(lastNameFilter)
        Criteria cr = lfm.getCriterionObject(sessionFactory.getCurrentSession())

        def results = cr.list()
        assertTrue results.size() > 0

        results.each { it ->
            assertEquals "smith", it.lastName.toLowerCase()
        }
    }

    void testGetCriteriaLastNameFirstName( ) {
        // Setup a filter for lastName = "Smith"
        def lastNameFilter =
            ["filter[0][field]" : "searchLastName",
                    "filter[0][value]" : "SmitH",
                    "filter[0][operator]" : "eq",
                    "filter[1][field]" : "searchFirstName",
                    "filter[1][value]" : "am",
                    "filter[1][operator]" : "st"
            ]

        def lfm = new ListFilterManager(PersonPersonView, filterDefinition)
        lfm.saveFilter(lastNameFilter)
        Criteria cr = lfm.getCriterionObject(sessionFactory.getCurrentSession())

        def results = cr.list()
        assertTrue results.size() > 0

        results.each { it ->
            assertEquals "Smith", it.lastName
            assertTrue it.firstName.startsWith("Am")
        }
    }


    void testGetCriteriaLastNameFirstNameMi( ) {
        // Setup a filter for lastName = "Smith"
        def lastNameFilter =
            ["filter[0][field]" : "searchLastName",
                    "filter[0][value]" : "SmitH",
                    "filter[0][operator]" : "eq",
                    "filter[1][field]" : "searchFirstName",
                    "filter[1][value]" : "am",
                    "filter[1][operator]" : "st",
                    "filter[2][field]" : "searchMiddleName",
                    "filter[2][value]" : "yN",
                    "filter[2][operator]" : "co"
            ]

        def lfm = new ListFilterManager(PersonPersonView, filterDefinition)
        lfm.saveFilter(lastNameFilter)
        Criteria cr = lfm.getCriterionObject(sessionFactory.getCurrentSession())

        def results = cr.list()
        assertTrue results.size() > 0

        results.each { it ->
            assertEquals "Smith", it.lastName
            assertTrue it.firstName.startsWith("Am")
            assertTrue it.middleName.indexOf("yn") > -1
        }
    }


    void testGetCriteriaLastNameFirstNameMiNE( ) {
        // Setup a filter for lastName = "Smith"
        def lastNameFilter =
            ["filter[0][field]" : "searchLastName",
                    "filter[0][value]" : "SmitH",
                    "filter[0][operator]" : "eq",
                    "filter[1][field]" : "searchFirstName",
                    "filter[1][value]" : "am",
                    "filter[1][operator]" : "st",
                    "filter[2][field]" : "searchMiddleName",
                    "filter[2][value]" : "Lynn",
                    "filter[2][operator]" : "ne"
            ]

        def lfm = new ListFilterManager(PersonPersonView, filterDefinition)
        lfm.saveFilter(lastNameFilter)
        Criteria cr = lfm.getCriterionObject(sessionFactory.getCurrentSession())

        def results = cr.list()
        assertTrue results.size() > 0

        results.each { it ->
            assertEquals "Smith", it.lastName
            assertTrue it.firstName.startsWith("Am")
            assertTrue it.middleName.indexOf("Lynn") == -1
        }
    }
}
