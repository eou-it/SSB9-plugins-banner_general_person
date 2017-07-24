/*********************************************************************************
 Copyright 2016-2017 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import org.apache.log4j.Logger

/**
 * Utility to deal with phone numbers.
 * This class mainly acts as a wrapper for Google's common library for parsing, formatting, and validating international phone numbers.
 * https://github.com/googlei18n/libphonenumber
 *
 */
abstract class PhoneNumberUtility {

    private static final log = Logger.getLogger(PhoneNumberUtility.class)

    private static final String REGEX_EXCEPT_ALPHABETS = "[^a-zA-Z]"
    private static final String REGEX_EXCEPT_ALPHABETS_DIGITS = "[^a-zA-Z0-9]"
    private static final String EMPTY_STRING = "";

    /**
     * Given a string like "+1 (972) 383-7883" parses into country code, area code and phone number.
     * The given string will not be parses if it contains alphabetic letters.  This means that valid number like 1–800–GO-FEDEX will
     * also be ignored.  This is due to the behavior of google utility.  Google utility will convert 1–800–GO-FEDEX to 1–800–4633339 which may
     * create confusion for caller of the method.
     *
     * @param requestPhoneNumber numberToParse
     * @param default2CharISOCountryCode the ISO 3166-1 two-letter region code that denotes the region that we are expecting the number to be from. Optional so can be set to null.
     * @return Map with 3 keys, countryPhone, phoneArea and phoneNumber. Map does not contain countryPhone if default2CharISOCountryCode used for parsing.
     */
    static def parsePhoneNumber(String requestPhoneNumber, String default2CharISOCountryCode) {
        Map parts = [:]

        // Do not parse if given phone number is null or it contains alphabetic letters
        if (!requestPhoneNumber || requestPhoneNumber.replaceAll(REGEX_EXCEPT_ALPHABETS, EMPTY_STRING).length() > 0) {
            return parts
        }

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance()
        Phonenumber.PhoneNumber parseResult
        try {
            parseResult = phoneNumberUtil.parseAndKeepRawInput(requestPhoneNumber, default2CharISOCountryCode)
        } catch (NumberParseException ex) {
            log.error "Error while parsing $requestPhoneNumber", ex
        }
        boolean validNum = false
        if (parseResult) {
            validNum = phoneNumberUtil.isValidNumber(parseResult)
        }
        log.debug("Is phone number " + requestPhoneNumber + " valid? " + validNum)

        if (validNum) {
            long nationalNumber = parseResult.getNationalNumber()
            String nationalNumberStr = String.valueOf(nationalNumber)
            log.debug("National Number: " + nationalNumber)
            int nationalDestinationCodeLength = phoneNumberUtil.getLengthOfNationalDestinationCode(parseResult)
            if (nationalDestinationCodeLength > 0) {
                parts.put('phoneArea', nationalNumberStr.substring(0, nationalDestinationCodeLength))
                parts.put('phoneNumber', nationalNumberStr.substring(nationalDestinationCodeLength))
            } else {
                parts.put('phoneNumber', nationalNumberStr)
            }

            // Populate country code only when defaultRegion not used for parsing
            // defaultRegion is ISO 3166-1 two-letter country code
            log.debug("Country code source: " + parseResult.getCountryCodeSource())
            if (parseResult.hasCountryCodeSource() && !(parseResult.getCountryCodeSource() == Phonenumber.PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY)) {
                parts.put('countryPhone', String.valueOf(parseResult.getCountryCode()))
            }
        }

        log.debug("Parse result: " + parts)
        return parts
    }

    /**
     * In Banner DB, we store country code, area code and phone number in three separate fields but Ellucian Ethos Data Model sends the entire phone number as one string.
     * So to know if the number sent in the request is same as the one in DB this method can be used.
     *
     * @param requestPhoneNumber Phone number as sent in Ellucian Ethos Data Model request
     * @param dbCountryCode Country code in DB
     * @param dbAreaCode Arad code in DB
     * @param dbPhoneNumber Phone number in DB
     * @return true if same
     */
    static boolean comparePhoneNumber(String requestPhoneNumber, String dbCountryCode, String dbAreaCode, String dbPhoneNumber) {
        if (requestPhoneNumber) {
            // First level comparison
            String dbTemp = (dbCountryCode ?: EMPTY_STRING) + (dbAreaCode ?: EMPTY_STRING) + (dbPhoneNumber ?: EMPTY_STRING)
            log.debug "Comparing $requestPhoneNumber with $dbTemp..."
            if (requestPhoneNumber.equals(dbTemp)) {
                return true
            }

            // Second level comparison - Keep only alphabets and digits to compare
            dbTemp = (dbCountryCode ? dbCountryCode.replaceAll(REGEX_EXCEPT_ALPHABETS_DIGITS, EMPTY_STRING) : EMPTY_STRING) +
                    (dbAreaCode ? dbAreaCode.replaceAll(REGEX_EXCEPT_ALPHABETS_DIGITS, EMPTY_STRING) : EMPTY_STRING) +
                    (dbPhoneNumber ? dbPhoneNumber.replaceAll(REGEX_EXCEPT_ALPHABETS_DIGITS, EMPTY_STRING) : EMPTY_STRING)
            String requestTemp = requestPhoneNumber.replaceAll(REGEX_EXCEPT_ALPHABETS_DIGITS, EMPTY_STRING)
            log.debug "Comparing $requestTemp with $dbTemp..."
            if (requestTemp.equals(dbTemp)) {
                return true
            }
        }
        return false
    }


    static String getRegionCodeForCountryCode(int countryCallingCode) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance()
        return phoneNumberUtil.getRegionCodeForCountryCode(countryCallingCode)
    }

}
