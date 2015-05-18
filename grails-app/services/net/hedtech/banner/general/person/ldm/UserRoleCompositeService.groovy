/*******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

import net.hedtech.banner.general.person.PersonIdentificationNameCurrent
import net.hedtech.banner.general.person.StudentRole
import net.hedtech.banner.general.person.ldm.v1.RoleDetail
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.QueryBuilder


class UserRoleCompositeService {
/**
 *
 * @param [role :"Student|Faculty",sortAndPaging[sort:"firstName|lastName",...etc]
 * @return List of ordered pidms [12345,12344]
 * We are using executeQuery for performance reasons, only returning
 * what we really need, and using our ability to inner join un-related domains in executeQuery.
 */
    def fetchAllByRole(Map params, boolean count = false) {
        def results
        switch (params.role.toLowerCase()) {
            case 'faculty':
                def orderByString = params.sort ? " order by a." + params.sort : "" +
                        (params.order && params.sort) ? " " + params.order : ""
                try {
                    def domainClass = Class.forName("net.hedtech.banner.student.faculty.FacultyAppointmentAccessView",
                            true, Thread.currentThread().getContextClassLoader())
                    def query = "Select a.pidm from PersonIdentificationNameCurrent a," +
                            " FacultyAppointmentAccessView b" +
                            " where a.pidm = b.pidm" +
                            " and b.activeIndicator = 'A'" +
                            " and b.instructorIndicator = 'Y'" +
                            " and b.termEffective = ( select min(c.termEffective)" +
                            "                       from FacultyAppointmentAccessView c, Term e" +
                            "                       where c.pidm = b.pidm" +
                            "                       and c.termEnd = e.code" +
                            "                       and c.activeIndicator = 'A'" +
                            "                       and c.instructorIndicator = 'Y'" +
                            "                       and current_date < e.endDate)" +
                            orderByString
                    results = PersonIdentificationNameCurrent.executeQuery(query)
                }
                catch (ClassNotFoundException e) {
                    log.debug "Student faculty plugin not present, unable to process Faculty roles"
                }

                break
            case 'student':
                def query = """from StudentRole a"""
                DynamicFinder dynamicFinder = new DynamicFinder(StudentRole.class, query, "a")

                if (count) {
                    results = dynamicFinder.count([params: [:], criteria: []])
                } else {
                    def filterMap = QueryBuilder.getFilterData(params)
                    results = dynamicFinder.find([params: filterMap.params, criteria: filterMap.criteria], filterMap.pagingAndSortParams)
                }

                break
        }

        results
    }

    Map<Integer, List<RoleDetail>> fetchAllRolesByPidmInList(List pidms, def students, Boolean studentRole) {
        def results = [:]
        if (pidms.size()) {
            try {
                def domainClass = Class.forName("net.hedtech.banner.student.faculty.FacultyAppointmentAccessView",
                        true, Thread.currentThread().getContextClassLoader())
                def query = "Select a.pidm, d.startDate, f.startDate from PersonIdentificationNameCurrent a," +
                        " FacultyAppointmentAccessView b, Term d, Term f" +
                        " where a.pidm = b.pidm" +
                        " and d.code = b.termEffective " +
                        " and f.code = b.termEnd" +
                        " and b.activeIndicator = 'A'" +
                        " and b.instructorIndicator = 'Y'" +
                        " and b.termEffective = ( select min(c.termEffective)" +
                        "                       from FacultyAppointmentAccessView c, Term e" +
                        "                       where c.pidm = b.pidm" +
                        "                       and c.termEnd = e.code" +
                        "                       and c.activeIndicator = 'A'" +
                        "                       and c.instructorIndicator = 'Y'" +
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
            //Building Student role details
            if(!studentRole) {
                students = StudentRole.fetchByPidms(pidms)
            }
            students.each { it ->
                def roles = results.get(it.pidm) ?: []
                def newRole = new RoleDetail()
                newRole.role = 'Student'
                roles << newRole
                results.put(it.pidm, roles)
            }
        }

        results
    }

}
