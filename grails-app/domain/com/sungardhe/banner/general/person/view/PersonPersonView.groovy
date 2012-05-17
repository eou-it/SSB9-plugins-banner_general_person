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
 * Person Spriden model.
 */
@Entity
@Table(name = "SVQ_SPRIDEN")
@NamedQueries(value = [
@NamedQuery(name = "PersonPersonView.search",
query = """FROM  PersonPersonView a
WHERE ((a.bannerId like :id and :id is not null) or :id is null)
      and (((soundex(a.lastName) = soundex(:soundexLastName)) and :soundexLastName is not null) or
              (a.searchLastName like :lastName and
               :lastName is not null) or (:lastName is null and :soundexLastName is null))
      and (((soundex(a.firstName) = soundex(:soundexFirstName)) and :soundexFirstName is not null) or
               (a.searchFirstName like :firstName and
                 :firstName is not null) or
                  (:firstName is null and :soundexFirstName is null))
       and ((a.searchMiddleName like :midName and :midName is not null) or
              :midName is null)
        and ((a.changeIndicator = :changeIndicator and :changeIndicator is not null) or
              :changeIndicator is null)
        and((a.nameType = :nameType and :nameType is not null) or
              :nameType is null)
	   and a.entityIndicator = 'P' """),
@NamedQuery(name = "PersonPersonView.searchCount",
query = """select count(a.bannerId) FROM  PersonPersonView a
     WHERE ((a.bannerId like :id and :id is not null) or :id is null)
      and (((soundex(a.lastName) = soundex(:soundexLastName)) and :soundexLastName is not null) or
              (a.searchLastName like :lastName and
               :lastName is not null) or (:lastName is null and :soundexLastName is null))
      and (((soundex(a.firstName) = soundex(:soundexFirstName)) and :soundexFirstName is not null) or
               (a.searchFirstName like :firstName and
                 :firstName is not null) or
                  (:firstName is null and :soundexFirstName is null))
       and ((a.searchMiddleName like :midName and :midName is not null) or
              :midName is null)
        and ((a.changeIndicator = :changeIndicator and :changeIndicator is not null) or
              :changeIndicator is null)
        and((a.nameType = :nameType and :nameType is not null) or
              :nameType is null)
	   and a.entityIndicator = 'P' """),
@NamedQuery(name = "PersonPersonView.searchByPidms",
query = """FROM  PersonPersonView a
WHERE ((a.bannerId like :id and :id is not null) or :id is null)
      and (((soundex(a.lastName) = soundex(:soundexLastName)) and :soundexLastName is not null) or
              (a.searchLastName like :lastName and
               :lastName is not null) or (:lastName is null and :soundexLastName is null))
      and (((soundex(a.firstName) = soundex(:soundexFirstName)) and :soundexFirstName is not null) or
               (a.searchFirstName like :firstName and
                 :firstName is not null) or
                  (:firstName is null and :soundexFirstName is null))
       and ((a.searchMiddleName like :midName and :midName is not null) or
              :midName is null)
        and ((a.changeIndicator = :changeIndicator and :changeIndicator is not null) or
              :changeIndicator is null)
        and((a.nameType = :nameType and :nameType is not null) or
              :nameType is null)
	   and a.entityIndicator = 'P'
	   and a.pidm IN (:pidms)""")
])
class PersonPersonView extends PersonView {

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
     * The Last Name field in SOUNDEX phonetic format.
     */
    @Column(name = "SOUNDEX_LAST_NAME", length = 4)
    String soundexLastName

    /**
     * The First Name field in SOUNDEX phonetic format.
     */
    @Column(name = "SOUNDEX_FIRST_NAME", length = 4)
    String soundexFirstName

    /**
     * The name type of the person
     */
    @Column(name = "NAME_TYPE", length = 4)
    String nameType


    public String toString() {
        """PersonPersonView[
					id=$id,
					pidm=$pidm,
					bannerId=$bannerId,
					lastName=$lastName,
					firstName=$firstName,
					middleName=$middleName,
					birthDate=$birthDate,
					nameType=$nameType,
					changeIndicator=$changeIndicator,
					entityIndicator=$entityIndicator]"""
    }

