/*********************************************************************************
Copyright 2012-2017 Ellucian Company L.P. and its affiliates.
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

    def createEmergencyContactWithPriorityShuffle(newContact) {
        def contacts = getEmergencyContactsByPidm(newContact.pidm)
        def toBeCreated = []

        if (contacts) {
            def toBeDeleted = []

            contacts.each { it ->
                def itPriority = it.priority as Integer

                if (itPriority >= (newContact.priority as Integer)) {
                    // The new contact is being inserted somewhere before this one, so we have to re-set its
                    // priority.  As priority is not updateable, we need to delete and re-create.
                    toBeDeleted << it
                    def contact = populateEmergencyContact(it)
                    contact.priority = itPriority + 1 // Bump it up to make a place for the new contact
                    toBeCreated <<contact
                }
            }

            // Delete the existing ones which have changed priority
            if (toBeDeleted) {
                delete(toBeDeleted)
            }
        }

        // (Re)create emergency contacts for user
        toBeCreated << newContact
        create(toBeCreated)
    }

    def updateEmergencyContactWithPriorityShuffle(updatedContact) {
        def contacts = getEmergencyContactsByPidm(updatedContact.pidm)

        def existingWithSamePriority = contacts.find {
            it.id == updatedContact.id && it.priority == updatedContact.priority
        }

        if (existingWithSamePriority) {
            // Priority has not been updated; this is a simple update of just one contact
            update(updatedContact)
        } else {
            // Priority has changed in the updated contact; priorities of other contacts may need to be shuffled
            def toBeCreated = []
            def toBeDeleted = []
            def currentPriority = 1

            contacts.each {
                if (it.id == updatedContact.id) {
                    toBeDeleted << it // Deleting, as it will be replaced by the updated one
                    toBeCreated << updatedContact
                } else {
                    if (currentPriority == updatedContact.priority) {
                        currentPriority++ // Bump past this position reserved for the updated contact
                    }

                    if (currentPriority != (it.priority as Integer)) {
                        // Priority on this needs to change due to the new priority of the updated contact. As priority
                        // is not updateable, we need to delete and re-create.
                        toBeDeleted << it
                        def contact = populateEmergencyContact(it)
                        contact.priority = currentPriority
                        toBeCreated << contact
                    }

                    currentPriority++
                }
            }

            // Delete the existing ones which have changed priority
            if (toBeDeleted) {
                delete(toBeDeleted)
            }

            // (Re)create emergency contacts for user
            create(toBeCreated)
        }
    }

    def deleteEmergencyContactWithPriorityShuffle(contactToDelete) {
        def contacts = getEmergencyContactsByPidm(contactToDelete.pidm)
        def toBeDeleted = []
        def toBeCreated = []

        contacts.each {
            if (it.id == contactToDelete.id) {
                toBeDeleted << it
            } else {
                def itPriority = it.priority as Integer
                def contactToDeletePriority = contactToDelete.priority as Integer

                if (itPriority > contactToDeletePriority) {
                    // Priority on this needs to change due to the deleted contact. As priority
                    // is not updateable, we need to delete and re-create.
                    toBeDeleted << it
                    def contact = populateEmergencyContact(it)
                    contact.priority = itPriority - 1 // Bump it down due to the deleted contact above it
                    toBeCreated << contact
                }
            }
        }

        // Delete the one specified for deletion as well as the existing ones which have changed priority
        delete(toBeDeleted)

        // Recreate emergency contacts for user
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
