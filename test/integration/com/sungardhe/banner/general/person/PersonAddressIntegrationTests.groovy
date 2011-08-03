/*******************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *******************************************************************************/
/**
 Banner Automator Version: 1.21
 Generated: Thu Jun 16 04:44:46 EDT 2011
 */
package com.sungardhe.banner.general.person

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.exceptions.ApplicationException
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import com.sungardhe.banner.general.system.AddressType
import com.sungardhe.banner.general.system.State
import com.sungardhe.banner.general.system.County
import com.sungardhe.banner.general.system.Nation
import com.sungardhe.banner.general.system.AddressSource
import grails.validation.ValidationException
import groovy.sql.Sql
import org.junit.Ignore


class PersonAddressIntegrationTests extends BaseIntegrationTestCase {

	/*PROTECTED REGION ID(personaddress_domain_integration_test_data) ENABLED START*/
	//Test data for creating new domain instance
	//Valid test data (For success tests)
	def i_success_addressType
	def i_success_state
	def i_success_county
	def i_success_nation
	def i_success_addressSource

	def i_success_pidm = 500030
	def i_success_sequenceNumber = 0
	def i_success_fromDate = new Date()
	def i_success_toDate = new Date()
	def i_success_streetLine1 = "i_success_streetLine1"
	def i_success_streetLine2 = "i_success_streetLine2"
	def i_success_streetLine3 = "i_success_streetLine3"
	def i_success_city = "TTTTT"
	def i_success_zip = "TTTTT"
	def i_success_phoneArea = "TTTTT"
	def i_success_phoneNumber = "TTTTT"
	def i_success_phoneExtension = "TTTTT"
	def i_success_statusIndicator = "I"
	def i_success_userData = "TTTTT"
	def i_success_deliveryPoint = 1
	def i_success_correctionDigit = 1
	def i_success_carrierRoute = "TTTT"
	def i_success_goodsAndServiceTaxTaxId = "TTTTT"
	def i_success_reviewedIndicator = "Y"
	def i_success_reviewedUser = "TTTTT"
	def i_success_countryPhone = "TTTT"
	def i_success_houseNumber = "TTTTT"
	def i_success_streetLine4 = "TTTTT"
	//Invalid test data (For failure tests)
	def i_failure_addressType
	def i_failure_state
	def i_failure_county
	def i_failure_nation
	def i_failure_addressSource

	def i_failure_pidm = 500029
	def i_failure_sequenceNumber = null
	def i_failure_fromDate = new Date()
	def i_failure_toDate = new Date()
	def i_failure_streetLine1 = "i_failure_streetLine1"
	def i_failure_streetLine2 = "i_failure_streetLine2"
	def i_failure_streetLine3 = "i_failure_streetLine3"
	def i_failure_city = "TTTTT"
	def i_failure_zip = "TTTTT"
	def i_failure_phoneArea = "TTTTT"
	def i_failure_phoneNumber = "TTTTT"
	def i_failure_phoneExtension = "TTTTT"
	def i_failure_statusIndicator = "I"
	def i_failure_userData = "TTTTT"
	def i_failure_deliveryPoint = 1
	def i_failure_correctionDigit = 1
	def i_failure_carrierRoute = "TTTT"
	def i_failure_goodsAndServiceTaxTaxId = "TTTTT"
	def i_failure_reviewedIndicator = "Y"
	def i_failure_reviewedUser = "TTTTT"
	def i_failure_countryPhone = "TTTT"
	def i_failure_houseNumber = "TTTTT"
	def i_failure_streetLine4 = "TTTTT"

	//Test data for creating updating domain instance
	//Valid test data (For success tests)
	def u_success_addressType
	def u_success_state
	def u_success_county
	def u_success_nation
	def u_success_addressSource

