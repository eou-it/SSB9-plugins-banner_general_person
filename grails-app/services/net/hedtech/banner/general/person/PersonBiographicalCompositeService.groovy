/*********************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

/**
 * This composite service handles PersonBasicPersonBase and PersonRace domain CRUD as one transaction.
 * Valid map keys are:
 * deletePersonRaces - deletes a collection of PersonRace
 * deletePersonBasicPersonBase - deletes a single PersonBasicPersonBase
 * personBasicPersonBase - inserts or updates a single PersonBasicPersonBase
 * personRaces - insert or updates a collection of PersonRace
 */
class PersonBiographicalCompositeService {

    boolean transactional = true
    def sessionFactory
    def log = Logger.getLogger(this.getClass())

    def personBasicPersonBaseService
    def personRaceService


    public def createOrUpdate(map) {
        if (map?.deletePersonRaces) {
            deleteDomains(map?.deletePersonRaces, personRaceService)
        }

        if (map?.deletePersonBasicPersonBase) {
            deleteDomains([map?.deletePersonBasicPersonBase], personBasicPersonBaseService)
        }

        if (map?.personBasicPersonBase) {
            processInsertUpdates(map?.personBasicPersonBase, personBasicPersonBaseService)
        }

        if (map?.personRaces) {
            processInsertUpdates(map?.personRaces, personRaceService)
        }
    }

    /**
     *  Delete domains.
     */
    private def deleteDomains(domainList, service) {
        domainList.each { domain ->
            def map = [domainModel: domain]
            service.delete(map)
        }
    }

    /**
     * Insert or update domains.
     */
    private def processInsertUpdates(domainList, service) {
        domainList.each { domain ->
            if (domain.id) {
                service.update(domainModel: domain)
            } else {
                service.create(domainModel: domain)
            }
        }
    }

}
