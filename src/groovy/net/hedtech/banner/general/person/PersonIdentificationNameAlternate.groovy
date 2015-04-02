/*********************************************************************************
 Copyright 2013-2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import net.hedtech.banner.general.system.FgacDomain
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.service.DatabaseModifiesState

import javax.persistence.*

/**
 * Person Identification/Name alternate identification model.
 *
 * NOTE: This domain is NOT used to maintain the current SPRIDEN record
 * and will never operate on a SPRIDEN record that has a NULL value in the
 * change indicator.
 */
@Entity
@Table(name = "SV_SPRIDEN_ALT")
@NamedQueries(value = [
@NamedQuery(name = "PersonIdentificationNameAlternate.fetchAllByPidm",
query = """FROM  PersonIdentificationNameAlternate a
	       WHERE a.pidm = :pidm """),
@NamedQuery(name = "PersonIdentificationNameAlternate.fetchAllIdChangesByBannerId",
query = """FROM  PersonIdentificationNameAlternate a
	       WHERE a.bannerId = :bannerId
	         AND a.changeIndicator = 'I' """),
@NamedQuery(name = "PersonIdentificationNameAlternate.fetchByPidmAndNameType",
query = """FROM  PersonIdentificationNameAlternate a
             WHERE a.pidm IN :pidms
             AND a.nameType.code = :nameType
	         AND a.changeIndicator = 'N'
	         order by createDate desc""")
])
@DatabaseModifiesState
class PersonIdentificationNameAlternate implements Serializable {

    /**
     * Surrogate ID for SPRIDEN
     */
    @Id
    @Column(name = "SPRIDEN_SURROGATE_ID")
    @SequenceGenerator(name = "SPRIDEN_SEQ_GEN", allocationSize = 1, sequenceName = "SPRIDEN_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPRIDEN_SEQ_GEN")
    Long id

    /**
     * Internal identification number of the person.  This needs to be nullable = true
     * because it is generated by the API
     */
    @Column(name = "SPRIDEN_PIDM", length = 22)
    Integer pidm

    /**
     * This field defines the identification number used to access person on-line.  This needs to be nullable = true
     * because it is generated by the API
     */
    @Column(name = "SPRIDEN_ID", length = 9)
    String bannerId

    /**
     * This field defines the last name of person.
     */
    @Column(name = "SPRIDEN_LAST_NAME", nullable = false, length = 60)
    String lastName

    /**
     * This field entifies the first name of person.
     */
    @Column(name = "SPRIDEN_FIRST_NAME", length = 60)
    String firstName

    /**
     * This field entifies the mdle name of person.
     */
    @Column(name = "SPRIDEN_MI", length = 60)
    String middleName

    /**
     * This field entifies whether type of change made to the record was an ID       number change or a name change. Val values: I - ID change, N - name change.
     */
    @Column(name = "SPRIDEN_CHANGE_IND", length = 1)
    String changeIndicator

    /**
     * This field entifies whether record is person or non-person record.  It does   not display on the form. Val values:  P - person, C - non-person.
     */
    @Column(name = "SPRIDEN_ENTITY_IND", length = 1)
    String entityIndicator

