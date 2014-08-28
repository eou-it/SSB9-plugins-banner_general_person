/*******************************************************************************
Copyright 2014 Ellucian Company L.P. and its affiliates.
*******************************************************************************/
package net.hedtech.banner.general.overall

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Transient
import javax.persistence.GenerationType
import javax.persistence.SequenceGenerator
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import net.hedtech.banner.general.system.CommonMatchingSource


/**
 * Common Matching Results Global Temporary Table
 */

@Entity
@Table(name = "GOTCMRT")
class CommonMatchingResultsGlobalTemporary implements Serializable {

	/**
	 * Surrogate ID for GOTCMRT
	 */
	@Id
	@Column(name="GOTCMRT_SURROGATE_ID")
	@SequenceGenerator(name ="GOTCMRT_SEQ_GEN", allocationSize =1, sequenceName  ="GOTCMRT_SURROGATE_ID_SEQUENCE")
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator ="GOTCMRT_SEQ_GEN")
	Long id

	/**
	 * Optimistic lock token for GOTCMRT
	 */
	@Version
	@Column(name = "GOTCMRT_VERSION")
	Long version

	/**
	 * PRIORITY NUMBER: Priority number of rule to be processed.
	 */
	@Column(name = "GOTCMRT_CMSR_PRIORITY_NO")
	Integer priorityNumberPriorityNumber

	/**
	 * PIDM: The pidm returned as matched or suspense for the rule as a result of the Common Matching process.
	 */
	@Column(name = "GOTCMRT_PIDM")
	Integer pidm

	/**
	 * RESULT TYPE: Primary match that generated the result.
	 */
	@Column(name = "GOTCMRT_RESULT_TYPE")
	String resultType

	/**
	 * MATCH COUNT: Internal use only.
	 */
	@Column(name = "GOTCMRT_MATCH_COUNT")
	Integer matchCount

	/**
	 * MISSING COUNT: Internal use only.
	 */
	@Column(name = "GOTCMRT_MISSING_COUNT")
	Integer missingCount

	/**
	 * UNMATCH COUNT: Internal use only.
	 */
	@Column(name = "GOTCMRT_UNMATCH_COUNT")
	Integer unmatchCount

	/**
	 * MESSAGE: The results of the Common Matching identifying match conditions that are met or unmet.
	 */
	@Column(name = "GOTCMRT_MESSAGE")
	String message

	/**
	 * RESULT INDICATOR: Match result for the row.
	 */
	@Column(name = "GOTCMRT_RESULT_IND")
	String resultIndicator

	/**
	 * ID ROWID: The rowid of the spriden record returned for the ID match.
	 */
	@Column(name = "GOTCMRT_SPRIDEN_ID_ROWID")
	String personIdentificationIdRowid

	/**
	 * NAME ROWID: The rowid of the spriden record returned for the NAME match.
	 */
	@Column(name = "GOTCMRT_SPRIDEN_NAME_ROWID")
	String personIdentificationNameRowid

	/**
	 * ADDRESS ROWID: The rowid of the spraddr record returned for an address match.
	 */
	@Column(name = "GOTCMRT_SPRADDR_ROWID")
	String addressRowid

	/**
	 * TELEPHONE ROWID: The rowid of the sprtele record returned for a telephone match.
	 */
	@Column(name = "GOTCMRT_SPRTELE_ROWID")
	String persontelephoneRowid

	/**
	 * E-MAIL ROWID: The rowid of the goremal record returned for an e-mail match.
	 */
	@Column(name = "GOTCMRT_GOREMAL_ROWID")
	String goremalRowid

	/**
	 * Last modification date for GOTCMRT
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GOTCMRT_ACTIVITY_DATE")
	Date lastModified

	/**
	 * Last modified by column for GOTCMRT
	 */
	@Column(name = "GOTCMRT_USER_ID")
	String lastModifiedBy

	/**
	 * Data origin column for GOTCMRT
	 */
	@Column(name = "GOTCMRT_DATA_ORIGIN")
	String dataOrigin


	/**
	 * Foreign Key : FKV_GOTCMRT_INV_GTVCMSC_CODE
	 */
	@ManyToOne
	@JoinColumns([
		@JoinColumn(name="GOTCMRT_CMSC_CODE", referencedColumnName="GTVCMSC_CODE")
		])
	CommonMatchingSource commonMatchingSource


	public String toString() {
		"""CommonMatchingResultsGlobalTemporary[
					id=$id,
					version=$version,
					priorityNumberPriorityNumber=$priorityNumberPriorityNumber,
					pidm=$pidm,
					resultType=$resultType,
					matchCount=$matchCount,
					missingCount=$missingCount,
					unmatchCount=$unmatchCount,
					message=$message,
					resultIndicator=$resultIndicator,
					personIdentificationIdRowid=$personIdentificationIdRowid,
					personIdentificationNameRowid=$personIdentificationNameRowid,
					addressRowid=$addressRowid,
					persontelephoneRowid=$persontelephoneRowid,
					goremalRowid=$goremalRowid,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin,
					commonMatchingSource=$commonMatchingSource]"""
	}


	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof CommonMatchingResultsGlobalTemporary)) return false
	    CommonMatchingResultsGlobalTemporary that = (CommonMatchingResultsGlobalTemporary) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(priorityNumberPriorityNumber != that.priorityNumberPriorityNumber) return false
        if(pidm != that.pidm) return false
        if(resultType != that.resultType) return false
        if(matchCount != that.matchCount) return false
        if(missingCount != that.missingCount) return false
        if(unmatchCount != that.unmatchCount) return false
        if(message != that.message) return false
        if(resultIndicator != that.resultIndicator) return false
        if(personIdentificationIdRowid != that.personIdentificationIdRowid) return false
        if(personIdentificationNameRowid != that.personIdentificationNameRowid) return false
        if(addressRowid != that.addressRowid) return false
        if(persontelephoneRowid != that.persontelephoneRowid) return false
        if(goremalRowid != that.goremalRowid) return false
        if(lastModified != that.lastModified) return false
        if(lastModifiedBy != that.lastModifiedBy) return false
        if(dataOrigin != that.dataOrigin) return false
        if(commonMatchingSource != that.commonMatchingSource) return false
        return true
    }


	int hashCode() {
		int result
	    result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (priorityNumberPriorityNumber != null ? priorityNumberPriorityNumber.hashCode() : 0)
        result = 31 * result + (pidm != null ? pidm.hashCode() : 0)
        result = 31 * result + (resultType != null ? resultType.hashCode() : 0)
        result = 31 * result + (matchCount != null ? matchCount.hashCode() : 0)
        result = 31 * result + (missingCount != null ? missingCount.hashCode() : 0)
        result = 31 * result + (unmatchCount != null ? unmatchCount.hashCode() : 0)
        result = 31 * result + (message != null ? message.hashCode() : 0)
        result = 31 * result + (resultIndicator != null ? resultIndicator.hashCode() : 0)
        result = 31 * result + (personIdentificationIdRowid != null ? personIdentificationIdRowid.hashCode() : 0)
        result = 31 * result + (personIdentificationNameRowid != null ? personIdentificationNameRowid.hashCode() : 0)
        result = 31 * result + (addressRowid != null ? addressRowid.hashCode() : 0)
        result = 31 * result + (persontelephoneRowid != null ? persontelephoneRowid.hashCode() : 0)
        result = 31 * result + (goremalRowid != null ? goremalRowid.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (commonMatchingSource != null ? commonMatchingSource.hashCode() : 0)
        return result
	}

	static constraints = {
		priorityNumberPriorityNumber(nullable:false, min: -99, max: 99)
		pidm(nullable:false, min: -99999999, max: 99999999)
		resultType(nullable:false, maxSize:1)
		matchCount(nullable:false, min: -99, max: 99)
		missingCount(nullable:false, min: -99, max: 99)
		unmatchCount(nullable:false, min: -99, max: 99)
		message(nullable:false, maxSize:250)
		resultIndicator(nullable:false, maxSize:1)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
		commonMatchingSource(nullable:false)
    }
}
