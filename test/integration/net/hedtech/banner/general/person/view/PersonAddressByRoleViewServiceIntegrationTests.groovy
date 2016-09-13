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

    @Test
    void testGetActiveAddressesByRolesWithUserHavingMultipleRoles() {
        // This user has both STUDENT and EMPLOYEE roles
        def pidm = PersonUtility.getPerson("710000007").pidm

        // The PersonAddressByRoleView.fetchAddressesByPidmAndRoles method called by
        // personAddressByRoleViewService.getActiveAddressesByRoles (below) returns
        // multiple *non-unique* addresses for users *having multiple roles*.  We're testing
        // here to make sure that personAddressByRoleViewService.getActiveAddressesByRoles
        // fixes that.
        // NOTE:  This user is configured with two roles *in the database*.  We also explicitly
        // pass in two roles *here* (see next line).  Both of these criteria must be true for
        // PersonAddressByRoleView.fetchAddressesByPidmAndRoles to return non-unique values.
        def roles = ['STUDENT', 'EMPLOYEE']

        def entityResult = PersonAddressByRoleView.fetchAddressesByPidmAndRoles([pidm:pidm, roles:roles])
        def serviceResult = personAddressByRoleViewService.getActiveAddressesByRoles(roles, pidm)

        // A convoluted test to determine if the service method result is half the size of the
        // entity method result, while skirting double precision issues (i.e. ideally we'd write the
        // below as "assertEquals serviceResult.size(), entityResult.size() / 2", but then we run into
        // double precision issues).  The service method result is half the size because it removed
        // non-unique addresses that the entity method handed it.
        def serviceResultSize = serviceResult.size()
        assertEquals serviceResultSize, entityResult.size() - serviceResultSize
    }
}