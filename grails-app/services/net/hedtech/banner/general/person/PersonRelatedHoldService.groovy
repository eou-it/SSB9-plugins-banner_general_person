/*********************************************************************************
 Copyright 2009-2018 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger
import org.codehaus.groovy.runtime.InvokerHelper
import org.springframework.security.core.context.SecurityContextHolder

class PersonRelatedHoldService extends ServiceBase {
    private static final log = Logger.getLogger(PersonRelatedHoldService.class)
    boolean transactional = true

    def sessionFactory

    void preCreate(map) {
        if(!map.domainModel.pidm)
            map.domainModel.pidm = map.keyBlock.pidm
    }


    void preUpdate(map) {
        validateHoldForUpdate(map.domainModel)
    }


    void preDelete(map) {
        validateHoldForDelete(map.domainModel)
    }

    /**
     * User who created Hold record ==> Can always update the record
     * User created hold type with Release Indicator flag checked ==> No other user can updated any field on this record except the one who originally created
     * User create hold type with Release Indicator flag unchecked ==> Other users can update all the fields except Hold Type and Release Indicator
     * @param hold
     * @return
     */
    //Check view
    private validateHoldForUpdate(PersonRelatedHold hold) {
        log.debug "Compare hold.createdBy: " + hold.createdBy + " to SecurityContextHolder.context?.authentication?.principal: " +
                SecurityContextHolder.context?.authentication?.principal
        if (!(hold.createdBy?.toUpperCase(  ) == SecurityContextHolder.context?.authentication?.principal?.username?.toUpperCase() ||
                hold.createdBy?.toUpperCase(  ) == SecurityContextHolder.context?.authentication?.principal?.oracleUserName?.toUpperCase())) {
            if (findDirty(hold, 'holdType'))
                throw new ApplicationException(PersonRelatedHold, "@@r1:holdCodeUpdateNotAllowed@@")
            if (findDirty(hold, 'releaseIndicator'))
                throw new ApplicationException(PersonRelatedHold, "@@r1:releaseIndicatorUpdateNotAllowed@@")
            if (hold.releaseIndicator) {
                if (findDirty(hold, 'reason'))
                    throw new ApplicationException(PersonRelatedHold, "@@r1:updateNotAllowedByAnotherUser@@")
                if (findDirty(hold, 'fromDate'))
                    throw new ApplicationException(PersonRelatedHold, "@@r1:updateNotAllowedByAnotherUser@@")
                if (findDirty(hold, 'toDate'))
                    throw new ApplicationException(PersonRelatedHold, "@@r1:updateNotAllowedByAnotherUser@@")
                if (findDirty(hold, 'amountOwed'))
                    throw new ApplicationException(PersonRelatedHold, "@@r1:updateNotAllowedByAnotherUser@@")
                if (findDirty(hold, 'originator'))
                    throw new ApplicationException(PersonRelatedHold, "@@r1:updateNotAllowedByAnotherUser@@")
            }
        }
    }

    /**
     * User created hold type with Release Indicator flag checked ==> No other user can updated any field on this record except the one who originally created the record
     *                                                            ==> No other user can delete the record.
     * @param hold
     * @return
     */
    private validateHoldForDelete(PersonRelatedHold hold) {
        if (!(hold.createdBy == SecurityContextHolder.context?.authentication?.principal?.username)) {
            if (hold.releaseIndicator) {
                throw new ApplicationException(PersonRelatedHold, "@@r1:deleteNotAllowedByAnotherUser@@")
            }
        }
    }


    private findDirty(hold, String field) {
        def content = ServiceBase.extractParams(PersonRelatedHold, hold)
        def domainObject = PersonRelatedHold.get(content?.id)
        use(InvokerHelper) {
            domainObject.setProperties(content)
        }
        def changedNames = domainObject.dirtyPropertyNames
        def changed = changedNames.find { it == field}
        return changed
    }


    public PersonRelatedHold fetchById(Long id) {
        return PersonRelatedHold.withSession { session ->
            session.getNamedQuery("PersonRelatedHold.fetchById").setLong('id', id).uniqueResult()
        }
    }

    def getWebDisplayableHolds(def pidm) {

        def result = [rows: [], message: '']
        def list = PersonRelatedHold.fetchByPidmAndDateBetween(new Integer(pidm), new Date())

        if(list.size() == 0) {
            result.message = 'noHoldsExist'
        }
        else {
            def webList = list.findAll { it -> it.holdType.displayWebIndicator }

            if (webList.size() == 0) {
                result.message = 'noWebHoldsExist'
            }
            else {
                webList.each { it ->
                    def holdFor = []
                    if (it.holdType.accountsReceivableHoldIndicator) {
                        holdFor.add('accountsReceivable')
                    }
                    if (it.holdType.registrationHoldIndicator) {
                        holdFor.add('registration')
                    }
                    if (it.holdType.transcriptHoldIndicator) {
                        holdFor.add('transcripts')
                    }
                    if (it.holdType.graduationHoldIndicator) {
                        holdFor.add('graduation')
                    }
                    if (it.holdType.gradeHoldIndicator) {
                        holdFor.add('grades')
                    }
                    if (it.holdType.enrollmentVerificationHoldIndicator) {
                        holdFor.add('enrollmentVerification')
                    }
                    if (it.holdType.applicationHoldIndicator) {
                        holdFor.add('application')
                    }
                    if (it.holdType.complianceHoldIndicator) {
                        holdFor.add('evaluation')
                    }

                    def hold = [
                            hold_for     : holdFor,
                            r_amount_owed: it.amountOwed,
                            r_from_date  : it.fromDate,
                            r_reason     : it.reason,
                            r_to_date    : it.toDate,
                            stvhold_desc : it.holdType.description,
                            stvorig_desc : it.originator?.description
                    ]

                    result.rows.add(hold)
                }
            }
        }

        return result
    }
}
