/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import net.hedtech.banner.general.system.TelephoneType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder

class PersonAddressUtilityIntegrationTests extends BaseIntegrationTestCase {


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testFormatAddress() {
        def personAddressMap = [houseNumber:"30",streetLine1:"Marigold Drive",streetLine2:"Off Shasta Main Rd",state:"PA",displayHouseNumber:true]

        def application = ApplicationHolder.application
        ApplicationContext applicationContext = application.mainContext
        def messageSource = applicationContext.getBean("messageSource")
        def lineFormat = messageSource.getMessage("default.personAddress.line1.format", null, LocaleContextHolder.getLocale())
        assertEquals "\$houseNumber \$streetLine1", lineFormat

        def formattedAddress = PersonAddressUtility.formatDefaultAddress(personAddressMap)
        //perform the replacement here to compare the results

        assertEquals formattedAddress.addressLine1,"30 Marigold Drive"
        assertEquals formattedAddress.addressLine2, "Off Shasta Main Rd"
        assertEquals formattedAddress.addressLine7, "PA"
        assertEquals formattedAddress.addressLine6, ''
    }



}

