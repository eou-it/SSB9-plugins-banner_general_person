/*********************************************************************************
 Copyright 2009-2020 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import static groovy.test.GroovyAssert.*
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.testing.BaseIntegrationTestCase

@Integration
@Rollback
class PersonIdentificationNameIntegrationTests extends BaseIntegrationTestCase {

    def personIdentificationNameService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCreatePersonIdentificationName() {
        def personIdentificationName = newPersonIdentificationName()
        save personIdentificationName
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName.id
    }


    @Test
    void testDeletePersonIdentificationName() {
        def personIdentificationName = newPersonIdentificationName()
        save personIdentificationName

        def persistentPropertyMap = personIdentificationName.gormPersistentEntity.persistentPropertyNames.collectEntries {
            [it, personIdentificationName[it]]
        }

        def newPersonIdentification = new PersonIdentificationName(persistentPropertyMap)
        newPersonIdentification.changeIndicator = "N"
        newPersonIdentification.lastName = "YYYYYYYY"
        save newPersonIdentification
        assertNotNull newPersonIdentification.id

        newPersonIdentification.delete()

    }


    @Test
    void testValidation() {
        def personIdentificationName = newPersonIdentificationName()
        assertTrue "PersonIdentificationName could not be validated as expected due to ${personIdentificationName.errors}", personIdentificationName.validate()
    }


    @Test
    void testNullValidationFailure() {
        def personIdentificationName = new PersonIdentificationName()
        assertFalse "PersonIdentificationName should have failed validation", personIdentificationName.validate()
        assertErrorsFor personIdentificationName, 'nullable',
                        [
                                'lastName'
                        ]
        assertNoErrorsFor personIdentificationName,
                          [
                                  'firstName',
                                  'middleName',
                                  'changeIndicator',
                                  'entityIndicator',
                                  'userData',
                                  'origin',
                                  'searchLastName',
                                  'searchFirstName',
                                  'searchMiddleName',
                                  'soundexLastName',
                                  'soundexFirstName',
                                  'createUser',
                                  'createDate',
                                  'surnamePrefix',
                                  'nameType',
                                  'cohortReasonEFineGrainedAccessControlDomain'
                          ]
    }


    @Test
    void testMaxSizeValidationFailures() {
        def personIdentificationName = new PersonIdentificationName(
                firstName: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                middleName: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                changeIndicator: 'XXX',
                entityIndicator: 'XXX',
                userData: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                origin: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                searchLastName: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                searchFirstName: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                searchMiddleName: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                soundexLastName: 'XXXXXX',
                soundexFirstName: 'XXXXXX',
                createUser: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                surnamePrefix: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "PersonIdentificationName should have failed validation", personIdentificationName.validate()
        assertErrorsFor personIdentificationName, 'maxSize', ['firstName', 'middleName', 'changeIndicator', 'entityIndicator', 'userData', 'origin', 'searchLastName', 'searchFirstName', 'searchMiddleName', 'soundexLastName', 'soundexFirstName', 'createUser', 'surnamePrefix']
    }



    @Test
    void testFetchByBannerId() {
        def personIdentificationName1 = newPersonIdentificationName()
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id

        def personIdentificationName2 = newPersonIdentificationName()
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id

        def searchString = personIdentificationName1.bannerId
        def invalidSearchString = "XXX"

        def results = PersonIdentificationName.fetchByBannerId(searchString)
        assertNotNull results
        assertTrue results.contains(personIdentificationName1)
        assertFalse results.contains(personIdentificationName2)

        assertTrue PersonIdentificationName.fetchByBannerId(invalidSearchString)?.isEmpty()
    }


    @Test
    void testFetchBySomeBannerId() {
        def invalidSearchString = "XXX"
        def results = PersonIdentificationName.fetchBySomeBannerId(invalidSearchString)
        assertEquals results?.size(), 1
        assertNull results?.list[0]

        def limitSearchString1 = "%"
        results = PersonIdentificationName.fetchBySomeBannerId(limitSearchString1)
        assertEquals results?.size(), 1
        assertNull results?.list[0]

        def limitSearchString2 = "%%"
        results = PersonIdentificationName.fetchBySomeBannerId(limitSearchString2)
        assertEquals results?.size(), 1
        assertNull results?.list[0]
    }


    @Test
    void testFetchBySomeName() {
        def limitSearchString1 = "%"
        def results = PersonIdentificationName.fetchBySomeName(limitSearchString1)
        assertEquals results?.size(), 1
        assertNull results?.list[0]

        def limitSearchString2 = "%%"
        results = PersonIdentificationName.fetchBySomeName(limitSearchString2)
        assertEquals results?.size(), 1
        assertNull results?.list[0]
    }


    @Test
    void testFetchAllPersonByNameOrBannerId() {
        def personIdentificationName1 = newPersonIdentificationName()
        personIdentificationName1.firstName = "One"
        personIdentificationName1.middleName = null
        personIdentificationName1.lastName = "UnitTest"
        personIdentificationName1.bannerId = "UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals personIdentificationName1.firstName + " " + personIdentificationName1.lastName, personIdentificationName1.fullName
        def personIdentificationName2 = newPersonIdentificationName()
        personIdentificationName2.firstName = "Second"
        personIdentificationName2.middleName = "MI"
        personIdentificationName2.lastName = "UnitTest"
        personIdentificationName2.bannerId = "UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals personIdentificationName2.firstName + " " + personIdentificationName2.middleName + " " + personIdentificationName2.lastName, personIdentificationName2.fullName
        def searchString = "unittest"
        def invalidSearchString = "T!!!@@@"
        def pagingAndSortParams = [max: 10, offset: 0]
        def results = PersonIdentificationName.fetchAllPersonByNameOrBannerId(searchString, pagingAndSortParams)
        assertNotNull results
        assertTrue results.contains(personIdentificationName1)
        assertTrue results.contains(personIdentificationName2)
        assertTrue PersonIdentificationName.fetchAllPersonByNameOrBannerId(invalidSearchString, pagingAndSortParams)?.isEmpty()
    }


    @Test
    void testFetchAllPersonByBannerId() {
        def personIdentificationName1 = newPersonIdentificationName()
        personIdentificationName1.firstName = "One"
        personIdentificationName1.middleName = null
        personIdentificationName1.lastName = "UnitTest"
        personIdentificationName1.bannerId = "UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals personIdentificationName1.firstName + " " + personIdentificationName1.lastName, personIdentificationName1.fullName
        def personIdentificationName2 = newPersonIdentificationName()
        personIdentificationName2.firstName = "Second"
        personIdentificationName2.middleName = "MI"
        personIdentificationName2.lastName = "UnitTest"
        personIdentificationName2.bannerId = "UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals personIdentificationName2.firstName + " " + personIdentificationName2.middleName + " " + personIdentificationName2.lastName, personIdentificationName2.fullName
        def searchString = "unittest"
        def invalidSearchString = "T!!!@@@"
        def pagingAndSortParams = [max: 10, offset: 0]
        def results = PersonIdentificationName.fetchAllPersonByBannerId(searchString, pagingAndSortParams)
        assertNotNull results
        assertTrue results.size() >= 2
        assertTrue results.contains(personIdentificationName1)
        assertTrue results.contains(personIdentificationName2)
        assertTrue PersonIdentificationName.fetchAllPersonByBannerId(invalidSearchString, pagingAndSortParams)?.isEmpty()
        invalidSearchString = null
        assertTrue PersonIdentificationName.fetchAllPersonByBannerId(invalidSearchString, pagingAndSortParams)?.isEmpty()
    }


    @Test
    void testFetchAllPersonByLastName() {
        def personIdentificationName1 = newPersonIdentificationName()
        personIdentificationName1.firstName = "One"
        personIdentificationName1.middleName = null
        personIdentificationName1.lastName = "UnitTest"
        personIdentificationName1.bannerId = "UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals personIdentificationName1.firstName + " " + personIdentificationName1.lastName, personIdentificationName1.fullName
        def personIdentificationName2 = newPersonIdentificationName()
        personIdentificationName2.firstName = "Second"
        personIdentificationName2.middleName = "MI"
        personIdentificationName2.lastName = "UnitTest"
        personIdentificationName2.bannerId = "UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals personIdentificationName2.firstName + " " + personIdentificationName2.middleName + " " + personIdentificationName2.lastName, personIdentificationName2.fullName
        def searchString = "unittest"
        def invalidSearchString = "T!!!@@@"
        def pagingAndSortParams = [max: 10, offset: 0]
        def results = PersonIdentificationName.fetchAllPersonByLastName(searchString, pagingAndSortParams)
        assertNotNull results
        assertTrue results.size() >= 2
        assertTrue results.contains(personIdentificationName1)
        assertTrue results.contains(personIdentificationName2)
        assertTrue PersonIdentificationName.fetchAllPersonByLastName(invalidSearchString, pagingAndSortParams)?.isEmpty()
        invalidSearchString = null
        assertTrue PersonIdentificationName.fetchAllPersonByLastName(invalidSearchString, pagingAndSortParams)?.isEmpty()
    }


    @Test
    void testFetchAllNonPersonByNameOrBannerId() {
        def personIdentificationName1 = newNonPersonIdentificationName("FIRSTCOMPANY")
        personIdentificationName1.bannerId = "UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals "FIRSTCOMPANY", personIdentificationName1.fullName
        def personIdentificationName2 = newNonPersonIdentificationName("SECONDCOMPANY")
        personIdentificationName2.bannerId = "UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals "SECONDCOMPANY", personIdentificationName2.fullName
        def searchString = "unittest"
        def invalidSearchString = "T!!!@@@"
        def pagingAndSortParams = [max: 10, offset: 0]
        def results = PersonIdentificationName.fetchAllNonPersonByNameOrBannerId(searchString, pagingAndSortParams)
        assertNotNull results
        assertTrue results.contains(personIdentificationName1)
        assertTrue results.contains(personIdentificationName2)

        searchString = "first"
        results = PersonIdentificationName.fetchAllNonPersonByNameOrBannerId(searchString, pagingAndSortParams)
        assertNotNull results
        assertTrue results.contains(personIdentificationName1)
        assertFalse results.contains(personIdentificationName2)

        assertTrue PersonIdentificationName.fetchAllNonPersonByNameOrBannerId(invalidSearchString, pagingAndSortParams)?.isEmpty()
    }


    @Test
    void testFetchAllNonPersonByBannerId() {
        def personIdentificationName1 = newNonPersonIdentificationName("FIRSTCOMPANY")
        personIdentificationName1.bannerId = "UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals "FIRSTCOMPANY", personIdentificationName1.fullName
        def personIdentificationName2 = newNonPersonIdentificationName("SECONDCOMPANY")
        personIdentificationName2.bannerId = "UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals "SECONDCOMPANY", personIdentificationName2.fullName
        def searchString = "unittest"
        def invalidSearchString = "T!!!@@@"
        def pagingAndSortParams = [max: 10, offset: 0]
        def results = PersonIdentificationName.fetchAllNonPersonByBannerId(searchString, pagingAndSortParams)
        assertNotNull results
        assertTrue results.size() >= 2
        assertTrue results.contains(personIdentificationName1)
        assertTrue results.contains(personIdentificationName2)

        assertTrue PersonIdentificationName.fetchAllNonPersonByBannerId(invalidSearchString, pagingAndSortParams)?.isEmpty()
        invalidSearchString = null
        assertTrue PersonIdentificationName.fetchAllNonPersonByBannerId(invalidSearchString, pagingAndSortParams)?.isEmpty()
    }


    @Test
    void testFetchAllNonPersonByLastName() {
        def personIdentificationName1 = newNonPersonIdentificationName("FIRSTCOMPANY")
        personIdentificationName1.bannerId = "UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals "FIRSTCOMPANY", personIdentificationName1.fullName
        def personIdentificationName2 = newNonPersonIdentificationName("SECONDCOMPANY")
        personIdentificationName2.bannerId = "UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals "SECONDCOMPANY", personIdentificationName2.fullName
        def searchString = "SECOND"
        def invalidSearchString = "T!!!@@@"
        def pagingAndSortParams = [max: 10, offset: 0]
        def results = PersonIdentificationName.fetchAllNonPersonByLastName(searchString, pagingAndSortParams)
        assertNotNull results
        assertTrue results.size() >= 1
        assertTrue results.contains(personIdentificationName2)

        assertTrue PersonIdentificationName.fetchAllNonPersonByLastName(invalidSearchString, pagingAndSortParams)?.isEmpty()
        invalidSearchString = null
        assertTrue PersonIdentificationName.fetchAllNonPersonByLastName(invalidSearchString, pagingAndSortParams)?.isEmpty()
    }


    @Test
    void testFetchCountPersonByNameOrBannerId() {
        def personIdentificationName1 = newPersonIdentificationName()
        personIdentificationName1.firstName = "One"
        personIdentificationName1.middleName = null
        personIdentificationName1.lastName = "UnitTest"
        personIdentificationName1.bannerId = "UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals personIdentificationName1.firstName + " " + personIdentificationName1.lastName, personIdentificationName1.fullName
        def personIdentificationName2 = newPersonIdentificationName()
        personIdentificationName2.firstName = "Second"
        personIdentificationName2.middleName = "MI"
        personIdentificationName2.lastName = "UnitTest"
        personIdentificationName2.bannerId = "UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals personIdentificationName2.firstName + " " + personIdentificationName2.middleName + " " + personIdentificationName2.lastName, personIdentificationName2.fullName
        def searchString = "unittest"
        def invalidSearchString = "T!!!@@@"

        def personCount = PersonIdentificationName.fetchCountPersonByNameOrBannerId(searchString)
        assertNotNull personCount
        assertTrue personCount > 0

        personCount = PersonIdentificationName.fetchCountPersonByNameOrBannerId(invalidSearchString)
        assertFalse personCount > 0

        invalidSearchString = null
        personCount = PersonIdentificationName.fetchCountPersonByNameOrBannerId(invalidSearchString)
        assertFalse personCount > 0

    }


    @Test
    void testFetchCountPersonByBannerId() {
        def personIdentificationName1 = newPersonIdentificationName()
        personIdentificationName1.firstName = "One"
        personIdentificationName1.middleName = null
        personIdentificationName1.lastName = "UnitTest"
        personIdentificationName1.bannerId = "UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals personIdentificationName1.firstName + " " + personIdentificationName1.lastName, personIdentificationName1.fullName
        def personIdentificationName2 = newPersonIdentificationName()
        personIdentificationName2.firstName = "Second"
        personIdentificationName2.middleName = "MI"
        personIdentificationName2.lastName = "UnitTest"
        personIdentificationName2.bannerId = "UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals personIdentificationName2.firstName + " " + personIdentificationName2.middleName + " " + personIdentificationName2.lastName, personIdentificationName2.fullName
        def searchString = "unittest"
        def invalidSearchString = "T!!!@@@"

        def personCount = PersonIdentificationName.fetchCountPersonByBannerId(searchString)
        assertNotNull personCount
        assertTrue personCount > 0

        personCount = PersonIdentificationName.fetchCountPersonByBannerId(invalidSearchString)
        assertFalse personCount > 0

        invalidSearchString = null
        personCount = PersonIdentificationName.fetchCountPersonByNameOrBannerId(invalidSearchString)
        assertFalse personCount > 0
    }


    @Test
    void testFetchCountPersonByLastName() {
        def personIdentificationName1 = newPersonIdentificationName()
        personIdentificationName1.firstName = "One"
        personIdentificationName1.middleName = null
        personIdentificationName1.lastName = "UnitTest"
        personIdentificationName1.bannerId = "UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals personIdentificationName1.firstName + " " + personIdentificationName1.lastName, personIdentificationName1.fullName
        def personIdentificationName2 = newPersonIdentificationName()
        personIdentificationName2.firstName = "Second"
        personIdentificationName2.middleName = "MI"
        personIdentificationName2.lastName = "UnitTest"
        personIdentificationName2.bannerId = "UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals personIdentificationName2.firstName + " " + personIdentificationName2.middleName + " " + personIdentificationName2.lastName, personIdentificationName2.fullName
        def searchString = "unittest"
        def invalidSearchString = "T!!!@@@"

        def personCount = PersonIdentificationName.fetchCountPersonByLastName(searchString)
        assertNotNull personCount
        assertTrue personCount > 0

        personCount = PersonIdentificationName.fetchCountPersonByLastName(invalidSearchString)
        assertFalse personCount > 0

        invalidSearchString = null
        personCount = PersonIdentificationName.fetchCountPersonByLastName(invalidSearchString)
        assertFalse personCount > 0
    }


    @Test
    void testFetchCountNonPersonByNameOrBannerId() {
        def personIdentificationName1 = newNonPersonIdentificationName("FIRSTCOMPANY")
        personIdentificationName1.bannerId = "UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals "FIRSTCOMPANY", personIdentificationName1.fullName
        def personIdentificationName2 = newNonPersonIdentificationName("SECONDCOMPANY")
        personIdentificationName2.bannerId = "UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals "SECONDCOMPANY", personIdentificationName2.fullName
        def searchString = "unittest"
        def invalidSearchString = "T!!!@@@"

        def nonPersonCount = PersonIdentificationName.fetchCountNonPersonByNameOrBannerId(searchString)
        assertNotNull nonPersonCount
        assertTrue nonPersonCount > 0

        nonPersonCount = PersonIdentificationName.fetchCountNonPersonByNameOrBannerId(invalidSearchString)
        assertFalse nonPersonCount > 0

        invalidSearchString = null
        nonPersonCount = PersonIdentificationName.fetchCountNonPersonByNameOrBannerId(invalidSearchString)
        assertFalse nonPersonCount > 0

    }


    @Test
    void testFetchCountNonPersonByBannerId() {
        def personIdentificationName1 = newNonPersonIdentificationName("FIRSTCOMPANY")
        personIdentificationName1.bannerId = "UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals "FIRSTCOMPANY", personIdentificationName1.fullName
        def personIdentificationName2 = newNonPersonIdentificationName("SECONDCOMPANY")
        personIdentificationName2.bannerId = "UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals "SECONDCOMPANY", personIdentificationName2.fullName
        def searchString = "unittest"
        def invalidSearchString = "T!!!@@@"

        def nonPersonCount = PersonIdentificationName.fetchCountNonPersonByBannerId(searchString)
        assertNotNull nonPersonCount
        assertTrue nonPersonCount > 0

        nonPersonCount = PersonIdentificationName.fetchCountNonPersonByBannerId(invalidSearchString)
        assertFalse nonPersonCount > 0

        invalidSearchString = null
        nonPersonCount = PersonIdentificationName.fetchCountNonPersonByBannerId(invalidSearchString)
        assertFalse nonPersonCount > 0

    }


    @Test
    void testFetchCountNonPersonByLastName() {
        def personIdentificationName1 = newNonPersonIdentificationName("FIRSTCOMPANY")
        personIdentificationName1.bannerId = "UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals "FIRSTCOMPANY", personIdentificationName1.fullName
        def personIdentificationName2 = newNonPersonIdentificationName("SECONDCOMPANY")
        personIdentificationName2.bannerId = "UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals "SECONDCOMPANY", personIdentificationName2.fullName
        def searchString = "SECOND"
        def invalidSearchString = "T!!!@@@"

        def nonPersonCount = PersonIdentificationName.fetchCountNonPersonByLastName(searchString)
        assertNotNull nonPersonCount
        assertTrue nonPersonCount > 0

        nonPersonCount = PersonIdentificationName.fetchCountNonPersonByLastName(invalidSearchString)
        assertFalse nonPersonCount > 0

        invalidSearchString = null
        nonPersonCount = PersonIdentificationName.fetchCountNonPersonByLastName(invalidSearchString)
        assertFalse nonPersonCount > 0
    }


    @Test
    void testFetchBannerPersonOrNonPerson() {
        def personIdentificationName1 = newNonPersonIdentificationName("FIRSTCOMPANY")
        personIdentificationName1.bannerId = "UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals "FIRSTCOMPANY", personIdentificationName1.fullName
        def personIdentificationName2 = newNonPersonIdentificationName("SECONDCOMPANY")
        personIdentificationName2.bannerId = "UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals "SECONDCOMPANY", personIdentificationName2.fullName
        def searchString = "UNITTEST1"
        def invalidSearchString = "T!!!@@@"
        def result = PersonIdentificationName.fetchBannerPersonOrNonPerson(searchString)
        assertNotNull result
        assertTrue result.equals(personIdentificationName1)
        assertFalse result.equals(personIdentificationName2)

        searchString = "UNITTEST2"
        result = PersonIdentificationName.fetchBannerPersonOrNonPerson(searchString)
        assertNotNull result
        assertFalse result.equals(personIdentificationName1)
        assertTrue result.equals(personIdentificationName2)

        assertTrue PersonIdentificationName.fetchBannerPersonOrNonPerson(invalidSearchString) == null
    }


    @Test
    void testFetchByName() {
        def personIdentificationName1 = newPersonIdentificationName()
        personIdentificationName1.firstName = "One"
        personIdentificationName1.middleName = null
        personIdentificationName1.lastName = "UnitTest"
        personIdentificationName1.bannerId = "UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals personIdentificationName1.firstName + " " + personIdentificationName1.lastName, personIdentificationName1.fullName
        def personIdentificationName2 = newPersonIdentificationName()
        personIdentificationName2.firstName = "Second"
        personIdentificationName2.middleName = "MI"
        personIdentificationName2.lastName = "UnitTest"
        personIdentificationName2.bannerId = "UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals personIdentificationName2.firstName + " " + personIdentificationName2.middleName + " " + personIdentificationName2.lastName, personIdentificationName2.fullName
        def searchString = "unittest"
        def invalidSearchString = "T!!!@@@"

        def results = PersonIdentificationName.fetchByName(searchString)
        assertNotNull results
        assertTrue results.contains(personIdentificationName1)
        assertTrue results.contains(personIdentificationName2)

        assertTrue PersonIdentificationName.fetchByName(invalidSearchString)?.isEmpty()
    }


    @Test
    void testFetchPersonBySoundexName() {
        def personIdentificationName1 = newPersonIdentificationName()
        personIdentificationName1.firstName = "One"
        personIdentificationName1.lastName = "UnitTest"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id

        def personIdentificationName2 = newPersonIdentificationName()
        personIdentificationName2.firstName = "One"
        personIdentificationName2.lastName = "UnitTest2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id

        def results = PersonIdentificationName.fetchPersonBySoundexName("UnitTest", "One")
        assertNotNull results
        assertEquals results?.size(), 2
    }


    @Test
    void testFetchNonPersonBySoundexName() {
        def personIdentificationName1 = newPersonIdentificationName()
        personIdentificationName1.firstName = null
        personIdentificationName1.lastName = "UnitTest"
        personIdentificationName1.entityIndicator = "C"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id

        def personIdentificationName2 = newPersonIdentificationName()
        personIdentificationName2.firstName = null
        personIdentificationName2.lastName = "UnitTest2"
        personIdentificationName2.entityIndicator = "C"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id

        def results = PersonIdentificationName.fetchNonPersonBySoundexName("UnitTest")
        assertNotNull results
        assertEquals results?.size(), 2
    }


    @Test
    void testBannerIdOrNameExists() {
        def person = newPersonIdentificationName()
        person.save(flush: true, failOnError: true)

        assertTrue PersonIdentificationName.bannerIdOrNameExists(
                person.pidm, person.bannerId,
                person.lastName, person.firstName, person.middleName,
                person.nameType.code, person.surnamePrefix)

        assertFalse PersonIdentificationName.bannerIdOrNameExists(
                person.pidm, person.bannerId,
                person.lastName, person.firstName, person.middleName,
                person.nameType.code, "X")
    }


    @Test
    void testFetchPersonCurrentRecord() {
        // you cannot update the spriden ID or name using the sv_spriden
        def sql = new Sql(sessionFactory.getCurrentSession().connection())

        def idSql = """select gb_common.f_generate_id bannerId from dual"""
        def firstBannerId = sql.firstRow(idSql).bannerId
        assertNotNull firstBannerId

        def secondBannerId = "TTT001"

        def bannerPidm = null
        sql.call("""
         declare

         Lv_Id_Ref Gb_Identification.Identification_Ref;
         spriden_current Gb_Identification.identification_rec;
         test_pidm spriden.spriden_pidm%type;
         test_rowid varchar2(30);
         begin

         gb_identification.p_create(
         P_ID_INOUT => ${firstBannerId},
         P_LAST_NAME => 'Miller',
         P_FIRST_NAME => 'Ann',
         P_MI => 'Elizabeth',
         P_CHANGE_IND => NULL,
         P_ENTITY_IND => 'P',
         P_User => User,
         P_ORIGIN => 'banner',
         P_NTYP_CODE => NULL,
         P_DATA_ORIGIN => 'banner',
         P_PIDM_INOUT => test_pidm,
         P_Rowid_Out => Test_Rowid);

         Lv_Id_Ref := Gb_Identification.F_Query_One(test_Pidm);
         Fetch Lv_Id_Ref Into spriden_current;
         CLOSE lv_id_ref;

         GB_IDENTIFICATION.P_UPDATE(p_PIDM => test_pidm,
         p_ID => ${secondBannerId},
         p_LAST_NAME => spriden_current.r_LAST_NAME,
         p_FIRST_NAME => spriden_current.r_FIRST_NAME,
         p_MI => spriden_current.r_MI,
         p_CHANGE_IND => spriden_current.r_CHANGE_IND,
         p_ENTITY_IND => spriden_current.r_ENTITY_IND,
         p_USER =>user,
         P_Ntyp_Code => Spriden_Current.r_Ntyp_Code,
         p_DATA_ORIGIN =>'banner',
         p_SURNAME_PREFIX => spriden_current.r_SURNAME_PREFIX,
         P_Origin => Spriden_Current.R_Origin,
         P_Rowid => Spriden_Current.r_internal_record_id);

        ${Sql.VARCHAR} := test_pidm ;
         end ;
         """) {output_info -> bannerPidm = output_info}

        def currentPerson = PersonIdentificationName.fetchPersonCurrentRecord(firstBannerId)
        assertNotNull currentPerson
        assertEquals Integer.valueOf(bannerPidm), currentPerson.pidm
        assertEquals secondBannerId, currentPerson.bannerId
    }


    private def newPersonIdentificationName() {
        def inameType = NameType.findWhere(code: "BRTH")

        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
        def bannerValues = sql.firstRow(idSql)
        def ibannerId = bannerValues.bannerId
        def ipidm = bannerValues.pidm

        // leave the pidm null to get a generated on, and the ID equal to GENERATED to get a newly
        // generated ID
        def personIdentificationName = new PersonIdentificationName(
                pidm: ipidm,
                bannerId: ibannerId,
                lastName: "TTTTT",
                firstName: "TTTTT",
                middleName: "TTTTT",
                changeIndicator: null,
                entityIndicator: "P",
                nameType: inameType
        )
        return personIdentificationName
    }


    private def newNonPersonIdentificationName(name) {
        def inameType = NameType.findWhere(code: "BRTH")

        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
        def bannerValues = sql.firstRow(idSql)
        def ibannerId = bannerValues.bannerId
        def ipidm = bannerValues.pidm

        // leave the pidm null to get a generated on, and the ID equal to GENERATED to get a newly
        // generated ID
        def personIdentificationName = new PersonIdentificationName(
                pidm: ipidm,
                bannerId: ibannerId,
                lastName: name,
                changeIndicator: null,
                entityIndicator: "C",
                nameType: inameType,
                lastModifiedBy: "grails"
        )
        return personIdentificationName
    }


    @Test
    void testAltId() {
        // you cannot update the spriden ID or name using the sv_spriden
        // you need to use the API. The reason is the sv_spriden trigger sets the surrogate ID
        // the API to update will insert a new spriden with the new ID and the surrogate ID is not set correctly
        // create the new ID using pl/sql block
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        def idSql = """select gb_common.f_generate_id bannerId from dual"""
        def bannerId = sql.firstRow(idSql).bannerId
        assertNotNull bannerId
        def bannerPidm = null

        sql.call("""
         declare

         Lv_Id_Ref Gb_Identification.Identification_Ref;
         spriden_current Gb_Identification.identification_rec;
         test_pidm spriden.spriden_pidm%type;
         test_rowid varchar2(30);
         begin

         gb_identification.p_create(
         P_ID_INOUT => ${bannerId},
         P_LAST_NAME => 'Miller',
         P_FIRST_NAME => 'Ann',
         P_MI => 'Elizabeth',
         P_CHANGE_IND => NULL,
         P_ENTITY_IND => 'P',
         P_User => User,
         P_ORIGIN => 'banner',
         P_NTYP_CODE => NULL,
         P_DATA_ORIGIN => 'banner',
         P_PIDM_INOUT => test_pidm,
         P_Rowid_Out => Test_Rowid);

         Lv_Id_Ref := Gb_Identification.F_Query_One(test_Pidm);
         Fetch Lv_Id_Ref Into spriden_current;
         CLOSE lv_id_ref;

         GB_IDENTIFICATION.P_UPDATE(p_PIDM => test_pidm,
         p_ID =>'TTT001',
         p_LAST_NAME => spriden_current.r_LAST_NAME,
         p_FIRST_NAME => spriden_current.r_FIRST_NAME,
         p_MI => spriden_current.r_MI,
         p_CHANGE_IND => spriden_current.r_CHANGE_IND,
         p_ENTITY_IND => spriden_current.r_ENTITY_IND,
         p_USER =>user,
         P_Ntyp_Code => Spriden_Current.r_Ntyp_Code,
         p_DATA_ORIGIN =>'banner',
         p_SURNAME_PREFIX => spriden_current.r_SURNAME_PREFIX,
         P_Origin => Spriden_Current.R_Origin,
         P_Rowid => Spriden_Current.r_internal_record_id);

        ${Sql.VARCHAR} := test_pidm ;
         end ;
         """) {output_info -> bannerPidm = output_info}

        def persons = PersonIdentificationName.findAllByPidm(bannerPidm)
        assertEquals 2, persons.size()
        // this is the alternative id
        def alternativePerson = persons.find { it.changeIndicator == "I"}
        assertNotNull alternativePerson.pidm
        assertEquals bannerId, alternativePerson.bannerId
        // this is the current ID
        def currentPerson = persons.find { it.changeIndicator == null}
        assertNotNull currentPerson.pidm
        assertEquals "TTT001", currentPerson.bannerId

    }


    @Test
    void testFetchBannerPerson() {
        def person = newPersonIdentificationName()
        person.firstName = "test"
        person.lastName = "testLast"
        save person
        person.refresh()
        def result = PersonIdentificationName.fetchBannerPerson(person.pidm)

        assertNotNull result
        assertTrue result instanceof PersonIdentificationName
    }


    @Test
    void testFetchBannerPersonList() {
        def personIdentificationNameList = []
        (0..5).each {
            def person = newPersonIdentificationName()
            person.save(failOnError: true, flush: true)
            personIdentificationNameList.add(person)
        }
        def persons = PersonIdentificationName.fetchBannerPersonList(personIdentificationNameList.pidm)

        assertFalse persons.empty
        assertTrue persons.size() > 1
        assertTrue persons[0] instanceof PersonIdentificationName
    }


    @Test
    void testFetchPIDNameByValidGuid(){
        def person=GlobalUniqueIdentifier.fetchByLdmName('persons')[0]
        assertNotNull person
        def guid=person.guid
        assertNotNull guid
        def pid=PersonIdentificationName.getPersonIdentificationNameCurrentByGUID(guid)
        assertNotNull pid
    }

    @Test
    void testFetchPIDNameByInvalidGuid(){
        shouldFail(ApplicationException) {
            PersonIdentificationName.getPersonIdentificationNameCurrentByGUID("Invalid-Guid")
        }
    }
}
