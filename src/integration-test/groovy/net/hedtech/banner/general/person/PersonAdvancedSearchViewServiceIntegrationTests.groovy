/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/

package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import net.hedtech.banner.general.person.view.PersonAdvancedSearchView
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

@Integration
@Rollback
class PersonAdvancedSearchViewServiceIntegrationTests extends BaseIntegrationTestCase {

    PersonAdvancedSearchViewService personAdvancedSearchViewService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testFetchAllByCriteria_EachPageHasUniqueRecords_WithoutPassingSortField() {
        int max = 500
        int offset = 0

        def content = [:]
        def totalCount = personAdvancedSearchViewService.countByCriteria(content)
        def surrogateIds = [:]

        while (offset < totalCount) {
            def entities = personAdvancedSearchViewService.fetchAllByCriteria(content, null, null, max, offset)
            assertTrue entities.size() <= max
            assertListIsSortedOnField(entities, "id")

            entities.each {
                assertTrue !surrogateIds.containsKey(it.id)
                surrogateIds.put(it.id, it)
            }

            offset += max
        }
    }


    @Test
    void testFetchAllByCriteria_EachPageHasUniqueRecords_PassingNonUniqueSortField() {
        int max = 500
        int offset = 0

        def content = [:]
        def totalCount = personAdvancedSearchViewService.countByCriteria(content)
        def surrogateIds = [:]

        while (offset < totalCount) {
            def entities = personAdvancedSearchViewService.fetchAllByCriteria(content, "lastName", null, max, offset)
            assertTrue entities.size() <= max
            assertListIsSortedOnField(entities, "lastName")

            entities.each {
                assertTrue !surrogateIds.containsKey(it.id)
                surrogateIds.put(it.id, it)
            }

            offset += max
        }
    }


    @Test
    void testFetchAllWithGuidByCriteria() {
        String firstName
        String lastName
        String middleName
        String surnamePrefix
        String bannerId
        String namePrefix
        String nameSuffix

        //with out pagination
        List entities = personAdvancedSearchViewService.fetchAllByCriteria(params)
        assertFalse entities.isEmpty()
        entities.each {
            PersonAdvancedSearchView personSearchView = it
            assertNotNull personSearchView

            //set filter value
            if (personSearchView.bannerId && !bannerId) {
                bannerId = personSearchView.bannerId
            }

            if (personSearchView.firstName && !firstName) {
                firstName = personSearchView.firstName
            }

            if (personSearchView.middleName && !middleName) {
                middleName = personSearchView.middleName
            }

            if (personSearchView.lastName && !lastName) {
                lastName = personSearchView.lastName
            }

            if (personSearchView.surnamePrefix && !surnamePrefix) {
                surnamePrefix = personSearchView.surnamePrefix
            }

            if (personSearchView.namePrefix && !namePrefix) {
                namePrefix = personSearchView.namePrefix
            }

            if (personSearchView.nameSuffix && !nameSuffix) {
                nameSuffix = personSearchView.nameSuffix
            }

        }
        Long expectedCount = personAdvancedSearchViewService.countByCriteria(params)
        assertEquals entities.size(), expectedCount

        verifyFilterByBannerId(bannerId)
        verifyFilterByFirstName(firstName)
        verifyFilterByMiddleName(middleName)
        verifyFilterByLastName(lastName)
        verifyFilterBySurnamePrefix(surnamePrefix)
        verifyFilterByNameSuffix(nameSuffix)
        verifyFilterByNamePrefix(namePrefix)

        //with pagination
        params.clear()
        entities = personAdvancedSearchViewService.fetchAllByCriteria(params, null, null, 1, 0)
        assertFalse entities.isEmpty()
        assertEquals 1, entities.size()
        verifyFilterDataResponse(params, entities)
    }


    private void verifyFilterByBannerId(String bannerId) {
        // bannerId filter
        params.clear()
        assertNotNull bannerId
        params.bannerId = bannerId
        def entities = personAdvancedSearchViewService.fetchAllByCriteria(params)
        verifyFilterDataResponse(params, entities)
        Long expectedCount = personAdvancedSearchViewService.countByCriteria(params)
        assertEquals entities.size(), expectedCount
    }


    private void verifyFilterByFirstName(String firstName) {
        // firstName filter
        params.clear()
        assertNotNull firstName
        params.firstName = firstName
        params.sort = 'lastName'
        params.order = 'desc'
        def entities = personAdvancedSearchViewService.fetchAllByCriteria(params, params.sort, params.order, 0, -1,)
        verifyFilterDataResponse(params, entities)
        Long expectedCount = personAdvancedSearchViewService.countByCriteria(params)
        assertEquals entities.size(), expectedCount
    }


