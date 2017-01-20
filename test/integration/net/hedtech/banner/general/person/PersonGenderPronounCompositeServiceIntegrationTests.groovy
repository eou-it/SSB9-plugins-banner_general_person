/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test
import org.junit.After

class PersonGenderPronounCompositeServiceIntegrationTests extends BaseIntegrationTestCase {
    def personGenderPronounCompositeService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /* TODO: uncomment tests after seed data is created
    @Test
    void testFetchPersonalDetails() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)
        assertEquals 'Other', details.gender.description
        assertEquals 'TR1', details.gender.code
    }

    @Test
    void testUpdatePerson() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)
        details.pidm = pidm

        assertEquals 'Other', details.gender.description
        assertEquals 'TR1', details.gender.code
        assertEquals null, details.preferenceFirstName

        details.gender= [code : 'WOMA']
        details.pronoun= [code : 'HER']
        details.preferenceFirstName = 'NickName'
        details.maritalStatus = MaritalStatus.findByCode('S')

        personGenderPronounCompositeService.updatePerson(details)
        def newDetails = personGenderPronounCompositeService.fetchPersonalDetails(pidm)

        assertEquals details.id, newDetails.id
        assertEquals 'WOMA', newDetails.gender.code
        assertEquals 'Woman', newDetails.gender.description
        assertEquals 'HER', newDetails.pronoun.code
        assertEquals 'her', newDetails.pronoun.description
        assertEquals 'NickName', newDetails.preferenceFirstName
        assertEquals 'S', newDetails.maritalStatus.code
        assertEquals details.version+1, newDetails.version
    }

    @Test
    void testUpdatePronoun() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)
        details.pidm = pidm

        assertEquals 'Other', details.gender.description
        assertEquals 'TR1', details.gender.code
        assertEquals null, details.preferenceFirstName

        details.pronoun = [code : 'HER']

        personGenderPronounCompositeService.updatePerson(details)
        def newDetails = personGenderPronounCompositeService.fetchPersonalDetails(pidm)

        assertEquals details.id, newDetails.id
        assertEquals 'TR1', newDetails.gender.code
        assertEquals 'Other', newDetails.gender.description
        assertEquals 'HER', newDetails.pronoun.code
        assertEquals 'her', newDetails.pronoun.description
        assertEquals null, newDetails.preferenceFirstName
        assertEquals details.version+1, newDetails.version
    }

    @Test
    void testUpdatePersonNoGender() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)
        details.pidm = pidm

        assertEquals 'Other', details.gender.description
        assertEquals 'TR1', details.gender.code
        assertEquals null, details.preferenceFirstName

        details.gender = [code: null]
        details.pronoun= [code : 'HER']
        details.preferenceFirstName = 'NickName'

        personGenderPronounCompositeService.updatePerson(details)
        def newDetails = personGenderPronounCompositeService.fetchPersonalDetails(pidm)

        assertEquals details.id, newDetails.id
        assertEquals 'HER', newDetails.pronoun.code
        assertEquals 'her', newDetails.pronoun.description
        assertEquals null, newDetails.gender.code
        assertEquals null, newDetails.gender.description
        assertEquals 'NickName', newDetails.preferenceFirstName
        assertEquals details.version+1, newDetails.version
    }

    @Test
    void testUpdatePersonNoChanges() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)
        details.pidm = pidm

        assertEquals 'Other', details.gender.description
        assertEquals 'TR1', details.gender.code
        assertEquals null, details.preferenceFirstName

        personGenderPronounCompositeService.updatePerson(details)
        def newDetails = personGenderPronounCompositeService.fetchPersonalDetails(pidm)

        assertEquals details.id, newDetails.id
        assertEquals null, newDetails.pronoun.code
        assertEquals 'TR1', newDetails.gender.code
        assertEquals 'Other', newDetails.gender.description
        assertEquals null, newDetails.preferenceFirstName
        assertEquals details.version, newDetails.version
    }

    @Test
    void testUpdatePersonInvalidGender() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)
        details.pidm = pidm

        assertEquals 'Other', details.gender.description
        assertEquals 'TR1', details.gender.code
        assertEquals null, details.preferenceFirstName

        details.gender = [code: 'INVALID']
        details.pronoun= [code : 'HER']
        details.preferenceFirstName = 'NickName'

        try {
            personGenderPronounCompositeService.updatePerson(details)
        }
        catch (ApplicationException e) {
            def newDetails = personGenderPronounCompositeService.fetchPersonalDetails(pidm)

            assertEquals details.id, newDetails.id
            assertEquals null, newDetails.pronoun.code
            assertEquals null, newDetails.pronoun.description
            assertEquals 'TR1', newDetails.gender.code
            assertEquals 'Other', newDetails.gender.description
            assertEquals null, newDetails.preferenceFirstName
            assertEquals details.version, newDetails.version
        }
    }

    @Test
    void testUpdatePersonBadVersion() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)
        details.pidm = pidm

        assertEquals 'Other', details.gender.description
        assertEquals 'TR1', details.gender.code
        assertEquals null, details.preferenceFirstName

        details.version = details.version - 2
        details.pronoun = [code : 'HER']

        shouldFail(ApplicationException) {
            personGenderPronounCompositeService.updatePerson(details)
        }
    }
    */

    @Test
    void testCheckGeneral889Installed() {
        def result = personGenderPronounCompositeService.checkGenderPronounInstalled()
        assertTrue result
    }
}