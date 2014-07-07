/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.ldm.person

import net.hedtech.banner.general.person.PersonEmail

/**
 * LDM Decorator for person resource emails.
 */
class EmailV1 {
    @Delegate private final PersonEmail email

    def EmailV1(PersonEmail email) {
        this.email = email
    }

    def getEmailType() {
        // TODO: Filter and decode/map enumerations.
        this.email?.emailType?.description
    }

}
