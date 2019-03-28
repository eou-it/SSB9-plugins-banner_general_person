/*******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

import grails.validation.Validateable
import net.hedtech.banner.general.person.PersonIdentificationNameAlternate
import org.grails.datastore.gorm.GormValidateable
import org.grails.datastore.mapping.dirty.checking.DirtyCheckable

/**
 * LDM Decorator for person resource names.
 */
public class NameAlternate implements GormValidateable, DirtyCheckable, Validateable {

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
