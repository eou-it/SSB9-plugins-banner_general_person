/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.*

@Entity
@Table(name = "GORVISA")
@ToString
@EqualsAndHashCode
@NamedQueries(
        [
                @NamedQuery(name = "VisaInformation.fetchAllByPidmInList",
                        query = """ FROM VisaInformation where pidm in (:pidmList)""")
        ]
)

class VisaInformation implements Serializable{

    /**
     * SURROGATE ID: Immutable unique key
     */
    @Id
    @Column(name = "GORVISA_SURROGATE_ID")
    @SequenceGenerator(name = "GORVISA_SEQ_GEN", allocationSize = 1, sequenceName = "GORVISA_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GORVISA_SEQ_GEN")
    Long id

    /**
     * VERSION: Optimistic lock token
     */
    @Version
    @Column(name = "GORVISA_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * VISA TYPE CODE: The Type of Visa Issued
     */
    @Column(name = "GORVISA_VTYP_CODE")
    String visaTypeCode

    /**
     * VISA ISSUE DATE: The date the Visa was Issued
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GORVISA_VISA_ISSUE_DATE")
    Date visaIssueDate

    /**
     * VISA EXPIRE DATE: The date the visa expires
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GORVISA_VISA_EXPIRE_DATE")
    Date visaExpireDate

    /**
     * PIDM:  Internal Identification number
     */
    @Column(name = "GORVISA_PIDM")
    Long pidm

    /**
     * SEQUENCE NUMBER: A Record Sequence Number
     */
    @Column(name = "GORVISA_SEQ_NO")
    Long seqNumber

    /**
     * ENTRY INDICATOR: This field indicates whether the visa is for entry into the country
     */
    @Column(name = "GORVISA_ENTRY_IND")
    String entryInd

    /**
     * ACTIVITY DATE:  Date of last activity (insert or update) on the record
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GORVISA_ACTIVITY_DATE")
    Date lastModified

    /**
     * USER ID: The Oracle ID of the user who changed the record
     */
    @Column(name = "GORVISA_USER_ID")
    String lastModifiedBy

    /**
     * DATA ORIGIN: Source system that created or updated the row
     */
    @Column(name = "GORVISA_DATA_ORIGIN")
    String dataOrigin

    static constraints = {
        visaTypeCode(nullable: false, blank: false, maxSize: 2)
        visaIssueDate(nullable: false)
        visaExpireDate(nullable: false)
        pidm(nullable: false, maxSize: 8)
        seqNumber(nullable: false, maxSize: 3)
        entryInd(nullable: false, maxSize: 1)
        lastModified(nullable: false)
        lastModifiedBy(nullable: false, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    public static List<VisaInformation> fetchAllByPidmInList(List<String> pidmList) {
        List<VisaInformation> visaList = []
        if (pidmList) {
            VisaInformation.withSession { session ->
                visaList = session.getNamedQuery("VisaInformation.fetchAllByPidmInList").setParameterList("pidmList", pidmList).list()
            }
        }
        return visaList
    }
}
