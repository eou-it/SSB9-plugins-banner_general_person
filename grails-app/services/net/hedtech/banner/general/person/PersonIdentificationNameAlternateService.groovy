/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.InstitutionalDescription
import net.hedtech.banner.service.ServiceBase

/**
 *  This service should only be used for retrieving PersonIdentificationNameAlternate data.
 *  Use of this service for CUD operation should be avoided since the standard CUD behavior
 *  has been overridden in the composite service PersonIdentificationNameCompositeService.
 */

class PersonIdentificationNameAlternateService extends ServiceBase {

    boolean transactional = true
    def sessionFactory


    def preCreate(map) {
        if (map?.domainModel) {
            if (!map.domainModel.changeIndicator) {
                throw new ApplicationException(PersonIdentificationNameCurrent, "@@r1:changeIndicatorCannotBeNull@@")
            }

            checkBannerIdOrNameExists(map.domainModel)
        }
    }


    def preDelete(map) {
        if (!map?.domainModel?.changeIndicator) {
            throw new ApplicationException(PersonIdentificationNameCurrent, "@@r1:changeIndicatorCannotBeNull@@")
        }
    }


    def preUpdate(map) {
        if (map?.domainModel) {
            if (!map?.domainModel?.changeIndicator) {
                throw new ApplicationException(PersonIdentificationNameCurrent, "@@r1:changeIndicatorCannotBeNull@@")
            }

            checkBannerIdOrNameExists(map.domainModel)
        }
    }


    private def checkBannerIdOrNameExists(domain) {
        def piiActive

        try {
            piiActive = PersonUtility.isPiiActive()

            if (piiActive) {
                PersonUtility.turnFgacOff()
            }
            def exists = PersonIdentificationName.bannerIdOrNameExists(domain.pidm, domain.bannerId, domain.lastName,
                    domain.firstName, domain.middleName, domain.nameType?.code, domain.surnamePrefix)
            if (exists) {
                throw new ApplicationException(PersonIdentificationName, "@@r1:bannerIdOrNameExists@@")
            }
            if (piiActive) {
                PersonUtility.turnFgacOn()
            }

        } catch (Exception e) {
            if (piiActive) {
                PersonUtility.turnFgacOn()
            }
            log.debug e.stackTrace
            throw e
        }
    }
}
