/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.query.DynamicFinder

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version
import javax.persistence.GenerationType
import javax.persistence.SequenceGenerator
import net.hedtech.banner.service.DatabaseModifiesState

/**
 * Person Race Table.
 */
@NamedQueries(value = [
@NamedQuery(name = "PersonRace.fetchByPidm",
        query = """ FROM PersonRace a
                            WHERE  a.pidm = :pidm""")
])
 /**
    * Where clause on this entity present in forms:
  * Order by clause on this entity present in forms:
*/
@Entity
@Table(name = "GV_GORPRAC")
@DatabaseModifiesState
class PersonRace implements Serializable {
	
	/**
	 * Surrogate ID for GORPRAC
	 */
	@Id
	@Column(name="GORPRAC_SURROGATE_ID")
	@SequenceGenerator(name ="GORPRAC_SEQ_GEN", allocationSize =1, sequenceName  ="GORPRAC_SURROGATE_ID_SEQUENCE")
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator ="GORPRAC_SEQ_GEN")
	Long id

	/**
	 * Optimistic lock token for GORPRAC
	 */
	@Version
	@Column(name = "GORPRAC_VERSION")
	Long version

	/**
	 * PIDM: Internal System Identification Number.
	 */
	@Column(name = "GORPRAC_PIDM")
	Integer pidm

	/**
	 * RACE CODE: This field identifies the institution defined race code on GORRACE 
	 */
	@Column(name = "GORPRAC_RACE_CDE")
	String race

	/**
	 * ACTIVITY DATE: This field defines the most current date a record is added or changed.
	 */
	@Column(name = "GORPRAC_ACTIVITY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastModified

	/**
	 * USER ID: User who inserted or last updated the data.
	 */
	@Column(name = "GORPRAC_USER_ID")
	String lastModifiedBy

	/**
	 * DATA ORIGIN: Source system that created or updated the row.
	 */
	@Column(name = "GORPRAC_DATA_ORIGIN")
	String dataOrigin

	
	
	public String toString() {
		"""PersonRace[
					id=$id, 
					version=$version, 
					pidm=$pidm, 
					race=$race, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
	}

	
	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof PersonRace)) return false
	    PersonRace that = (PersonRace) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(pidm != that.pidm) return false
        if(race != that.race) return false
        if(lastModified != that.lastModified) return false
        if(lastModifiedBy != that.lastModifiedBy) return false
        if(dataOrigin != that.dataOrigin) return false
        return true
    }

	
	int hashCode() {
		int result
	    result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (pidm != null ? pidm.hashCode() : 0)
        result = 31 * result + (race != null ? race.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
	}

	static constraints = {
		pidm(nullable:false, min: -99999999, max: 99999999)
		race(nullable:false, maxSize:3)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
    }


    //Read Only fields that should be protected against update
    public static readonlyProperties = [ 'pidm', 'race' ]


    /**
     * Finder for advanced filtering and sorting
     * @param filterData , pagingAndSortParams
     * @return filtered and sorted data
     */
    def static countAll(filterData) {
        finderByAll().count(filterData)
    }


    def static fetchSearch(filterData, pagingAndSortParams) {
        def personRaces = finderByAll().find(filterData, pagingAndSortParams)
        return personRaces
    }


    def private static finderByAll = {
        def query = """FROM PersonRace a WHERE a.pidm = :pidm"""
        return new DynamicFinder(PersonRace.class, query, "a")
    }


    static def fetchByPidm(Integer pidm) {
        if (pidm) {
            PersonRace.withSession { session ->
                List personRaceList = session.getNamedQuery('PersonRace.fetchByPidm').setInteger('pidm', pidm).list()
                return personRaceList
            }
        } else {
            return null
        }
    }

}
