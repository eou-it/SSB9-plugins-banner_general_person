/** *******************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of
 SunGard Higher Education and its subsidiaries. Any use of this software is limited
 solely to SunGard Higher Education licensees, and is further subject to the terms
 and conditions of one or more written license agreements between SunGard Higher
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher
 Education in the U.S.A. and/or other regions and/or countries.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.apache.log4j.Logger

class PersonIdentificationNameServiceIntegrationTests extends BaseIntegrationTestCase {

    def personIdentificationNameService

    def log = Logger.getLogger(this.getClass())


    protected void setUp() {
        formContext = ['GUAMENU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreatePersonIdentificationName() {
        def personIdentificationName = newPersonIdentificationName()
        personIdentificationName = personIdentificationNameService.create([domainModel: personIdentificationName])
        assertNotNull "PersonIdentificationName ID is null in PersonIdentificationName Service Tests Create", personIdentificationName.id
        assertNotNull personIdentificationName.dataOrigin
        assertNotNull personIdentificationName.lastModified
        assertNotNull "PersonIdentificationName nameType is null in PersonIdentificationName Service Tests", personIdentificationName.nameType
        assertNotNull "PersonIdentificationName pidm is null in PersonIdentificationName Service Tests", personIdentificationName.pidm
        assertNotNull "PersonIdentificationName banner id is null in PersonIdentificationName Service Tests", personIdentificationName.bannerId

    }


    void testPersonIdentificationNameDeleteWithNullChangeIndicator() {
        def personIdentificationName = newPersonIdentificationName()
        personIdentificationName = personIdentificationNameService.create([domainModel: personIdentificationName])
        // cannot delete an ID
        def id = personIdentificationName.id
        try {
            personIdentificationNameService.delete(id)
            fail "should have failed because you cannot delete banner spriden"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Cannot delete current record."
        }

    }


    void testPersonIdentificationNameDelete() {
        def personIdentificationName = newPersonIdentificationName()
        personIdentificationName = personIdentificationNameService.create([domainModel: personIdentificationName])
        assertNotNull personIdentificationName.id

        def newPersonIdentificationName = new PersonIdentificationName(personIdentificationName.properties)
        newPersonIdentificationName.changeIndicator = "N"
        newPersonIdentificationName.lastName = 'YYYYY'
        newPersonIdentificationName = personIdentificationNameService.create([domainModel: newPersonIdentificationName])
        assertNotNull newPersonIdentificationName.id

        def id = newPersonIdentificationName.id
        personIdentificationNameService.delete(id)
        // make sure it was deleted
        assertNull PersonIdentificationName.get(id)
    }


    void testPersonIdentificationEntityCheck() {
        def personIdentificationName = newPersonIdentificationName()
        personIdentificationName = personIdentificationNameService.create([domainModel: personIdentificationName])
        assertNotNull personIdentificationName.id
        assertEquals('P', personIdentificationNameService.fetchEntityOfPerson(personIdentificationName.pidm))
        def companyIdentificationName = newCompanyIdentificationName()
        companyIdentificationName = personIdentificationNameService.create(companyIdentificationName)
        assertNotNull companyIdentificationName.id
        assertEquals('C', personIdentificationNameService.fetchEntityOfPerson(companyIdentificationName.pidm))
    }


    void testSystemGeneratedIdPrefix() {
        assertEquals('A', personIdentificationNameService.getSystemGeneratedIdPrefix())
    }


    void testGetPrefixDisplayIndForSelfService() {
        def updateSql = """update  gordmsk set gordmsk_display_ind = 'N' where  gordmsk_objs_code   = '**SSB_MASKING'
               And Gordmsk_Block_Name  = 'F_FORMAT_NAME'
               And Gordmsk_Column_Name = 'SPRIDEN_SURNAME_PREFIX' """
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.executeUpdate(updateSql)

        assertEquals('N', personIdentificationNameService.getPrefixDisplayIndicatorForSelfService())
    }


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


    void testLengthFullNameWithSureNamePrefix() {
        def updateSql = """update  gordmsk set gordmsk_display_ind = 'Y' where  gordmsk_objs_code   = '**SSB_MASKING'
               And Gordmsk_Block_Name  = 'F_FORMAT_NAME'
               And Gordmsk_Column_Name = 'SPRIDEN_SURNAME_PREFIX' """
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.executeUpdate(updateSql)
        def ssbsql = "select gb_displaymask.f_ssb_format_name display from dual"
        def ssbPrefix = sql.firstRow(ssbsql)
        assertEquals "Y", ssbPrefix.display
        def personIdentificationName2 = newPersonIdentificationName()
        // max lengths: first 60, mi 60, last 60, surname 60
        personIdentificationName2.firstName = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"
        personIdentificationName2.middleName = "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM"
        personIdentificationName2.lastName = "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL"
        personIdentificationName2.surnamePrefix = "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"
        personIdentificationName2 = personIdentificationNameService.create([domainModel: personIdentificationName2])
        assertNotNull personIdentificationName2.id
        assertEquals "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL", personIdentificationName2.lastName
        assertEquals "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", personIdentificationName2.firstName
        assertEquals "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM", personIdentificationName2.middleName
        assertEquals "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS", personIdentificationName2.surnamePrefix
        assertEquals 60, personIdentificationName2.lastName.length()
        assertEquals 60, personIdentificationName2.firstName.length()
        assertEquals 60, personIdentificationName2.middleName.length()
        assertEquals 60, personIdentificationName2.surnamePrefix.length()

        def personFullName = PersonIdentificationName.findByPidm(personIdentificationName2.pidm)
        assertTrue personFullName?.fullName?.length() <= 182
        assertEquals "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL", personFullName.fullName

    }


    void testLengthFullNameWithOutSureNamePrefixDisplayed() {
        def updateSql = """update  gordmsk set gordmsk_display_ind = 'N' where  gordmsk_objs_code   = '**SSB_MASKING'
                  And Gordmsk_Block_Name  = 'F_FORMAT_NAME'
                  And Gordmsk_Column_Name = 'SPRIDEN_SURNAME_PREFIX' """
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.executeUpdate(updateSql)
        def ssbsql = "select gb_displaymask.f_ssb_format_name display from dual"
        def ssbPrefix = sql.firstRow(ssbsql)
        assertEquals "N", ssbPrefix.display
        def personIdentificationName2 = newPersonIdentificationName()
        // max lengths: first 60, mi 60, last 60, surname 60
        personIdentificationName2.firstName = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"
        personIdentificationName2.middleName = "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM"
        personIdentificationName2.lastName = "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL"
        personIdentificationName2.surnamePrefix = "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"
        personIdentificationName2 = personIdentificationNameService.create([domainModel: personIdentificationName2])
        assertNotNull personIdentificationName2.id
        assertEquals "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL", personIdentificationName2.lastName
        assertEquals "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", personIdentificationName2.firstName
        assertEquals "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM", personIdentificationName2.middleName
        assertEquals "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS", personIdentificationName2.surnamePrefix
        assertEquals 60, personIdentificationName2.lastName.length()
        assertEquals 60, personIdentificationName2.firstName.length()
        assertEquals 60, personIdentificationName2.middleName.length()
        assertEquals 60, personIdentificationName2.surnamePrefix.length()

        def personFullName = PersonIdentificationName.findByPidm(personIdentificationName2.pidm)
        assertTrue personFullName?.fullName?.length() <= 182
        assertEquals "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL", personFullName.fullName

    }


    void testLengthFullNameForContract() {
        def personIdentificationName2 = newCompanyIdentificationName()
        // max lengths: first 60, mi 60, last 60, surname 60
        personIdentificationName2.lastName = "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL"
        personIdentificationName2 = personIdentificationNameService.create([domainModel: personIdentificationName2])
        assertNotNull personIdentificationName2.id
        assertEquals "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL", personIdentificationName2.lastName
        assertNull personIdentificationName2.firstName
        assertNull personIdentificationName2.middleName
        assertEquals 60, personIdentificationName2.lastName.length()

        def personFullName = PersonIdentificationName.findByPidm(personIdentificationName2.pidm)
        assertTrue personFullName?.fullName?.length() == 60
        assertEquals "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL", personFullName.fullName

    }


    void testCreateOrUpdate() {
        def person = newPersonIdentificationName()
        person = personIdentificationNameService.createOrUpdate([domainModel: person])
        assertEquals 0L, person.version

        def origBannerId = person.bannerId

        // Update the Banner ID
        person.bannerId = "UPD_ID"
        def updatedPerson = personIdentificationNameService.createOrUpdate([domainModel: person])
        assertEquals 1L, updatedPerson.version

        def persons = PersonIdentificationName.findAllByPidm(person.pidm)
        assertEquals 2, persons.size()

        // Check the current id
        def currentPerson = persons.find { it.changeIndicator == null }
        assertNotNull currentPerson?.pidm
        assertEquals updatedPerson.bannerId, currentPerson.bannerId
        assertEquals 1L, currentPerson.version

        // Check the alternative id
        def alternativePerson = persons.find { it.changeIndicator == "I" }
        assertNotNull alternativePerson?.pidm
        assertEquals origBannerId, alternativePerson.bannerId
        assertEquals 0L, alternativePerson.version
    }


    void testUpdateBannerId() {
        def person = newPersonIdentificationName()
        person = personIdentificationNameService.create([domainModel: person])
        assertEquals 0L, person.version

        def origBannerId = person.bannerId

        // Update the Banner ID
        person.bannerId = "UPD_ID"
        def updatedPerson = personIdentificationNameService.update([domainModel: person])
        assertEquals 1L, updatedPerson.version

        def persons = PersonIdentificationName.findAllByPidm(person.pidm)
        assertEquals 2, persons.size()

        // Check the current id
        def currentPerson = persons.find { it.changeIndicator == null }
        assertNotNull currentPerson?.pidm
        assertEquals updatedPerson.bannerId, currentPerson.bannerId
        assertEquals 1L, currentPerson.version

        // Check the alternative id
        def alternativePerson = persons.find { it.changeIndicator == "I" }
        assertNotNull alternativePerson?.pidm
        assertEquals origBannerId, alternativePerson.bannerId
        assertEquals 0L, alternativePerson.version
    }


    void testUpdateName() {
        def person = newPersonIdentificationName()
        person = personIdentificationNameService.create([domainModel: person])
        assertEquals 0L, person.version

        def origLastName = person.lastName
        def origFirstName = person.firstName
        def origMiddleName = person.middleName

        // Update the Banner id
        person.lastName = "UPDATED_LAST_NAME"
        person.firstName = "UPDATED_FIRST_NAME"
        person.middleName = "UPDATED_MIDDLE_NAME"
        def updatedPerson = personIdentificationNameService.update([domainModel: person])
        assertEquals 1L, updatedPerson.version

        def persons = PersonIdentificationName.findAllByPidm(person.pidm)
        assertEquals 2, persons.size()

        // Check the current id
        def currentPerson = persons.find { it.changeIndicator == null }
        assertNotNull currentPerson?.pidm
        assertEquals updatedPerson.lastName, currentPerson.lastName
        assertEquals updatedPerson.firstName, currentPerson.firstName
        assertEquals updatedPerson.middleName, currentPerson.middleName
        assertEquals 1L, currentPerson.version

        // Check the alternative id
        def alternativePerson = persons.find { it.changeIndicator == "N" }
        assertNotNull alternativePerson?.pidm
        assertEquals origLastName, alternativePerson.lastName
        assertEquals origFirstName, alternativePerson.firstName
        assertEquals origMiddleName, alternativePerson.middleName
        assertEquals 0L, alternativePerson.version
    }


    void testUpdateNameType() {
        def person = newPersonIdentificationName()
        person = personIdentificationNameService.create([domainModel: person])
        assertEquals 0L, person.version

        def origNameType = person.nameType

        // Update the nameType
        person.nameType = NameType.findWhere(code: "BRTH")
        def updatedPerson = personIdentificationNameService.update([domainModel: person])
        assertEquals 1L, updatedPerson.version
        assertEquals "BRTH", updatedPerson.nameType.code
        assertEquals 1L, updatedPerson.version

        // Make sure an alternative id is not created for a name type update.
        def persons = PersonIdentificationName.findAllByPidm(person.pidm)
        assertEquals 1, persons.size()
    }


    void testCreateAndUpdateListOfPersons() {
        def person1 = newPersonIdentificationName()
        def person2 = newPersonIdentificationName()
        def persons = [person1, person2]
        persons = personIdentificationNameService.create(persons)
        assertEquals 2, persons.size()
        persons.each {
            assertNotNull it.id
        }

        // Update the last names
        persons.each {
            it.lastName = "UPDATED_LAST_NAME"
        }

        persons = personIdentificationNameService.update(persons)

        persons.each {
            def allPersons = PersonIdentificationName.findAllByPidm(it.pidm)
            assertEquals 2, allPersons.size()

            // Check the current id
            def currentPerson = allPersons.find { it.changeIndicator == null }
            assertNotNull currentPerson?.pidm
            assertEquals "UPDATED_LAST_NAME", currentPerson.lastName
            assertEquals 1L, currentPerson.version

            // Check the alternative id
            def alternativePerson = allPersons.find { it.changeIndicator == "N" }
            assertNotNull alternativePerson?.pidm
            assertEquals "Adams", alternativePerson.lastName
            assertEquals 0L, alternativePerson.version
        }
    }


    void testInvalidUpdateBannerIdAndName() {

        def person = newPersonIdentificationName()
        person = personIdentificationNameService.create([domainModel: person])
        assertNotNull person.id
        assertEquals 0L, person.version

        // Update the Banner id and name
        person.bannerId = "UPD_ID"
        person.lastName = "UPDATED_LAST_NAME"
        person.firstName = "UPDATED_FIRST_NAME"
        person.middleName = "UPDATED_MIDDLE_NAME"

        try {
            def updatedPerson = personIdentificationNameService.update([domainModel: person])
            fail "should have failed because you cannot update both the banner id and name"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "ID and Name cannot be changed at the same time."
        }
    }


    void testInvalidUpdateNoChange() {

        def person = newPersonIdentificationName()
        person = personIdentificationNameService.create([domainModel: person])
        assertNotNull person.id
        assertEquals 0L, person.version

        // No changes at all (not dirty)
        def updatedPerson = personIdentificationNameService.update([domainModel: person])
        assertEquals 0L, person.version

        // Now test a change to any field other than id, name, or name type.
        try {
            person.origin = "UPDATED"
            updatedPerson = personIdentificationNameService.update([domainModel: person])
            fail "Should have failed because you must update either banner id, name, or name type"
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "Must update at least ID, Name, or Name Type."
        }
    }


    private def newPersonIdentificationName() {
        def inameType = NameType.findWhere(code: "PROF")
        // leave the pidm null to get a generated on, and the ID equal to GENERATED to get a newly
        // generated ID
        def personIdentificationName = new PersonIdentificationName(
                pidm: null,
                bannerId: 'GENERATED',
                lastName: "Adams",
                firstName: "Troy",
                middleName: "W",
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
                lastName: "Adams",
                changeIndicator: null,
                entityIndicator: "C",
                nameType: inameType
        )
        return personIdentificationName
    }
}
