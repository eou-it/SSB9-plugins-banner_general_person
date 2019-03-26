/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

enum RoleName {

    STUDENT([v3: "Student", v6: "student"]),
    INSTRUCTOR([v3: "Faculty", v6: "instructor"]),
    EMPLOYEE([v6: "employee"]),
    VENDOR([v6: "vendor"]),
    ALUMNI([v6: "alumni"]),
    PROSPECTIVE_STUDENT([v6: "prospectiveStudent"]),
    ADVISOR([v6: "advisor"])

    private final Map<String, String> versionToEnumMap


    RoleName(Map<String, String> versionToEnumMap) {
        this.versionToEnumMap = versionToEnumMap
    }


    public Map<String, String> getVersionToEnumMap() {
        return versionToEnumMap
    }

    /**
     * Given "Faculty" and "v3" returns corresponding enum INSTRUCTOR.
     * This is useful in "create" and "update" operations to validate the input string.
     *
     * @param value
     * @param version
     * @return
     */
    public static RoleName getByString(String value, String version) {
        if (value) {
            Iterator itr = RoleName.values().iterator()
            while (itr.hasNext()) {
                RoleName roleName = itr.next()
                if (roleName.versionToEnumMap.containsKey(version) && roleName.versionToEnumMap[version].equalsIgnoreCase(value)) {
                    return roleName
                }
            }
        }
        return null
    }

}
