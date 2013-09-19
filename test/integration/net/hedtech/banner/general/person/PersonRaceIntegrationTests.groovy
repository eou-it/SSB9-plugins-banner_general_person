/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Ignore
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class PersonRaceIntegrationTests extends BaseIntegrationTestCase {

	//Test data for creating new domain instance
	//Valid test data (For success tests)

	def i_success_pidm
	def i_success_race = "MOA"
	//Invalid test data (For failure tests)

	def i_failure_pidm
	def i_failure_race = "MOAN"

	//Test data for creating updating domain instance
	//Valid test data (For success tests)

	def u_success_pidm
	def u_success_race = "MOA"
	//Valid test data (For failure tests)

	def u_failure_pidm
	def u_failure_race = "MOAN"

	protected void setUp() {
		formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
		initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references.
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
        u_success_pidm = i_success_pidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        u_failure_pidm = i_failure_pidm = PersonIdentificationName.findByBannerId("HOF00716").pidm
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testCreateValidPersonRace() {
		def personRace = newValidForCreatePersonRace()
		personRace.save( failOnError: true, flush: true )
		//Test if the generated entity now has an id assigned
        assertNotNull personRace.id
	}

	void testCreateInvalidPersonRace() {
		def personRace = newInvalidForCreatePersonRace()
		shouldFail(ValidationException) {
            personRace.save( failOnError: true, flush: true )
		}
	}

    @Ignore
	void testUpdateValidPersonRace() {
		def personRace = newValidForCreatePersonRace()
		personRace.save( failOnError: true, flush: true )
        assertNotNull personRace.id
        assertEquals 0L, personRace.version
        assertEquals i_success_pidm, personRace.pidm
        assertEquals i_success_race, personRace.race

		//Update the entity
		personRace.save( failOnError: true, flush: true )
		//Assert for sucessful update
        personRace = PersonRace.get( personRace.id )
        assertEquals 1L, personRace?.version
	}

    @Ignore
	void testUpdateInvalidPersonRace() {
		def personRace = newValidForCreatePersonRace()
		personRace.save( failOnError: true, flush: true )
        assertNotNull personRace.id
        assertEquals 0L, personRace.version
        assertEquals i_success_pidm, personRace.pidm
        assertEquals i_success_race, personRace.race

		//Update the entity with invalid values
		shouldFail(ValidationException) {
            personRace.save( failOnError: true, flush: true )
		}
	}

    @Ignore
    void testOptimisticLock() {
		def personRace = newValidForCreatePersonRace()
		personRace.save( failOnError: true, flush: true )

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update GV_GORPRAC set GORPRAC_VERSION = 999 where GORPRAC_SURROGATE_ID = ?", [ personRace.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
        shouldFail( HibernateOptimisticLockingFailureException ) {
            personRace.save( failOnError: true, flush: true )
        }
    }

	void testDeletePersonRace() {
		def personRace = newValidForCreatePersonRace()
		personRace.save( failOnError: true, flush: true )
		def id = personRace.id
		assertNotNull id
		personRace.delete()
		assertNull PersonRace.get( id )
	}

    void testValidation() {
       def personRace = newInvalidForCreatePersonRace()
       assertFalse "PersonRace could not be validated as expected due to ${personRace.errors}", personRace.validate()
    }

    void testNullValidationFailure() {
        def personRace = new PersonRace()
        assertFalse "PersonRace should have failed validation", personRace.validate()
        assertErrorsFor personRace, 'nullable',
                                               [
                                                 'pidm',
                                                 'race'
                                               ]
    }

    void testFetchByPidm() {
        def personRace = newValidForCreatePersonRace()
        personRace.save( failOnError: true, flush: true )
        i_success_race = "IND"
        def personRace1 = newValidForCreatePersonRace()
        personRace1.save( failOnError: true, flush: true )

        def results = PersonRace.findAllByPidm(i_success_pidm)
        assertEquals 2, results.size()

        def races = PersonRace.fetchByPidm(i_success_pidm)
        assertEquals races.size(), results.size()

    }


	private def newValidForCreatePersonRace() {
		def personRace = new PersonRace(
			pidm: i_success_pidm,
			race: i_success_race,
		)
		return personRace
	}

	private def newInvalidForCreatePersonRace() {
		def personRace = new PersonRace(
			pidm: i_failure_pidm,
			race: i_failure_race,
		)
		return personRace
	}
    void testFetchByPidmAndRaceSuccess() {
          PersonRace personRace = newValidForCreatePersonRace()
          personRace.save(failOnError: true, flush: true)
          assertNotNull personRace.id
          Long id = personRace.id
          PersonRace quiredPersonRace = PersonRace.fetchByPidmAndRace(personRace.pidm, personRace.race)

          assertNotNull quiredPersonRace
          assertEquals quiredPersonRace, personRace

          
          personRace.delete()
          assertNull personRace.get(id)
          quiredPersonRace = PersonRace.fetchByPidmAndRace(personRace.pidm, personRace.race)
          assertNull quiredPersonRace
     }


         void testFetchByPidmAndRaceForInvalidValues() {
          PersonRace personRace = newValidForCreatePersonRace()
          personRace.save(failOnError: true, flush: true)
          assertNotNull personRace.id

          // Test for Invalid pidm, Invalid term.code and Invalid studyPathSequenceNumber
          PersonRace quiredPersonRace = PersonRace.fetchByPidmAndRace(null, personRace.race)
          assertNull quiredPersonRace

          quiredPersonRace = PersonRace.fetchByPidmAndRace(personRace.pidm, null)
          assertNull quiredPersonRace

          quiredPersonRace = PersonRace.fetchByPidmAndRace(null, null)
          assertNull quiredPersonRace

          quiredPersonRace = PersonRace.fetchByPidmAndRace(-1, personRace.race)
          assertNull quiredPersonRace

          quiredPersonRace = PersonRace.fetchByPidmAndRace(-1, null)
          assertNull quiredPersonRace



         }




}
