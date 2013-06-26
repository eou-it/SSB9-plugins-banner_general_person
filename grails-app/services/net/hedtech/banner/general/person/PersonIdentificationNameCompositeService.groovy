/*********************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

import java.sql.CallableStatement

/**
 *  This service is used to process PersonIdentificationName, PersonIdentificationNameCurrent,
 *  and PersonIdentificationNameAlternate changes.
 *  The standard service update behavior for PersonIdentificationNameService, PersonIdentificationNameCurrentService,
 *  and PersonIdentificationNameAlternateService has been overridden in this composite service.
 */
class PersonIdentificationNameCompositeService extends ServiceBase {

    boolean transactional = true
    def sessionFactory
    def log = Logger.getLogger(this.getClass())

    def personIdentificationNameService
    def personIdentificationNameCurrentService
    def personIdentificationNameAlternateService


    public def createOrUpdate(map) {

        if (map?.deletePersonIdentificationNames) {
            deleteDomains(map?.deletePersonIdentificationNames, personIdentificationNameService)
        }

        if (map?.deletePersonIdentificationNameCurrents) {
            deleteDomains(map?.deletePersonIdentificationNameCurrents, personIdentificationNameCurrentService)
        }

        if (map?.deletePersonIdentificationNameAlternates) {
            deleteDomains(map?.deletePersonIdentificationNameAlternates, personIdentificationNameAlternateService)
        }

        if (map?.personIdentificationNames) {
            processInsertUpdates(map?.personIdentificationNames, personIdentificationNameService)
        }

        if (map?.personIdentificationNameCurrent) {
            def personIdentificationNameCurrents = [map?.personIdentificationNameCurrent]  // Make into a list to conform to composite service
            processInsertUpdates(personIdentificationNameCurrents, personIdentificationNameCurrentService)
        }

        if (map?.personIdentificationNameCurrents) {
            processInsertUpdates(map?.personIdentificationNameCurrents, personIdentificationNameCurrentService)
        }

        if (map?.personIdentificationNameAlternates) {
            processInsertUpdates(map?.personIdentificationNameAlternates, personIdentificationNameAlternateService)
        }
    }

    /**
     *  Delete PersonIdentificationName, PersonIdentificationNameCurrent, or PersonIdentificationNameAlternate domains.
     */
    private def deleteDomains(deleteList, service) {

        deleteList.each { domain ->
            def map = [domainModel: domain]
            service.delete(map)

            // Refresh all alternate id records when an alternate id is deleted.
            // Within the PL/SQL api, a delete to an alternate id record (changeIndicator not = null)
            // will also update other alternate id record.
            // This will refresh any hibernate objects that the PL/SQL api may have updated.
            // Note that the current id (changeIndicator = null) cannot be deleted and the PL/SQL api will throw an
            // exception if an attempt is made to do so.
            // TODO Change following query to use the alternate service
            if (domain?.changeIndicator) {
                def updatedPersons = PersonIdentificationName.fetchAllByPidmAndChangeIndicatorNotNull(domain.pidm)
            }
        }
    }

    /**
     * Insert or update the PersonIdentificationName,  PersonIdentificationNameCurrent,
     * or PersonIdentificationNameAlternate domains
     */
    private def processInsertUpdates(domainList, service) {

        domainList.each { domain ->
            if (domain.id) {
                updateDomain(domain)
            } else {
                service.create(domainModel: domain)
            }
        }
    }

    /**
     * This is a custom update method for the person identification record.
     * If an update is made to the current id record (changeIndicator = null),
     * the PL/SQL api will update the current id record and then an alternate id record
     * is created to track the old values.
     */
    private def updateDomain(domain) {

        preUpdate(domain)

        def connection
        def domainObject

        try {
            def content = extractParams(domain.class, domain, log)
            domainObject = fetch(domain.class, content?.id, log)
            domainObject.properties = content
            def dirty = isDirty(domainObject)

            if (dirty) {
                checkOptimisticLock(domainObject, content, log)
                validateReadOnlyPropertiesNotDirty(domainObject)

                connection = sessionFactory.currentSession.connection()
                CallableStatement sqlCall = buildUpdateCall(connection, domainObject)
                sqlCall.executeUpdate()

                domainObject.refresh()

                // Refresh all alternate id records if the current id is updated.
                // This will refresh any hibernate objects that the PL/SQL api might have created or updated.
                if (!domainObject?.changeIndicator) {
                    def updatedPersons = PersonIdentificationName.fetchAllByPidmAndChangeIndicatorNotNull(domainObject.pidm)
                }
            }

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

        domainObject
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
        sqlCall.setString(10, domainObject.nameType?.code)
        sqlCall.setString(11, domainObject.dataOrigin)
        sqlCall.setString(12, domainObject.surnamePrefix)
        sqlCall.setString(13, domainObject.personRowid)

        sqlCall
    }

    /**
     *  Verify that the change indicator is correct for the type of identification (current versus alternate).
     */
    private def preUpdate(domain) {
        if (domain instanceof PersonIdentificationNameCurrent) {
            if (domain?.changeIndicator) {
                throw new ApplicationException(PersonIdentificationNameCurrent, "@@r1:changeIndicatorMustBeNull@@")
            }
        } else if (domain instanceof PersonIdentificationNameAlternate) {
            if (!domain?.changeIndicator) {
                throw new ApplicationException(PersonIdentificationNameAlternate, "@@r1:changeIndicatorCannotBeNull@@")
            }
        }

    }

}
