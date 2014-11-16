/*********************************************************************************
Copyright 2012 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
/*******************************************************************************
 Copyright 2009-2012 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package net.hedtech.banner.general.person
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.person.PersonIdentificationNameDecorator
import net.hedtech.banner.person.dsl.NameTemplate
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder

class PersonIdentificationNameDecoratorIntegrationTests extends BaseIntegrationTestCase {

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
    void testNullNewPerson() {
        def newPerson = new PersonIdentificationNameDecorator()
        assertNull newPerson.pidm
        assertNull newPerson.displayName
    }


    @Test
    void testNewSectionFromSpriden() {

        def newPerson = new PersonIdentificationNameDecorator(PersonUtility.getPerson("HOF00714"))
        assertEquals newPerson.lastName, "Herwig"
        assertEquals newPerson.firstName, "Marita"
        assertEquals newPerson.bannerId, "HOF00714"

    }


    @Test
    void testNewSectionFromMap() {
        def instructor = PersonUtility.getPerson("HOF00714")
        def newPerson = new PersonIdentificationNameDecorator([pidm: instructor.pidm,
                lastName: instructor.lastName,
                firstName: instructor.firstName,
                middleName: instructor.middleName,
                bannerId: instructor.bannerId,
                surnamePrefix: instructor.surnamePrefix])
        assertEquals newPerson.lastName, "Herwig"
        assertEquals newPerson.firstName, "Marita"
        assertEquals newPerson.bannerId, "HOF00714"

    }


    @Test
    void testNewSectionFromMapUsingNameTemplateWithSurnamePrefixOff() {
        //get the name format from General Person Plugin messages.properties
        def application = ApplicationHolder.application
        ApplicationContext applicationContext = application.mainContext
        def messageSource = applicationContext.getBean("messageSource")
        def nameFormat = messageSource.getMessage("default.name.format", null, LocaleContextHolder.getLocale())

        //make sure masking has surnamePrefix turned off
        def updateSql = """update  gordmsk set gordmsk_display_ind = 'N' where  gordmsk_objs_code   = '**SSB_MASKING'
               And Gordmsk_Block_Name  = 'F_FORMAT_NAME'
               And Gordmsk_Column_Name = 'SPRIDEN_SURNAME_PREFIX' """
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.executeUpdate(updateSql)

        assertEquals('N', personIdentificationNameService.getPrefixDisplayIndicatorForSelfService())

        def instructor = PersonUtility.getPerson("HOF00714")
        def newPerson = new PersonIdentificationNameDecorator([pidm: instructor.pidm,
                lastName: instructor.lastName,
                firstName: instructor.firstName,
                middleName: instructor.middleName,
                surnamePrefix: "vanDe",
                bannerId: instructor.bannerId])
        assertEquals newPerson.lastName, "Herwig"
        assertEquals newPerson.bannerId, "HOF00714"
        //get the name using the NameTemplate directly
        def displayName = NameTemplate.format {
            lastName instructor.lastName
            firstName instructor.firstName
            mi instructor.middleName
            surnamePrefix ""
            formatTemplate nameFormat
            text
        }
        assertEquals displayName, newPerson.displayName
    }


    @Test
    void testNameFormatUsingNameTemplateWithSurnamePrefixTurnedOn() {
        //get the name format from General Person Plugin messages.properties
        def application = ApplicationHolder.application
        ApplicationContext applicationContext = application.mainContext
        def messageSource = applicationContext.getBean("messageSource")
        def nameFormat = messageSource.getMessage("default.name.format", null, LocaleContextHolder.getLocale())

        //make sure masking has surnamePrefix turned on
        def updateSql = """update  gordmsk set gordmsk_display_ind = 'Y' where  gordmsk_objs_code   = '**SSB_MASKING'
               And Gordmsk_Block_Name  = 'F_FORMAT_NAME'
               And Gordmsk_Column_Name = 'SPRIDEN_SURNAME_PREFIX' """
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.executeUpdate(updateSql)

        assertEquals('Y', personIdentificationNameService.getPrefixDisplayIndicatorForSelfService())

        def instructor = PersonUtility.getPerson("HOF00714")
        def newPerson = new PersonIdentificationNameDecorator([pidm: instructor.pidm,
                lastName: instructor.lastName,
                firstName: instructor.firstName,
                middleName: instructor.middleName,
                surnamePrefix: "vanDe",
                bannerId: instructor.bannerId])
        def formattedName = newPerson.formatDisplayName('Y')
        //If the surname prefix is not included in the name template of messages.properties, it will not display in name even when 'Y' is passed to formatDisplayName.
        assertEquals newPerson.displayName, formattedName
        assertEquals "vanDe", newPerson.surnamePrefix
    }


}
