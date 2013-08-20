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
import groovy.sql.Sql
import org.springframework.security.core.context.SecurityContextHolder
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.person.view.PersonView
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.context.ApplicationContext
import net.hedtech.banner.general.person.view.PersonAdvancedSearchView
import net.hedtech.banner.general.system.SdaCrosswalkConversion
import net.hedtech.banner.security.FormContext
import net.hedtech.banner.general.person.view.ExtendedWindowIdSearchView
import net.hedtech.banner.general.person.view.ExtendedWindowNameSearchView
import net.hedtech.banner.general.person.view.ExtendedWindowSsnIdSearchView


class PersonSearchService {

    static transactional = false
    def sessionFactory
    private final Logger log = Logger.getLogger(getClass())

    def institutionalDescriptionService

    private _nameFormat
    public static final String AUTO_COMPLETE_GROUP_NAME = 'IDNAMESEARCH'
    public static final String AUTO_COMPLETE_ID_CODE = 'ID_AUTO'
    public static final String AUTO_COMPLETE_NAME_CODE = 'NAME_AUTO'
    public static final String SEARCH_GUISRCH_CODE = 'SEARCH_MAX'
    public static final String SEARCH_GUISRCH_GROUP_NAME = 'GUISRCH'

    def nameComparator = [
            compare: { first, second ->
                if (first.pidm == second.pidm &&
                        first.bannerId == second.bannerId &&
                        first.lastName == second.lastName &&
                        first.firstName == second.firstName &&
                        first.middleName == second.middleName &&
                        first.changeIndicator == second.changeIndicator
                ) {
                    return 0
                } else {
                    return 1
                }

            }
    ] as Comparator

    def extendedWindowNameSearch(searchFilter, filterData, pagingAndSortParams) {
        def list
        def search = Arrays.asList(searchFilter?.replaceAll('[^a-zA-Z0-9%\\s]+', '').toUpperCase().split()).join("|")
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def name1, name2, name3
        (name1, name2, name3) = formatNameSearchFilterData(searchFilter)

        try {
            setFilterData(sql, name1, name2, name3)
            list = ExtendedWindowNameSearchView.findAll().each {
                it -> it.formattedName = net.hedtech.banner.general.person.PersonUtility.formatName(it)
            }
            return list

        } finally {
            sql?.close()
        }
    }

    private List formatNameSearchFilterData(searchFilter) {
        def name1, name3, name2
        (name1, name2, name3) = Arrays.asList(searchFilter?.replaceAll('[^a-zA-Z0-9%\\s]+', '').toUpperCase().split())
        name1 = setSearchName(name1)
        name2 = setSearchName(name2)
        name3 = setSearchName(name3)
        [name1, name2, name3]
    }

    private String setSearchName(name) {
        if (!name || name.count("%") == name.size()) {
            name = "%"
        }
        else if (name.size() == 1 && name.count("%") == 0) {
            name = name + "%"
        }
        else {
            name = "%" + name + "%"
        }
        name
    }

    def personNameSearch(searchFilter, filterData, pagingAndSortParams) {
        def list

        //remove all special characters
        def search = Arrays.asList(searchFilter?.replaceAll('[^a-zA-Z0-9%\\s]+', '').toUpperCase().split()).join("|")
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def name1, name2, name3
        (name1, name2, name3) = formatNameSearchFilterData(searchFilter)
        try {
            setFilterData(sql, name1, name2, name3)
            filterData.dynamicdomain = "PersonAdvancedFilterView"
            list = PersonAdvancedSearchView.fetchSearchEntityList2(filterData, pagingAndSortParams).each {
                it -> it.formattedName = net.hedtech.banner.general.person.PersonUtility.formatName(it)
            }

            return list

        } finally {
            sql?.close()
        }

    }

    private void setFilterData(sql, name1, name2, name3) {
        sql.call("{call soknsut.p_set_name1(${name1})}")
        sql.call("{call soknsut.p_set_name2(${name2})}")
        sql.call("{call soknsut.p_set_name3(${name3})}")
    }

