/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import grails.util.Holders
import net.hedtech.banner.general.system.TelephoneType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder
import org.junit.Before
import org.junit.Test
import org.junit.After

class PersonAddressUtilityIntegrationTests extends BaseIntegrationTestCase {


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testFormatAddress() {
        def personAddressMap = [houseNumber:"30",streetLine1:"Marigold Drive",streetLine2:"Off Shasta Main Rd",state:"PA",displayHouseNumber:true]

        def application = Holders.getGrailsApplication()
        ApplicationContext applicationContext = application.mainContext
        def messageSource = applicationContext.getBean("messageSource")
        def lineFormat = messageSource.getMessage("default.personAddress.line1.format", null, LocaleContextHolder.getLocale())
        assertEquals "\$streetLine1", lineFormat

        def formattedAddress = PersonAddressUtility.formatDefaultAddress(personAddressMap)
        //perform the replacement here to compare the results

        assertEquals formattedAddress.addressLine1,"Marigold Drive"
        assertEquals formattedAddress.addressLine2, "Off Shasta Main Rd"
        assertEquals formattedAddress.addressLine3, ''
        assertEquals formattedAddress.addressLine4, "PA"
    }



}

