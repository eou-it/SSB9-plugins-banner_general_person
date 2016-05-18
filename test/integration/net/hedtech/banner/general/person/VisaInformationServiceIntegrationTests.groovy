/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class VisaInformationServiceIntegrationTests extends  BaseIntegrationTestCase{

    def visaInformationService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    @Test
    void testCreateVisaInformation() {
        def visaInformation = newVisaInformation()
        visaInformation = visaInformationService.create([domainModel: visaInformation])
        assertNotNull visaInformation
        assertNotNull visaInformation.id
    }

    @Test
    void testUpdateVisaInformation() {
        def visaInformation = newVisaInformation()
        visaInformation = visaInformationService.create([domainModel: visaInformation])
        assertNotNull visaInformation

        VisaInformation visaInformationUpdate = VisaInformation.findWhere(pidm: 12348765L)
        assertNotNull visaInformationUpdate

        visaInformationUpdate.pidm = 87651234L
        visaInformationUpdate = visaInformationService.update([domainModel: visaInformationUpdate])
        assertEquals 87651234L, visaInformationUpdate.pidm
    }

    @Test
    void testDeleteVisaInformation() {
        def visaInformation = newVisaInformation()
        visaInformation = visaInformationService.create([domainModel: visaInformation])
        assertNotNull visaInformation

        visaInformationService.delete([domainModel: visaInformation])
        assertNull VisaInformation.get(visaInformation.id)

    }

    @Test
    void testList() {
        def visaInformation = visaInformationService.list()
        assertTrue visaInformation.size() > -1
    }

    @Test
    void testFetchAllByPidmInList() {
        List<String> pidms = VisaInformation.findAll(max: 10).pidm
        List<VisaInformation> visaInformationList = visaInformationService.fetchAllByPidmInList([pidms.first(), pidms.last()])
        visaInformationList.each {
            assertTrue("pidm should had been between: ${pidms.first()} & ${pidms.last()}, but is ${it.pidm}",
                    (pidms.first().equals(it.pidm) || pidms.last().equals(it.pidm)))
        }

    }

    @Test
    void testFetchAllByPidmInListNullList() {
        List<VisaInformation> visaInformationList = visaInformationService.fetchAllByPidmInList(null)
        assertNotNull visaInformationList
        assertEquals(0, visaInformationList.size())

    }

    @Test
    void testFetchAllByPidmInListEmptyList() {
        List<VisaInformation> visaInformationList = visaInformationService.fetchAllByPidmInList([])
        assertNotNull visaInformationList
        assertEquals(0, visaInformationList.size())

    }


    private def newVisaInformation(){
        Calendar cal = Calendar.getInstance()
        cal.setTime(new Date())
        cal.add(Calendar.DATE, -100);
        def visaInformation=new VisaInformation(
                visaTypeCode: "X7",
                visaIssueDate: cal.getTime(),
                visaExpireDate: new Date(),
                pidm: 12348765L,
                seqNumber: 241,
                entryInd: "N",
                lastModified: new Date(),
                lastModifiedBy: "GRAILS_USER"
        )
        return visaInformation
    }
}
