/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package net.hedtech.banner.general.person
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.*
import net.hedtech.banner.person.PersonAddressTelephone
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonAddressCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def personAddressService
    def personAddressCompositeService


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
    void testCreateInactiveAddresses() {
        //Create 2 new addresses both inactive, hence no date checking
        def ipidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        def personAddressTelephones = newPersonAddressTelephones("I")
        def personAddresses = PersonAddress.findAllByPidm(ipidm)
        def initSize = personAddresses.size()
        personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones,
                ])
        personAddresses = PersonAddress.findAllByPidm(ipidm)
        assertTrue "The number of addresses expected for HOF00714 is " + initSize.plus(2) + " but found "+ personAddresses.size(),personAddresses.size() == initSize.plus(2)
        assert extractAddressTypes(personAddresses).containsAll(['PO','BI'])
        personAddresses.each{ it ->
            if (it.addressType.code == 'PO') {
                assertTrue it.streetLine1 == "i_success_streetLine1" && it.streetLine2 == "i_success_streetLine2" && it.streetLine3 == "i_success_streetLine3"
                def telephone = PersonTelephone.fetchByPidmSequenceNoAndAddressTypeWithoutPrimaryCheck(ipidm,it.sequenceNumber,it.addressType)
                assertTrue telephone[0].phoneArea == "000"  &&  telephone[0].phoneNumber == "0000000"
               assertTrue telephone[0].telephoneType.code == "SC"
            }
        }
    }


    @Test
    void testCreateActiveAddressesOverlappingDates() {
        //Create 2 new addresses both active
        def ipidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        def personAddressTelephones = newPersonAddressTelephones(null)
        def personAddresses = PersonAddress.findAllByPidm(ipidm)
        def initSize = personAddresses.size()
        personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones,
                ])
        personAddresses = PersonAddress.findAllByPidm(ipidm)
        assertTrue "The number of addresses expected for HOF00714 is " + initSize.plus(2) + " but found "+ personAddresses.size(),personAddresses.size() == initSize.plus(2)
        //Create another address of same atyp as one of the two just created
        personAddressTelephones = newSinglePersonAddress("PO",new Date(),new Date())
        try {
            personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones,
                ])
            fail("This should have failed with @@r1:multipleAddressesExist")
        } catch(ApplicationException ae)   {
            assertApplicationException ae, "multipleAddressesExist"
        }
    }


    @Test
    void testCreateActiveAddressesNullDates() {
        //Create 2 new addresses both active
        def ipidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        def personAddressTelephones = newPersonAddressTelephones(null)
        def personAddresses = PersonAddress.findAllByPidm(ipidm)
        def initSize = personAddresses.size()
        personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones,
                ])
        personAddresses = PersonAddress.findAllByPidm(ipidm)
        assertTrue "The number of addresses expected for HOF00714 is " + initSize.plus(2) + " but found "+ personAddresses.size(),personAddresses.size() == initSize.plus(2)
        //Create another address of same atyp as one of the two just created
        personAddressTelephones = newSinglePersonAddress("PO",null,null)
        try {
            personAddressCompositeService.createOrUpdate(
                    [
                            createPersonAddressTelephones: personAddressTelephones,
                    ])
            fail("This should have failed with @@r1:multipleAddressesExist")
        } catch(ApplicationException ae)   {
            assertApplicationException ae, "multipleAddressesExist"
        }
    }


    @Test
    void testCreateActiveAddressesSuccessfulDates() {
        //Create 2 new addresses both active
        def ipidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        def personAddressTelephones = newPersonAddressTelephones(null)
        def personAddresses = PersonAddress.findAllByPidm(ipidm)
        def initSize = personAddresses.size()
        personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones,
                ])
        personAddresses = PersonAddress.findAllByPidm(ipidm)
        assertTrue "The number of addresses expected for HOF00714 is " + initSize.plus(2) + " but found "+ personAddresses.size(),personAddresses.size() == initSize.plus(2)
        //Create another address of same atyp as one of the two just created
        personAddressTelephones = newSinglePersonAddress("PO",new Date().plus(3),new Date().plus(10))
        personAddressCompositeService.createOrUpdate(
            [
                createPersonAddressTelephones: personAddressTelephones,
            ])
        personAddresses = PersonAddress.findAllByPidm(ipidm)
        assertTrue "The number of addresses expected for HOF00714 is " + initSize.plus(3) + " but found "+ personAddresses.size(),personAddresses.size() == initSize.plus(3)
        personAddresses.each{ it ->
            if (it.addressType.code == 'PO' && it.sequenceNumber == 2) {
                assertTrue it.streetLine1 == "i_streetLine11" && it.streetLine2 == "i_streetLine21" && it.streetLine3 == "i_streetLine31"
            }
        }
    }



    @Test
    void testPersonAddressDelete() {
        //Delete addresses taht got added
        def ipidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        def personAddressTelephones = newPersonAddressTelephones("I")
        def personAddresses = PersonAddress.findAllByPidm(ipidm)
        def initSize = personAddresses.size()
        personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones
                ])
        personAddresses = PersonAddress.findAllByPidm(ipidm)
        assertTrue "The number of addresses expected for HOF00714 is " + initSize+2 + " but found "+ personAddresses.size(),personAddresses.size() == initSize+2
        def deletePersonAddressTelephones =  personAddresses.collect{it -> [personAddress:it]}
        personAddressCompositeService.createOrUpdate(
                [
                    deletePersonAddressTelephones: deletePersonAddressTelephones
                ]
        )
        assertEquals PersonAddress.findAllByPidm(ipidm).size(),0
    }


    @Test
    void testUpdateAddressCreatePhone() {
        //Update of addresses along with updating the telephone with new telephones.  ie. create of sprtele
        def ipidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        def addressTelephone
        def personAddressTelephones
        def personAddresses = PersonAddress.findAllByPidm(ipidm)
        def initSize = personAddresses.size()
        personAddressTelephones = newPersonAddressTelephones("I")
        personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones,
                ])
        personAddresses = PersonAddress.findAllByPidm(ipidm)
        assertTrue "The number of addresses expected for HOF00714 is " + initSize + 2 + " but found "+ personAddresses.size(),personAddresses.size() == initSize+2
        personAddresses.each{ it ->
            if (it.addressType.code == 'PO') {
                it.streetLine1 = 'Test Update 1'
                addressTelephone = new PersonAddressTelephone(it,TelephoneType.findByCode('SB'),'000','000','00000','000',null)
                personAddressTelephones << addressTelephone
            }
            if (it.addressType.code == 'BI') {
                it.streetLine1 = 'Test Update 2'
                addressTelephone = new PersonAddressTelephone(it,TelephoneType.findByCode('EMER'),'111','111','11111','111',null)
                personAddressTelephones << addressTelephone
            }
        }
        personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones,
                ])
        personAddresses = PersonAddress.findAllByPidm(ipidm)
        def personTelephones = PersonTelephone.findAllByPidm(ipidm)
        assert extractAddressTypes(personAddresses).containsAll(['PO','BI'])
        assert extractTelephoneTypes(personTelephones).containsAll(['EMER','SB'])
        def telephone1 = personTelephones.find( {it -> it.telephoneType.code == 'EMER'})
        assertTrue telephone1.countryPhone == '111' && telephone1.phoneArea == '111' && telephone1.phoneNumber == '11111' && telephone1.phoneExtension == '111'
        telephone1 = personTelephones.find({it -> it.telephoneType.code == 'SB'})
        assertTrue telephone1.countryPhone == '000' && telephone1.phoneArea == '000' && telephone1.phoneNumber == '00000' && telephone1.phoneExtension == '000'
    }


    @Test
    void testUpdateAddressUpdatePhone() {
        //Update of addresses along with updating the telephone with new telephones.  ie. update of sprtele
        def ipidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        def addressTelephone
        def personAddressTelephones
        def personAddresses = PersonAddress.findAllByPidm(ipidm)
        def initSize = personAddresses.size()
        personAddressTelephones = newPersonAddressTelephones("I")
        personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones,
                ])
        personAddresses = PersonAddress.findAllByPidm(ipidm)
        assertTrue "The number of addresses expected for HOF00714 is " + initSize + 2 + " but found "+ personAddresses.size(),personAddresses.size() == initSize+2
        personAddresses.each{ it ->
            if (it.addressType.code == 'PO') {
                it.streetLine1 = 'Test Update 1'
                addressTelephone = PersonTelephone.fetchByPidmTelephoneTypeAndAddressType([pidm:ipidm,telephoneType:TelephoneType.findByCode('SC'),addressType:AddressType.findByCode('PO')]).list[0]
                personAddressTelephones << new PersonAddressTelephone(it,addressTelephone.telephoneType,'091',addressTelephone.phoneArea,addressTelephone.phoneNumber,addressTelephone.phoneExtension,addressTelephone.sequenceNumber)
            }
        }
        personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones,
                ])
        personAddresses = PersonAddress.findAllByPidm(ipidm)
        def personTelephones = PersonTelephone.findAllByPidm(ipidm)
        assert extractAddressTypes(personAddresses).containsAll(['PO','BI'])
        def telephone1 = personTelephones.find( {it -> it.telephoneType.code == 'SC'})
        assertTrue telephone1.countryPhone == '091'
    }


    @Test
    void testCreateActiveAddressesWithArchival() {
        //Create 2 new addresses both active
        def ipidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        def personAddressTelephones = newPersonAddressTelephones(null)
        def personAddresses = PersonAddress.findAllByPidm(ipidm)
        def initSize = personAddresses.size()
        personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones,
                ])
        personAddresses = PersonAddress.findAllByPidm(ipidm)
        assertTrue "The number of addresses expected for HOF00714 is " + initSize.plus(2) + " but found "+ personAddresses.size(),personAddresses.size() == initSize.plus(2)
        //Create another address of same atyp as one of the two just created
        personAddressTelephones = newSinglePersonAddress("PO",new Date().plus(3),new Date().plus(10))
        personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones,
                ], false)
        personAddresses = PersonAddress.findAllByPidm(ipidm)
        assertTrue "The number of addresses expected for HOF00714 is " + initSize.plus(3) + " but found "+ personAddresses.size(),personAddresses.size() == initSize.plus(3)
        personAddresses.each{ it ->
            if (it.addressType.code == 'PO' && it.sequenceNumber == 2) {
                assertTrue it.streetLine1 == "i_streetLine11" && it.streetLine2 == "i_streetLine21" && it.streetLine3 == "i_streetLine31"
            }
        }
    }


    @Test
    void testUpdateAddressWithArchival() {
        def ipidm = PersonIdentificationName.findByBannerId("GDP000001").pidm
        def personAddressTelephones
        def personAddresses = PersonAddress.findAllByPidm(ipidm)
        def initSize = personAddresses.size()
        def initStreet1 = ''
        personAddressTelephones = []
        personAddresses.each{ it ->
            if (it.addressType.code == 'MA') {
                initStreet1 = it.streetLine1
                def updatedAddress = [:]
                updatedAddress.id = it.id
                updatedAddress.version = it.version
                updatedAddress.pidm = it.pidm
                updatedAddress.addressType = it.addressType
                updatedAddress.fromDate = it.fromDate
                updatedAddress.toDate = it.toDate
                updatedAddress.houseNumber = it.houseNumber
                updatedAddress.streetLine1 = 'Test Update 1'
                updatedAddress.streetLine2 = it.streetLine2
                updatedAddress.streetLine3 = it.streetLine3
                updatedAddress.streetLine4 = it.streetLine4
                updatedAddress.city = it.city
                updatedAddress.county = it.county
                updatedAddress.state = it.state
                updatedAddress.zip = it.zip
                updatedAddress.nation = it.nation

                personAddressTelephones << [personAddress: updatedAddress]
            }
        }
        personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones,
                ], false)
        personAddresses = PersonAddress.findAllByPidm(ipidm)
        assert extractAddressTypes(personAddresses).contains('MA')
        assertEquals initSize+1, personAddresses.size()

        def updatedAddr = personAddresses.find( { it ->
            it.addressType.code == 'MA' && it.statusIndicator != 'I'
        })
        assertEquals 'Test Update 1', updatedAddr.streetLine1

        def archivedAddr = personAddresses.find( { it ->
            it.addressType.code == 'MA' && it.statusIndicator == 'I'
        })
        assertEquals initStreet1, archivedAddr.streetLine1
    }


    @Test
    void testUpdateAddressCannotDeletePhone() {
        //Update of addresses along with updating the telephone with new telephones.  ie. update of sprtele
        def ipidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        def addressTelephone
        def personAddressTelephones
        def personAddresses = PersonAddress.findAllByPidm(ipidm)
        def initSize = personAddresses.size()
        personAddressTelephones = newPersonAddressTelephones("I")
        personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones,
                ])
        personAddresses = PersonAddress.findAllByPidm(ipidm)
        assertTrue "The number of addresses expected for HOF00714 is " + initSize + 2 + " but found "+ personAddresses.size(),personAddresses.size() == initSize+2
        personAddresses.each{ it ->
            if (it.addressType.code == 'PO') {
                it.streetLine1 = 'Test Update 1'
                addressTelephone = PersonTelephone.fetchByPidmTelephoneTypeAndAddressType([pidm:ipidm,telephoneType:TelephoneType.findByCode('SC'),addressType:AddressType.findByCode('PO')]).list[0]
                personAddressTelephones << new PersonAddressTelephone(it,addressTelephone.telephoneType,null,null,null,null,addressTelephone.sequenceNumber)
            }
        }
        try {
            personAddressCompositeService.createOrUpdate(
                [
                        createPersonAddressTelephones: personAddressTelephones,
                ])
            fail("This should have failed with @@r1:telephoneNotDeleteFromAddress")
        } catch (ApplicationException ae) {
            assertApplicationException ae, "telephoneNotDeleteFromAddress"
        }

    }





    private def newSinglePersonAddress(String atyp, Date fromDate, Date toDate) {
        def personAddressTelephones = []
        def ipidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        def personAddress = new PersonAddress(
                pidm: ipidm,
                //sequenceNumber: i_success_sequenceNumber,
                fromDate: fromDate,
                toDate: toDate,
                streetLine1: "i_streetLine11",
                streetLine2: "i_streetLine21",
                streetLine3: "i_streetLine31",
                city: "TTTTT",
                zip: "TTTTT",
                phoneArea: "TTTTT",
                phoneNumber: "TTTTT",
                phoneExtension: "TTTTT",
                statusIndicator: null,
                userData: "TTTTT",
                deliveryPoint: 1,
                correctionDigit: 1,
                carrierRoute: "TTTT",
                goodsAndServiceTaxTaxId: "TTTTT",
                reviewedIndicator: "Y",
                reviewedUser: "TTTTT",
                countryPhone: "TTTT",
                houseNumber: "TTTTT",
                streetLine4: "TTTTT",
                addressType: AddressType.findByCode(atyp),
                state: State.findByCode("MER"),
                county: County.findByCode("044"),
                nation: Nation.findByCode("157"),
                addressSource: AddressSource.findByCode("BRD")
        )
        personAddressTelephones << new PersonAddressTelephone(personAddress,null,null,null,null,null,null)
        return personAddressTelephones
    }


    private List newPersonAddressTelephones(statusIndicator) {
        def ipidm = PersonIdentificationName.findByBannerId("HOF00714").pidm
        def personAddressTelephones = []
        def personAddress = new PersonAddress(
                pidm: ipidm,
                //sequenceNumber: i_success_sequenceNumber,
                fromDate: new Date(),
                toDate: new Date(),
                streetLine1: "i_success_streetLine1",
                streetLine2: "i_success_streetLine2",
                streetLine3: "i_success_streetLine3",
                city: "TTTTT",
                zip: "TTTTT",
                phoneArea: "TTTTT",
                phoneNumber: "TTTTT",
                phoneExtension: "TTTTT",
                statusIndicator: statusIndicator,
                userData: "TTTTT",
                deliveryPoint: 1,
                correctionDigit: 1,
                carrierRoute: "TTTT",
                goodsAndServiceTaxTaxId: "TTTTT",
                reviewedIndicator: "Y",
                reviewedUser: "TTTTT",
                countryPhone: "TTTT",
                houseNumber: "TTTTT",
                streetLine4: "TTTTT",
                addressType: AddressType.findByCode("PO"),
                state: State.findByCode("MER"),
                county: County.findByCode("044"),
                nation: Nation.findByCode("157"),
                addressSource: AddressSource.findByCode("BRD")
        )
        personAddressTelephones << new PersonAddressTelephone(personAddress,TelephoneType.findByCode('SC'),null,"000","0000000",null,null)

        def personAddress2 = new PersonAddress(
                pidm: ipidm,
                //sequenceNumber: i_success_sequenceNumber,
                fromDate: new Date(),
                toDate: new Date(),
                streetLine1: "i_success_streetLine1",
                streetLine2: "i_success_streetLine2",
                streetLine3: "i_success_streetLine3",
                city: "TTTTT",
                zip: "TTTTT",
                phoneArea: "TTTTT",
                phoneNumber: "TTTTT",
                phoneExtension: "TTTTT",
                statusIndicator: statusIndicator,
                userData: "TTTTT",
                deliveryPoint: 1,
                correctionDigit: 1,
                carrierRoute: "TTTT",
                goodsAndServiceTaxTaxId: "TTTTT",
                reviewedIndicator: "Y",
                reviewedUser: "TTTTT",
                countryPhone: "TTTT",
                houseNumber: "TTTTT",
                streetLine4: "TTTTT",
                addressType: AddressType.findByCode("BI"),
                state: State.findByCode("MER"),
                county: County.findByCode("044"),
                nation: Nation.findByCode("157"),
                addressSource: AddressSource.findByCode("BRD")
        )
        personAddressTelephones << new PersonAddressTelephone(personAddress2,null,null,null,null,null,null)
        return personAddressTelephones
    }

     private def extractAddressTypes(addresses) {
        return addresses.collect { it.addressType?.code}
    }


    private def extractTelephoneTypes(addresses) {
        return addresses.collect { it.telephoneType?.code}
    }
}
