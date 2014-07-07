/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.ldm.person

import net.hedtech.banner.general.person.PersonAddress


/**
 * LDM decorator for person resource address.
 */
class AddressV1 {

    @Delegate private final PersonAddress address

    def AddressV1( PersonAddress address ) {
        this.address = address
    }

    def getAddressType() {
        this.address?.addressType?.description
    }

    def getState() {
        this.address?.state?.description
    }

    def getNation() {
        this.address?.nation?.nation
    }

}