    /**
     * Returns the count of persons found by the query.
     * @param id search parameter
     * @param lastName search parameter
     * @param firstName search parameter
     * @param midName search parameter
     * @param soundexLastName search parameter
     * @param soundexFirstName search parameter
     * @param changeIndicator search parameter
     * @param nameType search parameter
     * @return count of persons found by the query
     */
    public static int fetchCountPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType) {
        def idCriteria
        def lastNameCriteria
        def firstNameCriteria
        def midNameCriteria


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

        def count = PersonPersonView.withSession {session ->

            org.hibernate.Query query = session.getNamedQuery('PersonPersonView.searchCount').
                    setString('id', idCriteria).
                    setString('lastName', lastNameCriteria).
                    setString('soundexLastName', soundexLastName).
                    setString('firstName', firstNameCriteria).
                    setString('soundexFirstName', soundexFirstName).
                    setString('midName', midNameCriteria).
                    setString("changeIndicator", changeIndicator).
                    setString("nameType", nameType)


            query.list().get(0)
        }
        return count
    }

    /**
     * Returns the list of persons.
     * @param id search parameter
     * @param lastName search parameter
     * @param firstName search parameter
     * @param midName search parameter
     * @param soundexLastName search parameter
     * @param soundexFirstName search parameter
     * @param changeIndicator search parameter
     * @param nameType search parameter
     * @param pagingAndSortParams search parameter
     * @return list of Persons
     */
    public static List fetchPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams) {
        def idCriteria
        def lastNameCriteria
        def firstNameCriteria
        def midNameCriteria


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

        def persons = PersonPersonView.withSession {session ->

            org.hibernate.Query query = session.getNamedQuery('PersonPersonView.search').
                    setString('id', idCriteria).
                    setString('lastName', lastNameCriteria).
                    setString('soundexLastName', soundexLastName).
                    setString('firstName', firstNameCriteria).
                    setString('soundexFirstName', soundexFirstName).
                    setString('midName', midNameCriteria).
                    setString("changeIndicator", changeIndicator).
                    setString("nameType", nameType)
            if (pagingAndSortParams) {
                query.setMaxResults(pagingAndSortParams?.max)
                query.setFirstResult(pagingAndSortParams?.offset)
            }
            query.list()
        }
        return persons
    }

    /**
     * Returns the map of persons.
     * @param id search parameter
     * @param lastName search parameter
     * @param firstName search parameter
     * @param midName search parameter
     * @param soundexLastName search parameter
     * @param soundexFirstName search parameter
     * @param changeIndicator search parameter
     * @param nameType search parameter
     * @param pagingAndSortParams search parameter
     * @return list of Persons
     */
    public static Map fetchPersonList(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams) {
        return [list: PersonPersonView.fetchPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams),
                totalCount: PersonPersonView.fetchCountPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType)]
    }

    /**
     * Fetches the list of Persons.
     * @param filterData filter data
     * @param pagingAndSortParams
     * @return list of Persons
     */
    def public static fetchSearchEntityList(filterData, pagingAndSortParams) {
        finderByAllEntityList().find(filterData, pagingAndSortParams)
    }

    /**
     * Fetches the list of Persons by soundex.
     * @param filterData filter data
     * @param pagingAndSortParams
     * @return list of Persons
     */
    def public static fetchSearchSoundexEntityList(filterData, pagingAndSortParams) {
        finderByAllSoundexEntityList().find(filterData, pagingAndSortParams)
    }

    /*
    * Returns the count of the filtered data.
    */

    def static countAllEntities(filterData) {
        finderByAllEntityList().count(filterData)
    }

    /*
    * Returns the count of the filtered data by soundex.
    */

    def static countAllSoundexEntities(filterData) {
        finderByAllSoundexEntityList().count(filterData)
    }

    /**
     *  Query String Builder
     */
    def private static finderByAllEntityList = {filterData ->
        def query = """FROM  PersonPersonView a
	                   WHERE a.entityIndicator = 'P' """

        return new DynamicFinder(PersonPersonView.class, query, "a")
    }

    /**
     *  Soundex Query String Builder
     */
    def private static finderByAllSoundexEntityList = {filterData ->
        def query = """FROM  PersonPersonView a
          WHERE ((soundex(a.lastName) = soundex(:soundexLastName)) or :soundexLastName is null)
                and ((soundex(a.firstName) = soundex(:soundexFirstName)) or :soundexFirstName is null)
	            and  a.entityIndicator = 'P' """

        return new DynamicFinder(PersonPersonView.class, query, "a")
    }

    /**
     * Fetches list of Persons with Pidm.
     * @param id search parameter
     * @param lastName search parameter
     * @param firstName search parameter
     * @param midName search parameter
     * @param soundexLastName search parameter
     * @param soundexFirstName search parameter
     * @param changeIndicator search parameter
     * @param nameType search parameter
     * @param pagingAndSortParams search parameter
     * @return list of Persons
     */
    public static List fetchPersonByPidms(pidms, id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams) {
        def idCriteria
        def lastNameCriteria
        def firstNameCriteria
        def midNameCriteria


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

        PersonPersonView.withSession {session ->

            def persons = session.getNamedQuery('PersonPersonView.searchByPidms').
                    setString('id', idCriteria).
                    setString('lastName', lastNameCriteria).
                    setString('soundexLastName', soundexLastName).
                    setString('firstName', firstNameCriteria).
                    setString('soundexFirstName', soundexFirstName).
                    setString('midName', midNameCriteria).
                    setString("changeIndicator", changeIndicator).
                    setString("nameType", nameType).
                    setParameterList("pidms", pidms)

            persons.setMaxResults(pagingAndSortParams.max)
            persons.setFirstResult(pagingAndSortParams.offset)

            return persons.list()
        }
    }
}
