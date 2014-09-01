/*********************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.person.ldm


import grails.util.GrailsNameUtils
import net.hedtech.banner.exceptions.ApplicationException
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
class RestrictionTypeCompositeService {

    def holdTypeService
    def
    private static final String RESTRICTION_TYPE_LDM_NAME = 'restriction-types'
    private static final String LDM_NAME = "persons"


    List<RestrictionType> list(Map params) {
        if (params?.parentPluralizedResourceName) {
            def returnLists = []
            if(params?.parentId){
                String guid = params.parentId?.trim()
                GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME, guid)
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
            List allowedSortFields = ['abbreviation', 'title']

            RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
            RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
            RestfulApiValidationUtility.validateSortOrder(params.order)

            params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)
            List<HoldType> holdTypes = holdTypeService.list(params) as List
            holdTypes.each { holdType ->
                restrictionTypes << new RestrictionType(holdType, GlobalUniqueIdentifier.findByLdmNameAndDomainId(RESTRICTION_TYPE_LDM_NAME, holdType.id)?.guid, new Metadata(holdType.dataOrigin))
            }
            return restrictionTypes

        }
    }


    Long count(params) {
        Integer count
        if (params?.parentPluralizedResourceName) {
            String guid = params.parentId?.trim()
            GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME, guid)
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
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: GrailsNameUtils.getNaturalName(RestrictionType.class.simpleName)))
        }

        HoldType holdType = HoldType.get(globalUniqueIdentifier.domainId)
        if (!holdType) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: GrailsNameUtils.getNaturalName(RestrictionType.class.simpleName)))
        }

        return new RestrictionType(holdType, globalUniqueIdentifier.guid, new Metadata(holdType.dataOrigin));
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
