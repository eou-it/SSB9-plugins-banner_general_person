/** *****************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */

package com.sungardhe.banner.general.person

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.general.person.PersonIdentificationName

import grails.test.GrailsUnitTestCase
import groovy.sql.Sql
import org.hibernate.annotations.OptimisticLock
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import com.sungardhe.banner.general.system.NameType
import com.sungardhe.banner.exceptions.ApplicationException


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

//  TODO:  this will require changes to the IDEN API
//    void testUpdatePersonIdentificationName() {
//        def personIdentificationName = newPersonIdentificationName()
//        personIdentificationName.save(flush: true)
//
//        assertNotNull personIdentificationName.id
//        assertEquals 0L, personIdentificationName.version
//        assertEquals "TTTTT", personIdentificationName.lastName
//        assertEquals "TTTTT", personIdentificationName.firstName
//        assertEquals "TTTTT", personIdentificationName.middleName
//        assertNull personIdentificationName.changeIndicator
//        assertEquals "P", personIdentificationName.entityIndicator
//
//        //Update the entity
//        def testDate = new Date()
//        personIdentificationName.lastName = "UUUUU"
//        personIdentificationName.firstName = "UUUUU"
//        personIdentificationName.middleName = "UUUUU"
//        personIdentificationName.changeIndicator = "N"
//        save personIdentificationName
//
//        personIdentificationName = PersonIdentificationName.get(personIdentificationName.id)
//        assertEquals 1L, personIdentificationName?.version
//        assertEquals "UUUUU", personIdentificationName.lastName
//        assertEquals "UUUUU", personIdentificationName.firstName
//        assertEquals "UUUUU", personIdentificationName.middleName
//        assertEquals "N", personIdentificationName.changeIndicator
//        assertEquals "P", personIdentificationName.entityIndicator
//    }
//
//
//    void testOptimisticLock() {
//        def personIdentificationName = newPersonIdentificationName()
//        personIdentificationName.save(flush: true)
//
//        def sql
//        try {
//            sql = new Sql(sessionFactory.getCurrentSession().connection())
//            sql.executeUpdate("update SV_SPRIDEN set SPRIDEN_VERSION = 999 where SPRIDEN_SURROGATE_ID = ?", [personIdentificationName.id])
//        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
//        }
//        //Try to update the entity
//        personIdentificationName.lastName = "UUUUU"
//        personIdentificationName.firstName = "UUUUU"
//        personIdentificationName.middleName = "UUUUU"
//        personIdentificationName.changeIndicator = "N"
//        shouldFail(HibernateOptimisticLockingFailureException) {
//            personIdentificationName.save(flush: true)
//        }
//    }


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

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personidentificationname_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
