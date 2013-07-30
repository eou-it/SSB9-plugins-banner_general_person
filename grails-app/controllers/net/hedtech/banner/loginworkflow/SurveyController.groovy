package net.hedtech.banner.loginworkflow

import net.hedtech.banner.general.person.PersonBasicPersonBase
import net.hedtech.banner.general.person.PersonRace
import net.hedtech.banner.general.system.Race
import net.hedtech.banner.general.system.RegulatoryRace
import net.hedtech.banner.general.system.SdaCrosswalkConversion

class SurveyController {
    def personRaceService
    def personBasicPersonBaseService
    static defaultAction = "index"

    def index() {
        render view: "survey"
    }

    /*def survey = {
        def pidm = 34525
        def pushSurvey = false
        def sdaxForSurveyStartDate = SdaCrosswalkConversion.fetchSurveyDates('RESTARTDAT', 1, 'SSMREDATE')[0]
        def sdaxForSurveyEndDate = SdaCrosswalkConversion.fetchSurveyDates('REENDDATE', 1, 'SSMREDATE')[0]
        if (sdaxForSurveyStartDate && sdaxForSurveyEndDate) {
            def today = new Date()
            def surveyStartDate = sdaxForSurveyStartDate?.reportingDate
            def surveyEndDate = sdaxForSurveyEndDate?.reportingDate ?: today
            // Survey start date is not null & Today is between Survey start and end dates
            if (surveyStartDate && (surveyStartDate <= today && today <= surveyEndDate)) {
                if (PersonBasicPersonBase.fetchByPidm(pidm)?.confirmedRe != 'Y') {
                    pushSurvey = true
                }
            }
        }
        if (pushSurvey) {
            def ethnicityList = ["Hispanic or Latino", "Not Hispanic or Latino"]
            def raceMap = [:]
            def regulatoryRaces = RegulatoryRace.fetchRequiredRegulatoryRaces()
            regulatoryRaces.each { regulatoryRace ->
                def races = []
                races = Race.fetchAllByRegulatoryRace(regulatoryRace.code)
                if (!races.isEmpty()) {
                    raceMap.put(regulatoryRace.code, races)
                }
            }
            // Get all Race Categories by system indicator = 'Y'
            // Loop over the Categories, get the Races under each Category and put them inside model.
            // Find a way to differentiate selected Races by the User from the not selected Races.
            def personRaces = PersonRace.fetchByPidm(34525)
            def personRaceCodes = []
            personRaces?.each {
                personRaceCodes.add(it.race)
            }
            def model = [raceMap: raceMap, regulatoryRaces: regulatoryRaces, ethnicityList: ethnicityList, personRaceCodes: personRaceCodes]
            render view: "survey", model: model
        } else {
            // Redirect to next page in the flow
            println "Survey not be pushed"
        }
    }*/


    /*def save = {
        def pidm = 34525
        println params
        // Save ethnicity
        def personBasicPersonBase = PersonBasicPersonBase.fetchByPidm(pidm)
        if (personBasicPersonBase) {
            *//*personBasicPersonBase {
                ethnic : 2
                confirmedRe : 'Y'
                confirmedReDate : new Date()
            }*//*
            personBasicPersonBase.ethnic = 2
            personBasicPersonBase.confirmedRe = 'Y'
            personBasicPersonBase.confirmedReDate = new Date()
            personBasicPersonBaseService.update(personBasicPersonBase)
        } else {
            personBasicPersonBase = new PersonBasicPersonBase(
                    pidm: pidm,
                    ethnic: 2,
                    confirmedRe: 'Y',
                    confirmedReDate: new Date(),
            )
            personBasicPersonBaseService.create(personBasicPersonBase)
        }
        // Save races
        def createPersonRaces = []
        def deletePersonRaces = []
        params.race?.each { race ->
            def personRace = PersonRace.fetchByPidmAndRace(pidm, race)
            createPersonRaces << (personRace ?: new PersonRace(pidm: pidm, race: race))
        }
        println "createPersonRaces: " + createPersonRaces
        def savedPersonRaces = PersonRace.fetchByPidm(pidm)
        deletePersonRaces = savedPersonRaces - createPersonRaces
        println "deletePersonRaces: " + deletePersonRaces
        personRaceService.createOrUpdate([createPersonRaces: createPersonRaces, deletePersonRaces: deletePersonRaces])

        render view: "survey"
    }*/

    def survey() {
        /*def ethnicityList = [1, 2]*/
        // "Not Hispanic or Latino", "Hispanic or Latino"
        def raceMap = [:]
        def regulatoryRaces = RegulatoryRace.fetchRequiredRegulatoryRaces()
        regulatoryRaces.each { regulatoryRace ->
            def races = []
            races = Race.fetchAllByRegulatoryRace(regulatoryRace.code)
            if (!races.isEmpty()) {
                raceMap.put(regulatoryRace.code, races)
            }
        }
        // Get all Race Categories by system indicator = 'Y'
        // Loop over the Categories, get the Races under each Category and put them inside model.
        // Find a way to differentiate selected Races by the User from the not selected Races.
        def personRaces = PersonRace.fetchByPidm(34525)
        def personRaceCodes = []
        personRaces?.each {
            personRaceCodes.add(it.race)
        }
        def personBasicPersonBase = PersonBasicPersonBase.fetchByPidm(34525)
        def personEthnicity = personBasicPersonBase?.ethnic
        session.setAttribute("raceMap", raceMap)
        session.setAttribute("regulatoryRaces", regulatoryRaces)
        def model = [raceMap: raceMap, regulatoryRaces: regulatoryRaces, personRaceCodes: personRaceCodes, personEthnicity: personEthnicity, postUrl: "${request.contextPath}/survey/save"]
        render view: "survey", model: model
    }


    def save = {
        def pidm = 34525
        println params
        def ethnicity = params.ethnicity ? params.ethnicity[0] : null
        // Save ethnicity
        def personBasicPersonBase = PersonBasicPersonBase.fetchByPidm(pidm)
        if (personBasicPersonBase) {
            personBasicPersonBase.ethnic = ethnicity
            personBasicPersonBase.confirmedRe = 'Y'
            personBasicPersonBase.confirmedReDate = new Date()
            personBasicPersonBaseService.update(personBasicPersonBase)
        } else {
            personBasicPersonBase = new PersonBasicPersonBase(
                    pidm: pidm,
                    ethnic: ethnicity,
                    confirmedRe: 'Y',
                    confirmedReDate: new Date(),
            )
            personBasicPersonBaseService.create(personBasicPersonBase)
        }
        // Save races
        def createPersonRaces = []
        def deletePersonRaces = []
        params.race?.each { race ->
            def personRace = PersonRace.fetchByPidmAndRace(pidm, race)
            createPersonRaces << (personRace ?: new PersonRace(pidm: pidm, race: race))
        }
        println "createPersonRaces: " + createPersonRaces
        def savedPersonRaces = PersonRace.fetchByPidm(pidm)
        deletePersonRaces = savedPersonRaces - createPersonRaces
        println "deletePersonRaces: " + deletePersonRaces
        personRaceService.createOrUpdate([createPersonRaces: createPersonRaces, deletePersonRaces: deletePersonRaces])

        render view: "survey"    // TODO: Need to validate this.
    }


    def completed () {
        request.getSession().setAttribute("surveydone", "true")
       /* String path = request.getSession().getAttribute("URI_ACCESSED")
        if(path == null) {
            path = "/"
        }
        redirect uri: path*/
        done();
    }
}
