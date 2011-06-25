/** *****************************************************************************

 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */

package com.sungardhe.banner.general.person

import com.sungardhe.banner.general.system.NameType
import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.exceptions.ApplicationException



class PersonIdentificationNameServiceIntegrationTests extends BaseIntegrationTestCase {

    def personIdentificationNameService


    protected void setUp() {
        formContext = ['SPAIDEN']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreatePersonIdentificationName() {
        def personIdentificationName = newPersonIdentificationName()
        personIdentificationName = personIdentificationNameService.create([domainModel: personIdentificationName])
        assertNotNull "PersonIdentificationName ID is null in PersonIdentificationName Service Tests Create", personIdentificationName.id
        assertNotNull personIdentificationName.dataOrigin
        assertNotNull personIdentificationName.lastModified
        assertNotNull "PersonIdentificationName nameType is null in PersonIdentificationName Service Tests", personIdentificationName.nameType
        assertNotNull "PersonIdentificationName pidm is null in PersonIdentificationName Service Tests", personIdentificationName.pidm
        assertNotNull "PersonIdentificationName banner id is null in PersonIdentificationName Service Tests", personIdentificationName.bannerId

    }


    void testPersonIdentificationNameDeleteWithNullChangeIndicator() {
        def personIdentificationName = newPersonIdentificationName()
        personIdentificationName = personIdentificationNameService.create([domainModel: personIdentificationName])
        // cannot delete an ID
        def id = personIdentificationName.id
        try {
            personIdentificationNameService.delete(id)
        }
        catch (ApplicationException ae) {
            assertNotNull ae.wrappedException
        }

    }


    void testPersonIdentificationNameDelete() {
        def personIdentificationName = newPersonIdentificationName()
        personIdentificationName = personIdentificationNameService.create([domainModel: personIdentificationName])
        assertNotNull personIdentificationName.id

        def newPersonIdentificationName = new PersonIdentificationName(personIdentificationName.properties)
        newPersonIdentificationName.changeIndicator = "N"
        newPersonIdentificationName.lastName = 'YYYYY'
        newPersonIdentificationName = personIdentificationNameService.create([domainModel: newPersonIdentificationName])
        assertNotNull newPersonIdentificationName.id

        def id = newPersonIdentificationName.id
        try {
            personIdentificationNameService.delete(id)
        }
        catch (ApplicationException ae) {
           assertNotNull ae.wrappedException
        }

    }


    void testPersonIdentificationEntityCheck() {
        def personIdentificationName = newPersonIdentificationName()
        personIdentificationName = personIdentificationNameService.create([domainModel: personIdentificationName])
        assertNotNull personIdentificationName.id
        assertEquals('P', personIdentificationNameService.fetchEntityOfPerson(personIdentificationName.pidm))
        def companyIdentificationName = newCompanyIdentificationName()
        companyIdentificationName = personIdentificationNameService.create(companyIdentificationName)
        assertNotNull companyIdentificationName.id
        assertEquals('C', personIdentificationNameService.fetchEntityOfPerson(companyIdentificationName.pidm))
    }


    void testSystemGeneratedIdPrefix() {
        assertEquals('A', personIdentificationNameService.getSystemGeneratedIdPrefix())
    }


    private def newPersonIdentificationName() {
        def inameType = NameType.findWhere(code: "PROF")
        // leave the pidm null to get a generated on, and the ID equal to GENERATED to get a newly
        // generated ID
        def personIdentificationName = new PersonIdentificationName(
                pidm: null,
                bannerId: 'GENERATED',
                lastName: "TTTTT",
                firstName: "TTTTT",
                middleName: "TTTTT",
                changeIndicator: null,
                entityIndicator: "P",
                nameType: inameType
        )
        return personIdentificationName
    }


    private def newCompanyIdentificationName() {
        def inameType = NameType.findWhere(code: "PROF")
        // leave the pidm null to get a generated on, and the ID equal to GENERATED to get a newly
        // generated ID
        def personIdentificationName = new PersonIdentificationName(
                pidm: null,
                bannerId: 'GENERATED',
                lastName: "TTTTT",
                changeIndicator: null,
                entityIndicator: "C",
                nameType: inameType
        )
        return personIdentificationName
    }

    /**
     * Please put all the custom service tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personidentificationname_custom_service_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
