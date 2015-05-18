/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.person

import net.hedtech.banner.general.system.Ethnicity
import net.hedtech.banner.general.system.MaritalStatus

import javax.persistence.*


@Entity
@Table(name = "SVQ_STUDENT_ROLE")
@NamedQueries(value = [
        @NamedQuery(name = "StudentRole.fetchByPidm",
                query = """FROM StudentRole a
                    where a.pidm = :pidm"""),
        @NamedQuery(name = "StudentRole.fetchByPidms",
                query = """FROM StudentRole a
                    where a.pidm in :pidms"""),
        @NamedQuery(name = "StudentRole.fetchByLastNameAndFirstName",
                query = """FROM StudentRole a
                    WHERE a.firstName = :firstName
                    AND a.lastName=:lastName""")
])
class StudentRole implements Serializable {

    @Id
    @Column(name = "SPRIDEN_SURROGATE_ID")
    Long id

    @Column(name = "SPRIDEN_ID")
    String bannerId


    @Column(name = "SPRIDEN_PIDM")
    Integer pidm


    @Column(name = "SPRIDEN_LAST_NAME")
    String lastName


    @Column(name = "SPRIDEN_FIRST_NAME")
    String firstName


    @Column(name = "SPRIDEN_MI")
    String middleName


    @Column(name = "SPRIDEN_SURNAME_PREFIX")
    String surnamePrefix


    @Column(name = "SPRIDEN_FULL_NAME")
    String fullName


    @Column(name = "SPRIDEN_DATA_ORIGIN")
    String dataOrigin


    @ManyToOne
    @JoinColumns([
            @JoinColumn(name = "SPBPERS_MRTL_CODE", referencedColumnName = "STVMRTL_CODE")
    ])
    MaritalStatus maritalStatus


    @ManyToOne
    @JoinColumns([
            @JoinColumn(name = "SPBPERS_ETHN_CODE", referencedColumnName = "STVETHN_CODE")
    ])
    Ethnicity ethnicity


    @Column(name = "SPBPERS_NAME_PREFIX")
    String namePrefix


    @Column(name = "SPBPERS_NAME_SUFFIX")
    String nameSuffix


    @Column(name = "SPBPERS_PREF_FIRST_NAME")
    String preferenceFirstName


    public String toString() {
        """StudentRole[
					id=$id,
					bannerId=$bannerId,
					pidm=$pidm,
					lastName=$lastName,
					firstName=$firstName,
					middleName=$middleName,
                    surnamePrefix=$surnamePrefix,
					fullName=$fullName,
                    dataOrigin=$dataOrigin,
                    maritalStatus=$maritalStatus,
					ethnicity=$ethnicity,
                    namePrefix=$namePrefix,
                    nameSuffix=$nameSuffix,
                    preferenceFirstName=$preferenceFirstName]"""
    }

    public static StudentRole fetchByPidm(Integer pidm) {

        StudentRole studentRole = StudentRole.withSession {
            session -> session.getNamedQuery('StudentRole.fetchByPidm').setInteger('pidm', pidm).uniqueResult()
        }
        return studentRole
    }


    public static List<StudentRole> fetchByPidms(List pidms) {

        List<StudentRole> studentRoles = StudentRole.withSession {session ->
            session.getNamedQuery('StudentRole.fetchByPidms').setParameterList('pidms', pidms).list()
        }

        return studentRoles
    }

    public static StudentRole fetchByLastNameAndFirstName(String firstName, String lastName) {

        StudentRole studentRole = StudentRole.withSession {
            session -> session.getNamedQuery('StudentRole.fetchByLastNameAndFirstName').setString('firstName', firstName).setString('lastName',lastName).uniqueResult()
        }
        return studentRole
    }

}