    def setPageSizeForFetch(pageSize) {
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.call("{call soknsut.p_set_page_size(${pageSize})}")
    }

    def extendedWindowIdSearch(searchFilter, filterData, pagingAndSortParams) {
        def searchResult
        def ssnSearchEnabledIndicator = institutionalDescriptionService.findByKey().ssnSearchEnabledIndicator
        def ssn
        def pii
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        searchFilter = addEscapeCharacter(searchFilter)
        try {
            searchFilter = "%" + searchFilter + "%"
            //sets the search filter per search request
            sql.call("{call soknsut.p_set_search_filter(${searchFilter})}")
            if (ssnSearchEnabledIndicator) {
                def userName = SecurityContextHolder.context?.authentication?.principal?.username?.toUpperCase()
                sql.call("{$Sql.VARCHAR = call g\$_chk_auth.g\$_check_authorization_fnc('SSN_SEARCH',${userName})}") {ssnSearch -> ssn = ssnSearch}

            } else {
                ssn = 'NO'
            }
            searchResult = extendedSearchComponent(ssn, filterData, pagingAndSortParams)
            sql.call("{$Sql.VARCHAR = call gokfgac.f_spriden_pii_active}") { result -> pii = result }
            if (searchResult?.size() == 0 && pii == "Y") {
                sql.execute("""call gokfgac.p_turn_fgac_off()""")
                searchResult = extendedSearchComponent(ssn, filterData, pagingAndSortParams)
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

    private String addEscapeCharacter(String searchFilter) {
        searchFilter.replaceAll('[-.\'\"_$:,;#@\\/]', {'\\' + it})
    }


    private List extendedSearchComponent(ssn, filterData, pagingAndSortParams) {
        List list
        if (ssn == "YES") {
            list = ExtendedWindowIdSearchView.findAll().each {
                it -> it.formattedName = net.hedtech.banner.general.person.PersonUtility.formatName(it)
            }
            if (list && list?.size() > 0) {
                return list
            }
            list = ExtendedWindowSsnIdSearchView.findAll().each {
                it -> it.formattedName = net.hedtech.banner.general.person.PersonUtility.formatName(it)
            }
            return list

        } else {
            list = ExtendedWindowIdSearchView.findAll().each {
                it -> it.formattedName = net.hedtech.banner.general.person.PersonUtility.formatName(it)
            }
            return list
        }
    }

    def personIdSearch(searchFilter, filterData, pagingAndSortParams) {
        def searchResult
        def ssnSearchEnabledIndicator = institutionalDescriptionService.findByKey().ssnSearchEnabledIndicator
        def ssn
        def pii

        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        searchFilter = addEscapeCharacter(searchFilter)
        try {
            searchFilter = "%" + searchFilter + "%"
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

    public boolean isSSNSearchEnabled() {
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

    public Boolean checkIfAutoCompleteIsEnabledForIDField() {
        Boolean autoCompleteEnabled = false
        String str = retrieveSdaCrosswalkConversionBannerCode(this.AUTO_COMPLETE_ID_CODE, this.AUTO_COMPLETE_GROUP_NAME)
        if (str && str.equals("YES")) {
            autoCompleteEnabled = true
        }
        return autoCompleteEnabled
    }

    public Boolean checkIfAutoCompleteIsEnabledForNameField() {
        Boolean autoCompleteEnabled = false
        String str = retrieveSdaCrosswalkConversionBannerCode(this.AUTO_COMPLETE_NAME_CODE, this.AUTO_COMPLETE_GROUP_NAME)
        if (str && str.equals("YES")) {
            autoCompleteEnabled = true
        }
        return autoCompleteEnabled
    }


    public String fetchNoOfRowsInPageForGUQSRCH() {
        return retrieveSdaCrosswalkConversionBannerCode(this.SEARCH_GUISRCH_CODE, this.SEARCH_GUISRCH_GROUP_NAME)
    }


    public boolean isFormFGACExcluded() {
        //FGAC related information is been stored in db session parameters.
        //Everytime we open a page a new connection is esatablished and there are possibilities the db session param's are purged.
        //So as of now to work around this issue we set FGAC context manually though it is handled in BannerDS.
        //Then we access the session param and get the appropriate result.
        String form = (FormContext.get() ? FormContext.get()[0] : null) // FormContext returns a list, but we'll just use the first entry
        if (form) {
            Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.call("{call gokfgac.p_object_excluded (?) }", [form])
        }
        boolean excluded = false
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def exclusionFlag = sql.firstRow(""" SELECT SYS_CONTEXT ('g\$_vbsi_context','ctx_fg_exclude_object') exclude_object FROM DUAL """).exclude_object
        if (exclusionFlag == 'Y') {
            excluded = true
        }
        return excluded
    }


    public boolean isPIIRestrictionApplicable() {
        boolean restrictionApplicable = false
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def pii
        sql.call("{$Sql.VARCHAR = call gokfgac.f_spriden_pii_active}") { result -> pii = result }
        def isUserExempt
        sql.call("{$Sql.VARCHAR = call gokfgac.f_is_user_exempt}") { result -> isUserExempt = result }

        if (pii == 'Y' && isUserExempt == 'N') {
            restrictionApplicable = true
        }
        return restrictionApplicable
    }


    private List searchComponent(ssn, filterData, pagingAndSortParams) {
        List list
        if (ssn == "YES") {
            //search by id first
            filterData.dynamicdomain = "PersonAdvancedIdFilterView"
            list = PersonAdvancedSearchView.fetchSearchEntityList2(filterData, pagingAndSortParams).each {
                it -> it.formattedName = net.hedtech.banner.general.person.PersonUtility.formatName(it)
            }

            if (list && list?.size() > 0) {
                return list
            }

            // if search by id return no data, then continue search by ssn
            filterData.dynamicdomain = "PersonAdvancedAlternateIdFilterView"
            list = PersonAdvancedSearchView.fetchSearchEntityList2(filterData, pagingAndSortParams).each {
                it -> it.formattedName = net.hedtech.banner.general.person.PersonUtility.formatName(it)
            }

            return list

        } else {
            filterData.dynamicdomain = "PersonAdvancedIdFilterView"

            list = PersonAdvancedSearchView.fetchSearchEntityList2(filterData, pagingAndSortParams).each {
                it -> it.formattedName = net.hedtech.banner.general.person.PersonUtility.formatName(it)
            }
            return list
        }
    }


    private String getNameFormat() {
        if (!_nameFormat) {
            def application = AH.application
            ApplicationContext applicationContext = application.mainContext
            def messageSource = applicationContext.getBean("messageSource")
            messageSource.getMessage("default.name.format", null, LCH.getLocale())
        } else {
            _nameFormat
        }
    }

    private String retrieveSdaCrosswalkConversionBannerCode(String internal, String internalGroup) {
        List lst = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup(internal, internalGroup)
        if (!lst.empty) {
            SdaCrosswalkConversion gtvsdax = lst.get(0)
            return gtvsdax.external
        }
        return null
    }

    public def personIdSearchCount() {
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def count = 0
        try {
            sql.eachRow("select count(distinct pidm) as totalCount from SVQ_IDSRCH ") { row ->
                count = row.totalCount
            }
        } finally {
            sql?.close()
        }
        return count
    }

    public def personSsnIdSearchCount() {
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def count = 0
        try {
            sql.eachRow("select count(distinct pidm) as totalCount from SVQ_ALTISRC ") { row ->
                count = row.totalCount
            }
        } finally {
            sql?.close()
        }
        return count
    }

    public def personNameSearchCount() {
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def count = 0
        try {
            sql.eachRow("select count(distinct pidm) as totalCount from SVQ_ADVSRCH ") { row ->
                count = row.totalCount
            }
        } finally {
            sql?.close()
        }
        return count
    }
}
