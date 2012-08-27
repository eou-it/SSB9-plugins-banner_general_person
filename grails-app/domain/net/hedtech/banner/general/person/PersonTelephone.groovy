
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
 Banner Automator Version: 1.21
 Generated: Thu Jun 16 04:44:41 EDT 2011
 */
package net.hedtech.banner.general.person

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
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import org.hibernate.annotations.GenericGenerator
import net.hedtech.banner.general.system.TelephoneType
import javax.persistence.Temporal
import javax.persistence.TemporalType
import net.hedtech.banner.general.system.AddressType


/**
 * Telephone Table
 */
/*PROTECTED REGION ID(persontelephone_namedqueries) ENABLED START*/
//TODO: NamedQueries that needs to be ported:
@NamedQueries(value = [
        @NamedQuery(name = "PersonTelephone.fetchByPidmSequenceNoAndAddressType",
                    query="""FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND  addressSequenceNumber = :addressSequenceNumber
                             AND  addressType  = :addressType
                             AND  primaryIndicator IS NOT NULL
                    """),
                    @NamedQuery(name = "PersonTelephone.fetchByPidmTelephoneTypeAndAddressType",
                    query="""FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND addressType IS NOT NULL
                             AND  (telephoneType LIKE :filter
                             OR addressType LIKE :filter)
                             ORDER BY DECODE(a.statusIndicator, 'I',-2) DESC
                    """)
])

 /**
    * Where clause on this entity present in forms:
  * Order by clause on this entity present in forms:
*/
/*PROTECTED REGION END*/
@Entity
@Table(name = "SV_SPRTELE")
class PersonTelephone implements Serializable {

