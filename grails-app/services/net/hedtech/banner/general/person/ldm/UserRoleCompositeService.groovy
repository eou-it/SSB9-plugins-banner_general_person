/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

import net.hedtech.banner.general.overall.UserRole
import net.hedtech.banner.general.person.PersonIdentificationNameCurrent
import net.hedtech.banner.general.person.ldm.v1.RoleDetail
import net.hedtech.banner.general.system.Term


class UserRoleCompositeService {
/**
 *
 * @param [role:"Student|Faculty",sortAndPaging[sort:"firstName|lastName",...etc]
 * @return List of ordered pidms [12345,12344]
 * We break the rules on this one for performance reasons, using executeQuery, only returning
 * what we really need, and using our ability to inner join un-related domains in executeQuery.
 */
    List fetchAllByRole( Map params ) {
        def results = []
        def orderByString = params.sortAndPaging?.sort ? " order by a." + params.sortAndPaging.sort : "" +
                (params.sortAndPaging?.order && params.sortAndPaging?.sort) ? " " + params.sortAndPaging?.order : ""
        switch (params.role.toLowerCase()) {
            case 'faculty':
                try {
                    def domainClass = Class.forName("net.hedtech.banner.student.faculty.FacultyAppointmentAccessView",
                            true, Thread.currentThread().getContextClassLoader() )
                    def query = "Select a.pidm from PersonIdentificationNameCurrent a," +
                            " FacultyAppointmentAccessView b" +
                            " where a.pidm = b.pidm" +
                            " and b.activeIndicator = 'A'" +
                            " and b.termEffective = ( select max(c.termEffective)" +
                            "                       from FacultyAppointmentAccessView c, Term d" +
                            "                       where c.pidm = b.pidm" +
                            "                       and c.termEffective = d.code" +
                            "                       and c.activeIndicator = 'A'" +
                            "                       and d.startDate < current_date)" +
                            orderByString
                    results = PersonIdentificationNameCurrent.executeQuery(query,[:],params.sortAndPaging)
                }
                catch( ClassNotFoundException e ){
                    log.debug "Student faculty plugin not present, unable to process Faculty roles"
                }

                break
            case 'student':
                def query = "Select a.pidm from PersonIdentificationNameCurrent a," +
                        " UserRole b where a.pidm = b.pidm" +
                        " and b.studentIndicator = true" +
                        orderByString
                results = PersonIdentificationNameCurrent.executeQuery(query,[:],params?.sortAndPaging?:[:])
                break
        }
        results
    }

    Map<Integer,List<RoleDetail>> fetchAllRolesByPidmInList( List pidms) {
        def results = [:]
        def endTerm = Term.executeQuery("Select min(a.code) from Term a where" +
                " a.startDate = (Select min(b.startDate) from Term b where " +
                " b.endDate > current_date)")[0]
        def allTerms = Term.list()
        if( endTerm ) {
            try {
                def domainClass = Class.forName("net.hedtech.banner.student.faculty.FacultyAppointmentAccessView",
                        true, Thread.currentThread().getContextClassLoader() )
                domainClass.fetchAllActiveByMinTermEffectiveAndPidmInList(endTerm, pidms).each { faculty ->
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
