/** *****************************************************************************
 � 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */

package com.sungardhe.banner.general.person

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import org.junit.Ignore

class PersonUtilityTests extends BaseIntegrationTestCase {


    protected void setUp() {
        formContext = ['SPAIDEN'] // Since we are not testing a controller, we need to explicitly set this
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
