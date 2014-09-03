/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

import net.hedtech.banner.general.person.ldm.v1.RoleDetail
import net.hedtech.banner.general.system.Term
import net.hedtech.banner.student.faculty.FacultyAppointmentAccessView


class UserRoleCompositeService {

    def dataSource

    Map fetchAllByRole( role ) {
        def results = [:]
            switch (role) {
                case 'Faculty':
                    def endTerms = Term.findAllByEndDateGreaterThan(new Date(),[sort: "code"])
                    log.error endTerms.toString()
                    if( endTerms.size() > 0 ) {
                        FacultyAppointmentAccessView.fetchAllActiveByMinTermEffective(endTerms[0]?.code).each { FacultyAppointmentAccessView it ->
                            def newRole = new RoleDetail()
                            newRole.role = 'Faculty'
                            Term startDate = Term.findByCode(it.termEffective)
                            Term endDate = Term.findByCode(it.termEnd) //TODO: Less queries to Term?
                            newRole.effectiveStartDate = startDate.startDate
                            newRole.effectiveEndDate = endDate.startDate
                            results.put(it.pidm, newRole)
                        }
                        log.error results.toString()
                    }
                    break
                case 'Student':
                    break
            }

        results
    }
}
