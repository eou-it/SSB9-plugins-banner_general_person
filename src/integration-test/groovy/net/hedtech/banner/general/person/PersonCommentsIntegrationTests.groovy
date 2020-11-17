/*********************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

@Integration
@Rollback
class PersonCommentsIntegrationTests extends BaseIntegrationTestCase{

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    //Create the person comments
    @Test
    void testCreatePersonComments(){
        def personComments = newPersonComments()
        save personComments
        //Test if the generated entity now has an id assigned
        assertNotNull personComments.id
    }

    //Update the person comments
    @Test
    void testUpdatePersonComments() {
        def personComments = newPersonComments()
        save personComments

        assertNotNull personComments.id
        assertEquals "GC", personComments.commentCode
        assertEquals "Graduation Name Change", personComments.text

        //Update the entity
        personComments.pidm = PersonUtility.getPerson("A00050995").pidm
        personComments.commentCode = "GC"
        personComments.text = "Graduation Name change requested"
        personComments.lastModified = new Date()
        personComments.lastModifiedBy = "GRAILS"
        personComments.dataOrigin = "Banner"
        save personComments

        personComments = PersonComments.get(personComments.id)
        assertEquals "GC", personComments.commentCode
        assertEquals "Graduation Name change requested", personComments.text
    }

    //Delete the person comments
    @Test
    void testDeletePersonComments() {
        def personComments = newPersonComments()
        save personComments
        def id = personComments.id
        assertNotNull personComments
        personComments.delete()
        assertNull PersonComments.get(id)
    }

    //Test to fetch the Person Comments
    @Test
    void testGetPersonCommentsDetailsBypidmAndComments(){
        def personComments = newPersonComments()
        save personComments
        //Test if the generated entity now has an id assigned
        assertNotNull personComments.id

        def personCommentsDetails = PersonComments.fetchByPidmAndComments(PersonUtility.getPerson("A00050995").pidm, 'Graduation Name %')
        assertNotNull personCommentsDetails.id
    }


    //Data to create person comments
    private def newPersonComments() {
        new PersonComments(pidm: PersonUtility.getPerson("A00050995").pidm, commentCode: "GC", text: "Graduation Name Change", lastModified: new Date(),
                lastModifiedBy: "GRAILS", dataOrigin: "Banner")
    }

}
