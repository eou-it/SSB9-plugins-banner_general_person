
/*******************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.

 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 *******************************************************************************/
/**
 Banner Automator Version: 1.29
 Generated: Mon Feb 13 13:50:49 EST 2012 
 */
package com.sungardhe.banner.general.person

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Transient
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.GenerationType
import javax.persistence.SequenceGenerator
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import org.hibernate.annotations.Type

import com.sungardhe.banner.general.system.Term


/**
 * Student Alternate PIN Table
 */
/*PROTECTED REGION ID(personstudentalternatepin_namedqueries) ENABLED START*/
/*PROTECTED REGION END*/
@Entity
@Table(name = "SPRAPIN")
@NamedQueries(value=[
    @NamedQuery(name="PersonStudentAlternatePin.fetchByPidmAndTerm",
    query="""FROM PersonStudentAlternatePin a
    WHERE a.pidm = :pidm
      AND a.term >= :term
    ORDER BY a.term, a.processName asc""")
])
class PersonStudentAlternatePin implements Serializable {
	
	/**
	 * Surrogate ID for SPRAPIN
	 */
	@Id
	@Column(name="SPRAPIN_SURROGATE_ID")
	@SequenceGenerator(name ="SPRAPIN_SEQ_GEN", allocationSize =1, sequenceName  ="SPRAPIN_SURROGATE_ID_SEQUENCE")
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator ="SPRAPIN_SEQ_GEN")
	Long id

	/**
	 * Optimistic lock token for SPRAPIN
	 */
	@Version
	@Column(name = "SPRAPIN_VERSION", nullable = false, precision = 19)
	Long version

	/**
	 * PIDM which will use this alternate PIN.
	 */
	@Column(name = "SPRAPIN_PIDM", nullable = false, precision = 8)
	Integer pidm

	/**
	 * Process name which will utilize this alternate PIN.
	 */
	@Column(name = "SPRAPIN_PROCESS_NAME", nullable = false, length = 15)
	String processName

	/**
	 * The assigned alternate PIN which will be used for the this individual for the named process.
	 */
	@Column(name = "SPRAPIN_PIN", nullable = false, length = 6)
	String pin

	/**
	 * Date of this record's creation or last update.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SPRAPIN_ACTIVITY_DATE")
	Date lastModified

	/**
	 * Last modified by column for SPRAPIN
	 */
	@Column(name = "SPRAPIN_USER_ID", length = 30)
	String lastModifiedBy

	/**
	 * Data origin column for SPRAPIN
	 */
	@Column(name = "SPRAPIN_DATA_ORIGIN", length = 30)
	String dataOrigin

	
	/**
	 * Foreign Key : FKV_SPRAPIN_INV_STVTERM_CODE
	 */
	@ManyToOne
	@JoinColumns([
		@JoinColumn(name="SPRAPIN_TERM_CODE", referencedColumnName="STVTERM_CODE")
		])
	Term term

    public static readonlyProperties = ['processName', 'pidm']

	public String toString() {
		"""PersonStudentAlternatePin[
					id=$id, 
					version=$version, 
					pidm=$pidm, 
					processName=$processName, 
					pin=$pin, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin, 
					term=$term]"""
	}

	
	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof PersonStudentAlternatePin)) return false
	    PersonStudentAlternatePin that = (PersonStudentAlternatePin) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(pidm != that.pidm) return false
        if(processName != that.processName) return false
        if(pin != that.pin) return false
        if(lastModified != that.lastModified) return false
        if(lastModifiedBy != that.lastModifiedBy) return false
        if(dataOrigin != that.dataOrigin) return false
        if(term != that.term) return false      
        return true
    }

	
	int hashCode() {
		int result
	    result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (pidm != null ? pidm.hashCode() : 0)
        result = 31 * result + (processName != null ? processName.hashCode() : 0)
        result = 31 * result + (pin != null ? pin.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (term != null ? term.hashCode() : 0)
        return result
	}

	static constraints = {
		pidm(nullable:false, min: -99999999, max: 99999999)
		processName(nullable:false, maxSize:15)
		pin(nullable:false, maxSize:6)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
		term(nullable:false)
		/**
	     * Please put all the custom constraints in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(personstudentalternatepin_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }
    
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personstudentalternatepin_custom_attributes) ENABLED START*/
    
    /*PROTECTED REGION END*/
        
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personstudentalternatepin_custom_methods) ENABLED START*/
    public static List fetchByPidmAndTerm(Integer pidm, String term) {
        def personStudentAlternatePin = []
        if (pidm && term) {
            personStudentAlternatePin = PersonStudentAlternatePin.withSession { session ->
                session.getNamedQuery('PersonStudentAlternatePin.fetchByPidmAndTerm')
                .setInteger('pidm', pidm)
                .setString('term', term)
                .list()
            }
        }
        return personStudentAlternatePin
    }
    
    /*PROTECTED REGION END*/
}
