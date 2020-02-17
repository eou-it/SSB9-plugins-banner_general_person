/*******************************************************************************
 Copyright 2017-2020 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import static groovy.test.GroovyAssert.*
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test
import org.junit.After

@Integration
@Rollback
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

    @Test
    void testFetchPersonalDetails() {
        setupGDP000005()

        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)
        assertEquals 'Other', details.gender.description
        assertEquals 'TR1', details.gender.code
    }

    @Test
    void testFetchPersonalDetailsWhereNoDetailsExist() {
        def pidm = PersonUtility.getPerson("GDP000004").pidm // Has no personal details
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)
        assertEquals 0, details.size()
    }

    @Test
    void testUpdatePerson() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)

        details.pidm = pidm
        details.gender = [code : 'WOMA']
        details.pronoun = [code : 'B002']
        details.preferenceFirstName = 'NickName'
        details.maritalStatus = MaritalStatus.findByCode('S')

        personGenderPronounCompositeService.updatePerson(details)
        def newDetails = personGenderPronounCompositeService.fetchPersonalDetails(details.pidm)

        assertEquals details.id, newDetails.id
        assertEquals 'WOMA', newDetails.gender.code
        assertEquals 'Woman', newDetails.gender.description
        assertEquals 'B002', newDetails.pronoun.code
        assertEquals 'she', newDetails.pronoun.description
        assertEquals 'NickName', newDetails.preferenceFirstName
        assertEquals 'S', newDetails.maritalStatus.code
        assertEquals details.version+1, newDetails.version
    }

    @Test
    void testUpdatePersonNoId() {
        def pidm = PersonUtility.getPerson("HOSH00018").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)

        details.pidm = pidm
        details.gender = [code : 'WOMA']
        details.pronoun = [code : 'B002']
        details.preferenceFirstName = 'NickName'
        details.maritalStatus = MaritalStatus.findByCode('S')

        try {
            personGenderPronounCompositeService.updatePerson(details)
            fail("I should have received an error but it passed; @@r1:personDoesNotExist@@ ")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "personDoesNotExist"
        }
    }

    @Test
    void testUpdatePersonBadId() {
        def pidm = PersonUtility.getPerson("HOSH00018").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)

        details.id = 9320932
        details.pidm = pidm
        details.gender = [code : 'WOMA']
        details.pronoun = [code : 'B002']
        details.preferenceFirstName = 'NickName'
        details.maritalStatus = MaritalStatus.findByCode('S')

        try {
            personGenderPronounCompositeService.updatePerson(details)
            fail("I should have received an error but it passed; @@r1:personDoesNotExist@@ ")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "personDoesNotExist"
        }
    }

    @Test
    void testUpdatePronoun() {
        def details = setupGDP000005()

        assertEquals 'Other', details.gender.description
        assertEquals 'TR1', details.gender.code
        assertEquals null, details.preferenceFirstName

        details.pronoun = [code : 'B002']

        personGenderPronounCompositeService.updatePerson(details)
        def newDetails = personGenderPronounCompositeService.fetchPersonalDetails(details.pidm)

        assertEquals details.id, newDetails.id
        assertEquals 'TR1', newDetails.gender.code
        assertEquals 'Other', newDetails.gender.description
        assertEquals 'B002', newDetails.pronoun.code
        assertEquals 'she', newDetails.pronoun.description
        assertEquals null, newDetails.preferenceFirstName
        assertEquals details.version+1, newDetails.version
    }

    @Test
    void testUpdatePersonNoGender() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)
        details.pidm = pidm
        assertEquals null, details.preferenceFirstName

        details.gender = [code: null]
        details.pronoun= [code : 'B002']
        details.preferenceFirstName = 'NickName'

        personGenderPronounCompositeService.updatePerson(details)
        def newDetails = personGenderPronounCompositeService.fetchPersonalDetails(details.pidm)

        assertEquals details.id, newDetails.id
        assertEquals 'B002', newDetails.pronoun.code
        assertEquals 'she', newDetails.pronoun.description
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

        assertEquals null, details.pronoun.code
        assertEquals null, details.gender.code
        assertEquals null, details.preferenceFirstName

        personGenderPronounCompositeService.updatePerson(details)
        def newDetails = personGenderPronounCompositeService.fetchPersonalDetails(details.pidm)

        assertEquals details.id, newDetails.id
        assertEquals null, newDetails.pronoun.code
        assertEquals null, newDetails.gender.code
        assertEquals null, newDetails.preferenceFirstName
        assertEquals details.version, newDetails.version
    }

    @Test
    void testUpdatePersonInvalidGender() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)
        details.pidm = pidm

        assertEquals null, details.pronoun.code
        assertEquals null, details.gender.code
        assertEquals null, details.preferenceFirstName

        details.gender = [code: 'INVALID']
        details.pronoun= [code : 'B002']
        details.preferenceFirstName = 'NickName'

        try {
            personGenderPronounCompositeService.updatePerson(details)
        }
        catch (ApplicationException e) {
            def newDetails = personGenderPronounCompositeService.fetchPersonalDetails(details.pidm)

            assertEquals details.id, newDetails.id
            assertEquals null, newDetails.pronoun.code
            assertEquals null, newDetails.pronoun.description
            assertEquals null, newDetails.gender.code
            assertEquals null, newDetails.gender.description
            assertEquals null, newDetails.preferenceFirstName
            assertEquals details.version, newDetails.version
        }
    }

    @Test
    void testUpdatePersonBadVersion() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)
        details.pidm = pidm

        assertEquals null, details.gender.description
        assertEquals null, details.gender.code
        assertEquals null, details.preferenceFirstName

        details.version = details.version - 2
        details.pronoun = [code : 'B002']

        shouldFail(ApplicationException) {
            personGenderPronounCompositeService.updatePerson(details)
        }
    }

    @Test
    void testCheckGeneral889Installed() {
        def result = personGenderPronounCompositeService.checkGenderPronounInstalled()
        assertTrue result
    }

    @Test
    void testFetchGenderList() {
        def result = personGenderPronounCompositeService.fetchGenderList()
        assertTrue 4 <= result.size()
        assertTrue result.code.contains('MAN')
        assertTrue result.description.contains('Woman')
    }

    @Test
    void testFetchGenderListOffset() {
        def result = personGenderPronounCompositeService.fetchGenderList(2, 2, '')
        assertTrue 2 >= result.size()
        assertTrue result.code.contains('WOMA')
        assertTrue result.description.contains('Woman')
    }

    @Test
    void testFetchPronounList() {
        def result = personGenderPronounCompositeService.fetchPronounList()
        assertTrue 4 <= result.size()
        assertTrue result.code.contains('C001')
        assertTrue result.description.contains('she')
    }

    @Test
    void testFetchPronounListOffset() {
        def result = personGenderPronounCompositeService.fetchPronounList(2, 4, '')
        assertTrue 2 >= result.size()
        assertTrue result.code.contains('B002')
        assertTrue result.description.contains('she')
    }

    private def setupGDP000005() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def details = personGenderPronounCompositeService.fetchPersonalDetails(pidm)
        details.pidm = pidm
        details.gender = [code: 'TR1', description: 'Other']

        personGenderPronounCompositeService.updatePerson(details)

        return details
    }
}
