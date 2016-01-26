/*********************************************************************************
  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.HoldType
import net.hedtech.banner.general.system.Originator
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken as UPAT
import org.springframework.security.core.context.SecurityContextHolder
import groovy.sql.Sql
import java.text.SimpleDateFormat

class PersonRelatedHoldServiceIntegrationTests extends BaseIntegrationTestCase {

    def personRelatedHoldService
    def selfServiceBannerAuthenticationProvider

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // (removing SOAHOLD because of GUOBOBS_UI_VERSION = B)
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCreateAndUpdatePersonRelatedHold() {
        //Create the new record by usr "grails_user"
        def personRelatedHold = newPersonRelatedHold()
        personRelatedHold.pidm = null
        def person = PersonUtility.getPerson("HOS00001")
        def pidm = person.pidm
        def keyBlockMap = [pidm: pidm]
        def map = [domainModel: personRelatedHold, keyBlock: keyBlockMap]
        personRelatedHold = personRelatedHoldService.create(map)

        // login as our other user
        def auth = selfServiceBannerAuthenticationProvider.authenticate(new UPAT(person.bannerId, '111111'))
        SecurityContextHolder.getContext().setAuthentication(auth)


        //Test if the generated entity now has an id assigned
        assertNotNull personRelatedHold.id
        assertTrue personRelatedHold.pidm==pidm //This will test preCreate pidm population
        assertEquals "Hold Type not as expected", personRelatedHold.holdType.code, "RG"
        assertEquals "Originator not as expected", personRelatedHold.originator.code, "ACCT"
        personRelatedHold.reason = "YYYY"
        personRelatedHold.lastModifiedBy = person.bannerId

        personRelatedHold = personRelatedHoldService.update([domainModel: personRelatedHold])

        assertEquals "Reason not as expected", "YYYY", personRelatedHold.reason
        assertEquals "grails_user", personRelatedHold.createdBy
        assertEquals person.bannerId, personRelatedHold.lastModifiedBy

        def sql

        //"Update the user_data field directly in DB to systest1
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SPRHOLD set SPRHOLD_USER = 'systest1' where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            sql?.close()
        }

        //Try to update Release Indicator created by "grails-User"  and fail
        personRelatedHold.discard()
        try {
            personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
            assertEquals "systest1", personRelatedHold.createdBy
            personRelatedHold.releaseIndicator = true
            personRelatedHoldService.update([domainModel: personRelatedHold])
            fail("this should have failed, user doing the update is not the user who created the hold")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "releaseIndicatorUpdateNotAllowed"
        }

        //Try to update Hold Type created by "grails-User"  and fail
        personRelatedHold.discard()
        try {
            personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
            personRelatedHold.holdType = HoldType.findByCode("WC")
            personRelatedHoldService.update([domainModel: personRelatedHold])
            fail("this should have failed, user doing the update is not the user who created the hold")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "holdCodeUpdateNotAllowed"
        }

        //Set back user Data field in the DB to "grails_user"
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SPRHOLD set SPRHOLD_USER = 'grails_user' where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            sql?.close()
        }

        //current loggedin user is grails_user, can updated Release indicator and Hold Type.
        logout()
        login("grails_user","u_pick_it")
        personRelatedHold.discard()
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        assertEquals "grails_user", personRelatedHold.createdBy
        personRelatedHold.releaseIndicator = true
        personRelatedHold.holdType = HoldType.findByCode("WC")
        personRelatedHold = personRelatedHoldService.update([domainModel: personRelatedHold])
        assertEquals "Release indicator not as expected", true, personRelatedHold.releaseIndicator

        // Now release Indicator is True, Updated the user in the DB and test all the field are not updatable
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SPRHOLD set SPRHOLD_USER = 'systest1' where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            sql?.close()
        }

        // field reason is not updatable by current logged in user because current logged-in user is grails_user and DB user is systest1
        personRelatedHold.discard()
        try {
            personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
            assertEquals "systest1", personRelatedHold.createdBy
            personRelatedHold.reason = "XXXX"
            personRelatedHoldService.update([domainModel: personRelatedHold])
            fail("this should have failed, user doing the update is not the user who last udated the hold")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "updateNotAllowedByAnotherUser"
        }

        // field fromDate is not updatable by current logged in user because current logged-in user is grails_user and DB user is systest1
        personRelatedHold.discard()
        try {
            personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
            personRelatedHold.fromDate = new Date()
            personRelatedHoldService.update([domainModel: personRelatedHold])
            fail("this should have failed, user doing the update is not the user who last udated the hold")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "updateNotAllowedByAnotherUser"
        }

        // field toDate is not updatable by current logged in user because current logged-in user is grails_user and DB user is systest1
        personRelatedHold.discard()
        try {
            personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
            personRelatedHold.toDate = new Date()
            personRelatedHoldService.update([domainModel: personRelatedHold])
            fail("this should have failed, user doing the update is not the user who last udated the hold")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "updateNotAllowedByAnotherUser"
        }

        // field amount is not updatable by current logged in user because current logged-in user is grails_user and DB user is systest1
        personRelatedHold.discard()
        try {
            personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
            personRelatedHold.amountOwed = 1000.00
            personRelatedHoldService.update([domainModel: personRelatedHold])
            fail("this should have failed, user doing the update is not the user who last udated the hold")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "updateNotAllowedByAnotherUser"
        }

        // field fromDate is not updatable by current logged in user because current logged-in user is grails_user and DB user is systest1
        personRelatedHold.discard()
        try {
            personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
            personRelatedHold.originator = Originator.findByCode("MATH")
            personRelatedHoldService.update([domainModel: personRelatedHold])
            fail("this should have failed, user doing the update is not the user who last udated the hold")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "updateNotAllowedByAnotherUser"
        }

        //Update back the user in the DB to grails_user and verify that fields are updatable.
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SPRHOLD set SPRHOLD_USER = 'grails_user' where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            sql?.close()
        }

        personRelatedHold.discard()
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        assertEquals "grails_user", personRelatedHold.createdBy
        personRelatedHold.reason = "XXXX"
        personRelatedHold.fromDate = new Date()
        personRelatedHold.toDate = new Date()
        personRelatedHold.amountOwed = 5000.00
        personRelatedHold.originator = Originator.findByCode("WC")

        personRelatedHold = personRelatedHoldService.update([domainModel: personRelatedHold])
        assertEquals "Reason not as expected", "XXXX", personRelatedHold.reason

    }


    @Test
    void testDeletePersonRelatedHold() {
        def personRelatedHold = newPersonRelatedHold()
        def map = [domainModel: personRelatedHold]
        personRelatedHold = personRelatedHoldService.create(map)
        //Test if the generated entity now has an id assigned
        assertNotNull personRelatedHold.id
        assertEquals "Hold Type not as expected", personRelatedHold.holdType.code, "RG"
        assertEquals "Originator not as expected", personRelatedHold.originator.code, "ACCT"
        assertEquals "grails_user", personRelatedHold.createdBy

        //Delete the record and verify that it allows to delete
        personRelatedHoldService.delete([domainModel: personRelatedHold])
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        assertNull(personRelatedHold)
    }


    @Test
    void testDeleteWithReleaseIndicatorBySameUser() {
        def personRelatedHold = newPersonRelatedHold()
        personRelatedHold.releaseIndicator = true
        def map = [domainModel: personRelatedHold]
        personRelatedHold = personRelatedHoldService.create(map)
        //Test if the generated entity now has an id assigned
        assertNotNull personRelatedHold.id
        assertEquals "Hold Type not as expected", personRelatedHold.holdType.code, "RG"
        assertEquals "Originator not as expected", personRelatedHold.originator.code, "ACCT"
        assertEquals "Release Indicator not as expected", true, personRelatedHold.releaseIndicator

        //Same user can delete record with release Indicator
        personRelatedHoldService.delete([domainModel: personRelatedHold])
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        assertNull(personRelatedHold)
    }


    @Test
    void testDeleteWithReleaseIndicatorOffByDifferentUser() {
        def sql
        def personRelatedHold = newPersonRelatedHold()
        def map = [domainModel: personRelatedHold]
        personRelatedHold = personRelatedHoldService.create(map)
        //Test if the generated entity now has an id assigned
        assertNotNull personRelatedHold.id
        assertEquals "grails_user", personRelatedHold.createdBy
        assertEquals "Hold Type not as expected", personRelatedHold.holdType.code, "RG"
        assertEquals "Originator not as expected", personRelatedHold.originator.code, "ACCT"

        // Now release Indicator is True, Updated the user in the DB and test all the field are not updatable
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SPRHOLD set SPRHOLD_USER = 'systest1' where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            sql?.close()
        }

        personRelatedHold.discard()
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        assertEquals "systest1", personRelatedHold.createdBy
        personRelatedHoldService.delete([domainModel: personRelatedHold])
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        assertNull(personRelatedHold)
    }


    @Test
    void testDeleteWithReleaseIndicatorOnByDifferentUser() {
        def sql
        def personRelatedHold = newPersonRelatedHold()
        personRelatedHold.releaseIndicator = true
        def map = [domainModel: personRelatedHold]
        personRelatedHold = personRelatedHoldService.create(map)
        assertEquals "Release Indicator not as expected", true, personRelatedHold.releaseIndicator
        assertNotNull personRelatedHold.id
        assertEquals "Hold Type not as expected", personRelatedHold.holdType.code, "RG"
        assertEquals "Originator not as expected", personRelatedHold.originator.code, "ACCT"
        assertEquals "grails_user", personRelatedHold.createdBy
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SPRHOLD set SPRHOLD_USER = 'systest1' where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            sql?.close()
        }


        personRelatedHold.discard()
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        assertEquals "systest1", personRelatedHold.createdBy
        try {
            personRelatedHoldService.delete([domainModel: personRelatedHold])
            fail("this should have failed, user doing the update is not the user who created the hold")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "deleteNotAllowedByAnotherUser"
        }
    }


    @Test
    void testReadOnlyPIDM() {
        def personRelatedHold = newPersonRelatedHold()
        personRelatedHold.releaseIndicator = true
        def map = [domainModel: personRelatedHold]
        personRelatedHold = personRelatedHoldService.create(map)
        map = [domainModel: personRelatedHold]

        map.domainModel.pidm = 222
        try {
            personRelatedHoldService.update([map.domainModel])
            fail("This should have failed with @@r1:readonlyFieldsCannotBeModified")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }


    private def newPersonRelatedHold() {

        def iholdType = HoldType.findWhere(code: "RG")
        def ioriginator = Originator.findWhere(code: "ACCT")
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def fromDate = df1.parse("2010-07-01")
        def toDate = df1.parse("2010-09-01")
        def pidm = PersonUtility.getPerson("HOS00001").pidm


        def personRelatedHold = new PersonRelatedHold(
                pidm: pidm,
                fromDate: fromDate,
                toDate: toDate,
                releaseIndicator: false,
                reason: "TTTTT",
                amountOwed: 1,
                holdType: iholdType,
                originator: ioriginator,
                lastModified: new Date(),
                lastModifiedBy: "test",
                createdBy: "grails_user",
                dataOrigin: "Banner"
        )
        return personRelatedHold
    }

}
