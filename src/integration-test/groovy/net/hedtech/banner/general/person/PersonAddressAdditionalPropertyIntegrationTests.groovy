/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import net.hedtech.banner.general.system.AddressSource
import net.hedtech.banner.general.system.AddressType
import net.hedtech.banner.general.system.County
import net.hedtech.banner.general.system.Nation
import net.hedtech.banner.general.system.State
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

@Integration
@Rollback
class PersonAddressAdditionalPropertyIntegrationTests extends BaseIntegrationTestCase {

    def i_success_addressType
    def i_success_state
    def i_success_county
    def i_success_nation
    def i_success_addressSource
    def i_success_pidm
    def i_success_sequenceNumber = 0
    def i_success_fromDate = new Date()
    def i_success_toDate = new Date()
    def i_success_streetLine1 = "i_success_streetLine1"
    def i_success_streetLine2 = "i_success_streetLine2"
    def i_success_streetLine3 = "i_success_streetLine3"
    def i_success_city = "TTTTT"
    def i_success_zip = "TTTTT"
    def i_success_phoneArea = "TTTTT"
    def i_success_phoneNumber = "TTTTT"
    def i_success_phoneExtension = "TTTTT"
    def i_success_statusIndicator = "I"
    def i_success_userData = "TTTTT"
    def i_success_deliveryPoint = 1
    def i_success_correctionDigit = 1
    def i_success_carrierRoute = "TTTT"
    def i_success_goodsAndServiceTaxTaxId = "TTTTT"
    def i_success_reviewedIndicator = "Y"
    def i_success_reviewedUser = "TTTTT"
    def i_success_countryPhone = "TTTT"
    def i_success_houseNumber = "TTTTT"
    def i_success_streetLine4 = "TTTTT"

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }

    @Test
    void testGetPersonAddressExtendedProperties() {
        def personAddress = newPersonAddress()
        personAddress.save(failOnError: true, flush: true)
        //Test if the generated personAddressExtendedProperties now has an id assigned
        assertNotNull personAddress.id
        def personAddExtendedProperties = PersonAddressAdditionalProperty.get(personAddress.id)
        assertNotNull personAddExtendedProperties
        assertNotNull personAddExtendedProperties.addressGuid
    }

    private def newPersonAddress() {
        i_success_pidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        i_success_addressType = AddressType.findWhere(code: "PO")
        i_success_state = State.findWhere(code: "MER")
        i_success_county = County.findWhere(code: "044")
        i_success_nation = Nation.findWhere(code: "157")
        i_success_addressSource = AddressSource.findWhere(code: "BRD")
        def personAddress = new PersonAddress(
                pidm: i_success_pidm,
                fromDate: i_success_fromDate,
                toDate: i_success_toDate,
                streetLine1: i_success_streetLine1,
                streetLine2: i_success_streetLine2,
                streetLine3: i_success_streetLine3,
                city: i_success_city,
                zip: i_success_zip,
                phoneArea: i_success_phoneArea,
                phoneNumber: i_success_phoneNumber,
                phoneExtension: i_success_phoneExtension,
                statusIndicator: i_success_statusIndicator,
                userData: i_success_userData,
                deliveryPoint: i_success_deliveryPoint,
                correctionDigit: i_success_correctionDigit,
                carrierRoute: i_success_carrierRoute,
                goodsAndServiceTaxTaxId: i_success_goodsAndServiceTaxTaxId,
                reviewedIndicator: i_success_reviewedIndicator,
                reviewedUser: i_success_reviewedUser,
                countryPhone: i_success_countryPhone,
                houseNumber: i_success_houseNumber,
                streetLine4: i_success_streetLine4,
                addressType: i_success_addressType,
                state: i_success_state,
                county: i_success_county,
                nation: i_success_nation,
                addressSource: i_success_addressSource
        )
        return personAddress
    }
}