	def u_success_pidm = 500030
	def u_success_sequenceNumber = null
	def u_success_fromDate = new Date()
	def u_success_toDate = new Date()
	def u_success_streetLine1 = "u_success_streetLine1"
	def u_success_streetLine2 = "u_success_streetLine2"
	def u_success_streetLine3 = "u_success_streetLine3"
	def u_success_city = "TTTTT"
	def u_success_zip = "TTTTT"
	def u_success_phoneArea = "TTTTT"
	def u_success_phoneNumber = "TTTTT"
	def u_success_phoneExtension = "TTTTT"
	def u_success_statusIndicator = "I"
	def u_success_userData = "TTTTT"
	def u_success_deliveryPoint = 1
	def u_success_correctionDigit = 1
	def u_success_carrierRoute = "TTTT"
	def u_success_goodsAndServiceTaxTaxId = "TTTTT"
	def u_success_reviewedIndicator = "Y"
	def u_success_reviewedUser = "TTTTT"
	def u_success_countryPhone = "TTTT"
	def u_success_houseNumber = "TTTTT"
	def u_success_streetLine4 = "TTTTT"
	//Valid test data (For failure tests)
	def u_failure_addressType
	def u_failure_state
	def u_failure_county
	def u_failure_nation
	def u_failure_addressSource

	def u_failure_pidm = 500029
	def u_failure_sequenceNumber = null
	def u_failure_fromDate = new Date()
	def u_failure_toDate = new Date()
	def u_failure_streetLine1 = "u_failure_streetLine1"
	def u_failure_streetLine2 = "u_failure_streetLine2"
	def u_failure_streetLine3 = "u_failure_streetLine3"
	def u_failure_city = "TTTTT"
	def u_failure_zip = "TTTTT"
	def u_failure_phoneArea = "TTTTT"
	def u_failure_phoneNumber = "TTTTT"
	def u_failure_phoneExtension = "TTTTT"
	def u_failure_statusIndicator = "I"
	def u_failure_userData = "TTTTT"
	def u_failure_deliveryPoint = 1
	def u_failure_correctionDigit = 1
	def u_failure_carrierRoute = "TTTT"
	def u_failure_goodsAndServiceTaxTaxId = "TTTTT"
	def u_failure_reviewedIndicator = "Y"
	def u_failure_reviewedUser = "TTTTT"
	def u_failure_countryPhone = "TTTT"
	def u_failure_houseNumber = "TTTTT"
	def u_failure_streetLine4 = "TTTTT"
	/*PROTECTED REGION END*/

