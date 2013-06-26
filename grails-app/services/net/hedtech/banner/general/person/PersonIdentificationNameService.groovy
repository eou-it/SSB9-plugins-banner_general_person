/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/

package net.hedtech.banner.general.person

import net.hedtech.banner.service.ServiceBase
import groovy.sql.Sql

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

class PersonIdentificationNameService extends ServiceBase {

    boolean transactional = true

    def sessionFactory

    /**
     * Please put all the custom methods in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personidentificationname_custom_service_methods) ENABLED START*/


    def fetchEntityOfPerson(pidm) {
        def sql
        def entity
        sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.call("{? = call gb_identification.f_get_entity(?)}",
                [Sql.VARCHAR, pidm]) { result -> entity = result }
        sql?.close()
        return entity
    }


    def getSystemGeneratedIdPrefix() {
        def sql
        def idPrefix
        sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.call("{? = call sokfunc.f_get_idprefix(?)}",
                [Sql.VARCHAR, 'ID']) { result -> idPrefix = result }
        sql?.close()
        return idPrefix
    }


     def getPrefixDisplayIndicatorForSelfService() {
        def sql
        def prefixInd
        sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.call("{? = call gb_displaymask.f_ssb_format_name()}",
                [Sql.VARCHAR]) { result -> prefixInd = result }
        sql?.close()
        return prefixInd
    }


     /**
      *  Calls stand alone function (sufname.sql) f_format_name to return a formatted name.
      * @param pidm The pidm of the name to be returned
      * @parm fmt The format of the name to be returned.
      * @return formattedName
      *
      *      LF30 - Last name, first name for 30 characters
      *      L30 -  Last name for 30 characters
      *      L60 -  Last name for 60 characters
      *      FL30 - First name, last name for 30 characters
      *      FL   - Last name, first name
      *      FMIL - First name, middle initial, last name
      *      FML  - First name, middle name, last name
      *      LFMI - Last name, first name, middle initial
      *      LFM  - Last name, first name, middle
      *      LFIMI30 - Last name, first name initial, middle name initial
      *                for 30 characters
      *
     */
    def getFormattedName(pidm, fmt) {
        def sql
        def formattedName
        sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.call("{? = call f_format_name(?,?)}",
                [Sql.VARCHAR, pidm, fmt]) { result -> formattedName = result }
        sql?.close()
        return formattedName
    }


    /*PROTECTED REGION END*/
}
