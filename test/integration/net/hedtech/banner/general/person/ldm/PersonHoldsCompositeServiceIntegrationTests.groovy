/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.person.ldm

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.person.PersonRelatedHold
import net.hedtech.banner.general.person.ldm.v6.PersonHoldsDecorator
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration Test cases for PersonHolsCompositeService</p>
 * */
class PersonHoldsCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def personHoldsCompositeService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * <p> Test to lists the person hold Records from PersonHolsCompositeService</p>
     * */
    @Test
    public void testListPersonHolds(){
        List personhldList = personHoldsCompositeService.list(params)
        assertNotNull personhldList
        assertFalse personhldList.isEmpty()
        List personHoldList = PersonRelatedHold.findAllByToDateGreaterThanEquals(new Date(),[max: 500])
        assertNotNull personHoldList
        assertEquals personHoldList.size(), personhldList.size()
    }


    /**
     * <p> Test to list the person holds of a person PersonHolsCompositeService</p>
     * */
    @Test
    public void testListPersonHolsWithPersonGuid(){
        params = personHoldFilterMap()
        List list = personHoldsCompositeService.list(params)
        assertFalse list.isEmpty()
    }


    private Map personHoldFilterMap() {
        def params = [max: '10', offset: '0']
        def personHoldsList =  PersonRelatedHold.fetchAll(params,new Date())
        def personHoldsGuid = personHoldsList.get(0).getAt(2).guid
        params.person = personHoldsGuid
        return params
    }


    /* *
    * Test to check count
    * */
    @Test
    public void testCount(){
        def count = personHoldsCompositeService.count(params)
        assertNotNull count
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def result = sql.firstRow("select count(*) as cnt from SPRHOLD where SPRHOLD_TO_DATE > TO_DATE(SYSDATE, 'DD-MM-YY') ")
        Long expectCount = result.cnt
        assertNotNull expectCount
        assertEquals expectCount,count
    }


    /* *
    * Test to check count with filter
    * */
    @Test
    public void testCountWithFilter(){
        params = personHoldFilterMap()
        def count = personHoldsCompositeService.count(params)
        assertNotNull count
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def result = sql.firstRow("select  count(*) as cnt from SPRHOLD,STVHLDD,GORGUID g1,GORGUID g2,GORGUID g3 where SPRHOLD_TO_DATE > TO_DATE(SYSDATE, 'DD-MM-YY') and STVHLDD_CODE = SPRHOLD_HLDD_CODE and g1.GORGUID_LDM_NAME = 'person-holds' AND g1.GORGUID_DOMAIN_SURROGATE_ID = SPRHOLD.SPRHOLD_SURROGATE_ID and g2.GORGUID_LDM_NAME = 'persons' AND g2.GORGUID_DOMAIN_KEY= SPRHOLD_PIDM AND g2.GORGUID_GUID = '"+params.person+"' and g3.GORGUID_LDM_NAME = 'restriction-types' AND g3.GORGUID_DOMAIN_KEY= SPRHOLD_HLDD_CODE")
        Long expectCount = result.cnt
        assertNotNull expectCount
        assertEquals expectCount, count
    }


    /**
     * <p> Test to get a single record from PersonHoldsCompositeService</p>
     * */
    @Test
    public void testShow() {
        List list = personHoldsCompositeService.list(params)
        assertNotNull list
        assertFalse list.isEmpty()
        PersonHoldsDecorator personHold = list?.get(0)
        assertNotNull personHold
        PersonHoldsDecorator newPersonHold = personHoldsCompositeService.get(personHold.id)
        assertNotNull newPersonHold
        assertEquals personHold.id,newPersonHold.id
        assertEquals personHold.person.id,newPersonHold.person.id
        assertEquals personHold.type.detail.id,newPersonHold.type.detail.id
        assertEquals personHold.startOn,newPersonHold.startOn
    }

    /**
     * <p> Test to pass an invalid guid to PersonHolsCompositeService which will return an exception saying record not found</p>
     * */
    @Test
    public void testInvalidShow(){
        List list = personHoldsCompositeService.list(params)
        assertNotNull list
        assertFalse list.isEmpty()
        PersonHoldsDecorator personHold = list.get(0)
        assertNotNull personHold
        try {
            personHoldsCompositeService.get(personHold.id.substring(0,personHold.id.length()-2))
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    /**
     * <p> Test show for AcademicHonorsCompositeService with null guid </p>
     * */
    @Test
    void testGetNullGuid() {
        try {
            personHoldsCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testGetEndOnDate() {
        PersonHoldsDecorator abc = new PersonHoldsDecorator(new PersonRelatedHold(),'123','1234','12345')
        abc.getEndOn();

    }

}
