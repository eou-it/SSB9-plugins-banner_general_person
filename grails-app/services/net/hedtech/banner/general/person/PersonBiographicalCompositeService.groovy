/*********************************************************************************
Copyright 2012-2019 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
 package net.hedtech.banner.general.person

import grails.gorm.transactions.Transactional

/**
 * This composite service handles PersonBasicPersonBase and PersonRace domain CRUD as one transaction.
 * Valid map keys are:
 * deletePersonRaces - deletes a collection of PersonRace
 * deletePersonBasicPersonBase - deletes a single PersonBasicPersonBase
 * personBasicPersonBase - inserts or updates a single PersonBasicPersonBase
 * personRaces - insert or updates a collection of PersonRace
 */
@Transactional
class PersonBiographicalCompositeService {

    def sessionFactory
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
