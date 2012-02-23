
/*******************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.

 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 *******************************************************************************/

package com.sungardhe.banner.general.person

import com.sungardhe.banner.exceptions.ApplicationException
import com.sungardhe.banner.testing.BaseIntegrationTestCase
import java.text.SimpleDateFormat
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import com.sungardhe.banner.general.system.Term


class PersonStudentAlternatePinIntegrationTests extends BaseIntegrationTestCase {
	
	/*PROTECTED REGION ID(personstudentalternatepin_domain_integration_test_data) ENABLED START*/
	//Test data for creating new domain instance
	//Valid test data (For success tests)
	def i_success_term
    def i_success_pidm
	def i_success_processName
	def i_success_pin

	//Invalid test data (For failure tests)
	def i_failure_term
	def i_failure_pidm
	def i_failure_processName
	def i_failure_pin
	
	//Test data for creating updating domain instance
	//Valid test data (For success tests)
	def u_success_term
	def u_success_pidm
	def u_success_processName
	def u_success_pin

	//Valid test data (For failure tests)
	def u_failure_term
	def u_failure_pidm
	def u_failure_processName
	def u_failure_pin
	/*PROTECTED REGION END*/
	
	protected void setUp() {
		formContext = ['SPAAPIN'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references. 
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(personstudentalternatepin_domain_integration_test_data_initialization) ENABLED START*/
		//Valid test data (For success tests)	
        i_success_term = Term.findByCode('201410')
        i_success_pidm = PersonIdentificationName.findByBannerId('GINNY').pidm
        i_success_processName = "SFAREGS"
        i_success_pin = "123456"

		//Valid test data (For success tests)
        u_success_term = Term.findByCode('201410')
        u_success_pin = "999999"

        u_failure_pidm = PersonIdentificationName.findByBannerId('OLIVER').pidm
        u_failure_processName = "REGISTRATION"

		/*PROTECTED REGION END*/
	}
	
	protected void tearDown() {
		super.tearDown()
	}

	void testCreateValidPersonStudentAlternatePin() {
		def personStudentAlternatePin = newValidForCreatePersonStudentAlternatePin()
		personStudentAlternatePin.save( failOnError: true, flush: true )
		//Test if the generated entity now has an id assigned		
        assertNotNull personStudentAlternatePin.id
	}

	void testCreateValidPersonStudentAlternatePinDuplicateKey() {
		def personStudentAlternatePin = newValidForCreatePersonStudentAlternatePin()
		personStudentAlternatePin.save( failOnError: true, flush: true )
		//Test if the generated entity now has an id assigned
        assertNotNull personStudentAlternatePin.id

        def personStudentAlternatePinDup = newValidForCreatePersonStudentAlternatePin()
        try {
            personStudentAlternatePinDup.save( failOnError: true, flush: true )
            //personStudentAlternatePinService.update(map)
            fail("This should have failed with unique key violation")
        } catch ( e) {

            def ae = new ApplicationException(personStudentAlternatePinDup, e)
            if (ae.wrappedException == null)
                fail("This should have failed with unique key violation, but got: ${e}")
        }
	}

	// The unique key on sprapin is term_code, pidm, and process_name.
    // The for allows updates of term and pin, but not process name.
    // Read only fields are processName, and pidm
    void testUpdateValidPersonStudentAlternatePin() {
		def personStudentAlternatePin = newValidForCreatePersonStudentAlternatePin()
		personStudentAlternatePin.save( failOnError: true, flush: true )
        assertNotNull personStudentAlternatePin.id
        assertEquals 0L, personStudentAlternatePin.version
        assertEquals i_success_pidm, personStudentAlternatePin.pidm
        assertEquals i_success_processName, personStudentAlternatePin.processName
        assertEquals i_success_pin, personStudentAlternatePin.pin
        
		//Update the entity
		personStudentAlternatePin.pin = u_success_pin
		personStudentAlternatePin.term = u_success_term
		personStudentAlternatePin.save( failOnError: true, flush: true )
		//Assert for sucessful update        
        personStudentAlternatePin = PersonStudentAlternatePin.get( personStudentAlternatePin.id )
        assertEquals 1L, personStudentAlternatePin?.version
        assertEquals u_success_pin, personStudentAlternatePin.pin
        assertEquals u_success_term, personStudentAlternatePin.term
	}

    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

    	def personStudentAlternatePin = newValidForCreatePersonStudentAlternatePin()
    	personStudentAlternatePin.save(flush: true, failOnError: true)
    	personStudentAlternatePin.refresh()
    	assertNotNull "PersonStudentAlternatePin should have been saved", personStudentAlternatePin.id
    	
    	assertEquals date.format(today), date.format(personStudentAlternatePin.lastModified)
    	assertEquals hour.format(today), hour.format(personStudentAlternatePin.lastModified)
    }

