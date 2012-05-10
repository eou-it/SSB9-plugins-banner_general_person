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
}
