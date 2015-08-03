/*********************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.person.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
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
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
class RestrictionTypeCompositeService extends LdmService {

    def holdTypeService
    def
    private static final String RESTRICTION_TYPE_LDM_NAME = 'restriction-types'
    private static final String PERSONS_LDM_NAME = "persons"
    private static final List<String> VERSIONS = ["v1","v2","v3","v4"]


    List<RestrictionType> list(Map params) {
        if (params?.parentPluralizedResourceName == "persons") {
            def returnLists = []
            if(params?.parentId){
                String guid = params.parentId?.trim()?.toLowerCase()
                GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(PERSONS_LDM_NAME, guid)
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
        else
        {
            List restrictionTypes = []
            List allowedSortFields = ("v4".equals(LdmService.getAcceptVersion(VERSIONS))? ['code', 'title']:['abbreviation', 'title'])

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


    Long count(params) {
        Integer count
        if (params?.parentPluralizedResourceName) {
            String guid = params.parentId?.trim()?.toLowerCase()
            GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(PERSONS_LDM_NAME, guid)
            PersonIdentificationName personIdentificationName = PersonUtility.getPerson(globalUniqueIdentifier.domainKey.toInteger())
            List<PersonRelatedHold> restrictionTypes = []
            restrictionTypes = PersonRelatedHold.fetchByPidmAndDateCompare(personIdentificationName.pidm, new Date())
            count = restrictionTypes.size()
        }
        else
        {
            count = HoldType.count()
        }
        return count
    }


    RestrictionType get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(RESTRICTION_TYPE_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException("restrictionType", new NotFoundException())
        }

        HoldType holdType = HoldType.get(globalUniqueIdentifier.domainId)
        if (!holdType) {
            throw new ApplicationException("restrictionType", new NotFoundException())
        }

        return getDecorator(holdType, globalUniqueIdentifier?.guid)
    }

/**
 * POST /api/restriction-types
 *
 * @param content Request body
 */
    def create(Map content) {
        validateRequest(content)
        HoldType holdType = bindRestrictionType(new HoldType(), content)
        String restrictionTypeGuid = content?.guid?.trim()?.toLowerCase()
        if (restrictionTypeGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            restrictionTypeGuid = updateGuidValue(holdType.id, restrictionTypeGuid, RESTRICTION_TYPE_LDM_NAME)?.guid
        } else {
            restrictionTypeGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(RESTRICTION_TYPE_LDM_NAME, holdType?.id)?.guid
        }
        return getDecorator(holdType, restrictionTypeGuid)
    }

    private void validateRequest(content) {
     if (HoldType.findByCode(content?.code)) {
            throw new ApplicationException('restriction.type', new BusinessLogicValidationException('code.exists.message', ['abbreviation']))
        }
    }

    private def bindRestrictionType(HoldType holdType, Map content) {
        bindData(holdType, content)
        holdTypeService.createOrUpdate(holdType)
    }

    private void bindData(domainModel, content) {
        super.setDataOrigin(domainModel,content)
        super.bindData(domainModel,content,[:])
    }

    private def getDecorator(HoldType holdType, String holdTypeGuid = null) {
        if (holdType) {
            if (!holdTypeGuid) {
                holdTypeGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(RESTRICTION_TYPE_LDM_NAME, holdType?.id)?.guid
            }
                new RestrictionType(holdType, holdTypeGuid, new Metadata(holdType?.dataOrigin))
        }
    }
    
    RestrictionType fetchByRestrictionTypeId(Long holdTypeId) {
        if (null == holdTypeId) {
            return null
        }
        HoldType holdType = holdTypeService.get(holdTypeId) as HoldType
        if (!holdType) {
            return null
        }
        return new RestrictionType(holdType, GlobalUniqueIdentifier.findByLdmNameAndDomainId(RESTRICTION_TYPE_LDM_NAME, holdTypeId)?.guid, new Metadata(holdType.dataOrigin))
    }


    RestrictionType fetchByRestrictionTypeCode(String holdTypeCode) {
        RestrictionType restrictionType = null
        if (holdTypeCode) {
            HoldType holdType = HoldType.findByCode(holdTypeCode)
            if (!holdType) {
                return restrictionType
            }
            restrictionType = new RestrictionType(holdType, GlobalUniqueIdentifier.findByLdmNameAndDomainId(RESTRICTION_TYPE_LDM_NAME, holdType.id)?.guid, new Metadata(holdType.dataOrigin))
        }
        return restrictionType
    }
}
