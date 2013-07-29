
/*******************************************************************************
Copyright 2013 Ellucian Company L.P. and its affiliates.
*******************************************************************************/
package net.hedtech.banner.general.person

import net.hedtech.banner.query.DynamicFinder

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import javax.persistence.GenerationType
import javax.persistence.SequenceGenerator
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne

import net.hedtech.banner.general.system.AdditionalIdentificationType


/**
 * This Table contains one to many additional Ids per PIDM.
 */
@Entity
@Table(name = "GV_GORADID")
class AdditionalID implements Serializable {

	/**
	 * Surrogate ID for GORADID
	 */
	@Id
	@Column(name="GORADID_SURROGATE_ID")
	@SequenceGenerator(name ="GORADID_SEQ_GEN", allocationSize =1, sequenceName  ="GORADID_SURROGATE_ID_SEQUENCE")
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator ="GORADID_SEQ_GEN")
	Long id

	/**
	 * Optimistic lock token for GORADID
	 */
	@Version
	@Column(name = "GORADID_VERSION")
	Long version

	/**
	 * PIDM:Internal identification number of the Validation Type.
	 */
	@Column(name = "GORADID_PIDM")
	Integer pidm

	/**
	 * ADDITIONAL IDENTIFICATION: Additional Identification Code.
	 */
	@Column(name = "GORADID_ADDITIONAL_ID")
	String additionalId

	/**
	 * ACTIVITY DATE:Date on which the record was created or last updated.
	 */
	@Column(name = "GORADID_ACTIVITY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastModified

	/**
	 * USER IDENTIFICATION: User ID of the user who created or last updated the record.
	 */
	@Column(name = "GORADID_USER_ID")
	String lastModifiedBy

	/**
	 * DATA ORIGIN:Source system that created or updated the row.
	 */
	@Column(name = "GORADID_DATA_ORIGIN")
	String dataOrigin


	/**
	 * Foreign Key : FK1_GORADID_INV_GTVADID_CODE
	 */
	@ManyToOne
	@JoinColumns([
		@JoinColumn(name="GORADID_ADID_CODE", referencedColumnName="GTVADID_CODE")
		])
	AdditionalIdentificationType additionalIdentificationType


	public String toString() {
		"""AdditionalID[
					id=$id,
					version=$version,
					pidm=$pidm,
					additionalId=$additionalId,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin,
					additionalIdentificationType=$additionalIdentificationType]"""
	}


	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof AdditionalID)) return false
	    AdditionalID that = (AdditionalID) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(pidm != that.pidm) return false
        if(additionalId != that.additionalId) return false
        if(lastModified != that.lastModified) return false
        if(lastModifiedBy != that.lastModifiedBy) return false
        if(dataOrigin != that.dataOrigin) return false
        if(additionalIdentificationType != that.additionalIdentificationType) return false
        return true
    }


	int hashCode() {
		int result
	    result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (pidm != null ? pidm.hashCode() : 0)
        result = 31 * result + (additionalId != null ? additionalId.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (additionalIdentificationType != null ? additionalIdentificationType.hashCode() : 0)
        return result
	}

	static constraints = {
		pidm(nullable:false, min: -99999999, max: 99999999)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
		additionalIdentificationType(nullable:false)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = [ 'pidm', 'additionalId', 'additionalIdentificationType' ]


    /**
     * Finder for advanced filtering and sorting
     * @param filterData , pagingAndSortParams
     * @return filtered and sorted data
     */
    def static countAll(filterData) {
        finderByAll().count(filterData)
    }


    def static fetchSearch(filterData, pagingAndSortParams) {
        def additionalIDs = finderByAll().find(filterData, pagingAndSortParams)
        return additionalIDs
    }


    def private static finderByAll = {
        def query = """FROM AdditionalID a WHERE a.pidm = :pidm"""
        return new DynamicFinder(AdditionalID.class, query, "a")
    }
}
