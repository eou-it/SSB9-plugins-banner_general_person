/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person.ldm


import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.person.PersonRelatedHold
import net.hedtech.banner.general.person.PersonUtility
import net.hedtech.banner.general.person.ldm.v1.PersonRestrictionType
import net.hedtech.banner.general.system.HoldType
import net.hedtech.banner.general.system.Originator
import net.hedtech.banner.general.system.ldm.v1.RestrictionType
import net.hedtech.banner.testing.BaseIntegrationTestCase

import java.text.SimpleDateFormat


class RestrictionTypeCompositeServiceIntegrationTests extends BaseIntegrationTestCase{

    HoldType i_success_holdType
    HoldType i_success_holdType_1
    HoldType i_success_holdType_2
    HoldType i_success_holdType_3
    String holdType_guid_1
    String holdType_guid_2
    String holdType_guid_3
    Integer pidm
    String bannerId

    Map guids = [:]
    private static final String LDM_NAME = "persons"
    def restrictionTypeCompositeService


    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        bannerId = "HOSADV001"
        pidm = PersonUtility.getPerson(bannerId).pidm
        newHoldType("TT")
        i_success_holdType = HoldType.findByCode('TT')
        i_success_holdType_1 = HoldType.findByCode('AR')
        i_success_holdType_2 = HoldType.findByCode('AP')
        i_success_holdType_3 = HoldType.findByCode('AL')
        holdType_guid_1 = restrictionTypeCompositeService.fetchByRestrictionTypeCode(i_success_holdType_1.code).guid
        holdType_guid_2 = restrictionTypeCompositeService.fetchByRestrictionTypeCode(i_success_holdType_2.code).guid
        holdType_guid_3 = restrictionTypeCompositeService.fetchByRestrictionTypeCode(i_success_holdType_3.code).guid
        guids << [guid1: holdType_guid_1]
        guids << [guid2: holdType_guid_2]
        guids << [guid3: holdType_guid_3]

        def holdList = PersonRelatedHold.fetchByPidm(pidm)
        holdList?.each{it.delete(flush: true, failOnError: true)}

        newPersonRelatedHold(i_success_holdType_1)
        newPersonRelatedHold(i_success_holdType_2)
        newPersonRelatedHold(i_success_holdType_3)

