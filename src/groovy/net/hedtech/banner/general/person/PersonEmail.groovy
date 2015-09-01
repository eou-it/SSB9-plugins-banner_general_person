/*********************************************************************************
 Copyright 2009-2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import net.hedtech.banner.general.system.EmailType
import net.hedtech.banner.general.system.SystemUtility
import net.hedtech.banner.query.DynamicFinder
import org.apache.log4j.Logger
import org.hibernate.annotations.Type

import javax.persistence.*

/**
 * Represents a person's email address
 */
@Entity
@Cacheable
@Table(name = "GV_GOREMAL")
@NamedQueries(value = [
@NamedQuery(name = "PersonEmail.fetchByPidmAndStatusAndWebDisplayAndPreferredIndicator",
query = """FROM PersonEmail a
    WHERE a.pidm = :pidm
    AND a.statusIndicator = :statusIndicator
    AND a.preferredIndicator = :preferredIndicator
    AND a.displayWebIndicator = :displayWebIndicator"""),
@NamedQuery(name = "PersonEmail.fetchByPidmAndStatus",
query = """FROM PersonEmail a
    WHERE a.pidm = :pidm
    AND a.statusIndicator = :statusIndicator
    order by a.preferredIndicator, a.emailType.code"""),
@NamedQuery(name = "PersonEmail.fetchFirstByPidmAndStatusAndWebDisplayAndPreferredIndicator",
query = """SELECT a.emailAddress
    FROM PersonEmail a
    WHERE a.pidm = :pidm
    AND a.statusIndicator = :statusIndicator
    AND a.preferredIndicator = :preferredIndicator
    AND a.displayWebIndicator = :displayWebIndicator
    order by a.lastModified desc, a.id asc"""),
@NamedQuery(name = "PersonEmail.fetchByPidmAndEmailTypeAndStatusAndWebDisplayAndPreferredIndicator",
query = """FROM PersonEmail a
    WHERE a.pidm = :pidm
    AND a.emailType.code = :emailType
    AND a.statusIndicator = :statusIndicator
    AND a.preferredIndicator = :preferredIndicator
    AND a.displayWebIndicator = :displayWebIndicator"""),
@NamedQuery(name = "PersonEmail.fetchListByPidmAndStatusAndWebDisplay",
query = """FROM PersonEmail a
    WHERE a.pidm IN :pidm
    AND a.statusIndicator = :statusIndicator
    AND a.displayWebIndicator = :displayWebIndicator"""),
@NamedQuery(name = "PersonEmail.fetchListByPidmAndStatus",
query = """FROM PersonEmail a
    WHERE a.pidm IN :pidm
    AND a.statusIndicator = :statusIndicator"""),
@NamedQuery(name = "PersonEmail.fetchByEmailAddressAndActiveStatus",
query = """FROM PersonEmail a
    WHERE upper(a.emailAddress) = upper(:emailAddress)
    AND a.statusIndicator = :statusIndicator"""),
@NamedQuery(name = "PersonEmail.fetchByPidmAndStatusAndWebDisplay",
query = """FROM PersonEmail a
    WHERE a.pidm = :pidm
    AND a.statusIndicator = :statusIndicator
    AND a.displayWebIndicator = :displayWebIndicator"""),
@NamedQuery(name = "PersonEmail.fetchListByPidmAndEmailTypes",
query = """FROM PersonEmail a
    WHERE a.pidm = :pidm
    AND a.emailType.code IN (:emailTypes)"""),
@NamedQuery(name = "PersonEmail.fetchByPidmsAndActiveStatus",
query = """FROM PersonEmail a
    WHERE a.pidm in (:pidms)
    and a.statusIndicator = 'A' """)
])
class PersonEmail implements Serializable {

    static mapping = {
        cache true
    }

    static def log = Logger.getLogger('net.hedtech.banner.general.person.PersonEmail')

