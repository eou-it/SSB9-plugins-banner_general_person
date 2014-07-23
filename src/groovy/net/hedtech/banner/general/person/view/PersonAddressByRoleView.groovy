/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person.view

import net.hedtech.banner.query.DynamicFinder

import javax.persistence.*

/**
 * Person Spriden model.
 */
@Entity
@Table(name = "GVQ_GORADRL_ADDRESS")
@NamedQueries(value = [
@NamedQuery(name = "PersonAddressByRoleView.fetchAddressesByPidmAndRole",
            query = """FROM  PersonAddressByRoleView a
                       WHERE a.pidm = :pidm and a.userRole = :role order by a.addressTypeDescription """),
@NamedQuery(name = "PersonAddressByRoleView.fetchAddressesByPidmAndRoles",
        query = """FROM PersonAddressByRoleView a
           WHERE   a.pidm  = :pidm
           AND a.userRole in :roles """)
])
class PersonAddressByRoleView {

    @Id
    @Column(name = "SPRADDR_SURROGATE_ID")
    @SequenceGenerator(name = "SPRADDR_SEQ_GEN", allocationSize = 1, sequenceName = "SPRADDR_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPRADDR_SEQ_GEN")
    Long id

    @Version
    @Column(name = "SPRADDR_VERSION")
    Long version

    @Column(name = "SPRADDR_PIDM")
    Integer pidm

    @Column(name = "STVATYP_DESC")
    String addressTypeDescription

    @Column(name = "SPRADDR_HOUSE_NUMBER")
    String houseNumber

    @Column(name = "SPRADDR_STREET_LINE1")
    String streetLine1

    @Column(name = "SPRADDR_STREET_LINE2")
    String streetLine2

    @Column(name = "SPRADDR_STREET_LINE3")
    String streetLine3

    @Column(name = "SPRADDR_STREET_LINE4")
    String streetLine4

    @Column(name = "SPRADDR_CITY")
    String city

    @Column(name = "SPRADDR_STAT_CODE")
    String state

    @Column(name = "SPRADDR_ZIP")
    String zip

    @Column(name = "SPRADDR_CNTY_CODE")
    String county

    @Column(name = "SPRADDR_NATN_CODE")
    String nation

    @Column(name = "SPRADDR_FROM_DATE")
    @Temporal(TemporalType.DATE)
    Date fromDate

    @Column(name = "SPRADDR_TO_DATE")
    @Temporal(TemporalType.DATE)
    Date toDate

    @Column(name = "GORADRL_PRIV_IND")
    String priviledgeIndicator

    @Column(name = "GORADRL_ROLE")
    String userRole

    @Column(name = "PRIVILEDGE_ORDER")
    Integer priviledgeSequence


    public String toString() {
        """PersonAddressByRoleView[
					id=$id,
					version=$version,
					pidm=$pidm,
					fromDate=$fromDate,
					toDate=$toDate,
					streetLine1=$streetLine1,
					streetLine2=$streetLine2,
					streetLine3=$streetLine3,
					city=$city,
					zip=$zip,
					houseNumber=$houseNumber,
					streetLine4=$streetLine4,
					addressTypeDescription=$addressTypeDescription,
					state=$state,
					county=$county,
					nation=$nation,
					userRole=$userRole,
                    priviledgeIndicator=$priviledgeIndicator,
                    priviledgeSequence=$priviledgeSequence]"""
    }


    boolean equals(object) {
        if (this.is(object)) return true
        if (!(object instanceof PersonAddressByRoleView)) return false
        if (!super.equals(object)) return false

        PersonAddressByRoleView that = (PersonAddressByRoleView) object

        if (city != that.city) return false
        if (addressTypeDescription != that.addressTypeDescription) return false
        if (fromDate != that.fromDate) return false
        if (houseNumber != that.houseNumber) return false
        if (id != that.id) return false
        if (pidm != that.pidm) return false
        if (priviledgeIndicator != that.priviledgeIndicator) return false
        if (priviledgeSequence != that.priviledgeSequence) return false
        if (state != that.state) return false
        if (streetLine1 != that.streetLine1) return false
        if (streetLine2 != that.streetLine2) return false
        if (streetLine3 != that.streetLine3) return false
        if (streetLine4 != that.streetLine4) return false
        if (toDate != that.toDate) return false
        if (userRole != that.userRole) return false
        if (version != that.version) return false
        if (zip != that.zip) return false
        if (county != that.county) return false
        if (nation != that.nation) return false

        return true
    }


    int hashCode() {
        int result = super.hashCode()
        result = 31 * result + (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (pidm != null ? pidm.hashCode() : 0)
        result = 31 * result + (addressTypeDescription != null ? addressTypeDescription.hashCode() : 0)
        result = 31 * result + (houseNumber != null ? houseNumber.hashCode() : 0)
        result = 31 * result + (streetLine1 != null ? streetLine1.hashCode() : 0)
        result = 31 * result + (streetLine2 != null ? streetLine2.hashCode() : 0)
        result = 31 * result + (streetLine3 != null ? streetLine3.hashCode() : 0)
        result = 31 * result + (streetLine4 != null ? streetLine4.hashCode() : 0)
        result = 31 * result + (city != null ? city.hashCode() : 0)
        result = 31 * result + (state != null ? state.hashCode() : 0)
        result = 31 * result + (zip != null ? zip.hashCode() : 0)
        result = 31 * result + (fromDate != null ? fromDate.hashCode() : 0)
        result = 31 * result + (toDate != null ? toDate.hashCode() : 0)
        result = 31 * result + (priviledgeIndicator != null ? priviledgeIndicator.hashCode() : 0)
        result = 31 * result + (userRole != null ? userRole.hashCode() : 0)
        result = 31 * result + (priviledgeSequence != null ? priviledgeSequence.hashCode() : 0)
        result = 31 * result + (county != null ? county.hashCode() : 0)
        result = 31 * result + (nation != null ? nation.hashCode() : 0)
        return result
    }


    public static List fetchAddressesByPidmAndRole(Map params) {
        List personAddressList = []
            PersonAddressByRoleView.withSession { session ->
                personAddressList = session.getNamedQuery('PersonAddressByRoleView.fetchAddressesByPidmAndRole').setInteger('pidm', params.pidm).setString('role', params.role).list()
            }
        return personAddressList
    }


    public static def fetchAddressesByPidmAndRoles(Map params) {

        def result = []
        if (params.roles?.size()) {
            result = PersonAddressByRoleView.withSession { session ->
                session.getNamedQuery('PersonAddressByRoleView.fetchAddressesByPidmAndRoles').setInteger('pidm', params?.pidm)
                        .setParameterList('roles', params.roles).list()
            }
        }
        return result
    }



}
