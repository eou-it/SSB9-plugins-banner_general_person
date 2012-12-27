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
/**
 Banner Automator Version: 1.24
 Generated: Thu Aug 04 14:06:16 EDT 2011
 */
package net.hedtech.banner.general.person

import net.hedtech.banner.testing.BaseIntegrationTestCase

import groovy.sql.Sql

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import org.springframework.context.i18n.LocaleContextHolder as LCH

class PersonAdvancedSearchIDNameIntegrationTests extends BaseIntegrationTestCase {

    def personSearchService

    protected void setUp() {
        formContext = ['GEAPART'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    /**
     * Tests Pagination
     */
    def testPagination() {
        def filterData = [:]
        def param = [:]

        def pagingAndSortParams = ["max": 100, "offset": 0]
        filterData.params = param
        def persons = personSearchService.personNameSearch("E", filterData, pagingAndSortParams)
        assertNotNull persons
    }

    /**
     * Tests the id search.
     * SPRIDEN_PIDM           SPRIDEN_ID SPRIDEN_LAST_NAME                                            SPRIDEN_FIRST_NAME                                           SPRIDEN_MI                                                   SPRIDEN_CHANGE_IND SPRIDEN_ENTITY_IND SPRIDEN_ACTIVITY_DATE     SPRIDEN_USER                   SPRIDEN_ORIGIN                 SPRIDEN_SEARCH_LAST_NAME                                     SPRIDEN_SEARCH_FIRST_NAME                                    SPRIDEN_SEARCH_MI                                            SPRIDEN_SOUNDEX_LAST_NAME SPRIDEN_SOUNDEX_FIRST_NAME SPRIDEN_NTYP_CODE SPRIDEN_CREATE_USER            SPRIDEN_CREATE_DATE       SPRIDEN_DATA_ORIGIN            SPRIDEN_CREATE_FDMN_CODE       SPRIDEN_SURNAME_PREFIX                                       SPRIDEN_SURROGATE_ID   SPRIDEN_VERSION        SPRIDEN_USER_ID                SPRIDEN_VPDI_CODE
---------------------- ---------- ------------------------------------------------------------ ------------------------------------------------------------ ------------------------------------------------------------ ------------------ ------------------ ------------------------- ------------------------------ ------------------------------ ------------------------------------------------------------ ------------------------------------------------------------ ------------------------------------------------------------ ------------------------- -------------------------- ----------------- ------------------------------ ------------------------- ------------------------------ ------------------------------ ------------------------------------------------------------ ---------------------- ---------------------- ------------------------------ -----------------
37859                  HOS00001   Jamison                                                      Emily                                                        Elizabeth                                                                       P                  07-JAN-11                 SAISUSR                        SPAIDEN                        JAMISON                                                      EMILY                                                        ELIZABETH                                                    J525                      E540                                         BANPROXY                       07-JAN-11                 GRAILS                                                                                                                     10558                  0


     */
    def testAdvancedSearchById() {

        def filterData = [:]
        def param = [:]

        def pagingAndSortParams = ["max": 8, "offset": 0]
        filterData.params = param

        //Step 1.
        // Client submits a search query as Lindblom
        //Returned Result for the Advanced Search UI Component
        def result = personSearchService.personIdSearch("HOS00001", filterData, pagingAndSortParams)
        println result
        assertNotNull result
        assertTrue result.size() > 0
        assertNotNull result.find { it.bannerId == "HOS00001"}

    }

    /**
     * Tests the id search.
     *
     */
    def testAdvancedSearchByCurrentId() {

        def filterData = [:]
        def param = [:]

        def pagingAndSortParams = ["max": 8, "offset": 0]
        filterData.params = param

        //Step 1.
        // Client submits a search query as Lindblom
        //Returned Result for the Advanced Search UI Component
        def result = personSearchService.personIdSearch("HOSWEB001", filterData, pagingAndSortParams)
        println result
        assertNotNull result
        assertTrue result.size() >= 1

    }

    /**
     * Tests the name search.
     */
    def testAdvancedSearchByName() {

        def filterData = [:]
        def param = [:]

        def pagingAndSortParams = ["max": 8, "offset": 0]
        filterData.params = param

        //Step 1.
        // Client submits a search query as Lindblom
        //Returned Result for the Advanced Search UI Component
        def result = personSearchService.personNameSearch("Lindblom", filterData, pagingAndSortParams)
        println result
        assertNotNull result
        assertTrue result.size() <= 8
        result[0].changeIndicator = ""
        //assertEquals "Lindblom, Atlas", result[0].formattedName

    }

    /**
     * Tests the advanced name search with additional parameters.
     */
    def testAdvancedSearchByNameAndAdditionalParameters() {

        def filterData = [:]
        def param = [:]

        def pagingAndSortParams = ["max": 8, "offset": 0]
        filterData.params = param

        //Step 1.
        // Client submits a search query
        //Returned Result for the Advanced Search UI Component
        def result = personSearchService.personNameSearch("10 STUDENT", filterData, pagingAndSortParams)
        println result
        assertNotNull result
        assertTrue result.size() > 0

        //Step 2
        // Client submits an additional filter search
        //Parameters: city,zip, and birthDate
        param."city" = "%Malvern%"
        param."zip" = "19355"
        param."birthDate" = Date.parse("yyyy-MM-dd", "1985-12-31")

        filterData.params = param

        def m1 = [:]
        m1."key" = "city"
        m1."binding" = "city"
        m1."operator" = "contains"

        def m2 = [:]
        m2."key" = "birthDate"
        m2."binding" = "birthDate"
        m2."operator" = "lessthan"

        def m3 = [:]
        m3."key" = "zip"
        m3."binding" = "zip"
        m3."operator" = "equals"

        def x = []
        x.add(m1)
        x.add(m2)
        x.add(m3)
        filterData.criteria = x

        //Filtered result
        result = personSearchService.personNameSearch("10 STUDENT", filterData, pagingAndSortParams)
        assertNotNull result
        //assertTrue result.size() == 1
        //assertEquals "N", result[0].sex
        //assertEquals "101, Student", result[0].formattedName

    }

    /**
     * Tests the advanced name search with special characters.
     */
    def testAdvancedSearchByNameWithSpecialCharacters() {

        def filterData = [:]
        def param = [:]

        def pagingAndSortParams = ["max": 8, "offset": 0]
        filterData.params = param

        def result = personSearchService.personNameSearch("O'Brien", filterData, pagingAndSortParams)
        assertNotNull result
        //def nameFound = result.find {it.lastName == "O'Brien"}
        //assertNotNull nameFound

        //nameFound = result.find {it.lastName == "Obrien"}
        //assertNotNull nameFound
        //assertTrue result.size() == 3

    }

    /**
     * Tests name format configuration.
     */
    def testNameFormat() {
        def application = AH.application
        ApplicationContext applicationContext = application.mainContext
        def messageSource = applicationContext.getBean("messageSource")
        assertNotNull messageSource.getMessage("default.name.format", null, LCH.getLocale())
    }

    /**
     * Tests the advanced search by SSN
     */
    def testAdvancedSearchBySsn() {

        def filterData = [:]
        def param = [:]


        def pagingAndSortParams = ["max": 100, "offset": 0]
        filterData.params = param

        def sql
        def url = CH.config.bannerDataSource.url
        println url
        try {

            sql = Sql.newInstance(url,   //  db =  new Sql( connectInfo.url,
                    "bansecr",
                    "u_pick_it",
                    'oracle.jdbc.driver.OracleDriver')


            sql.executeUpdate("update gubiprf set gubiprf_security_enabled_ind = 'N' where gubiprf_inst_key = 'INST'")
            sql.commit()

            //Step 1.
            // Client submits a search query to find an exact match
            // Search by SSN
            def persons = personSearchService.personIdSearch("543-54-5432", filterData, pagingAndSortParams)
            assertNotNull persons
            assertTrue persons.size() >= 1

        } finally {
            sql.executeUpdate("update gubiprf set gubiprf_security_enabled_ind = 'Y' where gubiprf_inst_key = 'INST'")
            sql.commit()
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
    }

    /**
     * Tests the advanced search by SSN  with additional parameters
     */
    def testAdvancedSearchBySsnAndAdditionalParameters() {

        def filterData = [:]
        def param = [:]


        def pagingAndSortParams = ["max": 100, "offset": 0]
        filterData.params = param

        def sql
        def url = CH.config.bannerDataSource.url
        println url
        try {

            sql = Sql.newInstance(url,   //  db =  new Sql( connectInfo.url,
                    "bansecr",
                    "u_pick_it",
                    'oracle.jdbc.driver.OracleDriver')


            sql.executeUpdate("update gubiprf set gubiprf_security_enabled_ind = 'N' where gubiprf_inst_key = 'INST'")
            sql.commit()

            //Step 1.
            // Client submits a search query to find an exact match
            // Search by SSN
            //   HOSK00009  999992359
            def persons = personSearchService.personIdSearch("999-99-2359", filterData, pagingAndSortParams)
            assertNotNull persons
            assertTrue persons.size() == 0

            //Step 2.
            // Client submits a search query by SSN and other parameter(i.e Id)
            // Search by SSN
            persons = personSearchService.personIdSearch("999999999", filterData, pagingAndSortParams)

            assertNotNull persons
            def ssnFound = persons.find {it.ssn == '999999999'}
            assertNotNull ssnFound

            param."lastName" = "%Lopez%"

            filterData.params = param

            def m1 = [:]
            m1."key" = "lastName"
            m1."binding" = "lastName"
            m1."operator" = "contains"

            def x = []
            x.add(m1)

            filterData.criteria = x

            //Step 2.
            // Client submits a search query by SSN and other parameter with an additional filter
            // Search by SSN
            def result = personSearchService.personIdSearch("999999999", filterData, pagingAndSortParams)
            //assertTrue result.size() == 1

            //assertEquals "999999999", result[0].ssn

        } finally {
            sql.executeUpdate("update gubiprf set gubiprf_security_enabled_ind = 'Y' where gubiprf_inst_key = 'INST'")
            sql.commit()
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
    }


     /**
     * Tests the advanced search for ssn
     */
    def testAllAdvancedSearchForSsnNotAllowed() {
        def filterData = [:]
        def param = [:]


        def pagingAndSortParams = ["max": 100, "offset": 0]
        filterData.params = param
        // Define text search based on LastName, FirstName, and Id 543-54-5432   123081212
        def persons = personSearchService.personIdSearch("999-99-2359", filterData, pagingAndSortParams)
        assertNotNull persons
        assertTrue persons.size() == 0
    }

   void testFetchNoOfRowsInPageForGUQSRCH() {
         def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update gtvsdax  set GTVSDAX_EXTERNAL_CODE = ? where GTVSDAX_INTERNAL_CODE = ? and GTVSDAX_INTERNAL_CODE_GROUP=? ", [30,'SEARCH_MAX','GUISRCH'])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
       assertEquals "30", personSearchService.fetchNoOfRowsInPageForGUQSRCH()
   }

    void testCheckIfAutoCompleteIsEnabledForIDField() {//('ID_AUTO', 'IDNAMESEARCH')
           def sql
          try {
              sql = new Sql(sessionFactory.getCurrentSession().connection())
              sql.executeUpdate("update gtvsdax  set GTVSDAX_EXTERNAL_CODE = ? where GTVSDAX_INTERNAL_CODE = ? and GTVSDAX_INTERNAL_CODE_GROUP=? ", ['YES','ID_AUTO','IDNAMESEARCH'])
          } finally {
              sql?.close() // note that the test will close the connection, since it's our current session's connection
          }
         assertTrue personSearchService.checkIfAutoCompleteIsEnabledForIDField()
     }


    void testCheckIfAutoCompleteIsEnabledForNameField() {//( 'NAME_AUTO', 'IDNAMESEARCH' )
           def sql
          try {
              sql = new Sql(sessionFactory.getCurrentSession().connection())
              sql.executeUpdate("update gtvsdax  set GTVSDAX_EXTERNAL_CODE = ? where GTVSDAX_INTERNAL_CODE = ? and GTVSDAX_INTERNAL_CODE_GROUP=? ", ['YES','NAME_AUTO','IDNAMESEARCH'])
          } finally {
              sql?.close() // note that the test will close the connection, since it's our current session's connection
          }
        assertTrue personSearchService.checkIfAutoCompleteIsEnabledForNameField()
     }

}
