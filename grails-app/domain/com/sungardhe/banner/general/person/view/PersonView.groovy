package com.sungardhe.banner.general.person.view

/** *******************************************************************************
 Copyright 2009-2012 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of
 SunGard Higher Education and its subsidiaries. Any use of this software is limited
 solely to SunGard Higher Education licensees, and is further subject to the terms
 and conditions of one or more written license agreements between SunGard Higher
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher
 Education in the U.S.A. and/or other regions and/or countries.
 ********************************************************************************* */

import javax.persistence.*

/**
 * Person Identification/Name model.
 */

@MappedSuperclass
abstract class PersonView implements Serializable {

    /**
     * Surrogate ID for SPRIDEN
     */
    @Id
    @Column(name = "SURROGATE_ID")
    Long id

    /**
     * Internal identification number of the person.  This needs to be nullable = true
     * because it is generated by the API
     */
    @Column(name = "PIDM")
    Integer pidm

    /**
     * This field defines the identification number used to access person on-line.  This needs to be nullable = true
     * because it is generated by the API
     */
    @Column(name = "ID")
    String bannerId

    /**
     * This field defines the last name of person.
     */
    @Column(name = "LAST_NAME")
    String lastName

    /**
     * This field entifies the first name of person.
     */
    @Column(name = "FIRST_NAME")
    String firstName

    /**
     * This field entifies the mdle name of person.
     */
    @Column(name = "MI")
    String middleName

    /**
     * This field defines the most current date record is created or changed.
     */
    @Column(name = "ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * DATA SOURCE: Source system that generated the data
     */
    @Column(name = "DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * Version column which is used as a optimistic lock token for SPRIDEN
     */
    @Version
    @Column(name = "VERSION", length = 19)
    Long version

    /**
     * Last Modified By column for SPRIDEN
     */
    @Column(name = "USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Birth Date
     */
    @Column(name = "BIRTH_DATE")
    String birthDate


    public static readonlyProperties = ['pidm']


    public String toString() {
        """PersonView[
					id=$id,
					pidm=$pidm,
					bannerId=$bannerId,
					lastName=$lastName,
					firstName=$firstName,
					middleName=$middleName,
					lastModified=$lastModified,
					dataOrigin=$dataOrigin,
					version=$version,
					lastModifiedBy=$lastModifiedBy,
					birthDate=$birthDate]"""
    }



    boolean equals(o) {
        if (this.is(o)) return true;

        if (getClass() != o.class) return false;

        PersonView that = (PersonView) o;

        if (bannerId != that.bannerId) return false;
        if (dataOrigin != that.dataOrigin) return false;

        if (firstName != that.firstName) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (lastName != that.lastName) return false;
        if (middleName != that.middleName) return false;
        if (pidm != that.pidm) return false;
        if (version != that.version) return false;
        if (birthDate != that.birthDate) return false;

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
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);

        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);

        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);

        return result;
    }

}
