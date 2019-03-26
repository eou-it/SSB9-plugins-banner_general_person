/*********************************************************************************
 Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import grails.gorm.transactions.Transactional
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.system.InstitutionalDescription
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.service.ServiceBase

// NOTE:
// This supports CRUD operations on the current ID record.  In other words, SPRIDEN records that have a null value
// in the changeIndicator field.  CRUD support for alternate IDs, SPRIDEN records with "I" or "N" in the changeIndicator
// field, is located in the PersonIdentificationNameAlternateService.

// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure.
@Transactional
class PersonIdentificationNameCurrentService extends ServiceBase {

    def sessionFactory
    GlobalUniqueIdentifierService globalUniqueIdentifierService


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


    def fetchAllWithGuidByPidmInList(Collection<Integer> pidms, String sortField = null, String sortOrder = null) {
        List rows = []
        if (pidms) {
            List entitiesList = PersonIdentificationNameCurrent.withSession { session ->
                String query = getGuidJoinHQL()
                query += " and a.pidm IN :pidms "
                if (sortField) {
                    def orderBy = " order by a." + sortField
                    if (["asc", "desc"].contains(sortOrder?.trim()?.toLowerCase())) {
                        orderBy += " $sortOrder"
                    }
                    query += orderBy
                }
                def hqlQuery = session.createQuery(query)
                hqlQuery.with {
                    setString('ldmName', GeneralValidationCommonConstants.PERSONS_LDM_NAME)
                    setParameterList('pidms', pidms.unique())
                    list()
                }
            }
            entitiesList.each {
                Map entitiesMap = [personIdentificationNameCurrent: it[0], globalUniqueIdentifier: it[1]]
                rows.add(entitiesMap)
            }
        }
        return rows
    }


    def getPidmToGuidMap(Collection<Integer> pidms) {
        def entities = []
        if (!pidms) {
            return entities
        }
        Collection<String> strPidms = pidms.collect { it.toString() }
        Collection<Collection<String>> subLists = strPidms.collate(1000)
        subLists.each {
            def subEntities = globalUniqueIdentifierService.fetchAllByLdmNameAndDomainKeyInList(GeneralValidationCommonConstants.PERSONS_LDM_NAME, it)
            if (subEntities) {
                entities.addAll(subEntities)
            }
        }
        return entities.collectEntries { [it.domainKey?.toInteger(), it.guid] }
    }


    def fetchByGuid(String guid) {
        Map row = [:]
        if (guid) {
            def entitiesList = PersonIdentificationNameCurrent.withSession { session ->
                String query = getGuidJoinHQL() + " and b.guid = :guid"
                def hqlQuery = session.createQuery(query)
                hqlQuery.with {
                    setString('ldmName', GeneralValidationCommonConstants.PERSONS_LDM_NAME)
                    setString('guid', guid)
                    uniqueResult()
                }
            }
            if (entitiesList) {
                row.personIdentificationNameCurrent = entitiesList[0]
                row.globalUniqueIdentifier = entitiesList[1]
            }
        }
        return row
    }


    def getCurrentNameByPidm(pidm) {
        def currentName = PersonIdentificationNameCurrent.fetchByPidm(pidm)

        return currentName
    }


    private String getGuidJoinHQL() {
        def query = """FROM PersonIdentificationNameCurrent a, GlobalUniqueIdentifier b
                            where a.entityIndicator = 'P' and a.changeIndicator is null
                            and b.domainKey = to_char(a.pidm) and b.ldmName = :ldmName
                     """
        return query
    }


    def List<PersonIdentificationNameCurrent> fetchAllByCriteria(Map content, String sortField = null, String sortOrder = null, int max = 0, int offset = -1) {
        def params = [:]
        def criteria = []
        def pagingAndSortParams = [:]

        buildCriteria(content, params, criteria)

        sortOrder = sortOrder ?: 'asc'
        if (sortField) {
            pagingAndSortParams.sortCriteria = [
                    ["sortColumn": sortField, "sortDirection": sortOrder],
                    ["sortColumn": "id", "sortDirection": "asc"]
            ]
        } else {
            pagingAndSortParams.sortColumn = "id"
            pagingAndSortParams.sortDirection = sortOrder
        }

        if (max > 0) {
            pagingAndSortParams.max = max
        }
        if (offset > -1) {
            pagingAndSortParams.offset = offset
        }

        return getDynamicFinderForFetchAllByCriteria().find([params: params, criteria: criteria], pagingAndSortParams)
    }


    def countByCriteria(Map content) {
        def params = [:]
        def criteria = []

        buildCriteria(content, params, criteria)

        return getDynamicFinderForFetchAllByCriteria().count([params: params, criteria: criteria])
    }


    private void buildCriteria(Map content, LinkedHashMap params, ArrayList criteria) {
    }


    private DynamicFinder getDynamicFinderForFetchAllByCriteria() {
        def query = """FROM PersonIdentificationNameCurrent a
                       where a.entityIndicator = 'P' and a.changeIndicator is null
		            """
        return new DynamicFinder(PersonIdentificationNameCurrent.class, query, "a")
    }

}
