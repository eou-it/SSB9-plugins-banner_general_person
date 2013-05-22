/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.AdmissionRequest
import net.hedtech.banner.general.system.SourceAndBackgroundInstitution
import net.hedtech.banner.testing.BaseIntegrationTestCase

class PriorCollegeServiceIntegrationTests extends BaseIntegrationTestCase {

    def priorCollegeService


    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testPriorCollegeValidCreate() {
        def priorCollege = newValidForCreatePriorCollege()
        def map = [domainModel: priorCollege]
        priorCollege = priorCollegeService.create(map)
        assertNotNull "PriorCollege ID is null in PriorCollege Service Tests Create", priorCollege.id
        assertNotNull "PriorCollege sourceAndBackgroundInstitution is null in PriorCollege Service Tests", priorCollege.sourceAndBackgroundInstitution
        assertNotNull "PriorCollege admissionRequest is null in PriorCollege Service Tests", priorCollege.admissionRequest
        assertNotNull priorCollege.pidm
        assertNotNull priorCollege.officialTransaction
        assertNotNull priorCollege.version
        assertNotNull priorCollege.dataOrigin
        assertNotNull priorCollege.lastModifiedBy
        assertNotNull priorCollege.lastModified
    }


    void testPriorCollegeInvalidCreate() {
        def priorCollege = newInvalidForCreatePriorCollege()
        def map = [domainModel: priorCollege]
        shouldFail(ApplicationException) {
            priorCollegeService.create(map)
        }
    }


    void testPriorCollegeValidUpdate() {
        def priorCollege = newValidForCreatePriorCollege()
        def map = [domainModel: priorCollege]
        priorCollege = priorCollegeService.create(map)
        assertNotNull "PriorCollege ID is null in PriorCollege Service Tests Create", priorCollege.id
        assertNotNull "PriorCollege sourceAndBackgroundInstitution is null in PriorCollege Service Tests", priorCollege.sourceAndBackgroundInstitution
        assertNotNull "PriorCollege admissionRequest is null in PriorCollege Service Tests", priorCollege.admissionRequest
        assertNotNull priorCollege.pidm
        assertNotNull priorCollege.officialTransaction
        assertNotNull priorCollege.version
        assertNotNull priorCollege.dataOrigin
        assertNotNull priorCollege.lastModifiedBy
        assertNotNull priorCollege.lastModified

        //Update the entity with new values
        priorCollege.admissionRequest = AdmissionRequest.findByCode("TUTD")
        priorCollege.officialTransaction = null

        map.domainModel = priorCollege
        priorCollege = priorCollegeService.update(map)

        // test the values
        assertNull priorCollege.officialTransaction
        assertEquals "TUTD", priorCollege.admissionRequest.code
    }


    void testPriorCollegeInvalidUpdate() {
        def priorCollege = newValidForCreatePriorCollege()
        def map = [domainModel: priorCollege]
        priorCollege = priorCollegeService.create(map)
        assertNotNull "PriorCollege ID is null in PriorCollege Service Tests Create", priorCollege.id
        assertNotNull "PriorCollege sourceAndBackgroundInstitution is null in PriorCollege Service Tests", priorCollege.sourceAndBackgroundInstitution
        assertNotNull "PriorCollege admissionRequest is null in PriorCollege Service Tests", priorCollege.admissionRequest
        assertNotNull priorCollege.pidm
        assertNotNull priorCollege.officialTransaction
        assertNotNull priorCollege.version
        assertNotNull priorCollege.dataOrigin
        assertNotNull priorCollege.lastModifiedBy
        assertNotNull priorCollege.lastModified

        //Update the entity with new invalid values
        priorCollege.officialTransaction = "ZZ"
        map.domainModel = priorCollege
        shouldFail(ApplicationException) {
            priorCollege = priorCollegeService.update(map)
        }
    }


    void testPriorCollegeDelete() {
        def priorCollege = newValidForCreatePriorCollege()
        def map = [domainModel: priorCollege]
        priorCollege = priorCollegeService.create(map)
        assertNotNull "PriorCollege ID is null in PriorCollege Service Tests Create", priorCollege.id

        def id = priorCollege.id
        priorCollegeService.delete([domainModel: priorCollege])
        assertNull "PriorCollege should have been deleted", priorCollege.get(id)
    }


    void testReadOnly() {
        def priorCollege = newValidForCreatePriorCollege()
        def map = [domainModel: priorCollege]
        priorCollege = priorCollegeService.create(map)
        assertNotNull "PriorCollege ID is null in PriorCollege Service Tests Create", priorCollege.id

        priorCollege.sourceAndBackgroundInstitution = SourceAndBackgroundInstitution.findWhere(code: "000000")
        priorCollege.pidm = PersonIdentificationName.findByBannerId("HOR000002").pidm
        try {
            priorCollegeService.update([domainModel: priorCollege])
            fail("This should have failed with @@r1:readonlyFieldsCannotBeModified")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }


    private def newValidForCreatePriorCollege() {
        def priorCollege = new PriorCollege(
                pidm: PersonIdentificationName.findByBannerId("HOR000001").pidm,
                transactionRecvDate: new Date(),
                transactionRevDate: new Date(),
                officialTransaction: "Y",
                sourceAndBackgroundInstitution: SourceAndBackgroundInstitution.findWhere(code: "999999"),
                admissionRequest: AdmissionRequest.findByCode("VISA"),
        )
        return priorCollege
    }


    private def newInvalidForCreatePriorCollege() {
        def priorCollege = new PriorCollege(
                pidm: null,
                transactionRecvDate: new Date(),
                transactionRevDate: new Date(),
                officialTransaction: "ZZ",
                sourceAndBackgroundInstitution: null,
                admissionRequest: null,
        )
        return priorCollege
    }
}
