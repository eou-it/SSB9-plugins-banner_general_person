/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person


class PersonRaceCompositeService {
    def raceService
    def personRaceService

    def getRacesByPidm(pidm) {
        def personRaces = personRaceService.fetchRaceByPidmList([pidm])
        def races = personRaces.collect { it.race }

        return raceService.fetchAllByRaceInList(races)
    }
}
