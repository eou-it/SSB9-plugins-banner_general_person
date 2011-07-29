/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */

package com.sungardhe.banner.general.person

import com.sungardhe.banner.general.system.NameType
import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql

class PersonIdentificationNameIntegrationTests extends BaseIntegrationTestCase {

    def personIdentificationNameService


    protected void setUp() {
        formContext = ['SPAIDEN'] // Since we are not testing a controller, we need to explicitly set this
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


    void testValidationMessages() {
        def personIdentificationName = newPersonIdentificationName()
        personIdentificationName.lastName = null
        assertFalse personIdentificationName.validate()
        assertLocalizedError personIdentificationName, 'nullable', /.*Field.*lastName.*of class.*PersonIdentificationName.*cannot be null.*/, 'lastName'
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


    void testFetchAllPersonByNameOrBannerId() {
        def personIdentificationName1 = newPersonIdentificationName()
        personIdentificationName1.firstName="One"
        personIdentificationName1.middleName=null
        personIdentificationName1.lastName="UnitTest"
        personIdentificationName1.bannerId="UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals  personIdentificationName1.firstName+" "+personIdentificationName1.lastName, personIdentificationName1.fullName
        def personIdentificationName2 = newPersonIdentificationName()
        personIdentificationName2.firstName="Second"
        personIdentificationName2.middleName="MI"
        personIdentificationName2.lastName="UnitTest"
        personIdentificationName2.bannerId="UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals  personIdentificationName2.firstName+" "+personIdentificationName2.middleName+" "+personIdentificationName2.lastName, personIdentificationName2.fullName
        def searchString = "unittest"
        def invalidSearchString = "T!!!@@@"
         def pagingAndSortParams = [max:10, offset:0]
        def results = PersonIdentificationName.fetchAllPersonByNameOrBannerId(searchString,pagingAndSortParams)
        assertNotNull results
        assertTrue results.contains(personIdentificationName1)
        assertTrue results.contains(personIdentificationName2)
        assertTrue PersonIdentificationName.fetchAllPersonByNameOrBannerId(invalidSearchString,pagingAndSortParams)?.isEmpty()
    }

    void testFetchAllNonPersonByNameOrBannerId() {
        def personIdentificationName1 = newNonPersonIdentificationName("FIRSTCOMPANY")
        personIdentificationName1.bannerId="UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals  "FIRSTCOMPANY", personIdentificationName1.fullName
        def personIdentificationName2 = newNonPersonIdentificationName("SECONDCOMPANY")
        personIdentificationName2.bannerId="UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals  "SECONDCOMPANY", personIdentificationName2.fullName
        def searchString = "unittest"
        def invalidSearchString = "T!!!@@@"
         def pagingAndSortParams = [max:10, offset:0]
        def results = PersonIdentificationName.fetchAllNonPersonByNameOrBannerId(searchString,pagingAndSortParams)
        assertNotNull results
        assertTrue results.contains(personIdentificationName1)
        assertTrue results.contains(personIdentificationName2)

        searchString = "first"
        results = PersonIdentificationName.fetchAllNonPersonByNameOrBannerId(searchString,pagingAndSortParams)
        assertNotNull results
        assertTrue results.contains(personIdentificationName1)
        assertFalse results.contains(personIdentificationName2)

        assertTrue PersonIdentificationName.fetchAllNonPersonByNameOrBannerId(invalidSearchString,pagingAndSortParams)?.isEmpty()
    }

     void testFetchBannerPersonOrNonPerson() {
        def personIdentificationName1 = newNonPersonIdentificationName("FIRSTCOMPANY")
        personIdentificationName1.bannerId="UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals  "FIRSTCOMPANY", personIdentificationName1.fullName
        def personIdentificationName2 = newNonPersonIdentificationName("SECONDCOMPANY")
        personIdentificationName2.bannerId="UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals  "SECONDCOMPANY", personIdentificationName2.fullName
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
        personIdentificationName1.firstName="One"
        personIdentificationName1.middleName=null
        personIdentificationName1.lastName="UnitTest"
        personIdentificationName1.bannerId="UNITTEST1"
        save personIdentificationName1
        //This is required to compute grails derived columns
        personIdentificationName1.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName1.id
        assertEquals  personIdentificationName1.firstName+" "+personIdentificationName1.lastName, personIdentificationName1.fullName
        def personIdentificationName2 = newPersonIdentificationName()
        personIdentificationName2.firstName="Second"
        personIdentificationName2.middleName="MI"
        personIdentificationName2.lastName="UnitTest"
        personIdentificationName2.bannerId="UNITTEST2"
        save personIdentificationName2
        //This is required to compute grails derived columns
        personIdentificationName2.refresh()
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName2.id
        assertEquals  personIdentificationName2.firstName+" "+personIdentificationName2.middleName+" "+personIdentificationName2.lastName, personIdentificationName2.fullName
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

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personidentificationname_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
