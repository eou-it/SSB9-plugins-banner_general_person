/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

import net.hedtech.banner.general.person.PersonTelephone

/**
 * LDM Decorator for person resource phones.
 */
class Phone {
    @Delegate private final PersonTelephone phone
    String phoneType
    String phoneNumberDetail

    def Phone( PersonTelephone phone ) {
        this.phone = phone
        this.phoneNumberDetail = phone.internationalAccess ?: phone.phoneArea + phone.phoneNumber
    }

    def getTelephoneType() {
        this.phoneType
    }

    def setTelephoneType(String phoneType) {
        this.phoneType = phoneType
    }
}
