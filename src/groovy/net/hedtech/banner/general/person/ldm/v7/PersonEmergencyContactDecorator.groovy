/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person.ldm.v7

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.person.ldm.v6.NameV6

@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class PersonEmergencyContactDecorator {
    NameV6 name
    String priority
    def contactAddress
    PersonEmergencyPhoneDecorator phone
    String hedmRelationshipType
    String relationshipGuid

    def getTypes() {
        return ["emergency"]
    }

    def getRelationship() {
        if(hedmRelationshipType) {
            return ["type": hedmRelationshipType, "detail": ["id": relationshipGuid]]
        }
    }

    def getPhones() {
        if(phone) {
            return [phone]
        }
    }

}
