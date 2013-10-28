/** *******************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of
 SunGard Higher Education and its subsidiaries. Any use of this software is limited
 solely to SunGard Higher Education licensees, and is further subject to the terms
 and conditions of one or more written license agreements between SunGard Higher
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher
 Education in the U.S.A. and/or other regions and/or countries.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import net.hedtech.banner.general.system.FgacDomain
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.service.DatabaseModifiesState
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version

/**
 * Person Identification/Name model.
 */
@Entity
@Table(name = "SV_SPRIDEN")
@NamedQueries(value = [
@NamedQuery(name = "PersonIdentificationName.fetchByBannerId",
        query = """FROM  PersonIdentificationName a
	  	                WHERE a.bannerId like :filter
	  	                and a.entityIndicator = 'P'
	  	                and a.changeIndicator is null
	  	                ORDER by a.lastName, a.firstName, a.middleName, a.bannerId  """),
@NamedQuery(name = "PersonIdentificationName.fetchByName",
        query = """FROM  PersonIdentificationName a
	  	                WHERE  ( a.lastName like :filter
	  	                OR a.searchLastName like :filter)
	  	                and a.entityIndicator = 'P'
	  	                and a.changeIndicator is null
	  	                 ORDER by a.lastName, a.firstName, a.middleName, a.bannerId  """),
@NamedQuery(name = "PersonIdentificationName.fetchPersonByBannerId",
        query = """FROM  PersonIdentificationName a
	  	                WHERE a.bannerId = :filter
	  	                and a.entityIndicator = 'P'
	  	                and a.changeIndicator is null    """),
@NamedQuery(name = "PersonIdentificationName.fetchPersonByPidm",
        query = """FROM  PersonIdentificationName a
	  	                WHERE a.pidm = :filter
	  	                and a.entityIndicator = 'P'
	  	                and a.changeIndicator is null    """),
@NamedQuery(name = "PersonIdentificationName.fetchPersonOrNonPersonByPidm",
        query = """FROM  PersonIdentificationName a
	  	                WHERE a.pidm = :filter
	  	                and a.changeIndicator is null    """),
@NamedQuery(name = "PersonIdentificationName.fetchPersonOrNonPersonByBannerId",
        query = """FROM  PersonIdentificationName a
                        WHERE (a.pidm in (SELECT Y.pidm FROM PersonIdentificationName Y WHERE Y.bannerId =  :filter  AND Y.changeIndicator = 'I')
                        or a.pidm in (SELECT Z.pidm FROM PersonIdentificationName Z WHERE Z.bannerId = :filter AND Z.changeIndicator is NULL))
                       and a.changeIndicator is null
	  	               ORDER by a.lastName, a.firstName, a.middleName, a.bannerId """),
@NamedQuery(name = "PersonIdentificationName.fetchAllPersonByNameOrBannerId",
        query = """FROM  PersonIdentificationName a
	  	                WHERE (a.bannerId like :filter
	  	                or a.searchFirstName like :filter
	  	                or a.searchMiddleName like :filter
	  	                or a.searchLastName like :filter)
	  	                and a.entityIndicator = 'P' order by lastName, firstName, middleName, bannerId  """),
@NamedQuery(name = "PersonIdentificationName.fetchAllPersonByBannerId",
        query = """FROM  PersonIdentificationName a
	  	                WHERE a.bannerId like :filter
	  	                and a.entityIndicator = 'P' order by lastName, firstName, middleName, bannerId  """),
@NamedQuery(name = "PersonIdentificationName.fetchAllPersonByLastName",
        query = """FROM  PersonIdentificationName a
	  	                WHERE a.searchLastName like :filter
	  	                and a.entityIndicator = 'P' order by lastName, firstName, middleName, bannerId  """),
@NamedQuery(name = "PersonIdentificationName.fetchCountPersonByNameOrBannerId",
        query = """select count(a.bannerId) FROM  PersonIdentificationName a
	  	                WHERE (a.bannerId like :filter
	  	                or a.searchFirstName like :filter
	  	                or a.searchMiddleName like :filter
	  	                or a.searchLastName like :filter)
	  	                and a.entityIndicator = 'P' """),
@NamedQuery(name = "PersonIdentificationName.fetchCountPersonByBannerId",
        query = """select count(a.bannerId) FROM  PersonIdentificationName a
	  	                WHERE a.bannerId like :filter
	  	                and a.entityIndicator = 'P' """),
@NamedQuery(name = "PersonIdentificationName.fetchCountPersonByLastName",
        query = """select count(a.bannerId) FROM  PersonIdentificationName a
	  	                WHERE a.searchLastName like :filter
	  	                and a.entityIndicator = 'P' """),
@NamedQuery(name = "PersonIdentificationName.fetchAllNonPersonByNameOrBannerId",
        query = """FROM  PersonIdentificationName a
	  	                WHERE (a.bannerId like :filter
	  	                or a.searchLastName like :filter)
	  	                and a.entityIndicator = 'C' order by lastName, bannerId  """),
@NamedQuery(name = "PersonIdentificationName.fetchAllNonPersonByBannerId",
        query = """FROM  PersonIdentificationName a
	  	                WHERE a.bannerId like :filter
	  	                and a.entityIndicator = 'C' order by lastName, bannerId  """),
@NamedQuery(name = "PersonIdentificationName.fetchAllNonPersonByLastName",
        query = """FROM  PersonIdentificationName a
	  	                WHERE a.searchLastName like :filter
	  	                and a.entityIndicator = 'C' order by lastName, bannerId  """),
@NamedQuery(name = "PersonIdentificationName.fetchCountNonPersonByNameOrBannerId",
        query = """select count(a.bannerId) FROM  PersonIdentificationName a
	  	                WHERE (a.bannerId like :filter
	  	                or a.searchLastName like :filter)
	  	                and a.entityIndicator = 'C' """),
@NamedQuery(name = "PersonIdentificationName.fetchCountNonPersonByBannerId",
        query = """select count(a.bannerId) FROM  PersonIdentificationName a
	  	                WHERE a.bannerId like :filter
	  	                and a.entityIndicator = 'C' """),
@NamedQuery(name = "PersonIdentificationName.fetchCountNonPersonByLastName",
        query = """select count(a.bannerId) FROM  PersonIdentificationName a
	  	                WHERE a.searchLastName like :filter
	  	                and a.entityIndicator = 'C' """),
@NamedQuery(name = "PersonIdentificationName.fetchPersonByAlternativeBannerId",
        query = """FROM PersonIdentificationName a
                        WHERE a.bannerId = :filter
                        and a.entityIndicator = 'P'
                        and a.changeIndicator = 'I' """)
])
@DatabaseModifiesState
class PersonIdentificationName implements Serializable {

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
     * ORIGIN: The name of the Banner Object that was used most recently to update the row in the spren table.
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

