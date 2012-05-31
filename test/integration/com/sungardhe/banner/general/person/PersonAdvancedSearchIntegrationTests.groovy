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
package com.sungardhe.banner.general.person

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.general.person.view.PersonAdvancedSearchView
import groovy.sql.Sql
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import org.springframework.context.i18n.LocaleContextHolder as LCH


import org.junit.Ignore

class PersonAdvancedSearchIntegrationTests extends BaseIntegrationTestCase {

    def personSearchService

    protected void setUp() {
        formContext = ['GEAPART'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    /**
     * Tests the list of additional ids for inquire page.
     */
    def testDynamicFinder() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."city" = "%seattle%"

        def pidmList = []
        pidmList.add(new Integer("2415"))
        pidmList.add(new Integer("2416"))

        param."pidms" = pidmList

        filterData.params = param

        def m1 = [:]
        m1."key" = "city"
        m1."binding" = "city"
        m1."operator" = "contains"

        def x = []
        x.add(m1)
        filterData.criteria = x

        def result = PersonAdvancedSearchView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertTrue result.size() == 4

    }

    /**
     * Tests the advanced search.
     * 1. Filter
     * 2. City
     * 3. BirthDate
     * 4. Zip
     */
    def testAdvancedSearchByFilterAndByCityAndByBirthDateAndByZip() {
        // Define text search based on LastName, FirstName, and Id
        def persons = personSearchService.fetchTextSearch("33 STUDENT 104 S104")
        assertNotNull persons

        // Step 1: returns a list of pidms
        assertNotNull(persons.collect { it.pidm } as HashSet)

        def pidmSet = (persons.collect { new Integer(it.pidm.toString()) } as HashSet)

        List<Integer> pidmList = new ArrayList<Integer>(pidmSet);

        // Step 2: returns a list of persons based on list of pidms
        // search criteria: City, Zip, Birth Date
        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."city" = "%seattle%"
        param."zip" = "98199"
        param."birthDate" = Date.parse("yyyy-MM-dd", "1970-12-31")

        param."pidms" = pidmList

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

        def result = PersonAdvancedSearchView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertTrue result.size() == 1
        assertEquals "M", result[0].sex
    }

    /**
     * Tests the advanced search.
     * 1. Filter
     * 2. LastName
     */
    def testAdvancedSearchByFilterAndByLastName() {
        // Define text search based on LastName, FirstName, and Id
        def persons = personSearchService.fetchTextSearch("33 STUDENT 104 S104")
        assertNotNull persons

        // Step 1: returns a list of pidms
        assertNotNull(persons.collect { it.pidm } as HashSet)

        def pidmSet = (persons.collect { new Integer(it.pidm.toString()) } as HashSet)

        List<Integer> pidmList = new ArrayList<Integer>(pidmSet);

        // Step 2: returns a list of persons based on list of pidms
        // search criteria: LastName
        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."lastName" = "%Langley%"

        param."pidms" = pidmList

        filterData.params = param

        def m1 = [:]
        m1."key" = "lastName"
        m1."binding" = "lastName"
        m1."operator" = "contains"


        def x = []
        x.add(m1)

        filterData.criteria = x

        def result = PersonAdvancedSearchView.fetchSearchEntityList(filterData, pagingAndSortParams)
        assertTrue result.size() == 1
        assertEquals "Langley", result[0].lastName
    }


    /**
      * Tests the advanced search with special characters
      */
     def testAdvancedSearchWithSpecialCharacters() {

        def filterData = [:]
        def param = [:]
        //IMPORTANT!!!: Must provide Order to have people linked/displayed in order by pidm
        def order = "@@table@@pidm asc, @@table@@lastName asc, @@table@@firstName asc"
        def pagingAndSortParams = ["max": 8, "offset": 0, "sortColumn": order]
        filterData.params = param
        //Step 1.
        // Client submits a search query to find an exact match
        // Search by ID , there are two records for this pidm with a name change
        def result = personSearchService.personSearch("~~2``2@@2##2%%2&&2''2??2**2", filterData, pagingAndSortParams)
        assertNotNull result
        assertTrue result.size() == 1
        assertEquals "~~2``2@@2##2%%2&&2''2??2**2" , result[0].lastName
     }


    /**
     * Tests the advanced search..
     */
    def testAllAdvancedSearch() {
        // Define text search based on LastName, FirstName, and Id
        def persons = personSearchService.personSearch("33 STUDENT 104 S104")
        assertNotNull persons

        // Step 1: returns a list of pidms
        assertNotNull(persons.collect { it.pidm } as HashSet)

        def pidmSet = (persons.collect { new Integer(it.pidm.toString()) } as HashSet)

        List<Integer> pidmList = new ArrayList<Integer>(pidmSet);

        // Step 2: returns a list of persons based on list of pidms
        // search criteria: City, Zip, Birth Date
        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."city" = "%seattle%"
        param."zip" = "98199"
        param."birthDate" = Date.parse("yyyy-MM-dd", "1970-12-31")

        param."pidms" = pidmList

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

        def result = PersonAdvancedSearchView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertTrue result.size() == 1
        assertEquals "M", result[0].sex
    }

    /**
     * Tests the advanced search for ssn
     */
    def testAllAdvancedSearchForSsnAllowed() {

        def sql
        def url = CH.config.bannerDataSource.url
        try {

            sql = Sql.newInstance(url,   //  db =  new Sql( connectInfo.url,
                    "bansecr",
                    "u_pick_it",
                    'oracle.jdbc.driver.OracleDriver')


            sql.executeUpdate("update gubiprf set gubiprf_security_enabled_ind = 'N' where gubiprf_inst_key = 'INST'")
            sql.commit()
            // Define text search based on LastName, FirstName, and Id 543-54-5432   123081212
            def persons = personSearchService.personSearch("543-54-5432")

            assertNotNull persons
            assertTrue persons.size() == 1

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
        // Define text search based on LastName, FirstName, and Id 543-54-5432   123081212
        def persons = personSearchService.personSearch("543-54-5432")
        assertNotNull persons
        assertTrue persons.size() == 0
    }

    /**
     * Tests the advanced search for id
     */
    def testAllAdvancedSearchForId() {

        def persons = personSearchService.personSearch("A00000706")
        assertNotNull persons
        assertTrue persons.size() == 1

        // two Ids
        persons = personSearchService.personSearch("S104")
        assertNotNull persons
        assertTrue persons.size() == 1
        assertEquals "A00000747", persons[0].bannerId

        persons = personSearchService.personSearch("A00000747")

        assertNotNull persons
        assertTrue persons.size() == 1
        assertEquals "A00000747", persons[0].bannerId

    }

    /**
     * Tests the advanced search -- Main WorkFlow.
     * MAIN FLOW
     */
    def testAllAdvancedSearchByIdMainWorkflow() {

        def filterData = [:]
        def param = [:]
        //IMPORTANT!!!: Must provide Order to have people linked/displayed in order by pidm
        def order = "@@table@@pidm asc, @@table@@lastName asc, @@table@@firstName asc"
        def pagingAndSortParams = ["max": 8, "offset": 0, "sortColumn": order]
        filterData.params = param
        //Step 1.
        // Client submits a search query to find an exact match
        // Search by ID , there are two records for this pidm with a name change
        def result = personSearchService.personSearch("A00000706", filterData, pagingAndSortParams)
        assertNotNull result
        assertTrue result.size() == 1
        //Step 1.1
        // Client submits a search query to find an exact match
        // Search by the old ID , there are two records for this pidm with a name change
        result = personSearchService.personSearch("S104", filterData, pagingAndSortParams)
        assertNotNull result
        assertTrue result.size() == 1
        assertEquals "A00000747", result[0].bannerId
        assertEquals "104, Student", result[0].formattedName

        //Step 2.
        // Client submits a search query as 33 STUDENT 104 S104
        //Returned Result for the Advanced Search UI Component
        result = personSearchService.personSearch("Lindblom", filterData, pagingAndSortParams)
        println "####"
        println result
        assertNotNull result
        assertTrue result.size() == 8
        println result
        assertEquals "Lindblomery, Daniella", result[0].formattedName

        //Step 3.
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
        result = personSearchService.personSearch("10 STUDENT", filterData, pagingAndSortParams)
        assertTrue result.size() == 1
        assertEquals "N", result[0].sex
        assertEquals "101, Student", result[0].formattedName

    }

    /**
     * Tests the advanced search for long list of pidms.
     */
    def testAllAdvancedSearchByPidmsLongList() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."city" = "%seattle%"

        filterData.params = param

        def m1 = [:]
        m1."key" = "city"
        m1."binding" = "city"
        m1."operator" = "contains"

        def x = []
        x.add(m1)

        filterData.criteria = x

        def result = personSearchService.personSearch("A", filterData, pagingAndSortParams)

        assertTrue result.size() == 8

    }

    /**
     * Tests the advanced search for multiple ssn match
     */
    def testAllAdvancedSearchForMultipleSsnAllowed() {

        def sql
        def url = CH.config.bannerDataSource.url
        try {

            sql = Sql.newInstance(url,   //  db =  new Sql( connectInfo.url,
                    "bansecr",
                    "u_pick_it",
                    'oracle.jdbc.driver.OracleDriver')


            sql.executeUpdate("update gubiprf set gubiprf_security_enabled_ind = 'N' where gubiprf_inst_key = 'INST'")
            sql.commit()

            def persons = personSearchService.personSearch("543-54-5432 88")

            assertNotNull persons
            def ssnFound = persons.find {it.ssn == '543-54-5432'}
            assertNotNull ssnFound

            ssnFound = persons.find {it.ssn == '094588888'}
            assertNotNull ssnFound

            def idFound = persons.find {it.bannerId == 'A00000788'}
            assertNotNull idFound

        } finally {
            sql.executeUpdate("update gubiprf set gubiprf_security_enabled_ind = 'Y' where gubiprf_inst_key = 'INST'")
            sql.commit()
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
    }

    /**
     * Tests the advanced search for multiple ssn match -- Main WorkFlow
     */
    /*
    [PersonAdvancedSearchView[
                        id=90,
                        pidm=2188,
                        bannerId=A00000721,
                        lastName=105,
                        firstName=Student,
                        middleName=null,
                        birthDate=1990-12-05,
                        ssn=543-54-5432,
                        changeIndicator=null,
                        entityIndicator=P,
                        nameType=null,
                        city=Seattle,
                        state=WA,
                        zip=98199,
                        sex=N, PersonAdvancedSearchView[
                        id=91,
                        pidm=2188,
                        bannerId=S105,
                        lastName=105,
                        firstName=Student,
                        middleName=null,
                        birthDate=1990-12-05,
                        ssn=543-54-5432,
                        changeIndicator=I,
                        entityIndicator=P,
                        nameType=null,
                        city=Seattle,
                        state=WA,
                        zip=98199,
                        sex=N]
    */
    def testAllAdvancedSearchBySsnMainWorkflow() {

        def filterData = [:]
        def param = [:]

        //IMPORTANT!!!:Must provide Order to have people linked/displayed in order by pidm
        def order = "@@table@@pidm asc, @@table@@lastName asc, @@table@@firstName asc"

        def pagingAndSortParams = ["max": 100, "offset": 0, "sortColumn": order]
        filterData.params = param

        def sql
        def url = CH.config.bannerDataSource.url
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
            def persons = personSearchService.personSearch("543-54-5432", filterData, pagingAndSortParams)
            assertNotNull persons
            assertTrue persons.size() == 1


            //Step 2.
            // Client submits a search query by SSN and other parameter(i.e Id)
            // Search by SSN
            persons = personSearchService.personSearch("999999999", filterData, pagingAndSortParams)

            assertNotNull persons
            def ssnFound = persons.find {it.ssn == '999999999'}
            assertNotNull ssnFound

            param."bannerId" = "%A0001034%"  // actual id = A00010347

            filterData.params = param

            def m1 = [:]
            m1."key" = "bannerId"
            m1."binding" = "bannerId"
            m1."operator" = "contains"

            def x = []
            x.add(m1)

            filterData.criteria = x

            //Step 2.
            // Client submits a search query by SSN and other parameter with an additional filter
            // Search by SSN
            def result = personSearchService.personSearch("999999999", filterData, pagingAndSortParams)

            assertTrue result.size() == 1

            assertEquals "999999999", result[0].ssn

        } finally {
            sql.executeUpdate("update gubiprf set gubiprf_security_enabled_ind = 'Y' where gubiprf_inst_key = 'INST'")
            sql.commit()
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
    }

    def testNameFormat() {
        def application = AH.application
        ApplicationContext applicationContext = application.mainContext
        def messageSource = applicationContext.getBean("messageSource")
        assertNotNull messageSource.getMessage("default.name.format", null, LCH.getLocale())
    }
}
