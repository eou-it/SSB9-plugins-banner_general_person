/*********************************************************************************
Copyright 2016 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
package net.hedtech.banner.general.person

import net.hedtech.banner.person.PersonTelephoneDecorator
import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

class PersonTelephoneService extends ServiceBase {

    boolean transactional = true


    def fetchActiveTelephonesByPidm(pidm, sequenceConfig = null, includeUnlisted = false) {
        def telephoneRecords = includeUnlisted ? PersonTelephone.fetchActiveTelephoneWithUnlistedByPidm(pidm) :
                                                 PersonTelephone.fetchActiveTelephoneByPidm(pidm)
        def telephone
        def telephones = []
        def decorator
        def telephoneDisplaySequence = PersonUtility.getDisplaySequence('telephoneDisplaySequence', sequenceConfig)

        telephoneRecords.each { it ->
            telephone = [:]
            telephone.id = it.id
            telephone.version = it.version
            telephone.telephoneType = it.telephoneType
            telephone.displayPriority = telephoneDisplaySequence ? telephoneDisplaySequence[telephone.telephoneType.code] : null
            telephone.internationalAccess = it.internationalAccess
            telephone.countryPhone = it.countryPhone
            telephone.phoneArea = it.phoneArea
            telephone.phoneNumber = it.phoneNumber
            telephone.phoneExtension = it.phoneExtension
            telephone.unlistIndicator = it.unlistIndicator
            telephone.primaryIndicator = it.primaryIndicator

            decorator = new PersonTelephoneDecorator(it)
            telephone.displayPhoneNumber = decorator.displayPhone

            telephones << telephone
        }

        return telephones
    }

}
