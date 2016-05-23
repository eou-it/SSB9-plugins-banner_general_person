/*********************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person.ldm.v6

import net.hedtech.banner.general.person.PersonRelatedHold
import net.hedtech.banner.general.system.ldm.v1.RestrictionType
import net.hedtech.banner.general.utility.DateConvertHelperService

class PersonHoldsDecorator {

    private  PersonRelatedHold personRelatedHold
    String personId
    String id
    String personHoldTypeId

    PersonHoldsDecorator(PersonRelatedHold personRelatedHold,String personHoldId,String personId,String personHoldTypeId){
        this.personRelatedHold = personRelatedHold
        this.id = personHoldId
        this.personId = personId
        this.personHoldTypeId = personHoldTypeId
    }

    Map getPerson(){
        return [id:personId]
    }

    RestrictionType getType(){
     return   new  RestrictionType(personRelatedHold.holdType,personHoldTypeId,null)
    }

    String getEndOn(){
      return DateConvertHelperService.convertDateIntoUTCFormat(personRelatedHold.toDate)
    }

    String getStartOn(){
        return DateConvertHelperService.convertDateIntoUTCFormat(personRelatedHold.fromDate)
    }

}
