/*******************************************************************************
 Copyright 2014-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

import grails.validation.Validateable
import net.hedtech.banner.general.person.PersonEmail
import org.grails.datastore.gorm.GormValidateable
import org.grails.datastore.mapping.dirty.checking.DirtyCheckable

/**
 * LDM Decorator for person resource emails.
 */
class Email implements GormValidateable, DirtyCheckable, Validateable {
    @Delegate private final PersonEmail email
    String guid
    String emailType

    def Email(String guid, PersonEmail email) {
        this.guid = guid
        this.email = email
    }

    def getEmailType() {
        this.emailType
    }

    def setEmailType(String emailType) {
        this.emailType = emailType
    }

}
