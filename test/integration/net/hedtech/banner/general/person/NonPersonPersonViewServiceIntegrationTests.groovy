/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.person

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before

class NonPersonPersonViewServiceIntegrationTests extends BaseIntegrationTestCase {


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

}
