/*********************************************************************************
  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.person

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.Disability
import net.hedtech.banner.general.system.DisabilityAssistance
import net.hedtech.banner.general.system.MedicalCondition
import net.hedtech.banner.general.system.MedicalEquipment
import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql

@Integration
@Rollback
class MedicalInformationServiceIntegrationTests extends BaseIntegrationTestCase {

    def medicalInformationService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']// Since we are not testing a controller, we need to explicitly set this  (removing GOAMEDI because of GUOBOBS_UI_VERSION = B)
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCreate() {

        def medicalInformation = newMedicalInformation()

        medicalInformation = medicalInformationService.create([domainModel: medicalInformation])
        assertNotNull "Medical Information ID is null in Medical Information Service Tests", medicalInformation.id
        assertNotNull "Medical Information PIDM is null in Medical Information Service Tests", medicalInformation.pidm
        assertNotNull "Medical Information Term is null in Medical Information Service Tests", medicalInformation.medicalCondition
    }


    @Test
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


   @Test
   void testDisabilitySurvey() {
       def mi = newMedicalInformationForDisabSurvey('DN');
       mi = medicalInformationService.create([domainModel:mi])

       assertNotNull "Medical Information should be valued",mi.id
       assertEquals('DN',mi.disability.code)

       mi.disability = Disability.findByCode('DY')
       mi = medicalInformationService.update([domainModel:mi])
       assertEquals('DY', mi.disability.code)

       def updatedmi = medicalInformationService.get(mi.id)
       assertEquals(mi.pidm, updatedmi.pidm)
       assertEquals ('DY',updatedmi.disability.code)
   }


    @Test
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


    @Test
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


    @Test
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


    @Test
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

    public MedicalInformation newMedicalInformationForDisabSurvey(String disabilityCode) {
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

        def medicalInformation = new MedicalInformation(disability: Disability.findByCode(disabilityCode),
                disabilityAssistance: createDisabilityAssistance(),
                medicalCondition: MedicalCondition.findByCode('DISABSURV'),
                medicalEquipment: createMedicalEquipment(),
                pidm: person.pidm,
                disabilityIndicator: true,
                comment: "unit-test",
                medicalDate: new Date(),
                onsetAge: 29,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")
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
