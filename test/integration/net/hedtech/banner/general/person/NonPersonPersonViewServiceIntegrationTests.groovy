/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.person.view.NonPersonPersonView
import net.hedtech.banner.general.person.view.NonPersonPersonViewService
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

class NonPersonPersonViewServiceIntegrationTests extends BaseIntegrationTestCase {

    NonPersonPersonViewService nonPersonPersonViewService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @Test
    void testCreateNotAllowed() {
        shouldFail(ApplicationException) {
            nonPersonPersonViewService.create([NonPersonPersonView: new NonPersonPersonView()])
        }
    }

    @Test
    void testUpdateNotAllowed() {
        shouldFail(ApplicationException) {
            nonPersonPersonViewService.update([NonPersonPersonView: new NonPersonPersonView()])
        }
    }

    @Test
    void testDeleteNotAllowed() {
        shouldFail(ApplicationException) {
            nonPersonPersonViewService.delete([NonPersonPersonView: new NonPersonPersonView()])
        }
    }

    @Test
    void testFetchAllWithGuidByPidmInList() {
        List rows = nonPersonPersonViewService.fetchAllWithGuidByCriteria(params, 1, 0)
        assertNotNull rows
        assertFalse rows.isEmpty()
        List<Integer> pidms = rows.nonPersonPersonView.pidm
        assertNotNull pidms
        assertFalse pidms.isEmpty()
        List entitiesList = nonPersonPersonViewService.fetchAllWithGuidByPidmInList(pidms)
        assertNotNull entitiesList
        assertFalse entitiesList.isEmpty()
        assertEquals rows.size(), entitiesList.size()

        Iterator iterator = rows.iterator()
        Iterator iterator1 = rows.iterator()
        while (iterator.hasNext() && iterator1.hasNext()) {
            Map actualMap = iterator.next()
            Map expectedMap = iterator1.next()
            assertEquals actualMap.nonPersonPersonView, expectedMap.nonPersonPersonView
            assertEquals actualMap.globalUniqueIdentifier, expectedMap.globalUniqueIdentifier
        }
    }

    @Test
    void testFetchAllWithGuidByCriteria() {
        List rows = nonPersonPersonViewService.fetchAllWithGuidByCriteria(params, 1, 0)
        assertNotNull rows
        assertFalse rows.isEmpty()
        String bannerId = rows.nonPersonPersonView.bannerId[0]
        assertNotNull bannerId
        List entitiesList = nonPersonPersonViewService.fetchAllWithGuidByCriteria([bannerId: bannerId])
        assertNotNull entitiesList
        assertFalse entitiesList.isEmpty()
        assertEquals rows.size(), entitiesList.size()

        int actualCount = nonPersonPersonViewService.countByCriteria([bannerId: bannerId])
        assertNotNull actualCount
        assertEquals actualCount, entitiesList.size()

        Iterator iterator = rows.iterator()
        Iterator iterator1 = rows.iterator()
        while (iterator.hasNext() && iterator1.hasNext()) {
            Map actualMap = iterator.next()
            Map expectedMap = iterator1.next()
            assertEquals actualMap.nonPersonPersonView, expectedMap.nonPersonPersonView
            assertEquals actualMap.globalUniqueIdentifier, expectedMap.globalUniqueIdentifier
        }
    }

    @Test
    void testFetchAllWithGuid() {
        List rows = nonPersonPersonViewService.fetchAllWithGuidByCriteria(params)
        assertNotNull rows
        assertFalse rows.isEmpty()
        List<Integer> pidms = rows.nonPersonPersonView.pidm
        assertNotNull pidms
        assertFalse pidms.isEmpty()
        List entitiesList = nonPersonPersonViewService.fetchAllWithGuidByPidmInList(pidms)
        assertNotNull entitiesList
        assertFalse entitiesList.isEmpty()
        assertEquals rows.size(), entitiesList.size()

        int actualCount = nonPersonPersonViewService.countByCriteria(params)
        assertNotNull actualCount
        assertEquals actualCount, entitiesList.size()

        Iterator iterator = rows.iterator()
        Iterator iterator1 = rows.iterator()
        while (iterator.hasNext() && iterator1.hasNext()) {
            Map actualMap = iterator.next()
            Map expectedMap = iterator1.next()
            assertEquals actualMap.nonPersonPersonView, expectedMap.nonPersonPersonView
            assertEquals actualMap.globalUniqueIdentifier, expectedMap.globalUniqueIdentifier
        }
    }

    @Test
    void testFetchByGuid() {
        List rows = nonPersonPersonViewService.fetchAllWithGuidByCriteria(params, 1, 0)
        assertNotNull rows
        assertFalse rows.isEmpty()
        String guid = rows.globalUniqueIdentifier.guid[0]
        assertNotNull guid
        Map entitiesMap = nonPersonPersonViewService.fetchByGuid(guid)
        assertNotNull entitiesMap
        assertFalse entitiesMap.isEmpty()

        Iterator iterator = rows.iterator()
        while (iterator.hasNext()) {
            Map actualMap = iterator.next()
            assertEquals actualMap.nonPersonPersonView, entitiesMap.nonPersonPersonView
            assertEquals actualMap.globalUniqueIdentifier, entitiesMap.globalUniqueIdentifier
        }
    }

    @Test
    void testFetchByInvalidGuid() {
        List rows = nonPersonPersonViewService.fetchAllWithGuidByCriteria(params, 1, 0)
        assertNotNull rows
        assertFalse rows.isEmpty()
        String guid = rows.globalUniqueIdentifier.guid[0]
        assertNotNull guid
        Map entitiesMap = nonPersonPersonViewService.fetchByGuid(guid.substring(5))
        assertNotNull entitiesMap
        assertTrue entitiesMap.isEmpty()
    }

    @Test
    void testCountByCriteria(){
        List rows = nonPersonPersonViewService.fetchAllWithGuidByCriteria(params, 1, 0)
        assertNotNull rows
        assertFalse rows.isEmpty()
        String bannerId = rows.nonPersonPersonView.bannerId[0]+"Test"
        assertNotNull bannerId
        List entitiesList = nonPersonPersonViewService.fetchAllWithGuidByCriteria([bannerId: bannerId])
        assertNotNull entitiesList

        int actualCount = nonPersonPersonViewService.countByCriteria([bannerId: bannerId])
        assertNotNull actualCount
        assertEquals actualCount, entitiesList.size()
    }

}

