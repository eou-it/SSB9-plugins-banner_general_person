/*******************************************************************************
 Copyright 2016-2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

enum CredentialType {

    SOCIAL_SECURITY_NUMBER([v3: "Social Security Number", v6: "ssn", v8: "ssn"]),
    SOCIAL_INSURANCE_NUMBER([v3: "Social Insurance Number", v6: "sin", v8: "sin"]),
    BANNER_ID([v3: "Banner ID", v6: "bannerId", v8: "bannerId"]),
    COLLEAGUE_PERSON_ID([v3: "Colleague Person ID", v6: "colleaguePersonId", v8: "colleaguePersonId"]),
    ELEVATE_ID([v3: "Elevate ID", v6: "elevateId", v8: "elevateId"]),
    BANNER_SOURCED_ID([v3: "Banner Sourced ID", v6: "bannerSourcedId", v8: "bannerSourcedId"]),
    BANNER_USER_NAME([v3: "Banner User Name", v6: "bannerUserName", v8: "bannerUserName"]),
    BANNER_UDC_ID([v3: "Banner UDC ID", v6: "bannerUdcId", v8: "bannerUdcId"]),
    COLLEAGUE_USER_NAME([v8: "colleagueUserName"])

    private final Map<String, String> versionToEnumMap


    CredentialType(Map<String, String> versionToEnumMap) {
        this.versionToEnumMap = versionToEnumMap
    }


    public Map<String, String> getVersionToEnumMap() {
        return versionToEnumMap
    }


    public static CredentialType getByDataModelValue(String value, String version) {
        if (value) {
            Iterator itr = CredentialType.values().iterator()
            while (itr.hasNext()) {
                CredentialType credentialType = itr.next()
                if (credentialType.versionToEnumMap.containsKey(version) && credentialType.versionToEnumMap[version].equals(value)) {
                    return credentialType
                }
            }
        }
        return null
    }

}
