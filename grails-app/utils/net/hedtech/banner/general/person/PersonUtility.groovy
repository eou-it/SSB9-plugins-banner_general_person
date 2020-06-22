/*********************************************************************************
 Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import grails.util.Holders
import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.service.ServiceBase
import grails.util.Holders as SCH
import org.grails.web.util.GrailsApplicationAttributes as GA
import org.hibernate.SessionFactory
import org.codehaus.groovy.runtime.InvokerHelper
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.web.context.request.RequestContextHolder

import java.sql.CallableStatement

/**
 * This is a helper class that is used to help common validation and other processing for
 * General Person objects
 *
 */

class PersonUtility {

    static final PERSON_CONFIG = 'personConfig'
    static final PROXY_PREFERRED_NAME_PRODUCT_NAME = 'Banner Proxy'
    static final PROXY_PREFERRED_NAME_APPLICATION_NAME = 'ProxyAccess'


    public static Boolean isPersonValid(String bannerId) {
        if (!PersonIdentificationName.fetchBannerPerson(bannerId)) {
            return false
        } else return true
    }


    public static Object getPerson(String bannerId) {
        def person = getAltPerson(bannerId)
        if (person) {
            return getPerson(person.pidm)
        } else return PersonIdentificationName.fetchBannerPerson(bannerId)
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
        SessionFactory sessionFactory = Holders.getGrailsApplication().getMainContext().sessionFactory
        def session = sessionFactory.currentSession
        def sql = new Sql(session.connection())
        def bio = sql.rows(bioSql, [pidm])[0]
        return bio?.dead == 'Y'
    }


    public static Boolean isPersonConfidential(Integer pidm) {
        def confSql = """ select nvl(spbpers_confid_ind, 'N') confidential
                      from spbpers where spbpers_pidm = ? """
        SessionFactory sessionFactory = Holders.getGrailsApplication().getMainContext().sessionFactory
        def session = sessionFactory.currentSession
        def sql = new Sql(session.connection())
        def conf = sql.rows(confSql, [pidm])[0]
        return conf?.confidential == 'Y'
    }


    public static Map isPersonConfidentialOrDeceased(Integer pidm) {
        def sqlQuery = """ select nvl(spbpers_confid_ind, 'N') confidential,  nvl (spbpers_dead_ind,  'N') dead
                      from spbpers where spbpers_pidm = ? """
        SessionFactory sessionFactory = Holders.getGrailsApplication().getMainContext().sessionFactory
        def session = sessionFactory.currentSession
        def sql = new Sql(session.connection())
        def conf = sql.rows(sqlQuery, [pidm])[0]
        return [confidential: conf?.confidential == 'Y', deceased: conf?.dead == 'Y']
    }


    public static String getEmailId(Integer pidm) {
        def emailQuery = """ select goremal_email_address email from goremal
                         where goremal_pidm = ?
                           and goremal_status_ind = ?
                           and goremal_preferred_ind = ? """
        SessionFactory sessionFactory = Holders.getGrailsApplication().getMainContext().sessionFactory
        def session = sessionFactory.currentSession
        def sql = new Sql(session.connection())
        def conf = sql.rows(emailQuery, [pidm, 'A', 'Y'])[0]
        return conf?.email
    }

    public static String getEmailIdByCodeAndStatus(Integer pidm,String emailCode,String status) {
        def emailQuery = """ select goremal_email_address email from goremal
                         where goremal_pidm = ?
                           and goremal_status_ind = ?
                           and goremal_emal_code = ? """
        //def ctx = SCH.servletContext.getAttribute(GA.APPLICATION_CONTEXT)
       // def sessionFactory = ctx.sessionFactory
        SessionFactory sessionFactory = Holders.getGrailsApplication().getMainContext().sessionFactory
        def session = sessionFactory.currentSession
        def sql = new Sql(session.connection())
        def conf = sql.rows(emailQuery, [pidm, status,emailCode])[0]
        return conf?.email
    }

    //Public method for formatting a person's name based on the LinkedHashMap passed in.
    public static String formatName(person) {
        if (!person) return null
        def nameFormat = getNameFormat()
        def displayName = nameFormat
        if (nameFormat.contains("\$lastName")) displayName = displayName.replace("\$lastName", person?.lastName)
        if (nameFormat.contains("\$firstName")) displayName = displayName.replace("\$firstName", person?.firstName ?: '')
        if (nameFormat.contains("\$mi")) displayName = displayName.replace("\$mi", person?.middleName ?: '')
        if (nameFormat.contains("\$surnamePrefix")) displayName = displayName.replace("\$surnamePrefix", person?.surnamePrefix ?: '')
        displayName = displayName.trim()
        return displayName
    }

    //This will retrieve the default.name.format from the messages.properties.
    public static String getNameFormat() {
        def message = ''
        try {
            def application = Holders.getGrailsApplication()
            ApplicationContext applicationContext = application.mainContext
            def messageSource = applicationContext.getBean("messageSource")
            messageSource.getMessage("default.name.format", null, LocaleContextHolder.getLocale())
        }
        catch (org.springframework.context.NoSuchMessageException ae) {
            message = "\$lastName, \$firstName"
        }

    }