    /**
     * This field defines the most current date record is created or changed.
     */
    @Column(name = "SPRIDEN_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * USER: The ID for the user that most recently updated the record.
     */
    @Column(name = "SPRIDEN_USER", length = 30)
    String userData

    /**
     * ORIGIN: The name of the Banner Object that was used most recently to update the row in the spriden table.
     */
    @Column(name = "SPRIDEN_ORIGIN", length = 30)
    String origin

    /**
     * The Last Name field with all spaces and punctuation removed and all letters capitalized.
     */
    @Column(name = "SPRIDEN_SEARCH_LAST_NAME", length = 60)
    String searchLastName

    /**
     * The First Name field with all spaces and punctuation removed and all letters capitalized.
     */
    @Column(name = "SPRIDEN_SEARCH_FIRST_NAME", length = 60)
    String searchFirstName

    /**
     * The MI (Mdle Initial) field with all spaces and punctuation removed and all letters capitalized.
     */
    @Column(name = "SPRIDEN_SEARCH_MI", length = 60)
    String searchMiddleName

    /**
     * The Last Name field in SOUNDEX phonetic format.
     */
    @Column(name = "SPRIDEN_SOUNDEX_LAST_NAME", length = 4)
    String soundexLastName

    /**
     * The First Name field in SOUNDEX phonetic format.
     */
    @Column(name = "SPRIDEN_SOUNDEX_FIRST_NAME", length = 4)
    String soundexFirstName

    /**
     * Record Create User: This field contains Banner User ID which created new record
     */
    @Column(name = "SPRIDEN_CREATE_USER", length = 30)
    String createUser

    /**
     * Record Create Date: This field contains date new record created
     */
    @Column(name = "SPRIDEN_CREATE_DATE")
    @Temporal(TemporalType.DATE)
    Date createDate

    /**
     * DATA SOURCE: Source system that generated the data
     */
    @Column(name = "SPRIDEN_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * SURNAME PREFIX: Name tag preceding the last name or surname.  (Van, Von, Mac, etc.)
     */
    @Column(name = "SPRIDEN_SURNAME_PREFIX", length = 60)
    String surnamePrefix

    /**
     * Version column which is used as a optimistic lock token for SPRIDEN
     */
    @Version
    @Column(name = "SPRIDEN_VERSION", length = 19)
    Long version

    /**
     * Last Modified By column for SPRIDEN
     */
    @Column(name = "SPRIDEN_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Foreign Key : FK1_SPRIDEN_INV_GTVNTYP_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPRIDEN_NTYP_CODE", referencedColumnName = "GTVNTYP_CODE")
    ])
    NameType nameType

