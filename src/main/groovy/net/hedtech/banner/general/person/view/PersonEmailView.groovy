/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person.view

import javax.persistence.*
import net.hedtech.banner.query.DynamicFinder

/**
 * Person Email model.
 */
@Entity
@Table(name = "GVQ_GOREMAL")
@NamedQueries(value = [
@NamedQuery(name = "PersonEmailView.fetchByEmailAddress",
query = """FROM  PersonEmailView a
	  	                WHERE UPPER(a.emailAddress) like :filter
	  	                ORDER by a.lastName, a.firstName, a.middleName, a.bannerId, a.emailCode, a.emailAddress """),
@NamedQuery(name = "PersonEmailView.fetchByEmailAddressPidm",
query = """FROM  PersonEmailView a
	  	                WHERE UPPER(a.emailAddress) like :filter
	  	                AND a.pidm IN (:pidms) """),
@NamedQuery(name = "PersonEmailView.fetchCountByEmailAddress",
query = """select count(a.emailAddress) FROM PersonEmailView a
	  	                WHERE a.emailAddress like :filter """)
])
class PersonEmailView extends PersonView {

    /**
     * Email Code
     */
    @Column(name = "EMAIL_CODE")
    String emailCode

    /**
     * Email Address
     */
    @Column(name = "EMAIL_ADDRESS")
    String emailAddress

    public static int fetchCountPersonEmailAddress(filter) {
        def queryCriteria
        if (!filter) return 0
        queryCriteria = filter.toUpperCase() + "%"
        def count = PersonEmailView.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonEmailView.fetchCountByEmailAddress').setString('filter', queryCriteria)
            query.list().get(0)
        }
        return count
    }


    public static Map fetchPersonEmailAddress(filter, pagingAndSortParams) {
        return [list: PersonEmailView.fetchByEmailAddress(filter, pagingAndSortParams), totalCount: PersonEmailView.fetchCountPersonEmailAddress(filter)]
    }


    public static Map fetchPersonEmailAddress(pagingAndSortParams) {
        //Reason is person lookup will have performance issues when filter is unavailable
        return [list: []]
    }


    public static List fetchByEmailAddress(filter, pagingAndSortParams) {
        def queryCriteria
        if (!filter) return []
        queryCriteria = "%" + filter.toUpperCase() + "%"
        def emails = PersonEmailView.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonEmailView.fetchByEmailAddress').setString('filter', queryCriteria)
            query.setMaxResults(pagingAndSortParams.max);
            query.setFirstResult(pagingAndSortParams.offset);
            query.list()
        }
        return emails
    }


    public static List fetchByEmailAddress(filter) {
        def queryCriteria
        if (!filter) return []
        queryCriteria = "%" + filter.toUpperCase() + "%"
        def emails = PersonEmailView.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonEmailView.fetchByEmailAddress').setString('filter', queryCriteria)
            query.list()
        }
        return emails
    }

    public static List fetchByEmailAddressAndPidm(pidms, filter) {
        def queryCriteria
        if (!filter) return []
        queryCriteria = "%" + filter.toUpperCase() + "%"

        PersonEmailView.withSession {session ->
            def emails = session.getNamedQuery('PersonEmailView.fetchByEmailAddressPidm').setString('filter', queryCriteria).setParameterList("pidms", pidms).list()
            return emails
        }
    }


    def static countAllEntities(filterData) {
        finderByAllEntityList().count(filterData)
    }


    def public static fetchSearchEntityList(filterData, pagingAndSortParams) {
        finderByAllEntityList().find(filterData, pagingAndSortParams)
    }

    def private static finderByAllEntityList = {filterData ->
        def query = """FROM  PersonEmailView  email """
        return new DynamicFinder(PersonEmailView.class, query, "email")
    }


    public String toString() {
        """PersonEmailView[
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
					emailAdress=$emailAddress,
					emailCode=$emailCode]"""
    }
}
