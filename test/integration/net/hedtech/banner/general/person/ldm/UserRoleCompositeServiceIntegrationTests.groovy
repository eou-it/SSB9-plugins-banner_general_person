/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.person.ldm

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test


class UserRoleCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def userRoleCompositeService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @Test
    void testfetchAllByStudentRole() {
        def sortParams = [:]
        sortParams.put('sort', 'firstName')
        sortParams.put('order', 'asc')
        Map params = [role: 'student', sortAndPaging: sortParams]

        def pidms = userRoleCompositeService.fetchAllByRole(params)
        assertTrue pidms.size() > 0
    }


}
