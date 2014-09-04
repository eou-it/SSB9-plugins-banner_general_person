/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

import net.hedtech.banner.general.overall.UserRole
import net.hedtech.banner.general.person.ldm.v1.RoleDetail
import net.hedtech.banner.general.system.Term


class UserRoleCompositeService {

    Map fetchAllByRole( String role ) {
        def results = [:]
            switch (role.toLowerCase()) {
                case 'faculty':
                    def allTerms = Term.list()
                    def endTerms = Term.findAllByEndDateGreaterThan(new Date(),[sort: "code"])
                    if( endTerms.size() > 0 ) {
                        try {
                            def domainClass = Class.forName("net.hedtech.banner.student.faculty.FacultyAppointmentAccessView",
                                    true, Thread.currentThread().getContextClassLoader() )
                            domainClass.fetchAllActiveByMinTermEffective(endTerms[0]?.code).each { faculty ->
                                def roles = results.get(faculty.pidm) ?: []
                                def newRole = new RoleDetail()
                                newRole.role = 'Faculty'
                                Term startDate = allTerms.find{ it.code == faculty.termEffective }
                                Term endDate = allTerms.find{ it.code == faculty.termEnd }
                                newRole.effectiveStartDate = startDate.startDate
                                newRole.effectiveEndDate = endDate.startDate
                                roles << newRole
                                results.put(faculty.pidm, roles )
                            }
                        }
                        catch( ClassNotFoundException e ){
                            log.debug "Student faculty plugin not present, unable to process Faculty roles"
                        }
                    }
                    break
                case 'student':
                    if( endTerms.size() > 0 ) {
                        UserRole.findAllByStudentIndicator(true).each { it ->
                            def roles = results.get(it.pidm) ?: []
                            def newRole = new RoleDetail()
                            newRole.role = 'Student'
                            roles << newRole
                            results.put(it.pidm, roles )
                        }

                    }
                    break
            }

        results
    }

    Map<Integer,List<RoleDetail>> fetchAllRolesByPidmInList( List pidms) {
        def results = [:]
        def endTerms = Term.findAllByEndDateGreaterThan(new Date(),[sort: "code"])
        def allTerms = Term.list()
        if( endTerms.size() > 0 ) {
            try {
                def domainClass = Class.forName("net.hedtech.banner.student.faculty.FacultyAppointmentAccessView",
                        true, Thread.currentThread().getContextClassLoader() )
                domainClass.fetchAllActiveByMinTermEffectiveAndPidmInList(endTerms[0]?.code, pidms).each { faculty ->
                    def roles = results.get(faculty.pidm) ?: []
                    def newRole = new RoleDetail()
                    newRole.role = 'Faculty'
                    Term startDate = allTerms.find{ it.code == faculty.termEffective }
                    Term endDate = allTerms.find{ it.code == faculty.termEnd }
                    newRole.effectiveStartDate = startDate.startDate
                    newRole.effectiveEndDate = endDate.startDate
                    roles << newRole
                    results.put(faculty.pidm, roles )
                }
            }
            catch( ClassNotFoundException e ){
                log.debug "Student faculty plugin not present, unable to process Faculty roles"
            }
        }
        UserRole.findAllByStudentIndicatorAndPidmInList(true, pidms).each { it ->
            def roles = results.get(it.pidm) ?: []
            def newRole = new RoleDetail()
            newRole.role = 'Student'
            roles << newRole
            results.put(it.pidm, roles )
        }
        results
    }
}