    /**
     * Foreign Key : FKV_SPRIDEN_INV_STVCREA_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPRIDEN_CREATE_FDMN_CODE", referencedColumnName = "GTVFDMN_CODE")
    ])
    FgacDomain fgacDomain

    @Column(name = "FULL_NAME", updatable = false, insertable = false)
    String fullName

    @Column(name = "SPRIDEN_V_ROWID", updatable = false, insertable = false)
    String personRowid

    public static readonlyProperties = [
            'pidm',
            'changeIndicator',
            'createUser',
            'createDate',
            'searchLastName',
            'searchFirstName',
            'searchMiddleName',
            'soundexLastName',
            'soundexFirstName',
            'fgacDomain',
            'fullName',
            'personRowid']


    public String toString() {
        """PersonIdentificationName[
					id=$id,
					pidm=$pidm,
					bannerId=$bannerId,
					lastName=$lastName,
					firstName=$firstName,
					middleName=$middleName,
					changeIndicator=$changeIndicator,
					entityIndicator=$entityIndicator,
					lastModified=$lastModified,
					userData=$userData,
					origin=$origin,
					searchLastName=$searchLastName,
					searchFirstName=$searchFirstName,
					searchMiddleName=$searchMiddleName,
					soundexLastName=$soundexLastName,
					soundexFirstName=$soundexFirstName,
					createUser=$createUser,
					createDate=$createDate,
					dataOrigin=$dataOrigin,
					surnamePrefix=$surnamePrefix,
					version=$version,
					lastModifiedBy=$lastModifiedBy,
					nameType=$nameType,
					fullName=$fullName,
                    personRowid=$personRowid,
					fgacDomain=$fgacDomain]"""
    }



    boolean equals(o) {
        if (this.is(o)) return true;

        if (getClass() != o.class) return false;

        PersonIdentificationNameAlternate that = (PersonIdentificationNameAlternate) o;

        if (bannerId != that.bannerId) return false;
        if (changeIndicator != that.changeIndicator) return false;
        if (createDate != that.createDate) return false;
        if (createUser != that.createUser) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (entityIndicator != that.entityIndicator) return false;
        if (fgacDomain != that.fgacDomain) return false;
        if (firstName != that.firstName) return false;
        if (fullName != that.fullName) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (lastName != that.lastName) return false;
        if (middleName != that.middleName) return false;
        if (nameType != that.nameType) return false;
        if (origin != that.origin) return false;
        if (pidm != that.pidm) return false;
        if (personRowid != that.personRowid) return false;
        if (searchFirstName != that.searchFirstName) return false;
        if (searchLastName != that.searchLastName) return false;
        if (searchMiddleName != that.searchMiddleName) return false;
        if (soundexFirstName != that.soundexFirstName) return false;
        if (soundexLastName != that.soundexLastName) return false;
        if (surnamePrefix != that.surnamePrefix) return false;
        if (userData != that.userData) return false;
        if (version != that.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (pidm != null ? pidm.hashCode() : 0);
        result = 31 * result + (bannerId != null ? bannerId.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (changeIndicator != null ? changeIndicator.hashCode() : 0);
        result = 31 * result + (entityIndicator != null ? entityIndicator.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (userData != null ? userData.hashCode() : 0);
        result = 31 * result + (origin != null ? origin.hashCode() : 0);
        result = 31 * result + (personRowid != null ? personRowid.hashCode() : 0);
        result = 31 * result + (searchLastName != null ? searchLastName.hashCode() : 0);
        result = 31 * result + (searchFirstName != null ? searchFirstName.hashCode() : 0);
        result = 31 * result + (searchMiddleName != null ? searchMiddleName.hashCode() : 0);
        result = 31 * result + (soundexLastName != null ? soundexLastName.hashCode() : 0);
        result = 31 * result + (soundexFirstName != null ? soundexFirstName.hashCode() : 0);
        result = 31 * result + (createUser != null ? createUser.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        result = 31 * result + (surnamePrefix != null ? surnamePrefix.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (nameType != null ? nameType.hashCode() : 0);
        result = 31 * result + (fgacDomain != null ? fgacDomain.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        return result;
    }

//  The Banner ID and PIDM are nullable: true because the API generates these
    static constraints = {
        pidm(nullable: true)
        lastName(nullable: false, maxSize: 60)
        firstName(nullable: true, maxSize: 60)
        middleName(nullable: true, maxSize: 60)
        changeIndicator(nullable: false, maxSize: 1, inList: ["I", "N"])
        entityIndicator(nullable: true, maxSize: 1, inList: ["P", "C"])
        lastModified(nullable: true)
        userData(nullable: true, maxSize: 30)
        origin(nullable: true, maxSize: 30)
        personRowid(nullable: true)
        searchLastName(nullable: true, maxSize: 60)
        searchFirstName(nullable: true, maxSize: 60)
        searchMiddleName(nullable: true, maxSize: 60)
        soundexLastName(nullable: true, maxSize: 4)
        soundexFirstName(nullable: true, maxSize: 4)
        createUser(nullable: true, maxSize: 30)
        createDate(nullable: true)
        dataOrigin(nullable: true, maxSize: 30)
        surnamePrefix(nullable: true, maxSize: 60)
        lastModifiedBy(nullable: true, maxSize: 30)
        bannerId(nullable: true)
        nameType(nullable: true)
        fgacDomain(nullable: true)
        fullName(nullable: true)
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
        def personIdentificationNameAlternates = finderByAll().find(filterData, pagingAndSortParams)
        return personIdentificationNameAlternates
    }


    def private static finderByAll = {
        def query = """FROM PersonIdentificationNameAlternate a WHERE a.pidm = :pidm"""
        return new DynamicFinder(PersonIdentificationNameAlternate.class, query, "a")
    }


    def static fetchAllByPidm(Integer pidm) {
        def personIdentificationNameAlternates = []

        if (pidm) {
            personIdentificationNameAlternates = PersonIdentificationNameAlternate.withSession { session ->
                def list = session.getNamedQuery('PersonIdentificationNameAlternate.fetchAllByPidm').setInteger('pidm', pidm).list()
            }
        }

        return personIdentificationNameAlternates
    }


    def static fetchAllIdChangesByBannerId(String bannerId) {
        def personIdentificationNameAlternates = []

        if (bannerId) {
            personIdentificationNameAlternates = PersonIdentificationNameAlternate.withSession { session ->
                def list = session.getNamedQuery('PersonIdentificationNameAlternate.fetchAllIdChangesByBannerId').setString('bannerId', bannerId).list()
            }
        }

        return personIdentificationNameAlternates
    }


    def static fetchAllByPidmsAndNameType(List pidms, String nameType) {
        List<PersonIdentificationNameAlternate> personIdentificationNameAlternates = PersonIdentificationNameAlternate.withSession { session ->
            session.getNamedQuery('PersonIdentificationNameAlternate.fetchByPidmAndNameType').setParameterList('pidms', pidms).setString('nameType', nameType)?.list()
        }

        return personIdentificationNameAlternates
    }

}
