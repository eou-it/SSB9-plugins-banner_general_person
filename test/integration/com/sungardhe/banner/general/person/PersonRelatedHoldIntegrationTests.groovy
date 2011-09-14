
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
 
package com.sungardhe.banner.general.person

import com.sungardhe.banner.general.system.HoldType
import com.sungardhe.banner.general.system.Originator
import com.sungardhe.banner.testing.BaseIntegrationTestCase
import java.text.SimpleDateFormat
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException


class PersonRelatedHoldIntegrationTests extends BaseIntegrationTestCase {

	//def personRelatedHoldService
	
	protected void setUp() {
		formContext = ['SOAHOLD', 'SOQHOLD'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testCreatePersonRelatedHold() {
		def personRelatedHold = newPersonRelatedHold()
		save personRelatedHold
		//Test if the generated entity now has an id assigned		
        assertNotNull personRelatedHold.id
	}

	void testUpdatePersonRelatedHold() {
		def personRelatedHold = newPersonRelatedHold()
		save personRelatedHold
       
        assertNotNull personRelatedHold.id
        assertEquals 0L, personRelatedHold.version
        assertEquals 1, personRelatedHold.pidm
        assertEquals "TTTTT", personRelatedHold.userData
        assertNotNull personRelatedHold.fromDate
        assertNotNull personRelatedHold.toDate
        assertEquals false, personRelatedHold.releaseIndicator
        assertEquals "TTTTT", personRelatedHold.reason
        assertEquals 1, personRelatedHold.amountOwed
        
		//Update the entity
		def testDate = new Date()
		personRelatedHold.pidm = 1
		personRelatedHold.userData = "UUUUU"
		personRelatedHold.fromDate = testDate
		personRelatedHold.toDate = testDate
		personRelatedHold.releaseIndicator = false
		personRelatedHold.reason = "UUUUU"
		personRelatedHold.amountOwed = 0
		personRelatedHold.lastModified = testDate
		personRelatedHold.lastModifiedBy = "test"
		personRelatedHold.dataOrigin = "Banner" 
        save personRelatedHold
        
        personRelatedHold = PersonRelatedHold.get( personRelatedHold.id )
        assertEquals 1L, personRelatedHold?.version
        assertEquals 1, personRelatedHold.pidm
        assertEquals "UUUUU", personRelatedHold.userData
        assertEquals testDate, personRelatedHold.fromDate
        assertEquals testDate, personRelatedHold.toDate
        assertEquals false, personRelatedHold.releaseIndicator
        assertEquals "UUUUU", personRelatedHold.reason
        assertEquals 0, personRelatedHold.amountOwed
	}

    void testOptimisticLock() { 
		def personRelatedHold = newPersonRelatedHold()
		save personRelatedHold
        
        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update SV_SPRHOLD set SPRHOLD_VERSION = 999 where SPRHOLD_SURROGATE_ID = ?", [ personRelatedHold.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		personRelatedHold.pidm= 1
		personRelatedHold.userData="UUUUU"
		personRelatedHold.fromDate= new Date()
		personRelatedHold.toDate= new Date()
		personRelatedHold.releaseIndicator=false
		personRelatedHold.reason="UUUUU"
		personRelatedHold.amountOwed= 0
		personRelatedHold.lastModified= new Date()
		personRelatedHold.lastModifiedBy="test"
		personRelatedHold.dataOrigin= "Banner" 
        shouldFail( HibernateOptimisticLockingFailureException ) {
            personRelatedHold.save( flush: true )
        }
    }
	
	void testDeletePersonRelatedHold() {
		def personRelatedHold = newPersonRelatedHold()
		save personRelatedHold
		def id = personRelatedHold.id
		assertNotNull id
		personRelatedHold.delete()
		assertNull PersonRelatedHold.get( id )
	}
	
    void testValidation() {
       def personRelatedHold = newPersonRelatedHold()
       assertTrue "PersonRelatedHold could not be validated as expected due to ${personRelatedHold.errors}", personRelatedHold.validate()
    }

    void testNullValidationFailure() {
        def personRelatedHold = new PersonRelatedHold()
        assertFalse "PersonRelatedHold should have failed validation", personRelatedHold.validate()
        assertErrorsFor personRelatedHold, 'nullable', 
                                               [ 
                                                 'pidm', 
                                                 'userData', 
                                                 'fromDate', 
                                                 'toDate',
                                                 'holdType'
                                               ]
        assertNoErrorsFor personRelatedHold,
        									   [ 
             									 'reason', 
             									 'amountOwed',                                                  
                                                 'originator',
                                                 'releaseIndicator'
											   ]
    }
    
    void testMaxSizeValidationFailures() {
        def personRelatedHold = new PersonRelatedHold( 
        reason:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' )
		assertFalse "PersonRelatedHold should have failed validation", personRelatedHold.validate()
		assertErrorsFor personRelatedHold, 'maxSize', [ 'reason' ]    
    }

  
    
  private def newPersonRelatedHold() {
  
    /**
     * Please use the appropriate finder methods to load the references here
     * This area is being protected to preserve the customization on regeneration 
     */
    /*PROTECTED REGION ID(personrelatedhold_integration_tests_data_fetch_for_references) ENABLED START*/
	    def iholdType = HoldType.findWhere(code: "RG")
	    def ioriginator = Originator.findWhere(code: "ACCT")
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def fromDate = df1.parse("2010-07-01")
        def toDate = df1.parse("2010-09-01")
    /*PROTECTED REGION END*/
     
    def personRelatedHold = new PersonRelatedHold(
    		pidm: 1,
    		userData: "TTTTT", 
            fromDate: fromDate,
            toDate: toDate,
    		releaseIndicator: false,
    		reason: "TTTTT", 
    		amountOwed: 1,
			holdType: iholdType, 
			originator: ioriginator, 
            lastModified: new Date(),
			lastModifiedBy: "test", 
			dataOrigin: "Banner"
        )
        return personRelatedHold
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personrelatedhold_custom_integration_test_methods) ENABLED START*/

    void testInvalidBeginDatesEndDate() {
        testDates("2010-10-01", "2010-09-01", true)
        testDates("2010-10-01", "2010-09-01", false)
    }


    private def testDates(String fromDate , String toDate, Boolean fromOk ) {
        def personRelatedHold = newPersonRelatedHold()

        def df1 = new SimpleDateFormat("yyyy-MM-dd")

        if ( fromDate) personRelatedHold.fromDate = df1.parse(fromDate)
        if ( toDate) personRelatedHold.toDate = df1.parse(toDate)

        assertFalse "Should fail validation", personRelatedHold.validate()
        if (! fromOk ) assertErrorsFor personRelatedHold, 'validator', [ 'fromDate']
        else assertErrorsFor personRelatedHold, 'validator', [ 'toDate']
    }


    void testFetchByPidm() {
        def holdPidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("210009102").pidm

        def holdList = PersonRelatedHold.fetchByPidm(holdPidm)
        assertNotNull(holdList)
    }
        
    
    void testFetchByPidmAndDateBetween() {
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def holdDate = df1.parse("2010-12-01")
        def holdPidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("210009102").pidm

        def holdList = PersonRelatedHold.fetchByPidmAndDateBetween(holdPidm, holdDate)
        assertNotNull(holdList)
    }
    
    
    void testFetchByPidmDateAndHoldType() {
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def holdDate = df1.parse("2010-12-01")
        def holdPidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("210009102").pidm
        def holdType = HoldType.findByCode("AS")

        def holdList = PersonRelatedHold.fetchByPidmDateAndHoldType(holdPidm, holdDate, holdType.code )
        assertNotNull(holdList)
    }



    void testFetchByPidmAndDateCompare() {
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def holdDate = df1.parse("2010-12-01")
        def holdPidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("210009102").pidm

        def holdList = PersonRelatedHold.fetchByPidmAndDateCompare(holdPidm, holdDate)
        assertNotNull(holdList)
    }


    void testFetchRegistrationHoldsExist() {
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def holdDate = df1.parse("2010-12-01")
        def holdPidm = PersonIdentificationName.findByBannerIdAndChangeIndicatorIsNull("210009102").pidm

        def registrationHoldsExist = PersonRelatedHold.registrationHoldsExist(holdPidm, holdDate)
        assertTrue(registrationHoldsExist)
    }
    /*PROTECTED REGION END*/
}
