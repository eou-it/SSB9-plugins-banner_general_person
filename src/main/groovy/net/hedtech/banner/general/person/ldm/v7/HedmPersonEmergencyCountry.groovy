/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm.v7


enum HedmPersonEmergencyCountry {
    AUS("AUSTRALIA", CountryPostalCodePatternConstants.AUS),
    BRA("BRAZIL", CountryPostalCodePatternConstants.BRA),
    CAN("CANADA", CountryPostalCodePatternConstants.CAN),
    MEX("MEXICO", CountryPostalCodePatternConstants.MEX),
    NLD("NETHERLANDS", CountryPostalCodePatternConstants.NLD),
    GBR("UNITED KINGDOM OF GREAT BRITAIN AND NORTHERN IRELAND", CountryPostalCodePatternConstants.GBR),
    USA("UNITED STATES OF AMERICA", CountryPostalCodePatternConstants.USA)

    private String postalTitle
    private String postalCodePattern


    HedmPersonEmergencyCountry(String postalTitle, String postalCodePattern) {
        this.postalTitle = postalTitle
        this.postalCodePattern = postalCodePattern
    }


    String getPostalTitle() {
        return postalTitle
    }

    String getPostalCodePattern() {
        return postalCodePattern
    }


    public static HedmPersonEmergencyCountry getByString(String value) {
        if (value) {
            Iterator itr = HedmPersonEmergencyCountry.values().iterator()
            while (itr.hasNext()) {
                HedmPersonEmergencyCountry hedmCountry = itr.next()
                if (hedmCountry.toString().equals(value)) {
                    return hedmCountry
                }
            }
        }
        return null
    }
}
