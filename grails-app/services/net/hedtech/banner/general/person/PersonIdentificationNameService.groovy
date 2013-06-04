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
 ********************************************************************************* */

package net.hedtech.banner.general.person

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase
import org.springframework.transaction.interceptor.TransactionAspectSupport

import java.sql.CallableStatement

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
 * !! OVERRIDES THE SERVICE BASE UPDATE METHOD !!
 * We override this method because the PL/SQL API will perform both an update and an insert on the same table (SPRIDEN)
 * when a name change or id change happens. Due to the behavior of the ON INSTEAD and surrogate id triggers, this causes a unique key constraint
 * error to occur on the insert statement.
 */
    public def update(domainModelOrMap, flushImmediately = true) {
        log.debug " ${this.class.simpleName} .update IS AN OVERRIDE TO THE NORMAL UPDATE METHOD"
        log.debug "${this.class.simpleName}.create invoked with domainModelOrMap = $domainModelOrMap and flushImmediately = $flushImmediately"
        log.trace "${this.class.simpleName}.create transaction attributes: ${TransactionAspectSupport?.currentTransactionInfo()?.getTransactionAttribute()}"
        setDbmsApplicationInfo " ${this.class.simpleName} .update()"

        def content
        def domainObject
        def connection

        try {
            content = extractParams(getDomainClass(), domainModelOrMap, log)
            domainObject = fetch(getDomainClass(), content?.id, log)

            // Now we'll set the provided properties (content) onto our pristine domainObject instance -- this may make the model dirty
            domainObject.properties = content

            if (isDirty(domainObject)) {
                log.trace "${this.class.simpleName}.update will update model with dirty properties ${domainObject.getDirtyPropertyNames()?.join(", ")}"

                checkOptimisticLock(domainObject, content, log)
                validateReadOnlyPropertiesNotDirty(domainObject)

                domainObject = invokeServicePreUpdate(domainModelOrMap, domainObject)

                connection = sessionFactory.getCurrentSession().connection()
                CallableStatement sqlCall = buildUpdateCall(connection, domainObject)
                sqlCall.executeUpdate()
            }

            refreshIfNeeded(domainObject)

            log.trace "${this.class.simpleName}.update will now invoke the postUpdate callback if it exists"
            if (this.respondsTo('postUpdate')) {
                this.postUpdate([before: domainModelOrMap, after: domainObject])
            }

            domainObject
        }
        catch (ApplicationException ae) {
            log.debug "Could not update an existing ${this.class.simpleName} with id = ${domainModelOrMap?.id} due to exception: ${ae.message}", ae
            throw ae
        }
        catch (ValidationException e) {
            def ae = new ApplicationException(getDomainClass(), e)
            log.debug "Could not update an existing ${this.class.simpleName} with id = ${domainObject?.id} due to exception: $ae", e
            checkOptimisticLock(domainObject, content, log) // optimistic lock trumps validation errors
            throw ae
        }
        catch (e) {
            log.debug "Could not update an existing ${this.class.simpleName} with id = ${domainModelOrMap?.id} due to exception: ${e.message}", e
            throw new ApplicationException(getDomainClass(), e)
        } finally {
            connection?.close()
            clearDbmsApplicationInfo()
        }
    }


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


    private def buildUpdateCall(connection, domainObject) {

        String createIdentification = "{ call gb_identification.p_update(?,?,?,?,?,?,?,?,?,?,?,?,?) }"
        CallableStatement sqlCall = connection.prepareCall(createIdentification)

        if (domainObject.pidm == null) {
            sqlCall.setNull(1, java.sql.Types.INTEGER)
        } else {
            sqlCall.setInt(1, domainObject.pidm)
        }
        sqlCall.setString(2, domainObject.bannerId)
        sqlCall.setString(3, domainObject.lastName)
        sqlCall.setString(4, domainObject.firstName)
        sqlCall.setString(5, domainObject.middleName)
        sqlCall.setString(6, domainObject.changeIndicator)
        sqlCall.setString(7, domainObject.entityIndicator)
        sqlCall.setString(8, domainObject.createUser)
        sqlCall.setString(9, domainObject.origin)
        sqlCall.setString(10, domainObject.nameType.code)
        sqlCall.setString(11, domainObject.dataOrigin)
        sqlCall.setString(12, domainObject.surnamePrefix)
        sqlCall.setNull(13, java.sql.Types.VARCHAR)   // rowid is passed as null since domain doesn't track it

        sqlCall
    }
}
