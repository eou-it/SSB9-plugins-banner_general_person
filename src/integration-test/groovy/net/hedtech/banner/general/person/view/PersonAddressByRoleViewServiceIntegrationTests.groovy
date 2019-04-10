/*******************************************************************************
 Copyright 2016-2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.view

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import net.hedtech.banner.general.person.PersonUtility
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

@Integration
@Rollback
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
        def pidm = PersonUtility.getPerson("GDP000003").pidm

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

        Set set = new HashSet()
        boolean dupesExist = false
        for(def a : entityResult) {
            dupesExist = !set.add(a)
            if(dupesExist) break
        }
        assertTrue dupesExist

        def serviceResult = personAddressByRoleViewService.getActiveAddressesByRoles(roles, pidm)
        assertTrue entityResult.size() >= serviceResult.size()

        set = new HashSet()
        dupesExist = false
        for(def a : serviceResult) {
            dupesExist = !set.add(a)
            if(dupesExist) break
        }
        assertFalse dupesExist
    }
}