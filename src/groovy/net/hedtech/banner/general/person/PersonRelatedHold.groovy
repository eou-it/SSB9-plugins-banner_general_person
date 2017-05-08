/*********************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.system.HoldType
import net.hedtech.banner.general.system.Originator
import net.hedtech.banner.service.DatabaseModifiesState
import org.hibernate.annotations.Type
import javax.persistence.*
import net.hedtech.banner.query.DynamicFinder

/**
 * Person Related Holds model.
 */
@NamedQueries(value = [
        @NamedQuery(name = "PersonRelatedHold.fetchById",
                query = """FROM PersonRelatedHold a
             WHERE a.id = :id"""),
        @NamedQuery(name = "PersonRelatedHold.fetchByPidm",
                query = """FROM PersonRelatedHold a
             WHERE a.pidm = :pidm
          ORDER BY a.fromDate desc, a.toDate desc """),
        @NamedQuery(name = "PersonRelatedHold.fetchByPidmAndDateBetween",
                query = """FROM PersonRelatedHold a
             WHERE a.pidm = :pidm
               AND TRUNC(:compareDate) BETWEEN TRUNC(a.fromDate) AND TRUNC(a.toDate)
          ORDER BY a.fromDate desc, a.toDate desc """),
        @NamedQuery(name = "PersonRelatedHold.fetchByPidmDateAndHoldType",
                query = """FROM  PersonRelatedHold a
             WHERE a.pidm = :pidm
               AND a.holdType.code = :holdType
               AND ( TRUNC(:compareDate) >= TRUNC(a.fromDate)
                     AND TRUNC(:compareDate) < TRUNC(a.toDate) )
          ORDER BY a.fromDate desc, a.toDate desc """),
        @NamedQuery(name = "PersonRelatedHold.fetchByPidmAndDateCompare",
                query = """FROM  PersonRelatedHold a
             WHERE a.pidm = :pidm
               AND ( TRUNC(:compareDate) >= TRUNC(a.fromDate)
                     AND TRUNC(:compareDate) < TRUNC(a.toDate) )
          ORDER BY a.fromDate desc, a.toDate desc """),
        @NamedQuery(name = "PersonRelatedHold.fetchByPidmListAndDateCompare",
                query = """FROM  PersonRelatedHold a
             WHERE a.pidm IN :pidm
               AND ( TRUNC(:compareDate) >= TRUNC(a.fromDate)
                     AND TRUNC(:compareDate) < TRUNC(a.toDate) )
          ORDER BY a.fromDate desc, a.toDate desc """),
        @NamedQuery(name = "PersonRelatedHold.fetchAllDistinctHoldTypeByPidmAndDateCompare",
                query = """SELECT a.holdType.code
            FROM PersonRelatedHold a
            WHERE a.pidm = :pidm
            AND ( TRUNC(:compareDate) >= TRUNC(a.fromDate)
            AND TRUNC(:compareDate) <= TRUNC(a.toDate) )
            GROUP BY a.holdType.code
            ORDER BY a.holdType.code """),
        @NamedQuery(name = "PersonRelatedHold.fetchAll",
                query = """FROM PersonRelatedHold p,GlobalUniqueIdentifier g1,GlobalUniqueIdentifier g2,GlobalUniqueIdentifier g3
            WHERE (TRUNC(:compareDate) <= TRUNC(p.toDate))
            AND g1.ldmName = 'person-holds' AND g1.domainId = p.id
            AND g2.ldmName = 'persons' AND g2.domainKey = TO_CHAR(p.pidm)
            AND g3.ldmName = 'restriction-types' AND g3.domainKey= p.holdType.code"""),
        @NamedQuery(name = "PersonRelatedHold.countRecord",
                query = """SELECT count(*) FROM PersonRelatedHold p,GlobalUniqueIdentifier g1,GlobalUniqueIdentifier g2,GlobalUniqueIdentifier g3
            WHERE (TRUNC(:compareDate) <= TRUNC(p.toDate))
            AND g1.ldmName = 'person-holds' AND g1.domainId = p.id
            AND g2.ldmName = 'persons' AND g2.domainKey = TO_CHAR(p.pidm)
            AND g3.ldmName = 'restriction-types' AND g3.domainKey= p.holdType.code"""),
        @NamedQuery(name = "PersonRelatedHold.fetchByPersonHoldGuid",
                query = """FROM PersonRelatedHold p,GlobalUniqueIdentifier g1,GlobalUniqueIdentifier g2,GlobalUniqueIdentifier g3
            WHERE (TRUNC(:compareDate) <= TRUNC(p.toDate))
            AND g1.ldmName = 'person-holds' AND g1.domainId = p.id AND g1.guid = :guid
            AND g2.ldmName = 'persons' AND g2.domainKey = TO_CHAR(p.pidm)
            AND g3.ldmName = 'restriction-types' AND g3.domainKey= p.holdType.code"""),
        @NamedQuery(name = "PersonRelatedHold.fetchCountByPersonGuid",
                query = """SELECT count(*) FROM PersonRelatedHold p,GlobalUniqueIdentifier g1,GlobalUniqueIdentifier g2,GlobalUniqueIdentifier g3
            WHERE (TRUNC(:compareDate) <= TRUNC(p.toDate))
            AND g1.ldmName = 'person-holds' AND g1.domainId = p.id
            AND g2.ldmName = 'persons' AND g2.domainKey = TO_CHAR(p.pidm) AND g2.guid = :guid
            AND g3.ldmName = 'restriction-types' AND g3.domainKey= p.holdType.code"""),
        @NamedQuery(name = "PersonRelatedHold.fetchByPersonsGuid",
                query = """FROM PersonRelatedHold p,GlobalUniqueIdentifier g1,GlobalUniqueIdentifier g2,GlobalUniqueIdentifier g3
            WHERE (TRUNC(:compareDate) <= TRUNC(p.toDate))
            AND g1.ldmName = 'person-holds' AND g1.domainId = p.id
            AND g2.ldmName = 'persons' AND g2.domainKey = TO_CHAR(p.pidm) AND g2.guid = :guid
            AND g3.ldmName = 'restriction-types' AND g3.domainKey= p.holdType.code""")
])

