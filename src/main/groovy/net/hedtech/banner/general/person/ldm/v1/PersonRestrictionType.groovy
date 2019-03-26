/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person.ldm.v1

/**
 * CDM decorator for persons/{guid}/restriction-types resource
 */
class PersonRestrictionType {

    String guid

    PersonRestrictionType(String guid) {
        this.guid = guid
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        PersonRestrictionType that = (PersonRestrictionType) o
        if (guid != that.guid) return false
        return true
    }


    int hashCode() {
        int result
        result = (guid != null ? guid.hashCode() : 0)
        return result
    }


    public String toString() {
        """PersonRestrictionType[
                    guid=$guid]"""
    }
}
