/*******************************************************************************
 Copyright 2014-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

import grails.validation.Validateable
import net.hedtech.banner.general.person.PersonTelephone
import org.grails.datastore.gorm.GormValidateable
import org.grails.datastore.mapping.dirty.checking.DirtyCheckable

/**
 * LDM Decorator for person resource phones.
 */
class Phone implements GormValidateable, DirtyCheckable, Validateable {
    @Delegate private final PersonTelephone phone
    String phoneType
    String phoneNumberDetail

    def Phone( PersonTelephone phone ) {
        this.phone = phone
    }

    def getTelephoneType() {
        this.phoneType
    }

    def setTelephoneType(String phoneType) {
        this.phoneType = phoneType
    }
}
