/*********************************************************************************
Copyright 2012-2019 Ellucian Company L.P. and its affiliates.
**********************************************************************************/

package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.junit.Before
import org.junit.Test
import org.junit.After

import static groovy.test.GroovyAssert.*
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.testing.BaseIntegrationTestCase

@Integration
@Rollback
class PersonRaceServiceIntegrationTests extends BaseIntegrationTestCase {

    def personRaceService

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


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction
    void initializeTestDataForReferences() {
        u_success_pidm = i_success_pidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        u_failure_pidm = i_failure_pidm = PersonIdentificationName.findByBannerId("HOF00716").pidm

    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testPersonRaceValidCreate() {
        def personRace = newValidForCreatePersonRace()
        def map = [domainModel: personRace]
        personRace = personRaceService.create(map)
        assertNotNull "PersonRace ID is null in PersonRace Service Tests Create", personRace.id
        assertNotNull personRace.version
        assertNotNull personRace.dataOrigin
        assertNotNull personRace.lastModifiedBy
        assertNotNull personRace.lastModified
    }


    @Test
    void testPersonRaceInvalidCreate() {
        def personRace = newInvalidForCreatePersonRace()
        def map = [domainModel: personRace]
        shouldFail(ApplicationException) {
            personRace = personRaceService.create(map)
        }
    }


    @Test
    void testPersonRaceInvalidUpdate() {
        def personRace = newValidForCreatePersonRace()
        def map = [domainModel: personRace]
        personRace = personRaceService.create(map)
        assertNotNull "PersonRace ID is null in PersonRace Service Tests Create", personRace.id
        assertNotNull personRace.version
        assertNotNull personRace.dataOrigin
        assertNotNull personRace.lastModifiedBy
        assertNotNull personRace.lastModified
        //Update the entity with new invalid values

        personRace.race = null

        map.domainModel = personRace
        shouldFail(ApplicationException) {
            personRace = personRaceService.update(map)
        }
    }


    @Test
    void testPersonRaceDelete() {
        def personRace = newValidForCreatePersonRace()
        def map = [domainModel: personRace]
        personRace = personRaceService.create(map)
        assertNotNull "PersonRace ID is null in PersonRace Service Tests Create", personRace.id
        def id = personRace.id
        map = [domainModel: personRace]
        personRaceService.delete(map)
        assertNull "PersonRace should have been deleted", personRace.get(id)
    }


    @Test
    void testReadOnly() {
        def personRace = newValidForCreatePersonRace()
        def map = [domainModel: personRace]
        personRace = personRaceService.create(map)
        assertNotNull "PersonRace ID is null in PersonRace Service Tests Create", personRace.id
        personRace.pidm = PersonUtility.getPerson("HOS00001").pidm
        personRace.race = null
        try {
            personRaceService.update([domainModel: personRace])
            fail("This should have failed with @@r1:readonlyFieldsCannotBeModified")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }

    @Test
    void testCreateOrUpdateValid() {
        PersonRace personRace = newValidForCreatePersonRace()

        Map map = [createPersonRaces: personRace]
        personRaceService.createOrUpdate(map)
        PersonRace quiredPersonRace =PersonRace.fetchByPidmAndRace(personRace.pidm,personRace.race)
        assertNotNull quiredPersonRace
        assertNotNull "PersonRace ID is null in PersonRace Service Tests Create", quiredPersonRace.id
        assertNotNull quiredPersonRace.version
        assertNotNull quiredPersonRace.dataOrigin
        assertNotNull quiredPersonRace.lastModifiedBy
        assertNotNull quiredPersonRace.lastModified

        map = [deletePersonRaces: quiredPersonRace]
        personRaceService.createOrUpdate(map)
        quiredPersonRace =PersonRace.fetchByPidmAndRace(personRace.pidm,personRace.race)
        assertNull quiredPersonRace



    }

    @Test
    void testCreateOrUpdateInValid() {
        PersonRace personRace = newInvalidForCreatePersonRace()
        Map map = [createPersonRaces: personRace]
        shouldFail(ApplicationException) {
            personRace = personRaceService.createOrUpdate(map)
        }
    }

    @Test
    void testDeletePersonRacesForInValid() {
           def personRace = newInvalidForCreatePersonRace()
           def map = [deletePersonRaces: personRace]
           shouldFail(ApplicationException) {
               personRace = personRaceService.createOrUpdate(map)
           }
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
}
