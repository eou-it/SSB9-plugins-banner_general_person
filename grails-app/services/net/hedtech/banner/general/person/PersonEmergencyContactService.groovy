/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
 package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

class PersonEmergencyContactService extends ServiceBase {

    def getEmergencyContactsByPidm(pidm) {
        def contacts = PersonEmergencyContact.fetchByPidmOrderByPriority(pidm)

        return contacts
    }

    def checkEmergencyContactFieldsValid(contact){
        if(!contact.firstName){
            throw new ApplicationException(PersonAddress, "@@r1:firstNameRequired@@")
        }
        if(!contact.lastName){
            throw new ApplicationException(PersonAddress, "@@r1:lastNameRequired@@")
        }
    }

    def createUpdateOrDeleteEmergencyContactWithPriorityShuffle(updatedContact, isDelete=false) {
        // If priority has been changed, in addition to any updates to the affected contact
        // we need to shuffle priorities for all contacts.
        // NOTE: Priority is not updatable, so the strategy is to delete and then re-insert
        // all pre-existing contacts.

        def contacts = getEmergencyContactsByPidm(updatedContact.pidm)
        def toBeCreated = []

        if (contacts) {
            // Create list of EXISTING contacts WITHOUT the updated (or new) one
            contacts.each { it ->
                if (it.id != updatedContact.id) {
                    // Create a new map that can be inserted back into the table
                    def contact = populateEmergencyContact(it)
                    toBeCreated.push(contact)
                }
            }

            if (!isDelete) {
                // Insert new or updated contact at proper location.
                // (Priority is one-based, while its place in the list is zero-based, so make the adjustment.)
                def updatedContactPriority = updatedContact.priority as Integer
                toBeCreated.add(updatedContactPriority - 1, updatedContact)
            }

            // Sweep through, setting priority on each one
            def currentPriority = 1

            toBeCreated.each { it ->
                it.priority = currentPriority++
            }

            // Delete all emergency contacts for user
            delete(contacts)
        } else if (!isDelete) {
            // No emergency contacts exist; just insert this one
            toBeCreated.push(updatedContact)
        }

        // (Re)create emergency contacts for user
        create(toBeCreated)
    }

    def populateEmergencyContact(entity) {
        [
                pidm: entity.pidm,
                priority: entity.priority,
                relationship: entity.relationship,
                firstName: entity.firstName,
                middleInitial: entity.middleInitial,
                lastName: entity.lastName,
                surnamePrefix: entity.surnamePrefix,
                addressType: entity.addressType,
                houseNumber: entity.houseNumber,
                streetLine1: entity.streetLine1,
                streetLine2: entity.streetLine2,
                streetLine3: entity.streetLine3,
                streetLine4: entity.streetLine4,
                city: entity.city,
                zip: entity.zip,
                state: entity.state,
                nation: entity.nation,
                phoneArea: entity.phoneArea,
                phoneNumber: entity.phoneNumber,
                phoneExtension: entity.phoneExtension,
                countryPhone: entity.countryPhone
        ]
    }
}
