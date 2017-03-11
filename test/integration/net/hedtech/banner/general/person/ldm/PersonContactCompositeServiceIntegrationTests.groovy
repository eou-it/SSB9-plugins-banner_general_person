/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.person.PersonUtility
import net.hedtech.banner.general.person.ldm.v7.PersonContactDecorator
import net.hedtech.banner.general.person.ldm.v7.PersonEmergencyAddressDecorator
import net.hedtech.banner.general.person.view.PersonEmergencyContactView
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.codehaus.groovy.grails.plugins.testing.GrailsMockHttpServletRequest
import org.junit.Before
import org.junit.Test


class PersonContactCompositeServiceIntegrationTests extends BaseIntegrationTestCase {
    PersonContactCompositeService personContactCompositeService
    Integer o_success_pidm
    PersonEmergencyContactView o_success_personEmergencyContact
    String o_success_personGuid
    String i_success_priority = "2"
    List o_success_types = ["emergency"]

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeDataReferences()
    }

    private void initializeDataReferences() {
        o_success_pidm = PersonUtility.getPerson("HOSFE2000").pidm
        o_success_personGuid = GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(o_success_pidm.toString(), 'persons')?.guid
        o_success_personEmergencyContact = PersonEmergencyContactView.findByPidmAndPriority(o_success_pidm, i_success_priority)
    }

    @Test
    void testGet_PersonContact_v7() {
        setAcceptHeader("application/vnd.hedtech.integration.v7+json")
        PersonContactDecorator decorator = personContactCompositeService.get(o_success_personEmergencyContact.guid)
        assertNotNull decorator
        assertEquals o_success_personEmergencyContact.guid, decorator.guid
        assertEquals o_success_personGuid, decorator.personGuid
        assertEquals o_success_personGuid, decorator.getPerson().id
        assertNotNull decorator.contact
        assertEquals decorator.contact, decorator.getContacts()[0]
        assertEquals o_success_personEmergencyContact.priority.toInteger(), decorator.contact.priority
        assertEquals o_success_types, decorator.contact.types
        assertEquals o_success_personEmergencyContact.firstName, decorator.contact.name.firstName
        assertEquals o_success_personEmergencyContact.middleInitial, decorator.contact.name.middleName
        assertEquals o_success_personEmergencyContact.lastName, decorator.contact.name.lastName
        String fullName = decorator.contact.name.getFullName(o_success_personEmergencyContact.firstName, o_success_personEmergencyContact.middleInitial, o_success_personEmergencyContact.lastName)
        assertEquals fullName, decorator.contact.name.fullName
        assertNotNull decorator.contact.contactAddress.get("address")
        PersonEmergencyAddressDecorator address = decorator.contact.contactAddress.get("address")
        assertEquals address.getAddressLinesForAddress(o_success_personEmergencyContact), address.addressLines
        assertEquals o_success_personEmergencyContact.city, address.place.country.locality
        assertEquals o_success_personEmergencyContact.zip, address.place.country.postalCode
        assertEquals o_success_personEmergencyContact.addressTypeGuid, address.addressTypeGuid
        assertEquals o_success_personEmergencyContact.addressTypeGuid, address.type.detail.id
        assertEquals decorator.contact.phone, decorator.contact.phones[0]
        String o_success_PhoneNumber = (o_success_personEmergencyContact.phoneArea ?: "") + (o_success_personEmergencyContact.phoneNumber ?: "")
        assertEquals o_success_PhoneNumber, decorator.contact.phone.number
        assertEquals o_success_personEmergencyContact.phoneExtension, decorator.contact.phone.extension
        assertEquals o_success_personEmergencyContact.relationshipTypeGuid, decorator.contact.relationshipGuid
        assertEquals o_success_personEmergencyContact.relationshipTypeGuid, decorator.contact.relationship.detail.id
    }

    @Test
    void testGet_InvalidPersonContact_v7() {
        setAcceptHeader("application/vnd.hedtech.integration.v7+json")
        String guid = 'xxxxx'
        try {
            personContactCompositeService.get(guid)
            fail('Invalid guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, 'NotFoundException'
        }
    }

    @Test
    void testList_PersonContact_v7() {
        setAcceptHeader("application/vnd.hedtech.integration.v7+json")
        Map params = ["person": o_success_personGuid]
        List<PersonContactDecorator> decorators = personContactCompositeService.list(params)
        assertTrue decorators.size() > 0
        PersonContactDecorator decorator = decorators.find { it.contact.priority == i_success_priority.toInteger() }
        assertNotNull decorator
        assertEquals o_success_personEmergencyContact.guid, decorator.guid
        assertEquals o_success_personGuid, decorator.personGuid
        assertEquals o_success_personGuid, decorator.getPerson().id
        assertNotNull decorator.contact
        assertEquals decorator.contact, decorator.getContacts()[0]
        assertEquals o_success_personEmergencyContact.priority.toInteger(), decorator.contact.priority
        assertEquals o_success_types, decorator.contact.types
        assertEquals o_success_personEmergencyContact.firstName, decorator.contact.name.firstName
        assertEquals o_success_personEmergencyContact.middleInitial, decorator.contact.name.middleName
        assertEquals o_success_personEmergencyContact.lastName, decorator.contact.name.lastName
        String fullName = decorator.contact.name.getFullName(o_success_personEmergencyContact.firstName, o_success_personEmergencyContact.middleInitial, o_success_personEmergencyContact.lastName)
        assertEquals fullName, decorator.contact.name.fullName
        assertNotNull decorator.contact.contactAddress.get("address")
        PersonEmergencyAddressDecorator address = decorator.contact.contactAddress.get("address")
        assertEquals address.getAddressLinesForAddress(o_success_personEmergencyContact), address.addressLines
        assertEquals o_success_personEmergencyContact.city, address.place.country.locality
        assertEquals o_success_personEmergencyContact.zip, address.place.country.postalCode
        assertEquals o_success_personEmergencyContact.addressTypeGuid, address.addressTypeGuid
        assertEquals o_success_personEmergencyContact.addressTypeGuid, address.type.detail.id
        assertEquals decorator.contact.phone, decorator.contact.phones[0]
        String o_success_PhoneNumber = (o_success_personEmergencyContact.phoneArea ?: "") + (o_success_personEmergencyContact.phoneNumber ?: "")
        assertEquals o_success_PhoneNumber, decorator.contact.phone.number
        assertEquals o_success_personEmergencyContact.phoneExtension, decorator.contact.phone.extension
        assertEquals o_success_personEmergencyContact.relationshipTypeGuid, decorator.contact.relationshipGuid
        assertEquals o_success_personEmergencyContact.relationshipTypeGuid, decorator.contact.relationship.detail.id
        assertTrue personContactCompositeService.count(params) >= decorators.size()
    }


    private void setAcceptHeader(String mediaType) {
        GrailsMockHttpServletRequest request = LdmService.getHttpServletRequest()
        request.addHeader("Accept", mediaType)
    }

}
