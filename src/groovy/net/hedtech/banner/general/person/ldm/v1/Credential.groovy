/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

/**
 * LDM decorator for person resource credential.
 */
class Credential {
    String credentialType
    String credentialId
    Date effectiveStartDate
    Date effectiveEndDate

    public static final additionalIdMap = ['ELV8':'Elevate ID']

    def Credential( String credentialType, String credentialId,
                    Date effectiveStartDate, Date effectiveEndDate) {
        this.credentialType = credentialType
        this.credentialId = credentialId
        this.effectiveStartDate = effectiveStartDate
        this.effectiveEndDate = effectiveEndDate
    }
}
