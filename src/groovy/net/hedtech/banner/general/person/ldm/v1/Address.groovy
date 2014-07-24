/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

import net.hedtech.banner.general.person.PersonAddress


/**
 * LDM decorator for person resource address.
 */
class Address {

    @Delegate private final PersonAddress address
    String guid
    String addressType

    def Address( PersonAddress address ) {
        this.address = address
        this.addressType = null
    }

    def getState() {
        this.address?.state?.description
    }

    def getNation() {
        this.address?.nation?.nation
    }

    def getAddressType() {
        this.addressType
    }

    def setAddressType(String addressType) {
        this.addressType = addressType
    }

}
