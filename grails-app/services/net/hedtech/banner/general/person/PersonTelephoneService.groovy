/*********************************************************************************
 Copyright 2012-2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.person

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


}