    @Column(name = "FULL_NAME", nullable = true)
    String fullName

    public static readonlyProperties = ['pidm']


    public String toString() {
        """PersonIdentificationName[
					id=$id,
					pidm=$pidm,
					bannerId=$bannerId,
					lastName=$lastName,
					firstName=$firstName,
					mdleInitial=$middleName,
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
					fgacDomain=$fgacDomain]"""
    }



    boolean equals(o) {
        if (this.is(o)) return true;

        if (getClass() != o.class) return false;

        PersonIdentificationName that = (PersonIdentificationName) o;

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
        changeIndicator(nullable: true, maxSize: 1, inList: ["I", "D", "N"])
        entityIndicator(nullable: true, maxSize: 1, inList: ["P", "C"])
        lastModified(nullable: true)
        userData(nullable: true, maxSize: 30)
        origin(nullable: true, maxSize: 30)
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
        /**
         * Please put all the custom tests in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(personentificationname_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }

    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personentificationname_custom_methods) ENABLED START*/
    // methods used in id and name search lookups
    public static List fetchByBannerIdAutoComplete(filter) {

        return []
    }


    public static List fetchByBannerId(filter) {

        def queryCriteria
        if (filter) queryCriteria = "%" + filter.toUpperCase() + "%"
        else queryCriteria = "%"
        def names = PersonIdentificationName.withSession {session ->
            session.getNamedQuery('PersonIdentificationName.fetchByBannerId').setString('filter', queryCriteria).list()
        }
        return names
    }


    public static List fetchByName(filter) {

        def queryCriteria
        if (filter) queryCriteria = "%" + filter.toUpperCase() + "%"
        else queryCriteria = "%"
        def names = PersonIdentificationName.withSession {session ->
            session.getNamedQuery('PersonIdentificationName.fetchByName').setString('filter', queryCriteria).list()
        }
        return names
    }
    //Used for Lookup.
    public static def fetchBySomeBannerId() {
        return [list: []]
    }

    //Used for Lookup.
    public static def fetchBySomeBannerId(filter) {
        if ("%".equals(filter) || "%%".equals(filter)) {
            return [list: []]
        }
        def name
        if (filter) name = "%" + filter.toUpperCase() + "%"
        def returnObj = [list: PersonIdentificationName.fetchByBannerId(name)]
        return returnObj
    }

    //Used for Lookup.
    public static def fetchBySomeName() {
        return [list: []]
    }

    //Used for Lookup.
    public static def fetchBySomeName(filter) {
        if ("%".equals(filter) || "%%".equals(filter)) {
            return [list: []]
        }
        def name
        if (filter) name = "%" + filter.toUpperCase() + "%"
        def returnObj = [list: PersonIdentificationName.fetchByName(name)]
        return returnObj
    }

    ///Used for Lookup.
    public static Object fetchValidateBannerId(String bannerId) {
        def object = PersonIdentificationName.fetchBannerPerson(bannerId)
        return object
    }

    // Method used in utils to validate and return the name
    public static PersonIdentificationName fetchBannerPerson(String bannerId) {
        PersonIdentificationName object = PersonIdentificationName.withSession {session ->
            def list = session.getNamedQuery('PersonIdentificationName.fetchPersonByBannerId').setString('filter', bannerId).list()[0]
        }
        return object
    }
    //Used to fetch the Banner Alternate ID
    public static PersonIdentificationName fetchPersonByAlternativeBannerId(String bannerId) {
        PersonIdentificationName object = PersonIdentificationName.withSession {session ->
            def list = session.getNamedQuery('PersonIdentificationName.fetchPersonByAlternativeBannerId').setString('filter', bannerId).list()[0]
        }
        return object
    }

    public static PersonIdentificationName fetchBannerPerson(Integer pidm) {
        PersonIdentificationName object = PersonIdentificationName.withSession {session ->
            session.getNamedQuery('PersonIdentificationName.fetchPersonByPidm').setInteger('filter', pidm).list()[0]
        }
        return object
    }


    public static PersonIdentificationName fetchPersonOrNonPersonByPidm(Integer pidm) {
        PersonIdentificationName object = PersonIdentificationName.withSession {session ->
            session.getNamedQuery('PersonIdentificationName.fetchPersonOrNonPersonByPidm').setInteger('filter', pidm).list()[0]
        }
        return object
    }

    //Person Lookup Related Named Queries   and helper methods
    public static List fetchAllPersonByNameOrBannerId(filter, pagingAndSortParams) {
        def queryCriteria
        if (!filter) return []
        queryCriteria = filter.toUpperCase() + "%"
        def persons = PersonIdentificationName.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonIdentificationName.fetchAllPersonByNameOrBannerId').setString('filter', queryCriteria)
            query.setMaxResults(pagingAndSortParams.max)
            query.setFirstResult(pagingAndSortParams.offset)
            query.list()
        }
        return persons
    }


