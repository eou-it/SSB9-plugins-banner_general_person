/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

import net.hedtech.banner.general.person.PersonBasicPersonBase
import net.hedtech.banner.general.person.PersonUtility
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test


class PersonIntegrationTests extends BaseIntegrationTestCase {

    def i_success_person
    def i_success_pidm
    def i_success_person_base
    def i_success_metadata
    def i_success_maritalStatusDetail
    def i_success_ethnicityDetail
    def i_success_guid
    def i_success_credentials
    def i_success_addresses
    def i_success_phones
    def i_success_emails
    def i_success_names
    def i_success_races
    def i_success_roles
    def i_success_person_record


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeTestDataForReferences()
    }


    void initializeTestDataForReferences() {
        i_success_pidm = PersonUtility.getPerson('HOSP0001')?.pidm
        i_success_person_base = PersonBasicPersonBase.fetchByPidm(i_success_pidm)
        i_success_person_record = new Person(i_success_person_base)
        i_success_metadata = [dataOrigin: i_success_person_base.dataOrigin]
        i_success_maritalStatusDetail = [guid: "qbcde-fghijk-lmnop"]
        i_success_ethnicityDetail = [guid: "qwer-tyui-opas"]
        i_success_guid = "aaaaaa-bbbbbb-cccccc-ddddddd-eeeeee"
        i_success_credentials = [[credentialId: 'A00000753', credentialType: 'Banner ID']]
        i_success_addresses = [[addressType: 'Mailing', streetAddress1: '123 Sesame Street']]
        i_success_phones = [[phoneNumber: '6107435302', phoneType: 'Mobile']]
        i_success_emails = [[guid: "wertg-bgyd-kyote", emailAddress: 'abc@gmail.com', emailType: 'Home']]
        i_success_names = [[lastName: 'Smith', middleName: 'a', firstName: 'John', nameType: 'Primary']]
        i_success_races = [[guid: "1bf8e6a8-338c-41cf-8b54-50d5c0f66"]]
        i_success_roles = [[role: 'Student']]
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testPerson() {
        i_success_person = validPerson()
        assertNotNull i_success_person_record
        assertEquals 'Male', i_success_person.getSex()
        assertEquals i_success_person.person, i_success_person_base
        assertEquals i_success_person.metadata.dataOrigin, i_success_metadata.dataOrigin
        assertEquals i_success_person.maritalStatusDetail, i_success_maritalStatusDetail
        assertEquals i_success_person.ethnicityDetail, i_success_ethnicityDetail
        assertEquals i_success_person.guid, i_success_guid
        assertEquals i_success_person.credentials, i_success_credentials
        assertEquals i_success_person.phones, i_success_phones
        assertEquals i_success_person.emails, i_success_emails
        assertEquals i_success_person.names, i_success_names
        assertEquals i_success_person.races, i_success_races
        assertEquals i_success_person.roles, i_success_roles
    }


    private def validPerson() {
        def newPerson = new Person(i_success_person_base,
                i_success_guid,
                i_success_credentials,
                i_success_addresses,
                i_success_phones,
                i_success_emails,
                i_success_names,
                i_success_maritalStatusDetail,
                i_success_ethnicityDetail,
                i_success_races,
                i_success_roles
        )

        return newPerson
    }

}
