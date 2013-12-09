/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.InstitutionalDescription
import net.hedtech.banner.service.ServiceBase

import java.sql.CallableStatement
import java.sql.SQLException

/**
 *  This service should only be used for retrieving PersonIdentificationNameCurrent data.
 *  Use of this service for CUD operation should be avoided since the standard CUD behavior
 *  has been overridden in the composite service PersonIdentificationNameCompositeService.
 */

class PersonIdentificationNameCurrentService extends ServiceBase {

    boolean transactional = true
    def sessionFactory


    def preCreate(domainModelOrMap) {
        def domain = domainModelOrMap instanceof Map ? domainModelOrMap?.domainModel : domainModelOrMap

        if (domain) {
            validateChangeIndicator(domain.changeIndicator)
            validateId(domain)
        }
    }


    def preDelete(domainModelOrMap) {
        def domain = domainModelOrMap instanceof Map ? domainModelOrMap?.domainModel : domainModelOrMap

        if (domain) {
            validateChangeIndicator(domain.changeIndicator)
        }
    }


    def preUpdate(domainModelOrMap) {
        def domain = domainModelOrMap instanceof Map ? domainModelOrMap?.domainModel : domainModelOrMap

        if (domain) {
            validateChangeIndicator(domain.changeIndicator)

            if (PersonUtility.isDirtyProperty(PersonIdentificationNameCurrent, domain, "bannerId")) {
                validateId(domain)
            }
        }
    }


    private def validateChangeIndicator(changeIndicator) {
        if (changeIndicator) {
            throw new ApplicationException(PersonIdentificationNameCurrent, "@@r1:changeIndicatorMustBeNull@@")
        }
    }


    private def validateId(domain) {
        def piiActive
        def institutionalDescription = InstitutionalDescription.fetchByKey()

        // If SSN searching is enabled, check if ID exists as a SSN/SIN/TIN.
        if (institutionalDescription?.ssnSearchEnabledIndicator) {
            try {
                piiActive = PersonUtility.isPiiActive()

                if (piiActive) {
                    PersonUtility.turnFgacOff()
                }
                def bioList = PersonBasicPersonBase.fetchBySsn(domain.bannerId)
                if (bioList.size() > 0) {
                    throw new ApplicationException(PersonIdentificationNameCurrent, "@@r1:bannerIdExistsAsSsn@@")
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

}
