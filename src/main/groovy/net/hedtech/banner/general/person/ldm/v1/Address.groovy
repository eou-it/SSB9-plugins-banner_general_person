/*******************************************************************************
 Copyright 2014-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

import grails.validation.Validateable
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.person.PersonAddress
import org.grails.datastore.gorm.GormValidateable
import org.grails.datastore.mapping.dirty.checking.DirtyCheckable


/**
 * LDM decorator for person resource address.
 */
class Address implements GormValidateable, DirtyCheckable, Validateable {

    @Delegate
    private final PersonAddress address
    String guid
    String addressType
    def country
    static final String PROCESS_CODE = "HEDM"

    def Address(PersonAddress address) {
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
        this.address?.zip
    }

}
