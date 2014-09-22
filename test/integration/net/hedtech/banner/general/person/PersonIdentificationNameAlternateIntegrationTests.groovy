/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
/*********************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonIdentificationNameAlternateIntegrationTests extends BaseIntegrationTestCase {

    def personIdentificationNameAlternate


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
    void testCreatePersonIdentificationNameAlternate() {
        def personIdentificationNameCurrent = setupNewPersonIdentificationNameCurrent("P")

        // Create an id change
        def personIdentificationNameAlternate = new PersonIdentificationNameAlternate(
                pidm: personIdentificationNameCurrent.pidm,
                bannerId: "TTTT-U001",
                changeIndicator: "I",
                lastName: personIdentificationNameCurrent.lastName,
                firstName: personIdentificationNameCurrent.firstName,
                middleName: personIdentificationNameCurrent.middleName,
                entityIndicator: personIdentificationNameCurrent.entityIndicator,
                nameType: personIdentificationNameCurrent.nameType,
                lastModifiedBy: "grails"
        )

        personIdentificationNameAlternate.save(flush: true, failOnError: true)
        def id = personIdentificationNameAlternate.id

        personIdentificationNameAlternate = PersonIdentificationNameAlternate.get(id)
        assertNotNull personIdentificationNameAlternate?.id
        assertEquals "TTTT-U001", personIdentificationNameAlternate.bannerId
        assertEquals "I", personIdentificationNameAlternate.changeIndicator
        assertEquals personIdentificationNameCurrent.pidm, personIdentificationNameAlternate.pidm
        assertEquals personIdentificationNameCurrent.lastName, personIdentificationNameAlternate.lastName
        assertEquals personIdentificationNameCurrent.firstName, personIdentificationNameAlternate.firstName
        assertEquals personIdentificationNameCurrent.middleName, personIdentificationNameAlternate.middleName
        assertEquals personIdentificationNameCurrent.entityIndicator, personIdentificationNameAlternate.entityIndicator
        assertEquals personIdentificationNameCurrent.nameType?.code, personIdentificationNameAlternate.nameType?.code
    }


	@Test
    void testDeletePersonIdentificationNameAlternate() {
        def personIdentificationNameCurrent = setupNewPersonIdentificationNameCurrent("P")
        def personIdentificationNameAlternate = newIdChange(personIdentificationNameCurrent)

        personIdentificationNameAlternate.save(flush: true, failOnError: true)
        def id = personIdentificationNameAlternate.id

        personIdentificationNameAlternate = PersonIdentificationNameAlternate.get(id)
        assertNotNull personIdentificationNameAlternate.id
        assertEquals "I", personIdentificationNameAlternate.changeIndicator
        assertEquals personIdentificationNameCurrent.pidm, personIdentificationNameAlternate.pidm

        personIdentificationNameAlternate.delete()
        personIdentificationNameAlternate = PersonIdentificationNameAlternate.get(id)
        assertNull personIdentificationNameAlternate
    }


	@Test
    void testValidation() {
        def personIdentificationNameAlternate = new PersonIdentificationNameAlternate(
                pidm: 1234,
                bannerId: "TTTT-U001",
                changeIndicator: "I",
                lastName: "TTTTTTTT",
                firstName: "TTTTT",
                middleName: "TTTTTTTT",
                entityIndicator: "P",
                nameType: NameType.findByCode("BRTH"),
                lastModifiedBy: "grails"
        )

        assertTrue "PersonIdentificationNameAlternate could not be validated as expected due to ${personIdentificationNameAlternate.errors}", personIdentificationNameAlternate.validate()
    }


	@Test
    void testNullValidationFailure() {
        def personIdentificationNameAlternate = new PersonIdentificationNameAlternate()
        assertFalse "PersonIdentificationNameAlternate should have failed validation", personIdentificationNameAlternate.validate()
        assertErrorsFor personIdentificationNameAlternate, 'nullable',
                [
                        'lastName', 'changeIndicator'
                ]
        assertNoErrorsFor personIdentificationNameAlternate,
                [
                        'firstName',
                        'middleName',
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
                        'fgacDomain'
                ]
    }


	@Test
    void testInListValidationFailure() {
        def personIdentificationNameAlternate = new PersonIdentificationNameAlternate(
                pidm: 1234,
                bannerId: "TTTT-U001",
                changeIndicator: "T",
                lastName: "TTTTTTTT",
                firstName: "TTTTT",
                middleName: "TTTTTTTT",
                entityIndicator: "T",
                nameType: NameType.findByCode("BRTH"),
                lastModifiedBy: "grails"
        )


        assertFalse personIdentificationNameAlternate.validate()

        assertErrorsFor personIdentificationNameAlternate, 'inList',
                [
                        'changeIndicator', 'entityIndicator'
                ]
    }



	@Test
    void testMaxSizeValidationFailures() {
        def personIdentificationNameAlternate = new PersonIdentificationNameAlternate(
                firstName: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                middleName: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
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
        assertFalse "PersonIdentificationNameAlternate should have failed validation", personIdentificationNameAlternate.validate()
        assertErrorsFor personIdentificationNameAlternate, 'maxSize', ['firstName', 'middleName', 'entityIndicator', 'userData', 'origin', 'searchLastName', 'searchFirstName', 'searchMiddleName', 'soundexLastName', 'soundexFirstName', 'createUser', 'surnamePrefix']
    }


    private def newIdChange(personIdentificationNameCurrent) {
        def personIdentificationNameAlternate = new PersonIdentificationNameAlternate(
                pidm: personIdentificationNameCurrent.pidm,
                bannerId: "TTTT-U001",
                changeIndicator: "I",
                lastName: personIdentificationNameCurrent.lastName,
                firstName: personIdentificationNameCurrent.firstName,
                middleName: personIdentificationNameCurrent.middleName,
                entityIndicator: personIdentificationNameCurrent.entityIndicator,
                nameType: personIdentificationNameCurrent.nameType,
                lastModifiedBy: "grails"
        )

        return personIdentificationNameAlternate
    }

    /**
     * Setup a new PersonIdentificationNameCurrent for testing.
     */
    private def setupNewPersonIdentificationNameCurrent(entityIndicator) {
        def personIdentificationNameCurrent = newPersonIdentificationNameCurrent(entityIndicator)
        personIdentificationNameCurrent.save(flush: true, failOnError: true)
        assertNotNull personIdentificationNameCurrent.id
        return personIdentificationNameCurrent
    }


    private def newPersonIdentificationNameCurrent(entityIndicator) {
        // Generate the banner id and pidm
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
        def bannerValues = sql.firstRow(idSql)
        def bannerId = bannerValues.bannerId
        def pidm = bannerValues.pidm

        // Instantiate the new id
        def personIdentificationNameCurrent = new PersonIdentificationNameCurrent(
                pidm: pidm,
                bannerId: bannerId,
                lastName: "TTTTT",
                firstName: "TTTTT",
                middleName: "TTTTT",
                entityIndicator: entityIndicator,
                nameType: NameType.findByCode("BRTH"),
                lastModifiedBy: "grails"
        )

        return personIdentificationNameCurrent
    }


}
