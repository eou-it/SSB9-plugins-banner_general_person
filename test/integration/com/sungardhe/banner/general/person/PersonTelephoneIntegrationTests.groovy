
/*******************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

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
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import com.sungardhe.banner.general.system.TelephoneType
import com.sungardhe.banner.general.system.AddressType
import org.junit.Ignore


class PersonTelephoneIntegrationTests extends BaseIntegrationTestCase {

	/*PROTECTED REGION ID(persontelephone_domain_integration_test_data) ENABLED START*/
	//Test data for creating new domain instance
	//Valid test data (For success tests)
	def i_success_telephoneType
	def i_success_addressType

	def i_success_pidm = 999
	def i_success_sequenceNumber = 1
	def i_success_phoneArea = "TTTTT"
	def i_success_phoneNumber = "TTTTT"
	def i_success_phoneExtension = "TTTTT"
	def i_success_statusIndicator = "I"
	def i_success_addressSequenceNumber = null
	def i_success_primaryIndicator = "Y"
	def i_success_unlistIndicator = null
	def i_success_commentData = "TTTTT"
	def i_success_internationalAccess = "TTTTT"
	def i_success_countryPhone = "TTTT"
	//Invalid test data (For failure tests)
	def i_failure_telephoneType
	def i_failure_addressType

	def i_failure_pidm = 2
	def i_failure_sequenceNumber = 1
	def i_failure_phoneArea = "TTTTT"
	def i_failure_phoneNumber = "TTTTT"
	def i_failure_phoneExtension = "TTTTT"
	def i_failure_statusIndicator = "I"
	def i_failure_addressSequenceNumber = null
	def i_failure_primaryIndicator = "Y"
	def i_failure_unlistIndicator = "Y"
	def i_failure_commentData = "TTTTT"
	def i_failure_internationalAccess = "TTTTT"
	def i_failure_countryPhone = "WWWW"

	//Test data for creating updating domain instance
	//Valid test data (For success tests)
	def u_success_telephoneType
	def u_success_addressType

	def u_success_pidm = 999
	def u_success_sequenceNumber = 1
	def u_success_phoneArea = "WWWWW"
	def u_success_phoneNumber = "WWWWW"
	def u_success_phoneExtension = "WWWWW"
	def u_success_statusIndicator = "I"
	def u_success_addressSequenceNumber = null
	def u_success_primaryIndicator = "Y"
	def u_success_unlistIndicator = null
	def u_success_commentData = "WWWWW"
	def u_success_internationalAccess = "WWWWW"
	def u_success_countryPhone = "WWWW"

	//Valid test data (For failure tests)
	def u_failure_telephoneType
	def u_failure_addressType

	def u_failure_pidm = 999
	def u_failure_sequenceNumber = 1
	def u_failure_phoneArea = "TTTTT"
	def u_failure_phoneNumber = "TTTTT"
	def u_failure_phoneExtension = "TTTTT"
	def u_failure_statusIndicator = "Z"
	def u_failure_addressSequenceNumber = null
	def u_failure_primaryIndicator = "Z"
	def u_failure_unlistIndicator = "Z"
	def u_failure_commentData = "TTTTT"
	def u_failure_internationalAccess = "TTTTT"
	def u_failure_countryPhone = "WWWWWW"
	/*PROTECTED REGION END*/

	protected void setUp() {
		formContext = ['GEAPART'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references.
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(persontelephone_domain_integration_test_data_initialization) ENABLED START*/
		//Valid test data (For success tests)
    	i_success_telephoneType = TelephoneType.findWhere(code: "GR")
//    	i_success_addressType = AddressType.findWhere(code: "PR")

		//Invalid test data (For failure tests)
	    i_failure_telephoneType = TelephoneType.findWhere(code: "GR")
//	    i_failure_addressType = AddressType.findWhere(code: "PR")

		//Valid test data (For success tests)
	    u_success_telephoneType = TelephoneType.findWhere(code: "GR")
//	    u_success_addressType = AddressType.findWhere(code: "GV")

		//Valid test data (For failure tests)
    	u_failure_telephoneType = TelephoneType.findWhere(code: "GR")
//    	u_failure_addressType = AddressType.findWhere(code: "PR")

		//Test data for references for custom tests
		/*PROTECTED REGION END*/
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testCreateValidPersonTelephone() {
		def personTelephone = newValidForCreatePersonTelephone()
		save personTelephone
		//Test if the generated entity now has an id assigned
        assertNotNull personTelephone.id
	}

    @Ignore
	void testCreateInvalidPersonTelephone() {
		def personTelephone = newInvalidForCreatePersonTelephone()
        personTelephone.pidm = null
		shouldFail(ApplicationException) {
            personTelephone.save( failOnError: true, flush: true )
		}
	}

	void testUpdateValidPersonTelephone() {
		def personTelephone = newValidForCreatePersonTelephone()
		save personTelephone
        assertNotNull personTelephone.id
        assertEquals 0L, personTelephone.version
        assertEquals i_success_pidm, personTelephone.pidm
        assertEquals i_success_sequenceNumber, personTelephone.sequenceNumber
        assertEquals i_success_phoneArea, personTelephone.phoneArea
        assertEquals i_success_phoneNumber, personTelephone.phoneNumber
        assertEquals i_success_phoneExtension, personTelephone.phoneExtension
        assertEquals i_success_statusIndicator, personTelephone.statusIndicator
        assertEquals i_success_addressSequenceNumber, personTelephone.addressSequenceNumber
        assertEquals i_success_primaryIndicator, personTelephone.primaryIndicator
        assertEquals i_success_unlistIndicator, personTelephone.unlistIndicator
        assertEquals i_success_commentData, personTelephone.commentData
        assertEquals i_success_internationalAccess, personTelephone.internationalAccess
        assertEquals i_success_countryPhone, personTelephone.countryPhone

		//Update the entity
		personTelephone.phoneArea = u_success_phoneArea
		personTelephone.phoneNumber = u_success_phoneNumber
		personTelephone.phoneExtension = u_success_phoneExtension
		personTelephone.statusIndicator = u_success_statusIndicator
		personTelephone.addressSequenceNumber = u_success_addressSequenceNumber
		personTelephone.primaryIndicator = u_success_primaryIndicator
		personTelephone.unlistIndicator = u_success_unlistIndicator
		personTelephone.commentData = u_success_commentData
		personTelephone.internationalAccess = u_success_internationalAccess
		personTelephone.countryPhone = u_success_countryPhone
		personTelephone.addressType = u_success_addressType
        personTelephone.sequenceNumber = u_success_sequenceNumber
        save personTelephone
		//Asset for sucessful update
        personTelephone = PersonTelephone.get( personTelephone.id )
        assertEquals 1L, personTelephone?.version
        assertEquals u_success_phoneArea, personTelephone.phoneArea
        assertEquals u_success_phoneNumber, personTelephone.phoneNumber
        assertEquals u_success_phoneExtension, personTelephone.phoneExtension
        assertEquals u_success_statusIndicator, personTelephone.statusIndicator
        assertEquals u_success_addressSequenceNumber, personTelephone.addressSequenceNumber
        assertEquals u_success_primaryIndicator, personTelephone.primaryIndicator
        assertEquals u_success_unlistIndicator, personTelephone.unlistIndicator
        assertEquals u_success_commentData, personTelephone.commentData
        assertEquals u_success_internationalAccess, personTelephone.internationalAccess
        assertEquals u_success_countryPhone, personTelephone.countryPhone
		personTelephone.addressType = u_success_addressType
	}

    @Ignore
	void testUpdateInvalidPersonTelephone() {
		def personTelephone = newValidForCreatePersonTelephone()
		save personTelephone
        assertNotNull personTelephone.id
        assertEquals 0L, personTelephone.version
        assertEquals i_success_pidm, personTelephone.pidm
        assertEquals i_success_sequenceNumber, personTelephone.sequenceNumber
        assertEquals i_success_phoneArea, personTelephone.phoneArea
        assertEquals i_success_phoneNumber, personTelephone.phoneNumber
        assertEquals i_success_phoneExtension, personTelephone.phoneExtension
        assertEquals i_success_statusIndicator, personTelephone.statusIndicator
        assertEquals i_success_addressSequenceNumber, personTelephone.addressSequenceNumber
        assertEquals i_success_primaryIndicator, personTelephone.primaryIndicator
        assertEquals i_success_unlistIndicator, personTelephone.unlistIndicator
        assertEquals i_success_commentData, personTelephone.commentData
        assertEquals i_success_internationalAccess, personTelephone.internationalAccess
        assertEquals i_success_countryPhone, personTelephone.countryPhone

		//Update the entity with invalid values
		personTelephone.phoneArea = u_failure_phoneArea
		personTelephone.phoneNumber = u_failure_phoneNumber
		personTelephone.phoneExtension = u_failure_phoneExtension
		personTelephone.statusIndicator = u_failure_statusIndicator
		personTelephone.addressSequenceNumber = u_failure_addressSequenceNumber
		personTelephone.primaryIndicator = u_failure_primaryIndicator
		personTelephone.unlistIndicator = u_failure_unlistIndicator
		personTelephone.commentData = u_failure_commentData
		personTelephone.internationalAccess = u_failure_internationalAccess
		personTelephone.countryPhone = u_failure_countryPhone
		personTelephone.addressType = u_failure_addressType
        personTelephone.pidm = null
		shouldFail(ApplicationException) {
            personTelephone.save( failOnError: true, flush: true )
		}
	}

    void testOptimisticLock() {
		def personTelephone = newValidForCreatePersonTelephone()
		save personTelephone

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update SV_SPRTELE set SPRTELE_VERSION = 999 where SPRTELE_SURROGATE_ID = ?", [ personTelephone.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		personTelephone.phoneArea = u_success_phoneArea
		personTelephone.phoneNumber = u_success_phoneNumber
		personTelephone.phoneExtension = u_success_phoneExtension
		personTelephone.statusIndicator = u_success_statusIndicator
		personTelephone.addressSequenceNumber = u_success_addressSequenceNumber
		personTelephone.primaryIndicator = u_success_primaryIndicator
		personTelephone.unlistIndicator = u_success_unlistIndicator
		personTelephone.commentData = u_success_commentData
		personTelephone.internationalAccess = u_success_internationalAccess
		personTelephone.countryPhone = u_success_countryPhone
        shouldFail( HibernateOptimisticLockingFailureException ) {
            personTelephone.save( failOnError: true, flush: true )
        }
    }

	void testDeletePersonTelephone() {
		def personTelephone = newValidForCreatePersonTelephone()
		save personTelephone
		def id = personTelephone.id
		assertNotNull id
		personTelephone.delete()
		assertNull PersonTelephone.get( id )
	}

    void testValidation() {
       def personTelephone = newInvalidForCreatePersonTelephone()
       assertTrue "PersonTelephone could not be validated as expected due to ${personTelephone.errors}", personTelephone.validate()
    }

    void testNullValidationFailure() {
        def personTelephone = new PersonTelephone()
        assertFalse "PersonTelephone should have failed validation", personTelephone.validate()
        assertErrorsFor personTelephone, 'nullable',
                                               [
                                                 'pidm',
                                                 'sequenceNumber',
                                                 'telephoneType'
                                               ]
        assertNoErrorsFor personTelephone,
        									   [
             									 'phoneArea',
             									 'phoneNumber',
             									 'phoneExtension',
             									 'statusIndicator',
             									 'addressSequenceNumber',
             									 'primaryIndicator',
             									 'unlistIndicator',
             									 'commentData',
             									 'internationalAccess',
             									 'countryPhone',
                                                 'addressType'
											   ]
    }

    void testMaxSizeValidationFailures() {
        def personTelephone = new PersonTelephone(
        phoneArea:'XXXXXXXX',
        phoneNumber:'XXXXXXXXXXXXXX',
        phoneExtension:'XXXXXXXXXXXX',
        statusIndicator:'XXX',
        primaryIndicator:'XXX',
        unlistIndicator:'XXX',
        commentData:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
        internationalAccess:'XXXXXXXXXXXXXXXXXX',
        countryPhone:'XXXXXX' )
		assertFalse "PersonTelephone should have failed validation", personTelephone.validate()
		assertErrorsFor personTelephone, 'maxSize', [ 'phoneArea', 'phoneNumber', 'phoneExtension', 'statusIndicator', 'primaryIndicator', 'unlistIndicator', 'commentData', 'internationalAccess', 'countryPhone' ]
    }

	void testValidationMessages() {
	    def personTelephone = newInvalidForCreatePersonTelephone()
	    personTelephone.pidm = null
	    assertFalse personTelephone.validate()
	    assertLocalizedError personTelephone, 'nullable', /.*Field.*pidm.*of class.*PersonTelephone.*cannot be null.*/, 'pidm'
	    personTelephone.sequenceNumber = null
	    assertFalse personTelephone.validate()
	    assertLocalizedError personTelephone, 'nullable', /.*Field.*sequenceNumber.*of class.*PersonTelephone.*cannot be null.*/, 'sequenceNumber'
	    personTelephone.telephoneType = null
	    assertFalse personTelephone.validate()
	    assertLocalizedError personTelephone, 'nullable', /.*Field.*telephoneType.*of class.*PersonTelephone.*cannot be null.*/, 'telephoneType'
	}


	private def newValidForCreatePersonTelephone() {
		def personTelephone = new PersonTelephone(
			pidm: i_success_pidm,
			sequenceNumber: i_success_sequenceNumber,
			phoneArea: i_success_phoneArea,
			phoneNumber: i_success_phoneNumber,
			phoneExtension: i_success_phoneExtension,
			statusIndicator: i_success_statusIndicator,
			addressSequenceNumber: i_success_addressSequenceNumber,
			primaryIndicator: i_success_primaryIndicator,
			unlistIndicator: i_success_unlistIndicator,
			commentData: i_success_commentData,
			internationalAccess: i_success_internationalAccess,
			countryPhone: i_success_countryPhone,
			telephoneType: i_success_telephoneType,
			addressType: i_success_addressType,
        	lastModified: new Date(),
			lastModifiedBy: "test",
			dataOrigin: "Banner"
	    )
		return personTelephone
	}

	private def newInvalidForCreatePersonTelephone() {
		def personTelephone = new PersonTelephone(
			pidm: i_failure_pidm,
			sequenceNumber: i_failure_sequenceNumber,
			phoneArea: i_failure_phoneArea,
			phoneNumber: i_failure_phoneNumber,
			phoneExtension: i_failure_phoneExtension,
			statusIndicator: i_failure_statusIndicator,
			addressSequenceNumber: i_failure_addressSequenceNumber,
			primaryIndicator: i_failure_primaryIndicator,
			unlistIndicator: i_failure_unlistIndicator,
			commentData: i_failure_commentData,
			internationalAccess: i_failure_internationalAccess,
			countryPhone: i_failure_countryPhone,
			telephoneType: i_failure_telephoneType,
			addressType: i_failure_addressType,
        	lastModified: new Date(),
			lastModifiedBy: "test",
			dataOrigin: "Banner"
		)
		return personTelephone
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(persontelephone_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
