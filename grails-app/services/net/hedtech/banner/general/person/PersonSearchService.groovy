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
package net.hedtech.banner.general.person

import org.apache.log4j.Logger
import net.hedtech.banner.general.person.view.PersonPersonView
import groovy.sql.Sql
import org.springframework.security.core.context.SecurityContextHolder
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.person.view.PersonView
import net.hedtech.banner.general.person.view.PersonAdvancedFilterView
import net.hedtech.banner.general.person.view.PersonAdvancedAlternateIdFilterView
import net.hedtech.banner.person.dsl.NameTemplate
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.context.ApplicationContext
import net.hedtech.banner.general.person.view.PersonAdvancedIdFilterView
import net.hedtech.banner.general.person.view.PersonAdvancedSearchView
import net.hedtech.banner.general.system.SdaCrosswalkConversion

class PersonSearchService {

    static transactional = false
    def sessionFactory
    private final Logger log = Logger.getLogger(getClass())

    def institutionalDescriptionService

    private _nameFormat


    def nameComparator = [
                    compare: { first, second ->
                        if (first.pidm == second.pidm &&
                                first.bannerId == second.bannerId &&
                                first.lastName == second.lastName &&
                                first.firstName == second.firstName &&
                                first.middleName == second.middleName &&
                                first.changeIndicator == second.changeIndicator
                                ){
                             return 0
                        }else {
                            return 1
                        }

                    }
            ] as Comparator


    def personNameSearch(searchFilter, filterData, pagingAndSortParams) {
        def currentList
        def list

        //remove all special characters
        def search = Arrays.asList(searchFilter?.replaceAll('[^a-zA-Z0-9%\\s]+', '').toUpperCase().split()).join("|")

        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())

