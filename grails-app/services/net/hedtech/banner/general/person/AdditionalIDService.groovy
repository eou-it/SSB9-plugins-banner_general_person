/*********************************************************************************
 Copyright 2012-2019 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.person

import grails.gorm.transactions.Transactional
import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

@Transactional
class AdditionalIDService extends ServiceBase {

    def fetchAllByPidmInListAndIdentificationTypeCodeInList(Collection<Integer> pidms, Collection<String> identificationTypeCodes) {
        def list = []
        if (pidms && identificationTypeCodes) {
            AdditionalID.withSession { session ->
                list = session.getNamedQuery('AdditionalId.fetchAllByPidmInListAndAdditionalIdentificationTypeInList')
                        .setParameterList('pidms', pidms)
                        .setParameterList('idTypes', identificationTypeCodes)
                        .list()
            }
        }
        return list
    }

    def fetchAllByPidmInList(Collection<Integer> pidms) {
        def list = []
        if (pidms) {
            AdditionalID.withSession { session ->
                list = session.getNamedQuery('AdditionalId.fetchAllByPidmInList')
                        .setParameterList('pidms', pidms)
                        .list()
            }
        }
        return list
    }
}
