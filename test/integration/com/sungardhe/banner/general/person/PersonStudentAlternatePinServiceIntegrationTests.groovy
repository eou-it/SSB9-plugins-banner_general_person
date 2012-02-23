/*******************************************************************************
 Copyright 2009-2012 SunGard Higher Education. All Rights Reserved.

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

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.exceptions.ApplicationException
import com.sungardhe.banner.general.system.Term

class PersonStudentAlternatePinServiceIntegrationTests extends BaseIntegrationTestCase {

    def personStudentAlternatePinService

	/*PROTECTED REGION ID(personstudentalternatepin_service_integration_test_data) ENABLED START*/
    def i_success_term
    def i_success_pidm
	def i_success_processName
	def i_success_pin

    def u_success_term
	def u_success_pin

    def u_failure_pidm
    def u_failure_processName

    def i_success_keyBlockMap = [term: i_success_term, pidm: i_success_pidm]
	/*PROTECTED REGION END*/

	protected void setUp() {
		formContext = ['SPAAPIN']
		super.setUp()
        initializeTestDataForReferences()
	}

	protected void tearDown() {
		super.tearDown()
	}

    void initializeTestDataForReferences() {
       i_success_term = Term.findByCode('201410')
       i_success_pidm = PersonIdentificationName.findByBannerId('GINNY').pidm
	   i_success_processName = "REGISTRATION"
	   i_success_pin = "123456"
       i_success_keyBlockMap = [term: i_success_term, pidm: i_success_pidm]

       u_success_term = Term.findByCode('201420')
	   u_success_pin = "654321"

       u_failure_pidm = PersonIdentificationName.findByBannerId('OLIVER').pidm
       u_failure_processName = "SFAREGS"
    }

    void testPersonStudentAlternatePinValidCreate() {
        def personStudentAlternatePin = newValidForCreatePersonStudentAlternatePin()
        def map = [keyBlock: i_success_keyBlockMap,domainModel: personStudentAlternatePin]

        personStudentAlternatePin = personStudentAlternatePinService.create(map)
        assertNotNull "PersonStudentAlternatePin ID is null in PersonStudentAlternatePin Service Tests Create", personStudentAlternatePin.id
        assertNotNull "PersonStudentAlternatePin term is null in PersonStudentAlternatePin Service Tests", personStudentAlternatePin.term
        assertNotNull personStudentAlternatePin.version
        assertNotNull personStudentAlternatePin.dataOrigin
        assertNotNull personStudentAlternatePin.lastModifiedBy
        assertNotNull personStudentAlternatePin.lastModified
    }

    void testPersonStudentAlternatePinValidUpdate() {
        def personStudentAlternatePin = newValidForCreatePersonStudentAlternatePin()
        def map = [keyBlock: i_success_keyBlockMap,	domainModel: personStudentAlternatePin]

        personStudentAlternatePin = personStudentAlternatePinService.create(map)
        assertNotNull "PersonStudentAlternatePin ID is null in PersonStudentAlternatePin Service Tests Create", personStudentAlternatePin.id
        assertNotNull "PersonStudentAlternatePin term is null in PersonStudentAlternatePin Service Tests", personStudentAlternatePin.term
        assertNotNull personStudentAlternatePin.version
        assertNotNull personStudentAlternatePin.dataOrigin
        assertNotNull personStudentAlternatePin.lastModifiedBy
        assertNotNull personStudentAlternatePin.lastModified
        //Update the entity with new values
        personStudentAlternatePin.term = u_success_term
        personStudentAlternatePin.pin = u_success_pin

        map.keyBlock = i_success_keyBlockMap
        map.domainModel = personStudentAlternatePin
        personStudentAlternatePinService.update(map)

        assertEquals u_success_pin, personStudentAlternatePin.pin
        assertEquals u_success_term, personStudentAlternatePin.term
    }

    void testPersonStudentAlternatePinDelete() {
        def personStudentAlternatePin = newValidForCreatePersonStudentAlternatePin()
        def map = [keyBlock: i_success_keyBlockMap, domainModel: personStudentAlternatePin]
        personStudentAlternatePin = personStudentAlternatePinService.create(map)
        assertNotNull "PersonStudentAlternatePin ID is null in PersonStudentAlternatePin Service Tests Create", personStudentAlternatePin.id

        def id = personStudentAlternatePin.id
        personStudentAlternatePinService.delete( id )
        assertNull "PersonStudentAlternatePin should have been deleted", personStudentAlternatePin.get(id)
      }

    void testReadOnlyForPidm() {
        def personStudentAlternatePin = newValidForCreatePersonStudentAlternatePin()
        def map = [keyBlock: i_success_keyBlockMap,	domainModel: personStudentAlternatePin]

        personStudentAlternatePin = personStudentAlternatePinService.create(map)
        assertNotNull "PersonStudentAlternatePin ID is null in PersonStudentAlternatePin Service Tests Create", personStudentAlternatePin.id
        assertNotNull "PersonStudentAlternatePin term is null in PersonStudentAlternatePin Service Tests", personStudentAlternatePin.term
        assertNotNull personStudentAlternatePin.version
        assertNotNull personStudentAlternatePin.dataOrigin
        assertNotNull personStudentAlternatePin.lastModifiedBy
        assertNotNull personStudentAlternatePin.lastModified

        personStudentAlternatePin.pidm = u_failure_pidm

        map.keyBlock = i_success_keyBlockMap
        map.domainModel = personStudentAlternatePin
        try {
            personStudentAlternatePinService.update(map)
            fail("This should have failed with @@r1:readonlyFieldsCannotBeModified")
        } catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }

    void testReadOnlyForProcessName() {
        def personStudentAlternatePin = newValidForCreatePersonStudentAlternatePin()
        def map = [keyBlock: i_success_keyBlockMap,	domainModel: personStudentAlternatePin]

        personStudentAlternatePin = personStudentAlternatePinService.create(map)
        assertNotNull "PersonStudentAlternatePin ID is null in PersonStudentAlternatePin Service Tests Create", personStudentAlternatePin.id
        assertNotNull "PersonStudentAlternatePin term is null in PersonStudentAlternatePin Service Tests", personStudentAlternatePin.term
        assertNotNull personStudentAlternatePin.version
        assertNotNull personStudentAlternatePin.dataOrigin
        assertNotNull personStudentAlternatePin.lastModifiedBy
        assertNotNull personStudentAlternatePin.lastModified

        personStudentAlternatePin.processName = u_failure_processName

        map.keyBlock = i_success_keyBlockMap
        map.domainModel = personStudentAlternatePin
        try {
            personStudentAlternatePinService.update(map)
            fail("This should have failed with @@r1:readonlyFieldsCannotBeModified")
        } catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }


    private def newValidForCreatePersonStudentAlternatePin() {
        def personStudentAlternatePin = new PersonStudentAlternatePin(
            pidm: i_success_pidm,
            processName: i_success_processName,
            pin: i_success_pin,
            term: i_success_term
        )
        return personStudentAlternatePin
    }

    /**
     * Please put all the custom service tests in this protected section to protect the code
     * from being overwritten on re-generation
    */
    /*PROTECTED REGION ID(personstudentalternatepin_custom_service_integration_test_methods) ENABLED START*/
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
         def map = [keyBlock: i_success_keyBlockMap,domainModel: personStudentAlternatePinTwo]
         personStudentAlternatePinTwo = personStudentAlternatePinService.create(map)
         assertNotNull personStudentAlternatePinTwo.id

         map = [keyBlock: i_success_keyBlockMap,domainModel: personStudentAlternatePinThree]
         personStudentAlternatePinThree = personStudentAlternatePinService.create(map)
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
         def map = [keyBlock: i_success_keyBlockMap,domainModel: personStudentAlternatePinTwo]
         personStudentAlternatePinTwo = personStudentAlternatePinService.create(map)
         assertNotNull personStudentAlternatePinTwo.id

         map = [keyBlock: i_success_keyBlockMap,domainModel: personStudentAlternatePinThree]
         personStudentAlternatePinThree = personStudentAlternatePinService.create(map)
         assertNotNull personStudentAlternatePinThree.id

         map = [keyBlock: i_success_keyBlockMap,domainModel: personStudentAlternatePinFour]
         personStudentAlternatePinFour = personStudentAlternatePinService.create(map)
         assertNotNull personStudentAlternatePinFour.id

         map = [keyBlock: i_success_keyBlockMap,domainModel: personStudentAlternatePinFive]
         personStudentAlternatePinFive = personStudentAlternatePinService.create(map)
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
    /*PROTECTED REGION END*/


}
