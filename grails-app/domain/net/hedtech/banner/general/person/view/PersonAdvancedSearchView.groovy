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
package net.hedtech.banner.general.person.view

import net.hedtech.banner.query.DynamicFinder
import javax.persistence.*
import net.hedtech.banner.query.QueryBuilder
import net.hedtech.banner.person.dsl.NameTemplate

/**
 * Person Advanced Search model.
 */
@Entity
@Table(name = "SVQ_SADVSRC")
class PersonAdvancedSearchView extends PersonView {

    /**
     * This field defines the current identification number.
     */
    @Column(name = "CURRENT_ID")
    String currentBannerId
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

    /**
     * The name type of the person
     */
    @Column(name = "NAME_TYPE")
    String nameType

    /**
     * The city
     */
    @Column(name = "CITY")
    String city

    /**
     * The state
     */
    @Column(name = "STATE")
    String state

    /**
     * The zip code
     */
    @Column(name = "ZIP")
    String zip

    /**
     * The sex of the person
     */
    @Column(name = "SEX")
    String sex

    /**
     * This field defines the last name of person.
     */
    @Column(name = "CURRENT_LAST_NAME")
    String currentLastName

    /**
     * This field entifies the first name of person.
     */
    @Column(name = "CURRENT_FIRST_NAME")
    String currentFirstName

    /**
     * This field entifies the mdle name of person.
     */
    @Column(name = "CURRENT_MI")
    String currentMiddleName

    public String getFormattedName() {
        if(changeIndicator != null) {
             return NameTemplate.format {
                    lastName currentLastName
                    firstName currentFirstName
                    mi currentMiddleName
                    surnamePrefix surnamePrefix
                    nameSuffix nameSuffix
                    namePrefix namePrefix
                    formatTemplate getNameFormat()
                    text
                }
        }else {
            return super.getFormattedName()
        }

    }

    public String getBaseFormattedName() {
        return super.getFormattedName()
    }

    //@Transient
    //String formattedName

    public String toString() {
        """PersonAdvancedSearchView[
					id=$id,
					pidm=$pidm,
					bannerId=$bannerId,
					currentBannerId=$currentBannerId,
					lastName=$lastName,
					firstName=$firstName,
					middleName=$middleName,
					birthDate=$birthDate,
					ssn=$ssn,
					changeIndicator=$changeIndicator,
					entityIndicator=$entityIndicator,
					nameType=$nameType,
					city=$city,
					state=$state,
					zip=$zip,
					sex=$sex,
					formattedName=$formattedName"""
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
        def query = """FROM PersonAdvancedSearchView a
                      WHERE a.pidm IN (:pidms)"""

        return new DynamicFinder(PersonAdvancedSearchView.class, query, "a")
    }

    def private static finderByAllEntityList2Count = {filterData ->
        def query = """from PersonAdvancedSearchView data
                   where data.id in (select
                   max(a.id)  from PersonAdvancedSearchView a
                       where exists ( from ${filterData.dynamicdomain} as af where af.pidm = a.pidm )
                   group by a.pidm, a.bannerId, a.lastName, a.firstName, a.middleName, a.changeIndicator  ${ QueryBuilder.dynamicGroupby("a", filterData?.params +  (null == filterData?.extraparams ? [:] : filterData?.extraparams) - (null == filterData?.removeparams ? [:] : filterData?.removeparams) )}
                   having CASE WHEN 1 =
                           ( ${QueryBuilder.buildQuery("""select sum(count(distinct b.pidm)) as total from PersonAdvancedSearchView b
                               where exists ( from ${filterData.dynamicdomain} as afb where afb.pidm = b.pidm )
                               group by b.pidm, b.bannerId, b.lastName, b.firstName, b.middleName, b.changeIndicator ${ QueryBuilder.dynamicGroupby("b", filterData?.params + (null == filterData?.extraparams ? [:] : filterData?.extraparams) - (null == filterData?.removeparams ? [:] : filterData?.removeparams) )}
                               having b.changeIndicator is null """, "b", filterData?.criteria,[:])}   )
                           THEN a.changeIndicator
                           ELSE null
                           END is null   )
                           """

        return new DynamicFinder(PersonAdvancedSearchView.class, query, "data")
    }

    def static countAllEntities2(filterData) {
        finderByAllEntityList2Count(filterData).count(filterData)
    }

}
