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
 * Person Phone model.
 */
@Entity
@Table(name = "SVQ_SPRTELE")
@NamedQueries(value = [
@NamedQuery(name = "PersonPhoneView.fetchByPhone",
query = """FROM  PersonPhoneView a
	  	                WHERE UPPER(a.phoneSearch) like :filter
	  	                ORDER by a.lastName, a.firstName, a.middleName, a.bannerId, a.phoneSearch  """),
@NamedQuery(name = "PersonPhoneView.fetchByPhonePidm",
query = """FROM  PersonPhoneView a
	  	                WHERE UPPER(a.phoneSearch) like :filter
	  	                AND a.pidm IN (:pidms) """),
@NamedQuery(name = "PersonPhoneView.fetchCountByPhone",
query = """select count(a.phoneSearch) FROM PersonPhoneView a
	  	                WHERE a.phoneSearch like :filter """)
])
class PersonPhoneView extends PersonView {

    /**
     * Tele Code
     */
    @Column(name = "TELE_CODE")
    String phoneCode

    /**
     * Phone Search
     */
    @Column(name = "PHONE_SEARCH")
    String phoneSearch

    /**
     * Phone number
     */
    @Column(name = "PHONE")
    String phone


    public static int fetchCountPersonPhone(filter) {
        def queryCriteria
        if (!filter) return 0
        queryCriteria = filter.toUpperCase() + "%"
        def count = PersonPhoneView.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonPhoneView.fetchCountByPhone').setString('filter', queryCriteria)
            query.list().get(0)
        }
        return count
    }


    public static Map fetchPersonPhone(filter, pagingAndSortParams) {
        return [list: PersonPhoneView.fetchByPhone(filter, pagingAndSortParams), totalCount: PersonPhoneView.fetchCountPersonPhone(filter)]
    }


    public static Map fetchPersonPhone(pagingAndSortParams) {
        //Reason is person lookup will have performance issues when filter is unavailable
        return [list: []]
    }


    public static List fetchByPhone(filter, pagingAndSortParams) {
        def queryCriteria
        if (!filter) return []

        queryCriteria = "%" + filter.replaceAll("[\\s\\-()]", "") + "%"
        def phones = PersonPhoneView.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonPhoneView.fetchByPhone').setString('filter', queryCriteria)
            query.setMaxResults(pagingAndSortParams.max);
            query.setFirstResult(pagingAndSortParams.offset);
            query.list()
        }
        return phones
    }


    public static List fetchByPhone(filter) {
        def queryCriteria
        if (!filter) return []

        queryCriteria = "%" + filter.replaceAll("[\\s\\-()]", "") + "%"
        def phones = PersonPhoneView.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonPhoneView.fetchByPhone').setString('filter', queryCriteria)
            query.list()
        }
        return phones
    }


    def static countAllEntities(filterData) {
        finderByAllEntityList().count(filterData)
    }


    def public static fetchSearchEntityList(filterData, pagingAndSortParams) {
        finderByAllEntityList().find(filterData, pagingAndSortParams)
    }


    def private static finderByAllEntityList = {filterData ->
        def query = """FROM  PersonPhoneView phone """
        return new DynamicFinder(PersonPhoneView.class, query, "phone")
    }

    public static List fetchByPhoneAndPidm(pidms, filter) {
        def queryCriteria
        if (!filter) return []
        queryCriteria = "%" + filter.toUpperCase() + "%"

        PersonPhoneView.withSession {session ->
            def phones = session.getNamedQuery('PersonPhoneView.fetchByPhonePidm').setString('filter', queryCriteria).setParameterList("pidms", pidms).list()
            return phones
        }
    }

    public String toString() {
        """PersonPhoneView[
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
					phone=$phone,
					phoneCode=$phoneCode]"""
    }
}
