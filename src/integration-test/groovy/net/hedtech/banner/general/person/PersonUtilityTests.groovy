/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import grails.util.Holders
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.person.dsl.NameTemplate
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.web.context.request.RequestContextHolder

class PersonUtilityTests extends BaseIntegrationTestCase {


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
    void testValidPerson() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("HOS00001", null).pidm
        assertNotNull pidm

        def testPidm = PersonUtility.isPersonValid("HOS00001")
        assertTrue testPidm

        testPidm = PersonUtility.isPersonValid("HOSCCCCC")
        assertFalse testPidm
    }


    @Test
    void testGetWithBannerId() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("HOS00001", null).pidm
        assertNotNull pidm
        def testPidm = PersonUtility.getPerson("HOS00001")
        assertNotNull testPidm
        assertNotNull testPidm.fullName

        testPidm = PersonUtility.getPerson("HOSXXXX1")
        assertNull testPidm
    }


    @Test
    void testGetWithPidm() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("HOS00001", null).pidm
        assertNotNull pidm
        def testPidm = PersonUtility.getPerson(pidm)
        assertNotNull testPidm
        assertNotNull testPidm.fullName
        assertEquals testPidm.bannerId, "HOS00001"

    }


    @Test
    void testFormatName() {
        //get the name format from General Person Plugin messages.properties
        def application = Holders.getGrailsApplication()
        ApplicationContext applicationContext = application.mainContext
        def messageSource = applicationContext.getBean("messageSource")
        def nameFormat = messageSource.getMessage("default.name.format", null, LocaleContextHolder.getLocale())

        def instructor = PersonUtility.getPerson("HOF00714")
        def formattedName = PersonUtility.formatName([lastName: instructor.lastName, firstName: instructor.firstName, middleName: instructor.middleName, surnamePrefix: ""])
        //get the name using the NameTemplate directly
        def displayName = NameTemplate.format {
            lastName instructor.lastName
            firstName instructor.firstName
            mi instructor.middleName
            surnamePrefix instructor.surnamePrefix
            formatTemplate nameFormat
            text
        }
        assertEquals displayName, formattedName
    }


    @Test
    void testNullPersonFormatName() {
        def formatName = PersonUtility.formatName([])
        assertNull formatName

        formatName = PersonUtility.formatName()
        assertNull formatName
    }


    @Test
    void testIsPersonDeceased() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("HOF00718", null).pidm
        assertNotNull pidm
        def testPidm = PersonUtility.isPersonDeceased(pidm)
        assertTrue testPidm
    }


    @Test
    void testIsPersonConfidential() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("EVT00023", null).pidm
        assertNotNull pidm
        def testPidm = PersonUtility.isPersonConfidential(pidm)
        assertTrue testPidm
    }


    @Test
    void testIsPersonConfidentialOrDeceased() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("EVT00023", null).pidm
        assertNotNull pidm
        def testPidm = PersonUtility.isPersonConfidentialOrDeceased(pidm)
        assertTrue testPidm.confidential
        assertFalse testPidm.deceased

        pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("EVT00024", null).pidm
        assertNotNull pidm
        testPidm = PersonUtility.isPersonConfidentialOrDeceased(pidm)
        assertFalse testPidm.confidential
        assertTrue testPidm.deceased
    }


    @Test
    void testGetEmailId() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("STUAFR152", null).pidm
        assertNotNull pidm
        def emailId = PersonUtility.getEmailId(pidm)
        assertNotNull emailId
        assertEquals emailId, "Zayne152@college.edu"
    }

    @Test
    void testSetPersonConfigInSession() {
        def session = RequestContextHolder.currentRequestAttributes().request.session
        assertNull session.getAttribute(PersonUtility.PERSON_CONFIG)

        PersonUtility.setPersonConfigInSession([:])

        assertNotNull session.getAttribute(PersonUtility.PERSON_CONFIG)
    }

    @Test
    void testGetEmailIdByCodeAndStatus() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("HOF00714", null).pidm
        assertNotNull pidm
        def emailId = PersonUtility.getEmailIdByCodeAndStatus(pidm,'HOME','A')
        assertNotNull emailId
        assertEquals emailId, "Marita.Herwig@sungarduniv.edu"
    }

    @Test
    void testGetPersonConfigFromSession() {
        assertEquals [:], PersonUtility.getPersonConfigFromSession()

        def session = RequestContextHolder.currentRequestAttributes().request.session
        session.setAttribute(PersonUtility.PERSON_CONFIG, [:])

        assertNotNull PersonUtility.getPersonConfigFromSession()
    }

    @Test
    void testGetDisplaySequence() {
        def session = RequestContextHolder.currentRequestAttributes().request.session
        assertNull session.getAttribute(PersonUtility.PERSON_CONFIG)

        def sequenceConfig = [processCode: 'PERSONAL_INFORMATION_SSB', settingName: 'OVERVIEW.ADDRESS.TYPE']
        def addrPriorities = PersonUtility.getDisplaySequence('addressDisplayPriorities', sequenceConfig)

        assertNotNull session.getAttribute(PersonUtility.PERSON_CONFIG)
        assertEquals(2, addrPriorities.size())
        assertEquals(1, addrPriorities["UPDATE_ME (PRIORITY 1)"])
        assertEquals(2, addrPriorities["UPDATE_ME (PRIORITY 2)"])
    }

    @Test
    void testGetDisplaySequenceWithNullSequenceConfig() {
        def session = RequestContextHolder.currentRequestAttributes().request.session
        assertNull session.getAttribute(PersonUtility.PERSON_CONFIG)

        def addrPriorities = PersonUtility.getDisplaySequence('addressDisplayPriorities', null)

        assertNull session.getAttribute(PersonUtility.PERSON_CONFIG)
        assertNull addrPriorities
    }



//can't find the preferredNameService anywhere in the application. So commenting out this test.
//    @Test
//    void testGetPreferredName() {
//        def application = Holders.getGrailsApplication()
//        def ctx = Holders.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
//        def preferredNameService = ctx.preferredNameService
//        application.config.productName = 'Student'
//        application.config.banner.applicationName = 'Student Self-Service'
//
//        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("HOF00714", null).pidm
//        assertNotNull pidm
//
//        def params=[:]
//        params.put("pidm",pidm)
//        String usage = preferredNameService.getUsage("Student","Student Self-Service")
//        String preferredName1 = PersonUtility.getPreferredName(params)
//        assertNotNull preferredName1
//
//        params.put("usage",usage)
//        String preferredName2 = preferredNameService.getPreferredName(params)
//        assertNotNull preferredName2
//
//        assertEquals preferredName1, preferredName2
//    }

}
