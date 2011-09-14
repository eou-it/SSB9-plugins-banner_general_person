
/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
/**
 Banner Automator Version: 1.24
 Generated: Thu Aug 04 14:06:16 EDT 2011 
 */
package com.sungardhe.banner.general.person

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.exceptions.ApplicationException 
import groovy.sql.Sql 
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import com.sungardhe.banner.general.system.EmailType
import java.sql.Timestamp


class PersonEMailIntegrationTests extends BaseIntegrationTestCase {
	
	/*PROTECTED REGION ID(personemail_domain_integration_test_data) ENABLED START*/
	//Test data for creating new domain instance
	//Valid test data (For success tests)
	def i_success_emailType

	def i_success_pidm = 1
	def i_success_emailAddress = "TTTTT"
	def i_success_statusIndicator = "I"
	def i_success_preferredIndicator = true
	def i_success_commentData = "TTTTT"
	def i_success_displayWebIndicator = true    
	//Invalid test data (For failure tests)
	def i_failure_emailType

	def i_failure_pidm = 1
	def i_failure_emailAddress = "TTTTT"
	def i_failure_statusIndicator = "I"
	def i_failure_preferredIndicator = true
	def i_failure_commentData = "TTTTT"
	def i_failure_displayWebIndicator = true
	
	//Test data for creating updating domain instance
	//Valid test data (For success tests)
	def u_success_emailType

	def u_success_pidm = 1
	def u_success_emailAddress = "TTTTT"
	def u_success_statusIndicator = "I"
	def u_success_preferredIndicator = true
	def u_success_commentData = "TTTTT"
	def u_success_displayWebIndicator = true	
	//Valid test data (For failure tests)
	def u_failure_emailType

	def u_failure_pidm = 1
	def u_failure_emailAddress = "TTTTT"
	def u_failure_statusIndicator = "I"
	def u_failure_preferredIndicator = true
	def u_failure_commentData = "TTTTT"
	def u_failure_displayWebIndicator = true	
	/*PROTECTED REGION END*/
	
