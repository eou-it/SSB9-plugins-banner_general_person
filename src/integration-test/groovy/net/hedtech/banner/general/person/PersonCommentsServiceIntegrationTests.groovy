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
class PersonCommentsServiceIntegrationTests extends BaseIntegrationTestCase {

    def personCommentsService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    //Test to create the Person Comments
    @Test
    void testCreatePersonComments(){
        def personComments = new PersonComments(pidm: PersonUtility.getPerson("A00050995").pidm, commentCode: "GC", text: "Graduation Name Change", lastModified: new Date(),
                lastModifiedBy: "GRAILS", dataOrigin: "Banner")
        personComments = personCommentsService.create([domainModel: personComments])
        assertNotNull personComments
    }

    //Test to Update the Person Comments
    @Test
    void testUpdatePersonComments() {
        def personComments = new PersonComments(pidm: PersonUtility.getPerson("A00050995").pidm, commentCode: "GC", text: "Graduation Name Change", lastModified: new Date(),
                lastModifiedBy: "GRAILS", dataOrigin: "Banner")
        personComments = personCommentsService.create([domainModel: personComments])

        PersonComments personCommentsUpdate = PersonComments.findWhere(commentCode: "GC")
        assertNotNull personCommentsUpdate

        personCommentsUpdate.text = "Graduation Name Change Requested"
        personCommentsUpdate = personCommentsService.update([domainModel: personCommentsUpdate])
        assertEquals "Graduation Name Change Requested", personCommentsUpdate.text
    }

    //Test to Delete the Person Comments
    @Test
    void testDeletePersonComments() {
        def personComments =new PersonComments(pidm: PersonUtility.getPerson("A00050995").pidm, commentCode: "GC", text: "Graduation Name Change", lastModified: new Date(),
                lastModifiedBy: "GRAILS", dataOrigin: "Banner")
        personComments = personCommentsService.create([domainModel: personComments])
        assertNotNull personComments
        def id = personComments.id
        def deleteMe = PersonComments.get(id)
        personCommentsService.delete([domainModel: deleteMe])
        assertNull PersonComments.get(id)

    }

    //Test to fetch the Person Comments
    @Test
    void testGetPersonCommentsDetailsBypidmAndComments(){
        def personComments = new PersonComments(pidm: PersonUtility.getPerson("A00050995").pidm, commentCode: "GC", text: "Graduation Name Change to test1", lastModified: new Date(),
                lastModifiedBy: "GRAILS", dataOrigin: "Banner")
        personComments = personCommentsService.create([domainModel: personComments])
        assertNotNull personComments

        def personCommentsDetails = personCommentsService.getPersonCommentsDetailsBypidmAndComments(PersonUtility.getPerson("A00050995").pidm, 'Name Change to %')
        assertNotNull personCommentsDetails
    }
}
