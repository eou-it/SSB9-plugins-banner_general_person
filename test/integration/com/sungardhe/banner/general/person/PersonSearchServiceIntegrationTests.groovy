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

package com.sungardhe.banner.general.person

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import org.junit.Ignore

class PersonSearchServiceIntegrationTests extends BaseIntegrationTestCase {

    def personSearchService

    protected void setUp() {
        formContext = ['GEAPART']// Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }

    void testFindPerson() {
        def pagingAndSortParams = ["max": 8, "offset": 0]
        def id
        def lastName = "Duck"
        def firstName
        def midName
        def soundexLastName
        def soundexFirstName
        def changeIndicator
        def nameType = ""

        //search by last name
        def results = personSearchService.findPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)
        assert results.size() == 4
    }

    /**
     * Tests the list of persons for the pidm list
     */
    def testSearchPersonByPidm() {
        def pidmList = []
        pidmList.add(new Integer("1358"))
        pidmList.add(new Integer("1355"))

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def id
        def lastName = "Duck"
        def firstName
        def midName
        def soundexLastName
        def soundexFirstName
        def changeIndicator
        def nameType = ""

        //search by last name
        def results = personSearchService.findPersonByPidm(pidmList, id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)
        assert results.size() == 2
    }


    // advanced search by First Name and Last Name in any order
    def testAdvancedSearchByFistNameAndLastName() {
        def persons = personSearchService.fetchTextSearch("LINDBLOM|GANNON")
        assertNotNull persons
        def lastName = persons[0].lastName
        assert (/Lindblom/ =~ lastName)

        persons = personSearchService.fetchTextSearch("GANNON|LINDBLOM")
        assertNotNull persons
        lastName = persons[0].lastName
        assert (/Lindblom/ =~ lastName)

        // returns a list of pidms
        assertNotNull (persons.collect { it.pidm } as HashSet)

    }


    // advanced search by Id
    def testAdvancedSearchByIdAndLastName() {
        def persons = personSearchService.fetchTextSearch("WEB")
        assertNotNull persons

        assertNotNull persons.findAll {it.bannerId=="WEBT" }
    }


    // advanced search by Id
    def testAdvancedSearchById() {
        //[[pidm:2185, bannerId:S101, name:Student, lastName:101, mi:null, boosterSearch:2, changeIndicator:I],
        // [pidm:2185, bannerId:A00000718, name:Student, lastName:101, mi:null, boosterSearch:2, changeIndicator:null]]

        def persons = personSearchService.fetchTextSearch("A00000718")
        assertNotNull persons
        assertTrue persons.size() == 1
        assertEquals 2185, persons[0].pidm
        assertEquals "A00000718", persons[0].bannerId
    }

    // advanced search by Id
    def testAdvancedSearchById_1() {
       // Alternate id=S101; pidm=2185
       // [[pidm:2185, bannerId:S101, name:Student, lastName:101, mi:null, boosterSearch:2],
       // [pidm:2185, bannerId:A00000718, name:Student, lastName:101, mi:null, boosterSearch:2]]


        def persons = personSearchService.fetchTextSearch("S101")
        assertNotNull persons
        assertTrue persons.size() == 1
        assertEquals 2185, persons[0].pidm
        assertEquals "A00000718", persons[0].bannerId
    }

