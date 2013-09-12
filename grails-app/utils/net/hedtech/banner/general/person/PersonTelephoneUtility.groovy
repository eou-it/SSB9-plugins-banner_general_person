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

class PersonTelephoneUtility {

    //Public method for formatting a person's telephone number based on the phone record passed in.

    public static String formatPhone(Map personTelephoneMap) {
        def phoneFormat
        if (personTelephoneMap?.phoneInternational) phoneFormat = getInternationalPhoneFormat()
        else phoneFormat = getPhoneFormat()

        def displayPhone = phoneFormat
        if (phoneFormat.contains("\$phoneInternational")) displayPhone = displayPhone.replace("\$phoneInternational", personTelephoneMap?.phoneInternational ?: '')
        if (phoneFormat.contains("\$phoneCountry")) displayPhone = displayPhone.replace("\$phoneCountry", personTelephoneMap?.phoneCountry ?: '')
        if (phoneFormat.contains("\$phoneArea")) displayPhone = displayPhone.replace("\$phoneArea", personTelephoneMap?.phoneArea ?: '')
        if (phoneFormat.contains("\$phoneNumber")) displayPhone = displayPhone.replace("\$phoneNumber", personTelephoneMap?.phoneNumber ?: '')
        if (phoneFormat.contains("\$phoneExtension")) displayPhone = displayPhone.replace("\$phoneExtension", personTelephoneMap?.phoneExtension ?: '')

        displayPhone = displayPhone.trim()
        return displayPhone
    }

    //This will retrieve the default.personTelephone.format from the messages.properties.
    public static String getPhoneFormat() {
        def message = ''
        try {
            def application = ApplicationHolder.application
            ApplicationContext applicationContext = application.mainContext
            def messageSource = applicationContext.getBean("messageSource")
            messageSource.getMessage("default.personTelephone.format", null, LocaleContextHolder.getLocale())
        }
        catch (org.springframework.context.NoSuchMessageException ae) {
            message = "\$phoneCountry, \$phoneArea, \$phoneNumber, \$phoneExtension"
        }

    }

    //This will retrieve the default.personTelephone.international.format from the messages.properties.
    public static String getInternationalPhoneFormat() {
        def message = ''
        try {
            def application = ApplicationHolder.application
            ApplicationContext applicationContext = application.mainContext
            def messageSource = applicationContext.getBean("messageSource")
            messageSource.getMessage("default.personTelephone.international.format", null, LocaleContextHolder.getLocale())
        }
        catch (org.springframework.context.NoSuchMessageException ae) {
            message = "\$phoneInternational"
        }

    }

}
