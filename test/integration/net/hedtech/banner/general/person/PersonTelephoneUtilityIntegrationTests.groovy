/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import net.hedtech.banner.general.system.TelephoneType
import net.hedtech.banner.person.dsl.NameTemplate
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.junit.Ignore
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder

class PersonTelephoneUtilityIntegrationTests extends BaseIntegrationTestCase {

    //Valid test data (For success tests)
    def i_success_telephoneType
    def i_success_addressType

    def i_success_pidm = 999
    def i_success_sequenceNumber = 1
    def i_success_phoneArea = "607"
    def i_success_phoneNumber = "1234567"
    def i_success_phoneExtension = "987"
    def i_success_statusIndicator = ""
    def i_success_addressSequenceNumber = null
    def i_success_primaryIndicator = "Y"
    def i_success_unlistIndicator = null
    def i_success_commentData = "This is a comment"
    def i_success_internationalAccess = "123.456.78909"
    def i_success_countryPhone = "9"


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
        initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    void initializeTestDataForReferences() {
        //Valid test data (For success tests)
        i_success_telephoneType = TelephoneType.findByCode("GR")

    }


    protected void tearDown() {
        super.tearDown()
    }


    void testFormatPhone() {
        def personTelephone = newValidPersonTelephone()
        save personTelephone
        //Test if the generated entity now has an id assigned
        assertNotNull personTelephone.id

        //get the phone format from General Person Plugin messages.properties
        def application = ApplicationHolder.application
        ApplicationContext applicationContext = application.mainContext
        def messageSource = applicationContext.getBean("messageSource")
        def phoneFormat = messageSource.getMessage("default.personTelephone.format", null, LocaleContextHolder.getLocale())
        assertEquals "\$phoneCountry \$phoneArea \$phoneNumber \$phoneExtension", phoneFormat

        def personTelephoneMap = [phoneCountry: personTelephone?.countryPhone, phoneArea: personTelephone?.phoneArea, phoneNumber: personTelephone?.phoneNumber, phoneExtension: personTelephone?.phoneExtension]
        def formattedPhone = PersonTelephoneUtility.formatPhone(personTelephoneMap)
        //perform the replacement here to compare the results
        def displayPhone = phoneFormat
        if (phoneFormat.contains("\$phoneInternational")) displayPhone = displayPhone.replace("\$phoneInternational", personTelephone?.internationalAccess ?: '')
        if (phoneFormat.contains("\$phoneCountry")) displayPhone = displayPhone.replace("\$phoneCountry", personTelephone?.countryPhone ?: '')
        if (phoneFormat.contains("\$phoneArea")) displayPhone = displayPhone.replace("\$phoneArea", personTelephone?.phoneArea ?: '')
        if (phoneFormat.contains("\$phoneNumber")) displayPhone = displayPhone.replace("\$phoneNumber", personTelephone?.phoneNumber ?: '')
        if (phoneFormat.contains("\$phoneExtension")) displayPhone = displayPhone.replace("\$phoneExtension", personTelephone?.phoneExtension ?: '')

        assertEquals formattedPhone, displayPhone
        assertEquals "9 607 1234567 987", formattedPhone
    }


    void testFormatInternationalPhone() {
        def personTelephone = newValidPersonInternationalTelephone()
        save personTelephone
        //Test if the generated entity now has an id assigned
        assertNotNull personTelephone.id

        //get the phone format from General Person Plugin messages.properties
        def application = ApplicationHolder.application
        ApplicationContext applicationContext = application.mainContext
        def messageSource = applicationContext.getBean("messageSource")
        def phoneFormat = messageSource.getMessage("default.personTelephone.international.format", null, LocaleContextHolder.getLocale())

        def personTelephoneMap = [phoneInternational: personTelephone?.internationalAccess]
        def formattedPhone = PersonTelephoneUtility.formatPhone(personTelephoneMap)
        //perform the replacement here to compare the results
        def displayPhone = phoneFormat
        if (phoneFormat.contains("\$phoneInternational")) displayPhone = displayPhone.replace("\$phoneInternational", personTelephone?.internationalAccess ?: '')
        if (phoneFormat.contains("\$phoneCountry")) displayPhone = displayPhone.replace("\$phoneCountry", personTelephone?.countryPhone ?: '')
        if (phoneFormat.contains("\$phoneArea")) displayPhone = displayPhone.replace("\$phoneArea", personTelephone?.phoneArea ?: '')
        if (phoneFormat.contains("\$phoneNumber")) displayPhone = displayPhone.replace("\$phoneNumber", personTelephone?.phoneNumber ?: '')
        if (phoneFormat.contains("\$phoneExtension")) displayPhone = displayPhone.replace("\$phoneExtension", personTelephone?.phoneExtension ?: '')

        assertEquals formattedPhone, displayPhone
        assertEquals "123.456.78909", formattedPhone
    }

