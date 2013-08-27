/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/

/*******************************************************************************
Copyright 2013 Ellucian Company L.P. and its affiliates.
*******************************************************************************/
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.*
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonAddressServiceIntegrationTests extends BaseIntegrationTestCase {

  def personAddressService

	//Test data for creating new domain instance
	//Valid test data (For success tests)
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
    //Invalid test data (For failure tests)
    def i_failure_addressType
    def i_failure_state
    def i_failure_county
    def i_failure_nation
    def i_failure_addressSource

    def i_failure_pidm
    def i_failure_sequenceNumber = null
    def i_failure_fromDate = new Date()
    def i_failure_toDate = new Date()
    def i_failure_streetLine1 = "i_failure_streetLine1"
    def i_failure_streetLine2 = "i_failure_streetLine2"
    def i_failure_streetLine3 = "i_failure_streetLine3"
    def i_failure_city = "TTTTT"
    def i_failure_zip = "TTTTT"
    def i_failure_phoneArea = "TTTTT"
    def i_failure_phoneNumber = "TTTTT"
    def i_failure_phoneExtension = "TTTTT"
    def i_failure_statusIndicator = "I"
    def i_failure_userData = "TTTTT"
    def i_failure_deliveryPoint = 1
    def i_failure_correctionDigit = 1
    def i_failure_carrierRoute = "TTTT"
    def i_failure_goodsAndServiceTaxTaxId = "TTTTT"
    def i_failure_reviewedIndicator = "Y"
    def i_failure_reviewedUser = "TTTTT"
    def i_failure_countryPhone = "TTTT"
    def i_failure_houseNumber = "TTTTT"
    def i_failure_streetLine4 = "TTTTT"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)
    def u_success_addressType
    def u_success_state
    def u_success_county
    def u_success_nation
    def u_success_addressSource

    def u_success_pidm
    def u_success_sequenceNumber = 1
    def u_success_fromDate = new Date()
    def u_success_toDate = new Date()
    def u_success_streetLine1 = "u_success_streetLine1"
    def u_success_streetLine2 = "u_success_streetLine2"
    def u_success_streetLine3 = "u_success_streetLine3"
    def u_success_city = "TTTTT"
    def u_success_zip = "TTTTT"
    def u_success_phoneArea = "TTTTT"
    def u_success_phoneNumber = "TTTTT"
    def u_success_phoneExtension = "TTTTT"
    def u_success_statusIndicator = "I"
    def u_success_userData = "TTTTT"
    def u_success_deliveryPoint = 1
    def u_success_correctionDigit = 1
    def u_success_carrierRoute = "TTTT"
    def u_success_goodsAndServiceTaxTaxId = "TTTTT"
    def u_success_reviewedIndicator = "Y"
    def u_success_reviewedUser = "TTTTT"
    def u_success_countryPhone = "TTTT"
    def u_success_houseNumber = "TTTTT"
    def u_success_streetLine4 = "TTTTT"
    //Valid test data (For failure tests)
    def u_failure_addressType
    def u_failure_state
    def u_failure_county
    def u_failure_nation
    def u_failure_addressSource

    def u_failure_pidm
    def u_failure_sequenceNumber = null
    def u_failure_fromDate = new Date()
    def u_failure_toDate = new Date()
    def u_failure_streetLine1 = "u_failure_streetLine1"
    def u_failure_streetLine2 = "u_failure_streetLine2"
    def u_failure_streetLine3 = "u_failure_streetLine3"
    def u_failure_city = "TTTTT"
    def u_failure_zip = "TTTTT"
    def u_failure_phoneArea = "TTTTT"
    def u_failure_phoneNumber = "TTTTT"
    def u_failure_phoneExtension = "TTTTT"
    def u_failure_statusIndicator = "I"
    def u_failure_userData = "TTTTT"
    def u_failure_deliveryPoint = 1
    def u_failure_correctionDigit = 1
    def u_failure_carrierRoute = "TTTT"
    def u_failure_goodsAndServiceTaxTaxId = "TTTTT"
    def u_failure_reviewedIndicator = "Y"
    def u_failure_reviewedUser = "TTTTT"
    def u_failure_countryPhone = "TTTT"
    def u_failure_houseNumber = "TTTTT"
    def u_failure_streetLine4 = "TTTTT"


	protected void setUp() {
		formContext = ['GUAGMNU']
		super.setUp()
        initializeTestDataForReferences()
	}

	//This method is used to initialize test data for references.
	//A method is required to execute database calls as it requires a active transaction
	void initializeTestDataForReferences() {
		//Valid test data (For success tests)
        u_success_pidm = i_success_pidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        u_failure_pidm = i_failure_pidm = PersonIdentificationName.findByBannerId("HOF00716").pidm


        i_success_addressType = AddressType.findByCode("PO")
        i_success_state = State.findByCode("MER")
        i_success_county = County.findByCode("044")
        i_success_nation = Nation.findByCode("157")
        i_success_addressSource = AddressSource.findByCode("BRD")

        //Invalid test data (For failure tests)
        i_failure_addressType = AddressType.findByCode("GR")
        i_failure_state = State.findByCode("GLO")
        i_failure_county = County.findByCode("045")
        i_failure_nation = Nation.findByCode("40")
        i_failure_addressSource = AddressSource.findByCode("BRD")

        //Valid test data (For success tests)
        u_success_addressType = AddressType.findByCode("PO")
        u_success_state = State.findByCode("MER")
        u_success_county = County.findByCode("044")
        u_success_nation = Nation.findByCode("157")
        u_success_addressSource = AddressSource.findByCode("BRD")

        //Valid test data (For failure tests)
        u_failure_addressType = AddressType.findByCode("GR")
        u_failure_state = State.findByCode("GLO")
        u_failure_county = County.findByCode("045")
        u_failure_nation = Nation.findByCode("40")
        u_failure_addressSource = AddressSource.findByCode("BRD")

		//Test data for references for custom tests

	}

	protected void tearDown() {
		super.tearDown()
	}

	void testPersonAddressValidCreate() {
		def personAddress = newValidForCreatePersonAddress()
		def map = [domainModel: personAddress]
        personAddress = personAddressService.create(map)
		assertNotNull "PersonAddress ID is null in PersonAddress Service Tests Create", personAddress.id
//		assertNotNull "PersonAddress addressType is null in PersonAddress Service Tests", personAddress.addressType
//		assertNotNull "PersonAddress state is null in PersonAddress Service Tests", personAddress.state
//		assertNotNull "PersonAddress county is null in PersonAddress Service Tests", personAddress.county
//		assertNotNull "PersonAddress nation is null in PersonAddress Service Tests", personAddress.nation
//		assertNotNull "PersonAddress addressSource is null in PersonAddress Service Tests", personAddress.addressSource
//	    assertNotNull personAddress.version
//	    assertNotNull personAddress.dataOrigin
//		assertNotNull personAddress.lastModifiedBy
//	    assertNotNull personAddress.lastModified
    }

	void testPersonAddressInvalidCreate() {
		def personAddress = newInvalidForCreatePersonAddress()
		def map = [domainModel: personAddress]
		shouldFail(ApplicationException) {
            personAddress = personAddressService.create(map)
		}
    }

	void testPersonAddressValidUpdate() {
		def personAddress = newValidForCreatePersonAddress()
		def map = [domainModel: personAddress]
        personAddress = personAddressService.create(map)
		assertNotNull "PersonAddress ID is null in PersonAddress Service Tests Create", personAddress.id
		assertNotNull "PersonAddress addressType is null in PersonAddress Service Tests", personAddress.addressType
		assertNotNull "PersonAddress state is null in PersonAddress Service Tests", personAddress.state
		assertNotNull "PersonAddress county is null in PersonAddress Service Tests", personAddress.county
		assertNotNull "PersonAddress nation is null in PersonAddress Service Tests", personAddress.nation
		assertNotNull "PersonAddress addressSource is null in PersonAddress Service Tests", personAddress.addressSource
	    assertNotNull personAddress.version
	    assertNotNull personAddress.dataOrigin
//		assertNotNull personAddress.lastModifiedBy
	    assertNotNull personAddress.lastModified
		//Update the entity with new values
		personAddress.addressType = u_success_addressType
		personAddress.state = u_success_state
		personAddress.county = u_success_county
		personAddress.nation = u_success_nation
		personAddress.addressSource = u_success_addressSource
		personAddress.pidm = u_success_pidm
		personAddress.sequenceNumber = u_success_sequenceNumber
//		personAddress.fromDate = u_success_fromDate
//		personAddress.toDate = u_success_toDate
		personAddress.streetLine1 = u_success_streetLine1
		personAddress.streetLine2 = u_success_streetLine2
		personAddress.streetLine3 = u_success_streetLine3
		personAddress.city = u_success_city
		personAddress.zip = u_success_zip
		personAddress.phoneArea = u_success_phoneArea
		personAddress.phoneNumber = u_success_phoneNumber
		personAddress.phoneExtension = u_success_phoneExtension
		personAddress.statusIndicator = u_success_statusIndicator
		personAddress.userData = u_success_userData
		personAddress.deliveryPoint = u_success_deliveryPoint
		personAddress.correctionDigit = u_success_correctionDigit
		personAddress.carrierRoute = u_success_carrierRoute
		personAddress.goodsAndServiceTaxTaxId = u_success_goodsAndServiceTaxTaxId
		personAddress.reviewedIndicator = u_success_reviewedIndicator
		personAddress.reviewedUser = u_success_reviewedUser
		personAddress.countryPhone = u_success_countryPhone
		personAddress.houseNumber = u_success_houseNumber
		personAddress.streetLine4 = u_success_streetLine4
		
//		map.domainModel = personAddress
		personAddress = personAddressService.update([domainModel: personAddress])
		// test the values
		assertEquals u_success_pidm, personAddress.pidm
		assertEquals u_success_sequenceNumber, personAddress.sequenceNumber
//		assertEquals u_success_fromDate, personAddress.fromDate
//		assertEquals u_success_toDate, personAddress.toDate
		assertEquals u_success_streetLine1, personAddress.streetLine1
		assertEquals u_success_streetLine2, personAddress.streetLine2
		assertEquals u_success_streetLine3, personAddress.streetLine3
		assertEquals u_success_city, personAddress.city
		assertEquals u_success_zip, personAddress.zip
//		assertEquals u_success_phoneArea, personAddress.phoneArea
//		assertEquals u_success_phoneNumber, personAddress.phoneNumber
//		assertEquals u_success_phoneExtension, personAddress.phoneExtension
		assertEquals u_success_statusIndicator, personAddress.statusIndicator
		assertEquals u_success_userData, personAddress.userData
		assertEquals u_success_deliveryPoint, personAddress.deliveryPoint
		assertEquals u_success_correctionDigit, personAddress.correctionDigit
		assertEquals u_success_carrierRoute, personAddress.carrierRoute
		assertEquals u_success_goodsAndServiceTaxTaxId, personAddress.goodsAndServiceTaxTaxId
		assertEquals u_success_reviewedIndicator, personAddress.reviewedIndicator
		assertEquals u_success_reviewedUser, personAddress.reviewedUser
		assertEquals u_success_countryPhone, personAddress.countryPhone
		assertEquals u_success_houseNumber, personAddress.houseNumber
		assertEquals u_success_streetLine4, personAddress.streetLine4
		assertEquals u_success_addressType, personAddress.addressType
		assertEquals u_success_state, personAddress.state
		assertEquals u_success_county, personAddress.county
		assertEquals u_success_nation, personAddress.nation
		assertEquals u_success_addressSource, personAddress.addressSource
	}

	void testPersonAddressInvalidUpdate() {
		def personAddress = newValidForCreatePersonAddress()
		def map = [domainModel: personAddress]
        personAddress = personAddressService.create(map)
		assertNotNull "PersonAddress ID is null in PersonAddress Service Tests Create", personAddress.id
		assertNotNull "PersonAddress addressType is null in PersonAddress Service Tests", personAddress.addressType
		assertNotNull "PersonAddress state is null in PersonAddress Service Tests", personAddress.state
		assertNotNull "PersonAddress county is null in PersonAddress Service Tests", personAddress.county
		assertNotNull "PersonAddress nation is null in PersonAddress Service Tests", personAddress.nation
		assertNotNull "PersonAddress addressSource is null in PersonAddress Service Tests", personAddress.addressSource
	    assertNotNull personAddress.version
	    assertNotNull personAddress.dataOrigin
//		assertNotNull personAddress.lastModifiedBy
	    assertNotNull personAddress.lastModified
		//Update the entity with new invalid values
		personAddress.addressType = u_failure_addressType
		personAddress.state = u_failure_state
		personAddress.county = u_failure_county
		personAddress.nation = u_failure_nation
		personAddress.addressSource = u_failure_addressSource
		personAddress.pidm = u_failure_pidm
		personAddress.sequenceNumber = u_failure_sequenceNumber
		personAddress.fromDate = u_failure_fromDate
		personAddress.toDate = u_failure_toDate
		personAddress.streetLine1 = u_failure_streetLine1
		personAddress.streetLine2 = u_failure_streetLine2
		personAddress.streetLine3 = u_failure_streetLine3
		personAddress.city = u_failure_city
		personAddress.zip = u_failure_zip
		personAddress.phoneArea = u_failure_phoneArea
		personAddress.phoneNumber = u_failure_phoneNumber
		personAddress.phoneExtension = u_failure_phoneExtension
		personAddress.statusIndicator = u_failure_statusIndicator
		personAddress.userData = u_failure_userData
		personAddress.deliveryPoint = u_failure_deliveryPoint
		personAddress.correctionDigit = u_failure_correctionDigit
		personAddress.carrierRoute = u_failure_carrierRoute
		personAddress.goodsAndServiceTaxTaxId = u_failure_goodsAndServiceTaxTaxId
		personAddress.reviewedIndicator = u_failure_reviewedIndicator
		personAddress.reviewedUser = u_failure_reviewedUser
		personAddress.countryPhone = u_failure_countryPhone
		personAddress.houseNumber = u_failure_houseNumber
		personAddress.streetLine4 = u_failure_streetLine4
		
		map.domainModel = personAddress
		shouldFail(ApplicationException) {
			personAddress = personAddressService.update(map)
		}
	}

    void testPersonEmailPrimaryKeyUpdate() {
        def personAddress = newValidForCreatePersonAddress()
        def map = [domainModel: personAddress]
        personAddress = personAddressService.create(map)
        assertNotNull "PersonEmail ID is null in PersonEmail Service Tests Create", personAddress.id
        assertNotNull "PersonEmail emailType is null in PersonEmail Service Tests", personAddress.addressType
        assertNotNull personAddress.version
        assertNotNull personAddress.dataOrigin
//        assertNotNull personAddress.lastModifiedBy
        assertNotNull personAddress.lastModified

        personAddress.addressType = AddressType.findByCode("BI")
        try {
            personAddressService.update([domainModel: personAddress])
            fail("This should have failed with @@r1:primaryKeyFieldsCannotBeModified")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "primaryKeyFieldsCannotBeModified"
        }
    }


	void testPersonAddressDelete() {
		def personAddress = newValidForCreatePersonAddress()
		def map = [domainModel: personAddress]
        personAddress = personAddressService.create(map)
		assertNotNull "PersonAddress ID is null in PersonAddress Service Tests Create", personAddress.id
		def id = personAddress.id
        map = [domainModel: personAddress]
		personAddressService.delete( map )
		assertNull "PersonAddress should have been deleted", personAddress.get(id)
  	}


	private def newValidForCreatePersonAddress() {
        def personAddress = new PersonAddress(
                pidm: i_success_pidm,
                //sequenceNumber: i_success_sequenceNumber,
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

	private def newInvalidForCreatePersonAddress() {
        def personAddress = new PersonAddress(
                pidm: i_failure_pidm,
                sequenceNumber: i_failure_sequenceNumber,
                fromDate: i_failure_fromDate,
                toDate: i_failure_toDate,
                streetLine1: i_failure_streetLine1,
                streetLine2: i_failure_streetLine2,
                streetLine3: i_failure_streetLine3,
//			city: i_failure_city,
                city: null,
                zip: i_failure_zip,
                phoneArea: i_failure_phoneArea,
                phoneNumber: i_failure_phoneNumber,
                phoneExtension: i_failure_phoneExtension,
                statusIndicator: i_failure_statusIndicator,
                userData: i_failure_userData,
                deliveryPoint: i_failure_deliveryPoint,
                correctionDigit: i_failure_correctionDigit,
                carrierRoute: i_failure_carrierRoute,
                goodsAndServiceTaxTaxId: i_failure_goodsAndServiceTaxTaxId,
                reviewedIndicator: i_failure_reviewedIndicator,
                reviewedUser: i_failure_reviewedUser,
                countryPhone: i_failure_countryPhone,
                houseNumber: i_failure_houseNumber,
                streetLine4: i_failure_streetLine4,
                addressType: i_failure_addressType,
                state: i_failure_state,
                county: i_failure_county,
                nation: i_failure_nation,
                addressSource: i_failure_addressSource
        )
		return personAddress
	}
}
