/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.person.PersonAddress


/**
 * LDM decorator for person resource address.
 */
class Address {

    @Delegate private final PersonAddress address
    String guid
    String addressType
    def country
    static final String PROCESS_CODE = "HEDM"
    static final String PERSON_REGION = "PERSON.ADDRESSES.REGION"
    static final String PERSON_POSTAL_CODE = "PERSON.ADDRESSES.POSTAL.CODE"

    def Address( PersonAddress address ) {
        this.address = address
        this.addressType = null
        this.country = address.nation

    }

    def getCounty() {
        this.address?.county?.description
    }

    def getAddressType() {
        this.addressType
    }

    def setAddressType(String addressType) {
        this.addressType = addressType
    }

    def getState() {
            this.address?.state?.code
        }

    def getZip() {
        IntegrationConfiguration postalCodeRule = IntegrationConfiguration.findByProcessCodeAndSettingNameAndValue(PROCESS_CODE, PERSON_POSTAL_CODE, this.address?.zip)
        if (postalCodeRule && postalCodeRule.value == this.address?.zip) {
            return null
        } else {
            this.address?.zip
        }
    }

}
