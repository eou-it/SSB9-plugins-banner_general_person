/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package net.hedtech.banner.general.person
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.general.system.EmailType
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PersonEmailCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def personEmailService
    def personEmailCompositeService


	@Before
	public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


	@After
	public void tearDown() {
        super.tearDown()
    }


	@Test
    void testCreate() {

        def personEmails = newPersonEmails()
        personEmailCompositeService.createOrUpdate(
                [
                        personEmails: personEmails,
                ])
        def ipidm = net.hedtech.banner.general.person.PersonUtility.getPerson("HOS00004").pidm
        def cntPersonEmail = PersonEmail.findByPidm(ipidm)
        assertNotNull "PersonEmail ID is null in PersonEmail Service Tests Create", cntPersonEmail.id
    }


	@Test
    void testPersonEmailDelete() {
        def personEmails = newPersonEmails()
        def pidm = personEmails[0].pidm
        def keyBlock = [pidm: pidm]
        personEmailCompositeService.createOrUpdate([personEmails: personEmails, keyBlock: keyBlock])
        def deletePersonEmail = PersonEmail.findAllByPidm(pidm)
        assertEquals deletePersonEmail.size(), 2

        personEmailCompositeService.createOrUpdate([deletePersonEmails: deletePersonEmail, keyBlock: keyBlock])
        deletePersonEmail = PersonEmail.findAllByPidm(pidm)
        assertEquals deletePersonEmail.size(), 0

    }


	@Test
    void testUpdateOfComment() {

        def personEmail = newSinglePersonEmail()
        personEmailCompositeService.createOrUpdate(
                [
                        personEmails: personEmail,
                ])
        def pidm = personEmail.pidm
        assertNotNull personEmail.id

        personEmail.commentData = "Test Update"
        def personEmails = []
        personEmails << personEmail
        def keyBlock = [pidm: pidm]
        personEmailCompositeService.createOrUpdate([personEmails: personEmails, keyBlock: keyBlock])
        def cntPersonEmail = PersonEmail.findByPidm(pidm)
        assertEquals cntPersonEmail.commentData, "Test Update"
    }


	@Test
    void testUpdateOfEmailType() {

        def personEmail = newSinglePersonEmail()
        personEmailCompositeService.createOrUpdate(
                [
                        personEmails: personEmail,
                ])
        def pidm = personEmail.pidm
        assertNotNull personEmail.id

        personEmail.emailType = EmailType.findByCode("DEN")
        def personEmails = []
        personEmails << personEmail
        def keyBlock = [pidm: pidm]
        personEmailCompositeService.createOrUpdate([personEmails: personEmails, keyBlock: keyBlock])
        def cntPersonEmail = PersonEmail.findByPidm(pidm)
        assertEquals cntPersonEmail.emailType, EmailType.findByCode("DEN")
    }


	@Test
      void testUpdateOfEmailAddress() {

        def personEmail = newSinglePersonEmail()
        personEmailCompositeService.createOrUpdate(
                [
                        personEmails: personEmail,
                ])
        def pidm = personEmail.pidm
        assertNotNull personEmail.id

        personEmail.emailAddress = "updatedAddress@domain.edu"
        def personEmails = []
        personEmails << personEmail
        def keyBlock = [pidm: pidm]
        personEmailCompositeService.createOrUpdate([personEmails: personEmails, keyBlock: keyBlock])
        def cntPersonEmail = PersonEmail.findByPidm(pidm)
        assertEquals cntPersonEmail.emailAddress, "updatedAddress@domain.edu"
    }


	@Test
      void testUpdateOfEmailAddressAndType() {

        def personEmail = newSinglePersonEmail()
        personEmailCompositeService.createOrUpdate(
                [
                        personEmails: personEmail,
                ])
        def pidm = personEmail.pidm
        assertNotNull personEmail.id
        personEmail.emailType = EmailType.findByCode("DEN")
        personEmail.emailAddress = "updatedAddress@domain.edu"
        def personEmails = []
        personEmails << personEmail
        def keyBlock = [pidm: pidm]
        personEmailCompositeService.createOrUpdate([personEmails: personEmails, keyBlock: keyBlock])
        def cntPersonEmail = PersonEmail.findByPidm(pidm)
        assertEquals cntPersonEmail.emailAddress, "updatedAddress@domain.edu"
        assertEquals cntPersonEmail.emailType, EmailType.findByCode("DEN")
    }


	@Test
     void testUpdateOfEmailAddressAndTypeAndComment() {

        def personEmail = newSinglePersonEmail()
        personEmailCompositeService.createOrUpdate(
                [
                        personEmails: personEmail,
                ])
        def pidm = personEmail.pidm
        assertNotNull personEmail.id
        personEmail.emailType = EmailType.findByCode("DEN")
        personEmail.emailAddress = "updatedAddress@domain.edu"
        personEmail.commentData = "testing multiple updates"
        def personEmails = []
        personEmails << personEmail
        def keyBlock = [pidm: pidm]
        personEmailCompositeService.createOrUpdate([personEmails: personEmails, keyBlock: keyBlock])
        def cntPersonEmail = PersonEmail.findByPidm(pidm)
        assertEquals cntPersonEmail.emailAddress, "updatedAddress@domain.edu"
        assertEquals cntPersonEmail.emailType, EmailType.findByCode("DEN")
        assertEquals cntPersonEmail.commentData, "testing multiple updates"
    }



    private def newSinglePersonEmail() {
        def ipidm = net.hedtech.banner.general.person.PersonUtility.getPerson("HOS00004").pidm
        def personEmail = new PersonEmail(
                pidm: ipidm,
                emailAddress: "TTT@msn.com",
                statusIndicator: "A",
                preferredIndicator: false,
                commentData: "TTTTT",
                displayWebIndicator: true,
                emailType: EmailType.findByCode("CAMP")
        )
        return personEmail
    }


    private List newPersonEmails() {
        def ipidm = net.hedtech.banner.general.person.PersonUtility.getPerson("HOS00004").pidm
        def personEmails = []
        def personEmail = new PersonEmail(
                pidm: ipidm,
                emailAddress: "TTT@msn.com",
                statusIndicator: "A",
                preferredIndicator: false,
                commentData: "TTTTT",
                displayWebIndicator: true,
                emailType: EmailType.findByCode("CAMP")
        )
        personEmails << personEmail

        def personEmail2 = new PersonEmail(
                pidm: ipidm,
                emailAddress: "TTTS@msn.com",
                statusIndicator: "A",
                preferredIndicator: false,
                commentData: "TTTTT",
                displayWebIndicator: true,
                emailType: EmailType.findByCode("DEN")
        )
        personEmails << personEmail2
        return personEmails
    }
}
