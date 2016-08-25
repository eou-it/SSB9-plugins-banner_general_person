/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfigurationService
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.person.ldm.v6.NameV6
import net.hedtech.banner.general.person.ldm.v7.PersonContactDecorator
import net.hedtech.banner.general.person.ldm.v7.PersonEmergencyAddressDecorator
import net.hedtech.banner.general.person.ldm.v7.PersonEmergencyContactDecorator
import net.hedtech.banner.general.person.ldm.v7.PersonEmergencyPhoneDecorator
import net.hedtech.banner.general.person.view.PersonEmergencyContactView
import net.hedtech.banner.general.person.view.PersonEmergencyContactViewService
import net.hedtech.banner.general.system.Nation
import net.hedtech.banner.general.system.ldm.AddressTypeCompositeService
import net.hedtech.banner.general.system.ldm.RelationshipCompositeService
import net.hedtech.banner.general.utility.IsoCodeService
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

@Transactional
class PersonContactCompositeService extends LdmService {
    PersonEmergencyContactViewService personEmergencyContactViewService
    IntegrationConfigurationService integrationConfigurationService
    RelationshipCompositeService relationshipCompositeService
    IsoCodeService isoCodeService
    AddressTypeCompositeService addressTypeCompositeService

    private static final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V7]

    /**
     * GET /api/person-contacts
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true)
    def get(id) {
        log.trace "getById:Begin:$id"
        String acceptVersion = getAcceptVersion(VERSIONS)

        List<PersonEmergencyContactView> entities = personEmergencyContactViewService.fetchAllByCriteria(["guid": id])
        PersonEmergencyContactView personEmergencyContactView
        if (entities) {
            personEmergencyContactView = entities[0]
        }
        if (!personEmergencyContactView) {
            throw new ApplicationException("PersonContactCompositeService", new NotFoundException())
        }
        return createPersonContactDataModelV7([personEmergencyContactView])[0]
    }

    /**
     * GET /api/person-contacts
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true)
    def list(Map params) {
        log.trace "list:Begin:$params"
        String acceptVersion = getAcceptVersion(VERSIONS)

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)

        String sortField = params.sort?.trim()
        String sortOrder = params.order?.trim()
        int max = params.max?.trim()?.toInteger() ?: 0
        int offset = params.offset?.trim()?.toInteger() ?: 0

        List<PersonEmergencyContactView> personEmergencyContactViewList = personEmergencyContactViewService.fetchAllByCriteria(prepareSearchMapForList(params), sortField, sortOrder, max, offset)

        return createPersonContactDataModelV7(personEmergencyContactViewList)
    }


    def count(Map params) {
        return personEmergencyContactViewService.countByCriteria(prepareSearchMapForList(params))
    }


    private def prepareSearchMapForList(final Map params) {
        Map searchMap = [:]
        if (params.containsKey('person')) {
            searchMap.put("personGuid", params.get("person"))
        }
        return searchMap
    }


    private List<PersonContactDecorator> createPersonContactDataModelV7(List<PersonEmergencyContactView> personEmergencyContactViewList) {
        List<PersonContactDecorator> personContactDecoratorList = []
        def bannerRelationshipTypeToHedmRelationshipTypeMap = relationshipCompositeService.getBannerRelationshipTypeToHedmV7RelationshipTypeMap()
        boolean institutionUsingISO2CountryCodes = integrationConfigurationService.isInstitutionUsingISO2CountryCodes()
        String defaultISO3CountryCode = integrationConfigurationService.getDefaultISO3CountryCodeForAddress(institutionUsingISO2CountryCodes)
        String defaultCountryTitle = getDefaultCountryTitle()
        def bannerAddressTypeToHedmAddressTypeMap = addressTypeCompositeService.getBannerAddressTypeToHedmV6AddressTypeMap()
        personEmergencyContactViewList.each {
            PersonContactDecorator personContactDecorator = new PersonContactDecorator()
            personContactDecorator.guid = it.guid
            personContactDecorator.personGuid = it.personGuid
            PersonEmergencyContactDecorator personEmergencyContactDecorator = new PersonEmergencyContactDecorator()
            personEmergencyContactDecorator.priority = it.priority
            personEmergencyContactDecorator.name = getName(it.firstName, it.middleInitial, it.lastName)

            String hedmAddressType = bannerAddressTypeToHedmAddressTypeMap.get(it.addressTypeCode)
            if (hedmAddressType) {
                PersonEmergencyAddressDecorator personEmergencyAddressDecorator = createContactAddressDataModel(it, institutionUsingISO2CountryCodes, defaultISO3CountryCode, defaultCountryTitle, hedmAddressType)
                personEmergencyContactDecorator.contactAddress = [address: personEmergencyAddressDecorator]
            }

            if (it.countryPhone || it.phoneArea || it.phoneNumber || it.phoneExtension) {
                personEmergencyContactDecorator.phone = new PersonEmergencyPhoneDecorator(it.countryPhone, it.phoneArea, it.phoneNumber, it.phoneExtension)
            }

            String hedmRelationshipType = bannerRelationshipTypeToHedmRelationshipTypeMap.get(it.relationshipCode)
            if (hedmRelationshipType) {
                personEmergencyContactDecorator.relationshipGuid = it.relationshipTypeGuid
                personEmergencyContactDecorator.hedmRelationshipType = hedmRelationshipType
            }

            personContactDecorator.contact = personEmergencyContactDecorator
            personContactDecoratorList << personContactDecorator
        }
        return personContactDecoratorList
    }


    private NameV6 getName(String firstName, String middleName, String lastName) {
        NameV6 name = new NameV6()
        name.firstName = firstName
        name.middleName = middleName
        name.lastName = lastName
        name.fullName = NameV6.getFullName(firstName, middleName, lastName)
        return name
    }


    private PersonEmergencyAddressDecorator createContactAddressDataModel(PersonEmergencyContactView personEmergencyContactView, boolean institutionUsingISO2CountryCodes, String defaultISO3CountryCode, String defaultCountryTitle, String hedmAddressType) {
        String iso3CountryCode = getISO3CountryCode(personEmergencyContactView, institutionUsingISO2CountryCodes)
        if (!iso3CountryCode) {
            iso3CountryCode = defaultISO3CountryCode
        }
        validateRegion(personEmergencyContactView)

        return new PersonEmergencyAddressDecorator(personEmergencyContactView, iso3CountryCode, defaultCountryTitle, hedmAddressType)
    }


    private String getISO3CountryCode(PersonEmergencyContactView personEmergencyContactView, boolean institutionUsingISO2CountryCodes) {
        String isoCountryCode
        if (personEmergencyContactView.nationCode) {
            isoCountryCode = Nation.findByCode(personEmergencyContactView.nationCode).scodIso
            if (isoCountryCode == null) {
                throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("country.code.not.mapped.to.iso.code.message", [personEmergencyContactView.nationCode]))
            }
        }

        if (institutionUsingISO2CountryCodes) {
            isoCountryCode = isoCodeService.getISO3CountryCode(isoCountryCode)
        }

        return isoCountryCode
    }


    private void validateRegion(PersonEmergencyContactView personEmergencyContactView) {
        if (!isISOCodeAvailableForState(personEmergencyContactView)) {
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("soaxref.region.mapping.not.found.message", [personEmergencyContactView.stateCode]))
        }
    }


    private boolean isISOCodeAvailableForState(PersonEmergencyContactView PersonEmergencyContactView) {
        boolean available = true
        if (PersonEmergencyContactView.stateCode != null && PersonEmergencyContactView.countryRegionCode == null) {
            available = false
        }
        return available
    }


    public String getDefaultCountryTitle() {
        String countryTitle
        Nation nation = Nation.findByScodIso(integrationConfigurationService.getDefaultISOCountryCodeForAddress())
        if (!nation) {
            countryTitle = "Unknown"
        } else {
            countryTitle = nation.nation
        }
        return countryTitle
    }


}
