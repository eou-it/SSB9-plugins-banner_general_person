/*********************************************************************************
 Copyright 2012-2019 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import grails.gorm.transactions.Transactional
import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

/**
 *  This service is used to process PersonIdentificationNameCurrent,
 *  PersonIdentificationNameAlternate, and PersonBasicPersonBase changes.
 */
@Transactional
class PersonIdentificationNameCompositeService extends ServiceBase {

    def sessionFactory
    def personIdentificationNameCurrentService
    def personIdentificationNameAlternateService
    def personBasicPersonBaseService


    public def createOrUpdate(map) {
        if (map?.deletePersonIdentificationNameCurrents) {
            deleteDomains(map?.deletePersonIdentificationNameCurrents, personIdentificationNameCurrentService)
        }

        if (map?.deletePersonIdentificationNameAlternates) {
            deleteDomains(map?.deletePersonIdentificationNameAlternates, personIdentificationNameAlternateService)
        }

        if (map?.personIdentificationNameCurrent) {
            processInsertUpdates([map?.personIdentificationNameCurrent], personIdentificationNameCurrentService)
        }

        if (map?.personIdentificationNameCurrents) {
            processInsertUpdates(map?.personIdentificationNameCurrents, personIdentificationNameCurrentService)
        }

        if (map?.personIdentificationNameAlternates) {
            processInsertUpdates(map?.personIdentificationNameAlternates, personIdentificationNameAlternateService)
        }

        if (map?.basicPerson) {
            processInsertUpdates([map?.basicPerson], personBasicPersonBaseService)
        }
    }

    /**
     *  Delete PersonIdentificationNameCurrent and/or PersonIdentificationNameAlternate domains.
     *
     *  NOTE: When deleting a PersonIdentificationNameAlternate, the PL/SQL API will also update the
     *  remaining PersonIdentificationNameAlternates. After the service delete has completed
     *  on the given PersonIdentificationNameAlternate, all other PersonIdentificationNameAlternate objects
     *  need to be refreshed in Hibernate.
     */
    private def deleteDomains(deleteList, service) {
        def map
        def pidm

        deleteList.each { domain ->
            map = [domainModel: domain]

            if (domain instanceof PersonIdentificationNameAlternate) {
                pidm = domain.pidm
                service.delete(map)
                // Refresh all remaining alternate id records when an alternate id is deleted.
                def updatedPersons = PersonIdentificationNameAlternate.fetchAllByPidm(pidm)
            } else {
                service.delete(map)
            }
        }
    }

    /**
     * Insert or update domains.
     *
     * NOTE: When changing the ID or name of the PersonIdentificationNameCurrent, the PL/SQL API will
     * also inserts a PersonIdentificationNameAlternate as an audit record. After the service update has completed
     * on PersonIdentificationNameCurrent, the PersonIdentificationNameAlternate objects need to be
     * refreshed in Hibernate.
     */
    private def processInsertUpdates(domainList, service) {

        domainList.each { domain ->
            if (domain.id) {
                service.update(domainModel: domain)

                if (domain instanceof PersonIdentificationNameCurrent) {
                    def updatedAlternates = PersonIdentificationNameAlternate.fetchAllByPidm(domain.pidm)
                }
            } else {
                service.create(domainModel: domain)
            }
        }
    }

}