	protected void setUp() {
		formContext = ['GEAPART'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references. 
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		/*PROTECTED REGION ID(personemail_domain_integration_test_data_initialization) ENABLED START*/
		//Valid test data (For success tests)	
    	i_success_emailType = EmailType.findWhere(code: "CAMP")

		//Invalid test data (For failure tests)
	    i_failure_emailType = EmailType.findWhere() //TODO: fill in the query condition 

		//Valid test data (For success tests)
	    u_success_emailType = EmailType.findWhere() //TODO: fill in the query condition 

		//Valid test data (For failure tests)
    	u_failure_emailType = EmailType.findWhere() //TODO: fill in the query condition 
		
		//Test data for references for custom tests
		/*PROTECTED REGION END*/
	}
	
	protected void tearDown() {
		super.tearDown()
	}

    /*
	void testCreateValidPersonEMail() {
		def personEMail = newValidForCreatePersonEMail()
		personEMail.save( failOnError: true, flush: true )
		//Test if the generated entity now has an id assigned		
        assertNotNull personEMail.id
	}

	void testCreateInvalidPersonEMail() {
		def personEMail = newInvalidForCreatePersonEMail()
		shouldFail(ValidationException) {
            personEMail.save( failOnError: true, flush: true )		
		}
	}

	void testUpdateValidPersonEMail() {
		def personEMail = newValidForCreatePersonEMail()
		personEMail.save( failOnError: true, flush: true )
        assertNotNull personEMail.id
        assertEquals 0L, personEMail.version
        assertEquals i_success_pidm, personEMail.pidm
        assertEquals i_success_emailAddress, personEMail.emailAddress
        assertEquals i_success_statusIndicator, personEMail.statusIndicator
        assertEquals i_success_preferredIndicator, personEMail.preferredIndicator
        assertEquals i_success_commentData, personEMail.commentData
        assertEquals i_success_displayWebIndicator, personEMail.displayWebIndicator
        
		//Update the entity
		personEMail.statusIndicator = u_success_statusIndicator 
		personEMail.preferredIndicator = u_success_preferredIndicator 
		personEMail.commentData = u_success_commentData 
		personEMail.displayWebIndicator = u_success_displayWebIndicator 
		
		personEMail.save( failOnError: true, flush: true )
		//Assert for sucessful update        
        personEMail = PersonEMail.get( personEMail.id )
        assertEquals 1L, personEMail?.version
        assertEquals u_success_statusIndicator, personEMail.statusIndicator
        assertEquals u_success_preferredIndicator, personEMail.preferredIndicator
        assertEquals u_success_commentData, personEMail.commentData
        assertEquals u_success_displayWebIndicator, personEMail.displayWebIndicator
		
	}
	
	void testUpdateInvalidPersonEMail() {
		def personEMail = newValidForCreatePersonEMail()
		personEMail.save( failOnError: true, flush: true )
        assertNotNull personEMail.id
        assertEquals 0L, personEMail.version
        assertEquals i_success_pidm, personEMail.pidm
        assertEquals i_success_emailAddress, personEMail.emailAddress
        assertEquals i_success_statusIndicator, personEMail.statusIndicator
        assertEquals i_success_preferredIndicator, personEMail.preferredIndicator
        assertEquals i_success_commentData, personEMail.commentData
        assertEquals i_success_displayWebIndicator, personEMail.displayWebIndicator
        
		//Update the entity with invalid values
		personEMail.statusIndicator = u_failure_statusIndicator 
		personEMail.preferredIndicator = u_failure_preferredIndicator 
		personEMail.commentData = u_failure_commentData 
		personEMail.displayWebIndicator = u_failure_displayWebIndicator 
		
		shouldFail(ValidationException) {
            personEMail.save( failOnError: true, flush: true )		
		}
	}

    void testDeletePersonEMail() {
		def personEMail = newValidForCreatePersonEMail()
		personEMail.save( failOnError: true, flush: true )
		def id = personEMail.id
		assertNotNull id
		personEMail.delete()
		assertNull PersonEMail.get( id )
	}
	
    void testValidation() {
       def personEMail = newInvalidForCreatePersonEMail()
       assertFalse "PersonEMail could not be validated as expected due to ${personEMail.errors}", personEMail.validate()
    }

    void testNullValidationFailure() {
        def personEMail = new PersonEMail()
        assertFalse "PersonEMail should have failed validation", personEMail.validate()
        assertErrorsFor personEMail, 'nullable', 
                                               [ 
                                                 'pidm', 
                                                 'emailAddress', 
                                                 'statusIndicator', 
                                                 'preferredIndicator', 
                                                 'displayWebIndicator',                                                  
                                                 'emailType'
                                               ]
        assertNoErrorsFor personEMail,
        									   [ 
             									 'commentData'                                                 
											   ]
    }
    
    void testMaxSizeValidationFailures() {
        def personEMail = new PersonEMail( 
        commentData:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' )
		assertFalse "PersonEMail should have failed validation", personEMail.validate()
		assertErrorsFor personEMail, 'maxSize', [ 'commentData' ]    
    }

    */
//	void testValidationMessages() {
//	    def personEMail = newInvalidForCreatePersonEMail()
//	    personEMail.pidm = null
//	    assertFalse personEMail.validate()
//	    assertLocalizedError personEMail, 'nullable', /.*Field.*pidm.*of class.*PersonEMail.*cannot be null.*/, 'pidm'
//	    personEMail.emailAddress = null
//	    assertFalse personEMail.validate()
//	    assertLocalizedError personEMail, 'nullable', /.*Field.*emailAddress.*of class.*PersonEMail.*cannot be null.*/, 'emailAddress'
//	    personEMail.statusIndicator = null
//	    assertFalse personEMail.validate()
//	    assertLocalizedError personEMail, 'nullable', /.*Field.*statusIndicator.*of class.*PersonEMail.*cannot be null.*/, 'statusIndicator'
//	    personEMail.preferredIndicator = null
//	    assertFalse personEMail.validate()
//	    assertLocalizedError personEMail, 'nullable', /.*Field.*preferredIndicator.*of class.*PersonEMail.*cannot be null.*/, 'preferredIndicator'
//	    personEMail.displayWebIndicator = null
//	    assertFalse personEMail.validate()
//	    assertLocalizedError personEMail, 'nullable', /.*Field.*displayWebIndicator.*of class.*PersonEMail.*cannot be null.*/, 'displayWebIndicator'
//	    personEMail.emailType = null
//	    assertFalse personEMail.validate()
//	    assertLocalizedError personEMail, 'nullable', /.*Field.*emailType.*of class.*PersonEMail.*cannot be null.*/, 'emailType'
//	}
  
    
	private def newValidForCreatePersonEMail() {
		def personEMail = new PersonEMail(
			pidm: i_success_pidm, 
			emailAddress: i_success_emailAddress, 
			statusIndicator: i_success_statusIndicator, 
			preferredIndicator: i_success_preferredIndicator, 
			commentData: i_success_commentData, 
			displayWebIndicator: i_success_displayWebIndicator, 
			emailType: i_success_emailType
		)
		return personEMail
	}

	private def newInvalidForCreatePersonEMail() {
		def personEMail = new PersonEMail(
			pidm: i_failure_pidm, 
			emailAddress: i_failure_emailAddress, 
			statusIndicator: i_failure_statusIndicator, 
			preferredIndicator: i_failure_preferredIndicator, 
			commentData: i_failure_commentData, 
			displayWebIndicator: i_failure_displayWebIndicator, 
			emailType: i_failure_emailType, 
		)
		return personEMail
	}

   /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personemail_custom_integration_test_methods) ENABLED START*/

   def testFetchByPidmAndStatusAndWebDisplayAndPreferredIndicator() {
       def results = PersonEMail.fetchByPidmAndStatusAndWebDisplayAndPreferredIndicator(33784, 'A', 'Y', 'Y')

       assertTrue results.size() == 1

       def res = results.get(0)

       assertEquals res.id, 1412


       assertEquals res.version, 0
       assertEquals res.pidm, 33784
       assertEquals res.emailAddress, "einstein2be@verizon.net"
       assertEquals res.statusIndicator, "A"
       assertEquals res.preferredIndicator, true
       assertEquals res.commentData, null
       assertEquals res.displayWebIndicator, true
       assertEquals res.dataOrigin, "Banner"

   }

    /*PROTECTED REGION END*/
}
