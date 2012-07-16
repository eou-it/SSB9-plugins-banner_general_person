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
 **********************************************************************************/
package net.hedtech.banner.general.person

import groovy.sql.Sql
import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// In general, it is preferred to 'extend ServiceBase' and not include the 'boolean transactional = true' and
// 'static defaultCrudMethods = true' lines.  Extending Service base provides more granular control of
// transaction attributes (using @Transactional annotations).  This 'injection' approach is left here on purpose,
// but should not be propagated to other services.  Please see ServiceBase documentation.

/**
 * A transactional service supporting persistence of the Medical Information model.
 * */
class MedicalInformationService extends ServiceBase{

    boolean transactional = true      // and make transactional (needed only when injecting versus extending)

    def institutionalDescriptionService // injected by Spring
    def sessionFactory                  // injected by Spring


    void preCreate(map) {
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.execute("""call gb_common.p_set_context( 'GB_MEDICAL', 'CHECK_HR_SECURITY', 'Y' )""")
    }


    void preUpdate(map) {
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.execute("""call gb_common.p_set_context( 'GB_MEDICAL', 'CHECK_HR_SECURITY', 'Y' )""")
    }


    void preDelete(map) {
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.execute("""call gb_common.p_set_context( 'GB_MEDICAL', 'CHECK_HR_SECURITY', 'Y' )""")
    }
}
