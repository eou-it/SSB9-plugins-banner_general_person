/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

@Integration
@Rollback
class PersonRaceCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def personRaceCompositeService

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
    void getRacesByPidm() {
        def pidm = PersonUtility.getPerson("GDP000005").pidm
        def races = personRaceCompositeService.getRacesByPidm(pidm)

        assertEquals 'ASI', races[0].race
        assertEquals 'Asian', races[0].description
    }
}
