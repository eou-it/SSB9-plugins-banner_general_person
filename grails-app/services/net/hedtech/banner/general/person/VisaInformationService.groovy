/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import net.hedtech.banner.service.ServiceBase

class VisaInformationService extends ServiceBase {

    boolean transactional = true

    public static List<VisaInformation> fetchAllByPidmInList(List<String> pidmList) {
        return VisaInformation.fetchAllByPidmInList(pidmList)
    }
}
