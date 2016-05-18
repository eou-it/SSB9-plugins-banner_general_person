/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.utility.DateConvertHelperService
import org.junit.Before
import org.junit.Test
import org.junit.After
import net.hedtech.banner.testing.BaseIntegrationTestCase

class VisaInformationIntegrationTests extends BaseIntegrationTestCase {

    def personIdentificationNameService


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
    void testGetVisaInformation(){
        def visaInformation=newVisaInformation()
        visaInformation.save(failOnError: true, flush: true)
        assertNotNull visaInformation.id
        def visaInfo=VisaInformation.get(visaInformation.id)
        assertNotNull visaInfo
    }

    @Test
    void testCreateVisaInformation(){
        def visaInformation=newVisaInformation()
        visaInformation.save(failOnError: true, flush: true)
        assertNotNull visaInformation.id
    }

    @Test
    void testUpdateVisaInformation(){
        def visaInformation=newVisaInformation()
        visaInformation.save(failOnError: true, flush: true)
        assertNotNull visaInformation.id
        assertEquals 12348765,visaInformation.pidm
        visaInformation.pidm=43215678
        visaInformation.save(failOnError: true, flush: true)
        assertEquals 43215678,visaInformation.pidm
    }

    @Test
    void testDeleteVisaInformation(){
        def visaInformation=newVisaInformation()
        visaInformation.save(failOnError: true, flush: true)
        assertNotNull visaInformation.id
        visaInformation.delete()
        assertNull VisaInformation.get(visaInformation.id)
    }

    @Test
    void testFetchAllByPidmInList() {
        List<String> pidms = VisaInformation.findAll(max: 10).pidm
        List<VisaInformation> visaInformationList = VisaInformation.fetchAllByPidmInList([pidms.first(), pidms.last()])
        visaInformationList.each {
            assertTrue("pidm should had been between: ${pidms.first()} & ${pidms.last()}, but is ${it.pidm}",
                    (pidms.first().equals(it.pidm) || pidms.last().equals(it.pidm)))
        }

    }

    @Test
    void testFetchAllByPidmInListNullList() {
        List<VisaInformation> visaInformationList = VisaInformation.fetchAllByPidmInList(null)
        assertNotNull visaInformationList
        assertEquals(0, visaInformationList.size())

    }

    @Test
    void testFetchAllByPidmInListEmptyList() {
        List<VisaInformation> visaInformationList = VisaInformation.fetchAllByPidmInList([])
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