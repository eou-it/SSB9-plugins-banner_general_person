/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder

/**
 * This is a helper class that is used to help common validation and other processing for
 * General Person objects
 *
 */

class PersonAddressUtility {

    //Public method for formatting a person's address

    public static Map formatDefaultAddress(Map personAddressMap) {
        def addressFormat
        def addressLine
        def displayAddress = [:]
        addressFormat = getAddressFormat( "default.personAddress.line1.format")
        if (addressFormat) displayAddress.addressLine1 = formatAddressLine(addressFormat,personAddressMap)
        addressFormat = getAddressFormat( "default.personAddress.line2.format")
        if (addressFormat) displayAddress.addressLine2 = formatAddressLine(addressFormat,personAddressMap)
        addressFormat = getAddressFormat( "default.personAddress.line3.format")
        if (addressFormat) displayAddress.addressLine3 = formatAddressLine(addressFormat,personAddressMap)
        addressFormat = getAddressFormat( "default.personAddress.line4.format")
        if (addressFormat) displayAddress.addressLine4 = formatAddressLine(addressFormat,personAddressMap)
        addressFormat = getAddressFormat( "default.personAddress.line5.format")
        if (addressFormat) displayAddress.addressLine5 = formatAddressLine(addressFormat,personAddressMap)
        addressFormat = getAddressFormat( "default.personAddress.line6.format")
        if (addressFormat) displayAddress.addressLine6 = formatAddressLine(addressFormat,personAddressMap)
        addressFormat = getAddressFormat( "default.personAddress.line7.format")
        if (addressFormat) displayAddress.addressLine7 = formatAddressLine(addressFormat,personAddressMap)
        addressFormat = getAddressFormat( "default.personAddress.line8.format")
        if (addressFormat) displayAddress.addressLine8 = formatAddressLine(addressFormat,personAddressMap)
        return displayAddress
    }


    public static String getAddressFormat(lineProperty) {
        def message = ''
        try {
            def application = ApplicationHolder.application
            ApplicationContext applicationContext = application.mainContext
            def messageSource = applicationContext.getBean("messageSource")
            messageSource.getMessage(lineProperty, null, LocaleContextHolder.getLocale())
        }
        catch (org.springframework.context.NoSuchMessageException ae) {
            message = null
        }

    }


    public static formatAddressLine(String addressFormat,Map personAddressMap){
        def displayLine = addressFormat
        if ((personAddressMap?.displayHouseNumber))  {
            if (addressFormat.contains("\$houseNumber"))
                displayLine = displayLine.replace("\$houseNumber", personAddressMap?.houseNumber ?: '')   }
        else displayLine = displayLine.replace("\$houseNumber",'')

        if (addressFormat.contains("\$streetLine1")) displayLine = displayLine.replace("\$streetLine1", personAddressMap?.streetLine1 ?: '')
        if (addressFormat.contains("\$streetLine2")) displayLine = displayLine.replace("\$streetLine2", personAddressMap?.streetLine2 ?: '')
        if (addressFormat.contains("\$streetLine3")) displayLine = displayLine.replace("\$streetLine3", personAddressMap?.streetLine3 ?: '')
        if ((personAddressMap?.displayStreetLine4))  {
            if (addressFormat.contains("\$streetLine4")) displayLine = displayLine.replace("\$streetLine4", personAddressMap?.streetLine4 ?: '')
        } else displayLine = displayLine.replace("\$streetLine4", '')
        if (addressFormat.contains("\$city")) displayLine = displayLine.replace("\$city", personAddressMap?.city ?: '')
        if (addressFormat.contains("\$state")) displayLine = displayLine.replace("\$state", personAddressMap?.state ?: '')
        if (addressFormat.contains("\$county")) displayLine = displayLine.replace("\$county", personAddressMap?.county ?: '')

        if (addressFormat.contains("\$zip")) displayLine = displayLine.replace("\$zip", personAddressMap?.zip ?: '')
        if (addressFormat.contains("\$country")) displayLine = displayLine.replace("\$country", personAddressMap?.country ?: '')

        displayLine = displayLine.trim()
        return displayLine
    }

}
