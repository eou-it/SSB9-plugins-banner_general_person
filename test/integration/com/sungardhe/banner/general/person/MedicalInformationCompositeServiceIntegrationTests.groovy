/** *****************************************************************************

 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */

package com.sungardhe.banner.general.person

import com.sungardhe.banner.exceptions.ApplicationException
import com.sungardhe.banner.general.system.Disability
import com.sungardhe.banner.general.system.DisabilityAssistance
import com.sungardhe.banner.general.system.MedicalCondition
import com.sungardhe.banner.general.system.MedicalEquipment
import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.junit.Ignore

class MedicalInformationCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def medicalInformationService
    def medicalInformationCompositeService


    protected void setUp() {
        formContext = ['GOAMEDI']// Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreate() {

        def medicalInformations = newMedicalInformation()
        def pidm = medicalInformations[0].pidm
        def keyBlock = [pidm: pidm]
        medicalInformationCompositeService.createOrUpdate([medicalInformations: medicalInformations, keyBlock: keyBlock])
        def cntMedical = MedicalInformation.findAllByPidm(pidm)
        assertEquals cntMedical.size(), 2

    }


    void testMedicalInformationDelete() {
        def medicalInformations = newMedicalInformation()
        def pidm = medicalInformations[0].pidm
        def keyBlock = [pidm: pidm]
        medicalInformationCompositeService.createOrUpdate([medicalInformations: medicalInformations, keyBlock: keyBlock])
        def deleteMedical = MedicalInformation.findAllByPidm(pidm)
        assertEquals deleteMedical.size(), 2

        medicalInformationCompositeService.createOrUpdate([deleteMedicalInformations: deleteMedical, keyBlock: keyBlock])
        deleteMedical = MedicalInformation.findAllByPidm(pidm)
        assertEquals deleteMedical.size(), 0

    }


    void testUpdateOfComment() {

        def medicalInformation = newSingleMedicalInformation()
        medicalInformation = medicalInformationService.create([domainModel: medicalInformation])
        def pidm = medicalInformation.pidm
        assertNotNull medicalInformation.id

        medicalInformation.comment = "Test Update"
        def medicalInformations = []
        medicalInformations << medicalInformation
        def keyBlock = [pidm: pidm]
        medicalInformationCompositeService.createOrUpdate([medicalInformations: medicalInformations, keyBlock: keyBlock])
        def cntMedical = MedicalInformation.findByPidm(pidm)
        assertEquals cntMedical.comment, "Test Update"

    }

    @Ignore
    // TODO for some reason this fails,  the medical condition is not flagged as being dirty,  i suspect that a domain
    // exists that has incorrect equals and hash causing this
    void testMediCodeUpdateNoPereacc() {
        setHRInstalledFalse()
        // create new medical information record
        def medicalInformation = newSingleMedicalInformation()
        medicalInformation = medicalInformationService.create([domainModel: medicalInformation])

        assertNotNull "Medical Information ID should not be null", medicalInformation.id
        def saveID = medicalInformation.id
        def pidm = medicalInformation.pidm

        assertEquals "Original medical cond must be ZZ", "ZZ", medicalInformation.medicalCondition.code

        // update it to have comment change and medical condition change
        MedicalInformation medicalUpdate = MedicalInformation.get(saveID)
        println "Medical condition before update: ${medicalUpdate.medicalCondition} ${medicalUpdate} isdirty: ${medicalUpdate.isDirty()}"
        def newMedCond = MedicalCondition.findByCode("PP")
        println "Update with this: ${newMedCond}"
        medicalUpdate.medicalCondition = newMedCond
        medicalUpdate.medicalEquipment = createMedicalEquipment("X1")

        def changedNames = medicalUpdate.dirtyPropertyNames
        println "Inside test, changed names: ${changedNames} ${medicalUpdate}"
       // medicalUpdate.comment = "MH test with condition C1"
        def medicalInformations = []
        medicalInformations << medicalUpdate
        def map = [medicalInformations: medicalInformations, keyBlock: [pidm: pidm]]

        changedNames = medicalUpdate.dirtyPropertyNames

        if (changedNames.size() > 0) {
            println "changed items: ${changedNames}"
        }
        else println "No changed items in medicalUpdate"
        medicalInformationCompositeService.createOrUpdate(map)

        // verify that the original is deleted and that the ID is not the same on the new record
        def delMedicalInfo = MedicalInformation.findByPidm(pidm)
        assertFalse(delMedicalInfo.id == saveID)
        assertEquals "Medical Condition was not updated to C1 ", "C1", delMedicalInfo.medicalCondition.code
        assertEquals "Comment should be MH", "MH test", delMedicalInfo.comment
        assertEquals "Medical Equip should be X1", "X1", delMedicalInfo.medicalEquipment.code

    }

    //PEREACC_PIDM PEREACC_POSN             PEREACC_SUFF PEREACC_REQUEST_DATE PEREACC_RQST_CODE PEREACC_MEDI_CODE                        PEREACC_VERIFY_IND PEREACC_MDEQ_CODE PEREACC_USER_ID                                                                  PEREACC_ACTIVITY_DATE PEREACC_ACST_CODE PEREACC_DISA_CODE PEREACC_BEGIN_DATE PEREACC_END_DATE PEREACC_COMMENT
    //        1728 F10000                   00           4/22/2003            ACTV              HC


    void testMediCodeUpdateWithPereacc() {
        setHRInstalledTrue()
        // create new medical information record
        def medicalInformation = newSingleMedicalInformation()
        medicalInformation = medicalInformationService.create([domainModel: medicalInformation])
        assertEquals medicalInformation.medicalCondition.code, "ZZ"
        // create the pereacc record
        String insertPereacc = "insert into pereacc (pereacc_pidm, pereacc_posn, pereacc_suff, pereacc_request_date," +
                " pereacc_rqst_code, pereacc_medi_code, pereacc_verify_ind, pereacc_user_id,   pereacc_activity_date," +
                "  pereacc_mdeq_code) values (${medicalInformation.pidm}, 'F10000', '00', sysdate, 'ACTV', 'ZZ', 'Y', 'grails',sysdate, 'zz')"
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def rowsIns = sql.executeUpdate(insertPereacc)
        assertEquals "One record should have been inserted", 1, rowsIns

        // save the medical condition
        MedicalInformation oldMedicalCondition = medicalInformation
        assertNotNull "Medical Information ID should not be null", medicalInformation.id
        MedicalInformation medicalInformationUpdate = MedicalInformation.get(medicalInformation.id)
        assertNotNull "Medical Information ID is null in Test Update", medicalInformationUpdate.id
        medicalInformationUpdate.medicalCondition = createMedicalCondition("Z#")
        medicalInformationUpdate.comment = "MH test"
        assertEquals "New medical cond must be Z#", "Z#", medicalInformationUpdate.medicalCondition.code

        // update it to have comment change and medical condition change
        def medicalInformations = []
        medicalInformations << medicalInformationUpdate
        def map = [medicalInformations: medicalInformations, keyBlock: [pidm: medicalInformationUpdate.pidm]]

        try {
            medicalInformationCompositeService.createOrUpdate(map)
            fail("I should have received an error but it passed; @@r1:cannotChangeEmployeeMedical@@ ")
        }
        catch (ApplicationException ae) {
            if (ae.wrappedException =~ /@@r1:cannotChangeEmployeeMedical@@/) {
                // println "Found correct message code @@r1:cannotChangeEmployeeMedical@@"
            }
            else
                fail("Did not find expected error code @@r1:cannotChangeEmployeeMedical@@, found: ${ae.wrappedException}")
        }
        catch (e) {
            fail "This should have failed with @@r1:cannotChangeEmployeeMedical@@ but found exception: ${e} "
        }
    }


    void testMediCodeUpdateWithPereaccWithoutMap() {
        setHRInstalledTrue()
        // create new medical information record
        def medicalInformation = newSingleMedicalInformation()
        medicalInformation = medicalInformationService.create([domainModel: medicalInformation])

        // create the pereacc record
        String insertPereacc = "insert into pereacc (pereacc_pidm, pereacc_posn, pereacc_suff, pereacc_request_date," +
                " pereacc_rqst_code, pereacc_medi_code, pereacc_verify_ind, pereacc_user_id,   pereacc_activity_date, " +
                " pereacc_mdeq_code) values (   " +
                "  ${medicalInformation.pidm}, 'F10000', '00', sysdate, 'ACTV', 'ZZ', 'Y', 'grails',sysdate, 'zz')"
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def rowsIns = sql.executeUpdate(insertPereacc)
        assertEquals "One record should have been inserted", 1, rowsIns


        assertNotNull "Medical Information ID should not be null", medicalInformation.id
        //  assertEquals "Version should be 0", 0, medicalInformation.version
        MedicalInformation medicalInformationUpdate = medicalInformation.get(
                medicalInformation.id)
        assertNotNull "Medical Information ID is null in Test Update", medicalInformationUpdate.id
        assertEquals "Original medical cond must be ZZ", "ZZ", medicalInformationUpdate.medicalCondition.code

        // update it to have comment change and medical condition change

        medicalInformationUpdate.medicalCondition = createMedicalCondition("Z#")
        medicalInformationUpdate.comment = "MH test"
        def medicalInformations = []
        medicalInformations << medicalInformationUpdate
        def map = [medicalInformations: medicalInformations, keyBlock: [pidm: medicalInformationUpdate.pidm]]

        try {
            medicalInformationCompositeService.createOrUpdate(map)
            fail("I should have received an error but it passed; @@r1:cannotChangeEmployeeMedical@@ ")
        }
        catch (ApplicationException ae) {
            if (ae.wrappedException =~ /@@r1:cannotChangeEmployeeMedical@@/) {
                // println "Found correct message code @@r1:cannotChangeEmployeeMedical@@"
            }
            else
                fail("Did not find expected error code @@r1:cannotChangeEmployeeMedical@@, found: ${ae.wrappedException}")
        }
        catch (e) {
            fail "This should have failed with @@r1:cannotChangeEmployeeMedical@@ but found exception: ${e} "
        }
    }


    private def newSingleMedicalInformation() {
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
        def bannerValues = sql.firstRow(idSql)
        def ibannerId = bannerValues.bannerId
        def ipidm = bannerValues.pidm

        def person = new PersonIdentificationName(
                pidm: ipidm,
                bannerId: ibannerId,
                lastName: "TTTTT",
                firstName: "TTTTT",
                middleName: "TTTTT",
                changeIndicator: null,
                entityIndicator: "P"
        )
        person.save(flush: true)
        assert person.id
        def medicalInformation = new MedicalInformation(disability: createDisability(),
                                                        disabilityAssistance: createDisabilityAssistance(),
                                                        medicalCondition: createMedicalCondition(),
                                                        medicalEquipment: createMedicalEquipment(),
                                                        pidm: person.pidm,
                                                        disabilityIndicator: true,
                                                        comment: "unit-test",
                                                        medicalDate: new Date(),
                                                        onsetAge: 29)
        return medicalInformation
    }


    private List newMedicalInformation() {
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        String idSql = """select gb_common.f_generate_id bannerId, gb_common.f_generate_pidm pidm from dual """
        def bannerValues = sql.firstRow(idSql)
        def ibannerId = bannerValues.bannerId
        def ipidm = bannerValues.pidm

        def person = new PersonIdentificationName(
                pidm: ipidm,
                bannerId: ibannerId,
                lastName: "TTTTT",
                firstName: "TTTTT",
                middleName: "TTTTT",
                changeIndicator: null,
                entityIndicator: "P"
        )
        person.save(flush: true)
        assert person.id
        def medicalInformations = []
        def medicalInformation = new MedicalInformation(disability: createDisability(),
                                                        disabilityAssistance: createDisabilityAssistance(),
                                                        medicalCondition: createMedicalCondition(),
                                                        medicalEquipment: createMedicalEquipment(),
                                                        pidm: person.pidm,
                                                        disabilityIndicator: true,
                                                        comment: "unit-test",
                                                        medicalDate: new Date(),
                                                        onsetAge: 29)
        medicalInformations << medicalInformation

        def medicalInformation2 = new MedicalInformation(disability: createDisability("%2"),
                                                         disabilityAssistance: createDisabilityAssistance("%2"),
                                                         medicalCondition: createMedicalCondition("!@"),
                                                         medicalEquipment: createMedicalEquipment("!@"),
                                                         pidm: person.pidm,
                                                         disabilityIndicator: false,
                                                         comment: "unit-test2",
                                                         medicalDate: new Date(),
                                                         onsetAge: 29)
        medicalInformations << medicalInformation2
        return medicalInformations
    }


    private Disability createDisability(String disCode) {
        if (!disCode) disCode = "%%"

        save(new Disability(code: disCode, description: "unit-test", lastModified: new Date(),
                            lastModifiedBy: "test", dataOrigin: "banner"))
    }


    private DisabilityAssistance createDisabilityAssistance(String disAss) {

        if (!disAss) disAss = "%%"
        save(new DisabilityAssistance(code: disAss, description: "unit-test", lastModified: new Date(),
                                      lastModifiedBy: "test", dataOrigin: "Banner"))
    }


    private MedicalCondition createMedicalCondition(String mediCond) {
        if (!mediCond) mediCond = "ZZ"
        save(new MedicalCondition(code: mediCond, description: "unit-test", lastModified: new Date(),
                                  lastModifiedBy: "test", dataOrigin: "Banner"))
    }


    private MedicalEquipment createMedicalEquipment(String medEq) {
        if (!medEq) medEq = "zz"
        save(new MedicalEquipment(code: medEq, description: "unit-test", lastModified: new Date(),
                                  lastModifiedBy: "test", dataOrigin: "Banner"))
    }


    private def setHRInstalledTrue() {
        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            def rowCount = sql.executeUpdate("update gubinst set GUBINST_HUMANRE_INSTALLED = 'Y' where GUBINST_KEY = 'INST'")
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
    }


    private def setHRInstalledFalse() {
        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            def rowCount = sql.executeUpdate("update gubinst set GUBINST_HUMANRE_INSTALLED = 'N' where GUBINST_KEY = 'INST'")
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
    }
}