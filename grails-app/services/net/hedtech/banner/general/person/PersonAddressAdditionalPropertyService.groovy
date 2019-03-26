/*******************************************************************************
 Copyright 2016-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import grails.gorm.transactions.Transactional
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.SystemUtility
import net.hedtech.banner.service.ServiceBase


@Transactional
class PersonAddressAdditionalPropertyService extends ServiceBase {

    def preCreate(map) {
        throwUnsupportedException()
    }

    def throwUnsupportedException() {
        throw new ApplicationException(PersonAddressAdditionalPropertyService, "@@r1:unsupported.operation@@")
    }

    List<PersonAddressAdditionalProperty> fetchAllBySurrogateIds(Collection<Long> surrogateIds) {
        List entities = []
        if (surrogateIds) {
            def surrogateIdList = SystemUtility.splitList(surrogateIds, 1000)
            surrogateIdList.each { surrogateIdPartition ->
                List entitiesPartition = PersonAddressAdditionalProperty.withSession { session ->
                    def query = session.getNamedQuery('PersonAddressAdditionalProperty.fetchAllBySurrogateIds')
                    query.with {
                        setParameterList('surrogateIds', surrogateIdPartition)
                        list()
                    }
                }
                entities.addAll(entitiesPartition)
            }
        }
        return entities
    }

}
