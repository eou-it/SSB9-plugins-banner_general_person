/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.SdaCrosswalkConversion
import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

class PersonRaceService extends ServiceBase {

    boolean transactional = true
    /*def sessionFactory*/

    def createOrUpdate(map) {
        if (map?.deletePersonRaces) {
            processDeletes(map.deletePersonRaces)
        }
        if (map?.createPersonRaces) {
            processInsertUpdates(map.createPersonRaces)
        }
    }


    private void processDeletes(domains) {
        domains.each { domain ->
            this.delete(domain)
        }
    }


    private void processInsertUpdates(domains) {
        domains.each { personRace ->
            if (personRace.id == null) {
                this.create([domainModel: personRace])
            } else {
                this.update([domainModel: personRace])
            }
        }
    }

}
