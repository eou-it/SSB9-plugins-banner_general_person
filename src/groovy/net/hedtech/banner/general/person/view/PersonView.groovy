package net.hedtech.banner.general.person.view

import grails.util.Holders

/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

import javax.persistence.*
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder as LCH
import net.hedtech.banner.person.dsl.NameTemplate

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
    String id

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
    @Temporal(TemporalType.DATE)
    Date birthDate

    /**
     * The surname prefix of the person
     */
    @Column(name = "SURNAME_PREFIX", length = 60)
    String surnamePrefix

    /**
     * The preferred first name of the person
     */
    @Column(name = "PREFERRED_FIRST_NAME", length = 60)
    String preferredFirstName

    /**
     * The name prefix of the person
     */
    @Column(name = "NAME_PREFIX", length = 20)
    String namePrefix

    /**
     * The name suffix of the person
     */
    @Column(name = "NAME_SUFFIX", length = 20)
    String nameSuffix

    @Transient
    public String formattedName

    public String getFormattedName() {
        return NameTemplate.format {
            lastName lastName
            firstName firstName
            mi middleName
            surnamePrefix surnamePrefix
            nameSuffix nameSuffix
            namePrefix namePrefix
            formatTemplate getNameFormat()
            text
        }
    }

    public def getNameFormat() {
        def application = Holders.getGrailsApplication()
        ApplicationContext applicationContext = application.mainContext
        def messageSource = applicationContext.getBean("messageSource")
        messageSource.getMessage("default.name.format", null, LCH.getLocale())
    }


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
					birthDate=$birthDate
					surnamePrefix=$surnamePrefix,
					preferredFirstName=$preferredFirstName,
					namePrefix=$namePrefix
					nameSuffix=$nameSuffix]"""
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
