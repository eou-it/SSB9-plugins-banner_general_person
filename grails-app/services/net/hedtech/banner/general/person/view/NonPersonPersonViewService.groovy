/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person.view

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.person.view.NonPersonPersonView
import net.hedtech.banner.service.ServiceBase

class NonPersonPersonViewService extends ServiceBase {

    boolean transactional = true

    def preCreate(map) {
        throwUnsupportedException()
    }

    def preUpdate(map) {
        throwUnsupportedException()
    }

    def preDelete(map) {
        throwUnsupportedException()
    }

    def throwUnsupportedException() {
        throw new ApplicationException(NonPersonPersonView, "@@r1:unsupported.operation@@")
    }


    def fetchAllWithGuidByPidmInList(Collection<Integer> pidms) {
        List rows = []
        if (pidms) {
            List entitiesList = NonPersonPersonView.withSession { session ->
                String query = getGuidJoinHQL()
                query += " and a.pidm IN :pidms "
                def hqlQuery = session.createQuery(query)
                hqlQuery.with {
                    setString('ldmName', GeneralValidationCommonConstants.NON_PERSONS_LDM_NAME)
                    setParameterList('pidms', pidms.unique())
                    list()
                }
            }
            entitiesList.each {
                Map entitiesMap = [nonPersonPersonView: it[0], globalUniqueIdentifier: it[1]]
                rows.add(entitiesMap)
            }
        }
        return rows
    }

    def fetchAllWithGuidByCriteria(Map params, int max=0, int offset=-1) {
        List rows = []
        List entitiesList = NonPersonPersonView.withSession { session ->
            String query = getGuidJoinHQL()
            if(params.bannerId){
                query += " and a.bannerId = :bannerId "
            }
            def hqlQuery = session.createQuery(query)
            hqlQuery.with {
                setString('ldmName', GeneralValidationCommonConstants.NON_PERSONS_LDM_NAME)
                if(params.bannerId) {
                    setString('bannerId', params.bannerId.trim())
                }
                if (max > 0) {
                    setMaxResults(max)
                }
                if (offset > -1) {
                    setFirstResult(offset)
                }
                list()
            }
        }
        entitiesList.each {
            Map entitiesMap = [nonPersonPersonView: it[0], globalUniqueIdentifier: it[1]]
            rows.add(entitiesMap)
        }
        return rows
    }

    def countByCriteria(Map params) {
        return NonPersonPersonView.withSession { session ->
            String query = " select count(*) " + getGuidJoinHQL()
            if (params.bannerId) {
                query += " and a.bannerId = :bannerId "
            }
            def hqlQuery = session.createQuery(query)
            hqlQuery.with {
                setString('ldmName', GeneralValidationCommonConstants.NON_PERSONS_LDM_NAME)
                if (params.bannerId) {
                    setString('bannerId', params.bannerId.trim())
                }
                uniqueResult()
            }
        }
    }

    def fetchByGuid(String guid) {
        Map row = [:]
        if (guid) {
            def entitiesList = NonPersonPersonView.withSession { session ->
                String query = getGuidJoinHQL() + " and b.guid = :guid"
                def hqlQuery = session.createQuery(query)
                hqlQuery.with {
                    setString('ldmName', GeneralValidationCommonConstants.NON_PERSONS_LDM_NAME)
                    setString('guid', guid)
                    uniqueResult()
                }
            }
            if(entitiesList){
            row.nonPersonPersonView = entitiesList[0]
            row.globalUniqueIdentifier = entitiesList[1]
            }
        }
        return row
    }


    private String getGuidJoinHQL() {
        def query = """ FROM NonPersonPersonView a, GlobalUniqueIdentifier b
                        where a.entityIndicator = 'C' and a.changeIndicator is null
                        and b.domainKey = a.pidm and b.ldmName = :ldmName
                     """
        return query
    }

}
