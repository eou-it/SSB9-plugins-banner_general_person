/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person.ldm.v7

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.person.view.PersonEmergencyContactView

import java.util.regex.Matcher
import java.util.regex.Pattern

@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class PersonEmergencyCountryDecorator {

    String code
    String title
    String postalTitle
    PersonEmergencyRegionDecorator region
    String locality
    String postalCode

    PersonEmergencyCountryDecorator(String iso3CountryCode, PersonEmergencyContactView personEmergencyContactView, String defaultCountryTitle) {
        this.code = iso3CountryCode
        if (personEmergencyContactView.countryTitle) {
            this.title = personEmergencyContactView.countryTitle
        } else {
            this.title = defaultCountryTitle
        }

        this.postalTitle = getPostalTitleForAddress(iso3CountryCode)
        if (personEmergencyContactView.countryRegionCode || personEmergencyContactView.countryRegionTitle) {
            this.region = new PersonEmergencyRegionDecorator(personEmergencyContactView.countryRegionCode, personEmergencyContactView.countryRegionTitle)
        }

        this.locality = personEmergencyContactView.city
        this.postalCode = getPostalCodeForAddress(personEmergencyContactView, iso3CountryCode)
    }


    private String getPostalCodeForAddress(PersonEmergencyContactView personEmergencyContactView, String iso3CountryCode) {
        if (isValueMatchesPattern(getPostalCodePattern(iso3CountryCode), personEmergencyContactView.zip)) {
            return personEmergencyContactView.zip
        }
        return null
    }


    private boolean isValueMatchesPattern(String patternStr, String value) {
        boolean matches = false
        if (patternStr && value) {
            Pattern pattern = Pattern.compile(patternStr)
            Matcher matcher = pattern.matcher(value);
            matches = matcher.matches()
        }
        return matches
    }


    private String getPostalCodePattern(String iso3CountryCode) {
        String pattern
        HedmPersonEmergencyCountry hedmCountry = HedmPersonEmergencyCountry.getByString(iso3CountryCode)
        if (hedmCountry) {
            pattern = hedmCountry.postalCodePattern
        }
        return pattern
    }


    private String getPostalTitleForAddress(String iso3CountryCode) {
        String addrTitle = null
        HedmPersonEmergencyCountry hedmCountry = HedmPersonEmergencyCountry.getByString(iso3CountryCode)
        if (hedmCountry) {
            addrTitle = hedmCountry.postalTitle
        }
        return addrTitle
    }
}
