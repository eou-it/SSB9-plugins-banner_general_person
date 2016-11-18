/*********************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import net.hedtech.banner.general.person.view.PersonEmergencyContactView
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.jdbc.UncategorizedSQLException


class PersonEmergencyContactViewIntegrationTests extends BaseIntegrationTestCase {
    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * Tests that view does not allow create,update,delete  operations and is readonly
     */

    @Test
    void testCreateExceptionResults() {
        PersonEmergencyContactView personEmergencyContactView = PersonEmergencyContactView.findAll([max: 10])[0]
        assertNotNull personEmergencyContactView.toString()
        PersonEmergencyContactView newPersonParentView = new PersonEmergencyContactView(personEmergencyContactView.properties)
        newPersonParentView.firstName = "John"
        newPersonParentView.id = 99999
        shouldFail(UncategorizedSQLException) {
            newPersonParentView.save(flush: true, onError: true)
        }

    }

    @Test
    void testUpdateExceptionResults() {
        PersonEmergencyContactView personEmergencyContactView = PersonEmergencyContactView.findAll([max: 10])[0]
        assertNotNull personEmergencyContactView.toString()
        personEmergencyContactView.lastName = "Smith"
        shouldFail(UncategorizedSQLException) {
            personEmergencyContactView.save(flush: true, onError: true)
        }
    }

    @Test
    void testDeleteExceptionResults() {
        PersonEmergencyContactView personEmergencyContactView = PersonEmergencyContactView.findAll([max: 10])[0]
        assertNotNull personEmergencyContactView.toString()
        shouldFail() {
            personEmergencyContactView.delete(flush: true, onError: true)
        }
    }
}
