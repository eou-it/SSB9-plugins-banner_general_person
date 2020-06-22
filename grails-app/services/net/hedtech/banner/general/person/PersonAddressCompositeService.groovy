/*********************************************************************************
Copyright 2012-2019 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
 package net.hedtech.banner.general.person

import grails.gorm.transactions.Transactional
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.i18n.MessageHelper

@Transactional
class PersonAddressCompositeService {

    def personAddressService
    def personTelephoneService
    def sessionFactory

    def createOrUpdate(map, overwrite = true) {
        if (map?.deletePersonAddressTelephones) {
            processDeletes(map.deletePersonAddressTelephones.collect{it.personAddress})
        }
        if (map?.createPersonAddressTelephones) {
            processInsertUpdates(map.createPersonAddressTelephones, overwrite)
        }
    }


    private void processInsertUpdates(domains, overwrite = true) {
        def address
        domains.each { domain ->
            //As the edits for the address checking are done in API, we will not do them here.
            if (domain.personAddress.id == null)   {
                if (!(domain.personAddress.statusIndicator == 'I'))
                    checkDatesForCreate(domain.personAddress)
                address = personAddressService.create([domainModel:domain.personAddress])
                if ((domain.telephoneType) || (domain.countryPhone) || (domain.phoneArea) || (domain.phoneNumber) || (domain.phoneExtension))      {
                    checkPhone(domain)
                    personTelephoneService.create([pidm:domain.personAddress.pidm,telephoneType:domain.telephoneType,countryPhone:domain.countryPhone,phoneArea:domain.phoneArea,phoneNumber:domain.phoneNumber,phoneExtension:domain.phoneExtension,addressType:domain.personAddress.addressType,addressSequenceNumber:address.sequenceNumber])
                }  }
            else   {
                checkDatesForUpdate(domain.personAddress)

                if (overwrite) {
                    personAddressService.update(domain.personAddress)
                } else {
                    updateWithoutOverwrite(domain.personAddress)
                }

                if (domain.telephoneSequenceNumber)     {
                // if delete issue an error here
                    def telephone = PersonTelephone.fetchByPidmTelephoneTypeAndTelephoneSequence([pidm:domain.personAddress.pidm,telephoneType:domain.telephoneType,telephoneSequenceNumber:domain.telephoneSequenceNumber])
                    if (!(domain.phoneNumber) && (telephone.phoneNumber) )
                         throw new ApplicationException(PersonAddress,"@@r1:telephoneNotDeleteFromAddress")
                    checkPhone(domain)

                    telephone.countryPhone = domain.countryPhone
                    telephone.phoneArea = domain.phoneArea
                    telephone.phoneNumber = domain.phoneNumber
                    telephone.phoneExtension = domain.phoneExtension
                    telephone.telephoneType = domain.telephoneType
                    personTelephoneService.update(telephone)}
                else
                    if ((domain.telephoneType) || (domain.countryPhone) || (domain.phoneArea) || (domain.phoneNumber) || (domain.phoneExtension))      {
                        checkPhone(domain)
                        personTelephoneService.create([pidm:domain.personAddress.pidm,addressType:domain.personAddress.addressType,addressSequenceNumber:domain.personAddress.sequenceNumber,telephoneType:domain.telephoneType,countryPhone:domain.countryPhone,phoneArea:domain.phoneArea,phoneNumber:domain.phoneNumber,phoneExtension:domain.phoneExtension])
                    }
            }
        }
    }

    private void updateWithoutOverwrite(address) {
        def existingAddr = personAddressService.get(address.id)

        // Archive the old address as inactive
        if (existingAddr) {
            existingAddr.statusIndicator = 'I'
            personAddressService.update(existingAddr)
        }

        personAddressService.create([domainModel:address])
    }

    private void processDeletes(domains) {
        domains.each { domain ->
            personAddressService.delete(domain)
        }
    }


    private boolean checkDatesForCreate(domain) {
        def addressCriteria = [pidm:domain.pidm,addressType:domain.addressType,fromDate:domain.fromDate,toDate:domain.toDate]
        if (domain.fromDate == null && domain.toDate == null)   {
          if (PersonAddress.fetchNotInactiveAddressByPidmAndAddressType(addressCriteria).list.size() > 0)
            throw new ApplicationException(PersonAddress,"@@r1:multipleAddressesExist@@")
        } else {
         if (PersonAddress.fetchActiveAddressByPidmAndAddressTypeAndDates(addressCriteria).list.size() > 0)
            throw new ApplicationException(PersonAddress,"@@r1:multipleAddressesExist@@")
        }
    }


    private boolean checkDatesForUpdate(domain) {
        PersonUtility.checkForOptimisticLockingError(domain, PersonAddress,
                MessageHelper.message("default.optimistic.locking.failure.refresh", MessageHelper.message("personInfo.title.address")))
        def addressCriteria = [pidm:domain.pidm,addressType:domain.addressType,fromDate:domain.fromDate,toDate:domain.toDate,id:domain.id]
         if (domain.fromDate == null && domain.toDate == null)   {
          if (PersonAddress.fetchNotInactiveAddressByPidmAndAddressTypeExcludingId(pidm:domain.pidm,addressType:domain.addressType,fromDate:domain.fromDate,toDate:domain.toDate,id:domain.id).list.size() > 0)
            throw new ApplicationException(PersonAddress,"@@r1:multipleAddressesExist@@")
        } else {
        if (PersonAddress.fetchActiveAddressByPidmAndAddressTypeAndDatesExcludingId(addressCriteria).list.size() > 0)
            throw new ApplicationException(PersonAddress,"@@r1:multipleAddressesExist@@")
         }
    }


    private boolean checkPhone(domain) {
        if (((domain.countryPhone) || (domain.phoneArea)) && !(domain.phoneNumber))  {
            throw new ApplicationException(PersonAddress,"@@r1:phoneNumberNeededWithAncillaryPhoneInfo@@")
        }
    }


}



