/*******************************************************************************
 Copyright 2009-2012 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package net.hedtech.banner.general.person

import net.hedtech.banner.general.system.TelephoneType
import net.hedtech.banner.person.PersonTelephoneDecorator
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonTelephoneDecoratorIntegrationTests extends BaseIntegrationTestCase {

    def personTelephoneService


    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testNullNewPersonTelephone() {
        def newPersonTelephone = new PersonTelephoneDecorator()
        assertNull newPersonTelephone.pidm
        assertNull newPersonTelephone.sequenceNumber
        assertNull newPersonTelephone.phoneArea
        assertNull newPersonTelephone.phoneNumber
        assertNull newPersonTelephone.phoneExtension
        assertNull newPersonTelephone.statusIndicator
        assertNull newPersonTelephone.addressSequenceNumber
        assertNull newPersonTelephone.primaryIndicator
        assertNull newPersonTelephone.unlistIndicator
        assertNull newPersonTelephone.commentData
        assertNull newPersonTelephone.internationalAccess
        assertNull newPersonTelephone.countryPhone
        assertNull newPersonTelephone.telephoneType
        assertNull newPersonTelephone.addressType
        assertNull newPersonTelephone.displayPhone
    }


    void testNewSectionFromSprtele() {
        def personPidm = PersonUtility.getPerson("HOF00746").pidm
        def personTelephone = PersonTelephone.fetchActiveTelephoneByPidmAndTelephoneType(personPidm, 'MA')
        assertNotNull personTelephone.pidm
        assertNotNull personTelephone.telephoneType
        def newPersonTelephone = new PersonTelephoneDecorator(personTelephone)
        assertEquals newPersonTelephone.phoneArea, "252"
        assertEquals newPersonTelephone.phoneNumber, "8205871"
        assertEquals newPersonTelephone.displayPhone, "252 8205871"
    }


    void testNewSectionFromMap() {
        def personPidm = PersonUtility.getPerson("HOF00746").pidm
        def newPersonTelephone = new PersonTelephoneDecorator([pidm: personPidm,
                phoneArea: "111",
                phoneNumber: "123-4567",
                phoneExtension: "x987",
                primaryIndicator: "Y",
                telephoneType: TelephoneType.findByCode("PR")])
        assertEquals newPersonTelephone.phoneArea, "111"
        assertEquals newPersonTelephone.phoneNumber, "123-4567"
        assertEquals newPersonTelephone.phoneExtension, "x987"
        assertEquals newPersonTelephone.primaryIndicator, "Y"
        assertEquals newPersonTelephone.telephoneType, TelephoneType.findByCode("PR")
        assertEquals "111 123-4567 x987", newPersonTelephone.displayPhone
    }


}