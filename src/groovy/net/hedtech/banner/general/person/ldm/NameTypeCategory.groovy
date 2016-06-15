/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

enum NameTypeCategory {

    PERSONAL("Primary", "personal"),
    BIRTH("Birth", "birth"),
    LEGAL(null, "legal"),

    private final String v3Enum
    private final String v6Enum


    NameTypeCategory(String v3Enum, String v6Enum) {
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
     * Given a string like "Primary" returns corresponding enum PERSONAL.
     * This is useful in "create" and "update" operations to validate the input string.
     *
     * @param value
     * @return
     */
    public static NameTypeCategory getByString(String value) {
        Iterator itr = NameTypeCategory.values().iterator()
        while (itr.hasNext()) {
            NameTypeCategory nameTypeCategory = itr.next()
            def vals = []
            if (nameTypeCategory.v3Enum) {
                vals << nameTypeCategory.v3Enum
            }
            if (nameTypeCategory.v6Enum) {
                vals << nameTypeCategory.v6Enum
            }
            if (vals.contains(value)) {
                return nameTypeCategory
            }
        }
        return null
    }

}
