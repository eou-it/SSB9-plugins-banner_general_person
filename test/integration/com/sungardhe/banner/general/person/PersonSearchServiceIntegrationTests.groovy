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

        def person = personSearchService.fetchTextSearch("A00000718")
        assertNotNull person
        assertEquals 2185, person.pidm
        assertEquals "A00000718", person.bannerId
    }

    // advanced search by Id
    def testAdvancedSearchById_1() {
       // Alternate id=S101; pidm=2185
       // [[pidm:2185, bannerId:S101, name:Student, lastName:101, mi:null, boosterSearch:2],
       // [pidm:2185, bannerId:A00000718, name:Student, lastName:101, mi:null, boosterSearch:2]]


        def person = personSearchService.fetchTextSearch("S101")
        assertNotNull person
        assertEquals 2185, person.pidm
        assertEquals "A00000718", person.bannerId
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
        println persons
    }

    // advanced search by Id
    def testAdvancedSearchById_3() {
       // [[pidm:2067, bannerId:A00000612, name:System, lastName:Test of PPAIDEN, mi:null, boosterSearch:2],
       // [pidm:2067, bannerId:A00000612, name:System, lastName:Test of PPAIDEN changed, mi:null, boosterSearch:2]]
        def person = personSearchService.fetchTextSearch("A00000612")
        assertNotNull person
        assertEquals 2067, person.pidm
        assertEquals "Test of PPAIDEN changed", person.lastName
    }
}