    /**
     * Surrogate ID for GOREMAL
     */
    @Id
    @Column(name = "GOREMAL_SURROGATE_ID")
    @SequenceGenerator(name = "GOREMAL_SEQ_GEN", allocationSize = 1, sequenceName = "GOREMAL_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GOREMAL_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GOREMAL
     */
    @Version
    @Column(name = "GOREMAL_VERSION")
    Long version

    /**
     * The pidm of the entity who owns this e-mail information.
     */
    @Column(name = "GOREMAL_PIDM")
    Integer pidm

    /**
     * The e-mail address.
     */
    @Column(name = "GOREMAL_EMAIL_ADDRESS")
    String emailAddress

    /**
     * The status of the e-mail address: (A)ctive, (I)nactive
     */
    @Column(name = "GOREMAL_STATUS_IND")
    String statusIndicator = "A"

    /**
     * This column indicates if the e-mail address is the preferred contact address.
     */
    @Type(type = "yes_no")
    @Column(name = "GOREMAL_PREFERRED_IND")
    Boolean preferredIndicator = false

    /**
     * This is a free format comment regarding the e-mail information.
     */
    @Column(name = "GOREMAL_COMMENT")
    String commentData

    /**
     * Indicate whether a e-mail address should appear on Web.
     */
    @Type(type = "yes_no")
    @Column(name = "GOREMAL_DISP_WEB_IND")
    Boolean displayWebIndicator = true

    /**
     * The date on which the row was added or modified.
     */
    @Column(name = "GOREMAL_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * The user id when the row was added or modified.
     */
    @Column(name = "GOREMAL_USER_ID")
    String lastModifiedBy

    /**
     * DATA ORIGIN: Source system that created or updated the row
     */
    @Column(name = "GOREMAL_DATA_ORIGIN")
    String dataOrigin

    /**
     * Foreign Key : FKV_GOREMAL_INV_GTVEMAL_CODE
     */
    @ManyToOne
    @JoinColumns([
            @JoinColumn(name = "GOREMAL_EMAL_CODE", referencedColumnName = "GTVEMAL_CODE")
    ])
    EmailType emailType


    public String toString() {
        """PersonEmail[
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
        if (id != that.id) return false
        if (version != that.version) return false
        if (pidm != that.pidm) return false
        if (emailAddress != that.emailAddress) return false
        if (statusIndicator != that.statusIndicator) return false
        if (preferredIndicator != that.preferredIndicator) return false
        if (commentData != that.commentData) return false
        if (displayWebIndicator != that.displayWebIndicator) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        if (emailType != that.emailType) return false
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
        pidm(nullable: false, min: -99999999, max: 99999999)
        emailAddress(nullable: false, maxSize: 128)
        statusIndicator(nullable: false, maxSize: 1, inList: ["I", "A"])
        preferredIndicator(nullable: false)
        commentData(nullable: true, maxSize: 60)
        displayWebIndicator(nullable: false)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        emailType(nullable: false)

    }
    //Read Only fields that should be protected against update
//    public static readonlyProperties = ['pidm', 'emailAddress', 'emailType']

    public static readonlyProperties = ['pidm']

    /**
     * Finder for advanced filtering and sorting
     * @param filterData , pagingAndSortParams
     * @return filtered and sorted data
     */
    def static countAll(filterData) {
        finderByAll().count(filterData)
    }


    def static fetchSearch(filterData, pagingAndSortParams) {
        def personEmails = finderByAll().find(filterData, pagingAndSortParams)
        return personEmails
    }


    def private static finderByAll = {
        def query = """FROM PersonEmail a WHERE a.pidm = :pidm"""
        return new DynamicFinder(PersonEmail.class, query, "a")
    }


    public static List fetchByPidmAndStatusAndWebDisplayAndPreferredIndicator(Integer pidm, String statusIndicator,
                                                                              String displayWebIndicator, String preferredIndicator) {

        def email = PersonEmail.withSession { session ->
            session.getNamedQuery('PersonEmail.fetchByPidmAndStatusAndWebDisplayAndPreferredIndicator').setCacheable(true).setInteger('pidm', pidm).setString('statusIndicator', statusIndicator).setString('displayWebIndicator', displayWebIndicator).setString('preferredIndicator', preferredIndicator).list()
        }
        log.debug "Executing fetchByPidmAndStatusAndWebDisplayAndPreferredIndiator  with pidm = ${pidm} and status = ${statusIndicator} and displayWebIndicator = ${displayWebIndicator} and preferredIndicator = {$preferredIndicator}"
        log.debug "Fetched number of emails ${email.size()}"
        return email
    }


    public static List fetchByPidmAndStatus(Integer pidm, String statusIndicator) {

        def email = PersonEmail.withSession { session ->
            session.getNamedQuery('PersonEmail.fetchByPidmAndStatus').setCacheable(true).setInteger('pidm', pidm).setString('statusIndicator', statusIndicator).list()
        }
        log.debug "Executing fetchByPidmAndStatus  with pidm = ${pidm} and status = ${statusIndicator}  "
        log.debug "Fetched number of emails ${email.size()}"
        return email
    }


    public static String fetchFirstByPidmAndStatusAndWebDisplayAndPreferredIndicator(Integer pidm, String statusIndicator,
                                                                              String displayWebIndicator, String preferredIndicator) {

        def email = PersonEmail.withSession { session ->
            session.getNamedQuery('PersonEmail.fetchFirstByPidmAndStatusAndWebDisplayAndPreferredIndicator').setCacheable(true).setInteger('pidm', pidm).setString('statusIndicator', statusIndicator).setString('displayWebIndicator', displayWebIndicator).setString('preferredIndicator', preferredIndicator).list()[0]
        }
        log.debug "Executing fetchFirstByPidmAndStatusAndWebDisplayAndPreferredIndiator  with pidm = ${pidm} and status = ${statusIndicator} and displayWebIndicator = ${displayWebIndicator} and preferredIndicator = {$preferredIndicator}"
        log.debug "Fetched number of emails ${email} }"
        return email
    }


    public static PersonEmail fetchByPidmAndEmailTypeAndStatusAndWebDisplayAndPreferredIndicator(Integer pidm, String emailType, String statusIndicator,
                                                                                          String displayWebIndicator, String preferredIndicator) {

        def email = PersonEmail.withSession {session ->
            session.getNamedQuery('PersonEmail.fetchByPidmAndEmailTypeAndStatusAndWebDisplayAndPreferredIndicator').setCacheable(true).setInteger('pidm', pidm).setString('emailType',emailType).setString('statusIndicator', statusIndicator).setString('displayWebIndicator', displayWebIndicator).setString('preferredIndicator', preferredIndicator).list()
        }
        log.debug "Executing fetchByPidmAndEmailTypeAndStatusAndWebDisplayAndPreferredIndicator  with pidm = ${pidm} and status = ${statusIndicator} and displayWebIndicator = ${displayWebIndicator} and preferredIndicator = {$preferredIndicator}"
        log.debug "Fetched number of emails ${email.size()}"
        return email[0]
    }


    public static List fetchListByPidmAndStatusAndWebDisplay(List pidm, String statusIndicator, String displayWebIndicator) {
        def emails = PersonEmail.withSession { session ->
            session.getNamedQuery('PersonEmail.fetchListByPidmAndStatusAndWebDisplay').setCacheable(true).setParameterList('pidm', pidm).setString('statusIndicator', statusIndicator).setString('displayWebIndicator', displayWebIndicator).list()
        }

        log.debug "Executing fetchListByPidmAndStatusAndWebDisplay  with pidms = ${pidm} and status = ${statusIndicator} and displayWebIndicator = ${displayWebIndicator}"
        log.debug "Fetched number of emails ${emails.size()} }"
        return emails
    }


    public static List fetchListByPidmAndStatus(List pidm, String statusIndicator) {
        def emails = PersonEmail.withSession { session ->
            session.getNamedQuery('PersonEmail.fetchListByPidmAndStatus').setCacheable(true).setParameterList('pidm', pidm).setString('statusIndicator', statusIndicator).list()
        }

        log.debug "Executing fetchListByPidmAndStatus  with pidms = ${pidm} and status = ${statusIndicator}"
        log.debug "Fetched number of emails ${emails.size()} }"
        return emails
    }


    public static PersonEmail fetchByEmailAddressAndActiveStatus(String address) {
        PersonEmail personEmail = PersonEmail.withSession { session ->
            session.getNamedQuery( 'PersonEmail.fetchByEmailAddressAndActiveStatus' ).setCacheable(true).setString( 'emailAddress',
                    address ).setString( 'statusIndicator', 'A' ).list()[0]
        }
        return personEmail
    }


    public static List fetchByPidmAndStatusAndWebDisplay(Integer pidm, String statusIndicator,
                                                         String displayWebIndicator) {

        def emails = PersonEmail.withSession { session ->
            session.getNamedQuery('PersonEmail.fetchByPidmAndStatusAndWebDisplay').setCacheable(true).setInteger('pidm', pidm).setString('statusIndicator', statusIndicator).setString('displayWebIndicator', displayWebIndicator).list()
        }
        log.debug "Executing fetchByPidmAndStatusAndWebDisplayAndPreferredIndiator  with pidm = ${pidm} and status = ${statusIndicator} and displayWebIndicator = ${displayWebIndicator}"
        log.debug "Fetched number of emails ${emails.size()}"
        return emails
    }


    public static List fetchListByPidmAndEmailTypes(Integer pidm, def emailTypes) {
        def emails = PersonEmail.withSession { session ->
            session.getNamedQuery('PersonEmail.fetchListByPidmAndEmailTypes').setCacheable(true).setInteger('pidm', pidm).setParameterList('emailTypes', emailTypes).list()
        }

        return emails
    }


    public static List fetchByPidmsAndActiveStatus(List pidm) {
        def partitionList = SystemUtility.splitList(pidm, 1000)
        def emails = []
        partitionList.each { pidmList ->
            def emailList = PersonEmail.withSession { session ->
                session.getNamedQuery('PersonEmail.fetchByPidmsAndActiveStatus').setParameterList('pidms', pidmList).list()
            }
            emails.addAll(emailList)
        }
        log.debug "Executing fetchByPidmsAndActiveStatus with pidms = ${pidm} "
        log.debug "Fetched number of emails ${emails.size()} }"
        return emails
    }

}