    private void verifyFilterByMiddleName(String middleName) {
        // middleName filter
        params.clear()
        assertNotNull middleName
        params.middleName = middleName
        params.sort = 'firstName'
        params.order = 'asc'
        def entities = personAdvancedSearchViewService.fetchAllByCriteria(params, params.sort, params.order, 0, -1,)
        verifyFilterDataResponse(params, entities)
        Long expectedCount = personAdvancedSearchViewService.countByCriteria(params)
        assertEquals entities.size(), expectedCount
    }


    private void verifyFilterByLastName(String lastName) {
        // lastName filter
        params.clear()
        assertNotNull lastName
        params.lastName = lastName
        params.sort = 'firstName'
        params.order = 'desc'
        def entities = personAdvancedSearchViewService.fetchAllByCriteria(params, params.sort, params.order, 0, -1,)
        verifyFilterDataResponse(params, entities)
        Long expectedCount = personAdvancedSearchViewService.countByCriteria(params)
        assertEquals entities.size(), expectedCount
    }


    private void verifyFilterBySurnamePrefix(String surnamePrefix) {
        // surnamePrefix filter
        params.clear()
        assertNotNull surnamePrefix
        params.surnamePrefix = surnamePrefix
        params.sort = 'lastName'
        params.order = 'asc'
        def entities = personAdvancedSearchViewService.fetchAllByCriteria(params, params.sort, params.order, 0, -1,)
        verifyFilterDataResponse(params, entities)
        Long expectedCount = personAdvancedSearchViewService.countByCriteria(params)
        assertEquals entities.size(), expectedCount
    }


    private void verifyFilterByNameSuffix(String nameSuffix) {
        // nameSuffix filter
        params.clear()
        assertNotNull nameSuffix
        params.nameSuffix = nameSuffix
        params.sort = 'firstName'
        params.order = 'asc'
        def entities = personAdvancedSearchViewService.fetchAllByCriteria(params, params.sort, params.order, 0, -1,)
        verifyFilterDataResponse(params, entities)
        Long expectedCount = personAdvancedSearchViewService.countByCriteria(params)
        assertEquals entities.size(), expectedCount
    }


    private void verifyFilterByNamePrefix(String namePrefix) {
        // namePrefix filter
        params.clear()
        assertNotNull namePrefix
        params.namePrefix = namePrefix
        params.sort = 'firstName'
        params.order = 'desc'
        def entities = personAdvancedSearchViewService.fetchAllByCriteria(params, params.sort, params.order, 0, -1,)
        verifyFilterDataResponse(params, entities)
        Long expectedCount = personAdvancedSearchViewService.countByCriteria(params)
        assertEquals entities.size(), expectedCount
    }


    private void verifyFilterDataResponse(params, entities) {
        assertFalse entities.isEmpty()
        entities.each {
            PersonAdvancedSearchView personSearchView = it
            assertNotNull personSearchView
            if (params.bannerId) {
                assertEquals params.bannerId, personSearchView.bannerId
            }

            if (params.firstName) {
                assertEquals params.firstName.toLowerCase(), personSearchView.firstName.toLowerCase()
            }

            if (params.middleName) {
                assertEquals params.middleName.toLowerCase(), personSearchView.middleName.toLowerCase()
            }

            if (params.lastName) {
                assertEquals params.lastName.toLowerCase(), personSearchView.lastName.toLowerCase()
            }

            if (params.surnamePrefix) {
                assertEquals params.surnamePrefix.toLowerCase(), personSearchView.surnamePrefix.toLowerCase()
            }

            if (params.namePrefix) {
                assertEquals params.namePrefix.toLowerCase(), personSearchView.namePrefix.toLowerCase()
            }

            if (params.nameSuffix) {
                assertEquals params.nameSuffix.toLowerCase(), personSearchView.nameSuffix.toLowerCase()
            }

        }
    }


    private void assertListIsSortedOnField(def list, String field, String sortOrder = "ASC") {
        def prevListItemVal
        list.each {
            def curListItemVal = it[field]
            if (!prevListItemVal) {
                prevListItemVal = curListItemVal
            }
            if (sortOrder == "ASC") {
                assertTrue prevListItemVal.compareTo(curListItemVal) < 0 || prevListItemVal.compareTo(curListItemVal) == 0
            } else {
                assertTrue prevListItemVal.compareTo(curListItemVal) > 0 || prevListItemVal.compareTo(curListItemVal) == 0
            }
            prevListItemVal = curListItemVal
        }
    }

}
