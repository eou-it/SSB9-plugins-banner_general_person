/** *****************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.

 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 ****************************************************************************** */
/**
 Banner Automator Version: 1.29
 Generated: Thu Mar 08 11:17:12 EST 2012 
 */
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase
import org.springframework.security.core.context.SecurityContextHolder

class PersonRelatedHoldService extends ServiceBase {

    boolean transactional = true

    def sessionFactory

    void preCreate(map) {
        if(!map.domainModel.pidm)
            map.domainModel.pidm = map.keyBlock.pidm
    }


    void preUpdate(map) {
        validateHoldForUpdate(map.domainModel)
    }


    void preDelete(map) {
        validateHoldForDelete(map.domainModel)
    }

    /**
     * User who created Hold record ==> Can always update the record
     * User created hold type with Release Indicator flag checked ==> No other user can updated any field on this record except the one who originally created
     * User create hold type with Release Indicator flag unchecked ==> Other users can update all the fields except Hold Type and Release Indicator
     * @param hold
     * @return
     */
    //Check view
    private validateHoldForUpdate(PersonRelatedHold hold) {

        if (!(hold.lastModifiedBy == SecurityContextHolder.context?.authentication?.principal?.username)) {
            if (findDirty(hold, 'holdType'))
                throw new ApplicationException(PersonRelatedHold, "@@r1:holdCodeUpdateNotAllowed@@")
            if (findDirty(hold, 'releaseIndicator'))
                throw new ApplicationException(PersonRelatedHold, "@@r1:releaseIndicatorUpdateNotAllowed@@")
            if (hold.releaseIndicator) {
                if (findDirty(hold, 'reason'))
                    throw new ApplicationException(PersonRelatedHold, "@@r1:updateNotAllowedByAnotherUser@@")
                if (findDirty(hold, 'fromDate'))
                    throw new ApplicationException(PersonRelatedHold, "@@r1:updateNotAllowedByAnotherUser@@")
                if (findDirty(hold, 'toDate'))
                    throw new ApplicationException(PersonRelatedHold, "@@r1:updateNotAllowedByAnotherUser@@")
                if (findDirty(hold, 'amountOwed'))
                    throw new ApplicationException(PersonRelatedHold, "@@r1:updateNotAllowedByAnotherUser@@")
                if (findDirty(hold, 'originator'))
                    throw new ApplicationException(PersonRelatedHold, "@@r1:updateNotAllowedByAnotherUser@@")
            }
        }
    }

    /**
     * User created hold type with Release Indicator flag checked ==> No other user can updated any field on this record except the one who originally created the record
     *                                                            ==> No other user can delete the record.
     * @param hold
     * @return
     */
    private validateHoldForDelete(PersonRelatedHold hold) {
        if (!(hold.lastModifiedBy == SecurityContextHolder.context?.authentication?.principal?.username)) {
            if (hold.releaseIndicator) {
                throw new ApplicationException(PersonRelatedHold, "@@r1:deleteNotAllowedByAnotherUser@@")
            }
        }
    }


    private findDirty(hold, String field) {
        def content = ServiceBase.extractParams(PersonRelatedHold, hold)
        def domainObject = PersonRelatedHold.get(content?.id)
        domainObject.properties = content
        def changedNames = domainObject.dirtyPropertyNames
        def changed = changedNames.find { it == field}
        return changed
    }

}
