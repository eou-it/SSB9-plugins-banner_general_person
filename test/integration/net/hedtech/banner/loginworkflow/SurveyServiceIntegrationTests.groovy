/** *****************************************************************************
 © 2013 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package net.hedtech.banner.loginworkflow

import net.hedtech.banner.general.person.PersonIdentificationName
import net.hedtech.banner.general.person.PersonRace
import net.hedtech.banner.testing.BaseIntegrationTestCase
import net.hedtech.banner.exceptions.ApplicationException

/**
 * SurveyServiceIntegrationTests.
 *
 * Date: 9/17/13
 * Time: 2:30 PM
 */
class SurveyServiceIntegrationTests extends BaseIntegrationTestCase {
    SurveyService surveyService

    String i_success_ethnicity="1"
    Integer i_success_pidm
    String i_success_race="MOA"

    String u_success_ethnicity= "2"
    Integer u_success_pidm
    String u_success_race="MOA"

    String i_failure_ethnicity="02"
    Integer i_failure_pidm=01L
    String i_failure_race="MOAN"

    String u_failure_ethnicity="23"
    Integer u_failure_pidm=01L
    String u_failure_race="MOAN"

    protected void setUp() {
            formContext = ['GUAGMNU']
            super.setUp()
            initializeTestDataForReferences()
        }

    protected void tearDown() {
           super.tearDown()
       }

    void initializeTestDataForReferences() {
           u_success_pidm = i_success_pidm = PersonIdentificationName.findByBannerId("HOF00714").pidm

       }

    void testSaveSurveyResponseValid() {

        PersonRace quiredPersonRace = PersonRace.fetchByPidmAndRace(i_success_pidm, i_success_race)
        assertNull quiredPersonRace

        surveyService.saveSurveyResponse(i_success_pidm, i_success_ethnicity, i_success_race)

        quiredPersonRace = PersonRace.fetchByPidmAndRace(i_success_pidm, i_success_race)
        assertNotNull quiredPersonRace

        quiredPersonRace.delete()

        quiredPersonRace = PersonRace.fetchByPidmAndRace(i_success_pidm, i_success_race)
        assertNull quiredPersonRace

    }

    void testSaveSurveyResponseInvalid() {

        PersonRace quiredPersonRace = PersonRace.fetchByPidmAndRace(i_success_pidm, i_success_race)
        assertNull quiredPersonRace

        surveyService.saveSurveyResponse(i_success_pidm, null, i_success_race)

        quiredPersonRace = PersonRace.fetchByPidmAndRace(i_success_pidm, i_success_race)
        assertNotNull quiredPersonRace

        surveyService.saveSurveyResponse(i_success_pidm, i_success_ethnicity, null)

        quiredPersonRace = PersonRace.fetchByPidmAndRace(i_success_pidm, null)
        assertNull quiredPersonRace

        surveyService.saveSurveyResponse(i_success_pidm, null, null)

        quiredPersonRace = PersonRace.fetchByPidmAndRace(i_failure_pidm, null)
        assertNull quiredPersonRace

        shouldFail(ApplicationException) {
            surveyService.saveSurveyResponse(i_success_pidm, i_success_ethnicity, i_failure_race)
        }

        shouldFail(ApplicationException) {
            surveyService.saveSurveyResponse(i_success_pidm, i_failure_ethnicity, i_success_race)
        }
        shouldFail(ApplicationException) {
            surveyService.saveSurveyResponse(i_success_pidm, i_failure_ethnicity, i_failure_race)
        }
        shouldFail(ApplicationException) {
            surveyService.saveSurveyResponse(i_failure_pidm, i_success_ethnicity, i_success_race)
        }

        shouldFail(ApplicationException) {
            surveyService.saveSurveyResponse(i_failure_pidm, i_failure_ethnicity, i_success_race)
        }
        shouldFail(ApplicationException) {
            surveyService.saveSurveyResponse(i_failure_pidm, i_success_ethnicity, i_failure_race)
        }

    }
}
