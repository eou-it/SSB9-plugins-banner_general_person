/*******************************************************************************
 Copyright 2009-2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.query

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.person.PersonEmail
import org.hibernate.Criteria
import org.hibernate.criterion.Criterion
import org.hibernate.criterion.Restrictions
import org.hibernate.Session
import net.hedtech.banner.general.person.view.PersonPersonView
import net.hedtech.banner.testing.BaseIntegrationTestCase
import net.hedtech.banner.general.system.EmailType

/**
 *
 */
class ListFilterManagerIntegrationTests extends BaseIntegrationTestCase {
    def filterDefinition

    protected void setUp( ) {
        formContext = ['GUAGMNU']
        super.setUp()
        filterDefinition = [ [field: [code: "searchLastName", description: "Last Name"],
                operators: ["eq", "ne", "st"], preProcessor: ListFilterManager.capitalize],
            [field: [code: "searchFirstName", description: "First Name"], type: "freeform",
                    operators: ["eq", "ne", "st"], preProcessor: ListFilterManager.capitalize],
            [field: [code: "searchMiddleName", description: "Middle Name"], type: "freeform",
                    operators: ["eq", "ne", "st"], preProcessor: ListFilterManager.capitalize],
            [field: [code: "isStudent", description: "Is Student"], type: "operatoronly",
                    operators: ["t", "f"], preProcessor: ListFilterManager.capitalize],
            [field: [code: "specialLastName", description: "Spedial Last Name"], type: "freeform",
                    operators: ["eq", "ne", "st"], specialProcessor: ListFilterManagerIntegrationTests.specialProcessor]]
    }


    protected void tearDown( ) {
        super.tearDown()
    }


    void testSaveInvalidFilterBadOperator() {
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


    void testSaveInvalidFilterOperatorOnlyWithValue() {
        // bad operator
        // Setup a filter for lastName = "Smith"
        def lastNameFilter =
            ["filter[0][field]" : "isStudent",
                    "filter[0][value]" : "shouldhavenovalue",
                    "filter[0][operator]" : "t"
            ]

        def lfm = new ListFilterManager(PersonPersonView, filterDefinition)
        shouldFail(ApplicationException) {
            lfm.saveFilter(lastNameFilter)
        }
    }


    void testSaveInvalidFilterNoDef() {
        // bad operator
        // Setup a filter for lastName = "Smith"
        def lastNameFilter =
            ["filter[0][field]" : "nodef",
                    "filter[0][value]" : "shouldhavenovalue",
                    "filter[0][operator]" : "eq"
            ]

        def lfm = new ListFilterManager(PersonPersonView, filterDefinition)
        shouldFail(ApplicationException) {
            lfm.saveFilter(lastNameFilter)
        }
    }


    void testSaveInvalidType() {
        def filterDef2 = [ [field: [code: "searchLastName", description: "Last Name"], type: "invalid",
                operators: ["eq", "ne", "st"], preProcessor: ListFilterManager.capitalize]]

        // bad operator
        // Setup a filter for lastName = "Smith"
        def lastNameFilter =
            ["filter[0][field]" : "searchLastName",
                    "filter[0][value]" : "shouldhavenovalue",
                    "filter[0][operator]" : "eq"
            ]

        def lfm = new ListFilterManager(PersonPersonView, filterDef2)
        shouldFail(ApplicationException) {
            lfm.saveFilter(lastNameFilter)
        }
    }


    void testSaveValidationTypeNoDomain() {
        def filterDef2 = [ [field: [code: "emailType", description: "Email Type"], type: "validation",
                operators: ["eq", "ne",]] ]

        // bad operator
        // Setup a filter for lastName = "Smith"
        def emailTypeFilter =
            ["filter[0][field]" : "emailType",
                    "filter[0][value]" : "Business",
                    "filter[0][operator]" : "eq"
            ]

        def lfm = new ListFilterManager(PersonEmail, filterDef2)
        shouldFail(ApplicationException) {
            lfm.saveFilter(emailTypeFilter)
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
        Session session = sessionFactory.getCurrentSession()
        Criterion cro = lfm.getCriterionObject()
        Criteria cr = session.createCriteria(PersonPersonView, "cr1")
        cr.add(cro)


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
        Session session = sessionFactory.getCurrentSession()
        Criterion cro = lfm.getCriterionObject()
        Criteria cr = session.createCriteria(PersonPersonView, "cr1")
        cr.add(cro)

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
        Session session = sessionFactory.getCurrentSession()
        Criterion cro = lfm.getCriterionObject()
        Criteria cr = session.createCriteria(PersonPersonView, "cr1")
        cr.add(cro)

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
        Session session = sessionFactory.getCurrentSession()
        Criterion cro = lfm.getCriterionObject()
        Criteria cr = session.createCriteria(PersonPersonView, "cr1")
        cr.add(cro)

        def results = cr.list()
        assertTrue results.size() > 0

        results.each { it ->
            assertEquals "Smith", it.lastName
            assertTrue it.firstName.startsWith("Am")
            assertTrue it.middleName.indexOf("Lynn") == -1
        }
    }


    void testUseSpecialGenerator() {
        // Setup a filter for lastName = "Smith"
        def lastNameFilter =
            ["filter[0][field]" : "specialLastName",
                    "filter[0][value]" : "Smith",
                    "filter[0][operator]" : "eq"
            ]

        def lfm = new ListFilterManager(PersonPersonView, filterDefinition)
        lfm.saveFilter(lastNameFilter)
        Session session = sessionFactory.getCurrentSession()
        Criterion cro = lfm.getCriterionObject()
        Criteria cr = session.createCriteria(PersonPersonView, "cr1")
        cr.add(cro)

        def results = cr.list()
        assertTrue results.size() > 0

        results.each { it ->
            assertTrue "Smith" != it.lastName
        }
    }


    void testDefaultMapGenerator() {
        def all = EmailType.findAll()
        assertNotNull all
        assertTrue all.size() > 0
        all.each { it->
            def map = ListFilterManager.defaultMapGenerator(it)
            assertEquals it.description, map?.description
            assertEquals it.code, map?.code
        }

    }


    static def specialProcessor = { it ->
        // We get the whole map, which includes field, operator
        // For our test, return a restriction.ne regardless of the operator
        return Restrictions.ne("lastName", it.value)

    }

}
