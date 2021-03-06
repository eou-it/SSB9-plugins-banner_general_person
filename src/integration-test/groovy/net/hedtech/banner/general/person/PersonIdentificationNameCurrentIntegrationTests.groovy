/*********************************************************************************
 Copyright 2012 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
/*********************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import groovy.sql.Sql
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.general.system.SystemUtility
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

@Integration
@Rollback
class PersonIdentificationNameCurrentIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreatePersonIdentificationNameCurrent() {
        def personIdentificationNameCurrent = newPersonIdentificationNameCurrent("P")
        save personIdentificationNameCurrent
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationNameCurrent.id
    }


    @Test
    void testDeletePersonIdentificationNameCurrent() {
        def personIdentificationNameCurrent = setupNewPersonIdentificationNameCurrent("P")
        def id = personIdentificationNameCurrent.id

        personIdentificationNameCurrent = PersonIdentificationNameCurrent.get(id)
        assertNotNull personIdentificationNameCurrent?.id

        personIdentificationNameCurrent.delete()

        personIdentificationNameCurrent = PersonIdentificationNameCurrent.get(id)
        assertNull personIdentificationNameCurrent
    }


    @Test
    void testValidation() {
        def personIdentificationNameCurrent = newPersonIdentificationNameCurrent("P")
        assertTrue "PersonIdentificationNameCurrent could not be validated as expected due to ${personIdentificationNameCurrent.errors}", personIdentificationNameCurrent.validate()
    }


    @Test
    void testNullValidationFailure() {
        def personIdentificationNameCurrent = new PersonIdentificationNameCurrent()
        assertFalse "PersonIdentificationNameCurrent should have failed validation", personIdentificationNameCurrent.validate()
        assertErrorsFor personIdentificationNameCurrent, 'nullable',
                [
                        'lastName'
                ]
        assertNoErrorsFor personIdentificationNameCurrent,
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
    void testFetchByPidms() {
        def pidmList = []
        ["HOS00001", "HOSWEB001", "HOSWEB002", "HOSWEB003", "HOSWEB004", "HOSWEB005", "HOSWEB006"].each {
            def person = PersonUtility.getPerson(it)
            pidmList << person.pidm
        }
        assertEquals 7, pidmList.size()
        assertTrue pidmList instanceof List

        def persons = PersonIdentificationNameCurrent.fetchByPidms(pidmList, [max: 10, offset: 0])
        assertEquals 7, persons.size()
        persons.each { pers ->
            assertNotNull pidmList.find { it == pers.pidm }
        }

        def persons2 = PersonIdentificationNameCurrent.fetchByPidms(pidmList)
        assertEquals 7, persons2.size()
    }

    @Test
    void testFetchByPidmslargeList() {
        def pidmList = []
        PersonIdentificationNameCurrent.findAll().each { person ->
            pidmList << person.pidm
        }

        assertTrue pidmList.size() > 1000
        assertTrue pidmList instanceof List
        def pidmPartitions = SystemUtility.splitList(pidmList, 1000)
        assertTrue pidmPartitions.size() > 1

        def persons = PersonIdentificationNameCurrent.fetchByPidms(pidmList)
        assertEquals pidmList.size(), persons.size()

    }


    @Test
    void testNullChangeIndicatorValidationFailure() {
        def personIdentificationNameCurrent = newPersonIdentificationNameCurrent("P")
        personIdentificationNameCurrent.changeIndicator = "I"

        assertFalse personIdentificationNameCurrent.validate()
        assert personIdentificationNameCurrent.errors.getFieldErrors('changeIndicator').code == ['changeIndicatorMustBeNull']
    }


    @Test
    void testMaxSizeValidationFailures() {
        def personIdentificationNameCurrent = new PersonIdentificationNameCurrent(
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
        assertFalse "PersonIdentificationNameCurrent should have failed validation", personIdentificationNameCurrent.validate()
        assertErrorsFor personIdentificationNameCurrent, 'maxSize', ['firstName', 'middleName', 'entityIndicator', 'userData', 'origin', 'searchLastName', 'searchFirstName', 'searchMiddleName', 'soundexLastName', 'soundexFirstName', 'createUser', 'surnamePrefix']
    }

    /**
     * Setup a new PersonIdentificationNameCurrent for testing.
     */
    private def setupNewPersonIdentificationNameCurrent(entityIndicator) {
        def personIdentificationNameCurrent = newPersonIdentificationNameCurrent(entityIndicator)
        save personIdentificationNameCurrent
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
