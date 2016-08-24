/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person.ldm.v7

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class PersonEmergencyRegionDecorator {
    String code
    String title

    PersonEmergencyRegionDecorator(String code, String title){
        this.code = code
        this.title = title
    }

}
