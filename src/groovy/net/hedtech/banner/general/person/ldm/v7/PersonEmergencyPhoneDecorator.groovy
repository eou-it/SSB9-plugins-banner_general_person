/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person.ldm.v7

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class PersonEmergencyPhoneDecorator {
    String countryCallingCode
    String number
    String extension

    def PersonEmergencyPhoneDecorator(String countryPhone, String phoneArea, String phoneNumber, String phoneExtension) {
        this.countryCallingCode = countryPhone
        if (phoneArea || phoneNumber) {
            this.number = (phoneArea ?: "") + (phoneNumber ?: "")
        }
        this.extension = phoneExtension
    }
}
