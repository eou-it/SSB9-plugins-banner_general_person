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
}