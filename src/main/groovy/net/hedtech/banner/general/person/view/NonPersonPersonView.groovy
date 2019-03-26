/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person.view

import net.hedtech.banner.query.DynamicFinder
import javax.persistence.*
import net.hedtech.banner.query.criteria.CriteriaParamUtil

/**
 * NonPerson Spriden model.
 */
@Entity
@Table(name = "SVQ_SPRIDEN")
@NamedQueries(value = [
@NamedQuery(name = "NonPersonPersonView.search",
query = """FROM  NonPersonPersonView a
WHERE ((a.bannerId like :id and :id is not null) or :id is null)
      and (((soundex(a.lastName) = soundex(:soundexLastName)) and :soundexLastName is not null) or
              (a.searchLastName like :lastName and
               :lastName is not null) or (:lastName is null and :soundexLastName is null))
        and ((a.changeIndicator = :changeIndicator and :changeIndicator is not null) or
              :changeIndicator is null)
        and((a.nameType = :nameType and :nameType is not null) or
              :nameType is null)
	   and a.entityIndicator = 'C' """),
@NamedQuery(name = "NonPersonPersonView.searchCount",
query = """select count(a.bannerId) FROM  NonPersonPersonView a
     WHERE ((a.bannerId like :id and :id is not null) or :id is null)
      and (((soundex(a.lastName) = soundex(:soundexLastName)) and :soundexLastName is not null) or
              (a.searchLastName like :lastName and
               :lastName is not null) or (:lastName is null and :soundexLastName is null))
        and ((a.changeIndicator = :changeIndicator and :changeIndicator is not null) or
              :changeIndicator is null)
        and((a.nameType = :nameType and :nameType is not null) or
              :nameType is null)
	   and a.entityIndicator = 'C' """)
])
class NonPersonPersonView extends PersonView {

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
     * The Last Name field in SOUNDEX phonetic format.
     */
    @Column(name = "SOUNDEX_LAST_NAME", length = 4)
    String soundexLastName

    /**
     * The name type of the person
     */
    @Column(name = "NAME_TYPE", length = 4)
    String nameType


    public String toString() {
        """NonPersonPersonView[
					id=$id,
					pidm=$pidm,
					bannerId=$bannerId,
					lastName=$lastName,
					nameType=$nameType,
					changeIndicator=$changeIndicator,
					entityIndicator=$entityIndicator]"""
    }

    /**
     * Returns count of Non Persons.
     * @param id search parameter
     * @param lastName search parameter
     * @param soundexLastName search parameter
     * @param changeIndicator search parameter
     * @param nameType search parameter
     * @return count of Non-Persons found by the query
     */
    public static int fetchCountPerson(id, lastName, soundexLastName, changeIndicator, nameType) {
        def idCriteria
        def lastNameCriteria


        if (id) {
            idCriteria = "%" + id.toUpperCase() + "%"
        }

        if (lastName) {
            lastNameCriteria = "%" + lastName.toUpperCase() + "%"
        }

        def count = NonPersonPersonView.withSession {session ->

            org.hibernate.Query query = session.getNamedQuery('NonPersonPersonView.searchCount').
                    setString('id', idCriteria).
                    setString('lastName', lastNameCriteria).
                    setString('soundexLastName', soundexLastName).
                    setString("changeIndicator", changeIndicator).
                    setString("nameType", nameType)


            query.list().get(0)
        }
        return count
    }

    /**
     * Fetches Non Persons.
     * @param id search parameter
     * @param lastName search parameter
     * @param soundexLastName search parameter
     * @param changeIndicator search parameter
     * @param nameType search parameter
     * @param pagingAndSortParams search parameter
     * @return list of Non-Persons
     */
    public static List fetchPerson(id, lastName, soundexLastName, changeIndicator, nameType, pagingAndSortParams) {
        def idCriteria
        def lastNameCriteria

        if (id) {
            idCriteria = "%" + id.toUpperCase() + "%"
        }

        if (lastName) {
            lastNameCriteria = "%" + lastName.toUpperCase() + "%"
        }


        def persons = NonPersonPersonView.withSession {session ->

            org.hibernate.Query query = session.getNamedQuery('NonPersonPersonView.search').
                    setString('id', idCriteria).
                    setString('lastName', lastNameCriteria).
                    setString('soundexLastName', soundexLastName).
                    setString("changeIndicator", changeIndicator).
                    setString("nameType", nameType)

            query.setMaxResults(pagingAndSortParams.max);
            query.setFirstResult(pagingAndSortParams.offset);
            query.list()
        }
        return persons
    }

    /**
     * Returns the map of non persons.
     * @param id search parameter
     * @param lastName search parameter
     * @param soundexLastName search parameter
     * @param changeIndicator search parameter
     * @param nameType search parameter
     * @param pagingAndSortParams search parameter
     * @return list of Non-Persons
     */
    public static Map fetchPersonList(id, lastName, soundexLastName, changeIndicator, nameType, pagingAndSortParams) {
        return [list: NonPersonPersonView.fetchPerson(id, lastName, soundexLastName, changeIndicator, nameType, pagingAndSortParams),
                totalCount: NonPersonPersonView.fetchCountPerson(id, lastName, soundexLastName, changeIndicator, nameType)]
    }

    /**
     * Fetches the list of Non Persons.
     * @param filterData filter data
     * @param pagingAndSortParams
     * @return list of Non-Persons
     */
    def public static fetchSearchEntityList(filterData, pagingAndSortParams) {

        if (filterData?.params?.searchLastName) {
            //filterData?.params?.searchLastName = filterData?.params?.searchLastName?.replaceAll("[\\s]", "")

            String value = CriteriaParamUtil.getValue(filterData?.params?.searchLastName);
            value = value.replaceAll("[\\s]", "")
            CriteriaParamUtil.setValue(filterData?.params?.searchLastName, value);
        }

        finderByAllEntityList().find(filterData, pagingAndSortParams)
    }


     /**
     * Fetches the list of Non Persons by soundex.
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
        def query = """FROM  NonPersonPersonView a
	                   WHERE a.entityIndicator = 'C' """

        return new DynamicFinder(NonPersonPersonView.class, query, "a")
    }


    /**
     *  Soundex Query String Builder
     */
    def private static finderByAllSoundexEntityList = {filterData ->
        def query = """FROM  NonPersonPersonView a
          WHERE ((soundex(a.lastName) = soundex(:soundexLastName)) or :soundexLastName is null)
	            and  a.entityIndicator = 'C' """

        return new DynamicFinder(NonPersonPersonView.class, query, "a")
    }
}
