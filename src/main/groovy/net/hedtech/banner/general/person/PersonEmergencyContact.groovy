/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.general.system.AddressType
import net.hedtech.banner.general.system.Nation
import net.hedtech.banner.general.system.Relationship
import net.hedtech.banner.general.system.State
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.service.DatabaseModifiesState

import javax.persistence.*

/**
 * Emergency Contact Repeating Table
 */
@NamedQueries(value = [
@NamedQuery(name = "PersonEmergencyContact.fetchByPidmOrderByPriority",
query = """ FROM PersonEmergencyContact a
                            WHERE  a.pidm = :pidm
                            ORDER BY a.priority""")
])
/**
 * Where clause on this entity present in forms:
 * Order by clause on this entity present in forms:
 */
@Entity
@Table(name = "SV_SPREMRG")
@DatabaseModifiesState
class PersonEmergencyContact implements Serializable {

    /**
     * Surrogate ID for SPREMRG
     */
    @Id
    @Column(name = "SPREMRG_SURROGATE_ID")
    @SequenceGenerator(name = "SPREMRG_SEQ_GEN", allocationSize = 1, sequenceName = "SPREMRG_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPREMRG_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for SPREMRG
     */
    @Version
    @Column(name = "SPREMRG_VERSION")
    Long version

    /**
     * Internal identification number of person.
     */
    @Column(name = "SPREMRG_PIDM")
    Integer pidm

    /**
     * Priority indicator associated with emergency address of person record.
     */
    @Column(name = "SPREMRG_PRIORITY")
    String priority

    /**
     * Last name of person associated with emergency address information.
     */
    @Column(name = "SPREMRG_LAST_NAME")
    String lastName

    /**
     * First name of person associated with emergency address information.
     */
    @Column(name = "SPREMRG_FIRST_NAME")
    String firstName

    /**
     * Middle name of person associated with emergency address information.
     */
    @Column(name = "SPREMRG_MI")
    String middleInitial

    /**
     * Line one of emergency adddress.
     */
    @Column(name = "SPREMRG_STREET_LINE1")
    String streetLine1

    /**
     * Line two of emergency address.
     */
    @Column(name = "SPREMRG_STREET_LINE2")
    String streetLine2

    /**
     * Line three of emergency address.
     */
    @Column(name = "SPREMRG_STREET_LINE3")
    String streetLine3

    /**
     * City associated with emergency address.
     */
    @Column(name = "SPREMRG_CITY")
    String city

    /**
     * Zip code associated with emergency address.
     */
    @Column(name = "SPREMRG_ZIP")
    String zip

    /**
     * Area code of phone number associated with emergency address.
     */
    @Column(name = "SPREMRG_PHONE_AREA")
    String phoneArea

    /**
     * Phone number associated with emergency address.
     */
    @Column(name = "SPREMRG_PHONE_NUMBER")
    String phoneNumber

    /**
     * Extension of phone number associated with emergency address.
     */
    @Column(name = "SPREMRG_PHONE_EXT")
    String phoneExtension

    /**
     * SURNAME PREFIX: Name tag preceding the last name or surname.  (Van, Von, Mac, etc.)
     */
    @Column(name = "SPREMRG_SURNAME_PREFIX")
    String surnamePrefix

    /**
     * COUNTRY CODE: Telephone code that designates the region and country.
     */
    @Column(name = "SPREMRG_CTRY_CODE_PHONE")
    String countryPhone

    /**
     * HOUSE NUMBER: Building or lot number on a street or in an area.
     */
    @Column(name = "SPREMRG_HOUSE_NUMBER")
    String houseNumber

    /**
     * STREET LINE 4: Line four of emergency address.
     */
    @Column(name = "SPREMRG_STREET_LINE4")
    String streetLine4

    /**
     * Most current date that the record was created or changed. This field does not   display on screen.
     */
    @Column(name = "SPREMRG_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * USER ID: User who inserted or last update the data
     */
    @Column(name = "SPREMRG_USER_ID")
    String lastModifiedBy

    /**
     * DATA ORIGIN: Source system that created or updated the row
     */
    @Column(name = "SPREMRG_DATA_ORIGIN")
    String dataOrigin

    /**
     * Foreign Key : FK1_SPREMRG_INV_STVSTAT_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPREMRG_STAT_CODE", referencedColumnName = "STVSTAT_CODE")
    ])
    State state

    /**
     * Foreign Key : FK1_SPREMRG_INV_STVNATN_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPREMRG_NATN_CODE", referencedColumnName = "STVNATN_CODE")
    ])
    Nation nation

    /**
     * Foreign Key : FK1_SPREMRG_INV_STVRELT_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPREMRG_RELT_CODE", referencedColumnName = "STVRELT_CODE")
    ])
    Relationship relationship

    /**
     * Foreign Key : FK1_SPREMRG_INV_STVATYP_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPREMRG_ATYP_CODE", referencedColumnName = "STVATYP_CODE")
    ])
    AddressType addressType


    public String toString() {
        """PersonEmergencyContact[
					id=$id, 
					version=$version, 
					pidm=$pidm, 
					priority=$priority, 
					lastName=$lastName, 
					firstName=$firstName, 
					middleInitial=$middleInitial, 
					streetLine1=$streetLine1, 
					streetLine2=$streetLine2, 
					streetLine3=$streetLine3, 
					city=$city, 
					zip=$zip, 
					phoneArea=$phoneArea, 
					phoneNumber=$phoneNumber, 
					phoneExtension=$phoneExtension, 
					surnamePrefix=$surnamePrefix, 
					countryPhone=$countryPhone, 
					houseNumber=$houseNumber, 
					streetLine4=$streetLine4, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin, 
					state=$state, 
					nation=$nation, 
					relationship=$relationship, 
					addressType=$addressType]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof PersonEmergencyContact)) return false
        PersonEmergencyContact that = (PersonEmergencyContact) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (pidm != that.pidm) return false
        if (priority != that.priority) return false
        if (lastName != that.lastName) return false
        if (firstName != that.firstName) return false
        if (middleInitial != that.middleInitial) return false
        if (streetLine1 != that.streetLine1) return false
        if (streetLine2 != that.streetLine2) return false
        if (streetLine3 != that.streetLine3) return false
        if (city != that.city) return false
        if (zip != that.zip) return false
        if (phoneArea != that.phoneArea) return false
        if (phoneNumber != that.phoneNumber) return false
        if (phoneExtension != that.phoneExtension) return false
        if (surnamePrefix != that.surnamePrefix) return false
        if (countryPhone != that.countryPhone) return false
        if (houseNumber != that.houseNumber) return false
        if (streetLine4 != that.streetLine4) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        if (state != that.state) return false
        if (nation != that.nation) return false
        if (relationship != that.relationship) return false
        if (addressType != that.addressType) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (pidm != null ? pidm.hashCode() : 0)
        result = 31 * result + (priority != null ? priority.hashCode() : 0)
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0)
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0)
        result = 31 * result + (middleInitial != null ? middleInitial.hashCode() : 0)
        result = 31 * result + (streetLine1 != null ? streetLine1.hashCode() : 0)
        result = 31 * result + (streetLine2 != null ? streetLine2.hashCode() : 0)
        result = 31 * result + (streetLine3 != null ? streetLine3.hashCode() : 0)
        result = 31 * result + (city != null ? city.hashCode() : 0)
        result = 31 * result + (zip != null ? zip.hashCode() : 0)
        result = 31 * result + (phoneArea != null ? phoneArea.hashCode() : 0)
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0)
        result = 31 * result + (phoneExtension != null ? phoneExtension.hashCode() : 0)
        result = 31 * result + (surnamePrefix != null ? surnamePrefix.hashCode() : 0)
        result = 31 * result + (countryPhone != null ? countryPhone.hashCode() : 0)
        result = 31 * result + (houseNumber != null ? houseNumber.hashCode() : 0)
        result = 31 * result + (streetLine4 != null ? streetLine4.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (state != null ? state.hashCode() : 0)
        result = 31 * result + (nation != null ? nation.hashCode() : 0)
        result = 31 * result + (relationship != null ? relationship.hashCode() : 0)
        result = 31 * result + (addressType != null ? addressType.hashCode() : 0)
        return result
    }


    static constraints = {
        pidm(nullable: false, min: -99999999, max: 99999999)
        priority(nullable: false, maxSize: 1)
        lastName(nullable: false, maxSize: 60)
        firstName(nullable: false, maxSize: 60)
        middleInitial(nullable: true, maxSize: 60)
        streetLine1(nullable: true, maxSize: 75)
        streetLine2(nullable: true, maxSize: 75)
        streetLine3(nullable: true, maxSize: 75)
        city(nullable: true, maxSize: 50)
        zip(nullable: true, maxSize: 30)
        phoneArea(nullable: true, maxSize: 6)
        phoneNumber(nullable: true, maxSize: 12)
        phoneExtension(nullable: true, maxSize: 10)
        surnamePrefix(nullable: true, maxSize: 60)
        countryPhone(nullable: true, maxSize: 4)
        houseNumber(nullable: true, maxSize: 10)
        streetLine4(nullable: true, maxSize: 75)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        state(nullable: true)
        nation(nullable: true)
        relationship(nullable: true)
        addressType(nullable: true)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['pidm', 'priority']


    /**
     * Finder for advanced filtering and sorting
     * @param filterData , pagingAndSortParams
     * @return filtered and sorted data
     */
    def static countAll(filterData) {
        finderByAll().count(filterData)
    }


    def static fetchSearch(filterData, pagingAndSortParams) {
        def personEmergencyContacts = finderByAll().find(filterData, pagingAndSortParams)
        return personEmergencyContacts
    }


    def private static finderByAll = {
        def query = """FROM PersonEmergencyContact a WHERE a.pidm = :pidm"""
        return new DynamicFinder(PersonEmergencyContact.class, query, "a")
    }


    static List fetchByPidmOrderByPriority(Integer pidm) {
        if (pidm) {
            PersonEmergencyContact.withSession { session ->
                List personEmergencyContactList = session.getNamedQuery('PersonEmergencyContact.fetchByPidmOrderByPriority').setInteger('pidm', pidm).list()
                return personEmergencyContactList
            }
        } else {
            return null
        }
    }
}
