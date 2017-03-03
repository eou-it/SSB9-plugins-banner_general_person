/*********************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import groovy.sql.Sql
import net.hedtech.banner.service.ServiceBase
import org.springframework.web.context.request.RequestContextHolder

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
        if(isHrSecurityCheckEnabled()){
            Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.execute("""call gb_common.p_set_context( 'GB_MEDICAL', 'CHECK_HR_SECURITY', 'Y' )""")
        }
    }


    void preUpdate(map) {
        if(isHrSecurityCheckEnabled()) {
            Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.execute("""call gb_common.p_set_context( 'GB_MEDICAL', 'CHECK_HR_SECURITY', 'Y' )""")
        }
    }


    void preDelete(map) {
        if(isHrSecurityCheckEnabled()) {
            Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.execute("""call gb_common.p_set_context( 'GB_MEDICAL', 'CHECK_HR_SECURITY', 'Y' )""")
        }
    }

    private boolean isHrSecurityCheckEnabled() {
        boolean isHrSecurityCheckEnabled
        def session = RequestContextHolder?.currentRequestAttributes()?.request?.session

        if (session?.getAttribute("enableHRSecurityCheckForMedicalInfo") != null) {
            isHrSecurityCheckEnabled = session.getAttribute("enableHRSecurityCheckForMedicalInfo")
        }
        else {
            isHrSecurityCheckEnabled = true
            session.setAttribute("enableHRSecurityCheckForMedicalInfo", isHrSecurityCheckEnabled)
        }
        return isHrSecurityCheckEnabled
    }
}
