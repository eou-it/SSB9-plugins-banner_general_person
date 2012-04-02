
/*******************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.

 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 *******************************************************************************/
/**
 Banner Automator Version: 1.29
 Generated: Thu Mar 08 11:17:12 EST 2012 
 */
package com.sungardhe.banner.general.person

import com.sungardhe.banner.exceptions.ApplicationException
import com.sungardhe.banner.service.ServiceBase
import org.springframework.security.core.context.SecurityContextHolder

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).  
// These exceptions must be caught and handled by the controller using this service.
// 
// update and delete may throw com.sungardhe.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

class PersonRelatedHoldService extends ServiceBase {

    boolean transactional = true

    def sessionFactory

    void preUpdate (map) {
          validateHoldForUpdate(map.domainModel)
    }


    void preDelete (map) {
          validateHoldForDelete(map.domainModel)
    }


    private validateHoldForUpdate(PersonRelatedHold hold) {
        if (!(hold.userData == SecurityContextHolder.context?.authentication?.principal?.username)) {
            println(hold.userData || SecurityContextHolder.context?.authentication?.principal?.username)
            if (findDirty(hold,'holdType'))
                throw new ApplicationException(PesonRelatedHold, "@@r1:invalidHoldUser")
            if (findDirty(hold,'releaseIndicator'))
                    throw new ApplicationException(PersonRelatedHold, "@@r1:invalidHoldUser")
            if (hold.releaseIndicator) {
                if (findDirty(hold,'reason'))
                    throw new ApplicationException(PersonRelatedHold, "@@r1:invalidHoldUser")
                if (findDirty(hold,'fromDate'))
                    throw new ApplicationException(PersonRelatedHold, "@@r1:invalidHoldUser")
                if (findDirty(hold,'toDate'))
                    throw new ApplicationException(PersonRelatedHold, "@@r1:invalidHoldUser")
            }
    }
    }


    private validateHoldForDelete(PersonRelatedHold hold) {
         println("user data " + hold.userData + SecurityContextHolder.context?.authentication?.principal?.username)
        if (!(hold.userData == SecurityContextHolder.context?.authentication?.principal?.username)) {
            if (hold.releaseIndicator) {
                throw new ApplicationException(PersonRelatedHold, "@@r1:invalidHoldUserForDelete")
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

    /**
     * Please put all the custom methods in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personrelatedhold_custom_service_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
