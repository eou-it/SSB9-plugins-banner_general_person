/** *******************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of
 SunGard Higher Education and its subsidiaries. Any use of this software is limited
 solely to SunGard Higher Education licensees, and is further subject to the terms
 and conditions of one or more written license agreements between SunGard Higher
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher
 Education in the U.S.A. and/or other regions and/or countries.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql

class PersonIdentificationNameIntegrationTests extends BaseIntegrationTestCase {

    def personIdentificationNameService


    protected void setUp() {
        formContext = ['SOAIDEN'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreatePersonIdentificationName() {
        def personIdentificationName = newPersonIdentificationName()
        save personIdentificationName
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName.id
    }


    void testDeletePersonIdentificationName() {
        def personIdentificationName = newPersonIdentificationName()
        save personIdentificationName

        def newPersonIdentification = new PersonIdentificationName(personIdentificationName.properties)
        newPersonIdentification.changeIndicator = "N"
        newPersonIdentification.lastName = "YYYYYYYY"
        save newPersonIdentification
        assertNotNull newPersonIdentification.id

        newPersonIdentification.delete()

    }


    void testValidation() {
        def personIdentificationName = newPersonIdentificationName()
        assertTrue "PersonIdentificationName could not be validated as expected due to ${personIdentificationName.errors}", personIdentificationName.validate()
    }


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
                nameType: inameType,
                lastModifiedBy: "grails"
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

    void testUpdateNameType() {
        // Name type is the only field that can be updated on SPRIDEN without causing an alternate id
        // to be created.  That functionality is tested in the service integration test.
        // For this test we just update the name type.

        def personIdentificationName = newPersonIdentificationName()
        save personIdentificationName
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName.id


        personIdentificationName.nameType = NameType.findWhere(code: "PROF")
        save personIdentificationName

        def updated = personIdentificationName.get(personIdentificationName.id)
        assertEquals new Long(1), updated.version
        assertEquals "PROF",  updated.nameType.code
    }
}