    // advanced search by Id
    def testAdvancedSearchById_2() {

         //[[pidm:2076, bannerId:A00000618, name:Walter, lastName:Simpson, mi:Lee, boosterSearch:2, changeIndicator:null],
         // [pidm:2077, bannerId:A00000619, name:Ralph, lastName:Sommerfield, mi:null, boosterSearch:2, changeIndicator:null],
         // [pidm:1920, bannerId:A00000610, name:Vera, lastName:Velmar, mi:null, boosterSearch:2, changeIndicator:N],
         // [pidm:2067, bannerId:A00000612, name:System, lastName:Test of PPAIDEN changed, mi:null, boosterSearch:2, changeIndicator:null],
         // [pidm:2067, bannerId:A00000612, name:System, lastName:Test of PPAIDEN, mi:null, boosterSearch:2, changeIndicator:N],
         // [pidm:2071, bannerId:A00000613, name:Rose, lastName:Madden, mi:Marie, boosterSearch:2, changeIndicator:null],
         // [pidm:2073, bannerId:A00000615, name:Sarah, lastName:Madden, mi:Ann, boosterSearch:2, changeIndicator:null],
         // [pidm:2074, bannerId:A00000616, name:Rosemary, lastName:Madden, mi:Sherre, boosterSearch:2, changeIndicator:null],
         // [pidm:2075, bannerId:A00000617, name:Jamie, lastName:Madden, mi:Lee, boosterSearch:2, changeIndicator:null],
         // [pidm:1920, bannerId:A00000610, name:Vera, lastName:Velmar changed, mi:null, boosterSearch:2, changeIndicator:null],
         // [pidm:2065, bannerId:A00000611, name:Test, lastName:Business person 1, mi:null, boosterSearch:2, changeIndicator:null],
         // [pidm:2072, bannerId:A00000614, name:Robert, lastName:Madden, mi:Wayne, boosterSearch:2, changeIndicator:null]]

        def persons = personSearchService.fetchTextSearch("A0000061")
        assertNotNull persons
        assertTrue persons.size() > 1
    }

    // advanced search by Id
    def testAdvancedSearchById_3() {
       // [[pidm:2067, bannerId:A00000612, name:System, lastName:Test of PPAIDEN, mi:null, boosterSearch:2],
       // [pidm:2067, bannerId:A00000612, name:System, lastName:Test of PPAIDEN changed, mi:null, boosterSearch:2]]
        def persons = personSearchService.fetchTextSearch("A00000612")
        assertNotNull persons
        assertTrue persons.size() == 1
        assertEquals 2067, persons[0].pidm
        assertEquals "Test of PPAIDEN changed", persons[0].lastName
    }


