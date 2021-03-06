/*********************************************************************************
  Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import static groovy.test.GroovyAssert.*
import net.hedtech.banner.general.person.view.PersonAddressByRoleView
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.dao.InvalidDataAccessResourceUsageException
import org.junit.Before
import org.junit.Test
import org.junit.After

@Integration
@Rollback
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
        def pidm = PersonUtility.getPerson("GDP000001").pidm
        def personAddress = newPersonAddress(pidm)
        shouldFail(InvalidDataAccessResourceUsageException)   {
            personAddress.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdatePersonAddressByRole() {
        def pidm = PersonUtility.getPerson("GDP000003").pidm
        def personAddress = PersonAddressByRoleView.findByPidm(pidm)

        // Update domain values
        personAddress.houseNumber = 5
        personAddress.nationCode = "test"
        personAddress.streetLine1 = "test"
        personAddress.streetLine2 = "test"
        personAddress.streetLine3 = "test"
        personAddress.streetLine4 = "test"
        personAddress.nation = "test"

        shouldFail(InvalidDataAccessResourceUsageException)   {
            personAddress.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeletePersonAddressByRole() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def personAddress = PersonAddressByRoleView.findByPidm(pidm)

        shouldFail(InvalidDataAccessResourceUsageException)   {
            personAddress.delete(failOnError: true, flush: true)
        }
    }


    @Test
    void testFetchByPidmAndRole() {
        def pidm = PersonUtility.getPerson("GDP000003").pidm
        def employeeAddresses = PersonAddressByRoleView.fetchAddressesByPidmAndRole([pidm:pidm,role:'EMPLOYEE'])
        assertTrue employeeAddresses.size() >= 1

        employeeAddresses = PersonAddressByRoleView.fetchAddressesByPidmAndRoles([pidm:pidm,roles:['EMPLOYEE','STUDENT']])
         assertTrue employeeAddresses.size() >= 1
    }

    private def newPersonAddress(pidm) {
        def personAddress = new PersonAddressByRoleView(
                pidm: pidm,
                streetLine1: "test",
                streetLine2: "test",
                streetLine3: "test",
                streetLine4: "test",
                toDate: new Date(),
                fromDate: new Date(),
                priviledgeIndicator: "test",
                nationCode: "test",
                houseNumber: "test",
                city: "test",
                zip: "test",
                userRole: "test",
                stateCode: "test",
                addressType: "test",
                county: "test",
                nation: "test",
                state: "test",
                priviledgeSequence: 5,
                addressTypeDescription: "test",
                countyCode: "test"
        )
		return personAddress
	}


}
