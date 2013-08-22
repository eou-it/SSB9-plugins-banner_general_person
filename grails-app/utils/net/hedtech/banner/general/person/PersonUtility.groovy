/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes as GA

import groovy.sql.Sql
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder

/**
 * This is a helper class that is used to help common validation and other processing for
 * General Person objects
 *
 */

class PersonUtility {


    public static Boolean isPersonValid(String bannerId) {
        if (!PersonIdentificationName.fetchBannerPerson(bannerId)) {
            return false
        }
        else return true
    }

    public static Object getPerson(String bannerId) {
        def person = getAltPerson(bannerId)
        if (person) {
            return getPerson(person.pidm)
        }
        else return PersonIdentificationName.fetchBannerPerson(bannerId)
    }

    public static Object getAltPerson(String bannerId) {
        return PersonIdentificationName.fetchPersonByAlternativeBannerId(bannerId)
    }

    public static Object getPerson(Integer pidm) {
        return PersonIdentificationName.fetchBannerPerson(pidm)

    }

    public static Boolean isPersonDeceased(Integer pidm) {

        def bioSql =
            """select nvl (spbpers_dead_ind,  'N') dead
        from spbpers where spbpers_pidm = ? """
        def ctx = SCH.servletContext.getAttribute(GA.APPLICATION_CONTEXT)
        def sessionFactory = ctx.sessionFactory
        def session = sessionFactory.currentSession
        def sql = new Sql(session.connection())
        def bio = sql.firstRow(bioSql, [pidm])
        return bio?.dead == 'Y'
    }

    public static Boolean isPersonConfidential(Integer pidm) {
        def confSql = """ select nvl(spbpers_confid_ind, 'N') confidential
                      from spbpers where spbpers_pidm = ? """
        def ctx = SCH.servletContext.getAttribute(GA.APPLICATION_CONTEXT)
        def sessionFactory = ctx.sessionFactory
        def session = sessionFactory.currentSession
        def sql = new Sql(session.connection())
        def conf = sql.firstRow(confSql, [pidm])
        return conf?.confidential == 'Y'
    }

    public static Map isPersonConfidentialOrDeceased(Integer pidm) {
        def sqlQuery = """ select nvl(spbpers_confid_ind, 'N') confidential,  nvl (spbpers_dead_ind,  'N') dead
                      from spbpers where spbpers_pidm = ? """
        def ctx = SCH.servletContext.getAttribute(GA.APPLICATION_CONTEXT)
        def sessionFactory = ctx.sessionFactory
        def session = sessionFactory.currentSession
        def sql = new Sql(session.connection())
        def conf = sql.firstRow(sqlQuery, [pidm])
        return [confidential: conf?.confidential == 'Y', deceased: conf?.dead == 'Y']
    }

    public static String getEmailId(Integer pidm) {
        def emailQuery = """ select goremal_email_address email from goremal
                         where goremal_pidm = ?
                           and goremal_status_ind = ?
                           and goremal_preferred_ind = ? """
        def ctx = SCH.servletContext.getAttribute(GA.APPLICATION_CONTEXT)
        def sessionFactory = ctx.sessionFactory
        def session = sessionFactory.currentSession
        def sql = new Sql(session.connection())
        def conf = sql.firstRow(emailQuery, [pidm, 'A', 'Y'])
        return conf?.email
    }

    //Public method for formatting a person's name based on the LinkedHashMap passed in.
    public static String formatName(person) {
        def nameFormat = getNameFormat()
        def displayName = nameFormat
        if (nameFormat.contains("\$lastName")) displayName = displayName.replace("\$lastName", person?.lastName)
        if (nameFormat.contains("\$firstName")) displayName =  displayName.replace("\$firstName", person?.firstName?:'')
        if (nameFormat.contains("\$mi"))  displayName =  displayName.replace("\$mi", person?.middleName?:'')
        if (nameFormat.contains("\$surnamePrefix"))  displayName =  displayName.replace("\$surnamePrefix", person?.surnamePrefix?:'')
        displayName = displayName.trim()
        return displayName
    }

    //This will retrieve the default.name.format from the messages.properties.
    public static String getNameFormat() {
        def message = ''
        try {
            def application = ApplicationHolder.application
            ApplicationContext applicationContext = application.mainContext
            def messageSource = applicationContext.getBean("messageSource")
            messageSource.getMessage("default.name.format", null, LocaleContextHolder.getLocale())
        }
        catch (org.springframework.context.NoSuchMessageException ae) {
            message = "\$lastName, \$firstName"
        }

    }
}