        newPersonRelatedHold(i_success_holdType_1)
        newPersonRelatedHold(i_success_holdType_2)
        newPersonRelatedHold(i_success_holdType_3)
    }


    void testListWithoutPaginationParams() {
        List restrictionTypes = restrictionTypeCompositeService.list([:])
        assertNotNull restrictionTypes
        assertFalse restrictionTypes.isEmpty()
        assertTrue restrictionTypes.size() > 0
    }


    void testListWithPagination() {
        def paginationParams = [max: '2', offset: '0']
        List restrictionTypes = restrictionTypeCompositeService.list(paginationParams)
        assertNotNull restrictionTypes
        assertFalse restrictionTypes.isEmpty()
        assertTrue restrictionTypes.size() == 2
    }


    void testListForPersonRestrictionType() {
        def personHoldList1 = PersonRelatedHold.fetchByPidmAndDateCompare(pidm, new Date())
        assertNotNull(personHoldList1)
        assertEquals 6, personHoldList1.size()

        List<PersonRestrictionType> restrictionTypes = restrictionTypeCompositeService.list([parentPluralizedResourceName: LDM_NAME, parentId: getParentId()])
        assertNotNull restrictionTypes
        assertFalse restrictionTypes.isEmpty()
        assertTrue restrictionTypes.size() == 3
        restrictionTypes.each {
            assertTrue guids.containsValue(it.guid)
        }
    }


    void testListForPersonRestrictionTypeInvalidGuid() {
        try {
            List<PersonRestrictionType> restrictionTypes = restrictionTypeCompositeService.list([parentPluralizedResourceName: LDM_NAME, parentId: 'Invalid-Guid'])
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    void testListForPersonRestrictionTypeNullGuid() {
        try {
            List<PersonRestrictionType> restrictionTypes = restrictionTypeCompositeService.list([parentPluralizedResourceName: LDM_NAME, parentId: null])
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    void testCount() {
        assertNotNull i_success_holdType
        assertEquals HoldType.count(), restrictionTypeCompositeService.count()
        assertEquals PersonRelatedHold.fetchByPidmAndDateCompare(pidm, new Date()).size(), restrictionTypeCompositeService.count([parentPluralizedResourceName: LDM_NAME, parentId: getParentId()])
    }


    void testGetInvalidGuid() {
        try {
            restrictionTypeCompositeService.get('Invalid-guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    void testGetNullGuid() {
        try {
            restrictionTypeCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    void testGet() {
        def paginationParams = [max: '1', offset: '0']
        def restrictionTypes = restrictionTypeCompositeService.list(paginationParams)
        assertNotNull restrictionTypes
        assertFalse restrictionTypes.isEmpty()

        assertNotNull restrictionTypes[0].guid
        def restrictionType = restrictionTypeCompositeService.get(restrictionTypes[0].guid)
        assertNotNull restrictionType
        assertNotNull restrictionType.code
        assertEquals restrictionTypes[0].code, restrictionType.code
        assertNotNull restrictionType.guid
        assertEquals restrictionTypes[0].guid, restrictionType.guid
        assertNotNull restrictionType.guid
        assertEquals restrictionTypes[0].description, restrictionType.description
        assertNotNull restrictionType.metadata
        assertEquals restrictionTypes[0].metadata.dataOrigin, restrictionType.metadata.dataOrigin
        assertEquals restrictionTypes[0], restrictionType
    }


    void testFetchByRestrictionTypeIdInvalid() {
        try {
            restrictionTypeCompositeService.fetchByRestrictionTypeId(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    void testFetchByRestrictionTypeId() {
        RestrictionType restrictionType = restrictionTypeCompositeService.fetchByRestrictionTypeId(i_success_holdType.id)
        assertNotNull restrictionType
        assertEquals i_success_holdType.id, restrictionType.id
        assertEquals i_success_holdType.code, restrictionType.code
        assertEquals i_success_holdType.description, restrictionType.description
        assertEquals i_success_holdType.dataOrigin, restrictionType.metadata.dataOrigin
    }


    void testFetchByRestrictionTypeInvalid() {
        assertNull restrictionTypeCompositeService.fetchByRestrictionTypeCode(null)
        assertNull restrictionTypeCompositeService.fetchByRestrictionTypeCode('Q')
    }


    void testFetchByRestrictionTypeCode() {
        RestrictionType restrictionType = restrictionTypeCompositeService.fetchByRestrictionTypeCode(i_success_holdType.code)
        assertNotNull restrictionType
        assertEquals i_success_holdType.id, restrictionType.id
        assertEquals i_success_holdType.code, restrictionType.code
        assertEquals i_success_holdType.description, restrictionType.description
        assertEquals i_success_holdType.dataOrigin, restrictionType.metadata.dataOrigin
    }


    private def newHoldType(String code) {

        def holdType = new HoldType(
                code: code,
                registrationHoldIndicator: true,
                transcriptHoldIndicator: true,
                graduationHoldIndicator: true,
                gradeHoldIndicator: true,
                description: "TTTTT",
                accountsReceivableHoldIndicator: true,
                enrollmentVerificationHoldIndicator: true,
                voiceResponseMessageNumber: 1,
                displayWebIndicator: true,
                applicationHoldIndicator: true,
                complianceHoldIndicator: true,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        holdType.save(failOnError: true, flush: true)
    }


    private def newPersonRelatedHold(def iholdType) {

        def ioriginator = Originator.findWhere(code: "ACCT")
        def df1 = new SimpleDateFormat("yyyy-MM-dd")
        def fromDate = df1.parse("2010-07-01")
        def toDate = df1.parse("2040-09-01")


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
                dataOrigin: "Banner"
        )
        personRelatedHold.save(failOnError: true, flush: true)
    }


    private def getParentId(){
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(LDM_NAME,pidm.toString())
        return globalUniqueIdentifier.guid
    }

}