    // advanced search by SSN
    def testAdvancedSearchBySSN() {
        //[pidm:2188, bannerId:A00000721, ssn:543-54-5432, name:Student, lastName:105, mi:null, boosterSearch:2, changeIndicator:null]

        def persons = personSearchService.fetchTextWithSSNSearch("543-54-5432")
        assertNotNull persons
        assertTrue persons.size() == 1
        assertEquals 2188, persons[0].pidm
        assertEquals "A00000721", persons[0].bannerId
        assertEquals "543-54-5432", persons[0].ssn


        //[[pidm:2188, bannerId:S105, ssn:543-54-5432, name:Student, lastName:105, mi:null, boosterSearch:2, changeIndicator:I],
        // [pidm:2188, bannerId:A00000721, ssn:543-54-5432, name:Student, lastName:105, mi:null, boosterSearch:2, changeIndicator:null],
        // [pidm:2176, bannerId:S16, ssn:null, name:Student, lastName:16, mi:null, boosterSearch:2, changeIndicator:I],
        // [pidm:2176, bannerId:A00000709, ssn:null, name:Student, lastName:16, mi:null, boosterSearch:2, changeIndicator:null],
        // [pidm:2175, bannerId:S19, ssn:null, name:Student, lastName:19, mi:null, boosterSearch:2, changeIndicator:I],
        // [pidm:2175, bannerId:A00000708, ssn:null, name:Student, lastName:19, mi:null, boosterSearch:2, changeIndicator:null],
        // [pidm:2170, bannerId:A00000703, ssn:null, name:Student, lastName:2, mi:null, boosterSearch:2, changeIndicator:null],
        // [pidm:2170, bannerId:S2, ssn:null, name:Student, lastName:2, mi:null, boosterSearch:2, changeIndicator:I],
        // [pidm:2171, bannerId:S3, ssn:null, name:Student, lastName:3, mi:null, boosterSearch:2, changeIndicator:I],
        // [pidm:2171, bannerId:A00000704, ssn:null, name:Student, lastName:3, mi:null, boosterSearch:2, changeIndicator:null],
        // [pidm:2172, bannerId:A00000705, ssn:null, name:Student, lastName:4, mi:null, boosterSearch:2, changeIndicator:null],
        // [pidm:2172, bannerId:S4, ssn:null, name:Student, lastName:4, mi:null, boosterSearch:2, changeIndicator:I],
        // [pidm:2173, bannerId:A00000706, ssn:null, name:Student, lastName:5, mi:null, boosterSearch:2, changeIndicator:null],
        // [pidm:2173, bannerId:S5, ssn:null, name:Student, lastName:5, mi:null, boosterSearch:2, changeIndicator:I],
        // [pidm:2174, bannerId:S7, ssn:null, name:Student, lastName:7, mi:null, boosterSearch:2, changeIndicator:I],
        // [pidm:2174, bannerId:A00000707, ssn:null, name:Student, lastName:7, mi:null, boosterSearch:2, changeIndicator:null],
        // [pidm:2168, bannerId:studenta, ssn:666666666, name:Student, lastName:A, mi:null, boosterSearch:2, changeIndicator:I],
        // [pidm:2168, bannerId:A00000701, ssn:666666666, name:Student, lastName:A, mi:null, boosterSearch:2, changeIndicator:null],
        // [pidm:2622, bannerId:A00001052, ssn:256756689, name:Danny, lastName:Caroline, mi:A, boosterSearch:2, changeIndicator:null],
        // [pidm:32751, bannerId:10522635, ssn:319803441, name:Julian, lastName:Hardman, mi:David, boosterSearch:2, changeIndicator:I],
        // [pidm:32751, bannerId:A00011013, ssn:319803441, name:Julian, lastName:Hardman, mi:David, boosterSearch:2, changeIndicator:null],
        // [pidm:30850, bannerId:A00010520, ssn:null, name:Melanie, lastName:Hunter, mi:null, boosterSearch:2, changeIndicator:null],
        // [pidm:30851, bannerId:A00010521, ssn:null, name:Lindsey, lastName:James, mi:M, boosterSearch:2, changeIndicator:null],
        // [pidm:30852, bannerId:A00010522, ssn:null, name:Jared, lastName:Jenkins, mi:T, boosterSearch:2, changeIndicator:null],
        // [pidm:30853, bannerId:A00010523, ssn:null, name:Josh, lastName:Jimenez, mi:N, boosterSearch:2, changeIndicator:null],
        // [pidm:30854, bannerId:A00010524, ssn:null, name:Evan, lastName:Jordan, mi:null, boosterSearch:2, changeIndicator:null],
        // [pidm:30855, bannerId:A00010525, ssn:null, name:Nick, lastName:Joseph, mi:R, boosterSearch:2, changeIndicator:null],
        // [pidm:30856, bannerId:A00010526, ssn:null, name:Erik, lastName:Kelley, mi:D, boosterSearch:2, changeIndicator:null],
        // [pidm:30857, bannerId:A00010527, ssn:null, name:Julia, lastName:Kennedy, mi:R, boosterSearch:2, changeIndicator:null],
        // [pidm:30858, bannerId:A00010528, ssn:null, name:Dana, lastName:Khan, mi:A, boosterSearch:2, changeIndicator:null],
        // [pidm:30859, bannerId:A00010529, ssn:null, name:Craig, lastName:Le, mi:A, boosterSearch:2, changeIndicator:null],
        // [pidm:2167, bannerId:A00000700, ssn:null, name:A, lastName:Student, mi:null, boosterSearch:2, changeIndicator:null]]

        persons = personSearchService.fetchTextWithSSNSearch("543-54|A0000070|1052|Dana")
        assertNotNull persons
        assertTrue persons.size() > 1

        persons = personSearchService.fetchTextWithSSNSearch("543-54-54377")
        assertTrue persons.size() == 0
    }
}