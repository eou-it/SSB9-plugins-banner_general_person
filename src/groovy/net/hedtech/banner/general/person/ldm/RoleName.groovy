/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

enum RoleName {

    STUDENT("Student", "student"),
    INSTRUCTOR("Faculty", "instructor"),
    EMPLOYEE(null, "employee"),
    VENDOR(null, "vendor"),
    ALUMNI(null, "alumni"),
    PROSPECTIVE_STUDENT(null, "prospectiveStudent"),
    ADVISOR(null, "advisor")

    private final String v3Enum
    private final String v6Enum


    RoleName(String v3Enum, String v6Enum) {
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
     * Given a string like "Faculty" returns corresponding enum INSTRUCTOR.
     * This is useful in "create" and "update" operations to validate the input string.
     *
     * @param value
     * @return
     */
    public static RoleName getByString(String value) {
        Iterator itr = RoleName.values().iterator()
        while (itr.hasNext()) {
            RoleName roleName = itr.next()
            def vals = []
            if (roleName.v3Enum) {
                vals << roleName.v3Enum
            }
            if (roleName.v6Enum) {
                vals << roleName.v6Enum
            }
            if (vals.contains(value)) {
                return roleName
            }
        }
        return null
    }

}
