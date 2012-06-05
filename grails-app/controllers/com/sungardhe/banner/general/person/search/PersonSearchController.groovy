package com.sungardhe.banner.general.person.search

import grails.converters.JSON

class PersonSearchController {
    def personSearchService

    def index = { }

    def search = {
        def pageOffset = params.int('offset') ? params?.int('offset') : 0
        def pageMaxSize = params.int('max') ?  params?.int('max') : 100

        params.pagingAndSortParams = ["max": pageMaxSize, "offset": pageOffset]
        def result = personSearchService.findPerson(params?.id, params?.lastName,
                params?.firstName, params?.midName,
                params?.soundexLastName, params?.soundexFirstName,
                params?.changeIndicator, params?.nameType,
                params?.pagingAndSortParams)
        def people = [people: result]

        render people as JSON
    }


    def searchPidms = {
        def pageOffset = params.int('offset') ? params?.int('offset') : 0
        def pageMaxSize = params.int('max') ?  params?.int('max') : 100

        params.pagingAndSortParams = ["max": pageMaxSize, "offset": pageOffset]
        def result = personSearchService.findPersonByPidm(params?.pidms, params?.id, params?.lastName,
                params?.firstName, params?.midName,
                params?.soundexLastName, params?.soundexFirstName,
                params?.changeIndicator, params?.nameType,
                params?.pagingAndSortParams)
        def people = [people: result]

        render people as JSON
    }


    def searchText = {
        def result = personSearchService.personSearch(params?.searchFilter)
        def people = [people: result]

        render people as JSON
    }

    //This is a test action
    def searchPersonTest = {

        def pageOffset = params.int('offset') ? params?.int('offset') : 0
        def pageMaxSize = params.int('max') ? params?.int('max') : 100

        def filterData = [:]
        def param = [:]
        filterData.params = param

        def x = []

        if (params.city) {
            param."city" = params.city

            def m1 = [:]
            m1."key" = "city"
            m1."binding" = "city"
            m1."operator" = "contains"
            x.add(m1)
        }

        if (params.zip) {
            param."zip" = params.zip

            def m2 = [:]
            m2."key" = "zip"
            m2."binding" = "zip"
            m2."operator" = "contains"
            x.add(m2)
        }

        if (x.size() > 0) {
            filterData.criteria = x
        }

        //def order = "@@table@@pidm asc, @@table@@lastName asc, @@table@@firstName asc"
        //params.pagingAndSortParams = ["max": pageMaxSize, "offset": pageOffset, "sortColumn": order]

        params.pagingAndSortParams = ["max": pageMaxSize, "offset": pageOffset]

        def result = personSearchService.personSearch(params?.searchFilter, filterData, params.pagingAndSortParams)
        def people = [people: result]

        render people as JSON
    }
}
