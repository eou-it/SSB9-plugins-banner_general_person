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

import grails.util.GrailsNameUtils
import net.hedtech.banner.exceptions.ApplicationException
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.commons.GrailsClassUtils

import java.sql.CallableStatement

class PersonIdentificationNameCompositeService {

    def personIdentificationNameService
    boolean transactional = true
    def sessionFactory
    def log = Logger.getLogger(this.getClass())


    public def createOrUpdate(map) {

        if (map?.deleteAlternatePersonIdentificationNames) {
            deleteAlternatePersonIdentificationNames(map?.deleteAlternatePersonIdentificationNames, personIdentificationNameService)
        }

        processCurrentInsertUpdates(map?.currentPersonIdentificationNames, personIdentificationNameService)

        processAlternateInsertUpdates(map?.alternatePersonIdentificationNames, personIdentificationNameService)
    }


    /**
     *  Delete alternate id domains
     */
    private def deleteAlternatePersonIdentificationNames(deleteList, service) {

        deleteList.each { domain ->
            def map = [domainModel: domain]
            service.delete(map)

            // Refresh all alternate id records when is an alternate id is deleted.
            // This will refresh any hibernate objects that the PL/SQL api might have deleted/created/updated.
            def updatedPersons = PersonIdentificationName.fetchAllPersonByPidmAndChangeIndicatorNotNull(domain.pidm)
        }
    }

    /**
     *    Insert or update the current person identification records
     */
    private def processCurrentInsertUpdates(domainList, service) {

        domainList.each { domain ->
            if (domain.id) {
                updatePersonIdentificationName(domain)
            } else {
                service.create(domainModel: domain)
            }
        }
    }

    /**
     *    Insert or update the alternate person identification records
     */
    private def processAlternateInsertUpdates(domainList, service) {

        domainList.each { domain ->
            if (domain.id) {
                updatePersonIdentificationName(domain)
            } else {
                service.create(domainModel: domain)
            }
        }
    }

    /**
     * Update the current person identification record.
     * This is a custom update method for the current id record.
     * Within the PL/SQL api, an update is made to the current record
     * and then an alternate id record is created to track the old values.
     */
    private def updatePersonIdentificationName(domain) {

        def connection

        checkReadOnlyProperties(domain)

        try {
            connection = sessionFactory.currentSession.connection()
            CallableStatement sqlCall = buildUpdateCall(connection, domain)
            sqlCall.executeUpdate()
        }
        catch (ApplicationException ae) {
            log.debug "Could not update an existing ${this.class.simpleName} with id = ${domain?.id} due to exception: ${ae.message}", ae
            throw ae
        }
        catch (e) {
            log.debug "Could not update an existing ${this.class.simpleName} with id = ${domain?.id} due to exception: ${e.message}", e
            throw new ApplicationException(domain.class, e)
        } finally {
            connection?.close()
        }

        domain.refresh()

        // Refresh all alternate id records when is the current id is being updated.
        // This will refresh any hibernate objects that the PL/SQL api might have created/updated.
        if (domain.changeIndicator == null) {
            def updatedPersons = PersonIdentificationName.fetchAllPersonByPidmAndChangeIndicatorNotNull(domain.pidm)
        }

        domain
    }

    /**
     * Builds the PL/SQL call to update the SPRIDEN record.
     */
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
        sqlCall.setString(13, domainObject.personRowid)

        sqlCall
    }

    /**
     * Since the update operation calls the PL/SQL API directly (instead of using ServiceBase), we need to
     * manually check the readOnly properties.
     */
    private def checkReadOnlyProperties(domain) {
        def readonlyProperties = GrailsClassUtils.getPropertyOrStaticPropertyOrFieldValue(domain, 'readonlyProperties')
        if (readonlyProperties) {
            def dirtyProperties = domain.getDirtyPropertyNames()
            def modifiedReadOnlyProperties = dirtyProperties?.findAll { it in readonlyProperties }
            if (modifiedReadOnlyProperties.size() > 0) {
                log.warn "Attempt to modify ${domain.class} read-only properties ${modifiedReadOnlyProperties}"
                def cleanNames = modifiedReadOnlyProperties.collect { GrailsNameUtils.getNaturalName(it as String) }
                throw new ApplicationException(domain, "@@r1:readonlyFieldsCannotBeModified:${cleanNames.join(', ')}@@")
            }
        }
    }

}
