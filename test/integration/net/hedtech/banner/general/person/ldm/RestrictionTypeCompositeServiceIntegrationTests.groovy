/** *******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
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
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test

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
    private String invalid_sort_orderErrorMessage = 'RestfulApiValidationUtility.invalidSortField'
    def restrictionTypeCompositeService
    def i_success_content

    @Before
    public void setUp() {
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
        i_success_content = [code: 'SV',description:'Test Title', metadata: [dataOrigin: 'Banner']]
    }


    @Test
    void testListWithoutPaginationParams() {
        List restrictionTypes = restrictionTypeCompositeService.list([:])
        assertNotNull restrictionTypes
        assertFalse restrictionTypes.isEmpty()
        assertTrue restrictionTypes.size() > 0
    }


    @Test
    void testListWithPagination() {
        def paginationParams = [max: '2', offset: '0']
        List restrictionTypes = restrictionTypeCompositeService.list(paginationParams)
        assertNotNull restrictionTypes
        assertFalse restrictionTypes.isEmpty()
        assertTrue restrictionTypes.size() == 2
    }


    @Test
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


    @Test
    void testListForPersonRestrictionTypeInvalidGuid() {
        try {
            List<PersonRestrictionType> restrictionTypes = restrictionTypeCompositeService.list([parentPluralizedResourceName: LDM_NAME, parentId: 'Invalid-Guid'])
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testListForPersonRestrictionTypeNullGuid() {
        try {
            List<PersonRestrictionType> restrictionTypes = restrictionTypeCompositeService.list([parentPluralizedResourceName: LDM_NAME, parentId: null])
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testCount() {
        assertNotNull i_success_holdType
        assertEquals HoldType.count(), restrictionTypeCompositeService.count()
        assertEquals PersonRelatedHold.fetchByPidmAndDateCompare(pidm, new Date()).size(), restrictionTypeCompositeService.count([parentPluralizedResourceName: LDM_NAME, parentId: getParentId()])
    }


    @Test
    void testGetInvalidGuid() {
        try {
            restrictionTypeCompositeService.get('Invalid-guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testGetNullGuid() {
        try {
            restrictionTypeCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
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


    @Test
    void testFetchByRestrictionTypeIdInvalid() {
        try {
            restrictionTypeCompositeService.fetchByRestrictionTypeId(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    @Test
    void testFetchByRestrictionTypeId() {
        RestrictionType restrictionType = restrictionTypeCompositeService.fetchByRestrictionTypeId(i_success_holdType.id)
        assertNotNull restrictionType
        assertEquals i_success_holdType.id, restrictionType.id
        assertEquals i_success_holdType.code, restrictionType.code
        assertEquals i_success_holdType.description, restrictionType.description
        assertEquals i_success_holdType.dataOrigin, restrictionType.metadata.dataOrigin
    }


    @Test
    void testFetchByRestrictionTypeInvalid() {
        assertNull restrictionTypeCompositeService.fetchByRestrictionTypeCode(null)
        assertNull restrictionTypeCompositeService.fetchByRestrictionTypeCode('Q')
    }


    @Test
    void testFetchByRestrictionTypeCode() {
        RestrictionType restrictionType = restrictionTypeCompositeService.fetchByRestrictionTypeCode(i_success_holdType.code)
        assertNotNull restrictionType
        assertEquals i_success_holdType.id, restrictionType.id
        assertEquals i_success_holdType.code, restrictionType.code
        assertEquals i_success_holdType.description, restrictionType.description
        assertEquals i_success_holdType.dataOrigin, restrictionType.metadata.dataOrigin
    }

    /**
     * Test to check the RestrictionTypeCompositeService list method with valid sort and order field and supported version
     * If No "Accept" header is provided, by default it takes the latest supported version
     */
    @Test
    void testListWithValidSortAndOrderFieldWithSupportedVersion() {
        def params = [order: 'ASC', sort: 'code']
        def restrictionTypeList = restrictionTypeCompositeService.list(params)
        assertNotNull restrictionTypeList
        assertFalse restrictionTypeList.isEmpty()
        assertNotNull restrictionTypeList.code
        assertEquals HoldType.count(), restrictionTypeList.size()
        assertNotNull i_success_holdType
        assertTrue restrictionTypeList.id.contains(i_success_holdType.id)
        assertTrue restrictionTypeList.code.contains( i_success_holdType.code)
        assertTrue restrictionTypeList.description.contains(i_success_holdType.description)
        assertTrue restrictionTypeList.dataOrigin.contains(i_success_holdType.dataOrigin)
    }

    /**
     * Test to check the sort by code on RestrictionTypeCompositeService
     * */
    @Test
    public void testSortByCode(){
        params.order='ASC'
        params.sort='code'
        List list = restrictionTypeCompositeService.list(params)
        assertNotNull list
        def tempParam=null
        list.each{
            restrictionType->
                String code=restrictionType.code
                if(!tempParam){
                    tempParam=code
                }
                assertTrue tempParam.compareTo(code)<0 || tempParam.compareTo(code)==0
                tempParam=code
        }

        params.clear()
        params.order='DESC'
        params.sort='code'
        list = restrictionTypeCompositeService.list(params)
        assertNotNull list
        tempParam=null
        list.each{
            restrictionType->
                String code=restrictionType.code
                if(!tempParam){
                    tempParam=code
                }
                assertTrue tempParam.compareTo(code)>0 || tempParam.compareTo(code)==0
                tempParam=code
        }
    }

    /**
     * Test to check the RestrictionTypeCompositeService list method with invalid sort field
     */
    @Test
    void testListWithInvalidSortField() {
        try {
            def map = [sort: 'test']
            restrictionTypeCompositeService.list(map)
            fail()
        } catch (RestfulApiValidationException e) {
            assertEquals 400, e.getHttpStatusCode()
            assertEquals invalid_sort_orderErrorMessage , e.messageCode.toString()
        }
    }

    /**
     * Test to check the RestrictionTypeCompositeService list method with invalid order field
     */
    @Test
    void testListWithInvalidOrderField() {
        shouldFail(RestfulApiValidationException) {
            def map = [order: 'test']
            restrictionTypeCompositeService.list(map)
        }
    }

    /**
     * Test to check the RestrictionTypeCompositeService create method with valid in request content
     */
    @Test
    void testCreate() {
        def restrictionType = restrictionTypeCompositeService.create(i_success_content)
        assertNotNull restrictionType
        assertNotNull restrictionType.guid
        assertEquals i_success_content.code, restrictionType.code
        assertEquals i_success_content.description, restrictionType.description
    }

    /**
     * Test to check the RestrictionTypeCompositeService create method with exists code in request content
     */
    @Test
    void testCreateExistsCode(){
        i_success_content.code='TT'
        try{
            restrictionTypeCompositeService.create(i_success_content)
        }catch (ApplicationException ae){
            assertApplicationException ae, "code.exists.message"
        }
    }

    /**
     * Test to check the RestrictionTypeCompositeService update method with valid guid in request content
     */
    @Test
    void testUpdate() {
        i_success_content.put('id', holdType_guid_1)
        def restrictionType = restrictionTypeCompositeService.update(i_success_content)
        assertNotNull restrictionType
        assertNotNull restrictionType.guid
        assertEquals i_success_content.code, restrictionType.code
        assertEquals i_success_content.description, restrictionType.description
    }

    /**
     * Test to check the RestrictionTypeCompositeService update method with invalid guid in request content
     */
    @Test
    void testUpdateNullGuid() {
        i_success_content.put('id', null)
        try{
           restrictionTypeCompositeService.update(i_success_content)
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    /**
     * Test to check the RestrictionTypeCompositeService update method with non exists guid in request content
     */
    @Test
    void testUpdateNonExistsGuid() {
        i_success_content.put('id', 'TEST')
        def restrictionType = restrictionTypeCompositeService.update(i_success_content)
        assertNotNull restrictionType
        assertNotNull restrictionType.guid
        assertEquals i_success_content.id?.trim()?.toLowerCase(), restrictionType.guid
        assertEquals i_success_content.code, restrictionType.code
        assertEquals i_success_content.description, restrictionType.description
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
