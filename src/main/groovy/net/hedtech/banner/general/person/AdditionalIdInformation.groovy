/*******************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.person

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import javax.persistence.*

@Entity
@Table(name = "GORADID")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
@NamedQueries(value = [
        @NamedQuery(name = "AdditionalIdInformation.fetchExistingByPidm",
                query = """ SELECT  a.additionalId FROM AdditionalIdInformation a
		           WHERE a.pidm = :pidm 
		           AND a.adidCode = :adidCode
                  """),
        @NamedQuery(name = "AdditionalIdInformation.fetchExistingFullRecordByPidm",
                query = """  FROM AdditionalIdInformation a
		           WHERE a.pidm = :pidm 
		           AND a.adidCode = :adidCode
                   """)
])

class AdditionalIdInformation implements Serializable{

    @Id
    @Column(name = "GORADID_SURROGATE_ID")
    @SequenceGenerator(name = "GORADID_SEQ_GEN", allocationSize = 1, sequenceName = "GORADID_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GORADID_SEQ_GEN")
    Long surrogateId

    @Column(name = "GORADID_ACTIVITY_DATE")
    Date lastModified

    @Column(name = "GORADID_USER_ID")
    String lastModifiedBy

    @Column(name = "GORADID_DATA_ORIGIN")
    String dataOrigin

    @Column(name = "GORADID_VERSION")
    Long version

    @Column(name = "GORADID_PIDM")
    Integer pidm

    @Column(name = "GORADID_ADID_CODE")
    String adidCode

    @Column(name = "GORADID_ADDITIONAL_ID")
    String additionalId



    static def fetchExistingByPidm(Integer pidm,String adidCode) {

        def additionalIdInfo = AdditionalIdInformation.withSession { session ->
            session.getNamedQuery('AdditionalIdInformation.fetchExistingByPidm')
                    .setInteger('pidm', pidm)
                    .setString('adidCode', adidCode)
                    .uniqueResult()
        }
        return additionalIdInfo
    }

    static def fetchExistingFullRecordByPidm(Integer pidm,String adidCode) {
        def recordToUpdate = AdditionalIdInformation.withSession { session ->
            session.getNamedQuery('AdditionalIdInformation.fetchExistingFullRecordByPidm')
                    .setInteger('pidm', pidm)
                    .setString('adidCode', adidCode)
                    .uniqueResult()
        }
        recordToUpdate
    }

}
