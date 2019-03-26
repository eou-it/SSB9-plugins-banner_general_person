/*******************************************************************************
 Copyright 2016-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person

import grails.gorm.transactions.Transactional
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.person.view.PersonAdvancedSearchView
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.service.ServiceBase

/**
 * The service class is used for read operations only, it does not support insert/update
 */
@Transactional
class PersonAdvancedSearchViewService extends ServiceBase {

    def preCreate(map) {
        throwUnsupportedException()
    }


    def preUpdate(map) {
        throwUnsupportedException()
    }


    def preDelete(map) {
        throwUnsupportedException()
    }


    def throwUnsupportedException() {
        throw new ApplicationException(PersonAdvancedSearchView.class, "@@r1:unsupported.operation@@")
    }


    def fetchAllByCriteria(Map content, String sortField = null, String sortOrder = null, int max = 0, int offset = -1) {
        def params = [:]
        def criteria = []
        def pagingAndSortParams = [:]

        buildCriteria(content, params, criteria)

        sortOrder = sortOrder ?: 'asc'
        if (sortField) {
            pagingAndSortParams.sortCriteria = [
                    ["sortColumn": sortField, "sortDirection": sortOrder],
                    ["sortColumn": "id", "sortDirection": "asc"]
            ]
        } else {
            pagingAndSortParams.sortColumn = "id"
            pagingAndSortParams.sortDirection = sortOrder
        }

        if (max > 0) {
            pagingAndSortParams.max = max
        }
        if (offset > -1) {
            pagingAndSortParams.offset = offset
        }

        return getDynamicFinderForFetchAllByCriteria().find([params: params, criteria: criteria], pagingAndSortParams)
    }


    def countByCriteria(Map content) {
        def params = [:]
        def criteria = []

        buildCriteria(content, params, criteria)

        return getDynamicFinderForFetchAllByCriteria().count([params: params, criteria: criteria])
    }


    private void buildCriteria(Map content, LinkedHashMap params, ArrayList criteria) {
        if (content.firstName) {
            params.put("firstName", content.firstName?.trim())
            criteria.add([key: "firstName", binding: "firstName", operator: Operators.CONTAINS])
        }

        if (content.middleName) {
            params.put("middleName", content.middleName?.trim())
            criteria.add([key: "middleName", binding: "middleName", operator: Operators.CONTAINS])
        }

        if (content.lastName) {
            params.put("lastName", content.lastName?.trim())
            criteria.add([key: "lastName", binding: "lastName", operator: Operators.CONTAINS])
        }

        if (content.surnamePrefix) {
            params.put("surnamePrefix", content.surnamePrefix?.trim())
            criteria.add([key: "surnamePrefix", binding: "surnamePrefix", operator: Operators.CONTAINS])
        }

        if (content.bannerId) {
            params.put("bannerId", content.bannerId?.trim())
            criteria.add([key: "bannerId", binding: "bannerId", operator: Operators.EQUALS_IGNORE_CASE])
        }

        if (content.namePrefix) {
            params.put("namePrefix", content.namePrefix?.trim())
            criteria.add([key: "namePrefix", binding: "namePrefix", operator: Operators.CONTAINS])
        }

        if (content.nameSuffix) {
            params.put("nameSuffix", content.nameSuffix?.trim())
            criteria.add([key: "nameSuffix", binding: "nameSuffix", operator: Operators.CONTAINS])
        }
    }


    private DynamicFinder getDynamicFinderForFetchAllByCriteria() {
        def query = """FROM PersonAdvancedSearchView a
                       where a.entityIndicator = 'P' and a.changeIndicator is null
		            """
        return new DynamicFinder(PersonAdvancedSearchView.class, query, "a")
    }

}
