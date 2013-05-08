/*******************************************************************************
Copyright 2009-2012 Ellucian Company L.P. and its affiliates.
*******************************************************************************/ 


package net.hedtech.banner.person

import net.hedtech.banner.general.person.PersonIdentificationName
import net.hedtech.banner.general.person.PersonUtility

class PersonIdentificationNameDecorator {

    Integer pidm
    String firstName
    String lastName
    String middleName
    String displayName
    String surnamePrefix
    String bannerId


    PersonIdentificationNameDecorator(PersonIdentificationName person) {
        pidm = person.pidm
        firstName = person.firstName
        lastName = person.lastName
        middleName = person.middleName
        surnamePrefix = person.surnamePrefix
        bannerId = person.bannerId
        formatDisplayName()
    }


    PersonIdentificationNameDecorator(Map person) {
        pidm = person.pidm
        firstName = person.firstName
        lastName = person.lastName
        middleName = person.middleName
        surnamePrefix = person.surnamePrefix
        bannerId = person.bannerId
        formatDisplayName()
    }


    PersonIdentificationNameDecorator() {
        pidm = null
        firstName = null
        lastName = null
        middleName = null
        surnamePrefix = null
        bannerId = null
        displayName = null
    }


    public String toString() {
        """PersonIdentificationNameDecorator[
                   pidm=$pidm,
                   bannerId=$bannerId,
	               firstName=$firstName,
	               lastName=$lastName,
                   middleName=$middleName,
                   surnamePrefix=$surnamePrefix,
                   displayName=$displayName
                   ]"""
    }


    def formatDisplayName(def showSurname = 'N') {
        def surname
        if (showSurname == "Y" && surnamePrefix) {
            surname = surnamePrefix
        }

        //build a new map with person info for retrieving formatted name from Name Utility
        def person = [lastName: lastName, firstName: firstName, middleName:middleName, surnamePrefix: surname]
        displayName = PersonUtility.formatName(person)
        return displayName
    }

}
