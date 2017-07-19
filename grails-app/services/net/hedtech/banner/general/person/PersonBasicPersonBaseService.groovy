/*********************************************************************************
 Copyright 2012-2017 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.InstitutionalDescription
import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

class PersonBasicPersonBaseService extends ServiceBase {

    boolean transactional = true


    def preCreate(domainModelOrMap) {
        def domain = domainModelOrMap instanceof Map ? domainModelOrMap?.domainModel : domainModelOrMap

        if (domain) {
            validateSsn(domain)
        }
    }


    def preUpdate(domainModelOrMap) {
        def domain = domainModelOrMap instanceof Map ? domainModelOrMap?.domainModel : domainModelOrMap

        if (domain) {
            if (PersonUtility.isDirtyProperty(PersonBasicPersonBase, domain, "ssn")) {
                validateSsn(domain)
            }
        }
    }

    def getPersonalDetailsForPersonalInformation(pidm) {
        def personalDetails = [:]
        def personBase = PersonBasicPersonBase.fetchByPidm(pidm)
        if (personBase) {
            personalDetails = [
                    id                           : personBase.id,
                    version                      : personBase.version,
                    preferenceFirstName          : personBase.preferenceFirstName,
                    sex                          : personBase.sex,
                    birthDate                    : personBase.birthDate,
                    maritalStatus                : personBase.maritalStatus,
                    ethnic                       : personBase.ethnic,
                    activeDutySeprDate           : personBase.activeDutySeprDate,
                    armedServiceMedalVetIndicator: personBase.armedServiceMedalVetIndicator,
                    sdvetIndicator               : personBase.sdvetIndicator,
                    vetcFileNumber               : personBase.vetcFileNumber,
                    veraIndicator                : personBase.veraIndicator
            ]
        }

        return personalDetails
    }

    private def validateSsn(domain) {
        def institutionalDescription = InstitutionalDescription.fetchByKey()

        if (domain.ssn && institutionalDescription.ssnMaximumLength) {
            if (domain.ssn.length() > institutionalDescription.ssnMaximumLength) {
                throw new ApplicationException(PersonBasicPersonBase, "@@r1:ssnMaximumLengthExceeded:${institutionalDescription.ssnMaximumLength}@@")
            }
        }
    }

}
