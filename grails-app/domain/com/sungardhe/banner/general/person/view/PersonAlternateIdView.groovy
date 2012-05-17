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

import com.sungardhe.banner.query.DynamicFinder
import javax.persistence.*

/**
 * Person Spriden Alternate Id model.
 */
@Entity
@Table(name = "SVQ_SPRALTI")
@NamedQueries(value = [
@NamedQuery(name = "PersonAlternateIdView.search",
query = """FROM PersonAlternateIdView a
WHERE ((a.bannerId like :id and :id is not null) or :id is null)
      and ((a.searchLastName like :lastName and
               :lastName is not null) or :lastName is null )
      and ((a.searchFirstName like :firstName and
                 :firstName is not null) or :firstName is null )
      and ((a.searchMiddleName like :midName and :midName is not null) or
              :midName is null)
      and ((a.changeIndicator = :changeIndicator and :changeIndicator is not null) or
              :changeIndicator is null)
      and((UPPER(a.ssn) like :ssn and :ssn is not null) or
              :ssn is null)
	  and a.entityIndicator = 'P' """),
@NamedQuery(name = "PersonAlternateIdView.searchCount",
query = """select count(a.bannerId) FROM  PersonAlternateIdView a
WHERE ((a.bannerId like :id and :id is not null) or :id is null)
      and ((a.searchLastName like :lastName and
               :lastName is not null) or :lastName is null )
      and ((a.searchFirstName like :firstName and
                 :firstName is not null) or :firstName is null )
      and ((a.searchMiddleName like :midName and :midName is not null) or
              :midName is null)
      and ((a.changeIndicator = :changeIndicator and :changeIndicator is not null) or
              :changeIndicator is null)
      and((UPPER(a.ssn) like :ssn and :ssn is not null) or
              :ssn is null)
	  and a.entityIndicator = 'P' """)
])
class PersonAlternateIdView extends PersonView {

    /**
     * This field defines whether type of change made to the record was an ID number change or a name change. Val values: I - ID change, N - name change.
     */
    @Column(name = "CHANGE_INDICATOR", length = 1)
    String changeIndicator

    /**
     * This field defines whether record is person or non-person record.  It does not display on the form. Val values:  P - person, C - non-person.
     */
    @Column(name = "ENTITY_INDICATOR", length = 1)
    String entityIndicator

    /**
     * The Last Name field with all spaces and punctuation removed and all letters capitalized.
     */
    @Column(name = "SEARCH_LAST_NAME", length = 60)
    String searchLastName

    /**
     * The First Name field with all spaces and punctuation removed and all letters capitalized.
     */
    @Column(name = "SEARCH_FIRST_NAME", length = 60)
    String searchFirstName

    /**
     * The MI (Middle Initial) field with all spaces and punctuation removed and all letters capitalized.
     */
    @Column(name = "SEARCH_MI", length = 60)
    String searchMiddleName

    /**
     * The ssn of the person
     */
    @Column(name = "SSN", length = 15)
    String ssn

    public String toString() {
        """PersonAlternateIdView[
					id=$id,
					pidm=$pidm,
					bannerId=$bannerId,
					lastName=$lastName,
					firstName=$firstName,
					middleName=$middleName,
					birthDate=$birthDate,
					ssn=$ssn,
					changeIndicator=$changeIndicator]"""
    }

    /**
     *
     * @param id search parameter
     * @param lastName search parameter
     * @param firstName search parameter
     * @param midName search parameter
     * @param changeIndicator search parameter
     * @param ssn search parameter
     * @return count of persons found by the query
     */
    public static int fetchCountPerson(id, lastName, firstName, midName, changeIndicator, ssn) {
        def idCriteria
        def lastNameCriteria
        def firstNameCriteria
        def midNameCriteria
        def ssnCriteria


        if (id) {
            idCriteria = "%" + id.toUpperCase() + "%"
        }

        if (lastName) {
            lastNameCriteria = "%" + lastName.toUpperCase() + "%"
        }

        if (firstName) {
            firstNameCriteria = "%" + firstName.toUpperCase() + "%"
        }

        if (midName) {
            midNameCriteria = "%" + midName.toUpperCase() + "%"
        }

        if (ssn) {
            ssnCriteria = "%" + ssn.toUpperCase() + "%"
        }

        def count = PersonAlternateIdView.withSession {session ->

            org.hibernate.Query query = session.getNamedQuery('PersonAlternateIdView.searchCount').
                    setString('id', idCriteria).
                    setString('lastName', lastNameCriteria).
                    setString('firstName', firstNameCriteria).
                    setString('midName', midNameCriteria).
                    setString("changeIndicator", changeIndicator).
                    setString("ssn", ssnCriteria)


            query.list().get(0)
        }
        return count
    }

    /**
     *
     * @param id search parameter
     * @param lastName search parameter
     * @param firstName search parameter
     * @param midName search parameter
     * @param changeIndicator search parameter
     * @param ssn search parameter
     * @param pagingAndSortParams search parameter
     * @return list of Persons
     */
    public static List fetchPerson(id, lastName, firstName, midName, changeIndicator, ssn, pagingAndSortParams) {
        def idCriteria
        def lastNameCriteria
        def firstNameCriteria
        def midNameCriteria
        def ssnCriteria


        if (id) {
            idCriteria = "%" + id.toUpperCase() + "%"
        }

        if (lastName) {
            lastNameCriteria = "%" + lastName.toUpperCase() + "%"
        }

        if (firstName) {
            firstNameCriteria = "%" + firstName.toUpperCase() + "%"
        }

        if (midName) {
            midNameCriteria = "%" + midName.toUpperCase() + "%"
        }

        if (ssn) {
            ssnCriteria = "%" + ssn.toUpperCase() + "%"
        }

        println ssnCriteria

        def persons = PersonAlternateIdView.withSession {session ->

            org.hibernate.Query query = session.getNamedQuery('PersonAlternateIdView.search').
                    setString('id', idCriteria).
                    setString('lastName', lastNameCriteria).
                    setString('firstName', firstNameCriteria).
                    setString('midName', midNameCriteria).
                    setString("changeIndicator", changeIndicator).
                    setString("ssn", ssnCriteria)

            query.setMaxResults(pagingAndSortParams.max);
            query.setFirstResult(pagingAndSortParams.offset);
            query.list()
        }
        return persons
    }

    /**
     *
     * @param id search parameter
     * @param lastName search parameter
     * @param firstName search parameter
     * @param midName search parameter
     * @param changeIndicator search parameter
     * @param ssn search parameter
     * @param pagingAndSortParams search parameter
     * @return list of Persons
     */
    public static Map fetchPersonList(id, lastName, firstName, midName, changeIndicator, ssn, pagingAndSortParams) {
        return [list: PersonAlternateIdView.fetchPerson(id, lastName, firstName, midName, changeIndicator, ssn, pagingAndSortParams),
                totalCount: PersonAlternateIdView.fetchCountPerson(id, lastName, firstName, midName, changeIndicator, ssn)]
    }

    /**
     *
     * @param filterData filter data
     * @param pagingAndSortParams
     * @return list of Persons
     */
    def public static fetchSearchEntityList(filterData, pagingAndSortParams) {
        finderByAllEntityList().find(filterData, pagingAndSortParams)
    }

    /*
    * Returns the count of the filtered data.
    */

    def static countAllEntities(filterData) {
        finderByAllEntityList().count(filterData)
    }

    /**
     *  Query String Builder
     */
    def private static finderByAllEntityList = {filterData ->
        def query = """FROM PersonAlternateIdView a """

        return new DynamicFinder(PersonAlternateIdView.class, query, "a")
    }
}
