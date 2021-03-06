/*********************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import grails.async.Promise
import net.hedtech.banner.general.system.AddressType
import net.hedtech.banner.general.system.TelephoneType
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.service.DatabaseModifiesState

import javax.persistence.*

/**
 * Telephone Table
 */
@Entity
@Table(name = "SV_SPRTELE")
@NamedQueries(value = [
        @NamedQuery(name = "PersonTelephone.fetchByPidmTelephoneTypeAndTelephoneSequence",
                query = """FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND  telephoneType.code = :telephoneType
                             AND  sequenceNumber = :sequenceNumber
                    """),
        @NamedQuery(name = "PersonTelephone.fetchByPidmSequenceNoAndAddressType",
                query = """FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND  addressSequenceNumber = :addressSequenceNumber
                             AND  addressType.code  = :addressType
                             AND  primaryIndicator IS NOT NULL
                    """),
        @NamedQuery(name = "PersonTelephone.fetchByPidmSequenceNoAndAddressTypeWithPrimaryCheck",
                query = """FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND  addressSequenceNumber = :addressSequenceNumber
                             AND  addressType.code  = :addressType
                             AND  primaryIndicator = 'Y'
                    """),
        @NamedQuery(name = "PersonTelephone.fetchByPidmSequenceNoAndAddressTypeWithoutPrimaryCheck",
                query = """FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND  addressSequenceNumber = :addressSequenceNumber
                             AND  addressType.code  = :addressType
                             AND NVL(statusIndicator,'A') <> 'I'
                    """),
        @NamedQuery(name = "PersonTelephone.fetchMaxSequenceNumber",
                query = """select max(a.sequenceNumber)
                             FROM  PersonTelephone a
	  	                     WHERE a.pidm = :pidm
                    """),
        @NamedQuery(name = "PersonTelephone.fetchByPidmTelephoneTypeAndAddressType",
                query = """FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND addressType IS NOT NULL
                             AND  (telephoneType LIKE :filter
                             OR addressType LIKE :filter)
                             ORDER BY DECODE(a.statusIndicator, 'I',-2) DESC
                    """),
        @NamedQuery(name = "PersonTelephone.fetchActiveTelephoneByPidmAndTelephoneType",
                query = """FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND telephoneType = :telephoneType
                             AND primaryIndicator = 'Y'
                             AND NVL(statusIndicator,'A') <> 'I'
                             AND NVL(unlistIndicator,'N') <> 'Y'
                    """),
        @NamedQuery(name = "PersonTelephone.fetchListActiveTelephoneByPidmAndTelephoneType",
                query = """FROM PersonTelephone a
                             WHERE  pidm IN :pidm
                             AND telephoneType IN :telephoneType
                             AND primaryIndicator = 'Y'
                             AND NVL(statusIndicator,'A') <> 'I'
                             AND NVL(unlistIndicator,'N') <> 'Y'
                    """),
        @NamedQuery(name = "PersonTelephone.fetchListActiveTelephoneByPidmAndTelephoneTypeWithPrimaryPrefered",
                query = """FROM PersonTelephone a
                             WHERE  pidm IN :pidm
                             AND telephoneType IN :telephoneType
                             AND (primaryIndicator = 'Y'
                                OR (primaryIndicator IS NULL
                                        AND NOT EXISTS
                                            (SELECT 'X' FROM PersonTelephone b
                                              WHERE b.pidm = a.pidm
                                                AND b.telephoneType = a.telephoneType
                                                AND b.primaryIndicator = 'Y'
                                                AND NVL(statusIndicator,'A') <> 'I'
                                                AND NVL(unlistIndicator,'N') <> 'Y')))
                             AND NVL(statusIndicator,'A') <> 'I'
                             AND NVL(unlistIndicator,'N') <> 'Y'
                    """),
        @NamedQuery(name = "PersonTelephone.fetchActiveTelephoneByPidmAndAddressType",
                query = """FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND addressType = :addressType
                             AND primaryIndicator = 'Y'
                             AND NVL(statusIndicator,'A') <> 'I'
                             AND NVL(unlistIndicator,'N') <> 'Y' """),
        @NamedQuery(name = "PersonTelephone.fetchActiveTelephonesByPidmAndAddressTypes",
                query = """FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND addressType.code in :addressTypes
                             AND NVL(statusIndicator,'A') <> 'I'
                             AND NVL(unlistIndicator,'N') <> 'Y' """),
        @NamedQuery(name = "PersonTelephone.fetchTelephonesByPidmAndAddressTypes",
                query = """FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND ( addressType.code in :addressTypes
                               OR (addressType is null and unlistIndicator = 'Y' and primaryIndicator = 'Y'))
                             AND NVL(statusIndicator,'A') <> 'I' """),
        @NamedQuery(name = "PersonTelephone.fetchActiveTelephoneByPidm",
                query = """FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND NVL(statusIndicator,'A') <> 'I'
                             AND NVL(unlistIndicator,'N') <> 'Y'
                    """),
        @NamedQuery(name = "PersonTelephone.fetchActiveTelephoneByPidmInList",
                query = """FROM PersonTelephone a
                             WHERE  pidm IN :pidms
                             AND NVL(statusIndicator,'A') <> 'I'
                             AND NVL(unlistIndicator,'N') <> 'Y'
                    """),
        @NamedQuery(name = "PersonTelephone.fetchAllActiveByPidmInListAndTelephoneTypeCodeInList",
                query = """FROM PersonTelephone
                           WHERE pidm IN :pidms
                           AND NVL(statusIndicator,'A') <> 'I'
                           AND NVL(unlistIndicator,'N') <> 'Y'
                           AND concat(phoneArea, phoneNumber) is not null
                           AND telephoneType.code IN :telephoneTypes"""),
        @NamedQuery(name = "PersonTelephone.fetchAllByIdInListAndTelephoneTypeCodeInList",
                query = """FROM PersonTelephone
                           WHERE id IN :ids
                           AND telephoneType.code IN :telephoneTypes"""),
        @NamedQuery(name = "PersonTelephone.fetchActiveTelephoneWithUnlistedByPidm",
                query = """FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND NVL(statusIndicator,'A') <> 'I' """),
        @NamedQuery(name = "PersonTelephone.fetchActiveTelephoneWithUnlistedByPidmAndTelephoneType",
                query = """FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND telephoneType = :telephoneType
                             AND NVL(statusIndicator,'A') <> 'I'"""),
        @NamedQuery(name = "PersonTelephone.fetchActiveTelephoneListWithUnlistedByPidmAndTelephoneType",
                query = """FROM PersonTelephone a
                             WHERE  pidm = :pidm
                             AND telephoneType IN :telephoneType
                             AND NVL(statusIndicator,'A') <> 'I' """)
])
@DatabaseModifiesState
class PersonTelephone implements Serializable {

