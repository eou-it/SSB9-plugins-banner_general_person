/** *****************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
/**
 Banner Automator Version: 1.21
 Generated: Thu Jun 16 04:44:41 EDT 2011
 */
package com.sungardhe.banner.general.person

import javax.persistence.*
import org.hibernate.annotations.GenericGenerator
import com.sungardhe.banner.general.system.AddressType
import com.sungardhe.banner.general.system.State
import com.sungardhe.banner.general.system.County
import com.sungardhe.banner.general.system.Nation
import com.sungardhe.banner.general.system.AddressSource
import com.sungardhe.banner.service.DatabaseModifiesState

/**
 * Address Repeating Table
 */
/*PROTECTED REGION ID(personaddress_namedqueries) ENABLED START*/
//TODO: NamedQueries that needs to be ported:
@NamedQueries(value = [
@NamedQuery(name = "PersonAddress.fetchActiveAddressByPidmAndAddressType",
query = """ FROM PersonAddress a
                            WHERE  a.pidm = :pidm
                            AND  a.addressType = :addressType
                            AND NVL(a.statusIndicator,'Y') <> 'I'
                            AND  SYSDATE BETWEEN
                            NVL(SPRADDR_FROM_DATE,sysdate-1) AND
                            NVL(SPRADDR_TO_DATE,sysdate+1)
                """),
@NamedQuery(name = "PersonAddress.fetchAllByPidmAddressTypeAndStateOrderByStatusIndAddressTypeToDateFromDateAndSeqNo",
query = """ FROM PersonAddress a
                            WHERE a.pidm = :pidm
                            AND ( a.addressType LIKE :filter
                            OR a.state LIKE :filter )
                            ORDER BY (DECODE (
                                        a.statusIndicator,
                                        'I', -2,
                                        DECODE (
                                           TO_NUMBER (TO_CHAR (NVL (a.toDate, SYSDATE + 1), 'J'))
                                           - TO_NUMBER (TO_CHAR (SYSDATE, 'J')),
                                           0, 1,
                                           (TO_NUMBER (TO_CHAR (NVL (a.toDate, SYSDATE + 1), 'J'))
                                            - TO_NUMBER (TO_CHAR (SYSDATE, 'J'))))
                                        / DECODE (
                                             ABS (
                                                TO_NUMBER (
                                                   TO_CHAR (NVL (a.toDate, SYSDATE + 1), 'J'))
                                                - TO_NUMBER (TO_CHAR (SYSDATE, 'J'))),
                                             0, 1,
                                             (ABS (
                                                 TO_NUMBER (
                                                    TO_CHAR (NVL (a.toDate, SYSDATE + 1), 'J'))
                                                 - TO_NUMBER (TO_CHAR (SYSDATE, 'J'))))))
                                     *
                                     (DECODE (
                                           TO_NUMBER (TO_CHAR (SYSDATE, 'J'))
                                           - TO_NUMBER (
                                                TO_CHAR (NVL (a.fromDate, SYSDATE - 1), 'J')),
                                           0, 1,
                                           (TO_NUMBER (TO_CHAR (SYSDATE, 'J'))
                                            - TO_NUMBER (
                                                 TO_CHAR (NVL (a.fromDate, SYSDATE - 1), 'J'))))
                                        / DECODE (
                                             ABS (
                                                TO_NUMBER (TO_CHAR (SYSDATE, 'J'))
                                                - TO_NUMBER (
                                                     TO_CHAR (NVL (a.fromDate, SYSDATE - 1), 'J'))),
                                             0, 1,
                                             (ABS (
                                                 TO_NUMBER (TO_CHAR (SYSDATE, 'J'))
                                                 - TO_NUMBER (
                                                      TO_CHAR (NVL (a.fromDate, SYSDATE - 1), 'J'))))))) DESC,
                                     a.addressType,
                                     a.toDate DESC,
                                     a.fromDate DESC,
                                     a.sequenceNumber
                """)

])
/**
 * Where clause on this entity present in forms:
 * Order by clause on this entity present in forms:
 */
/*PROTECTED REGION END*/
@Entity
@Table(name = "SV_SPRADDR")
@DatabaseModifiesState
class PersonAddress implements Serializable {

