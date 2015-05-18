/*******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

/**
 * LDM Decorator for person resource names.
 */
public class Name {

    String firstName
    String middleName
    String lastName
    String surnamePrefix
    String nameType
    String title
    String pedigree
    String preferredName

    def Name(def personName, def person) {
        this.firstName = personName?.firstName
        this.middleName = personName?.middleName
        this.lastName = personName?.lastName
        this.surnamePrefix = personName?.surnamePrefix
        this.title = person?.namePrefix
        this.pedigree = person?.nameSuffix
        this.preferredName = person?.preferenceFirstName
    }


    def Name(def personName) {
        this.firstName = personName?.firstName
        this.middleName = personName?.middleName
        this.lastName = personName?.lastName
        this.surnamePrefix = personName?.surnamePrefix
        this.title = personName?.namePrefix
        this.pedigree = personName?.nameSuffix
        this.preferredName = personName?.preferenceFirstName
    }


    def getNameType() {
        this.nameType
    }

    def setNameType(String nameType) {
        this.nameType = nameType
    }
}
