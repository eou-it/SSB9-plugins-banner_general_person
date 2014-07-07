/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.ldm.person

import net.hedtech.banner.general.person.PersonTelephone

/**
 * LDM Decorator for person resource phones.
 */
class PhoneV1 {
    @Delegate private final PersonTelephone phone

    def PhoneV1( PersonTelephone phone ) {
        this.phone = phone
    }

    def getTelephoneType() {
        phone?.telephoneType?.description
    }
}
