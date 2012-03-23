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
package com.sungardhe.banner.general.person

import org.apache.log4j.Logger
import com.sungardhe.banner.general.person.view.PersonPersonView
import groovy.sql.Sql
import com.sungardhe.banner.general.person.view.PersonAdvancedSearchView

class PersonSearchService {

    static transactional = false
    def sessionFactory
    private final Logger log = Logger.getLogger(getClass())

    def findPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams) {
        return PersonPersonView.fetchPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)
    }

    def findPersonByPidm(pidms, id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams) {
        return PersonPersonView.fetchPersonByPidms(pidms, id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)
    }

    def fetchTextSearch(searchFilter) {
        def sql = new Sql(sessionFactory.getCurrentSession().connection())

        def sqlStatement = """
        select pidm, id, last_name, first_name, mi,change_indicator,
                soknsut.name_search_booster(search_last_name,'${searchFilter}') as boost
              from svq_spriden where pidm in (
            select pidm
              from svq_spriden a
              where REGEXP_LIKE(a.search_last_name||'::'||a.search_first_name||'::'||a.search_mi||'::'||a.id, '${searchFilter}')
              )
              order by boost, last_name
				              """

        def personsList = []
        def persons = sql.rows(sqlStatement)
        persons.each { personRow ->
            def person = [:]
            person.pidm = personRow.PIDM
            person.bannerId = personRow.ID
            person.name = personRow.FIRST_NAME
            person.lastName = personRow.LAST_NAME
            person.mi = personRow.MI
            person.boosterSearch = personRow.BOOST
            person.changeIndicator = personRow.CHANGE_INDICATOR

            personsList << person
        }


        def currentList = personsList.findAll { it.changeIndicator == null}


        if (currentList && currentList?.size() == 1) {
            return currentList
        } else {
            return personsList.sort {it.boosterSearch}
        }
    }

     def fetchTextWithSSNSearch(searchFilter) {
        def sql = new Sql(sessionFactory.getCurrentSession().connection())

        def sqlStatement = """
        select pidm, id, ssn, last_name, first_name, mi,change_indicator,
                soknsut.name_search_booster(search_last_name,'${searchFilter}') as boost
              from svq_spralti where pidm in (
            select pidm
              from svq_spralti a
              where REGEXP_LIKE(a.search_last_name||'::'||a.search_first_name||'::'||a.search_mi||'::'||a.id||'::'||a.ssn, '${searchFilter}')
              )
              order by boost,last_name
				              """

        def personsList = []
        def persons = sql.rows(sqlStatement)
        persons.each { personRow ->
            def person = [:]
            person.pidm = personRow.PIDM
            person.bannerId = personRow.ID
            person.ssn = personRow.SSN
            person.name = personRow.FIRST_NAME
            person.lastName = personRow.LAST_NAME
            person.mi = personRow.MI
            person.boosterSearch = personRow.BOOST
            person.changeIndicator = personRow.CHANGE_INDICATOR

            personsList << person
        }


        def currentList = personsList.findAll { it.changeIndicator == null}


        if (currentList && currentList?.size() == 1) {
            return currentList
        } else {
            return personsList.sort {it.boosterSearch}
        }
    }
}
