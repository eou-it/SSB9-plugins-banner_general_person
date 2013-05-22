/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.service.ServiceBase

class PriorCollegeCompositeService extends ServiceBase {

    boolean transactional = true

    def priorCollegeService
    def priorCollegeDegreeService
    def priorCollegeConcentrationAreaService
    def priorCollegeMajorService
    def priorCollegeMinorService


    def createOrUpdate(map) {
        // deletes
        if (map?.deletePriorCollegeConcentrationAreas)
            deleteDomain(map.deletePriorCollegeConcentrationAreas, priorCollegeConcentrationAreaService)

        if (map?.deletePriorCollegeMajors)
            deleteDomain(map.deletePriorCollegeMajors, priorCollegeMajorService)

        if (map?.deletePriorCollegeMinors)
            deleteDomain(map.deletePriorCollegeMinors, priorCollegeMinorService)

        if (map?.deletePriorCollegeDegrees)
            deleteDomain(map.deletePriorCollegeDegrees, priorCollegeDegreeService)

        if (map?.deletePriorColleges)
            deleteDomain(map.deletePriorColleges, priorCollegeService)

        // inserts or updates
        if (map?.priorColleges)
            createOrUpdateDomain(map.priorColleges, priorCollegeService)

        if (map?.priorCollegeDegrees)
            createOrUpdateDomain(map.priorCollegeDegrees, priorCollegeDegreeService)

        if (map?.priorCollegeConcentrationAreas)
            createOrUpdateDomain(map.priorCollegeConcentrationAreas, priorCollegeConcentrationAreaService)

        if (map?.priorCollegeMajors)
            createOrUpdateDomain(map.priorCollegeMajors, priorCollegeMajorService)

        if (map?.priorCollegeMinors)
            createOrUpdateDomain(map.priorCollegeMinors, priorCollegeMinorService)
    }

    /**
     *  Create or Update domains.
     *  NOTE: These domains, PriorCollegeConcentrationArea, PriorCollegeMajor and PriorCollegeMinor are non-updateable;
     *        delete then recreate instead.  Update can only work if none of the other domains are dirty.
     *        Ondelete, GRAILS will call update on any dirty domains.
     */
    private void createOrUpdateDomain(domains, service) {
        domains.each { domain ->
            if (domain.id) {
                if ((domain instanceof PriorCollegeConcentrationArea)
                        || (domain instanceof PriorCollegeMajor)
                        || (domain instanceof PriorCollegeMinor)) {
                    service.delete([domainModel: domain])
                    domain.id = null
                } else
                    service.update([domainModel: domain])
            }

            if (!domain.id)
                service.create([domainModel: domain])
        }
    }


    private void deleteDomain(domains, service) {
        domains.each { domain ->
            service.delete([domainModel: domain])
            domain.id = null
        }
    }
}