    void testOptimisticLock() {
		def personStudentAlternatePin = newValidForCreatePersonStudentAlternatePin()
		personStudentAlternatePin.save( failOnError: true, flush: true )
        
        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update SPRAPIN set SPRAPIN_VERSION = 999 where SPRAPIN_SURROGATE_ID = ?", [ personStudentAlternatePin.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		personStudentAlternatePin.pin = u_success_pin
        personStudentAlternatePin.term = u_success_term
        shouldFail( HibernateOptimisticLockingFailureException ) {
            personStudentAlternatePin.save( failOnError: true, flush: true )
        }
    }
	
	void testDeletePersonStudentAlternatePin() {
		def personStudentAlternatePin = newValidForCreatePersonStudentAlternatePin()
		personStudentAlternatePin.save( failOnError: true, flush: true )
		def id = personStudentAlternatePin.id
		assertNotNull id
		personStudentAlternatePin.delete()
		assertNull PersonStudentAlternatePin.get( id )
	}
	
    void testValidation() {
       def personStudentAlternatePin = newValidForCreatePersonStudentAlternatePin()
       assertTrue "PersonStudentAlternatePin could not be validated as expected due to ${personStudentAlternatePin.errors}", personStudentAlternatePin.validate()
    }

    void testNullValidationFailure() {
        def personStudentAlternatePin = new PersonStudentAlternatePin()
        assertFalse "PersonStudentAlternatePin should have failed validation", personStudentAlternatePin.validate()
        assertErrorsFor personStudentAlternatePin, 'nullable', 
                                               [ 
                                                 'pidm', 
                                                 'processName', 
                                                 'pin',                                                  
                                                 'term'
                                               ]
    }
    
 	private def newValidForCreatePersonStudentAlternatePin() {
		def personStudentAlternatePin = new PersonStudentAlternatePin(
			pidm: PersonIdentificationName.findByBannerId('GINNY').pidm,
			processName: i_success_processName,
			pin: i_success_pin,
			term: i_success_term, 
		)
		return personStudentAlternatePin
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personstudentalternatepin_custom_integration_test_methods) ENABLED START*/

    /**
     * Test to list result set is ordered by term and processName ascending.
     * Also ensure list result is that above term entered. (Data set only has records above keyBlock term)
     */
     void testFetchByPidmAndTermAllAboveKeyBlockTerm() {
         def pidm =  PersonUtility.getPerson("OLIVER").pidm
         def term =  Term.findByCode('200323')

         def processName_sfaregs = "SFAREGS"
         def processName_registration = "REGISTRATION"

         // Existing data for person is for processName of "TREQ" for terms 200323,200343,200410
         // We want to add other values to assure sort order on processName

         def personStudentAlternatePinTwo = new PersonStudentAlternatePin(
             pidm: PersonIdentificationName.findByBannerId('OLIVER').pidm,
             processName: processName_sfaregs,
             pin: i_success_pin,
             term: Term.findByCode('200323')
         )
         def personStudentAlternatePinThree = new PersonStudentAlternatePin(
             pidm: PersonIdentificationName.findByBannerId('OLIVER').pidm,
             processName: processName_registration,
             pin: i_success_pin,
             term: Term.findByCode('200323')
         )
         personStudentAlternatePinTwo.save( failOnError: true, flush: true )
         assertNotNull personStudentAlternatePinTwo.id
         personStudentAlternatePinThree.save( failOnError: true, flush: true )
         assertNotNull personStudentAlternatePinThree.id

         def resultList = PersonStudentAlternatePin.fetchByPidmAndTerm(pidm, term.code)
         assertTrue resultList.size() == 5

         // Assert sort order by term, proeessName
         assertEquals "200323", resultList[0].term.code
         assertEquals processName_registration, resultList[0].processName

         assertEquals "200323", resultList[1].term.code
         assertEquals processName_sfaregs, resultList[1].processName

         assertEquals "200323", resultList[2].term.code
         assertEquals "TREG", resultList[2].processName

         assertEquals "200343", resultList[3].term.code
         assertEquals "TREG", resultList[3].processName

         assertEquals "200410", resultList[4].term.code
         assertEquals "TREG", resultList[4].processName
     }

