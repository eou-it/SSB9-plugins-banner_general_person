/*********************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person.ldm.v6

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.system.ldm.v6.AddressTypeDecorator

/**
 * Person Address Decorator
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class PersonAddressDecorator {

    String addressGuid
    AddressTypeDecorator type
    String startOn
    String endOn

    def getAddress() {
        return [id: addressGuid]
    }
}
