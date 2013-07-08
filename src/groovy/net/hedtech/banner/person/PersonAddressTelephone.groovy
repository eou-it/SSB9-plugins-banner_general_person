/*******************************************************************************
Copyright 2013 Ellucian Company L.P. and its affiliates.
*******************************************************************************/
package net.hedtech.banner.person

import net.hedtech.banner.general.system.TelephoneType
import net.hedtech.banner.general.person.PersonAddress

//This class is used as an interface for the PersonAddressCompositeService

class PersonAddressTelephone {

    PersonAddress personAddress
    TelephoneType telephoneType
    String phoneArea
    String phoneNumber
    String phoneExtension
    String countryPhone
    Integer telephoneSequenceNumber



    PersonAddressTelephone(PersonAddress personAddress, TelephoneType telephoneType, String countryPhone, String phoneArea, String phoneNumber, String phoneExtension, Integer telephoneSequenceNumber) {
        this.personAddress = personAddress
        this.telephoneType = telephoneType
        this.countryPhone = countryPhone
        this.phoneArea = phoneArea
        this.phoneNumber = phoneNumber
        this.phoneExtension = phoneExtension
        this.telephoneSequenceNumber = telephoneSequenceNumber
    }


    PersonAddressTelephone(Map map) {
        this.personAddress = map.personAddress
        this.telephoneType = map.telephoneType
        this.countryPhone = map.countryPhone
        this.phoneArea = map.phoneArea
        this.phoneNumber = map.phoneNumber
        this.phoneExtension = map.phoneExtension
        this.telephoneSequenceNumber = map.telephoneSequenceNumber
    }


    public String toString ( ) {
        return "PersonAddressTelephone{" +
                "personAddress=" + personAddress +
                ", telephoneType=" + telephoneType +
                ", phoneArea='" + phoneArea + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", phoneExtension='" + phoneExtension + '\'' +
                ", countryPhone='" + countryPhone + '\''+
                ", telephoneSequenceNumber='" + telephoneSequenceNumber + '\''+
            '}' ;
    }

}