    /**
     * Test to list result set is ordered by term and processName ascending.
     * Also ensure list result is that above term entered. (Data set has records below keyBlock set)
     */
     void testFetchByPidmAndTermNotAllAboveKeyBlockTerm() {
         def pidm =  PersonUtility.getPerson("OLIVER").pidm
         def term =  Term.findByCode('200323')

         def processName_sfaregs = "SFAREGS"
         def processName_registration = "REGISTRATION"

         // Existing data for person is for processName of "TREQ" for terms 200323,200343,200410
         // We want to add other values to assure sort order on processName

         def personStudentAlternatePinTwo = new PersonStudentAlternatePin(
             pidm: PersonIdentificationName.findByBannerId('OLIVER').pidm,
             processName: processName_sfaregs,
             pin: i_success_pin,
             term: Term.findByCode('200323')
         )
         def personStudentAlternatePinThree = new PersonStudentAlternatePin(
             pidm: PersonIdentificationName.findByBannerId('OLIVER').pidm,
             processName: processName_registration,
             pin: i_success_pin,
             term: Term.findByCode('200323')
         )
         def personStudentAlternatePinFour = new PersonStudentAlternatePin(
             pidm: PersonIdentificationName.findByBannerId('OLIVER').pidm,
             processName: processName_sfaregs,
             pin: i_success_pin,
             term: Term.findByCode('199910')
         )
         def personStudentAlternatePinFive = new PersonStudentAlternatePin(
             pidm: PersonIdentificationName.findByBannerId('OLIVER').pidm,
             processName: processName_registration,
             pin: i_success_pin,
             term: Term.findByCode('199910')
         )
         personStudentAlternatePinTwo.save( failOnError: true, flush: true )
         assertNotNull personStudentAlternatePinTwo.id
         personStudentAlternatePinThree.save( failOnError: true, flush: true )
         assertNotNull personStudentAlternatePinThree.id
         personStudentAlternatePinFour.save( failOnError: true, flush: true )
         assertNotNull personStudentAlternatePinFour.id
         personStudentAlternatePinFive.save( failOnError: true, flush: true )
         assertNotNull personStudentAlternatePinFive.id

         def resultList = PersonStudentAlternatePin.fetchByPidmAndTerm(pidm, term.code)
         assertTrue resultList.size() == 5

         // Assert sort order by term, proeessName
         assertEquals "200323", resultList[0].term.code
         assertEquals processName_registration, resultList[0].processName

         assertEquals "200323", resultList[1].term.code
         assertEquals processName_sfaregs, resultList[1].processName

         assertEquals "200323", resultList[2].term.code
         assertEquals "TREG", resultList[2].processName

         assertEquals "200343", resultList[3].term.code
         assertEquals "TREG", resultList[3].processName

         assertEquals "200410", resultList[4].term.code
         assertEquals "TREG", resultList[4].processName

         resultList = []
         resultList =  PersonStudentAlternatePin.fetchByPidmAndTerm(pidm, Term.findByCode("199910").code)
         assertTrue resultList.size() == 7
     }

    void testFetchByPidmAndTermNoPidm() {
         def pidm
         def termOne =  Term.findByCode('200323')
         def termTwo

         def resultList = PersonStudentAlternatePin.fetchByPidmAndTerm(pidm, termOne.code)
         assertTrue resultList.size() == 0

         pidm =  PersonUtility.getPerson("OLIVER").pidm
         resultList = PersonStudentAlternatePin.fetchByPidmAndTerm(pidm, termTwo)
         assertTrue resultList.size() == 0
     }


    /*PROTECTED REGION END*/
}
