/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase

class PriorCollegeConcentrationAreaService extends ServiceBase {

    def preUpdate(map) {
        throw new ApplicationException(PriorCollegeConcentrationArea, "@@r1:unsupported.operation@@")
    }
}
