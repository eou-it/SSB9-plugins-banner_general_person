/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.general.system.*
import net.hedtech.banner.service.DatabaseModifiesState
import org.hibernate.annotations.Type

import javax.persistence.*

/**
 * Basic Person Base Table
 */
@Entity
@Table(name = "SV_SPBPERS")
@NamedQueries(value = [
@NamedQuery(name = "PersonBasicPersonBase.fetchByPidm",
query = """FROM  PersonBasicPersonBase a
	       WHERE a.pidm = :pidm """),
@NamedQuery(name = "PersonBasicPersonBase.fetchBySsn",
query = """FROM  PersonBasicPersonBase a
	       WHERE a.ssn = :ssn """),
@NamedQuery(name = "PersonBasicPersonBase.fetchByPidmList",
        query = """FROM  PersonBasicPersonBase a
	       WHERE a.pidm IN :pidm """)
])
@DatabaseModifiesState
class PersonBasicPersonBase implements Serializable {

    /**
     * Surrogate ID for SPBPERS
     */
    @Id
    @Column(name = "SPBPERS_SURROGATE_ID")
    @SequenceGenerator(name = "SPBPERS_SEQ_GEN", allocationSize = 1, sequenceName = "SPBPERS_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPBPERS_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for SPBPERS
     */
    @Version
    @Column(name = "SPBPERS_VERSION")
    Long version

    /**
     * Internal Identification Number of Person.
     */
    @Column(name = "SPBPERS_PIDM")
    Integer pidm

    /**
     * This field maintains person social security number.
     */
    @Column(name = "SPBPERS_SSN")
    String ssn

    /**
     * This field maintains person birth date.
     */
    @Column(name = "SPBPERS_BIRTH_DATE")
    @Temporal(TemporalType.DATE)
    Date birthDate

    /**
     * This field maintains the sex of person. Valid values are: M - Male, F - Female, N - Unknown.
     */
    @Column(name = "SPBPERS_SEX")
    String sex

    /**
     * This field identifies if a person record is confidential Valid value is: Y - confidential.
     */
    @Column(name = "SPBPERS_CONFID_IND")
    String confidIndicator = "N"

    /**
     * This field indicates if a person is deceased. Valid value is: Y - deceased.
     */
    @Column(name = "SPBPERS_DEAD_IND")
    String deadIndicator

    /**
     * This field maintains veteran identification number associated with person.
     */
    @Column(name = "SPBPERS_VETC_FILE_NUMBER")
    String vetcFileNumber

    /**
     * This field maintains legal name associated with person.
     */
    @Column(name = "SPBPERS_LEGAL_NAME")
    String legalName

    /**
     * This field maintains the preferred first name associated with person.
     */
    @Column(name = "SPBPERS_PREF_FIRST_NAME")
    String preferenceFirstName

    /**
     * This field maintains the prefix (Mr, Mrs, etc) used before person name.
     */
    @Column(name = "SPBPERS_NAME_PREFIX")
    String namePrefix

    /**
     * This field maintains the suffix (Jr, Sr, etc) used after person name.
     */
    @Column(name = "SPBPERS_NAME_SUFFIX")
    String nameSuffix

    /**
     * Veteran Category. None, (O)ther Protected Veteran Only, (V)ietnam Veteran only, (B)oth Vietnam and Other Eligible Veteran.
     */
    @Column(name = "SPBPERS_VERA_IND")
    String veraIndicator

    /**
     * Citizen Indicator.
     */
    @Column(name = "SPBPERS_CITZ_IND")
    String citizenshipIndicator

    /**
     * Person Deceased Date.
     */
    @Column(name = "SPBPERS_DEAD_DATE")
    @Temporal(TemporalType.DATE)
    Date deadDate

    /**
     * The hair color of the person being defined.
     */
    @Column(name = "SPBPERS_HAIR_CODE")
    String hair

    /**
     * The eye color of the person being defined.
     */
    @Column(name = "SPBPERS_EYES_CODE")
    String eyeColor

    /**
     * The city where the person was born.
     */
    @Column(name = "SPBPERS_CITY_BIRTH")
    String cityBirth

    /**
     * The Driver License Number as it appears on the actual license.
     */
    @Column(name = "SPBPERS_DRIVER_LICENSE")
    String driverLicense

    /**
     * The number value describing the height of the person.
     */
    @Column(name = "SPBPERS_HEIGHT")
    Integer height

    /**
     * The number value describing the weight of the person.
     */
    @Column(name = "SPBPERS_WEIGHT")
    Integer weight

    /**
     * Indicator to identify an individual as a special disabled veteran.
     */
    @Column(name = "SPBPERS_SDVET_IND")
    String sdvetIndicator

    /**
     * The issue date of the individuals driver license.
     */
    @Column(name = "SPBPERS_LICENSE_ISSUED_DATE")
    @Temporal(TemporalType.DATE)
    Date licenseIssuedDate

    /**
     * The expiration date of the individuals driver license.
     */
    @Column(name = "SPBPERS_LICENSE_EXPIRES_DATE")
    @Temporal(TemporalType.DATE)
    Date licenseExpiresDate

    /**
     * The indication of the individuals incarceration.
     */
    @Column(name = "SPBPERS_INCAR_IND")
    String incarcerationIndicator

    /**
     * The international tax id number.
     */
    @Column(name = "SPBPERS_ITIN")
    Integer itin

    /**
     * Active Duty Separation Date: The Date that the person was separated from active duty.
     */
    @Column(name = "SPBPERS_ACTIVE_DUTY_SEPR_DATE")
    @Temporal(TemporalType.DATE)
    Date activeDutySeprDate

    /**
     * ETHNIC CODE: This field identifies the ethnic code defined by the U.S. government. The valid values are 1 - Not Hispanic or Latino, 2 - Hispanic or Latino, or null.
     */
    @Column(name = "SPBPERS_ETHN_CDE")
    String ethnic

    /**
     * RACE AND ETHNICITY CONFIRMED: This field identifies the race and ethnicity has been confirmed. Valid values are (Y)es, (N)o and Null.
     */
    @Column(name = "SPBPERS_CONFIRMED_RE_CDE")
    String confirmedRe = "N"

    /**
     * RACE AND ETHNICITY CONFIRMED DATE: This field identifies when the race and ethnicity has been confirmed.
     */
    @Column(name = "SPBPERS_CONFIRMED_RE_DATE")
    @Temporal(TemporalType.DATE)
    Date confirmedReDate

    /**
     * ARMED FORCES SERVICE MEDAL INDICATOR:  Armed Forces Service Medal Indicator. Valid values are (Y)es, (N)o. Default value is N.
     */
    @Type(type = "yes_no")
    @Column(name = "SPBPERS_ARMED_SERV_MED_VET_IND")
    Boolean armedServiceMedalVetIndicator

    /**
     * This field defines the most current date a record is added or changed.
     */
    @Column(name = "SPBPERS_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * USER ID: User who inserted or last update the data
     */
    @Column(name = "SPBPERS_USER_ID")
    String lastModifiedBy

    /**
     * DATA ORIGIN: Source system that created or updated the row
     */
    @Column(name = "SPBPERS_DATA_ORIGIN")
    String dataOrigin

    /**
     * Foreign Key : FK1_SPBPERS_INV_STVLGCY_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPBPERS_LGCY_CODE", referencedColumnName = "STVLGCY_CODE")
    ])
    Legacy legacy

    /**
     * Foreign Key : FK1_SPBPERS_INV_STVETHN_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPBPERS_ETHN_CODE", referencedColumnName = "STVETHN_CODE")
    ])
    Ethnicity ethnicity

    /**
     * Foreign Key : FK1_SPBPERS_INV_STVMRTL_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPBPERS_MRTL_CODE", referencedColumnName = "STVMRTL_CODE")
    ])
    MaritalStatus maritalStatus

    /**
     * Foreign Key : FK1_SPBPERS_INV_STVRELG_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPBPERS_RELG_CODE", referencedColumnName = "STVRELG_CODE")
    ])
    Religion religion

    /**
     * Foreign Key : FK1_SPBPERS_INV_STVCITZ_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPBPERS_CITZ_CODE", referencedColumnName = "STVCITZ_CODE")
    ])
    CitizenType citizenType

    /**
     * Foreign Key : FK1_SPBPERS_INV_STVSTAT_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPBPERS_STAT_CODE_BIRTH", referencedColumnName = "STVSTAT_CODE")
    ])
    State stateBirth

    /**
     * Foreign Key : FK2_SPBPERS_INV_STVSTAT_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPBPERS_STAT_CODE_DRIVER", referencedColumnName = "STVSTAT_CODE")
    ])
    State stateDriver

    /**
     * Foreign Key : FK1_SPBPERS_INV_STVNATN_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPBPERS_NATN_CODE_DRIVER", referencedColumnName = "STVNATN_CODE")
    ])
    Nation nationDriver

    /**
     * Foreign Key : FK1_SPBPERS_INV_GTVUOMS_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPBPERS_UOMS_CODE_HEIGHT", referencedColumnName = "GTVUOMS_CODE")
    ])
    UnitOfMeasure unitOfMeasureHeight

    /**
     * Foreign Key : FK2_SPBPERS_INV_GTVUOMS_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "SPBPERS_UOMS_CODE_WEIGHT", referencedColumnName = "GTVUOMS_CODE")
    ])
    UnitOfMeasure unitOfMeasureWeight


    public String toString() {
        """PersonBasicPersonBase[
					id=$id, 
					version=$version, 
					pidm=$pidm, 
					ssn=$ssn, 
					birthDate=$birthDate, 
					sex=$sex, 
					confidIndicator=$confidIndicator, 
					deadIndicator=$deadIndicator, 
					vetcFileNumber=$vetcFileNumber, 
					legalName=$legalName, 
					preferenceFirstName=$preferenceFirstName, 
					namePrefix=$namePrefix, 
					nameSuffix=$nameSuffix, 
					veraIndicator=$veraIndicator, 
					citizenshipIndicator=$citizenshipIndicator,
					deadDate=$deadDate, 
					hair=$hair, 
					eyeColor=$eyeColor, 
					cityBirth=$cityBirth, 
					driverLicense=$driverLicense, 
					height=$height, 
					weight=$weight, 
					sdvetIndicator=$sdvetIndicator, 
					licenseIssuedDate=$licenseIssuedDate, 
					licenseExpiresDate=$licenseExpiresDate, 
					incarcerationIndicator=$incarcerationIndicator, 
					itin=$itin, 
					activeDutySeprDate=$activeDutySeprDate, 
					ethnic=$ethnic, 
					confirmedRe=$confirmedRe, 
					confirmedReDate=$confirmedReDate, 
					armedServiceMedalVetIndicator=$armedServiceMedalVetIndicator, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin, 
					legacy=$legacy, 
					ethnicity=$ethnicity, 
					maritalStatus=$maritalStatus, 
					religion=$religion, 
					citizenType=$citizenType, 
					stateBirth=$stateBirth, 
					stateDriver=$stateDriver, 
					nationDriver=$nationDriver, 
					unitOfMeasureHeight=$unitOfMeasureHeight, 
					unitOfMeasureWeight=$unitOfMeasureWeight]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof PersonBasicPersonBase)) return false
        PersonBasicPersonBase that = (PersonBasicPersonBase) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (pidm != that.pidm) return false
        if (ssn != that.ssn) return false
        if (birthDate != that.birthDate) return false
        if (sex != that.sex) return false
        if (confidIndicator != that.confidIndicator) return false
        if (deadIndicator != that.deadIndicator) return false
        if (vetcFileNumber != that.vetcFileNumber) return false
        if (legalName != that.legalName) return false
        if (preferenceFirstName != that.preferenceFirstName) return false
        if (namePrefix != that.namePrefix) return false
        if (nameSuffix != that.nameSuffix) return false
        if (veraIndicator != that.veraIndicator) return false
        if (citizenshipIndicator != that.citizenshipIndicator) return false
        if (deadDate != that.deadDate) return false
        if (hair != that.hair) return false
        if (eyeColor != that.eyeColor) return false
        if (cityBirth != that.cityBirth) return false
        if (driverLicense != that.driverLicense) return false
        if (height != that.height) return false
        if (weight != that.weight) return false
        if (sdvetIndicator != that.sdvetIndicator) return false
        if (licenseIssuedDate != that.licenseIssuedDate) return false
        if (licenseExpiresDate != that.licenseExpiresDate) return false
        if (incarcerationIndicator != that.incarcerationIndicator) return false
        if (itin != that.itin) return false
        if (activeDutySeprDate != that.activeDutySeprDate) return false
        if (ethnic != that.ethnic) return false
        if (confirmedRe != that.confirmedRe) return false
        if (confirmedReDate != that.confirmedReDate) return false
        if (armedServiceMedalVetIndicator != that.armedServiceMedalVetIndicator) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        if (legacy != that.legacy) return false
        if (ethnicity != that.ethnicity) return false
        if (maritalStatus != that.maritalStatus) return false
        if (religion != that.religion) return false
        if (citizenType != that.citizenType) return false
        if (stateBirth != that.stateBirth) return false
        if (stateDriver != that.stateDriver) return false
        if (nationDriver != that.nationDriver) return false
        if (unitOfMeasureHeight != that.unitOfMeasureHeight) return false
        if (unitOfMeasureWeight != that.unitOfMeasureWeight) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (pidm != null ? pidm.hashCode() : 0)
        result = 31 * result + (ssn != null ? ssn.hashCode() : 0)
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0)
        result = 31 * result + (sex != null ? sex.hashCode() : 0)
        result = 31 * result + (confidIndicator != null ? confidIndicator.hashCode() : 0)
        result = 31 * result + (deadIndicator != null ? deadIndicator.hashCode() : 0)
        result = 31 * result + (vetcFileNumber != null ? vetcFileNumber.hashCode() : 0)
        result = 31 * result + (legalName != null ? legalName.hashCode() : 0)
        result = 31 * result + (preferenceFirstName != null ? preferenceFirstName.hashCode() : 0)
        result = 31 * result + (namePrefix != null ? namePrefix.hashCode() : 0)
        result = 31 * result + (nameSuffix != null ? nameSuffix.hashCode() : 0)
        result = 31 * result + (veraIndicator != null ? veraIndicator.hashCode() : 0)
        result = 31 * result + (citizenshipIndicator != null ? citizenshipIndicator.hashCode() : 0)
        result = 31 * result + (deadDate != null ? deadDate.hashCode() : 0)
        result = 31 * result + (hair != null ? hair.hashCode() : 0)
        result = 31 * result + (eyeColor != null ? eyeColor.hashCode() : 0)
        result = 31 * result + (cityBirth != null ? cityBirth.hashCode() : 0)
        result = 31 * result + (driverLicense != null ? driverLicense.hashCode() : 0)
        result = 31 * result + (height != null ? height.hashCode() : 0)
        result = 31 * result + (weight != null ? weight.hashCode() : 0)
        result = 31 * result + (sdvetIndicator != null ? sdvetIndicator.hashCode() : 0)
        result = 31 * result + (licenseIssuedDate != null ? licenseIssuedDate.hashCode() : 0)
        result = 31 * result + (licenseExpiresDate != null ? licenseExpiresDate.hashCode() : 0)
        result = 31 * result + (incarcerationIndicator != null ? incarcerationIndicator.hashCode() : 0)
        result = 31 * result + (itin != null ? itin.hashCode() : 0)
        result = 31 * result + (activeDutySeprDate != null ? activeDutySeprDate.hashCode() : 0)
        result = 31 * result + (ethnic != null ? ethnic.hashCode() : 0)
        result = 31 * result + (confirmedRe != null ? confirmedRe.hashCode() : 0)
        result = 31 * result + (confirmedReDate != null ? confirmedReDate.hashCode() : 0)
        result = 31 * result + (armedServiceMedalVetIndicator != null ? armedServiceMedalVetIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (legacy != null ? legacy.hashCode() : 0)
        result = 31 * result + (ethnicity != null ? ethnicity.hashCode() : 0)
        result = 31 * result + (maritalStatus != null ? maritalStatus.hashCode() : 0)
        result = 31 * result + (religion != null ? religion.hashCode() : 0)
        result = 31 * result + (citizenType != null ? citizenType.hashCode() : 0)
        result = 31 * result + (stateBirth != null ? stateBirth.hashCode() : 0)
        result = 31 * result + (stateDriver != null ? stateDriver.hashCode() : 0)
        result = 31 * result + (nationDriver != null ? nationDriver.hashCode() : 0)
        result = 31 * result + (unitOfMeasureHeight != null ? unitOfMeasureHeight.hashCode() : 0)
        result = 31 * result + (unitOfMeasureWeight != null ? unitOfMeasureWeight.hashCode() : 0)
        return result
    }


    static constraints = {
        pidm(nullable: false, min: -99999999, max: 99999999)
        ssn(nullable: true, maxSize: 15)
        birthDate(nullable: true)
        sex(nullable: true, maxSize: 1)
        confidIndicator(nullable: true, maxSize: 1, inList: ["Y", "N"])
        deadIndicator(nullable: true, maxSize: 1, inList: ["Y"])
        vetcFileNumber(nullable: true, maxSize: 10)
        legalName(nullable: true, maxSize: 500)
        preferenceFirstName(nullable: true, maxSize: 60)
        namePrefix(nullable: true, maxSize: 20)
        nameSuffix(nullable: true, maxSize: 20)
        veraIndicator(nullable: true, maxSize: 1, inList: ["V", "B", "O", "N"])
        citizenshipIndicator(nullable: true, maxSize: 1, inList: ["U", "Y", "S"])
        deadDate(nullable: true)
        hair(nullable: true, maxSize: 2)
        eyeColor(nullable: true, maxSize: 2)
        cityBirth(nullable: true, maxSize: 50)
        driverLicense(nullable: true, maxSize: 20)
        height(nullable: true, min: -99, max: 99)
        weight(nullable: true, min: -9999, max: 9999)
        sdvetIndicator(nullable: true, maxSize: 1, inList: ["Y"])
        licenseIssuedDate(nullable: true)
        licenseExpiresDate(nullable: true)
        incarcerationIndicator(nullable: true, maxSize: 1)
        itin(nullable: true, min: -999999999, max: 999999999)
        activeDutySeprDate(nullable: true)
        ethnic(nullable: true, maxSize: 1, inList: ["1", "2"])
        confirmedRe(nullable: true, maxSize: 1, inList: ["Y", "N"])
        confirmedReDate(nullable: true)
        armedServiceMedalVetIndicator(nullable: false)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        legacy(nullable: true)
        ethnicity(nullable: true)
        maritalStatus(nullable: true)
        religion(nullable: true)
        citizenType(nullable: true)
        stateBirth(nullable: true)
        stateDriver(nullable: true)
        nationDriver(nullable: true)
        unitOfMeasureHeight(nullable: true)
        unitOfMeasureWeight(nullable: true)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['pidm']

    /**
     * Get the age of the person.
     */
    def calculateAge() {
        def months

        if (!birthDate) return null
        if (deadIndicator == 'Y' && (!deadDate)) return null

        def fromDate = new GregorianCalendar()
        fromDate.setTime(birthDate)

        def toDate = new GregorianCalendar()
        if (deadDate) {
            toDate.setTime(deadDate)
        }

        months = (toDate.get(Calendar.YEAR) - fromDate.get(Calendar.YEAR)) * 12
        months = months + (toDate.get(Calendar.MONTH) - fromDate.get(Calendar.MONTH))
        return new Integer(Math.floor(months / 12).intValue())
    }


    def static fetchByPidm(Integer pidm) {
        PersonBasicPersonBase object = PersonBasicPersonBase.withSession { session ->
            def list = session.getNamedQuery('PersonBasicPersonBase.fetchByPidm').setInteger('pidm', pidm).list()[0]
        }

        return object
    }


    def static List fetchByPidmList(List pidm) {
        List object = PersonBasicPersonBase.withSession { session ->
            def list = session.getNamedQuery('PersonBasicPersonBase.fetchByPidmList').setParameterList('pidm', pidm).list()
        }

        return object
    }


    def static fetchBySsn(String ssn) {
        def bioList

        if (ssn) {
            bioList = PersonBasicPersonBase.withSession { session ->
                def result = session.getNamedQuery('PersonBasicPersonBase.fetchBySsn').setString('ssn', ssn).list()
            }
        }

        return bioList
    }

}
