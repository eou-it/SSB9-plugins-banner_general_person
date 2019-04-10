/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v7

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import net.hedtech.banner.general.person.ldm.v7.HedmPersonEmergencyCountry
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

@Integration
@Rollback
class HedmPersonEmergencyCountryTests extends BaseIntegrationTestCase {


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeTestDataForReferences()
    }


    void initializeTestDataForReferences() {

    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void deriveTransactionDetailsTypeEnumWithValidEnums() {
        assertNotNull HedmPersonEmergencyCountry.getByString( 'AUSTRALIA' ) != null

    }



}
