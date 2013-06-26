/*********************************************************************************
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

package net.hedtech.banner.general.person

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.PersonType
import net.hedtech.banner.general.system.SourceAndBackgroundInstitution
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