@Entity
@Table(name = "SV_SPRHOLD")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
@DatabaseModifiesState
class PersonRelatedHold implements Serializable {

    /**
     * Surrogate ID for SPRHOLD
     */

    @Id
    @Column(name = "SPRHOLD_SURROGATE_ID")
    @SequenceGenerator(name = "SPRHOLD_SEQ_GEN", allocationSize = 1, sequenceName = "SPRHOLD_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPRHOLD_SEQ_GEN")
    Integer id

    /**
     * Optimistic lock token for SPRHOLD
     */

    @Version
    @Column(name = "SPRHOLD_VERSION")
    Integer version

    /**
     * Internal identification number of the person.
     */

    @Column(name = "SPRHOLD_PIDM")
    Integer pidm

    /**
     * Foreign Key : FK1_SPRHOLD_INV_STVHLDD_CODE
     */

    @ManyToOne
    @JoinColumns([
            @JoinColumn(name = "SPRHOLD_HLDD_CODE", referencedColumnName = "STVHLDD_CODE")
    ])
    HoldType holdType

    /**
     * This field identifies the person who created the hold. It might be an Oracle ID
     */
    @Column(name = "SPRHOLD_USER")
    String createdBy

    /**
     * This field identifies the system user signon id initiating hold.
     * **lastModifiedBy User is always set back to the original user by API
     */

    @Column(name = "SPRHOLD_USER_ID")
    String lastModifiedBy

    /**
     * This field identifies the effective begin date of hold.
     */

    @Column(name = "SPRHOLD_FROM_DATE")
    @Temporal(TemporalType.DATE)
    Date fromDate

    /**
     * This field identifies the end date hold expires.
     */

    @Column(name = "SPRHOLD_TO_DATE")
    @Temporal(TemporalType.DATE)
    Date toDate

    /**
     * This field indicates that only the system user who entered the hold may release the hold.  Valid value is:  Y - allow only system user to release       hold.
     */

