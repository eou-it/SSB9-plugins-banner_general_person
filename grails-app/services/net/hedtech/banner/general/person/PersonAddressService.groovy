/*******************************************************************************
 Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

class PersonAddressService extends ServiceBase {
    boolean transactional = true


    void preUpdate(domainModelOrMap) {
        def domain = domainModelOrMap instanceof Map ? domainModelOrMap?.domainModel : domainModelOrMap

        if (domain) {
            def dirtyProperties = domain.getDirtyPropertyNames()
            def changes = dirtyProperties.findAll { it == 'addressType' }
            if (changes.size() > 0) {
                log.warn "Attempt to modify ${domain.class} primary key ${changes}"
                throw new RuntimeException("@@r1:primaryKeyFieldsCannotBeModified@@")
            }
        }
    }


    List<PersonAddress> fetchAllByActiveStatusPidmsAndAddressTypes(Collection<Integer> pidms, Collection<String> addressTypeCodes) {
        def entities = []
        if (pidms && addressTypeCodes) {
            PersonAddress.withSession { session ->
                def query = session.getNamedQuery('PersonAddress.fetchAllByActiveStatusPidmsAndAddressTypes')
                query.with {
                    setParameterList('pidms', pidms)
                    setParameterList('addressTypes', addressTypeCodes)
                    entities = list()
                }
            }
        }
        return entities
    }

}
