/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

import net.hedtech.banner.general.person.ldm.v1.RoleDetail
import net.hedtech.banner.general.system.Term


class UserRoleCompositeService {

    Map fetchAllByRole( role ) {
        def endTerms = Term.findAllByEndDateGreaterThan(new Date(),[sort: "code"])
        def results = [:]
            switch (role) {
                case 'Faculty':
                    if( endTerms.size() > 0 ) {
                        try {
                            def domainClass = Class.forName("net.hedtech.banner.student.faculty.FacultyAppointmentAccessView",
                                    true, Thread.currentThread().getContextClassLoader() )
                            domainClass.fetchAllActiveByMinTermEffective(endTerms[0]?.code).each { it ->
                                def newRole = new RoleDetail()
                                newRole.role = 'Faculty'
                                Term startDate = Term.findByCode(it.termEffective)
                                Term endDate = Term.findByCode(it.termEnd) //TODO: Less queries to Term?
                                newRole.effectiveStartDate = startDate.startDate
                                newRole.effectiveEndDate = endDate.startDate
                                results.put(it.pidm, newRole)
                            }
                        }
                        catch( ClassNotFoundException e ){
                            log.warn "Student faculty plugin not present, unable to process Faculty roles"
                        }
                    }
                    break
                case 'Student':
                    if( endTerms.size() > 0 ) {
                        try {
                            def domainClass = Class.forName("",
                                    true, Thread.currentThread().getContextClassLoader() )
                            domainClass.fetchAllActiveByMinTermEffective(endTerms[0]?.code).each { it ->
                                def newRole = new RoleDetail()
                                newRole.role = 'Student'
                                Term startDate = Term.findByCode(it.termEffective)
                                newRole.effectiveStartDate = startDate.startDate
                                results.put(it.pidm, newRole)
                            }
                        }
                        catch( ClassNotFoundException e ){
                            log.warn "Student faculty plugin not present, unable to process Faculty roles"
                        }
                    }
                    break
            }

        results
    }


}
