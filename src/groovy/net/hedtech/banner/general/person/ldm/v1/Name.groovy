/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

import net.hedtech.banner.general.person.PersonBasicPersonBase
import net.hedtech.banner.general.person.PersonIdentificationNameCurrent

/**
 * LDM Decorator for person resource names.
 */
public class Name {

    @Delegate private final PersonIdentificationNameCurrent personName
    String nameType
    String title
    String pedigree
    String preferredName

    def Name(PersonIdentificationNameCurrent personName, def person ) {
        this.personName = personName
        this.title = person?.namePrefix
        this.pedigree = person?.nameSuffix
        this.preferredName = person?.preferenceFirstName
    }

    def getNameType() {
        this.nameType
    }

    def setNameType(String nameType) {
        this.nameType = nameType
    }
}
