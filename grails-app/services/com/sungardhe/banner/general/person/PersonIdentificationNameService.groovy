
/*******************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *******************************************************************************/

package com.sungardhe.banner.general.person


import org.springframework.security.core.context.SecurityContextHolder as SCH
import groovy.sql.Sql
import com.sungardhe.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).  
// These exceptions must be caught and handled by the controller using this service.
// 
// update and delete may throw com.sungardhe.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

class PersonIdentificationNameService extends ServiceBase {

    boolean transactional = true

    def sessionFactory

    
    /**
     * Please put all the custom methods in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(personidentificationname_custom_service_methods) ENABLED START*/
 
    /**
     *
     * Post Create refreshes the model to populate the API generated values
     */

    def postCreate (map){
        def newPers = PersonIdentificationName.get(map.after.id)
        newPers.refresh()

    }


    def fetchEntityOfPerson( pidm ){
      def sql
      def entity
      sql = new Sql(sessionFactory.getCurrentSession().connection())
      sql.call("{? = call gb_identification.f_get_entity(?)}",
                      [Sql.VARCHAR,pidm]) { result -> entity = result }
      sql?.close()
      return entity
    }


    def getSystemGeneratedIdPrefix() {
      def sql
      def idPrefix
      sql = new Sql(sessionFactory.getCurrentSession().connection())
      sql.call("{? = call sokfunc.f_get_idprefix(?)}",
                     [Sql.VARCHAR, 'ID']) { result -> idPrefix = result }
      sql?.close()
      return idPrefix
    }  
    /*PROTECTED REGION END*/
}
