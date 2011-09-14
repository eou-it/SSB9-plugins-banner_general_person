/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
package com.sungardhe.banner.general.person

import com.sungardhe.banner.general.system.HoldType
import com.sungardhe.banner.general.system.Originator
import com.sungardhe.banner.service.DatabaseModifiesState

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GenerationType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Version

import javax.persistence.Temporal
import javax.persistence.TemporalType
import org.hibernate.annotations.Type

/**
 * Person Related Holds model.
 */
@NamedQueries(value = [
@NamedQuery(name = "PersonRelatedHold.fetchByPidm",
query =  """FROM PersonRelatedHold a
	  	   WHERE a.pidm = :pidm
	  	ORDER BY a.fromDate desc, a.toDate desc """),
@NamedQuery(name = "PersonRelatedHold.fetchByPidmAndDateBetween",
query =  """FROM PersonRelatedHold a
	  	   WHERE a.pidm = :pidm
	  	     AND TRUNC(:compareDate) BETWEEN TRUNC(a.fromDate) AND TRUNC(a.toDate)
	  	ORDER BY a.fromDate desc, a.toDate desc """),
@NamedQuery(name = "PersonRelatedHold.fetchByPidmDateAndHoldType",
query =  """FROM  PersonRelatedHold a
	  	   WHERE a.pidm = :pidm
	  	     AND a.holdType.code = :holdType
	  	     AND ( TRUNC(:compareDate) >= TRUNC(a.fromDate)
	  	           AND TRUNC(:compareDate) < TRUNC(a.toDate) )
	  	ORDER BY a.fromDate desc, a.toDate desc """),
@NamedQuery(name = "PersonRelatedHold.fetchByPidmAndDateCompare",
query =  """FROM  PersonRelatedHold a
	  	   WHERE a.pidm = :pidm
	  	     AND ( TRUNC(:compareDate) >= TRUNC(a.fromDate)
	  	           AND TRUNC(:compareDate) < TRUNC(a.toDate) )
	  	ORDER BY a.fromDate desc, a.toDate desc """)])

@Entity
@Table(name="SV_SPRHOLD")
@DatabaseModifiesState 
class PersonRelatedHold implements Serializable {

