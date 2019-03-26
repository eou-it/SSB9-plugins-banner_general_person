/*******************************************************************************
 Copyright 2016-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person

import grails.gorm.transactions.Transactional


@Transactional
class PersonHolsService {

    /**
     * fetch person holds list
     * @param params,date
     * @return List
     * */
    List fetchAll(params,date){
        return PersonRelatedHold.fetchAll(params,date)
    }



    /**
     * fetch person holds data
     * @param date
     * @param params
     * @return List
     */
    List  fetchByPersonsGuid(params,date) {
        return PersonRelatedHold.fetchByPersonsGuid(params,date)

    }


    /**
     * get count of person holds data based on filter data
     * @param params,date
     * @return count
     */
    Integer  countRecordWithFilter(params,date) {
        return PersonRelatedHold.countRecordWithFilter(params,date)
    }

    /**
     * get count of person holds
     * @return count
     */
    Integer  countRecord(date) {
        return PersonRelatedHold.countRecord(date)
    }


    /**
     * fetch person holds data
     * @param params
     * @return List
     * */
    def fetchByPersonHoldGuid(guid,date){
        return PersonRelatedHold.fetchByPersonHoldGuid(guid,date)
    }

}
