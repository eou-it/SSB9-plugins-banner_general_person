/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase
import groovy.sql.Sql

import java.sql.DatabaseMetaData

// NOTE:
// This service is only used for readonly/query operations on SPRIDEN.  Any CUD operation must be done through the
// PersonIdentificationNameCurrentService or PersonIdentificationNameAlternateService.

class PersonIdentificationNameService extends ServiceBase {

    boolean transactional = true
    def sessionFactory


    def preCreate(map) {
        throw new ApplicationException(PersonIdentificationNameCurrent, "@@r1:unsupported.operation@@")
    }

    def preDelete(map) {
        throw new ApplicationException(PersonIdentificationNameCurrent, "@@r1:unsupported.operation@@")
    }

    def preUpdate(map) {
        throw new ApplicationException(PersonIdentificationNameCurrent, "@@r1:unsupported.operation@@")
    }


    def fetchEntityOfPerson(pidm) {
        def sql
        def entity
        sql = null
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())

            sql.call("{? = call gb_identification.f_get_entity(?)}",
                    [Sql.VARCHAR, pidm]) { result -> entity = result }
        }
        finally {
            sql?.close()
        }

        return entity
    }


    def getSystemGeneratedIdPrefix() {
        def sql
        def idPrefix
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.call("{? = call sokfunc.f_get_idprefix(?)}",
                    [Sql.VARCHAR, 'ID']) { result -> idPrefix = result }
        }
        finally {
            sql?.close()
        }

        return idPrefix
    }


     def getPrefixDisplayIndicatorForSelfService() {

        def prefixInd
        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.call("{? = call gb_displaymask.f_ssb_format_name()}",
                    [Sql.VARCHAR]) { result -> prefixInd = result }
        }
        finally {
            sql?.close()
        }
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
        try {
            DatabaseMetaData dmd = sessionFactory.getCurrentSession().connection().getMetaData()
            String url = dmd.getURL()
            println 'Database URL ====> '+ url
            println 'Database UserName ====>' + dmd.getUserName()
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.call("{? = call f_format_name(?,?)}",
                    [Sql.VARCHAR, pidm, fmt]) { result -> formattedName = result }
        }
        finally {
            sql?.close()
        }
        println 'fromat ===> ' +fmt
        println 'pidm ===>  ' +pidm
        println 'formattedName ===> ' +formattedName
        return formattedName
    }

}