	/**
	 * Surrogate ID for SPRTELE
	 */
	@Id
	@Column(name="SPRTELE_SURROGATE_ID")
	@SequenceGenerator(name ="SPRTELE_SEQ_GEN", allocationSize =1, sequenceName  ="SPRTELE_SURROGATE_ID_SEQUENCE")
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator ="SPRTELE_SEQ_GEN")
	Long id

	/**
	 * Optimistic lock token for SPRTELE
	 */
	@Version
	@Column(name = "SPRTELE_VERSION", nullable = false, precision = 19)
	Long version

	/**
	 * Internal identification number.
	 */
	@Column(name = "SPRTELE_PIDM", nullable = false, unique = true, precision = 8)
	Integer pidm

	/**
	 * Unique sequence number for telephone numbers associated with PIDM.
	 */
	@Column(name = "SPRTELE_SEQNO", nullable = false, unique = true, precision = 3)
	Integer sequenceNumber

	/**
	 * Telephone number area code.
	 */
	@Column(name = "SPRTELE_PHONE_AREA", length = 6)
	String phoneArea

	/**
	 * Telephone number.
	 */
	@Column(name = "SPRTELE_PHONE_NUMBER", length = 12)
	String phoneNumber

	/**
	 * Telephone number extention.
	 */
	@Column(name = "SPRTELE_PHONE_EXT", length = 10)
	String phoneExtension

	/**
	 * STATUS: Status of telephone number, active or inactive.
	 */
	@Column(name = "SPRTELE_STATUS_IND", length = 1)
	String statusIndicator

	/**
	 * Address type sequence number associated with address type.
	 */
	@Column(name = "SPRTELE_ADDR_SEQNO", precision = 2)
	Integer addressSequenceNumber

	/**
	 * Primary indicator to denote primary telephone numbers based on telephone types.
	 */
	@Column(name = "SPRTELE_PRIMARY_IND", length = 1)
	String primaryIndicator

	/**
	 * Unlisted telephone number indicator.
	 */
	@Column(name = "SPRTELE_UNLIST_IND", length = 1)
	String unlistIndicator

	/**
	 * Comment relating to telephone number.
	 */
	@Column(name = "SPRTELE_COMMENT", length = 60)
	String commentData

	/**
	 * Free format International access code for telephone number including country and city code.
	 */
	@Column(name = "SPRTELE_INTL_ACCESS", length = 16)
	String internationalAccess

	/**
	 * COUNTRY CODE: Telephone code that designates the region and country.
	 */
	@Column(name = "SPRTELE_CTRY_CODE_PHONE", length = 4)
	String countryPhone

	/**
	 * Date of last activity for telephone record.
	 */
	@Column(name = "SPRTELE_ACTIVITY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastModified

	/**
	 * USER ID: User who inserted or last update the data
	 */
	@Column(name = "SPRTELE_USER_ID", length = 30)
	String lastModifiedBy

	/**
	 * DATA SOURCE: Source system that generated the data
	 */
	@Column(name = "SPRTELE_DATA_ORIGIN", length = 30)
	String dataOrigin


	/**
	 * Foreign Key : FK1_SPRTELE_INV_STVTELE_CODE
	 */
	@ManyToOne
	@JoinColumns([
		@JoinColumn(name="SPRTELE_TELE_CODE", referencedColumnName="STVTELE_CODE")
    ])
	TelephoneType telephoneType

	/**
	 * Foreign Key : FK1_SPRTELE_INV_STVATYP_CODE
	 */
	@ManyToOne
	@JoinColumns([
		@JoinColumn(name="SPRTELE_ATYP_CODE", referencedColumnName="STVATYP_CODE")
    ])
	AddressType addressType


	public String toString() {
		"""PersonTelephone[
					id=$id,
					version=$version,
					pidm=$pidm,
					sequenceNumber=$sequenceNumber,
					phoneArea=$phoneArea,
					phoneNumber=$phoneNumber,
					phoneExtension=$phoneExtension,
					statusIndicator=$statusIndicator,
					addressSequenceNumber=$addressSequenceNumber,
					primaryIndicator=$primaryIndicator,
					unlistIndicator=$unlistIndicator,
					commentData=$commentData,
					internationalAccess=$internationalAccess,
					countryPhone=$countryPhone,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin,
					telephoneType=$telephoneType,
					addressType=$addressType]"""
	}


	boolean equals(o) {
	    if (this.is(o)) return true
	    if (!(o instanceof PersonTelephone)) return false
	    PersonTelephone that = (PersonTelephone) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(pidm != that.pidm) return false
        if(sequenceNumber != that.sequenceNumber) return false
        if(phoneArea != that.phoneArea) return false
        if(phoneNumber != that.phoneNumber) return false
        if(phoneExtension != that.phoneExtension) return false
        if(statusIndicator != that.statusIndicator) return false
        if(addressSequenceNumber != that.addressSequenceNumber) return false
        if(primaryIndicator != that.primaryIndicator) return false
        if(unlistIndicator != that.unlistIndicator) return false
        if(commentData != that.commentData) return false
        if(internationalAccess != that.internationalAccess) return false
        if(countryPhone != that.countryPhone) return false
        if(lastModified != that.lastModified) return false
        if(lastModifiedBy != that.lastModifiedBy) return false
        if(dataOrigin != that.dataOrigin) return false
        if(telephoneType != that.telephoneType) return false
        if(addressType != that.addressType) return false
        return true
    }


	int hashCode() {
		int result
	    result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (pidm != null ? pidm.hashCode() : 0)
        result = 31 * result + (sequenceNumber != null ? sequenceNumber.hashCode() : 0)
        result = 31 * result + (phoneArea != null ? phoneArea.hashCode() : 0)
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0)
        result = 31 * result + (phoneExtension != null ? phoneExtension.hashCode() : 0)
        result = 31 * result + (statusIndicator != null ? statusIndicator.hashCode() : 0)
        result = 31 * result + (addressSequenceNumber != null ? addressSequenceNumber.hashCode() : 0)
        result = 31 * result + (primaryIndicator != null ? primaryIndicator.hashCode() : 0)
        result = 31 * result + (unlistIndicator != null ? unlistIndicator.hashCode() : 0)
        result = 31 * result + (commentData != null ? commentData.hashCode() : 0)
        result = 31 * result + (internationalAccess != null ? internationalAccess.hashCode() : 0)
        result = 31 * result + (countryPhone != null ? countryPhone.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (telephoneType != null ? telephoneType.hashCode() : 0)
        result = 31 * result + (addressType != null ? addressType.hashCode() : 0)
        return result
	}

	static constraints = {
		pidm(nullable:false, min: -99999999, max: 99999999)
		sequenceNumber(nullable:false, min: -999, max: 999)
		phoneArea(nullable:true, maxSize:6)
		phoneNumber(nullable:true, maxSize:12)
		phoneExtension(nullable:true, maxSize:10)
		statusIndicator(nullable:true, maxSize:1, inList:["I"])
		addressSequenceNumber(nullable:true, min: -99, max: 99)
		primaryIndicator(nullable:true, maxSize:1, inList:["Y"])
		unlistIndicator(nullable:true, maxSize:1, inList:["Y"])
		commentData(nullable:true, maxSize:60)
		internationalAccess(nullable:true, maxSize:16)
		countryPhone(nullable:true, maxSize:4)
		lastModified(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
		telephoneType(nullable:false)
		addressType(nullable:true)
		/**
	     * Please put all the custom constraints in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(persontelephone_custom_constraints) ENABLED START*/

	    /*PROTECTED REGION END*/
    }

    /*PROTECTED REGION ID(persontelephone_readonly_properties) ENABLED START*/
    //Read Only fields that should be protected against update
    public static readonlyProperties = [ 'pidm', 'sequenceNumber', 'telephoneType' ]
    /*PROTECTED REGION END*/
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(persontelephone_custom_attributes) ENABLED START*/

    /*PROTECTED REGION END*/

    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(persontelephone_custom_methods) ENABLED START*/

    public static PersonTelephone fetchByPidmSequenceNoAndAddressType(Integer pidm, Integer addressSequenceNumber, AddressType addressType) {
        PersonTelephone.withSession { session ->
            def personTelephone = session.getNamedQuery('PersonTelephone.fetchByPidmSequenceNoAndAddressType')
                    .setInteger('pidm', pidm)
                    .setInteger('addressSequenceNumber', addressSequenceNumber)
                    .setString('addressType', addressType.code).list()[0]
            return personTelephone
        }
    }

    static def fetchByPidmTelephoneTypeAndAddressType(map) {
        if (map && map.pidm) {
            PersonTelephone.withSession { session ->
            return [list: session.getNamedQuery('PersonTelephone.fetchByPidmTelephoneTypeAndAddressType')
                .setInteger('pidm', map.pidm).setString('filter','%').list()]
            }
        } else {
            return null
        }
    }

    static def fetchByPidmTelephoneTypeAndAddressType(String filter, map) {
        if (map && map.pidm) {
            PersonTelephone.withSession { session ->
            return [list: session.getNamedQuery('PersonTelephone.fetchByPidmTelephoneTypeAndAddressType')
                .setInteger('pidm', map?.pidm).setString('filter', "%${filter?.toUpperCase()}%").list()]
            }
        } else {
            return null
        }
    }

    /*PROTECTED REGION END*/
}