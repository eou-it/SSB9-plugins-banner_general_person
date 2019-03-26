/*********************************************************************************
 Copyright 2012-2019 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.person

import grails.gorm.transactions.Transactional
import net.hedtech.banner.person.PersonTelephoneDecorator
import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure
@Transactional
class PersonTelephoneService extends ServiceBase {

    /**
     * fetch Active Person Phone information based on list on pidms and list of phone type codes
     * @param pidm
     * @param phoneTypes
     * @return List < PersonTelephone >
     */
    List<PersonTelephone> fetchAllActiveByPidmInListAndTelephoneTypeCodeInList(Collection<Integer> pidms, Collection<String> phoneTypes) {
        List<PersonTelephone> entities = []
        if (pidms && phoneTypes) {
            entities = PersonTelephone.withSession { session ->
                def namedQuery = session.getNamedQuery('PersonTelephone.fetchAllActiveByPidmInListAndTelephoneTypeCodeInList')
                namedQuery.with {
                    setParameterList('pidms', pidms)
                    setParameterList('telephoneTypes', phoneTypes)
                    list()
                }
            }
        }
        return entities
    }

    List<PersonTelephone> fetchAllByIdInListAndTelephoneTypeCodeInList(Collection<Long> ids, Collection<String> phoneTypes) {
        List<PersonTelephone> entities = []
        if (ids && phoneTypes) {
            entities = PersonTelephone.withSession { session ->
                def namedQuery = session.getNamedQuery('PersonTelephone.fetchAllByIdInListAndTelephoneTypeCodeInList')
                namedQuery.with {
                    setParameterList('ids', ids)
                    setParameterList('telephoneTypes', phoneTypes)
                    list()
                }
            }
        }
        return entities
    }

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
            telephone.sequenceNumber = it.sequenceNumber
            telephone.unlistIndicator = it.unlistIndicator
            telephone.primaryIndicator = it.primaryIndicator

            decorator = new PersonTelephoneDecorator(it)
            telephone.displayPhoneNumber = decorator.displayPhone

            telephones << telephone
        }

        return telephones
    }

    void inactivatePhone(phone) {
        def phoneToInactivate = PersonTelephone.get(phone.id)

        phoneToInactivate.statusIndicator = 'I'
        update(phoneToInactivate)
    }

    void inactivateAndCreate(phone) {
        inactivatePhone(phone)

        phone.remove('id');
        phone.remove('version');
        phone.statusIndicator = null
        create(phone)
    }

}
