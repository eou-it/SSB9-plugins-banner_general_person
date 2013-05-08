/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of
 SunGard Higher Education and its subsidiaries. Any use of this software is limited
 solely to SunGard Higher Education licensees, and is further subject to the terms
 and conditions of one or more written license agreements between SunGard Higher
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher
 Education in the U.S.A. and/or other regions and/or countries.
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
        formContext = ['SOAIDEN'] // Since we are not testing a controller, we need to explicitly set this
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


    @Ignore //TODO: need to enable this once its determined how seed data is to be incorporated - Amrit
    void testIsPersonConfidential() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("EVT00023", null).pidm
        assertNotNull pidm
        def testPidm = PersonUtility.isPersonConfidential(pidm)
        assertTrue testPidm
    }


    @Ignore //TODO: need to enable this once its determined how seed data is to be incorporated - Amrit
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


    @Ignore //TODO: need to enable this once its determined how seed data is to be incorporated - Manjunath
    void testGetEmailId() {
        def pidm = PersonIdentificationName.findByBannerIdAndChangeIndicator("CMOORE", null).pidm
        assertNotNull pidm
        def emailId = PersonUtility.getEmailId(pidm)
        assertNotNull emailId
        assertEquals emailId, "Cindy@comcast.net"
    }
}
