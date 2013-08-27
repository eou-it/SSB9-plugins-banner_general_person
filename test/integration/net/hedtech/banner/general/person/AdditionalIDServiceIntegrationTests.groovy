/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.AdditionalIdentificationType
import net.hedtech.banner.testing.BaseIntegrationTestCase

class AdditionalIDServiceIntegrationTests extends BaseIntegrationTestCase {

    def additionalIDService

    //Test data for creating new domain instance
    //Valid test data (For success tests)
    def i_success_additionalIdentificationType
    def i_success_pidm = 1
    def i_success_additionalId = "TTTTT"

    //Invalid test data (For failure tests)
    def i_failure_additionalIdentificationType
    def i_failure_pidm = 1
    def i_failure_additionalId = "TTTTT"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)
    def u_success_additionalIdentificationType
    def u_success_pidm = 1
    def u_success_additionalId = "TTTTTs"

    //Valid test data (For failure tests)
    def u_failure_additionalIdentificationType
    def u_failure_pidm = 1
    def u_failure_additionalId = "TTTTT"


    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeTestDataForReferences()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction
    void initializeTestDataForReferences() {
        //Valid test data (For success tests)
        i_success_additionalIdentificationType = AdditionalIdentificationType.findByCode("PROD")

        //Invalid test data (For failure tests)
        i_failure_additionalIdentificationType = AdditionalIdentificationType.findByCode("x")

        //Valid test data (For success tests)
        u_success_additionalIdentificationType = AdditionalIdentificationType.findByCode("PROD")

        //Valid test data (For failure tests)
        u_failure_additionalIdentificationType = AdditionalIdentificationType.findByCode("x")

        //Test data for references for custom tests

    }


    protected void tearDown() {
        super.tearDown()
    }


    void testAdditionalIDValidCreate() {
        def additionalID = newValidForCreateAdditionalID()
        def map = [domainModel: additionalID]
        additionalID = additionalIDService.create(map)
        assertNotNull "AdditionalID ID is null in AdditionalID Service Tests Create", additionalID.id
        assertNotNull "AdditionalID additionalIdentificationType is null in AdditionalID Service Tests", additionalID.additionalIdentificationType
        assertNotNull additionalID.version
        assertNotNull additionalID.dataOrigin
        assertNotNull additionalID.lastModifiedBy
        assertNotNull additionalID.lastModified
    }


    void testAdditionalIDInvalidCreate() {
        def additionalID = newInvalidForCreateAdditionalID()
        def map = [domainModel: additionalID]
        shouldFail(ApplicationException) {
            additionalID = additionalIDService.create(map)
        }
    }


    void testAdditionalIDValidUpdate() {
        def additionalID = newValidForCreateAdditionalID()
        def map = [domainModel: additionalID]
        additionalID = additionalIDService.create(map)
        assertNotNull "AdditionalID ID is null in AdditionalID Service Tests Create", additionalID.id
        assertNotNull "AdditionalID additionalIdentificationType is null in AdditionalID Service Tests", additionalID.additionalIdentificationType
        assertNotNull additionalID.version
        assertNotNull additionalID.dataOrigin
        assertNotNull additionalID.lastModifiedBy
        assertNotNull additionalID.lastModified
        //Update the entity with new values
        additionalID.additionalIdentificationType = u_success_additionalIdentificationType

        map.domainModel = additionalID
        additionalID = additionalIDService.update(map)
        // test the values
        assertEquals u_success_additionalIdentificationType, additionalID.additionalIdentificationType
    }


    void testAdditionalIDInvalidUpdate() {
        def additionalID = newValidForCreateAdditionalID()
        def map = [domainModel: additionalID]
        additionalID = additionalIDService.create(map)
        assertNotNull "AdditionalID ID is null in AdditionalID Service Tests Create", additionalID.id
        assertNotNull "AdditionalID additionalIdentificationType is null in AdditionalID Service Tests", additionalID.additionalIdentificationType
        assertNotNull additionalID.version
        assertNotNull additionalID.dataOrigin
        assertNotNull additionalID.lastModifiedBy
        assertNotNull additionalID.lastModified
        //Update the entity with new invalid values
        additionalID.additionalIdentificationType = u_failure_additionalIdentificationType

        map.domainModel = additionalID
        shouldFail(ApplicationException) {
            additionalID = additionalIDService.update(map)
        }
    }


    void testAdditionalIDDelete() {
        def additionalID = newValidForCreateAdditionalID()
        def map = [domainModel: additionalID]
        additionalID = additionalIDService.create(map)
        assertNotNull "AdditionalID ID is null in AdditionalID Service Tests Create", additionalID.id
        def id = additionalID.id
        map = [domainModel: additionalID]
        additionalIDService.delete(map)
        assertNull "AdditionalID should have been deleted", additionalID.get(id)
    }


    void testReadOnly() {
        def additionalID = newValidForCreateAdditionalID()
        def map = [domainModel: additionalID]
        additionalID = additionalIDService.create(map)
        assertNotNull "AdditionalID ID is null in AdditionalID Service Tests Create", additionalID.id
        additionalID.pidm = PersonUtility.getPerson("HOS00002").pidm
        try {
            additionalIDService.update([domainModel: additionalID])
            fail("This should have failed with @@r1:readonlyFieldsCannotBeModified")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }


    private def newValidForCreateAdditionalID() {
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def additionalID = new AdditionalID(
                pidm: pidm,
                additionalId: i_success_additionalId,
                additionalIdentificationType: i_success_additionalIdentificationType,
        )
        return additionalID
    }


    private def newInvalidForCreateAdditionalID() {
        def pidm = PersonUtility.getPerson("HOS00001").pidm
        def additionalID = new AdditionalID(
                pidm: pidm,
                additionalId: i_failure_additionalId,
                additionalIdentificationType: i_failure_additionalIdentificationType,
        )
        return additionalID
    }
}
