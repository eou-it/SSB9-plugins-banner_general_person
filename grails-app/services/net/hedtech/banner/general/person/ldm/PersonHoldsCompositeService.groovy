/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.person.ldm.v6.PersonHoldsDecorator
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for Person holds Service. Hold records on persons will be return .</p>
 */
class PersonHoldsCompositeService extends LdmService {

    def personHolsService
    private static final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V6]
    /**
     * GET /api/person-holds
     * @return List
     */
    @Transactional(readOnly = true)
    List<PersonHoldsDecorator> list(Map params) {
        String acceptVersion = getAcceptVersion(VERSIONS)

        List<PersonHoldsDecorator> personHoldsList = []
        def personHoldsListResult
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        params.offset = params.offset ?: 0
        if ((params.containsKey(GeneralValidationCommonConstants.PERSON_HOLD_SEARCH_FIELD))) {
            personHoldsListResult = personHolsService.fetchByPersonsGuid(params, new Date())
        } else {
            personHoldsListResult = personHolsService.fetchAll(params, new Date())
        }
        personHoldsListResult.each { personHolds ->
            personHoldsList << getPersonHoldDecortor(personHolds)
        }
        return personHoldsList
    }

    /**
     * @return Long value as total count
     */
    Long count(params) {
        if (params.containsKey(GeneralValidationCommonConstants.PERSON_HOLD_SEARCH_FIELD)) {
            return personHolsService.countRecordWithFilter(params, new Date())
        } else {
            return personHolsService.countRecord(new Date())
        }

    }

    /**
     * GET /api/person-holds/{guid}* @param guid
     * @return person holds
     */
    @Transactional(readOnly = true)
    PersonHoldsDecorator get(String guid) {
        String acceptVersion = getAcceptVersion(VERSIONS)

        def personHolds = personHolsService.fetchByPersonHoldGuid(guid?.toLowerCase()?.trim(), new Date())
        if (!personHolds) {
            throw new ApplicationException(PersonHoldsCompositeService.class, new NotFoundException())
        }
        return getPersonHoldDecortor(personHolds)
    }

    //Build decorator to return LDM response person holds.
    private def getPersonHoldDecortor(personHolds) {
        return new PersonHoldsDecorator(personHolds.getAt(0), personHolds.getAt(1).guid, personHolds.getAt(2).guid, personHolds.getAt(3).guid)
    }
}
