/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.person.view

class PersonAddressByRoleViewService {

    def getActiveAddressesByRoles(roles, pidm) {
        Map params = [roles: roles, pidm: pidm]
        PersonAddressByRoleView.fetchAddressesByPidmAndRoles(params)
    }
}