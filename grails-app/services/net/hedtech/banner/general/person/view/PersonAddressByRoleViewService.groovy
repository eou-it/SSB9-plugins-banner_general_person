/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.person.view

class PersonAddressByRoleViewService {

    def getActiveAddressesByRoles(roles, pidm) {
        Map params = [roles: roles, pidm: pidm]
        def addresses = PersonAddressByRoleView.fetchAddressesByPidmAndRoles(params)

        // PersonAddressByRoleView.fetchAddressesByPidmAndRoles has a known issue
        // of not selecting unique addresses, so work around that.
        addresses.unique()
    }
}