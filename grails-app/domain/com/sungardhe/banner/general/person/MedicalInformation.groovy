/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.person

import com.sungardhe.banner.general.system.Disability
import com.sungardhe.banner.general.system.DisabilityAssistance
import com.sungardhe.banner.general.system.MedicalCondition
import com.sungardhe.banner.general.system.MedicalEquipment
import com.sungardhe.banner.service.DatabaseModifiesState
import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Represents student medical information.  May be more than one per student.
 */
@Entity
@Table (name = "SV_SPRMEDI")
@DatabaseModifiesState 
class MedicalInformation implements Serializable {
        

    @Id
    @SequenceGenerator(name = "SPRMEDI_SEQ_GEN", allocationSize = 1, sequenceName = "SPRMEDI_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPRMEDI_SEQ_GEN")
    @Column(name = "SPRMEDI_SURROGATE_ID")
    Long id

    @Column (name="SPRMEDI_ACTIVITY_DATE" )
    Date lastModified

    @Column (name="SPRMEDI_USER_ID" )
    String lastModifiedBy

    @Column (name="SPRMEDI_DATA_ORIGIN" )
    String dataOrigin

  	@Version
	@Column(name="SPRMEDI_VERSION", nullable = false, length=19)
	Long version 

    @Column (name="SPRMEDI_PIDM",  nullable = false)
    Integer pidm

    @Column (name = "SPRMEDI_COMMENT")
    String comment

    @Column(name="SPRMEDI_ONSET_AGE")
    Integer onsetAge

    @Type (type = "yes_no")
    @Column (name = "SPRMEDI_DISB_IND")
    Boolean disabilityIndicator

    @Column(name="SPRMEDI_MEDI_CODE_DATE") 
    Date medicalDate

    @ManyToOne
    @JoinColumns ([@JoinColumn (name = "SPRMEDI_DISA_CODE", referencedColumnName = "STVDISA_CODE")])
    Disability disability

    @ManyToOne
    @JoinColumns ([@JoinColumn (name = "SPRMEDI_MEDI_CODE", referencedColumnName = "STVMEDI_CODE")])
    MedicalCondition medicalCondition

    @ManyToOne
    @JoinColumns ([@JoinColumn (name = "SPRMEDI_MDEQ_CODE", referencedColumnName = "STVMDEQ_CODE")])
    MedicalEquipment medicalEquipment

    @ManyToOne
    @JoinColumns ([@JoinColumn (name = "SPRMEDI_SPSR_CODE", referencedColumnName = "STVSPSR_CODE")])
    DisabilityAssistance disabilityAssistance

    public static readonlyProperties = [ 'pidm' , 'medicalCondition' ]


    static constraints = {
        disabilityIndicator(   maxSixe:10)
        medicalCondition(      maxSixe:3)
        disability(            nullable: true , maxSixe:2 )
        medicalEquipment(      nullable: true , maxSixe:3 )
        disabilityAssistance(  nullable: true , maxSixe:2 )
        medicalDate(          nullable: true  )
        onsetAge(              nullable: true  )
        comment(               nullable: true, maxSixe:4000  )
        
        dataOrigin(            nullable: true )
        lastModified(          nullable: true )
        lastModifiedBy(        nullable: true )
    }

    public String toString() {
        """MedicalInformation[id=$id,pidm=$pidm,
           disability Indicator:$disabilityIndicator,
           medical Date: $medicalDate,
           medicalCondition:$medicalCondition,
           medicalEquipment:$medicalEquipment,
           disabilityAssistance: $disabilityAssistance,
           disability: $disability,
           comment=$comment,
           onsetAge=$onsetAge ,
           version: $version,  data origin: $dataOrigin,  last modified by: $lastModifiedBy}"""
    }


    boolean equals(o) {
        if (this.is(o)) return true;

        if (getClass() != o.class) return false;

        MedicalInformation that = (MedicalInformation) o;

        if (comment != that.comment) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (disability != that.disability) return false;
        if (disabilityAssistance != that.disabilityAssistance) return false;
        if (disabilityIndicator != that.disabilityIndicator) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (medicalCondition != that.medicalCondition) return false;
        if (medicalDate != that.medicalDate) return false;
        if (medicalEquipment != that.medicalEquipment) return false;
        if (onsetAge != that.onsetAge) return false;
        if (pidm != that.pidm) return false;
        if (version != that.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (pidm != null ? pidm.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (onsetAge != null ? onsetAge.hashCode() : 0);
        result = 31 * result + (disabilityIndicator != null ? disabilityIndicator.hashCode() : 0);
        result = 31 * result + (medicalDate != null ? medicalDate.hashCode() : 0);
        result = 31 * result + (disability != null ? disability.hashCode() : 0);
        result = 31 * result + (medicalCondition != null ? medicalCondition.hashCode() : 0);
        result = 31 * result + (medicalEquipment != null ? medicalEquipment.hashCode() : 0);
        result = 31 * result + (disabilityAssistance != null ? disabilityAssistance.hashCode() : 0);
        return result;
    }
}
