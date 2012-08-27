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
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

/**
 * Integration tests for the <code>MedicalInformation</code> model.
 */
class MedicalInformationIntegrationTests extends BaseIntegrationTestCase {

    def medicalInformationService       // injected via spring


    protected void setUp() {
        formContext = ['GOAMEDI'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    void testCreate() {
        def entity = newMedicalInformation()
        save entity
        assertNotNull entity.id
    }


    void testUpdate() {
        def entity = newMedicalInformation()
        save entity

        assertNotNull entity.id
        assertEquals(new Long(0), entity.version)
        assertTrue(entity.disabilityIndicator)

        entity.disabilityIndicator = false
        save entity

        def updated = MedicalInformation.get(entity.id)
        assertEquals new Long(1), updated.version
        assertFalse updated.disabilityIndicator
    }


    void testOptimisticLock() {
        def entity = newMedicalInformation()
        save entity
        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SV_SPRMEDI set sprmedi_version = 999 where sprmedi_surrogate_id = ${entity.id}")
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        entity.comment = "This better fail ;-)"
        shouldFail(HibernateOptimisticLockingFailureException) {
            entity.save(flush:true, failOnError:true)
        }
    }


    void testDelete() {
        def entity = newMedicalInformation()
        save entity
        def id = entity.id
        entity.delete()
        assertNull MedicalInformation.get(id)
    }


    void testList() {
        def medInfos = MedicalInformation.list()
        assertTrue medInfos.size() > 0
    }


    void testFindWhere() {
        def entity = MedicalInformation.findWhere(onsetAge: 12)
        assertEquals "FindWhere did not return expected entity", 12, entity.onsetAge
    }


    void testFindAll() {
        def medInfos = MedicalInformation.findAll()
        assertTrue medInfos.size() >= 10
    }


    void testFindAllWithHQL() {
        String hql = "from MedicalInformation as m where m.onsetAge != null"
        def medInfos = MedicalInformation.findAll(hql)
        assertTrue medInfos.size() >= 10
    }


    void testFindByOnsetAge() {
        def entity = newMedicalInformation()
        save entity
        def medInfos = MedicalInformation.findAllByOnsetAge(29)
        assertTrue medInfos.size() > 0
        assert medInfos.any { it.pidm == entity.pidm }
    }



    void testAssociations() {
        def entity = newMedicalInformation()
        save entity
        def medEquipId = entity.medicalEquipment.id
        assertNotNull "Could not save medical information due to: ${entity.errors}", entity.save(flush:true, failOnError:true)

        def found = MedicalInformation.get(entity.id)
        assertEquals medEquipId, found.medicalEquipment.id
    }


    void testValidation() {
        def medicalInformation = newMedicalInformation()
        medicalInformation.pidm = null
        assertFalse "Medical information should have failed validation", medicalInformation.validate()

        medicalInformation.pidm = 99
        assertTrue "Could not validate medical information due to: ${medicalInformation.errors}", medicalInformation.validate()
    }


    void testCreateWithAPIError() {
        def entity = newMedicalInformation()
        assertNotNull entity.id

        def newMediInfo = new MedicalInformation(disability: createDisability("u2"),
                disabilityAssistance: createDisabilityAssistance("U2"),
                medicalCondition: createMedicalCondition("U2"),
                medicalEquipment: createMedicalEquipment("U2"),
                pidm: entity.pidm,
                disabilityIndicator: true,
                comment: "unit-test",
                medicalDate: new Date(),
                onsetAge: 29,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")

        try {
            newMediInfo.save(flush:true, failOnError:true)
            assertNotNull "Medical should not have been saved", newMediInfo.id
        }
        catch (e) {
            def ae = new ApplicationException(newMediInfo, e)
            if (ae.wrappedException == null) // =~ /Only one disability may be designated as primary for Medical Information/ )
                fail("This should have ended with the exception Only one disability may be designated as primary for Medical Information but got: ${ae}")
        }
    }

// ------------------------------- Helper Methods --------------------------------


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
                onsetAge: 29,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")
        save medicalInformation
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