        try {
            //sets the search filter per search request
            sql.call("{call soknsut.p_set_search_filter(${search})}")
            filterData.dynamicdomain = "PersonAdvancedFilterView"
            list = PersonAdvancedSearchView.fetchSearchEntityList2(filterData, pagingAndSortParams).each {
                it ->
                def lastNameValue = it.lastName
                def firstNameValue = it.firstName
                def middleNameValue = it.middleName
                def surnamePrefixValue = it.surnamePrefix
                def nameSuffixValue = it.nameSuffix
                def namePrefixValue = it.namePrefix

                it.formattedName = NameTemplate.format {
                    lastName lastNameValue
                    firstName firstNameValue
                    mi middleNameValue
                    surnamePrefix surnamePrefixValue
                    nameSuffix nameSuffixValue
                    namePrefix namePrefixValue
                    formatTemplate getNameFormat()
                    text
                }
            }

            return list
            /*currentList = list.findAll { it.changeIndicator == null}

            if (currentList && currentList?.size() == 1) {
                return currentList
            } else {
                return list.unique(nameComparator)
            }*/

        } finally {
            sql?.close()
        }

    }

    def personIdSearch(searchFilter, filterData, pagingAndSortParams) {
        def searchResult

        def ssnSearchEnabledIndicator = institutionalDescriptionService.findByKey().ssnSearchEnabledIndicator
        def ssn
        def pii

        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())

        try {
            searchFilter = "%"+searchFilter+"%"
            //sets the search filter per search request
            sql.call("{call soknsut.p_set_search_filter(${searchFilter})}")
            if (ssnSearchEnabledIndicator) {
                def userName = SecurityContextHolder.context?.authentication?.principal?.username?.toUpperCase()
                sql.call("{$Sql.VARCHAR = call g\$_chk_auth.g\$_check_authorization_fnc('SSN_SEARCH',${userName})}") {ssnSearch -> ssn = ssnSearch}

            } else {
                ssn = 'NO'
            }
            searchResult = searchComponent(ssn, filterData, pagingAndSortParams)
            sql.call("{$Sql.VARCHAR = call gokfgac.f_spriden_pii_active}") { result -> pii = result }
            if (searchResult?.size() == 0 && pii == "Y") {
                sql.execute("""call gokfgac.p_turn_fgac_off()""")
                searchResult = searchComponent(ssn, filterData, pagingAndSortParams)
                sql.execute("""call gokfgac.p_turn_fgac_on()""")
                if (searchResult?.size() > 0) {
                    throw new ApplicationException(PersonView, "@@r1:invalidId@@")
                }
            }

        } finally {
            sql?.close()
        }

        return searchResult
    }

    def isSSNSearchEnabled() {
        boolean enabled = true
        def userName = SecurityContextHolder.context?.authentication?.principal?.username?.toUpperCase()
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def ssn
        sql.call("{$Sql.VARCHAR = call g\$_chk_auth.g\$_check_authorization_fnc('SSN_SEARCH',${userName})}") {ssnSearch -> ssn = ssnSearch}
        if (ssn != "YES") {
           enabled = false
        }
        return enabled
    }


    def fetchNoOfRowsInPageForGUQSRCH() {
        def lst = SdaCrosswalkConversion.findAllWhere(internalGroup: 'GUISRCH', internal:'SEARCH_MAX')
        if (!lst.empty)  {
             SdaCrosswalkConversion gtvsdax = lst.get(0)
             return gtvsdax.external
        }
        return null
    }



    def private searchComponent(ssn, filterData, pagingAndSortParams) {
        def currentList
        def list

        if (ssn == "YES") {
            //search by id first
            filterData.dynamicdomain = "PersonAdvancedIdFilterView"
            list = PersonAdvancedSearchView.fetchSearchEntityList2(filterData, pagingAndSortParams).each {
                it ->
                def lastNameValue = it.lastName
                def firstNameValue = it.firstName
                def middleNameValue = it.middleName
                def surnamePrefixValue = it.surnamePrefix
                def nameSuffixValue = it.nameSuffix
                def namePrefixValue = it.namePrefix
                it.formattedName = NameTemplate.format {
                    lastName lastNameValue
                    firstName firstNameValue
                    mi middleNameValue
                    surnamePrefix surnamePrefixValue
                    nameSuffix nameSuffixValue
                    namePrefix namePrefixValue
                    formatTemplate getNameFormat()
                    text
                }
            }

            if(list && list?.size() > 0) {
                return list
            }
            /*currentList = list.findAll { it.changeIndicator == null}

            if (currentList && currentList?.unique(nameComparator).size() == 1) {
                return currentList
            } else if (currentList && currentList?.size() > 1) {
                return list.unique(nameComparator)
            }*/

            // if search by id return no data, then continue search by ssn
            filterData.dynamicdomain = "PersonAdvancedAlternateIdFilterView"
            list = PersonAdvancedSearchView.fetchSearchEntityList2(filterData, pagingAndSortParams).each {
                it ->
                def lastNameValue = it.lastName
                def firstNameValue = it.firstName
                def middleNameValue = it.middleName
                def surnamePrefixValue = it.surnamePrefix
                def nameSuffixValue = it.nameSuffix
                def namePrefixValue = it.namePrefix
                it.formattedName = NameTemplate.format {
                    lastName lastNameValue
                    firstName firstNameValue
                    mi middleNameValue
                    surnamePrefix surnamePrefixValue
                    nameSuffix nameSuffixValue
                    namePrefix namePrefixValue
                    formatTemplate getNameFormat()
                    text
                }
            }

            return list
            /*currentList = list.findAll { it.changeIndicator == null}

            if (currentList && currentList?.unique(nameComparator).size() == 1) {
                return currentList
            } else {
                //remove all duplicate values as a result of outer join to spraddr
                return list.unique(nameComparator)
            }*/
        } else {
            filterData.dynamicdomain = "PersonAdvancedIdFilterView"
            list = PersonAdvancedSearchView.fetchSearchEntityList2(filterData, pagingAndSortParams).each {
                it ->
                def lastNameValue = it.lastName
                def firstNameValue = it.firstName
                def middleNameValue = it.middleName
                def surnamePrefixValue = it.surnamePrefix
                def nameSuffixValue = it.nameSuffix
                def namePrefixValue = it.namePrefix
                it.formattedName = NameTemplate.format {
                    lastName lastNameValue
                    firstName firstNameValue
                    mi middleNameValue
                    surnamePrefix surnamePrefixValue
                    nameSuffix nameSuffixValue
                    namePrefix namePrefixValue
                    formatTemplate getNameFormat()
                    text
                }
            }
            return list

            /*currentList = list.findAll { it.changeIndicator == null}
            if (currentList && currentList?.unique(nameComparator).size() == 1) {
                return currentList
            } else {
                //remove all duplicate values as a result of outer join to spraddr
                return list.unique(nameComparator)
            }*/
        }
    }


    private def getNameFormat() {
        if (!_nameFormat) {
            def application = AH.application
            ApplicationContext applicationContext = application.mainContext
            def messageSource = applicationContext.getBean("messageSource")
            messageSource.getMessage("default.name.format", null, LCH.getLocale())
        } else {
            _nameFormat
        }
    }
}
