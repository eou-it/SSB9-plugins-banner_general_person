/*********************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person.view

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import javax.persistence.*

/**
 * Person parent View
 */
@Entity
@Table(name = "SVQ_SPREMRG_GUID_DTLS")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class PersonEmergencyContactView {

    /**
     * Surrogate ID for SPREMRG
     */
    @Id
    @Column(name = "SPREMRG_SURROGATE_ID")
    String id

    /**
     * Optimistic lock token for SPREMRG
     */
    @Version
    @Column(name = "SPREMRG_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Global Unique Identifier person emergency contact
     */
    @Column(name = "SPREMRG_GUID")
    String guid

    /**
     * Global Unique Identifier of person
     */
    @Column(name = "PERSON_GUID")
    String personGuid

    /**
     * Internal identification number of person.
     */
    @Column(name = "SPREMRG_PIDM")
    Integer pidm

    /**
     * Priority indicator associated with emergency address of person record.
     */
    @Column(name = "SPREMRG_PRIORITY")
    String priority

    /**
     * First name of person associated with emergency address information.
     */
    @Column(name = "SPREMRG_FIRST_NAME")
    String firstName

    /**
     * Middle name of person associated with emergency address information.
     */
    @Column(name = "SPREMRG_MI")
    String middleInitial

    /**
     * Last name of person associated with emergency address information.
     */
    @Column(name = "SPREMRG_LAST_NAME")
    String lastName

    /**
     * Country Title of an Address
     */
    @Column(name = "COUNTRY_TITLE")
    String countryTitle

    /**
     * HOUSE NUMBER: Building or lot number on a street or in an area.
     */
    @Column(name = "SPREMRG_HOUSE_NUMBER")
    String houseNumber

    /**
     * Line one of emergency adddress.
     */
    @Column(name = "SPREMRG_STREET_LINE1")
    String streetLine1

    /**
     * Line two of emergency address.
     */
    @Column(name = "SPREMRG_STREET_LINE2")
    String streetLine2

    /**
     * Line three of emergency address.
     */
    @Column(name = "SPREMRG_STREET_LINE3")
    String streetLine3

    /**
     * STREET LINE 4: Line four of emergency address.
     */
    @Column(name = "SPREMRG_STREET_LINE4")
    String streetLine4

    /**
     * Nation of an address
     */
    @Column(name = "SPREMRG_NATN_CODE")
    String nationCode

    /**
     * Country Region Code of an Address
     */
    @Column(name = "COUNTRY_REGION_CODE")
    String countryRegionCode

    /**
     * Country Region Title of an Address
     */
    @Column(name = "COUNTRY_REGION_TITLE")
    String countryRegionTitle

    /**
     * City associated with emergency address.
     */
    @Column(name = "SPREMRG_CITY")
    String city

    /**
     * Zip code associated with emergency address.
     */
    @Column(name = "SPREMRG_ZIP")
    String zip

    /**
     * State of an address
     */
    @Column(name = "SPREMRG_STAT_CODE")
    String stateCode

    /**
     * Address type code of an address
     */
    @Column(name = "SPREMRG_ATYP_CODE")
    String addressTypeCode

    /**
     * Global Unique Identifier of address type
     */
    @Column(name = "ADDRESS_TYPE_GUID")
    String addressTypeGuid

    /**
     * COUNTRY CODE: Telephone code that designates the region and country.
     */
    @Column(name = "SPREMRG_CTRY_CODE_PHONE")
    String countryPhone

    /**
     * Area code of phone number associated with emergency address.
     */
    @Column(name = "SPREMRG_PHONE_AREA")
    String phoneArea

    /**
     * Phone number associated with emergency address.
     */
    @Column(name = "SPREMRG_PHONE_NUMBER")
    String phoneNumber

    /**
     * Extension of phone number associated with emergency address.
     */
    @Column(name = "SPREMRG_PHONE_EXT")
    String phoneExtension

    /**
     * Relationship of a person
     */
    @Column(name = "SPREMRG_RELT_CODE")
    String relationshipCode

    /**
     * Global unique identifier of relationship type
     */
    @Column(name = "RELATIONSHIP_TYPE_GUID")
    String relationshipTypeGuid


}
