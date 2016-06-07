/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

enum CredentialType {

    SOCIAL_SECURITY_NUMBER("Social Security Number", "ssn"),
    SOCIAL_INSURANCE_NUMBER("Social Insurance Number", "sin"),
    BANNER_ID("Banner ID", "bannerId"),
    COLLEAGUE_PERSON_ID("Colleague Person ID", "colleaguePersonId"),
    ELEVATE_ID("Elevate ID", "elevateId"),
    BANNER_SOURCED_ID("Banner Sourced ID", "bannerSourcedId"),
    BANNER_USER_NAME("Banner User Name", "bannerUserName"),
    BANNER_UDC_ID("Banner UDC ID", "bannerUdcId")

    private final String v3Enum
    private final String v6Enum


    CredentialType(String v3Enum, String v6Enum) {
        this.v3Enum = v3Enum
        this.v6Enum = v6Enum
    }


    public String getV3() {
        return v3Enum
    }


    public String getV6() {
        return v6Enum
    }

    /**
     * Given a string like "bannerSourcedId" returns corresponding enum BANNER_SOURCED_ID.
     * This is useful in "create" and "update" operations to validate the input string.
     *
     * @param value
     * @return
     */
    public static CredentialType getByString(String value) {
        Iterator itr = CredentialType.values().iterator()
        while (itr.hasNext()) {
            CredentialType credentialType = itr.next()
            def vals = []
            if (credentialType.v3Enum) {
                vals << credentialType.v3Enum
            }
            if (credentialType.v6Enum) {
                vals << credentialType.v6Enum
            }
            if (vals.contains(value)) {
                return credentialType
            }
        }
        return null
    }

}
