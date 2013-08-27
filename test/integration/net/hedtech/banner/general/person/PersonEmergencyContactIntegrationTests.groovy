/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
package net.hedtech.banner.general.person

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.general.system.AddressType
import net.hedtech.banner.general.system.Nation
import net.hedtech.banner.general.system.Relationship
import net.hedtech.banner.general.system.State
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class PersonEmergencyContactIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)
    def i_success_state
    def i_success_nation
    def i_success_relationship
    def i_success_addressType

    def i_success_priority = "1"
    def i_success_lastName = "TTTTT"
    def i_success_firstName = "TTTTT"
    def i_success_middleInitial = "TTTTT"
    def i_success_streetLine1 = "TTTTT"
    def i_success_streetLine2 = "TTTTT"
    def i_success_streetLine3 = "TTTTT"
    def i_success_city = "TTTTT"
    def i_success_zip = "TTTTT"
    def i_success_phoneArea = "TTTTT"
    def i_success_phoneNumber = "TTTTT"
    def i_success_phoneExtension = "TTTTT"
    def i_success_surnamePrefix = "TTTTT"
    def i_success_countryPhone = "TTTT"
    def i_success_houseNumber = "TTTTT"
    def i_success_streetLine4 = "TTTTT"
    //Invalid test data (For failure tests)
    def i_failure_state
    def i_failure_nation
    def i_failure_relationship
    def i_failure_addressType

    def i_failure_priority = "1"
    def i_failure_lastName = "TTTTT"
    def i_failure_firstName = "TTTTT"
    def i_failure_middleInitial = "TTTTT"
    def i_failure_streetLine1 = "TTTTT"
    def i_failure_streetLine2 = "TTTTT"
    def i_failure_streetLine3 = "TTTTT"
    def i_failure_city = "TTTTT"
    def i_failure_zip = "TTTTT"
    def i_failure_phoneArea = "TTTTTTT"
    def i_failure_phoneNumber = "TTTTT"
    def i_failure_phoneExtension = "TTTTT"
    def i_failure_surnamePrefix = "TTTTT"
    def i_failure_countryPhone = "TTTT"
    def i_failure_houseNumber = "TTTTT"
    def i_failure_streetLine4 = "TTTTT"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)
    def u_success_state
    def u_success_nation
    def u_success_relationship
    def u_success_addressType

    def u_success_priority = "1"
    def u_success_lastName = "STTTT"
    def u_success_firstName = "TTTTT"
    def u_success_middleInitial = "TTTTT"
    def u_success_streetLine1 = "TTTTT"
    def u_success_streetLine2 = "TTTTT"
    def u_success_streetLine3 = "TTTTT"
    def u_success_city = "TTTTT"
    def u_success_zip = "TTTTT"
    def u_success_phoneArea = "TTTTT"
    def u_success_phoneNumber = "TTTTT"
    def u_success_phoneExtension = "TTTTT"
    def u_success_surnamePrefix = "TTTTT"
    def u_success_countryPhone = "TTTT"
    def u_success_houseNumber = "TTTTT"
    def u_success_streetLine4 = "TTTTT"
    //Valid test data (For failure tests)
    def u_failure_state
    def u_failure_nation
    def u_failure_relationship
    def u_failure_addressType

    def u_failure_priority = "1"
    def u_failure_lastName = "TTTTT"
    def u_failure_firstName = "TTTTT"
    def u_failure_middleInitial = "TTTTT"
    def u_failure_streetLine1 = "TTTTT"
    def u_failure_streetLine2 = "TTTTT"
    def u_failure_streetLine3 = "TTTTT"
    def u_failure_city = "TTTTT"
    def u_failure_zip = "TTTTT"
    def u_failure_phoneArea = "TTTTTTT"
    def u_failure_phoneNumber = "TTTTT"
    def u_failure_phoneExtension = "TTTTT"
    def u_failure_surnamePrefix = "TTTTT"
    def u_failure_countryPhone = "TTTT"
    def u_failure_houseNumber = "TTTTT"
    def u_failure_streetLine4 = "TTTTT"


    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction
    void initializeTestDataForReferences() {
        //Valid test data (For success tests)
        i_success_state = State.findByCode("MER")
        i_success_nation = Nation.findByCode("157")
        i_success_relationship = Relationship.findByCode("M")
        i_success_addressType = AddressType.findByCode("PO")

        //Invalid test data (For failure tests)
        i_failure_state = State.findByCode("GLO")
        i_failure_nation = Nation.findByCode("40")
        i_failure_relationship = Relationship.findByCode("Y")
        i_failure_addressType = AddressType.findByCode("GR")

        //Valid test data (For success tests)
        u_success_state = State.findByCode("MER")
        u_success_nation = Nation.findByCode("157")
        u_success_relationship = Relationship.findByCode("M")
        u_success_addressType = AddressType.findByCode("PO")

        //Valid test data (For failure tests)
        u_failure_state = State.findByCode("GLO")
        u_failure_nation = Nation.findByCode("40")
        u_failure_relationship = Relationship.findByCode("Y")
        u_failure_addressType = AddressType.findByCode("GR")
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidPersonEmergencyContact() {
        def personEmergencyContact = newValidForCreatePersonEmergencyContact()
        personEmergencyContact.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull personEmergencyContact.id
    }


    void testCreateInvalidPersonEmergencyContact() {
        def personEmergencyContact = newInvalidForCreatePersonEmergencyContact()
        shouldFail(ValidationException) {
            personEmergencyContact.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidPersonEmergencyContact() {
        def personEmergencyContact = newValidForCreatePersonEmergencyContact()
        personEmergencyContact.save(failOnError: true, flush: true)
        assertNotNull personEmergencyContact.id
        assertEquals 0L, personEmergencyContact.version
        assertEquals i_success_priority, personEmergencyContact.priority
        assertEquals i_success_lastName, personEmergencyContact.lastName
        assertEquals i_success_firstName, personEmergencyContact.firstName
        assertEquals i_success_middleInitial, personEmergencyContact.middleInitial
        assertEquals i_success_streetLine1, personEmergencyContact.streetLine1
        assertEquals i_success_streetLine2, personEmergencyContact.streetLine2
        assertEquals i_success_streetLine3, personEmergencyContact.streetLine3
        assertEquals i_success_city, personEmergencyContact.city
        assertEquals i_success_zip, personEmergencyContact.zip
        assertEquals i_success_phoneArea, personEmergencyContact.phoneArea
        assertEquals i_success_phoneNumber, personEmergencyContact.phoneNumber
        assertEquals i_success_phoneExtension, personEmergencyContact.phoneExtension
        assertEquals i_success_surnamePrefix, personEmergencyContact.surnamePrefix
        assertEquals i_success_countryPhone, personEmergencyContact.countryPhone
        assertEquals i_success_houseNumber, personEmergencyContact.houseNumber
        assertEquals i_success_streetLine4, personEmergencyContact.streetLine4

        //Update the entity
        personEmergencyContact.lastName = u_success_lastName
        personEmergencyContact.firstName = u_success_firstName
        personEmergencyContact.middleInitial = u_success_middleInitial
        personEmergencyContact.streetLine1 = u_success_streetLine1
        personEmergencyContact.streetLine2 = u_success_streetLine2
        personEmergencyContact.streetLine3 = u_success_streetLine3
        personEmergencyContact.city = u_success_city
        personEmergencyContact.zip = u_success_zip
        personEmergencyContact.phoneArea = u_success_phoneArea
        personEmergencyContact.phoneNumber = u_success_phoneNumber
        personEmergencyContact.phoneExtension = u_success_phoneExtension
        personEmergencyContact.surnamePrefix = u_success_surnamePrefix
        personEmergencyContact.countryPhone = u_success_countryPhone
        personEmergencyContact.houseNumber = u_success_houseNumber
        personEmergencyContact.streetLine4 = u_success_streetLine4
        personEmergencyContact.state = u_success_state
        personEmergencyContact.nation = u_success_nation
        personEmergencyContact.relationship = u_success_relationship
        personEmergencyContact.addressType = u_success_addressType

        personEmergencyContact.save(failOnError: true, flush: true)
        //Assert for sucessful update
        personEmergencyContact = PersonEmergencyContact.get(personEmergencyContact.id)
        assertEquals 1L, personEmergencyContact?.version
        assertEquals u_success_lastName, personEmergencyContact.lastName
        assertEquals u_success_firstName, personEmergencyContact.firstName
        assertEquals u_success_middleInitial, personEmergencyContact.middleInitial
        assertEquals u_success_streetLine1, personEmergencyContact.streetLine1
        assertEquals u_success_streetLine2, personEmergencyContact.streetLine2
        assertEquals u_success_streetLine3, personEmergencyContact.streetLine3
        assertEquals u_success_city, personEmergencyContact.city
        assertEquals u_success_zip, personEmergencyContact.zip
        assertEquals u_success_phoneArea, personEmergencyContact.phoneArea
        assertEquals u_success_phoneNumber, personEmergencyContact.phoneNumber
        assertEquals u_success_phoneExtension, personEmergencyContact.phoneExtension
        assertEquals u_success_surnamePrefix, personEmergencyContact.surnamePrefix
        assertEquals u_success_countryPhone, personEmergencyContact.countryPhone
        assertEquals u_success_houseNumber, personEmergencyContact.houseNumber
        assertEquals u_success_streetLine4, personEmergencyContact.streetLine4

        personEmergencyContact.state = u_success_state
        personEmergencyContact.nation = u_success_nation
        personEmergencyContact.relationship = u_success_relationship
        personEmergencyContact.addressType = u_success_addressType
    }


    void testUpdateInvalidPersonEmergencyContact() {
        def personEmergencyContact = newValidForCreatePersonEmergencyContact()
        personEmergencyContact.save(failOnError: true, flush: true)
        assertNotNull personEmergencyContact.id
        assertEquals 0L, personEmergencyContact.version
        assertEquals i_success_priority, personEmergencyContact.priority
        assertEquals i_success_lastName, personEmergencyContact.lastName
        assertEquals i_success_firstName, personEmergencyContact.firstName
        assertEquals i_success_middleInitial, personEmergencyContact.middleInitial
        assertEquals i_success_streetLine1, personEmergencyContact.streetLine1
        assertEquals i_success_streetLine2, personEmergencyContact.streetLine2
        assertEquals i_success_streetLine3, personEmergencyContact.streetLine3
        assertEquals i_success_city, personEmergencyContact.city
        assertEquals i_success_zip, personEmergencyContact.zip
        assertEquals i_success_phoneArea, personEmergencyContact.phoneArea
        assertEquals i_success_phoneNumber, personEmergencyContact.phoneNumber
        assertEquals i_success_phoneExtension, personEmergencyContact.phoneExtension
        assertEquals i_success_surnamePrefix, personEmergencyContact.surnamePrefix
        assertEquals i_success_countryPhone, personEmergencyContact.countryPhone
        assertEquals i_success_houseNumber, personEmergencyContact.houseNumber
        assertEquals i_success_streetLine4, personEmergencyContact.streetLine4

        //Update the entity with invalid values
        personEmergencyContact.lastName = u_failure_lastName
        personEmergencyContact.firstName = u_failure_firstName
        personEmergencyContact.middleInitial = u_failure_middleInitial
        personEmergencyContact.streetLine1 = u_failure_streetLine1
        personEmergencyContact.streetLine2 = u_failure_streetLine2
        personEmergencyContact.streetLine3 = u_failure_streetLine3
        personEmergencyContact.city = u_failure_city
        personEmergencyContact.zip = u_failure_zip
        personEmergencyContact.phoneArea = u_failure_phoneArea
        personEmergencyContact.phoneNumber = u_failure_phoneNumber
        personEmergencyContact.phoneExtension = u_failure_phoneExtension
        personEmergencyContact.surnamePrefix = u_failure_surnamePrefix
        personEmergencyContact.countryPhone = u_failure_countryPhone
        personEmergencyContact.houseNumber = u_failure_houseNumber
        personEmergencyContact.streetLine4 = u_failure_streetLine4


        personEmergencyContact.state = u_failure_state

        personEmergencyContact.nation = u_failure_nation

        personEmergencyContact.relationship = u_failure_relationship

        personEmergencyContact.addressType = u_failure_addressType
        shouldFail(ValidationException) {
            personEmergencyContact.save(failOnError: true, flush: true)
        }
    }


    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def personEmergencyContact = newValidForCreatePersonEmergencyContact()



        personEmergencyContact.save(flush: true, failOnError: true)
        personEmergencyContact.refresh()
        assertNotNull "PersonEmergencyContact should have been saved", personEmergencyContact.id

        // test date values -
        assertEquals date.format(today), date.format(personEmergencyContact.lastModified)
        assertEquals hour.format(today), hour.format(personEmergencyContact.lastModified)


    }


    void testOptimisticLock() {
        def personEmergencyContact = newValidForCreatePersonEmergencyContact()
        personEmergencyContact.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SV_SPREMRG set SPREMRG_VERSION = 999 where SPREMRG_SURROGATE_ID = ?", [personEmergencyContact.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        personEmergencyContact.lastName = u_success_lastName
        personEmergencyContact.firstName = u_success_firstName
        personEmergencyContact.middleInitial = u_success_middleInitial
        personEmergencyContact.streetLine1 = u_success_streetLine1
        personEmergencyContact.streetLine2 = u_success_streetLine2
        personEmergencyContact.streetLine3 = u_success_streetLine3
        personEmergencyContact.city = u_success_city
        personEmergencyContact.zip = u_success_zip
        personEmergencyContact.phoneArea = u_success_phoneArea
        personEmergencyContact.phoneNumber = u_success_phoneNumber
        personEmergencyContact.phoneExtension = u_success_phoneExtension
        personEmergencyContact.surnamePrefix = u_success_surnamePrefix
        personEmergencyContact.countryPhone = u_success_countryPhone
        personEmergencyContact.houseNumber = u_success_houseNumber
        personEmergencyContact.streetLine4 = u_success_streetLine4
        shouldFail(HibernateOptimisticLockingFailureException) {
            personEmergencyContact.save(failOnError: true, flush: true)
        }
    }


    void testDeletePersonEmergencyContact() {
        def personEmergencyContact = newValidForCreatePersonEmergencyContact()
        personEmergencyContact.save(failOnError: true, flush: true)
        def id = personEmergencyContact.id
        assertNotNull id
        personEmergencyContact.delete()
        assertNull PersonEmergencyContact.get(id)
    }


    void testValidation() {
        def personEmergencyContact = newInvalidForCreatePersonEmergencyContact()
        assertFalse "PersonEmergencyContact could not be validated as expected due to ${personEmergencyContact.errors}", personEmergencyContact.validate()
    }


    void testNullValidationFailure() {
        def personEmergencyContact = new PersonEmergencyContact()
        assertFalse "PersonEmergencyContact should have failed validation", personEmergencyContact.validate()
        assertErrorsFor personEmergencyContact, 'nullable',
                [
                        'pidm',
                        'priority',
                        'lastName',
                        'firstName'
                ]
        assertNoErrorsFor personEmergencyContact,
                [
                        'middleInitial',
                        'streetLine1',
                        'streetLine2',
                        'streetLine3',
                        'city',
                        'zip',
                        'phoneArea',
                        'phoneNumber',
                        'phoneExtension',
                        'surnamePrefix',
                        'countryPhone',
                        'houseNumber',
                        'streetLine4',
                        'state',
                        'nation',
                        'relationship',
                        'addressType'
                ]
    }


    void testMaxSizeValidationFailures() {
        def personEmergencyContact = new PersonEmergencyContact(
                middleInitial: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                streetLine1: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                streetLine2: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                streetLine3: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                city: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                zip: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                phoneArea: 'XXXXXXXX',
                phoneNumber: 'XXXXXXXXXXXXXX',
                phoneExtension: 'XXXXXXXXXXXX',
                surnamePrefix: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                countryPhone: 'XXXXXX',
                houseNumber: 'XXXXXXXXXXXX',
                streetLine4: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "PersonEmergencyContact should have failed validation", personEmergencyContact.validate()
        assertErrorsFor personEmergencyContact, 'maxSize', ['middleInitial', 'streetLine1', 'streetLine2', 'streetLine3', 'city', 'zip', 'phoneArea', 'phoneNumber', 'phoneExtension', 'surnamePrefix', 'countryPhone', 'houseNumber', 'streetLine4']
    }


    void testFetchByPidmOrderByPriority() {
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def personEmergencyContact = newValidForCreatePersonEmergencyContact()
        personEmergencyContact.save()
        def phone = personEmergencyContact.fetchByPidmOrderByPriority(pidm)
        assertNotNull "The phone for student with primary checked is as expected ", phone
    }


    private def newValidForCreatePersonEmergencyContact() {
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def personEmergencyContact = new PersonEmergencyContact(
                pidm: pidm,
                priority: i_success_priority,
                lastName: i_success_lastName,
                firstName: i_success_firstName,
                middleInitial: i_success_middleInitial,
                streetLine1: i_success_streetLine1,
                streetLine2: i_success_streetLine2,
                streetLine3: i_success_streetLine3,
                city: i_success_city,
                zip: i_success_zip,
                phoneArea: i_success_phoneArea,
                phoneNumber: i_success_phoneNumber,
                phoneExtension: i_success_phoneExtension,
                surnamePrefix: i_success_surnamePrefix,
                countryPhone: i_success_countryPhone,
                houseNumber: i_success_houseNumber,
                streetLine4: i_success_streetLine4,
                state: i_success_state,
                nation: i_success_nation,
                relationship: i_success_relationship,
                addressType: i_success_addressType,
        )
        return personEmergencyContact
    }


    private def newInvalidForCreatePersonEmergencyContact() {
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def personEmergencyContact = new PersonEmergencyContact(
                pidm: pidm,
                priority: i_failure_priority,
                lastName: i_failure_lastName,
                firstName: i_failure_firstName,
                middleInitial: i_failure_middleInitial,
                streetLine1: i_failure_streetLine1,
                streetLine2: i_failure_streetLine2,
                streetLine3: i_failure_streetLine3,
                city: i_failure_city,
                zip: i_failure_zip,
                phoneArea: i_failure_phoneArea,
                phoneNumber: i_failure_phoneNumber,
                phoneExtension: i_failure_phoneExtension,
                surnamePrefix: i_failure_surnamePrefix,
                countryPhone: i_failure_countryPhone,
                houseNumber: i_failure_houseNumber,
                streetLine4: i_failure_streetLine4,
                state: i_failure_state,
                nation: i_failure_nation,
                relationship: i_failure_relationship,
                addressType: i_failure_addressType,
        )
        return personEmergencyContact
    }

}
