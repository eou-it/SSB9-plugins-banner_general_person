/*********************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.person.view.PersonEmergencyContactView
import net.hedtech.banner.general.person.view.PersonEmergencyContactViewService
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test


class PersonEmergencyContactViewServiceIntegrationTests extends BaseIntegrationTestCase {
    PersonEmergencyContactViewService personEmergencyContactViewService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @Test
    void testCreateNotAllowed() {
        shouldFail(ApplicationException) {
            personEmergencyContactViewService.create([PersonEmergencyContactView: new PersonEmergencyContactView()])
        }
    }

    @Test
    void testUpdateNotAllowed() {
        shouldFail(ApplicationException) {
            personEmergencyContactViewService.update([PersonEmergencyContactView: new PersonEmergencyContactView()])
        }
    }

    @Test
    void testDeleteNotAllowed() {
        shouldFail(ApplicationException) {
            personEmergencyContactViewService.delete([PersonEmergencyContactView: new PersonEmergencyContactView()])
        }
    }

    @Test
    void testReadAllowed() {
        PersonEmergencyContactView personEmergencyContactView = PersonEmergencyContactView.findAll()[0]
        PersonEmergencyContactView objReadUsingService = personEmergencyContactViewService.read(personEmergencyContactView.id) as PersonEmergencyContactView
        assertNotNull objReadUsingService
        assertEquals(personEmergencyContactView.id, objReadUsingService.id)
    }

    @Test
    void testFetchAllByCriteria() {
        List<PersonEmergencyContactView> personEmergencyContactViews = personEmergencyContactViewService.fetchAllByCriteria([:])
        assertNotNull personEmergencyContactViews
        assertNotNull personEmergencyContactViews[0].id
    }

    @Test
    void testCountByCriteria() {
        def personEmergencyContactViewCount = personEmergencyContactViewService.countByCriteria([:])
        assertTrue personEmergencyContactViewCount > 0
    }

}
