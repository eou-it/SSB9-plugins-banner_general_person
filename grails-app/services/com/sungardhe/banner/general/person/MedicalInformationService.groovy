/** *****************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.person

import java.sql.CallableStatement
import java.sql.Date
import groovy.sql.Sql
import com.sungardhe.banner.general.system.MedicalCondition
import com.sungardhe.banner.exceptions.ApplicationException
import com.sungardhe.banner.service.DomainManagementMethodsInjector
import com.sungardhe.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// In general, it is preferred to 'extend ServiceBase' and not include the 'boolean transactional = true' and
// 'static defaultCrudMethods = true' lines.  Extending Service base provides more granular control of
// transaction attributes (using @Transactional annotations).  This 'injection' approach is left here on purpose,
// but should not be propagated to other services.  Please see ServiceBase documentation.

/**
 * A transactional service supporting persistence of the Medical Information model.
 * */
class MedicalInformationService {

    static defaultCrudMethods = true  // inject ServiceBase methods (versus extending from ServiceBase)
    boolean transactional = true      // and make transactional (needed only when injecting versus extending)

    def institutionalDescriptionService // injected by Spring
    def sessionFactory                  // injected by Spring


    void preCreate( map ) {
        Sql sql = new Sql( sessionFactory.getCurrentSession().connection() )
        sql.execute( """call gb_common.p_set_context( 'GB_MEDICAL', 'CHECK_HR_SECURITY', 'Y' )""" )
    }


    void preUpdate( map ) {
        Sql sql = new Sql( sessionFactory.getCurrentSession().connection() )
        sql.execute( """call gb_common.p_set_context( 'GB_MEDICAL', 'CHECK_HR_SECURITY', 'Y' )""" )
    }


    void preDelete( map ) {
        Sql sql = new Sql( sessionFactory.getCurrentSession().connection() )
        sql.execute( """call gb_common.p_set_context( 'GB_MEDICAL', 'CHECK_HR_SECURITY', 'Y' )""" )
    }
}
