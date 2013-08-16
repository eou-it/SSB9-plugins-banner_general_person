/*********************************************************************************
Copyright 2013 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.MedicalCondition
import net.hedtech.banner.service.ServiceBase
import groovy.sql.Sql

class MedicalInformationCompositeService {

    def medicalInformationService
    def institutionalDescriptionService
    boolean transactional = true
    def sessionFactory
    /**
     * Medical submit changes driver
     * Input map = keyBlock ([pidm)
     * Map names are:
     * keyBlock   (required)
     * deleteMedicalInformations (optional)
     * medicalInformations (optional)
     *
     * Composite service is required to handle when the medical condition is changed.  The primary key for the medical information
     * includes the medical condition.  The form allows that to be changed but the API does not.  Because we never want to override the update 
     * from the serviceBase class this service will examine the medical information update and do the delete and create if the medical condition
     * has been changed.
     */

    def createOrUpdate(map) {

        if (map?.deleteMedicalInformations) {
            deleteDomain(map?.deleteMedicalInformations, medicalInformationService, map?.keyBlock)
        }


        processInsertUpdates(map?.medicalInformations,
                medicalInformationService,
                map?.keyBlock)


    }

    /**
     *    Insert all new records
     *    If the existing record's medi  code was changed delete it and reinsert
     */

    private void processInsertUpdates(medicalList, service, Map keyBlock) {


        medicalList.each { test ->
            def map = [keyBlock: keyBlock, domainModel: test]
            if (test.id) {
                def oldMedicalCondition = findDirtyMedical(test)
                if (oldMedicalCondition) {
                    validateHR(test, oldMedicalCondition)
                    MedicalInformation newMed = new MedicalInformation(test.properties)
                    def delMap = [keyBlock: keyBlock, domainModel: test]

                    service.delete(delMap)
                    def newMap = [keyBlock: keyBlock, domainModel: newMed]
                    service.create(newMap)
                }
                else {
                    service.update(map)
                }

            }
            else {
                test = service.create(map)
            }
        }
    }

    /**
     * find out if the medical condition has been changed
     */

    private def findDirtyMedical(domain) {

        def keyMap = ["medicalCondition"]

        def content = ServiceBase.extractParams(MedicalInformation, domain)
        def domainObject = MedicalInformation.get(content?.id) //  ServiceBase.fetch(MedicalInformation, content?.id, log)
        domainObject.properties = content
        domainObject.version = content.version
        def changedNames = domainObject.dirtyPropertyNames

        if (changedNames.size() > 0) {
            def diff = changedNames - keyMap
            if (!(diff == changedNames))
                return domainObject.getPersistentValue('medicalCondition')
            else return null
        }
        else return null
    }

    /**
     *  If HR is installed and the PIDM is in HR the medical condition cannot be changed if they have another record in the pereacc
     * with the same medical condition
     */

    private def validateHR(domain, oldMedicalCondition) {
        if (institutionalDescriptionService.findByKey().hrInstalled) {
            def medicalInformation = domain
            MedicalCondition previousMedicalCondition = oldMedicalCondition

            if (previousMedicalCondition != medicalInformation.medicalCondition) {
                String HRsql = """select pereacc_medi_code from pereacc where pereacc_pidm = ? and pereacc_medi_code = ?"""
                Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
                def mediCode = sql.rows(HRsql, [medicalInformation.pidm, previousMedicalCondition.code])
                if (mediCode.size() > 0) {
                    throw new ApplicationException(MedicalInformation, "@@r1:cannotChangeEmployeeMedical@@")
                }
            }
        }
    }

    /**
     *  Delete  domain
     */

    private void deleteDomain(deleteList, service, Map keyBlockMap) {

        deleteList.each { tests ->
            def map = [keyBlock: keyBlockMap, domainModel: tests]
            service.delete(map)
        }
    }

}
