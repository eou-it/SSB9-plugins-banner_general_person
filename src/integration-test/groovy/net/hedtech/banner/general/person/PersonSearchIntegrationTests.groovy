/*********************************************************************************
  Copyright 2009-2018 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.general.person.view.PersonPersonView
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.person.dsl.NameTemplate
import net.hedtech.banner.testing.BaseIntegrationTestCase

import java.text.SimpleDateFormat

@Integration
@Rollback
class PersonSearchIntegrationTests extends BaseIntegrationTestCase {

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this(removing GEAPART because of GUOBOBS_UI_VERSION = B)
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /**
     * Tests the Person Search.
     */
    @Test
    void testPersonSearch() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def id
        def lastName = "Macca"
        def firstName
        def midName
        def soundexLastName
        def soundexFirstName
        def changeIndicator
        def nameType = ""

        //search by last name
        def results = PersonPersonView.fetchPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)
        assert results.size() == 1

        lastName = "Bates" //Ducey

        results = PersonPersonView.fetchPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)
        assert results.size() > 0
        assert results.size() <= 8

        //search by soundex   [Duck,Dog,Ducey,Diaz,Dawg]
        soundexLastName = "Onofrio"
        results = PersonPersonView.fetchPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)
        assert results.size() > 0
        assertTrue results.size() <= 8

        //search by change indicator
        soundexLastName = "Gaddis"
        changeIndicator = "N"
        results = PersonPersonView.fetchPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)
        assert results.size() > 0
        assertTrue results.size() <= 8

        //search by name type
        nameType = NameType.findWhere(code: "LEGL").code
        lastName = "Macca"
        soundexLastName = ""
        changeIndicator = ""
        results = PersonPersonView.fetchPerson(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)

        assert results.size() > 0
        assertTrue results.size() == 1

        //fetch all defined by page size with no parameters
        results = PersonPersonView.fetchPerson("", "", "", "", "", "", "", "", pagingAndSortParams)

        assertTrue results.size() == 8
    }

    /**
     * Tests the list of persons.
     */
    @Test
    void testPersonSearchByFilterAndPagination() {

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def id
        def lastName = "Thomas"
        def firstName
        def midName
        def soundexLastName
        def soundexFirstName
        def changeIndicator
        def nameType = ""

        def results = PersonPersonView.fetchPersonList(id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)

        assertNotNull results.list

        def res = results?.list[0]

        assertNotNull res.bannerId
        assertNotNull res.lastName
        assertNotNull res.firstName
        assertNotNull res.birthDate
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by lastName
     */
    @Test
    void testDynamicFinder1() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."searchLastName" = "Bates%"

        def m = [:]
        m."key" = "searchLastName"
        m."binding" = "searchLastName"
        m."operator" = "contains"

        filterData.params = param

        def x = []
        x.add(m)
        filterData.criteria = x

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() > 0
        assert result.size() <= 8
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by lastName with special characters
     */
    @Test
    void testDynamicFinder1_SpecialCharactersCondition() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."searchLastName" = "M'acca%".replaceAll('[^a-zA-Z0-9]+', '')

        def m = [:]
        m."key" = "searchLastName"
        m."binding" = "searchLastName"
        m."operator" = "contains"

        filterData.params = param

        def x = []
        x.add(m)
        filterData.criteria = x

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assertNotNull result.find {
            it.lastName == "Macca"
        }
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by changeIndicator is null
     */
    @Test
    void testDynamicFinder1_ChangeIndicator() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]

        param."searchLastName" = "Macca%"
        param."searchFirstName" = "Moul%"

        def m = [:]
        m."key" = "changeIndicator"
        m."binding" = "changeIndicator"
        m."operator" = "isnull"

        def m0 = [:]
        m0."key" = "searchLastName"
        m0."binding" = "searchLastName"
        m0."operator" = "contains"

        def m1 = [:]
        m1."key" = "searchFirstName"
        m1."binding" = "searchFirstName"
        m1."operator" = "contains"

        filterData.params = param

        def x = []
        x.add(m)
        x.add(m0)
        x.add(m1)
        filterData.criteria = x

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result
        assertTrue result.size() == 1
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by lastName, birthDate
     */
    @Test
    void testDynamicFinder1_1() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."searchLastName" = "Bates%"
        param."birthDate" = Date.parse("yyyy-MM-dd", "1994-01-16")

        filterData.params = param

        def m0 = [:]
        m0."key" = "searchLastName"
        m0."binding" = "searchLastName"
        m0."operator" = "contains"

        def m1 = [:]
        m1."key" = "birthDate"
        m1."binding" = "birthDate"
        m1."operator" = "lessthan"

        def x = []
        x.add(m0)
        x.add(m1)
        filterData.criteria = x

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() >= 2
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by lastName, birthDate
     */
    @Test
    void testDynamicFinder1_BirthDateEqualsCondition() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."searchLastName" = "Bate%"
        String strDateFrom = "1995/3/31" //"1975/12/15";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date t = Date.parse("yyyy-MM-dd", "1995-03-31") //"1965-02-27")
        param."birthDate" = t
        filterData.params = param

        def m0 = [:]
        m0."key" = "searchLastName"
        m0."binding" = "searchLastName"
        m0."operator" = "contains"

        def m1 = [:]
        m1."key" = "birthDate"
        m1."binding" = "birthDate"
        m1."operator" = "equals"

        def x = []
        x.add(m0)
        x.add(m1)
        filterData.criteria = x

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() == 1
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by lastName, firstName
     */
    @Test
    void testDynamicFinder2() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]

        param."searchLastName" = "BATES%"
        param."searchFirstName" = "CRYSTAL%"

        filterData.params = param

        def m0 = [:]
        m0."key" = "searchLastName"
        m0."binding" = "searchLastName"
        m0."operator" = "contains"

        def m1 = [:]
        m1."key" = "searchFirstName"
        m1."binding" = "searchFirstName"
        m1."operator" = "contains"

        def x = []
        x.add(m0)
        x.add(m1)
        filterData.criteria = x

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() > 0
        assert result.size() <= 8
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by Id
     */
    @Test
    void testDynamicFinder3() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."bannerId" = "%GDP000003%"

        def m = [:]
        m."key" = "bannerId"
        m."binding" = "bannerId"
        m."operator" = "contains"

        filterData.params = param

        def x = []
        x.add(m)
        filterData.criteria = x

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() > 0
        assert result.size() <= 8
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by soundexLastName
     */
    @Test
    void testDynamicFinder4() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."soundexLastName" = "Bates"
        param."soundexFirstName" = ""
        filterData.params = param

        def result = PersonPersonView.fetchSearchSoundexEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() <= 8
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by soundexLastName and soundexFirstName
     */
    @Test
    void testDynamicFinder4_1() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]
        param."soundexLastName" = "Bates"
        param."soundexFirstName" = "Crystal"
        filterData.params = param

        def result = PersonPersonView.fetchSearchSoundexEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() == 1
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by lastName, nameType
     */
    @Test
    void testDynamicFinder5() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]

        param."searchLastName" = "Macca"
        param."nameType" = "LEGL"

        filterData.params = param

        def m0 = [:]
        m0."key" = "searchLastName"
        m0."binding" = "searchLastName"
        m0."operator" = "contains"

        def m1 = [:]
        m1."key" = "nameType"
        m1."binding" = "nameType"
        m1."operator" = "equals"

        def x = []
        x.add(m0)
        x.add(m1)
        filterData.criteria = x

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() == 1
        assertEquals "LEGL", result[0].nameType
    }

    /**
     * Tests no data found.
     */
    @Test
    void testDynamicFinder6() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]

        param."searchLastName" = "McxxxAnderson"
        param."nameType" = "LEGL"

        filterData.params = param

        def m0 = [:]
        m0."key" = "searchLastName"
        m0."binding" = "searchLastName"
        m0."operator" = "contains"

        def m1 = [:]
        m1."key" = "nameType"
        m1."binding" = "nameType"
        m1."operator" = "equals"

        def x = []
        x.add(m0)
        x.add(m1)
        filterData.criteria = x

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assert result.size() == 0
    }

    /**
     * Tests the list of emails for the pidm list
     */
    @Test
    void testSearchPersonByPidm() {
        def pidmList = []
        pidmList.add(new Integer(PersonUtility.getPerson("HOPADM005").pidm))
        pidmList.add(new Integer(PersonUtility.getPerson("GDP000001").pidm))

        def pagingAndSortParams = ["max": 8, "offset": 0]
        def id
        def lastName = "Bates"
        def firstName
        def midName
        def soundexLastName
        def soundexFirstName
        def changeIndicator
        def nameType = ""

        //search by last name
        def results = PersonPersonView.fetchPersonByPidms(pidmList, id, lastName, firstName, midName, soundexLastName, soundexFirstName, changeIndicator, nameType, pagingAndSortParams)
        assert results.size() == 2
    }

    /**
     * Tests no parameters on query - fetch all.
     */
    @Test
    void testQueryAllWithNoParameters() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]

        filterData.params = param

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertTrue result.size() == 8
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by surnamePrefix
     */
    @Test
    void testDynamicFinderBySurnamePrefix() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]

        param."surnamePrefix" = "Van Der"

        filterData.params = param

        def m0 = [:]
        m0."key" = "surnamePrefix"
        m0."binding" = "surnamePrefix"
        m0."operator" = "contains"

        def x = []
        x.add(m0)

        filterData.criteria = x

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result

        assert result.size() >= 1
        //the below assert is commented because new seed data records added with 'Van Der' for surname prefix with different last name.
        //assertEquals "Bunte", result[0].lastName
    }

    /**
     * Tests the list of persons for inquiry page.
     * Search by nameSuffix
     */
    @Test
    void testDynamicFinderByNameSuffix() {

        def pagingAndSortParams = ["max": 8, "offset": 0]

        def filterData = [:]
        def param = [:]

        param."nameSuffix" = "Jr."

        filterData.params = param

        def m0 = [:]
        m0."key" = "nameSuffix"
        m0."binding" = "nameSuffix"
        m0."operator" = "contains"

        def x = []
        x.add(m0)

        filterData.criteria = x

        def result = PersonPersonView.fetchSearchEntityList(filterData, pagingAndSortParams)

        assertNotNull result
        assertNotNull result.find {
            it.nameSuffix == "Jr."
        }
    }

    /**
     * Tests the name formatter.
     */
    @Test
    void testNameFormatter() {
        def name = NameTemplate.format {
            lastName "Smith"
            firstName "John"
            mi "ZZ"
            formatTemplate '$lastName, $firstName'
            text
        }
        assertEquals "Smith, John", name

        name = NameTemplate.format {
            lastName "Smithaaaa"
            firstName "John"
            mi "ZZ"
            formatTemplate "<%=lastName[0..4]%>, <%=firstName%> <%=mi[0]%>"
            text
        }
        assertEquals "Smith, John Z", name

        name = NameTemplate.format {
            lastName "Smith"
            firstName "John"
            mi ""
            surnamePrefix "Van Der"
            nameSuffix "PhD"
            formatTemplate '$surnamePrefix $lastName, $firstName $nameSuffix'
            text
        }
        assertEquals "Van Der Smith, John PhD", name

        name = NameTemplate.format {
            lastName "Smith"
            firstName "John"
            mi ""
            surnamePrefix "Van Der"
            nameSuffix ""
            formatTemplate '$surnamePrefix $lastName, $firstName $nameSuffix'
            text
        }
        assertEquals "Van Der Smith, John", name


         name = NameTemplate.format {
            lastName "Smith"
            firstName "John"
            mi ""
            surnamePrefix "Van Der"
            nameSuffix ""
            formatTemplate '$surnamePrefix $lastName, $firstName, $nameSuffix'
            text
        }
        assertEquals "Van Der Smith, John", name

        name = NameTemplate.format {
            lastName "Smith"
            firstName ""
            mi ""
            surnamePrefix "Van Der"
            nameSuffix ""
            formatTemplate '$surnamePrefix $lastName,  $firstName, $nameSuffix'
            text
        }

        assertEquals "Van Der Smith", name

            name = NameTemplate.format {
            lastName "Smith"
            firstName "John"
            mi ""
            surnamePrefix ""
            nameSuffix ""
            formatTemplate '$surnamePrefix $lastName,  $firstName, $nameSuffix'
            text
        }

        assertEquals "Smith, John", name

            name = NameTemplate.format {
            lastName "Smith"
            firstName "John"
            mi ""
            surnamePrefix null
            nameSuffix null
            formatTemplate '$surnamePrefix $lastName,  $firstName, $nameSuffix'
            text
        }

        assertEquals "Smith, John", name

            name = NameTemplate.format {
            lastName "Smith"
            firstName "John"
            mi ""
            surnamePrefix null
            nameSuffix null
            namePrefix "Mr."
            formatTemplate '$namePrefix $surnamePrefix $lastName,  $firstName, $nameSuffix'
            text
        }

        assertEquals "Mr. Smith, John", name

            name = NameTemplate.format {
            lastName "Smith"
            firstName "John"
            mi ""
            surnamePrefix null
            nameSuffix null
            namePrefix null
            formatTemplate '$namePrefix $surnamePrefix $lastName,  $firstName, $nameSuffix'
            text
        }

        assertEquals "Smith, John", name
    }


}
