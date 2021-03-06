/*******************************************************************************
 Copyright 2013-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import grails.gorm.transactions.Transactional
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure
@Transactional
class PersonAddressService extends ServiceBase {

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

    def getActiveAddresses(map) {
        def activeAddresses = PersonAddress.fetchActiveAddressesByPidm(map)

        return activeAddresses
    }

    def checkAddressFieldsValid(address){
        if(!address.fromDate){
            throw new ApplicationException(PersonAddress, "@@r1:fromDateRequired@@")
        }
        if(!address.streetLine1){
            throw new ApplicationException(PersonAddress, "@@r1:streetLine1Required@@")
        }
    }
}
