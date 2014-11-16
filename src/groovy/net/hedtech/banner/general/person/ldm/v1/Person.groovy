/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v1

import net.hedtech.banner.general.person.PersonBasicPersonBase
import net.hedtech.banner.general.system.ldm.v1.Metadata

/**
 * LDM Decorator for person resource.
 */
class Person {
    @Delegate private final PersonBasicPersonBase person
    Metadata metadata
    def maritalStatusDetail
    def ethnicityDetail
    String guid
    List credentials = []
    List addresses = []
    List phones = []
    List emails = []
    List names = []
    List races = []
    List roles = []

    def Person(PersonBasicPersonBase person,
             def guid,
             def credentials,
             def addresses,
             def phones,
             def emails,
             def names,
             def maritalStatus,
             def ethnicity,
             def races,
             def roles) {
        // PersonBasicPersonBase is optional, create blank object if none exists.
        this.person = person ?: new PersonBasicPersonBase()
        this.guid = guid instanceof String ? guid : ""
        this.credentials = credentials instanceof List ? credentials : null
        this.addresses = addresses instanceof List ? addresses : null
        this.phones = phones instanceof List ? phones : null
        this.emails = emails instanceof List ? emails : null
        this.names = names instanceof List ? names : null
        this.races = races instanceof List ? races : null
        this.roles = roles instanceof List ? roles : null
        this.maritalStatusDetail = maritalStatus
        this.ethnicityDetail = ethnicity
        this.metadata = new Metadata (this.person.dataOrigin)

    }

    def Person(PersonBasicPersonBase person) {
        // PersonBasicPersonBase is optional, create blank unpersisted object if none exists.
        this.person = person ?: new PersonBasicPersonBase()
        this.metadata = new Metadata (this.person.dataOrigin)
    }

    def getSex() {
        this.person?.sex == 'M' ? "Male":
                (this.person?.sex == 'F' ? "Female" :
                        ( this.person?.sex == 'N' ? "Unknown" : null))
    }
}
