/*********************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.person.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.person.PersonIdentificationName
import net.hedtech.banner.general.person.PersonRelatedHold
import net.hedtech.banner.general.person.PersonUtility
import net.hedtech.banner.general.person.ldm.v1.Person
import net.hedtech.banner.general.person.ldm.v1.PersonRestrictionType
import net.hedtech.banner.general.system.HoldType
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.general.system.ldm.v1.RestrictionType
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * Service used to support "restriction-types" resource for CDM
 */
@Transactional
class RestrictionTypeCompositeService extends LdmService {

    public static final String PERSON_HOLD_TYPES = "person-hold-types"

    private static
    final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V1, GeneralValidationCommonConstants.VERSION_V4]

    def holdTypeService


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    List<RestrictionType> list(Map params) {
        String acceptVersion = getAcceptVersion(VERSIONS)

        if (params?.parentPluralizedResourceName == GeneralValidationCommonConstants.PERSONS_ENDPOINT) {
            def returnLists = []
            if (params?.parentId) {
                String guid = params.parentId?.trim()?.toLowerCase()
                GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.PERSONS_LDM_NAME, guid)
                if (!globalUniqueIdentifier) {
                    throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Person.class.simpleName))
                }
                PersonIdentificationName personIdentificationName = PersonUtility.getPerson(globalUniqueIdentifier.domainKey.toInteger())
                if (!personIdentificationName) {
                    throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Person.class.simpleName))
                }
                log.debug "Person '${personIdentificationName.bannerId}' found in SV_SPRIDEN with Surrogate Id ${personIdentificationName.id}"

                def restrictionTypes
                RestrictionType restrictionType
                restrictionTypes = PersonRelatedHold.fetchAllDistinctHoldTypeByPidmAndDateCompare(personIdentificationName.pidm, new Date())
                restrictionTypes?.each { hold ->
                    restrictionType = fetchByRestrictionTypeCode(hold)
                    returnLists << new PersonRestrictionType(restrictionType.guid)
                }
            }
            return returnLists
        }
        //start-for Version 6 Schema support -PERSON-HOLD-TYPES
        else if (params?.pluralizedResourceName?.equals(PERSON_HOLD_TYPES)) {
            List restrictionTypes = []
            RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
            params.offset = params?.offset ?: 0
            List<HoldType> holdTypes = holdTypeService.list(params) as List
            holdTypes.each { holdType ->
                restrictionTypes << getDecorator(holdType)
            }
            return restrictionTypes

        }
        //end-for Version 6 Schema support -PERSON-HOLD-TYPES
        else {
            List restrictionTypes = []
            // List allowedSortFields = ("v4".equals(LdmService.getAcceptVersion(GeneralValidationCommonConstants.VERSIONS_V1_V4)) ? ['code', 'title'] : ['abbreviation', 'title'])
            // List allowedSortFields = ("v4".equals(LdmService.getAcceptVersion(GeneralValidationCommonConstants.VERSIONS_V1_V4)) ? ['code', 'title'] : ['abbreviation', 'title'])
            List allowedSortFields = [GeneralValidationCommonConstants.ABBREVIATION, GeneralValidationCommonConstants.TITLE]
            RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
            RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
            RestfulApiValidationUtility.validateSortOrder(params.order)

            params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)
            List<HoldType> holdTypes = holdTypeService.list(params) as List
            holdTypes.each { holdType ->
                restrictionTypes << getDecorator(holdType)
            }
            return restrictionTypes

        }
    }


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    Long count(params) {
        Integer count
        if (params?.parentPluralizedResourceName) {
            String guid = params.parentId?.trim()?.toLowerCase()
            GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.PERSONS_LDM_NAME, guid)
            PersonIdentificationName personIdentificationName = PersonUtility.getPerson(globalUniqueIdentifier.domainKey.toInteger())
            List<PersonRelatedHold> restrictionTypes = []
            restrictionTypes = PersonRelatedHold.fetchByPidmAndDateCompare(personIdentificationName.pidm, new Date())
            count = restrictionTypes.size()
        } else {
            count = HoldType.count()
        }
        return count
    }


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    RestrictionType get(String guid) {
        String acceptVersion = getAcceptVersion(VERSIONS)

        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.RESTRICTION_TYPE_LDM_NAME, guid)
        if (!globalUniqueIdentifier && GeneralValidationCommonConstants.VERSION_V6.equals(getAcceptVersion(VERSIONS))) {
            throw new ApplicationException(GeneralValidationCommonConstants.PERSON_HOLD_TYPES, new NotFoundException())
        } else if (!globalUniqueIdentifier && !GeneralValidationCommonConstants.VERSION_V6.equals(getAcceptVersion(VERSIONS))) {
            throw new ApplicationException("restrictionType", new NotFoundException())
        }

        HoldType holdType = HoldType.get(globalUniqueIdentifier.domainId)
        if (!holdType && GeneralValidationCommonConstants.VERSION_V6.equals(getAcceptVersion(VERSIONS))) {
            throw new ApplicationException(GeneralValidationCommonConstants.PERSON_HOLD_TYPES, new NotFoundException())
        } else if (!holdType && !GeneralValidationCommonConstants.VERSION_V6.equals(getAcceptVersion(VERSIONS))) {
            throw new ApplicationException("restrictionType", new NotFoundException())
        }

        return getDecorator(holdType, globalUniqueIdentifier?.guid)
    }

    /**
     * POST /api/restriction-types
     *
     * @param content Request body
     * @return
     */
    def create(Map content) {
        String acceptVersion = getAcceptVersion(VERSIONS)

        HoldType holdType = HoldType.findByCode(content?.code)
        if (holdType) {
            /*  def parameterValue
              if (GeneralValidationCommonConstants.VERSION_V4.equals(LdmService.getAcceptVersion(GeneralValidationCommonConstants.VERSIONS_V1_V4))) {
                  parameterValue = GeneralValidationCommonConstants.CODE
              } else if (GeneralValidationCommonConstants.VERSION_V1.equals(LdmService.getAcceptVersion(GeneralValidationCommonConstants.VERSIONS_V1_V4))) {
                  parameterValue = GeneralValidationCommonConstants.ABBREVIATION
              }*/
            throw new ApplicationException('restriction.type', new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_EXISTS, [GeneralValidationCommonConstants.ABBREVIATION]))
        }
        holdType = bindRestrictionType(new HoldType(), content)
        String restrictionTypeGuid = content?.guid?.trim()?.toLowerCase()
        if (restrictionTypeGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            restrictionTypeGuid = updateGuidValue(holdType.id, restrictionTypeGuid, GeneralValidationCommonConstants.RESTRICTION_TYPE_LDM_NAME)?.guid
        } else {
            restrictionTypeGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(GeneralValidationCommonConstants.RESTRICTION_TYPE_LDM_NAME, holdType?.id)?.guid
        }
        return getDecorator(holdType, restrictionTypeGuid)
    }

    /**
     * PUT /api/restriction-types/<guid>
     *
     * @param content Request body
     * @return
     */
    def update(Map content) {
        String acceptVersion = getAcceptVersion(VERSIONS)

        String holdTypeGuid = content?.id?.trim()?.toLowerCase()
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.RESTRICTION_TYPE_LDM_NAME, holdTypeGuid)
        if (holdTypeGuid) {
            if (!globalUniqueIdentifier) {
                if (!content?.guid) {
                    content.put('guid', holdTypeGuid)
                }
                //Per strategy when a GUID was provided, the create should happen.
                return create(content)
            }
        } else {
            throw new ApplicationException("restrictionType", new NotFoundException())
        }

        HoldType holdType = HoldType.findById(globalUniqueIdentifier?.domainId)
        if (!holdType) {
            throw new ApplicationException("restrictionType", new NotFoundException())
        }

        // Should not allow to update holdType.code as it is read-only
        if (holdType.code != content?.code?.trim()) {
            content.put("code", holdType.code)
        }
        holdType = bindRestrictionType(holdType, content)
        return getDecorator(holdType, holdTypeGuid)
    }


    private def bindRestrictionType(HoldType holdType, Map content) {
        bindData(holdType, content)
        holdTypeService.createOrUpdate(holdType)
    }


    private void bindData(domainModel, content) {
        super.setDataOrigin(domainModel, content)
        super.bindData(domainModel, content, [:])
    }


    private def getDecorator(HoldType holdType, String holdTypeGuid = null) {
        if (holdType) {
            if (!holdTypeGuid) {
                holdTypeGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(GeneralValidationCommonConstants.RESTRICTION_TYPE_LDM_NAME, holdType?.id)?.guid
            }
            new RestrictionType(holdType, holdTypeGuid, new Metadata(holdType?.dataOrigin))
        }
    }


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    RestrictionType fetchByRestrictionTypeId(Long holdTypeId) {
        if (null == holdTypeId) {
            return null
        }
        HoldType holdType = holdTypeService.get(holdTypeId) as HoldType
        if (!holdType) {
            return null
        }
        return new RestrictionType(holdType, GlobalUniqueIdentifier.findByLdmNameAndDomainId(GeneralValidationCommonConstants.RESTRICTION_TYPE_LDM_NAME, holdTypeId)?.guid, new Metadata(holdType.dataOrigin))
    }


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    RestrictionType fetchByRestrictionTypeCode(String holdTypeCode) {
        RestrictionType restrictionType = null
        if (holdTypeCode) {
            HoldType holdType = HoldType.findByCode(holdTypeCode)
            if (!holdType) {
                return restrictionType
            }
            restrictionType = new RestrictionType(holdType, GlobalUniqueIdentifier.findByLdmNameAndDomainId(GeneralValidationCommonConstants.RESTRICTION_TYPE_LDM_NAME, holdType.id)?.guid, new Metadata(holdType.dataOrigin))
        }
        return restrictionType
    }

}
