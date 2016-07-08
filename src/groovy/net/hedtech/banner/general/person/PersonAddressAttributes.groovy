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
class PersonAddressAttributes implements Serializable{

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
    @Column(name= "SPRADDR_GUID")
    String addressGuid

    static constraints = {
        addressGuid(nullable: true, maxSize: 36)
    }
}