    @Type(type = "yes_no")
    @Column(name = "SPRHOLD_RELEASE_IND")
    Boolean releaseIndicator = false

    /**
     * Free format field which identifies the reason hold was placed.
     */

    @Column(name = "SPRHOLD_REASON")
    String reason

    /**
     * This field identifies a dollar amount associated with hold.
     */

    @Column(name = "SPRHOLD_AMOUNT_OWED")
    BigDecimal amountOwed

    /**
     * Foreign Key : FK1_SPRHOLD_INV_STVORIG_CODE
     */

    @ManyToOne
    @JoinColumns([
            @JoinColumn(name = "SPRHOLD_ORIG_CODE", referencedColumnName = "STVORIG_CODE")
    ])
    Originator originator

    /**
     * This field defines most current date record is created or changed.
     */

    @Column(name = "SPRHOLD_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * DATA SOURCE: Source system that created or updated the row
     */

    @Column(name = "SPRHOLD_DATA_ORIGIN")
    String dataOrigin


    public static readonlyProperties = ['pidm']




    static constraints = {
        pidm( nullable: false, maxsize: 22 )
        holdType( nullable: false )
        fromDate( nullable: false, validator: { val, obj ->
            if ((val != null) && (val > obj.toDate)) {
                return 'invalid.fromDateGreaterThanToDate'
            }
        } )
        toDate( nullable: false )
        releaseIndicator( nullable: false )
        reason( nullable: true, maxSize: 30 )
        amountOwed( nullable: true, max: 99999.99, min: 00000.00, scale: 2 )
        originator( nullable: true )
        lastModified( nullable: true )
        lastModifiedBy( nullable: true, maxSize: 30 )
        dataOrigin( nullable: true, maxSize: 30 )
    }


    public static List fetchByPidm( Integer pidm ) {

        def personRelatedHolds = PersonRelatedHold.withSession { session ->
            session.getNamedQuery( 'PersonRelatedHold.fetchByPidm' ).setInteger( 'pidm', pidm ).list()
        }

        return personRelatedHolds
    }

    /**
     * Retrieve person related hold records where the date sent in is between
     * the hold from date and the hold to date.
     */

    public static List fetchByPidmAndDateBetween( Integer pidm, Date compareDate ) {

        def personRelatedHolds = PersonRelatedHold.withSession { session ->
            session.getNamedQuery( 'PersonRelatedHold.fetchByPidmAndDateBetween' ).setInteger( 'pidm', pidm ).setDate( 'compareDate', compareDate ).list()
        }

        return personRelatedHolds
    }

    /**
     * Retrieve person related hold records where the hold type sent in exists and the date sent in is greater
     * than or equal to the hold from date and less than the hold to date.
     */

    public static List fetchByPidmDateAndHoldType( Integer pidm, Date compareDate, String holdType ) {

        def personRelatedHolds = PersonRelatedHold.withSession { session ->
            session.getNamedQuery( 'PersonRelatedHold.fetchByPidmDateAndHoldType' ).setInteger( 'pidm', pidm ).setDate( 'compareDate', compareDate ).setString( 'holdType', holdType ).list()
        }

        return personRelatedHolds
    }

    /**
     * Retrieve person related hold records where the date sent in is greater
     * than or equal to the hold from date and less than the hold to date.
     */

    public static List fetchByPidmAndDateCompare( Integer pidm, Date compareDate ) {

        def personRelatedHolds = PersonRelatedHold.withSession { session ->
            session.getNamedQuery( 'PersonRelatedHold.fetchByPidmAndDateCompare' ).setInteger( 'pidm', pidm ).setDate( 'compareDate', compareDate ).list()
        }

        return personRelatedHolds
    }

    /**
     * Retrieve distinct person related hold records where the date sent in is greater
     * than or equal to the hold from date and less than equal to the hold to date.
     */
    public static List fetchAllDistinctHoldTypeByPidmAndDateCompare( Integer pidm, Date compareDate ) {
        def personRelatedHolds = PersonRelatedHold.withSession { session ->
            session.getNamedQuery( 'PersonRelatedHold.fetchAllDistinctHoldTypeByPidmAndDateCompare' ).setInteger( 'pidm', pidm ).setDate( 'compareDate', compareDate ).list()
        }

        return personRelatedHolds
    }

    /**
     * Retrieve person related hold records where the date sent in is greater
     * than or equal to the hold from date and less than the hold to date, for a list of pidms.
     */

    public static List fetchByPidmListAndDateCompare( List pidm, Date compareDate ) {
        def personRelatedHolds = PersonRelatedHold.withSession { session ->
            session.getNamedQuery( 'PersonRelatedHold.fetchByPidmListAndDateCompare' ).setParameterList( 'pidm', pidm ).setDate( 'compareDate', compareDate ).list()
        }

        return personRelatedHolds
    }

    /**
     * Check for the existence of a person related registration hold record where the date sent in is greater
     * than or equal to the hold from date and less than the hold to date.  The person related hold type
     * must be defined as a registration hold on the STVHLDD record.
     */
    public static Boolean registrationHoldsExist( Integer pidm, Date compareDate ) {
        def registrationHoldsExist = false
        def personRelatedHolds
        def registrationHoldType

        personRelatedHolds = fetchByPidmAndDateCompare( pidm, compareDate )
        if (!registrationHoldsExist) {
            personRelatedHolds.each {
                registrationHoldType = HoldType.findByCodeAndRegistrationHoldIndicator( it.holdType.code, true )
                if (registrationHoldType != null)
                    registrationHoldsExist = true
            }
        }
        return registrationHoldsExist
    }


    def static countAll( filterData ) {
        finderByAll().count( filterData )
    }


    def static fetchSearch( filterData, pagingAndSortParams ) {
        def personRelatedHolds = finderByAll().find( filterData, pagingAndSortParams )
        return personRelatedHolds
    }


    def private static finderByAll = {
        def query = """FROM  PersonRelatedHold a
                       WHERE a.pidm = :pidm """
        return new DynamicFinder( PersonRelatedHold.class, query, "a" )
    }




    /**
     * Retrieve person related hold records where the date sent in is
     * less than the hold to date, for a list of pidms.
     */
    public static List fetchAll(params,compareDate){
        return PersonRelatedHold.withSession {session ->
            session.getNamedQuery('PersonRelatedHold.fetchAll')
                    .setDate( 'compareDate', compareDate )
                    .setMaxResults(params.max as Integer)
                    .setFirstResult(params.offset as Integer).list()
        }
    }


    /**
     * @param compareDate
     * @return count
     */
    public static Long countRecord(compareDate){
        return PersonRelatedHold.withSession { session ->
            session.getNamedQuery('PersonRelatedHold.countRecord').setDate( 'compareDate', compareDate ).uniqueResult()
        }
    }


    /**
     * @param params,compareDate
     * @return count for holds on a persons
     */
    public static Long countRecordWithFilter(params,compareDate) {
        return PersonRelatedHold.withSession { session ->
            session.getNamedQuery('PersonRelatedHold.fetchCountByPersonGuid').setDate( 'compareDate', compareDate ).setString('guid', params.person ).uniqueResult()
        }
    }



    /**
     * @param guid,compareDate
     * @return personHolds
     */
    public static def fetchByPersonHoldGuid(String guid,Date compareDate) {
        return PersonRelatedHold.withSession {
            session -> session.getNamedQuery( 'PersonRelatedHold.fetchByPersonHoldGuid' ).setDate('compareDate', compareDate ).setString( 'guid', guid ).uniqueResult()
        }
    }


    /**
     * @param params,compareDate
     * @return personHolds
     */
    public static List fetchByPersonsGuid(params,Date compareDate) {
        return PersonRelatedHold.withSession {
            session -> session.getNamedQuery( 'PersonRelatedHold.fetchByPersonsGuid' )
                    .setDate( 'compareDate', compareDate )
                    .setString( 'guid', params?.person)
                    .setMaxResults(params.max as Integer)
                    .setFirstResult(params.offset as Integer).list()
        }
    }
}
