/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase


class PersonAddressAdditionalPropertyService extends ServiceBase {
    boolean transactional = true

    def preCreate(map) {
        throwUnsupportedException()
    }

    def throwUnsupportedException() {
        throw new ApplicationException(PersonAddressAdditionalPropertyService, "@@r1:unsupported.operation@@")
    }

    List<PersonAddressAdditionalProperty> fetchAllBySurrogateIds(Collection<Long> surrogateIds) {
        def entities = []
        if (surrogateIds) {
            PersonAddressAdditionalProperty.withSession { session ->
                def query = session.getNamedQuery('PersonAddressAdditionalProperty.fetchAllBySurrogateIds')
                query.with {
                    setParameterList('surrogateIds', surrogateIds)
                    entities = list()
                }
            }
        }
        return entities
    }

}
