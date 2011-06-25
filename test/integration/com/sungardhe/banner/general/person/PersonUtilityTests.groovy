/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */

package com.sungardhe.banner.general.person

import com.sungardhe.banner.testing.BaseIntegrationTestCase

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
}
