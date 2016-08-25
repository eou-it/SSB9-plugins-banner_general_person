/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person.ldm.v7

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.person.view.PersonEmergencyContactView

@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class PersonEmergencyAddressDecorator {
    List<String> addressLines
    PersonEmergencyPlaceDecorator place
    private String addressTypeGuid
    private String hedmAddressType

    PersonEmergencyAddressDecorator(PersonEmergencyContactView personEmergencyContactView, String iso3CountryCode, String defaultCountryTitle, String hedmAddressType) {
        this.addressLines = getAddressLinesForAddress(personEmergencyContactView)
        if (!this.addressLines) {
            addressLines = ["."]
        }
        if (iso3CountryCode) {
            PersonEmergencyCountryDecorator country = new PersonEmergencyCountryDecorator(iso3CountryCode, personEmergencyContactView, defaultCountryTitle)
            this.place = new PersonEmergencyPlaceDecorator(country)
        }
        this.addressTypeGuid = personEmergencyContactView.addressTypeGuid
        this.hedmAddressType = hedmAddressType
    }


    def getType() {
        def obj
        if (hedmAddressType) {
            obj = ["addressType": hedmAddressType, "detail": ["id": addressTypeGuid]]
        }
        return obj
    }


    public List<String> getAddressLinesForAddress(PersonEmergencyContactView personEmergencyContactView) {
        List<String> addressLines = []
        if (personEmergencyContactView.houseNumber) {
            addressLines << personEmergencyContactView.houseNumber
        }
        if (personEmergencyContactView.streetLine1) {
            addressLines << personEmergencyContactView.streetLine1
        }
        if (personEmergencyContactView.streetLine2) {
            addressLines << personEmergencyContactView.streetLine2
        }
        if (personEmergencyContactView.streetLine3) {
            addressLines << personEmergencyContactView.streetLine3
        }
        if (personEmergencyContactView.streetLine4) {
            addressLines << personEmergencyContactView.streetLine4
        }
        return addressLines
    }
}
