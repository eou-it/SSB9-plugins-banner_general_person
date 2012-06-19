package com.sungardhe.banner.general.person.search

import grails.converters.JSON

class PersonSearchController {
    def personSearchService

    def index = { }

    def searchById = {
        def pageOffset = params.int('offset') ? params?.int('offset') : 0
        def pageMaxSize = params.int('max') ? params?.int('max') : 100

        def filterData = [:]
        def param = [:]
        filterData.params = param

        params.pagingAndSortParams = ["max": pageMaxSize, "offset": pageOffset]

        def result = personSearchService.personIdSearch(params?.searchFilter, filterData, params.pagingAndSortParams)
        def people = [people: result]

        render people as JSON
    }

    def searchByName = {
        def pageOffset = params.int('offset') ? params?.int('offset') : 0
        def pageMaxSize = params.int('max') ? params?.int('max') : 100

        def filterData = [:]
        def param = [:]
        filterData.params = param

        params.pagingAndSortParams = ["max": pageMaxSize, "offset": pageOffset]

        def result = personSearchService.personNameSearch(params?.searchFilter, filterData, params.pagingAndSortParams)
        def people = [people: result]

        render people as JSON
    }

    //This is a test action
    def searchPersonByIdTest = {

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

        params.pagingAndSortParams = ["max": pageMaxSize, "offset": pageOffset]

        def result = personSearchService.personIdSearch(params?.searchFilter, filterData, params.pagingAndSortParams)
        def people = [people: result]

        render people as JSON
    }

    def searchPersonByNameTest = {

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

        params.pagingAndSortParams = ["max": pageMaxSize, "offset": pageOffset]

        def result = personSearchService.personNameSearch(params?.searchFilter, filterData, params.pagingAndSortParams)
        def people = [people: result]

        render people as JSON
    }
}
