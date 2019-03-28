/*******************************************************************************
 Copyright 2014-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

import grails.validation.Validateable
import net.hedtech.banner.general.person.PersonIdentificationNameCurrent
import org.grails.datastore.gorm.GormValidateable
import org.grails.datastore.mapping.dirty.checking.DirtyCheckable

/**
 * LDM Decorator for person resource names.
 */
public class Name implements GormValidateable, DirtyCheckable, Validateable {

    @Delegate
    private final PersonIdentificationNameCurrent personName
    String nameType
    String title
    String pedigree
    String preferredName

    def Name(PersonIdentificationNameCurrent personName, def person) {
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
