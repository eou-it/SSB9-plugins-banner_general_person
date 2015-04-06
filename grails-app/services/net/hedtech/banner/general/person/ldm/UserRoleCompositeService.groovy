/*******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

import net.hedtech.banner.general.person.PersonIdentificationNameCurrent
import net.hedtech.banner.general.person.ldm.v1.RoleDetail


class UserRoleCompositeService {
/**
 *
 * @param [role:"Student|Faculty",sortAndPaging[sort:"firstName|lastName",...etc]
 * @return List of ordered pidms [12345,12344]
 * We are using executeQuery for performance reasons, only returning
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
                            " and b.termEffective = ( select min(c.termEffective)" +
                            "                       from FacultyAppointmentAccessView c, Term e" +
                            "                       where c.pidm = b.pidm" +
                            "                       and c.termEnd = e.code" +
                            "                       and c.activeIndicator = 'A'" +
                            "                       and current_date < e.endDate)" +
                            orderByString
                    results = PersonIdentificationNameCurrent.executeQuery(query,[:],params.sortAndPaging)
                }
                catch( ClassNotFoundException e ){
                    log.debug "Student faculty plugin not present, unable to process Faculty roles"
                }

                break
            case 'student':
                def query = "select a.pidm from PersonIdentificationNameCurrent a where exists" +
                        " (select 1 from StudentBaseReadonly b " +
                        "where a.pidm = b.pidm)" +
                        orderByString
                results = PersonIdentificationNameCurrent.executeQuery(query,[:],params?.sortAndPaging?:[:])
                break
        }
        results
    }

    Map<Integer,List<RoleDetail>> fetchAllRolesByPidmInList( List pidms) {
        def results = [:]
        if( pidms.size() ) {
            try {
                def domainClass = Class.forName("net.hedtech.banner.student.faculty.FacultyAppointmentAccessView",
                        true, Thread.currentThread().getContextClassLoader())
                def query = "Select a.pidm, d.startDate, f.startDate from PersonIdentificationNameCurrent a," +
                        " FacultyAppointmentAccessView b, Term d, Term f" +
                        " where a.pidm = b.pidm" +
                        " and d.code = b.termEffective " +
                        " and f.code = b.termEnd" +
                        " and b.activeIndicator = 'A'" +
                        " and b.termEffective = ( select min(c.termEffective)" +
                        "                       from FacultyAppointmentAccessView c, Term e" +
                        "                       where c.pidm = b.pidm" +
                        "                       and c.termEnd = e.code" +
                        "                       and c.activeIndicator = 'A'" +
                        "                       and current_date < e.endDate) " +
                        " and b.pidm in (" + pidms.join(',') + ")"
                PersonIdentificationNameCurrent.executeQuery(query).each { faculty ->
                    def roles = results.get(faculty[0]) ?: []
                    def newRole = new RoleDetail()
                    newRole.role = 'Faculty'
                    newRole.effectiveStartDate = faculty[1]
                    newRole.effectiveEndDate = faculty[2]
                    roles << newRole
                    results.put(faculty[0], roles)

                }
            }
            catch (ClassNotFoundException e) {
                log.debug "Student faculty plugin not present, unable to process Faculty roles"
            }
            try {
                def domainClass = Class.forName("net.hedtech.banner.student.generalstudent.StudentBaseReadonly",
                        true, Thread.currentThread().getContextClassLoader())
                def query = "Select a.pidm from PersonIdentificationNameCurrent a where exists" +
                        " (select 1 from StudentBaseReadonly b " +
                        "where a.pidm = b.pidm)" +
                        " and a.pidm in (" + pidms.join(',') + ")"
                PersonIdentificationNameCurrent.executeQuery(query).each { it ->
                    def roles = results.get(it) ?: []
                    def newRole = new RoleDetail()
                    newRole.role = 'Student'
                    roles << newRole
                    results.put(it, roles)
                }
            } catch (ClassNotFoundException e) {
                log.debug "Student common plugin not present, unable to process student roles"
            }
        }

        results
    }

}
