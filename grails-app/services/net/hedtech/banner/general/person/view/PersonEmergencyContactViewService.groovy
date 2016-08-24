/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person.view

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.service.ServiceBase

class PersonEmergencyContactViewService extends ServiceBase {

    boolean transactional = true

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
        throw new ApplicationException(PersonEmergencyContactView, "@@r1:unsupported.operation@@")
    }


    def fetchAllByCriteria(Map content, String sortField = null, String sortOrder = null, int max = 0, int offset = -1) {
        def params = [:]
        def criteria = []
        def pagingAndSortParams = [:]

        buildCriteria(content, params, criteria)


        if (max > 0) {
            pagingAndSortParams.max = max
        }
        if (offset > -1) {
            pagingAndSortParams.offset = offset
        }

        return getDynamicFinderForFetchByCriteria().find([params: params, criteria: criteria], pagingAndSortParams)
    }


    def countByCriteria(Map content) {
        def params = [:]
        def criteria = []

        buildCriteria(content, params, criteria)

        return getDynamicFinderForFetchByCriteria().count([params: params, criteria: criteria])
    }


    private void buildCriteria(Map content, LinkedHashMap params, ArrayList criteria) {
        if (content.containsKey('personGuid')) {
            params.put("personGuid", content.personGuid?.trim())
            criteria.add([key: "personGuid", binding: "personGuid", operator: Operators.EQUALS])
        }

        if (content.containsKey('guid')) {
            params.put("guid", content.guid?.trim())
            criteria.add([key: "guid", binding: "guid", operator: Operators.EQUALS_IGNORE_CASE])
        }
    }


    private DynamicFinder getDynamicFinderForFetchByCriteria() {
        def query = """FROM PersonEmergencyContactView a
		            """
        return new DynamicFinder(PersonEmergencyContactView.class, query, "a")
    }
}
