/*********************************************************************************
 Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import grails.gorm.transactions.Transactional
import net.hedtech.banner.exceptions.ApplicationException
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
@Transactional
class PersonIdentificationNameAlternateService extends ServiceBase {

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


    List<PersonIdentificationNameAlternate> fetchAllMostRecentlyCreated(List<Integer> pidms, List<String> nameTypeCodes) {
        List<PersonIdentificationNameAlternate> entities = []
        if (pidms && nameTypeCodes) {
            String hql = """ from PersonIdentificationNameAlternate a
                             where a.pidm in :pidms
                             and a.nameType.code in :nameTypes
                             and a.entityIndicator = 'P'
                             and a.changeIndicator = 'N'
                             and (a.pidm, a.nameType.code, a.createDate) in (select b.pidm, b.nameType.code, max(b.createDate)
                                                                             from PersonIdentificationNameAlternate b
                                                                             where b.entityIndicator = 'P'
                                                                             and b.changeIndicator = 'N'
                                                                             group by b.pidm, b.nameType.code)) """
            PersonIdentificationNameAlternate.withSession { session ->
                def query = session.createQuery(hql)
                query.setParameterList('pidms', pidms)
                query.setParameterList('nameTypes', nameTypeCodes)
                entities = query.list()
            }
        }
        return entities
    }

}
