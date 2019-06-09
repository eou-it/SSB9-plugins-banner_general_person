/*********************************************************************************
 Copyright 2016-2019 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import org.grails.testing.GrailsUnitTest
import org.junit.Test
import static org.junit.Assert.*
import spock.lang.Specification

/**
 * Tests for class PhoneNumberUtility
 */
class PhoneNumberUtilityTests  {

    @Test
    void testParsePhoneNumber() {
        def parts = [:]

        // Do not pass number itself
        parts = PhoneNumberUtility.parsePhoneNumber(null, null)
        assertEquals 0, parts.size()

        // FROM_NUMBER_WITH_PLUS_SIGN
        parts = PhoneNumberUtility.parsePhoneNumber("+19723837883", null)
        assertEquals 3, parts.size()
        assertEquals "1", parts["countryPhone"]
        assertEquals "972", parts["phoneArea"]
        assertEquals "3837883", parts["phoneNumber"]

        // International format
        parts = PhoneNumberUtility.parsePhoneNumber("+1 972-383-7883", null)
        assertEquals 3, parts.size()
        assertEquals "1", parts["countryPhone"]
        assertEquals "972", parts["phoneArea"]
        assertEquals "3837883", parts["phoneNumber"]

        // Out-of-country format from US
        parts = PhoneNumberUtility.parsePhoneNumber("+1 (972) 383-7883", null)
        assertEquals 3, parts.size()
        assertEquals "1", parts["countryPhone"]
        assertEquals "972", parts["phoneArea"]
        assertEquals "3837883", parts["phoneNumber"]

        // FROM_NUMBER_WITH_PLUS_SIGN
        // defaultRegion will not be used by google utility if number starts with + sign
        parts = PhoneNumberUtility.parsePhoneNumber("+19723837883", "US")
        assertEquals 3, parts.size()
        assertEquals "1", parts["countryPhone"]
        assertEquals "972", parts["phoneArea"]
        assertEquals "3837883", parts["phoneNumber"]

        // FROM_NUMBER_WITH_PLUS_SIGN
        // Trying to mislead with wrong defaultRegion but not a problem as utility will not use it for number starting with + sign
        parts = PhoneNumberUtility.parsePhoneNumber("+19723837883", "IN")
        assertEquals 3, parts.size()
        assertEquals "1", parts["countryPhone"]
        assertEquals "972", parts["phoneArea"]
        assertEquals "3837883", parts["phoneNumber"]

        // INVALID_COUNTRY_CODE. Missing or invalid default region.
        // either prefix with + sign or provide defaultRegion
        parts = PhoneNumberUtility.parsePhoneNumber("19723837883", null)
        assertEquals 0, parts.size()

        // FROM_NUMBER_WITHOUT_PLUS_SIGN
        parts = PhoneNumberUtility.parsePhoneNumber("19723837883", "US")
        assertEquals 3, parts.size()
        assertEquals "1", parts["countryPhone"]
        assertEquals "972", parts["phoneArea"]
        assertEquals "3837883", parts["phoneNumber"]

        // Number (without + prefix) belong to US but passing defaultRegion as IN
        parts = PhoneNumberUtility.parsePhoneNumber("19723837883", "IN")
        assertEquals 0, parts.size()

        // INVALID_COUNTRY_CODE. Missing or invalid default region.
        parts = PhoneNumberUtility.parsePhoneNumber("9723837883", null)
        assertEquals 0, parts.size()

        // FROM_DEFAULT_COUNTRY
        parts = PhoneNumberUtility.parsePhoneNumber("9723837883", "US")
        assertEquals 2, parts.size()
        assertFalse parts.containsKey("countryPhone")
        assertEquals "972", parts["phoneArea"]
        assertEquals "3837883", parts["phoneNumber"]

        // FROM_NUMBER_WITH_PLUS_SIGN
        // defaultRegion will not be used by google utility if number starts with + sign
        parts = PhoneNumberUtility.parsePhoneNumber("+9723837883", "US")
        assertEquals 0, parts.size()

        // FROM_DEFAULT_COUNTRY - but without area code invalid
        parts = PhoneNumberUtility.parsePhoneNumber("3837883", "US")
        assertEquals 0, parts.size()

        // Numbers with alphabetic letters will be ignored
        // Valid numbers like 1–800–GO-FEDEX will be converted to 1–800–4633339 by google utility
        // so they will not be handled
        parts = PhoneNumberUtility.parsePhoneNumber("1–800–GO-FEDEX", "US")
        assertEquals 0, parts.size()

        // NOT_A_NUMBER. The string supplied did not seem to be a phone number.
        parts = PhoneNumberUtility.parsePhoneNumber("AAABBBCCCDDD", "US")
        assertEquals 0, parts.size()
    }


    @Test
    void testComparePhoneNumber() {
        // First level comparison
        boolean same = PhoneNumberUtility.comparePhoneNumber("19723837883", "1", "972", "3837883")
        assertTrue same

        // First level comparison
        same = PhoneNumberUtility.comparePhoneNumber("1 3837883", "1", " ", "3837883")
        assertTrue same

        // First level comparison
        same = PhoneNumberUtility.comparePhoneNumber("13837883", "1", null, "3837883")
        assertTrue same

        // First level comparison
        same = PhoneNumberUtility.comparePhoneNumber("+1 (972) 383-7883", "+1 (", "972)", " 383-7883")
        assertTrue same

        // Second level comparison
        same = PhoneNumberUtility.comparePhoneNumber("+1 (972) 383-7883", "1", "972", "3837883")
        assertTrue same

        // Second level comparison
        same = PhoneNumberUtility.comparePhoneNumber("1972383-7883", "+1", "(972)", "383-7883")
        assertTrue same

        // Second level comparison
        same = PhoneNumberUtility.comparePhoneNumber("1–800–GO-FEDEX", "1", "800", "GOFEDEX")
        assertTrue same

        // Failed comparison
        same = PhoneNumberUtility.comparePhoneNumber("1800GOFEDEX", "1", "800", "4633339")
        assertFalse same

        // Null or empty string passed so no comparion happens
        same = PhoneNumberUtility.comparePhoneNumber("", "1", "972", "3837883")
        assertFalse same
    }


    @Test
    void testGetRegionCodeForCountryCode() {
        int countryCallingCode = 1
        String regionCode = PhoneNumberUtility.getRegionCodeForCountryCode(countryCallingCode)
        assertEquals "US", regionCode

        countryCallingCode = 91
        regionCode = PhoneNumberUtility.getRegionCodeForCountryCode(countryCallingCode)
        assertEquals "IN", regionCode

        // valid countryCallingCode, but non-geographical calling code like 800
        countryCallingCode = 800
        regionCode = PhoneNumberUtility.getRegionCodeForCountryCode(countryCallingCode)
        assertEquals "001", regionCode

        // Invalid countryCallingCode
        countryCallingCode = 2345
        regionCode = PhoneNumberUtility.getRegionCodeForCountryCode(countryCallingCode)
        assertEquals "ZZ", regionCode
    }

}