    public static List fetchAllPersonByBannerId(filter, pagingAndSortParams) {
        def queryCriteria
        if (!filter) return []
        queryCriteria = filter.toUpperCase() + "%"
        def persons = PersonIdentificationName.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonIdentificationName.fetchAllPersonByBannerId').setString('filter', queryCriteria)
            query.setMaxResults(pagingAndSortParams.max)
            query.setFirstResult(pagingAndSortParams.offset)
            query.list()
        }
        return persons
    }


    public static List fetchAllPersonByLastName(filter, pagingAndSortParams) {
        def queryCriteria
        if (!filter) return []
        queryCriteria = filter.toUpperCase() + "%"
        def persons = PersonIdentificationName.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonIdentificationName.fetchAllPersonByLastName').setString('filter', queryCriteria)
            query.setMaxResults(pagingAndSortParams.max)
            query.setFirstResult(pagingAndSortParams.offset)
            query.list()
        }
        return persons
    }



    public static int fetchCountPersonByNameOrBannerId(filter) {
        def queryCriteria
        if (!filter) return 0
        queryCriteria = filter.toUpperCase() + "%"
        def count = PersonIdentificationName.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonIdentificationName.fetchCountPersonByNameOrBannerId').setString('filter', queryCriteria)
            query.list().get(0)
        }
        return count
    }


    public static int fetchCountPersonByBannerId(filter) {
        def queryCriteria
        if (!filter) return 0
        queryCriteria = filter.toUpperCase() + "%"
        def count = PersonIdentificationName.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonIdentificationName.fetchCountPersonByBannerId').setString('filter', queryCriteria)
            query.list().get(0)
        }
        return count
    }


    public static int fetchCountPersonByLastName(filter) {
        def queryCriteria
        if (!filter) return 0
        queryCriteria = filter.toUpperCase() + "%"
        def count = PersonIdentificationName.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonIdentificationName.fetchCountPersonByLastName').setString('filter', queryCriteria)
            query.list().get(0)
        }
        return count
    }



    public static List fetchAllNonPersonByNameOrBannerId(filter, pagingAndSortParams) {
        def queryCriteria
        if (!filter) return []
        queryCriteria = filter.toUpperCase() + "%"
        def nonPersons = PersonIdentificationName.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonIdentificationName.fetchAllNonPersonByNameOrBannerId').setString('filter', queryCriteria)
            query.setMaxResults(pagingAndSortParams.max);
            query.setFirstResult(pagingAndSortParams.offset);
            query.list()
        }
        return nonPersons
    }


    public static List fetchAllNonPersonByBannerId(filter, pagingAndSortParams) {
        def queryCriteria
        if (!filter) return []
        queryCriteria = filter.toUpperCase() + "%"
        def nonPersons = PersonIdentificationName.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonIdentificationName.fetchAllNonPersonByBannerId').setString('filter', queryCriteria)
            query.setMaxResults(pagingAndSortParams.max);
            query.setFirstResult(pagingAndSortParams.offset);
            query.list()
        }
        return nonPersons
    }


    public static List fetchAllNonPersonByLastName(filter, pagingAndSortParams) {
        def queryCriteria
        if (!filter) return []
        queryCriteria = filter.toUpperCase() + "%"
        def nonPersons = PersonIdentificationName.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonIdentificationName.fetchAllNonPersonByLastName').setString('filter', queryCriteria)
            query.setMaxResults(pagingAndSortParams.max);
            query.setFirstResult(pagingAndSortParams.offset);
            query.list()
        }
        return nonPersons
    }



    public static int fetchCountNonPersonByNameOrBannerId(filter) {
        def queryCriteria
        if (!filter) return 0
        queryCriteria = filter.toUpperCase() + "%"
        def count = PersonIdentificationName.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonIdentificationName.fetchCountNonPersonByNameOrBannerId').setString('filter', queryCriteria)
            query.list().get(0)
        }
        return count
    }


    public static int fetchCountNonPersonByBannerId(filter) {
        def queryCriteria
        if (!filter) return 0
        queryCriteria = filter.toUpperCase() + "%"
        def count = PersonIdentificationName.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonIdentificationName.fetchCountNonPersonByBannerId').setString('filter', queryCriteria)
            query.list().get(0)
        }
        return count
    }


    public static int fetchCountNonPersonByLastName(filter) {
        def queryCriteria
        if (!filter) return 0
        queryCriteria = filter.toUpperCase() + "%"
        def count = PersonIdentificationName.withSession {session ->
            org.hibernate.Query query = session.getNamedQuery('PersonIdentificationName.fetchCountNonPersonByLastName').setString('filter', queryCriteria)
            query.list().get(0)
        }
        return count
    }


    public static PersonIdentificationName fetchBannerPersonOrNonPerson(String bannerId) {
        PersonIdentificationName object = PersonIdentificationName.withSession {session ->
            session.getNamedQuery('PersonIdentificationName.fetchPersonOrNonPersonByBannerId').setString('filter', bannerId).list()[0]
        }
        return object
    }

    //Used for spriden id Lookup.
    public static Map fetchAllForBannerId(filter) {
        return [list: []]
    }

    //Used for spriden id Lookup.
    public static Map fetchAllForBannerId() {
        return [list: []]
    }

    //Used for spriden id Lookup.
    public static Map fetchPersonByNameOrBannerId(filter, pagingAndSortParams) {
        return [list: PersonIdentificationName.fetchAllPersonByNameOrBannerId(filter, pagingAndSortParams), totalCount: PersonIdentificationName.fetchCountPersonByNameOrBannerId(filter)]
    }


    public static Map fetchPersonByBannerId(filter = null, pagingAndSortParams) {
        if(filter)
            return [list: PersonIdentificationName.fetchAllPersonByBannerId(filter, pagingAndSortParams), totalCount: PersonIdentificationName.fetchCountPersonByBannerId(filter)]
        return [list:[]]
    }


    public static Map fetchPersonByLastName(filter = null, pagingAndSortParams) {
        if(filter)
            return [list: PersonIdentificationName.fetchAllPersonByLastName(filter, pagingAndSortParams), totalCount: PersonIdentificationName.fetchCountPersonByLastName(filter)]
        return [list:[]]
    }


    //Used for spriden id Lookup.
    public static Map fetchPersonByNameOrBannerId(pagingAndSortParams) {
        //Reason is person lookup will have performance issues when filter is unavailable
        return [list: []]
    }


    //Used for spriden id Lookup.
    public static Map fetchNonPersonByNameOrBannerId(filter, pagingAndSortParams) {
        return [list: PersonIdentificationName.fetchAllNonPersonByNameOrBannerId(filter, pagingAndSortParams), totalCount: PersonIdentificationName.fetchCountNonPersonByNameOrBannerId(filter)]
    }


    public static Map fetchNonPersonByBannerId(filter = null, pagingAndSortParams) {
        if(filter)
            return [list: PersonIdentificationName.fetchAllNonPersonByBannerId(filter, pagingAndSortParams), totalCount: PersonIdentificationName.fetchCountNonPersonByBannerId(filter)]
        return [list:[]]
    }


    public static Map fetchNonPersonByLastName(filter = null, pagingAndSortParams) {
        if(filter)
            return [list: PersonIdentificationName.fetchAllNonPersonByLastName(filter, pagingAndSortParams), totalCount: PersonIdentificationName.fetchCountNonPersonByLastName(filter)]
        return [list:[]]
    }


    //Used for spriden id Lookup.
    public static Map fetchNonPersonByNameOrBannerId(pagingAndSortParams) {
        //Reason is person lookup will have performance issues when filter is unavailable
        return [list: []]
    }


    /*PROTECTED REGION END*/
}
