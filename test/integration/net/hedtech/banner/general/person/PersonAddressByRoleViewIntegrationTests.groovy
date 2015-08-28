/*********************************************************************************
  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import net.hedtech.banner.general.person.view.PersonAddressByRoleView
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.dao.InvalidDataAccessResourceUsageException
import org.junit.Before
import org.junit.Test
import org.junit.After

class PersonAddressByRoleViewIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreatePersonAddressByRole() {
        def pidm = PersonUtility.getPerson("710000006").pidm
        def personAddress = newPersonAddress(pidm)
        shouldFail(InvalidDataAccessResourceUsageException)   {
            personAddress.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdatePersonAddressByRole() {
        def pidm = PersonUtility.getPerson("511000008").pidm
        def personAddress = PersonAddressByRoleView.findByPidm(pidm)

        // Update domain values
        personAddress.houseNumber = 5
        shouldFail(InvalidDataAccessResourceUsageException)   {
            personAddress.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeletePersonAddressByRole() {
        def pidm = PersonUtility.getPerson("511000008").pidm
        def personAddress = PersonAddressByRoleView.findByPidm(pidm)

        shouldFail(InvalidDataAccessResourceUsageException)   {
            personAddress.delete(failOnError: true, flush: true)
        }
    }


    @Test
    void testFetchByPidmAndRole() {
        def pidm = PersonUtility.getPerson("511000008").pidm
        def employeeAddresses = PersonAddressByRoleView.fetchAddressesByPidmAndRole([pidm:pidm,role:'EMPLOYEE'])
        assertTrue employeeAddresses.size() == 1

        employeeAddresses = PersonAddressByRoleView.fetchAddressesByPidmAndRoles([pidm:pidm,roles:['EMPLOYEE','ALUMNI']])
         assertTrue employeeAddresses.size() == 3
    }

	private def newPersonAddress(pidm) {
		def personAddress = new PersonAddressByRoleView(
			pidm: pidm
	    )
		return personAddress
	}


}
