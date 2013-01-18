/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/

package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.Disability
import net.hedtech.banner.general.system.DisabilityAssistance
import net.hedtech.banner.general.system.MedicalCondition
import net.hedtech.banner.general.system.MedicalEquipment
import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql

class MedicalInformationServiceIntegrationTests extends BaseIntegrationTestCase {

    def medicalInformationService


    protected void setUp() {
        formContext = ['SOAIDEN']// Since we are not testing a controller, we need to explicitly set this  (removing GOAMEDI because of GUOBOBS_UI_VERSION = B)
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreate() {

        def medicalInformation = newMedicalInformation()

        medicalInformation = medicalInformationService.create([domainModel: medicalInformation])
        assertNotNull "Medical Information ID is null in Medical Information Service Tests", medicalInformation.id
        assertNotNull "Medical Information PIDM is null in Medical Information Service Tests", medicalInformation.pidm
        assertNotNull "Medical Information Term is null in Medical Information Service Tests", medicalInformation.medicalCondition
    }


    void testUpdate() {

        def medicalInformation = newMedicalInformation()
        medicalInformation = medicalInformationService.create([domainModel: medicalInformation])
        assertNotNull "Medical Information ID should not be null", medicalInformation.id
        assertEquals "unit-test", medicalInformation.comment
        def id = medicalInformation.id
        MedicalInformation medicalInformationUpdate = medicalInformation.get(id)
        assertNotNull "Medical Information ID is null in Test Update", medicalInformationUpdate.id
        medicalInformationUpdate.comment = "This is an update"
        def map = [domainModel: medicalInformationUpdate, oldMedicalCondition: medicalInformation]
        medicalInformationService.update(map)
        def medicalInformationUpdate2 = MedicalInformation.get(id)
        assertEquals "This is an update", medicalInformationUpdate2.comment
    }





    void testCreateWithAPIError() {
        def entity = newMedicalInformation()
        entity = medicalInformationService.create([domainModel: entity])
        assertNotNull entity.id

        def newMediInfo = new MedicalInformation(disability: createDisability("u2"),
                disabilityAssistance: createDisabilityAssistance("U2"),
                medicalCondition: createMedicalCondition("U2"),
                medicalEquipment: createMedicalEquipment("U2"),
                pidm: entity.pidm,
                disabilityIndicator: true,
                comment: "unit-test",
                medicalDate: new Date(),
                onsetAge: 29)

        try {
            newMediInfo = medicalInformationService.create([domainModel: newMediInfo])
            assertNotNull "Medical should not have been saved", newMediInfo.id
        }
        catch (e) {
            def ae = new ApplicationException(newMediInfo, e)
            if (ae.wrappedException == null) // =~ /Only one disability may be designated as primary for Medical Information/ )
                fail("This should have ended with the exception Only one disability may be designated as primary for Medical Information but got: ${ae}")
        }

    }


    void testUpdateWithAPIError() {
        def entity = newMedicalInformation()
        entity = medicalInformationService.create([domainModel: entity])
        assertNotNull entity.id

        def newMediInfo = new MedicalInformation(disability: createDisability("u2"),
                disabilityAssistance: createDisabilityAssistance("U2"),
                medicalCondition: createMedicalCondition("U2"),
                medicalEquipment: createMedicalEquipment("U2"),
                pidm: entity.pidm,
                disabilityIndicator: false,
                comment: "unit-test",
                medicalDate: new Date(),
                onsetAge: 29)

        newMediInfo = medicalInformationService.create([domainModel: newMediInfo])
        def updateMediInfo = MedicalInformation.get(newMediInfo.id)
        updateMediInfo.disabilityIndicator = true
        def map = [domainModel: updateMediInfo, oldMedicalCondition: newMediInfo.medicalCondition]
        try {
            updateMediInfo = medicalInformationService.update(map)
            assertNotNull "Medical should not have been saved upon update", updateMediInfo.id
        }
        catch (e) {
            def ae = new ApplicationException(updateMediInfo, e)
            if (ae.wrappedException == null) // =~ /Only one disability may be designated as primary for Medical Information/ )
                fail("This should have ended with the exception Only one disability may be designated as primary for Medical Information but got: ${ae}")
        }

    }


    void testReadOnly() {
        def medicalInfo = newMedicalInformation()
        medicalInfo = medicalInformationService.create([domainModel: medicalInfo])

        assertNotNull medicalInfo.id
        assertEquals 0L, medicalInfo.version

        //Update the entity readonly field
        def updatedMedicalInfo = MedicalInformation.get(medicalInfo.id)
        updatedMedicalInfo.medicalCondition = createMedicalCondition("C1")
        try {
            medicalInformationService.update([domainModel: updatedMedicalInfo])
            fail("This should have failed with @@r1:readonlyFieldsCannotBeModified")
        }
        catch (ApplicationException ae) {
            assertApplicationException ae, "readonlyFieldsCannotBeModified"
        }
    }


    void testMedicalInformationDelete() {
        def medicalInformation = newMedicalInformation()

        medicalInformation = medicalInformationService.create([domainModel: medicalInformation])

        MedicalInformation medicalInformationUpdate = medicalInformation.get(
                medicalInformation.id)
        medicalInformationService.delete(medicalInformationUpdate.id)

        assertNull "Medical Information should have been deleted", medicalInformation.get(medicalInformationUpdate.id)


    }


    public MedicalInformation newMedicalInformation() {
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
        person.save(flush:true, failOnError:true)
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


    private Disability createDisability(String disCode) {
        if (disCode == null) disCode = "uu"
        save(new Disability(code: disCode, description: "unit-test", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "banner"))
    }


    private DisabilityAssistance createDisabilityAssistance(String disAss) {
        if (disAss == null) disAss = "xx"
        save(new DisabilityAssistance(code: disAss, description: "unit-test", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner"))
    }


    private MedicalCondition createMedicalCondition(String mediCond) {
        if (mediCond == null) mediCond = "ZZ"
        save(new MedicalCondition(code: mediCond, description: "unit-test", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner"))
    }


    private MedicalEquipment createMedicalEquipment(String medEq) {
        if (medEq == null) medEq = "zz"
        save(new MedicalEquipment(code: medEq, description: "unit-test", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner"))
    }

}
