/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase


class PersonAddressExtendedPropertiesService extends ServiceBase {
    boolean transactional = true

    def preCreate(map) {
        throwUnsupportedException()
    }

    def preDelete(map) {
        throwUnsupportedException()
    }

    def throwUnsupportedException() {
        throw new ApplicationException(PersonAddressExtendedPropertiesService, "@@r1:unsupported.operation@@")
    }

    List<PersonAddressExtendedProperties> fetchAllBySurrogateIds(Collection<Long> surrogateIds) {
        def entities = []
        if (surrogateIds) {
            PersonAddressExtendedProperties.withSession { session ->
                def query = session.getNamedQuery('PersonAddressExtendedProperties.fetchAllBySurrogateIds')
                query.with {
                    setParameterList('surrogateIds', surrogateIds)
                    entities = list()
                }
            }
        }
        return entities
    }

}
