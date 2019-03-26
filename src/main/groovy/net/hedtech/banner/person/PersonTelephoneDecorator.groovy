/*******************************************************************************
 Copyright 2009-2012 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/


package net.hedtech.banner.person

import net.hedtech.banner.general.person.PersonTelephone
import net.hedtech.banner.general.person.PersonTelephoneUtility
import net.hedtech.banner.general.system.AddressType
import net.hedtech.banner.general.system.TelephoneType

class PersonTelephoneDecorator {

    Integer pidm
    Integer sequenceNumber
    String phoneArea
    String phoneNumber
    String phoneExtension
    String statusIndicator
    Integer addressSequenceNumber
    String primaryIndicator
    String unlistIndicator
    String commentData
    String internationalAccess
    String countryPhone
    TelephoneType telephoneType
    AddressType addressType
    String displayPhone


    PersonTelephoneDecorator(PersonTelephone personTelephone) {
        pidm = personTelephone.pidm
        sequenceNumber = personTelephone.sequenceNumber
        phoneArea = personTelephone.phoneArea
        phoneNumber = personTelephone.getPhoneNumber()
        phoneExtension = personTelephone.phoneExtension
        statusIndicator = personTelephone.statusIndicator
        addressSequenceNumber = personTelephone.addressSequenceNumber
        primaryIndicator = personTelephone.primaryIndicator
        unlistIndicator = personTelephone.unlistIndicator
        commentData = personTelephone.commentData
        internationalAccess = personTelephone.internationalAccess
        countryPhone = personTelephone.countryPhone
        telephoneType = personTelephone.telephoneType
        addressType = personTelephone.addressType
        formatDisplayPhone()
    }


    PersonTelephoneDecorator(Map personTelephone) {
        pidm = personTelephone.pidm
        sequenceNumber = personTelephone.sequenceNumber
        phoneArea = personTelephone.phoneArea
        phoneNumber = personTelephone.phoneNumber
        phoneExtension = personTelephone.phoneExtension
        statusIndicator = personTelephone.statusIndicator
        addressSequenceNumber = personTelephone.addressSequenceNumber
        primaryIndicator = personTelephone.primaryIndicator
        unlistIndicator = personTelephone.unlistIndicator
        commentData = personTelephone.commentData
        internationalAccess = personTelephone.internationalAccess
        countryPhone = personTelephone.countryPhone
        telephoneType = personTelephone.telephoneType
        addressType = personTelephone.addressType
        formatDisplayPhone()
    }


    PersonTelephoneDecorator() {
        pidm = null
        sequenceNumber = null
        phoneArea = null
        phoneNumber = null
        phoneExtension = null
        statusIndicator = null
        addressSequenceNumber = null
        primaryIndicator = null
        unlistIndicator = null
        commentData = null
        internationalAccess = null
        countryPhone = null
        telephoneType = null
        addressType = null
        displayPhone = null
    }


    public String toString() {
        """PersonTelephoneDecorator[
        pidm = $pidm,
        sequenceNumber = $sequenceNumber,
        phoneArea = $phoneArea,
        phoneNumber = $phoneNumber,
        phoneExtension = $phoneExtension,
        statusIndicator = $statusIndicator,
        addressSequenceNumber = $addressSequenceNumber,
        primaryIndicator = $primaryIndicator,
        unlistIndicator = $unlistIndicator,
        commentData = $commentData,
        internationalAccess = $internationalAccess,
        countryPhone = $countryPhone,
        telephoneType = $telephoneType,
        addressType = $addressType,
        displayPhone = $displayPhone]"""
    }


    public String formatDisplayPhone() {
        def personTelephoneMap = [phoneCountry: countryPhone, phoneArea: phoneArea, phoneNumber: phoneNumber, phoneExtension: phoneExtension, phoneInternational: internationalAccess]
        displayPhone = PersonTelephoneUtility.formatPhone(personTelephoneMap)
        return displayPhone
    }

}
