/*********************************************************************************
Copyright 2012-2017 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
 package net.hedtech.banner.general.person

import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

class PersonEmailService extends ServiceBase {

    boolean transactional = true


    void preUpdate(domainModelOrMap) {
        def domain = domainModelOrMap instanceof Map ? domainModelOrMap?.domainModel : domainModelOrMap

        if (domain) {
            def dirtyProperties = domain.getDirtyPropertyNames()
            def changes = dirtyProperties.findAll { it == "emailType" || it == 'emailAddress' }
            if (changes.size() > 0) {
                log.warn "Attempt to modify ${domain.class} primary key ${changes}"
                throw new RuntimeException("@@r1:primaryKeyFieldsCannotBeModified@@")
            }
        }
    }

    /**
     * Fetch Active Person Email Details
     * @param pidms
     * @return
     */
   List<PersonEmail> fetchAllActiveEmails(List<Integer> pidms,Set<String> codes){
      return PersonEmail.fetchListByActiveStatusPidmsAndEmailTypes(pidms,codes)
    }

    List<PersonEmail>fetchAllEmails(Integer pidm,Set<String> codes){
        return PersonEmail.fetchListByPidmAndEmailTypes(pidm, codes)
    }

    def getDisplayableEmails(pidm) {
        def emailList = PersonEmail.fetchByPidmAndActiveAndWebDisplayable(pidm)

        def emails = []
        emailList.each {
            def email = [:]

            email.emailType = [
                code: it.emailType.code,
                description: it.emailType.description,
                urlIndicator: it.emailType.urlIndicator
            ]

            email.emailAddress = it.emailAddress
            email.preferredIndicator = it.preferredIndicator
            email.commentData = it.commentData
            email.displayWebIndicator = it.displayWebIndicator

            email.id = it.id
            email.version = it.version

            emails << email
        }

        emails
    }

    def castEmailForUpdate (email) {
        def personEmail = email as PersonEmail
        personEmail.id = email.id
        personEmail.version = email.version

        personEmail
    }

    def updatePreferredEmail (email) {
        if(email.preferredIndicator){
            def existingEmails = PersonEmail.fetchByPidmAndActiveAndWebDisplayable(email.pidm)
            def oldPreferredEmail = existingEmails.find { it.preferredIndicator }

            if(oldPreferredEmail) {
                oldPreferredEmail.preferredIndicator = false
                update([domainModel: oldPreferredEmail])
            }
        }
    }

    def inactivateEmail (email) {
        def personEmail = castEmailForUpdate(email)
        personEmail.preferredIndicator = false
        personEmail.statusIndicator = 'I'
        update([domainModel: personEmail])
    }

    def updateIfExistingEmail(email) {
        def existingEmail = fetchByPidmAndTypeAndAddress(email.pidm, email.emailType.code, email.emailAddress)[0]
        if(existingEmail != null && existingEmail.statusIndicator != 'A') {
            email.id = existingEmail.id
            email.version = existingEmail.version
            email.statusIndicator = 'A'
            email = castEmailForUpdate(email)
        }
        return email
    }

    List fetchByPidmAndTypeAndAddress(Integer pidm, String typeCode, String emailAddress) {
        def emails = PersonEmail.withSession { session ->
            session.getNamedQuery('PersonEmail.fetchByPidmAndTypeAndAddress')
                    .setInteger('pidm', pidm)
                    .setString('emailType', typeCode)
                    .setString('emailAddress', emailAddress)
                    .list()
        }

        return emails
    }
}
