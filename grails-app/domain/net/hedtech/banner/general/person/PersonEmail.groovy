
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
/**
 Banner Automator Version: 1.24
 Generated: Thu Aug 04 14:06:15 EDT 2011 
 */
package net.hedtech.banner.general.person

import net.hedtech.banner.general.system.EmailType
import org.apache.log4j.Logger
import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Represents a person's email address
 */
@Entity
@Table(name = "GV_GOREMAL")
@NamedQueries(value=[
    @NamedQuery(name="PersonEmail.fetchByPidmAndStatusAndWebDisplayAndPreferredIndicator",
    query="""FROM PersonEmail a
    WHERE a.pidm = :pidm
    AND a.statusIndicator = :statusIndicator
    AND a.preferredIndicator = :preferredIndicator
    AND a.displayWebIndicator = :displayWebIndicator""")
])
class PersonEmail implements Serializable {
      static def log = Logger.getLogger( 'net.hedtech.banner.general.person.PersonEmail' )
	
	/**
	 * Surrogate ID for GOREMAL
	 */
	@Id
	@Column(name="GOREMAL_SURROGATE_ID")
	@SequenceGenerator(name ="GOREMAL_SEQ_GEN", allocationSize =1, sequenceName  ="GOREMAL_SURROGATE_ID_SEQUENCE")
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator ="GOREMAL_SEQ_GEN")
	Long id

	/**
	 * Optimistic lock token for GOREMAL
	 */
	@Version
	@Column(name = "GOREMAL_VERSION", nullable = false, precision = 19)
	Long version

	/**
	 * The pidm of the entity who owns this e-mail information.
	 */
	@Column(name = "GOREMAL_PIDM", nullable = false, unique = true, precision = 8)
	Integer pidm

	/**
	 * The e-mail address.
	 */
	@Column(name = "GOREMAL_EMAIL_ADDRESS", nullable = false, unique = true, length = 128)
	String emailAddress

	/**
	 * The status of the e-mail address: (A)ctive, (I)nactive
	 */
	@Column(name = "GOREMAL_STATUS_IND", nullable = false, length = 1)
	String statusIndicator

	/**
	 * This column indicates if the e-mail address is the preferred contact address.
	 */
	@Type(type = "yes_no")
	@Column(name = "GOREMAL_PREFERRED_IND", nullable = false, length = 1)
	Boolean preferredIndicator

	/**
	 * This is a free format comment regarding the e-mail information.
	 */
	@Column(name = "GOREMAL_COMMENT", length = 60)
	String commentData

	/**
	 * Indicate whether a e-mail address should appear on Web.
	 */
	@Type(type = "yes_no")
	@Column(name = "GOREMAL_DISP_WEB_IND", nullable = false, length = 1)
	Boolean displayWebIndicator

	/**
	 * The date on which the row was added or modified.
	 */
	@Column(name = "GOREMAL_ACTIVITY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastModified

	/**
	 * The user id when the row was added or modified.
	 */
	@Column(name = "GOREMAL_USER_ID", length = 30)
	String lastModifiedBy

	/**
	 * DATA ORIGIN: Source system that created or updated the row
	 */
	@Column(name = "GOREMAL_DATA_ORIGIN", length = 30)
	String dataOrigin

	
	/**
	 * Foreign Key : FKV_GOREMAL_INV_GTVEMAL_CODE
	 */
	@ManyToOne
	@JoinColumns([
		@JoinColumn(name="GOREMAL_EMAL_CODE", referencedColumnName="GTVEMAL_CODE")
		])
	EmailType emailType

	
	public String toString() {
		"""PersonEMail[
					id=$id, 
					version=$version, 
					pidm=$pidm, 
					emailAddress=$emailAddress, 
					statusIndicator=$statusIndicator, 
					preferredIndicator=$preferredIndicator, 
					commentData=$commentData, 
					displayWebIndicator=$displayWebIndicator, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin, 
					emailType=$emailType]"""
	}

	
	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof PersonEmail)) return false
	    PersonEmail that = (PersonEmail) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(pidm != that.pidm) return false
        if(emailAddress != that.emailAddress) return false
        if(statusIndicator != that.statusIndicator) return false
        if(preferredIndicator != that.preferredIndicator) return false
        if(commentData != that.commentData) return false
        if(displayWebIndicator != that.displayWebIndicator) return false
        if(lastModified != that.lastModified) return false
        if(lastModifiedBy != that.lastModifiedBy) return false
        if(dataOrigin != that.dataOrigin) return false
        if(emailType != that.emailType) return false      
        return true
    }

	
	int hashCode() {
		int result
	    result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (pidm != null ? pidm.hashCode() : 0)
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0)
        result = 31 * result + (statusIndicator != null ? statusIndicator.hashCode() : 0)
        result = 31 * result + (preferredIndicator != null ? preferredIndicator.hashCode() : 0)
        result = 31 * result + (commentData != null ? commentData.hashCode() : 0)
        result = 31 * result + (displayWebIndicator != null ? displayWebIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (emailType != null ? emailType.hashCode() : 0)
        return result
	}

	static constraints = {
		pidm(nullable:false, min: -99999999, max: 99999999)
		emailAddress(nullable:false, maxSize:128)
		statusIndicator(nullable:false, maxSize:1, inList:["I","A"])
		preferredIndicator(nullable:false)
		commentData(nullable:true, maxSize:60)
		displayWebIndicator(nullable:false)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
		emailType(nullable:false)
		/**
	     * Please put all the custom constraints in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(personemail_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }
    
    /*PROTECTED REGION ID(personemail_readonly_properties) ENABLED START*/
    //Read Only fields that should be protected against update
    public static readonlyProperties = [ 'pidm', 'emailAddress', 'emailType' ]
    /*PROTECTED REGION END*/        
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personemail_custom_attributes) ENABLED START*/
    
    /*PROTECTED REGION END*/
        
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personemail_custom_methods) ENABLED START*/

    public static List fetchByPidmAndStatusAndWebDisplayAndPreferredIndicator(Integer pidm, String statusIndicator,
                                                                              String displayWebIndicator, String preferredIndicator) {

        def email = PersonEmail.withSession {session ->
            session.getNamedQuery('PersonEmail.fetchByPidmAndStatusAndWebDisplayAndPreferredIndicator')
                   .setInteger('pidm', pidm)
                   .setString('statusIndicator', statusIndicator)
                   .setString('displayWebIndicator', displayWebIndicator)
                   .setString('preferredIndicator', preferredIndicator)
                   .list()
        }
        log.debug "Executing fetchByPidmAndStatusAndWebDisplayAndPreferredIndiator  with pidm = ${pidm} and status = ${statusIndicator} and displayWebIndicator = ${displayWebIndicator} and preferredIndicator = {$preferredIndicator}"
        log.debug "Fetched number of emails ${email.size()}"
        return email
    }

    /*PROTECTED REGION END*/
}