    /**
	 * Surrogate ID for SPRHOLD
	 */
	@Id
	@Column(name="SPRHOLD_SURROGATE_ID")
	@SequenceGenerator(name ="SPRHOLD_SEQ_GEN", allocationSize =1, sequenceName  ="SPRHOLD_SURROGATE_ID_SEQUENCE")
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator ="SPRHOLD_SEQ_GEN")
	Integer id

    /**
	 * Optimistic lock token for SPRHOLD
	 */
	@Version
	@Column(name="SPRHOLD_VERSION")
	Integer version
    
	/**
	 * Internal identification number of the person.
	 */
	@Column(name="SPRHOLD_PIDM")
	Integer pidm

    /**
	 * Foreign Key : FK1_SPRHOLD_INV_STVHLDD_CODE
	 */
	@ManyToOne
	@JoinColumns([
		@JoinColumn(name="SPRHOLD_HLDD_CODE", referencedColumnName="STVHLDD_CODE")
		])
	HoldType holdType

    /**
	 * This field identifies the system user signon id initiating hold.
	 */
	@Column(name="SPRHOLD_USER")
	String userData

	/**
	 * This field identifies the effective begin date of hold.
	 */
	@Column(name="SPRHOLD_FROM_DATE")
	Date fromDate

	/**
	 * This field identifies the end date hold expires. 
	 */
	@Column(name="SPRHOLD_TO_DATE")
	Date toDate

	/**
	 * This field indicates that only the system user who entered the hold may release the hold.  Valid value is:  Y - allow only system user to release       hold.
	 */
    @Type(type = "yes_no")
	@Column(name="SPRHOLD_RELEASE_IND")
	Boolean releaseIndicator = false

	/**
	 * Free format field which identifies the reason hold was placed.
	 */
	@Column(name="SPRHOLD_REASON")
	String reason

	/**
	 * This field identifies a dollar amount associated with hold.
	 */
	@Column(name="SPRHOLD_AMOUNT_OWED")
	BigDecimal amountOwed

    /**
	 * Foreign Key : FK1_SPRHOLD_INV_STVORIG_CODE
	 */
	@ManyToOne
	@JoinColumns([
		@JoinColumn(name="SPRHOLD_ORIG_CODE", referencedColumnName="STVORIG_CODE")
		])
	Originator originator
    
	/**
	 * This field defines most current date record is created or changed.
	 */
	@Column(name="SPRHOLD_ACTIVITY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastModified

	/**
	 * Last modified by column for SPRHOLD
	 */
	@Column(name="SPRHOLD_USER_ID")
	String lastModifiedBy

    /**
	 * DATA SOURCE: Source system that created or updated the row
	 */
	@Column(name="SPRHOLD_DATA_ORIGIN")
	String dataOrigin


	public static readonlyProperties = ['pidm', 'userData']


	public String toString() {
		"""PersonRelatedHold[
		            id=$id,
		            version=$version,
					pidm=$pidm,
					holdType=$holdType, 
					userData=$userData, 
					fromDate=$fromDate, 
					toDate=$toDate, 
					releaseIndicator=$releaseIndicator, 
					reason=$reason, 
					amountOwed=$amountOwed,
					originator=$originator, 
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
	}


    static constraints = {
		pidm(nullable:false, maxsize: 22)
        holdType(nullable:false)
		userData(nullable:false, maxSize:30)
		fromDate(nullable:false, validator: {val, obj ->
            if (( val  != null) && (val > obj.toDate)) {
                return 'invalid.fromDateGreaterThanToDate'
            }
        })
		toDate(nullable:false, validator: {val, obj ->
            if ((val != null) && (val < obj.fromDate)) {
                return 'invalid.toDateLessThanFromDate'
            }
        })
		releaseIndicator(nullable:false)
		reason(nullable:true, maxSize:30)
		amountOwed(nullable:true, max: 99999.99 , min: 00000.00, scale: 2 )
        originator(nullable:true)
		lastModified(nullable:true)
        lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
		/**
	     * Please put all the custom constraints in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(personrelatedhold_custom_constraints) ENABLED START*/

	    /*PROTECTED REGION END*/
    }


	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof PersonRelatedHold)) return false
	    PersonRelatedHold that = (PersonRelatedHold) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(pidm != that.pidm) return false
        if(holdType != that.holdType) return false
        if(userData != that.userData) return false
        if(fromDate != that.fromDate) return false
        if(toDate != that.toDate) return false
        if(releaseIndicator != that.releaseIndicator) return false
        if(reason != that.reason) return false
        if(amountOwed != that.amountOwed) return false
        if(originator != that.originator) return false
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
        result = 31 * result + (holdType != null ? holdType.hashCode() : 0)
        result = 31 * result + (userData != null ? userData.hashCode() : 0)
        result = 31 * result + (fromDate != null ? fromDate.hashCode() : 0)
        result = 31 * result + (toDate != null ? toDate.hashCode() : 0)
        result = 31 * result + (releaseIndicator != null ? releaseIndicator.hashCode() : 0)
        result = 31 * result + (reason != null ? reason.hashCode() : 0)
        result = 31 * result + (amountOwed != null ? amountOwed.hashCode() : 0)
        result = 31 * result + (originator != null ? originator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
	}


    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personrelatedhold_custom_methods) ENABLED START*/

    /**
     * Retrieve all person related hold records.
     */
    public static List fetchByPidm(Integer pidm) {

        def personRelatedHolds = PersonRelatedHold.withSession {session ->
            session.getNamedQuery('PersonRelatedHold.fetchByPidm').setInteger('pidm', pidm).list()
        }

        return personRelatedHolds
    }

    /**
     * Retrieve person related hold records where the date sent in is between
     * the hold from date and the hold to date.
     */
    public static List fetchByPidmAndDateBetween(Integer pidm, Date compareDate) {

        def personRelatedHolds = PersonRelatedHold.withSession {session ->
            session.getNamedQuery('PersonRelatedHold.fetchByPidmAndDateBetween').setInteger('pidm', pidm).setDate('compareDate', compareDate).list()
        }

        return personRelatedHolds
    }

     /**
     * Retrieve person related hold records where the hold type sent in exists and the date sent in is greater
     * than or equal to the hold from date and less than the hold to date.
     */
     public static List fetchByPidmDateAndHoldType(Integer pidm, Date compareDate, String holdType) {

        def personRelatedHolds = PersonRelatedHold.withSession {session ->
            session.getNamedQuery('PersonRelatedHold.fetchByPidmDateAndHoldType').setInteger('pidm', pidm).setDate('compareDate', compareDate).setString('holdType', holdType).list()
        }

        return personRelatedHolds
    }

    /**
     * Retrieve person related hold records where the date sent in is greater
     * than or equal to the hold from date and less than the hold to date.
     */
    public static List fetchByPidmAndDateCompare(Integer pidm, Date compareDate) {

        def personRelatedHolds = PersonRelatedHold.withSession {session ->
            session.getNamedQuery('PersonRelatedHold.fetchByPidmAndDateCompare').setInteger('pidm', pidm).setDate('compareDate', compareDate).list()
        }

        return personRelatedHolds
    }

    /**
     * Check for the existence of a person related registration hold record where the date sent in is greater
     * than or equal to the hold from date and less than the hold to date.  The person related hold type
     * must be defined as a registration hold on the STVHLDD record.
     */
    public static Boolean registrationHoldsExist(Integer pidm, Date compareDate) {
        def registrationHoldsExist = false
        def personRelatedHolds
        def registrationHoldType
        PersonRelatedHold.withSession {session ->
            personRelatedHolds = session.getNamedQuery('PersonRelatedHold.fetchByPidmAndDateCompare').setInteger('pidm', pidm).setDate('compareDate', compareDate).list()
                if (!registrationHoldsExist) {
                    personRelatedHolds.each {
                        registrationHoldType = HoldType.findWhere(registrationHoldIndicator: "Y", code: it.holdType.code)
                        if (registrationHoldType != null)
                        registrationHoldsExist = true
                    }
                }
        }
        return registrationHoldsExist
    }
    /*PROTECTED REGION END*/
}