	protected void setUp() {
		formContext = ['GEAPART'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references.
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(personaddress_domain_integration_test_data_initialization) ENABLED START*/
		//Valid test data (For success tests)
    	i_success_addressType = AddressType.findWhere(code:"PO") //TODO: fill in the query condition
    	i_success_state = State.findWhere(code:"MER") //TODO: fill in the query condition
    	i_success_county = County.findWhere(code:"044") //TODO: fill in the query condition
    	i_success_nation = Nation.findWhere(code:"157") //TODO: fill in the query condition
    	i_success_addressSource = AddressSource.findWhere(code:"BRD") //TODO: fill in the query condition

		//Invalid test data (For failure tests)
	    i_failure_addressType = AddressType.findWhere(code:"GR") //TODO: fill in the query condition
	    i_failure_state = State.findWhere(code:"GLO") //TODO: fill in the query condition
	    i_failure_county = County.findWhere(code:"045") //TODO: fill in the query condition
	    i_failure_nation = Nation.findWhere(code:"40") //TODO: fill in the query condition
	    i_failure_addressSource = AddressSource.findWhere(code:"BRD") //TODO: fill in the query condition

		//Valid test data (For success tests)
	    u_success_addressType = AddressType.findWhere(code:"PO") //TODO: fill in the query condition
	    u_success_state = State.findWhere(code:"MER") //TODO: fill in the query condition
	    u_success_county = County.findWhere(code:"044") //TODO: fill in the query condition
	    u_success_nation = Nation.findWhere(code:"157") //TODO: fill in the query condition
	    u_success_addressSource = AddressSource.findWhere(code:"BRD") //TODO: fill in the query condition

		//Valid test data (For failure tests)
    	u_failure_addressType = AddressType.findWhere(code:"GR") //TODO: fill in the query condition
    	u_failure_state = State.findWhere(code:"GLO") //TODO: fill in the query condition
    	u_failure_county = County.findWhere(code:"045") //TODO: fill in the query condition
    	u_failure_nation = Nation.findWhere(code:"40") //TODO: fill in the query condition
    	u_failure_addressSource = AddressSource.findWhere(code:"BRD") //TODO: fill in the query condition

		//Test data for references for custom tests
		/*PROTECTED REGION END*/
	}

	protected void tearDown() {
		super.tearDown()
	}


    //TODO: need to fix and enable this test - Amrit
    @Ignore
	void testCreateValidPersonAddress() {
		def personAddress = newValidForCreatePersonAddress()
		save personAddress
		//Test if the generated entity now has an id assigned
        assertNotNull personAddress.id
	}

	void testCreateInvalidPersonAddress() {
		def personAddress = newInvalidForCreatePersonAddress()
		shouldFail(ValidationException) {
            personAddress.save( failOnError: true, flush: true )
		}
	}


    //TODO: need to fix and enable this test - Amrit
    @Ignore
	void testUpdateValidPersonAddress() {
		def personAddress = newValidForCreatePersonAddress()
		save personAddress
        assertNotNull personAddress.id
        assertEquals 0L, personAddress.version
        assertEquals i_success_pidm, personAddress.pidm
        //assertEquals i_success_sequenceNumber, personAddress.sequenceNumber
        assertEquals i_success_fromDate, personAddress.fromDate
        assertEquals i_success_toDate, personAddress.toDate
        assertEquals i_success_streetLine1, personAddress.streetLine1
        assertEquals i_success_streetLine2, personAddress.streetLine2
        assertEquals i_success_streetLine3, personAddress.streetLine3
        assertEquals i_success_city, personAddress.city
        assertEquals i_success_zip, personAddress.zip
        assertEquals i_success_phoneArea, personAddress.phoneArea
        assertEquals i_success_phoneNumber, personAddress.phoneNumber
        assertEquals i_success_phoneExtension, personAddress.phoneExtension
        assertEquals i_success_statusIndicator, personAddress.statusIndicator
        assertEquals i_success_userData, personAddress.userData
        assertEquals i_success_deliveryPoint, personAddress.deliveryPoint
        assertEquals i_success_correctionDigit, personAddress.correctionDigit
        assertEquals i_success_carrierRoute, personAddress.carrierRoute
        assertEquals i_success_goodsAndServiceTaxTaxId, personAddress.goodsAndServiceTaxTaxId
        assertEquals i_success_reviewedIndicator, personAddress.reviewedIndicator
        assertEquals i_success_reviewedUser, personAddress.reviewedUser
        assertEquals i_success_countryPhone, personAddress.countryPhone
        assertEquals i_success_houseNumber, personAddress.houseNumber
        assertEquals i_success_streetLine4, personAddress.streetLine4

		//Update the entity
		personAddress.pidm = u_success_pidm
		//personAddress.sequenceNumber = u_success_sequenceNumber
		personAddress.fromDate = u_success_fromDate
		personAddress.toDate = u_success_toDate
		personAddress.streetLine1 = u_success_streetLine1
		personAddress.streetLine2 = u_success_streetLine2
		personAddress.streetLine3 = u_success_streetLine3
		personAddress.city = u_success_city
		personAddress.zip = u_success_zip
		personAddress.phoneArea = u_success_phoneArea
		personAddress.phoneNumber = u_success_phoneNumber
		personAddress.phoneExtension = u_success_phoneExtension
		personAddress.statusIndicator = u_success_statusIndicator
		personAddress.userData = u_success_userData
		personAddress.deliveryPoint = u_success_deliveryPoint
		personAddress.correctionDigit = u_success_correctionDigit
		personAddress.carrierRoute = u_success_carrierRoute
		personAddress.goodsAndServiceTaxTaxId = u_success_goodsAndServiceTaxTaxId
		personAddress.reviewedIndicator = u_success_reviewedIndicator
		personAddress.reviewedUser = u_success_reviewedUser
		personAddress.countryPhone = u_success_countryPhone
		personAddress.houseNumber = u_success_houseNumber
		personAddress.streetLine4 = u_success_streetLine4


		personAddress.addressType = u_success_addressType

		personAddress.state = u_success_state

		personAddress.county = u_success_county

		personAddress.nation = u_success_nation

		personAddress.addressSource = u_success_addressSource
        save personAddress
		//Asset for sucessful update
        personAddress = PersonAddress.get( personAddress.id )
        assertEquals 1L, personAddress?.version
        assertEquals u_success_pidm, personAddress.pidm
        //assertEquals u_success_sequenceNumber, personAddress.sequenceNumber
        assertEquals u_success_fromDate, personAddress.fromDate
        assertEquals u_success_toDate, personAddress.toDate
        assertEquals u_success_streetLine1, personAddress.streetLine1
        assertEquals u_success_streetLine2, personAddress.streetLine2
        assertEquals u_success_streetLine3, personAddress.streetLine3
        assertEquals u_success_city, personAddress.city
        assertEquals u_success_zip, personAddress.zip
        assertEquals u_success_phoneArea, personAddress.phoneArea
        assertEquals u_success_phoneNumber, personAddress.phoneNumber
        assertEquals u_success_phoneExtension, personAddress.phoneExtension
        assertEquals u_success_statusIndicator, personAddress.statusIndicator
        assertEquals u_success_userData, personAddress.userData
        assertEquals u_success_deliveryPoint, personAddress.deliveryPoint
        assertEquals u_success_correctionDigit, personAddress.correctionDigit
        assertEquals u_success_carrierRoute, personAddress.carrierRoute
        assertEquals u_success_goodsAndServiceTaxTaxId, personAddress.goodsAndServiceTaxTaxId
        assertEquals u_success_reviewedIndicator, personAddress.reviewedIndicator
        assertEquals u_success_reviewedUser, personAddress.reviewedUser
        assertEquals u_success_countryPhone, personAddress.countryPhone
        assertEquals u_success_houseNumber, personAddress.houseNumber
        assertEquals u_success_streetLine4, personAddress.streetLine4


		personAddress.addressType = u_success_addressType

		personAddress.state = u_success_state

		personAddress.county = u_success_county

		personAddress.nation = u_success_nation

		personAddress.addressSource = u_success_addressSource
	}


    //TODO: need to fix and enable this test - Amrit
    @Ignore
	void testUpdateInvalidPersonAddress() {
		def personAddress = newValidForCreatePersonAddress()
		save personAddress
        assertNotNull personAddress.id
        assertEquals 0L, personAddress.version
        assertEquals i_success_pidm, personAddress.pidm
        //assertEquals i_success_sequenceNumber, personAddress.sequenceNumber
        assertEquals i_success_fromDate, personAddress.fromDate
        assertEquals i_success_toDate, personAddress.toDate
        assertEquals i_success_streetLine1, personAddress.streetLine1
        assertEquals i_success_streetLine2, personAddress.streetLine2
        assertEquals i_success_streetLine3, personAddress.streetLine3
        assertEquals i_success_city, personAddress.city
        assertEquals i_success_zip, personAddress.zip
        assertEquals i_success_phoneArea, personAddress.phoneArea
        assertEquals i_success_phoneNumber, personAddress.phoneNumber
        assertEquals i_success_phoneExtension, personAddress.phoneExtension
        assertEquals i_success_statusIndicator, personAddress.statusIndicator
        assertEquals i_success_userData, personAddress.userData
        assertEquals i_success_deliveryPoint, personAddress.deliveryPoint
        assertEquals i_success_correctionDigit, personAddress.correctionDigit
        assertEquals i_success_carrierRoute, personAddress.carrierRoute
        assertEquals i_success_goodsAndServiceTaxTaxId, personAddress.goodsAndServiceTaxTaxId
        assertEquals i_success_reviewedIndicator, personAddress.reviewedIndicator
        assertEquals i_success_reviewedUser, personAddress.reviewedUser
        assertEquals i_success_countryPhone, personAddress.countryPhone
        assertEquals i_success_houseNumber, personAddress.houseNumber
        assertEquals i_success_streetLine4, personAddress.streetLine4

		//Update the entity with invalid values
		personAddress.pidm = u_failure_pidm
		//personAddress.sequenceNumber = u_failure_sequenceNumber
		personAddress.fromDate = u_failure_fromDate
		personAddress.toDate = u_failure_toDate
		personAddress.streetLine1 = u_failure_streetLine1
		personAddress.streetLine2 = u_failure_streetLine2
		personAddress.streetLine3 = u_failure_streetLine3
		personAddress.city = u_failure_city
		personAddress.zip = u_failure_zip
		personAddress.phoneArea = u_failure_phoneArea
		personAddress.phoneNumber = u_failure_phoneNumber
		personAddress.phoneExtension = u_failure_phoneExtension
		personAddress.statusIndicator = u_failure_statusIndicator
		personAddress.userData = u_failure_userData
		personAddress.deliveryPoint = u_failure_deliveryPoint
		personAddress.correctionDigit = u_failure_correctionDigit
		personAddress.carrierRoute = u_failure_carrierRoute
		personAddress.goodsAndServiceTaxTaxId = u_failure_goodsAndServiceTaxTaxId
		personAddress.reviewedIndicator = u_failure_reviewedIndicator
		personAddress.reviewedUser = u_failure_reviewedUser
		personAddress.countryPhone = u_failure_countryPhone
		personAddress.houseNumber = u_failure_houseNumber
		personAddress.streetLine4 = u_failure_streetLine4


		personAddress.addressType = u_failure_addressType

		personAddress.state = u_failure_state

		personAddress.county = u_failure_county

		personAddress.nation = u_failure_nation

		personAddress.addressSource = u_failure_addressSource
		shouldFail(ApplicationException) {
            personAddress.save( failOnError: true, flush: true )
		}
	}

    //TODO: need to fix and enable this test - Amrit
    @Ignore
    void testOptimisticLock() {
        def sql1 = new Sql(sessionFactory.getCurrentSession().connection())
        sql1.executeUpdate("update gubinst set gubinst_finance_installed = 'Y'")

		def personAddress = newValidForCreatePersonAddress()
		personAddress.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update SV_SPRADDR set SPRADDR_VERSION = 999 where SPRADDR_SURROGATE_ID = ?", [ personAddress.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		personAddress.pidm = u_success_pidm
		//personAddress.sequenceNumber = u_success_sequenceNumber
		personAddress.fromDate = u_success_fromDate
		personAddress.toDate = u_success_toDate
		personAddress.streetLine1 = u_success_streetLine1
		personAddress.streetLine2 = u_success_streetLine2
		personAddress.streetLine3 = u_success_streetLine3
		personAddress.city = u_success_city
		personAddress.zip = u_success_zip
		personAddress.phoneArea = u_success_phoneArea
		personAddress.phoneNumber = u_success_phoneNumber
		personAddress.phoneExtension = u_success_phoneExtension
		personAddress.statusIndicator = u_success_statusIndicator
		personAddress.userData = u_success_userData
		personAddress.deliveryPoint = u_success_deliveryPoint
		personAddress.correctionDigit = u_success_correctionDigit
		personAddress.carrierRoute = u_success_carrierRoute
		personAddress.goodsAndServiceTaxTaxId = u_success_goodsAndServiceTaxTaxId
		personAddress.reviewedIndicator = u_success_reviewedIndicator
		personAddress.reviewedUser = u_success_reviewedUser
		personAddress.countryPhone = u_success_countryPhone
		personAddress.houseNumber = u_success_houseNumber
		personAddress.streetLine4 = u_success_streetLine4
        shouldFail( HibernateOptimisticLockingFailureException ) {
            personAddress.save( failOnError: true, flush: true )
        }
    }


    //TODO: need to fix and enable this test - Amrit
    @Ignore
	void testDeletePersonAddress() {
		def personAddress = newValidForCreatePersonAddress()
		save personAddress
		def id = personAddress.id
		assertNotNull id
		personAddress.delete()
		assertNull PersonAddress.get( id )
	}

    //TODO: need to fix and enable this test - Amrit
    @Ignore
    void testValidation() {
       def personAddress = newValidForCreatePersonAddress()
       assertTrue "PersonAddress could not be validated as expected due to ${personAddress.errors}", personAddress.validate()
    }


    //TODO: need to fix and enable this test - Amrit
    @Ignore
    void testNullValidationFailure() {
        def personAddress = new PersonAddress()
        assertFalse "PersonAddress should have failed validation", personAddress.validate()
        assertErrorsFor personAddress, 'nullable',
                                               [
                                                 'pidm',
                                                 'sequenceNumber',
                                                 'city',
                                                 'addressType'
                                               ]
        assertNoErrorsFor personAddress,
        									   [
             									 'fromDate',
             									 'toDate',
             									 'streetLine1',
             									 'streetLine2',
             									 'streetLine3',
             									 'zip',
             									 'phoneArea',
             									 'phoneNumber',
             									 'phoneExtension',
             									 'statusIndicator',
             									 'userData',
             									 'deliveryPoint',
             									 'correctionDigit',
             									 'carrierRoute',
             									 'goodsAndServiceTaxTaxId',
             									 'reviewedIndicator',
             									 'reviewedUser',
             									 'countryPhone',
             									 'houseNumber',
             									 'streetLine4',
                                                 'state',
                                                 'county',
                                                 'nation',
                                                 'addressSource'
											   ]
    }


    //TODO: need to fix and enable this test - Amrit
    @Ignore
    void testMaxSizeValidationFailures() {
        def personAddress = new PersonAddress(
        streetLine1:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
        streetLine2:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
        streetLine3:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
        zip:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
        phoneArea:'XXXXXXXX',
        phoneNumber:'XXXXXXXXXXXXXX',
        phoneExtension:'XXXXXXXXXXXX',
        statusIndicator:'XXX',
        userData:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
        carrierRoute:'XXXXXX',
        goodsAndServiceTaxTaxId:'XXXXXXXXXXXXXXXXX',
        reviewedIndicator:'XXX',
        reviewedUser:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
        countryPhone:'XXXXXX',
        houseNumber:'XXXXXXXXXXXX',
        streetLine4:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' )
		assertFalse "PersonAddress should have failed validation", personAddress.validate()
		assertErrorsFor personAddress, 'maxSize', [ 'streetLine1', 'streetLine2', 'streetLine3', 'zip', 'phoneArea', 'phoneNumber', 'phoneExtension', 'statusIndicator', 'userData', 'carrierRoute', 'goodsAndServiceTaxTaxId', 'reviewedIndicator', 'reviewedUser', 'countryPhone', 'houseNumber', 'streetLine4' ]
    }

	void testValidationMessages() {
	    def personAddress = newInvalidForCreatePersonAddress()
	    personAddress.pidm = null
	    assertFalse personAddress.validate()
	    assertLocalizedError personAddress, 'nullable', /.*Field.*pidm.*of class.*PersonAddress.*cannot be null.*/, 'pidm'
	    personAddress.sequenceNumber = null
	    assertFalse personAddress.validate()
	    assertLocalizedError personAddress, 'nullable', /.*Field.*sequenceNumber.*of class.*PersonAddress.*cannot be null.*/, 'sequenceNumber'
	    personAddress.city = null
	    assertFalse personAddress.validate()
	    assertLocalizedError personAddress, 'nullable', /.*Field.*city.*of class.*PersonAddress.*cannot be null.*/, 'city'
	    personAddress.addressType = null
	    assertFalse personAddress.validate()
	    assertLocalizedError personAddress, 'nullable', /.*Field.*addressType.*of class.*PersonAddress.*cannot be null.*/, 'addressType'
	}


	private def newValidForCreatePersonAddress() {
		def personAddress = new PersonAddress(
			pidm: i_success_pidm,
			sequenceNumber: i_success_sequenceNumber,
			fromDate: i_success_fromDate,
			toDate: i_success_toDate,
			streetLine1: i_success_streetLine1,
			streetLine2: i_success_streetLine2,
			streetLine3: i_success_streetLine3,
			city: i_success_city,
			zip: i_success_zip,
			phoneArea: i_success_phoneArea,
			phoneNumber: i_success_phoneNumber,
			phoneExtension: i_success_phoneExtension,
			statusIndicator: i_success_statusIndicator,
			userData: i_success_userData,
			deliveryPoint: i_success_deliveryPoint,
			correctionDigit: i_success_correctionDigit,
			carrierRoute: i_success_carrierRoute,
			goodsAndServiceTaxTaxId: i_success_goodsAndServiceTaxTaxId,
			reviewedIndicator: i_success_reviewedIndicator,
			reviewedUser: i_success_reviewedUser,
			countryPhone: i_success_countryPhone,
			houseNumber: i_success_houseNumber,
			streetLine4: i_success_streetLine4,
			addressType: i_success_addressType,
			state: i_success_state,
			county: i_success_county,
			nation: i_success_nation,
			addressSource: i_success_addressSource,
        	lastModified: new Date(),
			lastModifiedBy: "test",
			dataOrigin: "Banner"
	    )
		return personAddress
	}

	private def newInvalidForCreatePersonAddress() {
		def personAddress = new PersonAddress(
			pidm: i_failure_pidm,
			sequenceNumber: i_failure_sequenceNumber,
			fromDate: i_failure_fromDate,
			toDate: i_failure_toDate,
			streetLine1: i_failure_streetLine1,
			streetLine2: i_failure_streetLine2,
			streetLine3: i_failure_streetLine3,
//			city: i_failure_city,
            city: null,
			zip: i_failure_zip,
			phoneArea: i_failure_phoneArea,
			phoneNumber: i_failure_phoneNumber,
			phoneExtension: i_failure_phoneExtension,
			statusIndicator: i_failure_statusIndicator,
			userData: i_failure_userData,
			deliveryPoint: i_failure_deliveryPoint,
			correctionDigit: i_failure_correctionDigit,
			carrierRoute: i_failure_carrierRoute,
			goodsAndServiceTaxTaxId: i_failure_goodsAndServiceTaxTaxId,
			reviewedIndicator: i_failure_reviewedIndicator,
			reviewedUser: i_failure_reviewedUser,
			countryPhone: i_failure_countryPhone,
			houseNumber: i_failure_houseNumber,
			streetLine4: i_failure_streetLine4,
			addressType: i_failure_addressType,
			state: i_failure_state,
			county: i_failure_county,
			nation: i_failure_nation,
			addressSource: i_failure_addressSource,
        	lastModified: new Date(),
			lastModifiedBy: "test",
			dataOrigin: "Banner"
		)
		return personAddress
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personaddress_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
