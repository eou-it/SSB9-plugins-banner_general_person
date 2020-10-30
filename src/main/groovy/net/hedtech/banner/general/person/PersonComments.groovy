/** *****************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version

/**
 * Person comments repeating  details
 */

@Entity
@Table(name = "SPRCMNT")
@ToString(includeNames = true, ignoreNulls = true)
@EqualsAndHashCode(includeFields = true)
class PersonComments implements Serializable {

    /*
     * Surrogate ID for SPRCMNT
     */
    @Id
    @Column(name = "SPRCMNT_SURROGATE_ID")
    @SequenceGenerator(name = "SPRCMNT_SEQ_GEN", allocationSize = 1, sequenceName =
            "SPRCMNT_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPRCMNT_SEQ_GEN")
    Long id

    /**
     *  Internal identification number of the person.
     */
    @Column(name = "SPRCMNT_PIDM")
    Integer pidm

    /**
     *  Appointment from time if the comment refers back to an appointment.
     */
    @Column(name = "SPRCMNT_CONTACT_FROM_TIME")
    Integer contactFromTime

    /**
     *  Appointment to time if the comment refers back to an appointment.
     */
    @Column(name = "SPRCMNT_CONTACT_TO_TIME")
    Integer contactToTime

    /**
     * This field identifies the comment type associated with person record.
     */
    @Column(name = "SPRCMNT_CMTT_CODE")
    String commentCode

    /**
     * Contact code how the comment's contents originated.
     */
    @Column(name = "SPRCMNT_CTYP_CODE")
    String commentTypeCode

    /**
     * identifies the most recent date a record was created or updated.
     */
    @Column(name = "SPRCMNT_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * This field indicates the date comment was added to the person record.
     */
    @Column(name = "SPRCMNT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date dateCommentAdded

    /**
     * Contact date/time when the comment's contents originated.
     */
    @Column(name = "SPRCMNT_CONTACT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date contactDate

    /**
     * Last Modified By column for SPRCMNT
     */
    @Column(name = "SPRCMNT_USER_ID")
    String lastModifiedBy

    /**
     * Version column which is used as a optimistic lock token for SPRCMNT
     */
    @Version
    @Column(name = "SPRCMNT_VERSION")
    Long version

    /**
     * Source system that created or updated the row.
     */
    @Column(name = "SPRCMNT_DATA_ORIGIN")
    String dataOrigin

    /**
     * Multi-entity processing code.
     * */
    @Column(name = "SPRCMNT_VPDI_CODE")
    String mepCode

    /**
     * This field identifies originator of the comment associated with person record.
     * */
    @Column(name = "SPRCMNT_ORIG_CODE")
    String originatorCode

    /**
     * A Y value in this field indicates that the comment associated with person record is confidential.
     * */
    @Column(name = "SPRCMNT_CONFIDENTIAL_IND")
    String confidentialInd

    /**
     * This field maintains the text of the comment associated with person record.
     * */
    @Column(name = "SPRCMNT_TEXT")
    String text

    /**
     * GUID storage
     * */
    @Column(name = "SPRCMNT_GUID")
    String guidStorage

    /**
     * This field maintains large narrative comment associated with person record.
     * */
    @Column(name="SPRCMNT_TEXT_NAR")
    @Lob
    String narrativeText


    static constraints = {
        pidm(nullable: false)
        id(nullable: false)
        lastModified(nullable: false)
        commentCode(nullable: false,  maxSize: 3)
        version(nullable: false)
        commentTypeCode(nullable: true,  maxSize: 4)
        lastModifiedBy(nullable: true,  maxSize: 30)
        contactDate(nullable: true)
        contactFromTime(nullable: true)
        contactToTime(nullable: true)
        dateCommentAdded(nullable: true)
        dataOrigin(nullable: true,  maxSize: 30)
        mepCode(nullable: true,  maxSize: 6)
        originatorCode(nullable: true,  maxSize: 4)
        confidentialInd(nullable: true,  maxSize: 1)
        text(nullable: true,  maxSize: 4000)
        guidStorage(nullable: true,  maxSize: 36)
        narrativeText(nullable: true)
    }

}
