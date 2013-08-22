/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase

/**
 *  This service should only be used for retrieving PersonIdentificationNameCurrent data.
 *  Use of this service for CUD operation should be avoided since the standard CUD behavior
 *  has been overridden in the composite service PersonIdentificationNameCompositeService.
 */

class PersonIdentificationNameCurrentService extends ServiceBase {

    boolean transactional = true
    def sessionFactory


    def preCreate(map) {
        if (map?.domainModel?.changeIndicator) {
            throw new ApplicationException(PersonIdentificationNameCurrent, "@@r1:changeIndicatorMustBeNull@@")
        }
    }


    def preDelete(map) {
        if (map?.domainModel?.changeIndicator) {
            throw new ApplicationException(PersonIdentificationNameCurrent, "@@r1:changeIndicatorMustBeNull@@")
        }
    }


    def preUpdate(map) {
        throw new ApplicationException(PersonIdentificationNameCurrent, "@@r1:unsupported.operation@@")
    }

}
