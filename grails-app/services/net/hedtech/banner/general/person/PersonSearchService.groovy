/*********************************************************************************
Copyright 2013 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
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
import net.hedtech.banner.security.FormContext

class PersonSearchService {

    static transactional = false
    def sessionFactory
    private final Logger log = Logger.getLogger(getClass())

    def institutionalDescriptionService

    private _nameFormat
    public static final String AUTO_COMPLETE_GROUP_NAME = 'IDNAMESEARCH'
    public static final String AUTO_COMPLETE_ID_CODE='ID_AUTO'
    public static final String AUTO_COMPLETE_NAME_CODE='NAME_AUTO'
    public static final String SEARCH_GUISRCH_CODE='SEARCH_MAX'
    public static final String SEARCH_GUISRCH_GROUP_NAME='GUISRCH'

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
        String str =  retrieveSdaCrosswalkConversionBannerCode( this.AUTO_COMPLETE_NAME_CODE, this.AUTO_COMPLETE_GROUP_NAME )
        if (str && str.equals("YES")) {
            autoCompleteEnabled = true
        }
        return autoCompleteEnabled
    }


    public String fetchNoOfRowsInPageForGUQSRCH() {
        return retrieveSdaCrosswalkConversionBannerCode( this.SEARCH_GUISRCH_CODE,this.SEARCH_GUISRCH_GROUP_NAME )
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
        if(exclusionFlag == 'Y') {
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

        if(pii == 'Y' && isUserExempt == 'N') {
            restrictionApplicable = true
        }
        return restrictionApplicable
    }


    private List searchComponent(ssn, filterData, pagingAndSortParams) {
        def currentList
        List list

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

    private String retrieveSdaCrosswalkConversionBannerCode(String internal,  String internalGroup) {
        List lst = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup( internal,internalGroup )
        if (!lst.empty) {
            SdaCrosswalkConversion gtvsdax = lst.get(0)
            return gtvsdax.external
        }
        return null
    }
}
