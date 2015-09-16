package net.hedtech.banner.general.person.ldm.v4

import net.hedtech.banner.general.person.PersonIdentificationNameCurrent

/**
 * LDM Decorator for person resource names.
 */
class Name extends net.hedtech.banner.general.person.ldm.v1.Name{

    def Name(PersonIdentificationNameCurrent personName, def person) {
      super(personName,person)
    }

    String getPedigree(){
        if(!super.getPedigree()){
            return ''
        }
        return super.getPedigree()
    }

    String getPreferredName(){
        if(!super.getPreferredName()){
            return ''
        }
        return super.getPreferredName()
    }

    String getTitle(){
        if(!super.getTitle()){
            return ''
        }
        return super.getTitle()
    }

    String getSurnamePrefix(){
        if(!super.getSurnamePrefix()){
            return ''
        }
        return super.getSurnamePrefix()
    }


}
