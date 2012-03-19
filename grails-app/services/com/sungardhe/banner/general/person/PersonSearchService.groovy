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
            select id, last_name, first_name, mi,
                   soknsut.name_search_booster(search_last_name,'${searchFilter}') as boost
              from svq_spriden
              where REGEXP_LIKE(search_last_name||search_first_name||search_mi||id, '${searchFilter}')
              order by boost
				              """

        def personsList = []
        def persons = sql.rows(sqlStatement)
        persons.each { personRow ->
            def person = [:]
            person.bannerId = personRow.ID
            person.name = personRow.FIRST_NAME
            person.lastName = personRow.LAST_NAME
            person.mi = personRow.MI
            person.boosterSearch = personRow.BOOST

            personsList << person
        }

        return personsList.sort {it.boosterSearch}
    }
}
