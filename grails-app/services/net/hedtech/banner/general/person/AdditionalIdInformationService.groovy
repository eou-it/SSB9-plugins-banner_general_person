/*********************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.person

import grails.gorm.transactions.Transactional
import net.hedtech.banner.service.ServiceBase
import net.hedtech.banner.exceptions.ApplicationException

import java.sql.SQLException

@Transactional
class AdditionalIdInformationService extends ServiceBase {
    def fetchExistingByPidm(reqData){
        def existsDetails
        def goradidExists
        if (reqData.pidm) {
            existsDetails = AdditionalIdInformation.fetchExistingByPidm(reqData.pidm,'USI')
        }
        if(existsDetails){
            goradidExists="Y"

        }
        else {
            goradidExists="N"

        }

        return  goradidExists
    }
    def saveAdditionalIdInformation(def jsonData) {

        String status = "success"
        def existingRecordFound = fetchExistingByPidm(jsonData)

        if(existingRecordFound=="Y") {

            try{
                AdditionalIdInformation existingRecord = AdditionalIdInformation.fetchExistingFullRecordByPidm(jsonData.pidm,'USI')
                existingRecord.additionalId = jsonData.additionalId
                existingRecord.dataOrigin = 'SELF-SERVICE'
                existingRecord.lastModifiedBy = 'SELF-SERVICE'
                existingRecord.lastModified =  new Date()

                this.update([domainModel: existingRecord])
            }
            catch (SQLException | ApplicationException | Exception ae) {
                log.error "Exception while saving into Goradid Table ", ae
                status = "error"
            }
        }
        else {

            try {
                def domain = new AdditionalIdInformation(
                        pidm: jsonData.pidm,
                        lastModified: new Date(),
                        adidCode: 'USI',
                        dataOrigin: 'SELF-SERVICE',
                        additionalId: jsonData.additionalId,
                        lastModifiedBy: 'SELF-SERVICE'
                )
                this.create([domainModel: domain])

            }
            catch (SQLException | ApplicationException | Exception ae) {
                log.error "Exception while saving into Goradid table ", ae
                status = "error"
            }
        }

        return status;

    }
}
