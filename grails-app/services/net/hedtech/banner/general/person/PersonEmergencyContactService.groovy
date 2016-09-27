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
}