    /**
     * Surrogate ID for SPRTELE
     */
    @Id
    @Column(name = "SPRTELE_SURROGATE_ID")
    @SequenceGenerator(name = "SPRTELE_SEQ_GEN", allocationSize = 1, sequenceName = "SPRTELE_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPRTELE_SEQ_GEN")
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
    @Column(name = "SPRTELE_SEQNO", unique = true, precision = 3)
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
            @JoinColumn(name = "SPRTELE_TELE_CODE", referencedColumnName = "STVTELE_CODE")
    ])
    TelephoneType telephoneType

    /**
     * Foreign Key : FK1_SPRTELE_INV_STVATYP_CODE
     */
    @ManyToOne
    @JoinColumns([
            @JoinColumn(name = "SPRTELE_ATYP_CODE", referencedColumnName = "STVATYP_CODE")
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
        if (id != that.id) return false
        if (version != that.version) return false
        if (pidm != that.pidm) return false
        if (sequenceNumber != that.sequenceNumber) return false
        if (phoneArea != that.phoneArea) return false
        if (phoneNumber != that.phoneNumber) return false
        if (phoneExtension != that.phoneExtension) return false
        if (statusIndicator != that.statusIndicator) return false
        if (addressSequenceNumber != that.addressSequenceNumber) return false
        if (primaryIndicator != that.primaryIndicator) return false
        if (unlistIndicator != that.unlistIndicator) return false
        if (commentData != that.commentData) return false
        if (internationalAccess != that.internationalAccess) return false
        if (countryPhone != that.countryPhone) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        if (telephoneType != that.telephoneType) return false
        if (addressType != that.addressType) return false
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
        pidm(nullable: false, min: -99999999, max: 99999999)
        sequenceNumber(nullable: true, min: -999, max: 999)
        phoneArea(nullable: true, maxSize: 6)
        phoneNumber(nullable: true, maxSize: 12)
        phoneExtension(nullable: true, maxSize: 10)
        statusIndicator(nullable: true, maxSize: 1, inList: ["I"])
        addressSequenceNumber(nullable: true, min: -99, max: 99)
        primaryIndicator(nullable: true, maxSize: 1, inList: ["Y"])
        unlistIndicator(nullable: true, maxSize: 1, inList: ["Y"])
        commentData(nullable: true, maxSize: 60)
        internationalAccess(nullable: true, maxSize: 16)
        countryPhone(nullable: true, maxSize: 4)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        telephoneType(nullable: false)
        addressType(nullable: true)
    }


    public static readonlyProperties = ['pidm', 'sequenceNumber']


    static PersonTelephone fetchByPidmSequenceNoAndAddressType(Integer pidm, Integer addressSequenceNumber,
                                                               def addressType) {
        def address
        if (addressType instanceof AddressType) address = addressType.code
        else address = addressType
        def personTelephone =
                PersonTelephone.withSession { session ->
                    session.getNamedQuery('PersonTelephone.fetchByPidmSequenceNoAndAddressType')
                            .setInteger('pidm', pidm)
                            .setInteger('addressSequenceNumber', addressSequenceNumber)
                            .setString('addressType', address).uniqueResult()
                }
        return personTelephone
    }

    /**
     * Finder for advanced filtering and sorting
     * @param filterData , pagingAndSortParams
     * @return filtered and sorted data
     */
    def static countAll(filterData) {
        finderByAll().count(filterData)
    }


    def static fetchSearch(filterData, pagingAndSortParams) {
        def personTelephones = finderByAll().find(filterData, pagingAndSortParams)
        return personTelephones
    }


    def private static finderByAll = {
        def query = """FROM PersonTelephone a WHERE a.pidm = :pidm"""
        return new DynamicFinder(PersonTelephone.class, query, "a")
    }


    public
    static PersonTelephone fetchByPidmSequenceNoAndAddressType(Integer pidm, Integer addressSequenceNumber, AddressType addressType) {
        PersonTelephone.withSession { session ->
            def personTelephone = session.getNamedQuery('PersonTelephone.fetchByPidmSequenceNoAndAddressType')
                    .setInteger('pidm', pidm)
                    .setInteger('addressSequenceNumber', addressSequenceNumber)
                    .setString('addressType', addressType.code).list()[0]

            return personTelephone
        }
    }


    static
    def fetchByPidmSequenceNoAndAddressTypeWithPrimaryCheck(Integer pidm, Integer addressSequenceNumber, AddressType addressType) {
        PersonTelephone.withSession { session ->
            def personTelephone = session.getNamedQuery('PersonTelephone.fetchByPidmSequenceNoAndAddressTypeWithPrimaryCheck')
                    .setInteger('pidm', pidm)
                    .setInteger('addressSequenceNumber', addressSequenceNumber)
                    .setString('addressType', addressType.code).list()[0]
            return personTelephone
        }
    }


    static
    def fetchByPidmSequenceNoAndAddressTypeWithoutPrimaryCheck(Integer pidm, Integer addressSequenceNumber, AddressType addressType) {
        PersonTelephone.withSession { session ->
            def personTelephone = session.getNamedQuery('PersonTelephone.fetchByPidmSequenceNoAndAddressTypeWithoutPrimaryCheck')
                    .setInteger('pidm', pidm)
                    .setInteger('addressSequenceNumber', addressSequenceNumber)
                    .setString('addressType', addressType.code).list()
            return personTelephone
        }
    }


    static def Integer fetchMaxSequenceNumber(Integer pidm) {
        PersonTelephone.withSession { session ->
            def maxSequenceNumber = session.getNamedQuery('PersonTelephone.fetchMaxSequenceNumber')
                    .setInteger('pidm', pidm).list()[0]
            return maxSequenceNumber
        }
    }


    static def fetchByPidmTelephoneTypeAndAddressType(map) {
        if (map && map.pidm) {
            PersonTelephone.withSession { session ->
                return [list: session.getNamedQuery('PersonTelephone.fetchByPidmTelephoneTypeAndAddressType')
                        .setInteger('pidm', map.pidm).setString('filter', '%').list()]
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


    static def fetchByPidmTelephoneTypeAndTelephoneSequence(Map map) {
        PersonTelephone.withSession { session ->
            def personTelephone = session.getNamedQuery('PersonTelephone.fetchByPidmTelephoneTypeAndTelephoneSequence')
                    .setInteger('pidm', map.pidm)
                    .setString('telephoneType', map.telephoneType.code)
                    .setInteger('sequenceNumber', map.telephoneSequenceNumber).list()[0]
            return personTelephone
        }
    }


    static PersonTelephone fetchActiveTelephoneByPidmAndTelephoneType(Integer pidm, String telephoneType) {
        PersonTelephone.withSession { session ->
            PersonTelephone personTelephone = session.getNamedQuery('PersonTelephone.fetchActiveTelephoneByPidmAndTelephoneType')
                    .setInteger('pidm', pidm)
                    .setString('telephoneType', telephoneType).list()[0]
            return personTelephone
        }
    }


    static List fetchListActiveTelephoneByPidmAndTelephoneType(List pidm, List telephoneType) {
        if (pidm.isEmpty() || telephoneType.isEmpty()) return []
        PersonTelephone.withSession { session ->
            List personTelephone = session.getNamedQuery('PersonTelephone.fetchListActiveTelephoneByPidmAndTelephoneType')
                    .setParameterList('pidm', pidm)
                    .setParameterList('telephoneType', telephoneType).list()
            return personTelephone
        }
    }


    static List fetchListActiveTelephoneByPidmAndTelephoneTypeWithPrimaryPrefered(List pidm, List telephoneType) {
        if (pidm.isEmpty() || telephoneType.isEmpty()) return []
        PersonTelephone.withSession { session ->
            List personTelephone = session.getNamedQuery('PersonTelephone.fetchListActiveTelephoneByPidmAndTelephoneTypeWithPrimaryPrefered')
                    .setParameterList('pidm', pidm)
                    .setParameterList('telephoneType', telephoneType).list()
            return personTelephone
        }
    }


    public
    static PersonTelephone fetchActiveTelephoneByPidmAndTelephoneType(Integer pidm, TelephoneType telephoneType) {
        PersonTelephone.withSession { session ->
            PersonTelephone personTelephone = session.getNamedQuery('PersonTelephone.fetchActiveTelephoneByPidmAndTelephoneType')
                    .setInteger('pidm', pidm)
                    .setString('telephoneType', telephoneType.code).list()[0]
            return personTelephone
        }
    }


    static def fetchActiveTelephoneByPidmAndAddressType(Integer pidm, AddressType addressType) {
        PersonTelephone.withSession { session ->
            session.getNamedQuery('PersonTelephone.fetchActiveTelephoneByPidmAndAddressType')
                    .setInteger('pidm', pidm)
                    .setString('addressType', addressType.code).list()
        }
    }


    public static def fetchActiveTelephonesByPidmAndAddressTypes(Map params) {

        def result = []
        result = PersonTelephone.withSession { session ->
            session.getNamedQuery('PersonTelephone.fetchActiveTelephonesByPidmAndAddressTypes').setInteger('pidm', params?.pidm)
                    .setParameterList('addressTypes', params.addressTypes).list()
        }

        return result
    }

    public static def fetchTelephonesByPidmAndAddressTypes(Map params) {

        def result = []
        result = PersonTelephone.withSession { session ->
            session.getNamedQuery('PersonTelephone.fetchTelephonesByPidmAndAddressTypes').setInteger('pidm', params?.pidm)
                    .setParameterList('addressTypes', params.addressTypes).list()
        }

        return result
    }

    static def fetchActiveTelephoneByPidm(Integer pidm) {
        PersonTelephone.withSession { session ->
            session.getNamedQuery('PersonTelephone.fetchActiveTelephoneByPidm')
                    .setInteger('pidm', pidm).list()
        }
    }

    static List<PersonTelephone> fetchActiveTelephoneByPidmInList(List<Integer> pidms) {
        if (pidms.isEmpty()) {
            return []
        }
        PersonTelephone.withSession { session ->
            session.getNamedQuery('PersonTelephone.fetchActiveTelephoneByPidmInList')
                    .setParameterList('pidms', pidms).list()
        }
    }

    static Promise fetchActiveTelephoneByPidmInListAsync(List<Integer> pidms) {
        PersonTelephone.async.task {
            fetchActiveTelephoneByPidmInList(pidms)
        }
    }

    static def fetchActiveTelephoneWithUnlistedByPidm(Integer pidm){
        PersonTelephone.withSession { session ->
            session.getNamedQuery('PersonTelephone.fetchActiveTelephoneWithUnlistedByPidm')
                    .setInteger('pidm', pidm).list()
        }
    }

    static def fetchActiveTelephoneWithUnlistedByPidmAndTelephoneType(Integer pidm, TelephoneType telephoneType){
        PersonTelephone.withSession { session ->
            session.getNamedQuery('PersonTelephone.fetchActiveTelephoneWithUnlistedByPidmAndTelephoneType')
                    .setInteger('pidm', pidm)
                    .setString('telephoneType', telephoneType.code).list()
        }
    }

    static def fetchActiveTelephoneListWithUnlistedByPidmAndTelephoneType(Integer pidm, List telephoneType){
        PersonTelephone.withSession { session ->
            session.getNamedQuery('PersonTelephone.fetchActiveTelephoneListWithUnlistedByPidmAndTelephoneType')
                    .setInteger('pidm', pidm)
                    .setParameterList('telephoneType', telephoneType).list()
        }
    }
}
