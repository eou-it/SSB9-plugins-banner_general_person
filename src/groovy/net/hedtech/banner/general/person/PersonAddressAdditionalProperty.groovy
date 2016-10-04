/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.person

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import javax.persistence.*

@Entity
@Table(name = "SPRADDR")
@ToString
@EqualsAndHashCode
@NamedQueries(value = [
        @NamedQuery(name = "PersonAddressAdditionalProperty.fetchAllBySurrogateIds",
                query = """FROM PersonAddressAdditionalProperty where id in (:surrogateIds) """)
])
class PersonAddressAdditionalProperty implements Serializable {

    /**
     * Surrogate ID for SPRADDR
     */
    @Id
    @Column(name = "SPRADDR_SURROGATE_ID")
    @SequenceGenerator(name = "SPRADDR_SEQ_GEN", allocationSize = 1, sequenceName = "SPRADDR_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPRADDR_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for SPRADDR
     */
    @Version
    @Column(name = "SPRADDR_VERSION")
    Long version

    /**
     * Global Unique Identifier for SPRADDR
     */
    @Column(name = "SPRADDR_GUID")
    String addressGuid

    /**
     * ISO county Code
     */
    @Column(name = "SPRADDR_CNTY_CODE_ISO")
    String countyISOCode

    /**
     * county title
     */
    @Column(name = "SPRADDR_CNTY_TITLE")
    String countyDescription

    static constraints = {
        addressGuid(nullable: true, maxSize: 36)
        countyISOCode(nullable: true, maxSize: 8)
        countyDescription(nullable:true, maxSize: 30)
    }
}
