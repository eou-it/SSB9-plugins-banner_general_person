/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.view

import net.hedtech.banner.general.person.PersonUtility
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class PersonAddressByRoleViewServiceIntegrationTests extends BaseIntegrationTestCase {

    def personAddressByRoleViewService

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
    void testGetActiveAddressesByRoles() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def roles = ['EMPLOYEE']

        def result = personAddressByRoleViewService.getActiveAddressesByRoles(roles, pidm)

        assertEquals '1200 Main Road', result[0].streetLine1  //current address
        assertEquals '123 Fake Street', result[1].streetLine1 //future address
    }
}