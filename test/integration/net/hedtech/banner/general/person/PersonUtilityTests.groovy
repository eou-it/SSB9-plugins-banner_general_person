/*********************************************************************************
  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import net.hedtech.banner.person.dsl.NameTemplate
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.junit.Ignore
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder

class PersonUtilityTests extends BaseIntegrationTestCase {


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testValidPerson() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("HOS00001", null).pidm
        assertNotNull pidm

        def testPidm = PersonUtility.isPersonValid("HOS00001")
        assertTrue testPidm

        testPidm = PersonUtility.isPersonValid("HOSCCCCC")
        assertFalse testPidm
    }


    void testGetWithBannerId() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("HOS00001", null).pidm
        assertNotNull pidm
        def testPidm = PersonUtility.getPerson("HOS00001")
        assertNotNull testPidm
        assertNotNull testPidm.fullName

        testPidm = PersonUtility.getPerson("HOSXXXX1")
        assertNull testPidm
    }


    void testGetWithPidm() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("HOS00001", null).pidm
        assertNotNull pidm
        def testPidm = PersonUtility.getPerson(pidm)
        assertNotNull testPidm
        assertNotNull testPidm.fullName
        assertEquals testPidm.bannerId, "HOS00001"

    }


    void testFormatName() {
        //get the name format from General Person Plugin messages.properties
        def application = ApplicationHolder.application
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


    void testIsPersonDeceased() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("JCSYS0001", null).pidm
        assertNotNull pidm
        def testPidm = PersonUtility.isPersonDeceased(pidm)
        assertTrue testPidm
    }


    void testIsPersonConfidential() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("EVT00023", null).pidm
        assertNotNull pidm
        def testPidm = PersonUtility.isPersonConfidential(pidm)
        assertTrue testPidm
    }


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


    void testGetEmailId() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("HOF00714", null).pidm
        assertNotNull pidm
        def emailId = PersonUtility.getEmailId(pidm)
        assertNotNull emailId
        assertEquals emailId, "Marita.Herwig@sungarduniv.edu"
    }
}
