/** *******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test
import org.junit.After


class StudentRoleIntegrationTests extends BaseIntegrationTestCase {


    def studentPidm
    def studentFirstName
    def studentLastName

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeTestDataForReferences()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }

    //This method is used to initialize test data for references.
    //A method is required to execute database calls as it requires a active transaction
    void initializeTestDataForReferences() {
        studentPidm = PersonUtility.getPerson("HOSADV005").pidm
        studentFirstName = PersonUtility.getPerson("HOSADV005").firstName
        studentLastName = PersonUtility.getPerson("HOSADV005").lastName
    }

    @Test
    void testStudentRoleForPidm() {
        StudentRole studentRole = StudentRole.fetchByPidm(studentPidm)
        assertNotNull studentRole
    }

    @Test
    void testStudentRoleForFirstNameAndLastName() {
        StudentRole studentRole = StudentRole.fetchByLastNameAndFirstName(studentFirstName,studentLastName)
        assertNotNull studentRole
    }

}
