/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

import net.hedtech.banner.general.person.PersonIdentificationNameAlternate

/**
 * LDM Decorator for person resource names.
 */
public class NameAlternate {

    @Delegate
    private final PersonIdentificationNameAlternate personNameAlternate
    String nameType

    def NameAlternate(PersonIdentificationNameAlternate personNameAlternate) {
        this.personNameAlternate = personNameAlternate
    }

    def getNameType() {
        this.nameType
    }

    def setNameType(String nameType) {
        this.nameType = nameType
    }
}
