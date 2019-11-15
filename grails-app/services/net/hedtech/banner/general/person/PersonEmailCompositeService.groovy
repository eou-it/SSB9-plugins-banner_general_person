/*********************************************************************************
Copyright 2012-2019 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
 package net.hedtech.banner.general.person

import grails.gorm.transactions.Transactional
import net.hedtech.banner.i18n.MessageHelper
import net.hedtech.banner.service.ServiceBase
import org.codehaus.groovy.runtime.InvokerHelper

@Transactional
class PersonEmailCompositeService {

    def personEmailService
    def sessionFactory
    /**
     * Composite service is required to handle when the e-mail type or e-mail address is changed.  The primary key for the e-mail information
     * includes the e-mail code, e-mail address and pidm.  The form allows that to be changed but the API does not.  Because we never want to override the update
     * from the serviceBase class this service will examine the e-mail update and do the delete and create if the e-mail type or e-mail address
     * has been changed.
     */

    def createOrUpdate(map) {

        if (map?.deletePersonEmails) {
            deleteDomain(map?.deletePersonEmails, personEmailService)
        }

        if (map?.personEmails) {
            processInsertUpdates(map?.personEmails, personEmailService)
        }
    }


    /**
     *    Insert all new records
     *    If the existing record's e-mail code or e-mail address was changed delete it and reinsert
     */
    private void processInsertUpdates(domains, service) {

        domains.each { domain ->
            if (domain.id == null)
                service.create(domain)
            else if (domain.id) {
                PersonUtility.checkForOptimisticLockingError(domain, PersonEmail,
                        MessageHelper.message("default.optimistic.locking.failure", MessageHelper.message("personInfo.title.email")))
                if (findIfPrimaryKeyChanged(domain)) {
                    PersonEmail newEmail = new PersonEmail(
                            emailAddress: domain.properties.emailAddress,
                            emailType: domain.properties.emailType,
                            commentData: domain.properties.commentData,
                            pidm: domain.properties.pidm,
                            displayWebIndicator: domain.properties.displayWebIndicator,
                            preferredIndicator: domain.properties.preferredIndicator
                    )
                    def delMap = [domainModel: domain]

                    service.delete(delMap)
                    def newMap = [domainModel: newEmail]
                    service.create(newMap)
                } else {
                    service.update([domainModel:domain])
                }
            }
        }
    }


    /**
     * find out if email type or address has been changed
     */
    private def findIfPrimaryKeyChanged(domain) {

        def content = ServiceBase.extractParams(PersonEmail, domain)
        def domainObject = PersonEmail.get(content?.id) //  ServiceBase.fetch(PersonEmail, content?.id, log)
        use(InvokerHelper) {
            domainObject.setProperties(content)
        }
        domainObject.version = content.version
        def changedNames = domainObject.dirtyPropertyNames

        if (changedNames.size() > 0) {
           def diff = changedNames.findAll{ it == "emailType" || it == 'emailAddress'}
           if (diff.size() > 0)
                return true
           else return false
        } else return false
    }


    /**
     *  Delete  domain
     */
    private void deleteDomain(domains, service) {
        domains.each { domain ->
            service.delete(domain)
        }
    }

    /**
     * Get Vendor Email Address
     * @param request code
     * @return
     */
    def fetchPreferredEmailAddress( vendorPidm ) {
        def vendorEmail = personEmailService.findPreferredEmailAddress( vendorPidm )
        return [emailAddress : vendorEmail ? vendorEmail.emailAddress : '']
    }

    /**
     * Get Vendor Email Address
     * @param request code
     * @return
     */
    def fetchEmailAddressList( vendorPidm, Map attrs, Map pagingParams ) {
        def list = []
        if(vendorPidm != null ) {
            def vendorEmail = personEmailService.findPersonEmailAddressList(vendorPidm, attrs, pagingParams)
            vendorEmail.each {
                list.add([
                        emailAddress: it.emailAddress
                ])
            }
        }
        return list
    }

}
