/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.person

import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes as GA
import groovy.sql.Sql

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
        return PersonIdentificationName.fetchBannerPerson(bannerId)
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
        def sql = new Sql (session.connection ())
        def bio = sql.firstRow (bioSql, [pidm])
        return bio?.dead == 'Y'
    }

}
