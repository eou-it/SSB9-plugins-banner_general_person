/*******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

import net.hedtech.banner.general.person.PersonEmail

/**
 * LDM Decorator for person resource emails.
 */
class Email {
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
