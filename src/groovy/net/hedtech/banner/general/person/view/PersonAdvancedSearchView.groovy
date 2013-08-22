/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person.view

import net.hedtech.banner.general.person.PersonUtility
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.QueryBuilder

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

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

    /**
     * The surname prefix of the person
     */
    @Column(name = "CURRENT_SURNAME_PREFIX", length = 60)
    String currentSurnamePrefix



    public String getFormattedName() {
        if (changeIndicator != null) {
            return PersonUtility.formatName(this)
        } else {
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

    def private static finderByAllEntityList2 = {filterData ->
        def query = """from PersonAdvancedSearchView data where exists ( from  ${filterData.dynamicdomain} as af where af.pidm = data.pidm)"""
        if (filterData.params.containsKey('city') || filterData.params.containsKey('state') || filterData.params.containsKey('zip')) {
            def addressfilterData = [params: [:], criteria: []]
            def count = 0
            def criteriaWithOutAddrCount = 0
            def criteriaWithOutAddr = []
            filterData.criteria.each {
                if (it.key.equals("city")) {
                    addressfilterData.params.city = filterData.params["city"]
                    addressfilterData.criteria[count] = it
                    count++
                } else if (it.key.equals("state")) {
                    addressfilterData.params.state = filterData.params["state"]
                    addressfilterData.criteria[count] = it
                    count++
                } else if (it.key.equals("zip")) {
                    addressfilterData.params.city = filterData.params["zip"]
                    addressfilterData.criteria[count] = it
                    count++
                } else {
                    criteriaWithOutAddr[criteriaWithOutAddrCount] = it
                    criteriaWithOutAddrCount++
                }
            }
            if (!addressfilterData.isEmpty()) {
                def addressQuery = QueryBuilder.buildQuery("""select distinct addr.pidm from PersonAddress addr
                                   """, "addr", addressfilterData, [:])
                query += """ and data.pidm in (${addressQuery})"""
                filterData.criteria = criteriaWithOutAddr
            }

        }
        return new DynamicFinder(PersonAdvancedSearchView.class, query, "data")
    }


    def static countAllEntities2(filterData) {
        def tempFilterData = copyFilterData(filterData)
        finderByAllEntityList2(tempFilterData).count(tempFilterData)
    }

    def public static fetchSearchEntityList2(filterData, pagingAndSortParams) {
        def tempFilterData = copyFilterData(filterData)
        finderByAllEntityList2(tempFilterData).find(tempFilterData, pagingAndSortParams)
    }

    def public static copyFilterData = { filterData ->
        def tempFilterData = [params: [:], criteria: []]
        tempFilterData.params = filterData.params
        tempFilterData.criteria = filterData.criteria
        tempFilterData.dynamicdomain = filterData.dynamicdomain
        tempFilterData.sortColumn = filterData.sortColumn
        tempFilterData.includeAlternate = filterData.includeAlternate
        return tempFilterData
    }

}
