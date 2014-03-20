/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.InstitutionalDescription
import net.hedtech.banner.service.ServiceBase

// NOTE:
// This supports CRUD operations on the alternate ID record.  In other words, SPRIDEN records that have an "I" or "N"
// in the changeIndicator field.  CRUD support for current IDs, SPRIDEN records with a null value in the changeIndicator
// field, is located in the PersonIdentificationNameCurrentService.

// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure.

class PersonIdentificationNameAlternateService extends ServiceBase {

    boolean transactional = true
    def sessionFactory


    def preCreate(domainModelOrMap) {
        def domain = domainModelOrMap instanceof Map ? domainModelOrMap?.domainModel : domainModelOrMap

        if (domain) {
            validateChangeIndicator(domain.changeIndicator)
            checkBannerIdOrNameExists(domain)
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
            checkBannerIdOrNameExists(domain)
        }
    }


    private def validateChangeIndicator(changeIndicator) {
        if (!changeIndicator) {
            throw new ApplicationException(PersonIdentificationNameAlternate, "@@r1:changeIndicatorCannotBeNull@@")
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