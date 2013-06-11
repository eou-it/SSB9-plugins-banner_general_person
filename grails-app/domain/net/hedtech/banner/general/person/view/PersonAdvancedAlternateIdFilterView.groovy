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

/**
 * Person Advanced Search model used for SSN search
 */
@Entity
@Table(name = "SVQ_ALTISRC")
class PersonAdvancedAlternateIdFilterView {

    /**
     * Surrogate ID for SPRIDEN
     */
    @Id
    @Column(name = "SURROGATE_ID")
    Long id

    /**
     * Internal identification number of the person.  This needs to be nullable = true
     * because it is generated by the API
     */
    @Column(name = "PIDM")
    Integer pidm

    /**
     * This field defines the identification number used to access person on-line.  This needs to be nullable = true
     * because it is generated by the API
     */
    @Column(name = "ID")
    String bannerId

    /**
     * This field defines the last name of person.
     */
    @Column(name = "LAST_NAME")
    String lastName

    /**
     * This field identifies the first name of person.
     */
    @Column(name = "FIRST_NAME")
    String firstName

    /**
     * This field identifies the middle name of person.
     */
    @Column(name = "MI")
    String middleName

    /**
     * This field defines whether type of change made to the record was an ID number change or a name change. Val values: I - ID change, N - name change.
     */
    @Column(name = "CHANGE_INDICATOR", length = 1)
    String changeIndicator

    /**
     * Version column which is used as a optimistic lock token for SPRIDEN
     */
    @Version
    @Column(name = "VERSION", length = 19)
    Long version

    public String toString() {
        """PersonAlternateIdFilterView[
					id=$id,
					pidm=$pidm,
					bannerId=$bannerId,
					lastName=$lastName,
					firstName=$firstName,
					middleName=$middleName,
					changeIndicator=$changeIndicator
					"""
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
                       where exists( from PersonAdvancedAlternateIdFilterView as af where af.pidm = a.pidm )
                      """

        return new DynamicFinder(PersonAdvancedSearchView.class, query, "a")
    }


    def public static fetchSearchEntityList2(filterData, pagingAndSortParams) {
        finderByAllEntityList2(filterData, pagingAndSortParams).find(filterData, pagingAndSortParams)
    }

    def private static finderByAllEntityList2 = {filterData, pagingAndSortParams ->

        def query = """from PersonAdvancedSearchView data
                   where data.id in (select
                   max ( a.id )  from PersonAdvancedSearchView a
                       where exists ( from PersonAdvancedAlternateIdFilterView as af where af.pidm = a.pidm )
                   group by a.pidm, a.bannerId, a.lastName, a.firstName, a.middleName, a.surnamePrefix, a.changeIndicator, a.nameType ${ QueryBuilder.dynamicGroupby("a", filterData?.params + (null == filterData?.extraparams ? [:] : filterData?.extraparams) - (null == filterData?.removeparams ? [:] : filterData?.removeparams))}
                   having CASE WHEN 1 =
                           ( ${QueryBuilder.buildQuery("""select sum(count(distinct b.pidm)) as total from PersonAdvancedSearchView b
                               where exists ( from PersonAdvancedAlternateIdFilterView as afb where afb.pidm = b.pidm )
                               group by b.pidm, b.bannerId, b.lastName, b.firstName, b.middleName, b.surnamePrefix, b.changeIndicator, b.nameType ${ QueryBuilder.dynamicGroupby("b", filterData?.params + (null == filterData?.extraparams ? [:] : filterData?.extraparams) - (null == filterData?.removeparams ? [:] : filterData?.removeparams))}
                               having b.changeIndicator is null """, "b", filterData ,[:])}   )
                           THEN a.changeIndicator
                           ELSE null
                           END is null   )
                           """

        return new DynamicFinder(PersonAdvancedSearchView.class, query, "data")
   }

    def private static finderByAllEntityList2Count = {filterData ->
        def query = """from PersonAdvancedSearchView data
                   where data.id in (select
                   max ( a.id )  from PersonAdvancedSearchView a
                       where exists ( from PersonAdvancedAlternateIdFilterView as af where af.pidm = a.pidm )
                   group by a.pidm, a.bannerId, a.lastName, a.firstName, a.middleName, a.changeIndicator  ${ QueryBuilder.dynamicGroupby("a", filterData?.params)}
                   having CASE WHEN 1 =
                           ( ${QueryBuilder.buildCountQuery("""select sum(count(distinct b.pidm)) as total from PersonAdvancedSearchView b
                               where exists ( from PersonAdvancedAlternateIdFilterView as afb where afb.pidm = b.pidm )
                               group by b.pidm, b.bannerId, b.lastName, b.firstName, b.middleName, b.changeIndicator ${ QueryBuilder.dynamicGroupby("b", filterData?.params)}
                               having b.changeIndicator is null """, "b", filterData?.criteria)}   )
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