    /**
     * Surrogate ID for SPRADDR
     */
    @Id
    @Column(name = "SPRADDR_SURROGATE_ID")
    @SequenceGenerator(name = "SPRADDR_SEQ_GEN", allocationSize = 1, sequenceName = "SPRADDR_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPRADDR_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for SPRADDR
     */
    @Version
    @Column(name = "SPRADDR_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Internal identification number of person.
     */
    @Column(name = "SPRADDR_PIDM", nullable = false, precision = 8)
    Integer pidm

    /**
     * This field assigns an internal sequence number to each address type associated with person. This field does not display on screen.
     */
    @Column(name = "SPRADDR_SEQNO", nullable = false, precision = 2)
    Integer sequenceNumber

    /**
     * This field maintains the effective start date of address associated with person.
     */
    @Column(name = "SPRADDR_FROM_DATE")
    Date fromDate

    /**
     * This field maintains the effective end date of address associated with person.
     */
    @Column(name = "SPRADDR_TO_DATE")
    Date toDate

    /**
     * This field maintains the first line of the address associated with person.
     */
    @Column(name = "SPRADDR_STREET_LINE1", length = 75)
    String streetLine1

    /**
     * This field maintains the second line of the address associated with person.
     */
    @Column(name = "SPRADDR_STREET_LINE2", length = 75)
    String streetLine2

    /**
     * This field maintains the third line of the address associated with person.
     */
    @Column(name = "SPRADDR_STREET_LINE3", length = 75)
    String streetLine3

    /**
     * This field maintains the city associated with the address of person.
     */
    @Column(name = "SPRADDR_CITY", nullable = false, length = 50)
    String city

    /**
     * This field maintains the zip code associated with the address of person.
     */
    @Column(name = "SPRADDR_ZIP", length = 30)
    String zip

    /**
     * This field maintains the area code of the phone number associated with address of person.
     */
    @Column(name = "SPRADDR_PHONE_AREA", length = 6)
    String phoneArea

    /**
     * This field maintains the phone number associated with address of person.
     */
    @Column(name = "SPRADDR_PHONE_NUMBER", length = 12)
    String phoneNumber

    /**
     * This field maintains the extension of the phone number associated with address of person.
     */
    @Column(name = "SPRADDR_PHONE_EXT", length = 10)
    String phoneExtension

    /**
     * This field identifies if address information is inactive. Valid value is I - Inactive.
     */
    @Column(name = "SPRADDR_STATUS_IND", length = 1)
    String statusIndicator

    /**
     * The Id for the User who create or update the record.
     */
    @Column(name = "SPRADDR_USER", length = 30)
    String userData

    /**
     * DELIVERY POINT: This field is used to designate the delivery point for mail as established by the Postal Service.
     */
    @Column(name = "SPRADDR_DELIVERY_POINT", precision = 2)
    Integer deliveryPoint

    /**
     * CORRECTION DIGIT: The Correction Digit field is defined by the Postal Service and is used to determine the digits used for delivery related to the ZIP code.
     */
    @Column(name = "SPRADDR_CORRECTION_DIGIT", precision = 1)
    Integer correctionDigit

    /**
     * CARRIER ROUTE: The addresses to which a carrier delivers mail. In common usage, carrier route includes city routes, rural routes, highway contract routes, post office box sections, and general delivery u
     nits.
     */
    @Column(name = "SPRADDR_CARRIER_ROUTE", length = 4)
    String carrierRoute

    /**
     * GST TAX IDENTIFICATION NUMBER: Goods and Services Tax Identification of vendor at this address
     */
    @Column(name = "SPRADDR_GST_TAX_ID", length = 15)
    String goodsAndServiceTaxTaxId

    /**
     * Reviewed Indicator, Y indicates the address has been reviewed
     */
    @Column(name = "SPRADDR_REVIEWED_IND", length = 1)
    String reviewedIndicator

    /**
     * Reviewed User, indicates the user who reviewed the address.
     */
    @Column(name = "SPRADDR_REVIEWED_USER", length = 30)
    String reviewedUser

    /**
     * COUNTRY CODE: Telephone code that designates the region and country.
     */
    @Column(name = "SPRADDR_CTRY_CODE_PHONE", length = 4)
    String countryPhone

    /**
     * HOUSE NUMBER: Building or lot number on a street or in an area.
     */
    @Column(name = "SPRADDR_HOUSE_NUMBER", length = 10)
    String houseNumber

    /**
     * STREET LINE 4: This field maintains the fourth line of the address associated with person.
     */
    @Column(name = "SPRADDR_STREET_LINE4", length = 75)
    String streetLine4

    /**
     * This field defines the most current date a record is added or changed.
     */
    @Column(name = "SPRADDR_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for SPRADDR
     */
    @Column(name = "SPRADDR_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * DATA SOURCE: Source system that generated the data
     */
    @Column(name = "SPRADDR_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * Foreign Key : FK1_SPRADDR_INV_STVATYP_CODE
     */
    @ManyToOne
    @JoinColumns([
        @JoinColumn(name = "SPRADDR_ATYP_CODE", referencedColumnName = "STVATYP_CODE")
    ])
    AddressType addressType

    /**
     * Foreign Key : FK1_SPRADDR_INV_STVSTAT_CODE
     */
    @ManyToOne
    @JoinColumns([
        @JoinColumn(name = "SPRADDR_STAT_CODE", referencedColumnName = "STVSTAT_CODE")
    ])
    State state

    /**
     * Foreign Key : FK1_SPRADDR_INV_STVCNTY_CODE
     */
    @ManyToOne
    @JoinColumns([
        @JoinColumn(name = "SPRADDR_CNTY_CODE", referencedColumnName = "STVCNTY_CODE")
    ])
    County county

    /**
     * Foreign Key : FK1_SPRADDR_INV_STVNATN_CODE
     */
    @ManyToOne
    @JoinColumns([
        @JoinColumn(name = "SPRADDR_NATN_CODE", referencedColumnName = "STVNATN_CODE")
    ])
    Nation nation

    /**
     * Foreign Key : FK1_SPRADDR_INV_STVASRC_CODE
     */
    @ManyToOne
    @JoinColumns([
        @JoinColumn(name = "SPRADDR_ASRC_CODE", referencedColumnName = "STVASRC_CODE")
    ])
    AddressSource addressSource


    public String toString() {
        """PersonAddress[
					id=$id,
					version=$version,
					pidm=$pidm,
					sequenceNumber=$sequenceNumber,
					fromDate=$fromDate,
					toDate=$toDate,
					streetLine1=$streetLine1,
					streetLine2=$streetLine2,
					streetLine3=$streetLine3,
					city=$city,
					zip=$zip,
					phoneArea=$phoneArea,
					phoneNumber=$phoneNumber,
					phoneExtension=$phoneExtension,
					statusIndicator=$statusIndicator,
					userData=$userData,
					deliveryPoint=$deliveryPoint,
					correctionDigit=$correctionDigit,
					carrierRoute=$carrierRoute,
					goodsAndServiceTaxTaxId=$goodsAndServiceTaxTaxId,
					reviewedIndicator=$reviewedIndicator,
					reviewedUser=$reviewedUser,
					countryPhone=$countryPhone,
					houseNumber=$houseNumber,
					streetLine4=$streetLine4,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin,
					addressType=$addressType,
					state=$state,
					county=$county,
					nation=$nation,
					addressSource=$addressSource]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof PersonAddress)) return false
        PersonAddress that = (PersonAddress) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (pidm != that.pidm) return false
        if (sequenceNumber != that.sequenceNumber) return false
        if (fromDate != that.fromDate) return false
        if (toDate != that.toDate) return false
        if (streetLine1 != that.streetLine1) return false
        if (streetLine2 != that.streetLine2) return false
        if (streetLine3 != that.streetLine3) return false
        if (city != that.city) return false
        if (zip != that.zip) return false
        if (phoneArea != that.phoneArea) return false
        if (phoneNumber != that.phoneNumber) return false
        if (phoneExtension != that.phoneExtension) return false
        if (statusIndicator != that.statusIndicator) return false
        if (userData != that.userData) return false
        if (deliveryPoint != that.deliveryPoint) return false
        if (correctionDigit != that.correctionDigit) return false
        if (carrierRoute != that.carrierRoute) return false
        if (goodsAndServiceTaxTaxId != that.goodsAndServiceTaxTaxId) return false
        if (reviewedIndicator != that.reviewedIndicator) return false
        if (reviewedUser != that.reviewedUser) return false
        if (countryPhone != that.countryPhone) return false
        if (houseNumber != that.houseNumber) return false
        if (streetLine4 != that.streetLine4) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        if (addressType != that.addressType) return false
        if (state != that.state) return false
        if (county != that.county) return false
        if (nation != that.nation) return false
        if (addressSource != that.addressSource) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (pidm != null ? pidm.hashCode() : 0)
        result = 31 * result + (sequenceNumber != null ? sequenceNumber.hashCode() : 0)
        result = 31 * result + (fromDate != null ? fromDate.hashCode() : 0)
        result = 31 * result + (toDate != null ? toDate.hashCode() : 0)
        result = 31 * result + (streetLine1 != null ? streetLine1.hashCode() : 0)
        result = 31 * result + (streetLine2 != null ? streetLine2.hashCode() : 0)
        result = 31 * result + (streetLine3 != null ? streetLine3.hashCode() : 0)
        result = 31 * result + (city != null ? city.hashCode() : 0)
        result = 31 * result + (zip != null ? zip.hashCode() : 0)
        result = 31 * result + (phoneArea != null ? phoneArea.hashCode() : 0)
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0)
        result = 31 * result + (phoneExtension != null ? phoneExtension.hashCode() : 0)
        result = 31 * result + (statusIndicator != null ? statusIndicator.hashCode() : 0)
        result = 31 * result + (userData != null ? userData.hashCode() : 0)
        result = 31 * result + (deliveryPoint != null ? deliveryPoint.hashCode() : 0)
        result = 31 * result + (correctionDigit != null ? correctionDigit.hashCode() : 0)
        result = 31 * result + (carrierRoute != null ? carrierRoute.hashCode() : 0)
        result = 31 * result + (goodsAndServiceTaxTaxId != null ? goodsAndServiceTaxTaxId.hashCode() : 0)
        result = 31 * result + (reviewedIndicator != null ? reviewedIndicator.hashCode() : 0)
        result = 31 * result + (reviewedUser != null ? reviewedUser.hashCode() : 0)
        result = 31 * result + (countryPhone != null ? countryPhone.hashCode() : 0)
        result = 31 * result + (houseNumber != null ? houseNumber.hashCode() : 0)
        result = 31 * result + (streetLine4 != null ? streetLine4.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (addressType != null ? addressType.hashCode() : 0)
        result = 31 * result + (state != null ? state.hashCode() : 0)
        result = 31 * result + (county != null ? county.hashCode() : 0)
        result = 31 * result + (nation != null ? nation.hashCode() : 0)
        result = 31 * result + (addressSource != null ? addressSource.hashCode() : 0)
        return result
    }


    static constraints = {
        pidm(nullable: false, min: -99999999, max: 99999999)
        sequenceNumber(nullable: false, min: -99, max: 99)
        fromDate(nullable: true)
        toDate(nullable: true)
        streetLine1(nullable: true, maxSize: 75)
        streetLine2(nullable: true, maxSize: 75)
        streetLine3(nullable: true, maxSize: 75)
        city(nullable: false, maxSize: 50)
        zip(nullable: true, maxSize: 30)
        phoneArea(nullable: true, maxSize: 6)
        phoneNumber(nullable: true, maxSize: 12)
        phoneExtension(nullable: true, maxSize: 10)
        statusIndicator(nullable: true, maxSize: 1, inList: ["I"])
        userData(nullable: true, maxSize: 30)
        deliveryPoint(nullable: true, min: -99, max: 99)
        correctionDigit(nullable: true, min: -9, max: 9)
        carrierRoute(nullable: true, maxSize: 4)
        reviewedIndicator(nullable: true, maxSize: 1, inList: ["Y", "N"])
        reviewedUser(nullable: true, maxSize: 30)
        countryPhone(nullable: true, maxSize: 4)
        houseNumber(nullable: true, maxSize: 10)
        streetLine4(nullable: true, maxSize: 75)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        addressType(nullable: false)
        state(nullable: true)
        county(nullable: true)
        nation(nullable: true)
        addressSource(nullable: true)
        /**
         * Please put all the custom constraints in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(personaddress_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }

    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personaddress_custom_attributes) ENABLED START*/

    /*PROTECTED REGION END*/

    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personaddress_custom_methods) ENABLED START*/


    static def fetchActiveAddressByPidmAndAddressType(Integer pidm, AddressType addressType) {
        if (pidm && addressType) {
            PersonAddress.withSession { session ->
                List personAddressList = session.getNamedQuery('PersonAddress.fetchActiveAddressByPidmAndAddressType').setInteger('pidm', pidm).setString('addressType', addressType.code).list()
                return personAddressList[0]
            }
        } else {
            return null
        }
    }


    static def fetchAllByPidmAddressTypeAndStateOrderByStatusIndAddressTypeToDateFromDateAndSeqNo(map) {
        if (map && map.pidm) {
            PersonAddress.withSession { session ->
                return [list: session.getNamedQuery('PersonAddress.fetchAllByPidmAddressTypeAndStateOrderByStatusIndAddressTypeToDateFromDateAndSeqNo').setInteger('pidm', map?.pidm).setString('filter', '%').list()]
            }
        } else {
            return null
        }
    }


    static def fetchAllByPidmAddressTypeAndStateOrderByStatusIndAddressTypeToDateFromDateAndSeqNo(String filter, map) {
        if (map && map.pidm) {
            PersonAddress.withSession { session ->
                return [list: session.getNamedQuery('PersonAddress.fetchAllByPidmAddressTypeAndStateOrderByStatusIndAddressTypeToDateFromDateAndSeqNo').setInteger('pidm', map?.pidm).setString('filter', "%${filter?.toUpperCase()}%").list()]
            }
        } else {
            return null
        }
    }

    /*PROTECTED REGION END*/
}
