/*********************************************************************************
  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql

class PersonIdentificationNameServiceIntegrationTests extends BaseIntegrationTestCase {

    def personIdentificationNameService


	@Before
	public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


	@After
	public void tearDown() {
        super.tearDown()
    }


	@Test
    void testCreatePersonIdentificationName() {
        def personIdentificationName = newPersonIdentificationName()

        try {
            personIdentificationName = personIdentificationNameService.create([domainModel: personIdentificationName])
            fail("This should have failed with @@r1:unsupported.operation")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "unsupported.operation"
        }
     }


	@Test
    void testUpdatePersonIdentificationName() {
        def personIdentificationName = newPersonIdentificationName()
        personIdentificationName.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull personIdentificationName.id

        personIdentificationName.bannerId ="UPD-001"

        try {
            personIdentificationNameService.update([domainModel: personIdentificationName])
            fail("This should have failed with @@r1:unsupported.operation")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "unsupported.operation"
        }
    }


	@Test
    void testDeletePersonIdentificationName() {
        def personIdentificationName = newPersonIdentificationName()
        personIdentificationName.save(failOnError: true, flush: true)
        def id = personIdentificationName.id

        try {
            personIdentificationNameService.delete(id)
            fail("This should have failed with @@r1:unsupported.operation")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "unsupported.operation"
        }
    }


	@Test
    void testSystemGeneratedIdPrefix() {
        assertEquals('A', personIdentificationNameService.getSystemGeneratedIdPrefix())
    }


	@Test
    void testGetPrefixDisplayIndForSelfService() {
        def updateSql = """update  gordmsk set gordmsk_display_ind = 'N' where  gordmsk_objs_code   = '**SSB_MASKING'
               And Gordmsk_Block_Name  = 'F_FORMAT_NAME'
               And Gordmsk_Column_Name = 'SPRIDEN_SURNAME_PREFIX' """
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.executeUpdate(updateSql)

        assertEquals('N', personIdentificationNameService.getPrefixDisplayIndicatorForSelfService())
    }


	@Test
    void testGetFormattedNameFL() {
        def fmt
        def pidm
        def formattedName
        pidm = PersonUtility.getPerson("HOS00001").pidm
        fmt = "FL"
        formattedName = personIdentificationNameService.getFormattedName(pidm, fmt)
        assertEquals(formattedName, 'Emily Jamison')

        fmt = "FMIL"
        formattedName = personIdentificationNameService.getFormattedName(pidm, fmt)
        assertEquals(formattedName, 'Emily E. Jamison')

        fmt = "FML"
        formattedName = personIdentificationNameService.getFormattedName(pidm, fmt)
        assertEquals(formattedName, 'Emily Elizabeth Jamison')
    }


    private def newPersonIdentificationName() {
        def inameType = NameType.findWhere(code: "PROF")

        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
        def bannerValues = sql.firstRow(idSql)
        def ibannerId = bannerValues.bannerId
        def ipidm = bannerValues.pidm

        // leave the pidm null to get a generated on, and the ID equal to GENERATED to get a newly
        // generated ID
        def personIdentificationName = new PersonIdentificationName(
                pidm: ipidm,
                bannerId: ibannerId,
                lastName: "TTTTT",
                firstName: "TTTTT",
                middleName: "TTTTT",
                changeIndicator: null,
                entityIndicator: "P",
                nameType: inameType
        )
        return personIdentificationName
    }


    private def newCompanyIdentificationName() {
        def inameType = NameType.findWhere(code: "PROF")
        // leave the pidm null to get a generated on, and the ID equal to GENERATED to get a newly
        // generated ID
        def personIdentificationName = new PersonIdentificationName(
                pidm: null,
                bannerId: 'GENERATED',
                lastName: "TTTTT",
                changeIndicator: null,
                entityIndicator: "C",
                nameType: inameType
        )
        return personIdentificationName
    }

}
