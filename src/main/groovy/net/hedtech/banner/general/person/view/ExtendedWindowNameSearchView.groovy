/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person.view

import grails.util.Holders
import net.hedtech.banner.general.person.PersonUtility

import javax.persistence.*
import net.hedtech.banner.person.dsl.NameTemplate
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder

@Entity
@Table(name = "SVQ_EXTNAMESRCH")
class ExtendedWindowNameSearchView {


    @Column(name = "ROW_COUNT")
    Long rowCount

    @Id
    @Column(name = "SURROGATE_ID")
    String id

    /**
     * This field defines the identification number used to access person on-line.  This needs to be nullable = true
     * because it is generated by the API
     */
    @Column(name = "ID")
    String bannerId

    /**
     * This field defines the current identification number.
     */
    @Column(name = "CURRENT_ID")
    String currentBannerId
    /**
     * This field defines whether type of change made to the record was an ID number change or a name change. Val values: I - ID change, N - name change.
     */
    @Column(name = "CHANGE_INDICATOR", length = 1)
    String changeIndicator

    /**
     * This field defines whether record is person or non-person record.  It does not display on the form. Val values:  P - person, C - non-person.
     */
    @Column(name = "ENTITY_INDICATOR", length = 1)
    String entityIndicator

    /**
     * The Last Name field with all spaces and punctuation removed and all letters capitalized.
     */
    @Column(name = "SEARCH_LAST_NAME", length = 60)
    String searchLastName

    /**
     * The First Name field with all spaces and punctuation removed and all letters capitalized.
     */
    @Column(name = "SEARCH_FIRST_NAME", length = 60)
    String searchFirstName

    /**
     * The MI (Middle Initial) field with all spaces and punctuation removed and all letters capitalized.
     */
    @Column(name = "SEARCH_MI", length = 60)
    String searchMiddleName

    /**
     * The ssn of the person
     */
    @Column(name = "SSN", length = 15)
    String ssn

    /**
     * The name type of the person
     */
    @Column(name = "NAME_TYPE")
    String nameType

    /**
     * The sex of the person
     */
    @Column(name = "SEX")
    String sex

    /**
     * This field defines the last name of person.
     */
    @Column(name = "CURRENT_LAST_NAME")
    String currentLastName

    /**
     * This field entifies the first name of person.
     */
    @Column(name = "CURRENT_FIRST_NAME")
    String currentFirstName

    /**
     * This field entifies the mdle name of person.
     */
    @Column(name = "CURRENT_MI")
    String currentMiddleName

    /**
     * The surname prefix of the person
     */
    @Column(name = "CURRENT_SURNAME_PREFIX", length = 60)
    String currentSurnamePrefix

    /**
     * Birth Date
     */
    @Column(name = "BIRTH_DATE")
    @Temporal(TemporalType.DATE)
    Date birthDate

    /**
     * DATA SOURCE: Source system that generated the data
     */
    @Column(name = "DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * This field defines the most current date record is created or changed.
     */
    @Column(name = "ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

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
     * Internal identification number of the person.  This needs to be nullable = true
     * because it is generated by the API
     */
    @Column(name = "PIDM")
    Integer pidm

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
        if (changeIndicator != null) {
            return PersonUtility.formatName(this)
        } else {
            return getDisplayName()
        }

    }

    public String getBaseFormattedName() {
        return getDisplayName()
    }

    public String getDisplayName() {
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
        messageSource.getMessage("default.name.format", null, LocaleContextHolder.getLocale())
    }



    @Override
    public String toString() {
        return "ExtendedWindowNameSearchView{" +
                "rowCount=" + rowCount +
                ", id='" + id + '\'' +
                ", bannerId='" + bannerId + '\'' +
                ", currentBannerId='" + currentBannerId + '\'' +
                ", changeIndicator='" + changeIndicator + '\'' +
                ", entityIndicator='" + entityIndicator + '\'' +
                ", searchLastName='" + searchLastName + '\'' +
                ", searchFirstName='" + searchFirstName + '\'' +
                ", searchMiddleName='" + searchMiddleName + '\'' +
                ", ssn='" + ssn + '\'' +
                ", nameType='" + nameType + '\'' +
                ", sex='" + sex + '\'' +
                ", currentLastName='" + currentLastName + '\'' +
                ", currentFirstName='" + currentFirstName + '\'' +
                ", currentMiddleName='" + currentMiddleName + '\'' +
                ", currentSurnamePrefix='" + currentSurnamePrefix + '\'' +
                ", birthDate=" + birthDate +
                ", dataOrigin='" + dataOrigin + '\'' +
                ", lastModified=" + lastModified +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", pidm=" + pidm +
                ", version=" + version +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", surnamePrefix='" + surnamePrefix + '\'' +
                ", preferredFirstName='" + preferredFirstName + '\'' +
                ", namePrefix='" + namePrefix + '\'' +
                ", nameSuffix='" + nameSuffix + '\'' +
                ",formattedName'" + formattedName + '\'' +
                '}';
    }


}
