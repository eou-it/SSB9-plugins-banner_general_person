/** *******************************************************************************
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

package com.sungardhe.banner.general.person

import com.sungardhe.banner.exceptions.ApplicationException
import com.sungardhe.banner.general.system.HoldType
import com.sungardhe.banner.general.system.Originator
import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import java.text.SimpleDateFormat

class PersonRelatedHoldServiceIntegrationTests extends BaseIntegrationTestCase {

    def personRelatedHoldService


    protected void setUp() {
        formContext = ['SOAHOLD']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateAndUpdatePersonRelatedHold() {
        def personRelatedHold = newPersonRelatedHold()
        def map = [domainModel: personRelatedHold]
        personRelatedHold = personRelatedHoldService.create(map)
        //Test if the generated entity now has an id assigned
        assertNotNull personRelatedHold.id
        assertEquals "Hold Type not as expected", personRelatedHold.holdType.code, "RG"
        assertEquals "Originator not as expected", personRelatedHold.originator.code, "ACCT"
        personRelatedHold.reason = "YYYY"
        personRelatedHold = personRelatedHoldService.update([domainModel: personRelatedHold])
        assertEquals "Reason not as expected", "YYYY", personRelatedHold.reason
        println(' the version before is ' + personRelatedHold.version)
        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SPRHOLD set SPRHOLD_USER = 'systest1' where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            sql?.close()
        }
        personRelatedHold.discard()
        try {
            personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
            personRelatedHold.releaseIndicator = true
            personRelatedHoldService.update([domainModel: personRelatedHold])
            fail("this should have failed, user doing the update is not the user who last udated the hold")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "invalidHoldUser"
        }
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SPRHOLD set SPRHOLD_USER = 'grails_user' where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            sql?.close()
        }
        personRelatedHold.discard()
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        personRelatedHold.releaseIndicator = true
        personRelatedHold = personRelatedHoldService.update([domainModel: personRelatedHold])
        assertEquals "Release indicator not as expected", true, personRelatedHold.releaseIndicator

        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SPRHOLD set SPRHOLD_USER = 'systest1' where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            sql?.close()
        }
        personRelatedHold.discard()
        try {
            personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
            personRelatedHold.reason = "XXXX"
            personRelatedHoldService.update([domainModel: personRelatedHold])
            fail("this should have failed, user doing the update is not the user who last udated the hold")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "invalidHoldUser"
        }
        personRelatedHold.discard()
        try {
            personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
            personRelatedHold.fromDate = new Date()
            personRelatedHoldService.update([domainModel: personRelatedHold])
            fail("this should have failed, user doing the update is not the user who last udated the hold")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "invalidHoldUser"
        }
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SPRHOLD set SPRHOLD_USER = 'grails_user' where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            sql?.close()
        }
        personRelatedHold.discard()
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        personRelatedHold.reason = "XXXX"
        personRelatedHold = personRelatedHoldService.update([domainModel: personRelatedHold])
        assertEquals "Reason not as expected", "XXXX", personRelatedHold.reason

    }


    void testCreateAndDeletePersonRelatedHold() {
        def personRelatedHold = newPersonRelatedHold()
        def map = [domainModel: personRelatedHold]
        personRelatedHold = personRelatedHoldService.create(map)
        //Test if the generated entity now has an id assigned
        assertNotNull personRelatedHold.id
        assertEquals "Hold Type not as expected", personRelatedHold.holdType.code, "RG"
        assertEquals "Originator not as expected", personRelatedHold.originator.code, "ACCT"
        personRelatedHold.reason = "YYYY"
        personRelatedHold = personRelatedHoldService.update([domainModel: personRelatedHold])
        assertEquals "Reason not as expected", "YYYY", personRelatedHold.reason
        personRelatedHoldService.delete([domainModel: personRelatedHold])
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        assertNull(personRelatedHold)
    }


    void testCreateAndDeleteWithReleaseIndicatorBySameUser() {
        def sql
        def personRelatedHold = newPersonRelatedHold()
        def map = [domainModel: personRelatedHold]
        personRelatedHold = personRelatedHoldService.create(map)
        //Test if the generated entity now has an id assigned
        assertNotNull personRelatedHold.id
        assertEquals "Hold Type not as expected", personRelatedHold.holdType.code, "RG"
        assertEquals "Originator not as expected", personRelatedHold.originator.code, "ACCT"
        personRelatedHold.userData = "grails_user"
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SPRHOLD set SPRHOLD_USER = 'grails_user' where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            sql?.close()
        }
        personRelatedHold.discard()
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        personRelatedHold.releaseIndicator = true
        personRelatedHold = personRelatedHoldService.update([domainModel: personRelatedHold])
        assertEquals "Release Indicator not as expected", true, personRelatedHold.releaseIndicator
        personRelatedHoldService.delete([domainModel: personRelatedHold])
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        assertNull(personRelatedHold)
    }


    void testCreateAndDeleteWithReleaseIndicatorByDifferentUserWithReleaseIndicatorOff() {
        def sql
        def personRelatedHold = newPersonRelatedHold()
        def map = [domainModel: personRelatedHold]
        personRelatedHold = personRelatedHoldService.create(map)
        //Test if the generated entity now has an id assigned
        assertNotNull personRelatedHold.id
        assertEquals "Hold Type not as expected", personRelatedHold.holdType.code, "RG"
        assertEquals "Originator not as expected", personRelatedHold.originator.code, "ACCT"
        personRelatedHoldService.delete([domainModel: personRelatedHold])
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        assertNull(personRelatedHold)
    }


    void testCreateAndDeleteWithReleaseIndicatorByDifferentUserWithReleaseIndicator() {
        def sql
        def personRelatedHold = newPersonRelatedHold()
        def map = [domainModel: personRelatedHold]
        personRelatedHold = personRelatedHoldService.create(map)
        //Test if the generated entity now has an id assigned
        assertNotNull personRelatedHold.id
        assertEquals "Hold Type not as expected", personRelatedHold.holdType.code, "RG"
        assertEquals "Originator not as expected", personRelatedHold.originator.code, "ACCT"
        // personRelatedHold.userData = "grails_user"
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SPRHOLD set SPRHOLD_USER = 'grails_user' where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            sql?.close()
        }
        personRelatedHold.discard()
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        personRelatedHold.releaseIndicator = true
        personRelatedHold = personRelatedHoldService.update([domainModel: personRelatedHold])
        assertEquals "Release Indicator not as expected", true, personRelatedHold.releaseIndicator
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SPRHOLD set SPRHOLD_USER = 'TTTTT' where SPRHOLD_SURROGATE_ID = ?", [personRelatedHold.id])
        } finally {
            sql?.close()
        }
        personRelatedHold.discard()
        personRelatedHold = PersonRelatedHold.findById(personRelatedHold.id)
        try {
            personRelatedHoldService.delete([domainModel: personRelatedHold])
            fail("this should have failed, user doing the update is not the user who created the hold")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "invalidHoldUserForDelete"
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
                userData: "TTTTT",
                fromDate: fromDate,
                toDate: toDate,
                releaseIndicator: false,
                reason: "TTTTT",
                amountOwed: 1,
                holdType: iholdType,
                originator: ioriginator,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return personRelatedHold
    }

}