    public static def boolean isDirtyProperty(domainClass, domainObject, String property) {
        def content = ServiceBase.extractParams(domainClass, domainObject)
        def oldDomainObject = domainClass.get(content?.id)
        use(InvokerHelper) {
            oldDomainObject.setProperties(content)
        }
        return (property in oldDomainObject.dirtyPropertyNames)
    }

    /**
     *  Checks if the object version in the session is different from the version of the object of the same ID
     *  stored in the database. Needed in certain scenarios where concurrent data editing issues
     *  occur before Hibernate is able to perform an optimistic locking check.
     */
    public static checkForOptimisticLockingError(sessionObject, objectClass, optimisticLockingErrorMessage){
        def objectInDatabase = objectClass?.get(sessionObject?.id)
        def optimisticLockingError = sessionObject?.version != objectInDatabase?.version
        if (optimisticLockingError){
            throw new ApplicationException("", optimisticLockingErrorMessage)
        }
    }


    public static boolean isPiiActive() {
        def connection
        CallableStatement sqlCall
        def piiActive = "N"
        SessionFactory sessionFactory = Holders.getGrailsApplication().getMainContext().sessionFactory


        try {
            connection = sessionFactory.currentSession.connection()
            sqlCall = connection.prepareCall("{ ? = call gokfgac.f_spriden_pii_active }")
            sqlCall.registerOutParameter(1, java.sql.Types.VARCHAR)
            sqlCall.executeUpdate()
            piiActive = sqlCall.getString(1)
        }
        finally {
           // sqlCall?.close()
        }

        return (piiActive == "Y")
    }


    public static turnFgacOff() {
        def connection
        CallableStatement sqlCall
        SessionFactory sessionFactory = Holders.getGrailsApplication().getMainContext().sessionFactory

        try {
            connection = sessionFactory.currentSession.connection()
            sqlCall = connection.prepareCall("{ call gokfgac.p_turn_fgac_off }")
            sqlCall.executeUpdate()
        }
        finally {
          //TODO grails3  sqlCall?.close()
        }
    }


    public static turnFgacOn() {
        def connection
        CallableStatement sqlCall
        SessionFactory sessionFactory = Holders.getGrailsApplication().getMainContext().sessionFactory


        try {
            connection = sessionFactory.currentSession.connection()
            sqlCall = connection.prepareCall("{ call gokfgac.p_turn_fgac_on }")
            sqlCall.executeUpdate()
        }
        finally {
          //TODO grails3  sqlCall?.close()
        }
    }


    public static getPreferredName (params){
        def ctx = Holders.getGrailsApplication().getMainContext()
        def preferredNameService = ctx.preferredNameService
        def productName=Holders?.config?.productName ?: null
        def applicationName=Holders?.config?.banner.applicationName ?: null

        if(productName)
            params.put("productname", productName)
        if(applicationName)
            params.put("appname", applicationName)

        return preferredNameService.getPreferredName(params)
    }

    /*Proxies always use the same name display rule to view the names of the students
    * who proxied them.*/
    public static getPreferredNameForProxyDisplay (studentPidm) {
        def context = Holders.getGrailsApplication().getMainContext()
        def preferredNameService = context.preferredNameService
        def proxyNameDisplayParams = ["pidm": studentPidm, "productname": PROXY_PREFERRED_NAME_PRODUCT_NAME, "appname": PROXY_PREFERRED_NAME_APPLICATION_NAME]
        return preferredNameService.getPreferredName(proxyNameDisplayParams)
    }

    public static setPersonConfigInSession(config) {
        def session = RequestContextHolder.currentRequestAttributes().request.session

        session.setAttribute(PERSON_CONFIG, config)
    }

    public static getPersonConfigFromSession() {
        def session = RequestContextHolder.currentRequestAttributes().request.session

        def personConfig = session.getAttribute(PERSON_CONFIG)

        (personConfig == null) ? [:] : personConfig
    }

    /**
     * Get sequence from GORICCR data in which Person items should be displayed in UI.
     * As sequences are fetched, cache them in a configuration map in the current session.
     * @param sequenceCacheProperty Property name this sequence should be cached under
     * @param sequenceConfig Map of properties used to extract sequence from GORICCR. Properties are
     *        processCode and settingName.
     * @return Sequence list specified by sequenceCacheProperty. Will be pulled from cache, if it already exists.
     */
    public static getDisplaySequence(sequenceCacheProperty, sequenceConfig) {
        if (!sequenceConfig) return null; // Sequence configuration is required

        def personConfig = getPersonConfigFromSession()

        if (personConfig[sequenceCacheProperty]) {
            return personConfig[sequenceCacheProperty] // Use cache
        }

        def processCode = sequenceConfig?.processCode
        def settingName = sequenceConfig?.settingName
        def sequence = [:]
        def itemList = IntegrationConfiguration.fetchAllByProcessCodeAndSettingName(processCode, settingName)

        itemList.each {it ->
            sequence[it.value] = it.sequenceNumber
        }

        personConfig[sequenceCacheProperty] = sequence
        setPersonConfigInSession(personConfig)

        sequence
    }
}
