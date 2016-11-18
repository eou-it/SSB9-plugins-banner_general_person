/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

enum OrganizationRoleName {

    VENDOR([v6: "vendor"]),
    PARTNER([v6: "partner"]),
    AFFILIATE([v6: "affiliate"]),
    CONSTITUENT([v6: "constituent"])

    private final Map<String, String> versionToEnumMap


    OrganizationRoleName(Map<String, String> versionToEnumMap) {
        this.versionToEnumMap = versionToEnumMap
    }


    public Map<String, String> getVersionToEnumMap() {
        return versionToEnumMap
    }

    /**
     * This is useful in "create" and "update" operations to validate the input string.
     *
     * @param value
     * @param version
     * @return
     */
    public static OrganizationRoleName getByString(String value, String version) {
        if (value) {
            Iterator itr = OrganizationRoleName.values().iterator()
            while (itr.hasNext()) {
                OrganizationRoleName roleName = itr.next()
                if (roleName.versionToEnumMap.containsKey(version) && roleName.versionToEnumMap[version].equalsIgnoreCase(value)) {
                    return roleName
                }
            }
        }
        return null
    }

}
