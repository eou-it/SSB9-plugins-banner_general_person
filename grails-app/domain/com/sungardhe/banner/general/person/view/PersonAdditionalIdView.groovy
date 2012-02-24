/** *******************************************************************************
 Copyright 2009-2012 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of
 SunGard Higher Education and its subsidiaries. Any use of this software is limited
 solely to SunGard Higher Education licensees, and is further subject to the terms
 and conditions of one or more written license agreements between SunGard Higher
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher
 Education in the U.S.A. and/or other regions and/or countries.
 ********************************************************************************* */
package com.sungardhe.banner.general.person.view

import javax.persistence.*
import com.sungardhe.banner.query.DynamicFinder

/**
 * Person Email model.
 */
@Entity
@Table(name = "GVQ_GORADID")
@NamedQueries(value = [
@NamedQuery(name = "PersonAdditionalIdView.fetchByAdditionalId",
query = """FROM PersonAdditionalIdView a
	  	                WHERE UPPER(a.additionalId) like :filter
	  	                ORDER by a.lastName, a.firstName, a.middleName, a.bannerId, a.adidCode, a.additionalId """),
@NamedQuery(name = "PersonAdditionalIdView.fetchByAdditionalIdPidm",
query = """FROM PersonAdditionalIdView a
	  	                WHERE UPPER(a.additionalId) like :filter
	  	                AND a.pidm IN (:pidms) """),
@NamedQuery(name = "PersonAdditionalIdView.fetchCountByAdditionalId",
query = """select count(a.additionalId) FROM PersonAdditionalIdView a
	  	                WHERE a.additionalId like :filter """)
])
class PersonAdditionalIdView extends PersonView {

    /**
     * Adid Code
     */
    @Column(name = "ADID_CODE")
    String adidCode

    /**
     * Additional Id
     */
    @Column(name = "ADDITIONAL_ID")
    String additionalId

    public static int fetchCountPersonAdditionalId(filter) {
        def queryCriteria
        if (!filter) return 0
        queryCriteria = filter.toUpperCase() + "%"
        def count = PersonAdditionalIdView.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonAdditionalIdView.fetchCountByAdditionalId').setString('filter', queryCriteria)
            query.list().get(0)
        }
        return count
    }


    public static Map fetchPersonAdditionalId(filter, pagingAndSortParams) {
        return [list: PersonAdditionalIdView.fetchByAdditionalId(filter, pagingAndSortParams), totalCount: PersonAdditionalIdView.fetchCountPersonAdditionalId(filter)]
    }


    public static Map fetchPersonAdditionalId(pagingAndSortParams) {
        //Reason is person lookup will have performance issues when filter is unavailable
        return [list: []]
    }


    public static List fetchByAdditionalId(filter, pagingAndSortParams) {
        def queryCriteria
        if (!filter) return []
        queryCriteria = "%" + filter.toUpperCase() + "%"
        def emails = PersonAdditionalIdView.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonAdditionalIdView.fetchByAdditionalId').setString('filter', queryCriteria)
            query.setMaxResults(pagingAndSortParams.max);
            query.setFirstResult(pagingAndSortParams.offset);
            query.list()
        }
        return emails
    }


    public static List fetchByAdditionalId(filter) {
        def queryCriteria
        if (!filter) return []
        queryCriteria = "%" + filter.toUpperCase() + "%"
        def emails = PersonAdditionalIdView.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonAdditionalIdView.fetchByAdditionalId').setString('filter', queryCriteria)
            query.list()
        }
        return emails
    }


    public static List fetchByAdditionalIdAndPidm(pidms, filter) {
        def queryCriteria
        if (!filter) return []
        queryCriteria = "%" + filter.toUpperCase() + "%"

        PersonAdditionalIdView.withSession {session ->
            def additionalIds = session.getNamedQuery('PersonAdditionalIdView.fetchByAdditionalIdPidm').setString('filter', queryCriteria).setParameterList("pidms", pidms).list()
            return additionalIds
        }
    }


    def static countAllEntities(filterData) {
        finderByAllEntityList().count(filterData)
    }


    def public static fetchSearchEntityList(filterData, pagingAndSortParams) {
        finderByAllEntityList().find(filterData, pagingAndSortParams)
    }


    def private static finderByAllEntityList = {filterData ->
        def query = """FROM  PersonAdditionalIdView  additionalid """
        return new DynamicFinder(PersonEmailView.class, query, "additionalid")
    }


    public String toString() {
         """PersonAdditionalIdView[
                     id=$id,
                     pidm=$pidm,
                     bannerId=$bannerId,
                     lastName=$lastName,
                     firstName=$firstName,
                     middleName=$middleName,
                     lastModified=$lastModified,
                     dataOrigin=$dataOrigin,
                     version=$version,
                     lastModifiedBy=$lastModifiedBy,
                     birthDate=$birthDate,
                     adidCode=$adidCode,
                     additionalId=$additionalId]"""
     }
}