    //Confirm that if there is international phone info, it will use that format.
    void testFormatInternationalPhoneWhenCountryExists() {
        def personTelephone = newValidPersonTelephoneWithInternationalAndCountry()
        save personTelephone
        //Test if the generated entity now has an id assigned
        assertNotNull personTelephone.id

        //get the phone format from General Person Plugin messages.properties
        def application = ApplicationHolder.application
        ApplicationContext applicationContext = application.mainContext
        def messageSource = applicationContext.getBean("messageSource")
        def phoneFormat = messageSource.getMessage("default.personTelephone.international.format", null, LocaleContextHolder.getLocale())

        def personTelephoneMap = [phoneInternational: personTelephone.internationalAccess, phoneCountry: personTelephone.countryPhone, phoneArea: personTelephone.phoneArea, phoneNumber: personTelephone.phoneNumber, phoneExtension: personTelephone.phoneExtension]
        def formattedPhone = PersonTelephoneUtility.formatPhone(personTelephoneMap)
        //perform the replacement here to compare the results
        def displayPhone = phoneFormat
        if (phoneFormat.contains("\$phoneInternational")) displayPhone = displayPhone.replace("\$phoneInternational", personTelephone?.internationalAccess ?: '')
        if (phoneFormat.contains("\$phoneCountry")) displayPhone = displayPhone.replace("\$phoneCountry", personTelephone?.countryPhone ?: '')
        if (phoneFormat.contains("\$phoneArea")) displayPhone = displayPhone.replace("\$phoneArea", personTelephone?.phoneArea ?: '')
        if (phoneFormat.contains("\$phoneNumber")) displayPhone = displayPhone.replace("\$phoneNumber", personTelephone?.phoneNumber ?: '')
        if (phoneFormat.contains("\$phoneExtension")) displayPhone = displayPhone.replace("\$phoneExtension", personTelephone?.phoneExtension ?: '')

        assertEquals formattedPhone, displayPhone
        assertEquals "123.456.78909", formattedPhone
    }


    private def newValidPersonTelephone() {
        def personTelephone = new PersonTelephone(
                pidm: i_success_pidm,
                sequenceNumber: i_success_sequenceNumber,
                phoneArea: i_success_phoneArea,
                phoneNumber: i_success_phoneNumber,
                phoneExtension: i_success_phoneExtension,
                statusIndicator: i_success_statusIndicator,
                addressSequenceNumber: i_success_addressSequenceNumber,
                primaryIndicator: i_success_primaryIndicator,
                unlistIndicator: i_success_unlistIndicator,
                commentData: i_success_commentData,
                countryPhone: i_success_countryPhone,
                telephoneType: i_success_telephoneType,
                addressType: i_success_addressType,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return personTelephone
    }


    private def newValidPersonInternationalTelephone() {
        def personTelephone = new PersonTelephone(
                pidm: i_success_pidm,
                sequenceNumber: i_success_sequenceNumber,
                statusIndicator: i_success_statusIndicator,
                addressSequenceNumber: i_success_addressSequenceNumber,
                primaryIndicator: i_success_primaryIndicator,
                unlistIndicator: i_success_unlistIndicator,
                commentData: i_success_commentData,
                internationalAccess: i_success_internationalAccess,
                telephoneType: i_success_telephoneType,
                addressType: i_success_addressType,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return personTelephone
    }


    private def newValidPersonTelephoneWithInternationalAndCountry() {
        def personTelephone = new PersonTelephone(
                pidm: i_success_pidm,
                sequenceNumber: i_success_sequenceNumber,
                phoneArea: i_success_phoneArea,
                phoneNumber: i_success_phoneNumber,
                phoneExtension: i_success_phoneExtension,
                statusIndicator: i_success_statusIndicator,
                addressSequenceNumber: i_success_addressSequenceNumber,
                primaryIndicator: i_success_primaryIndicator,
                unlistIndicator: i_success_unlistIndicator,
                commentData: i_success_commentData,
                internationalAccess: i_success_internationalAccess,
                countryPhone: i_success_countryPhone,
                telephoneType: i_success_telephoneType,
                addressType: i_success_addressType,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return personTelephone
    }
}

