/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v6

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Decorator used in EEDM "persons" V6
 *
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class NameV6 {

    def type
    String fullName
    String preference = "preferred"
    String firstName
    String middleName
    String lastName
    String lastNamePrefix
    String title
    String pedigree

    String preferredName


    public static String getFullName(String firstName, String middleName, String lastName) {
        StringBuilder sb = new StringBuilder()

        if (firstName) {
            sb.append(firstName)
            sb.append(' ')
        }

        if (middleName) {
            sb.append(middleName)
            sb.append(' ')
        }

        if (lastName) {
            sb.append(lastName)
        }

        return sb.toString()
    }

}
