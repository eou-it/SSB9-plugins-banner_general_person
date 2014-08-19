/*******************************************************************************
 Copyright 2009-2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.query

import net.hedtech.banner.exceptions.ApplicationException
import org.hibernate.Criteria
import org.hibernate.criterion.Conjunction
import org.hibernate.Session
import org.hibernate.criterion.Restrictions

/**
 * Considerations:
 *  On things like searchLastName, an operator might need to be applied to the data, such as making it all capitalized.
 */
class ListFilterManager {
    enum operators  { eq, ne, lt, gt, co, st, t, f}
    def filter
    def viewToFilter

    /**
     * This defines what is available to the filter. It is an array of map values that contain:
     *      field: The field to use
     *      preProcessor: A closure used to process the data for the field. There are some avaiable as part of this class.
     */
    def filterDefinition

    def ListFilterManager(Class viewToUse, def filterDefinitionToUse) {
        viewToFilter = viewToUse
        filterDefinition = filterDefinitionToUse
    }


    void saveFilter(def theFilter) {
        def filterToSave = QueryBuilder.createFilters(theFilter)
        validateFilter(filterToSave)
        filter = filterToSave
    }

    Criteria getCriterionObject(Session session) {
        Criteria cr = session.createCriteria(viewToFilter, "cr1")
        def restrictionsList = []
        filter.each { it ->
                restrictionsList << getRestriction(it)
        }

        if (restrictionsList.size() > 0) {
            if (restrictionsList.size() == 1) {
                cr.add(restrictionsList[0])
            }
            else {
                // AND them all together
                Conjunction criterion = Restrictions.conjunction()
                restrictionsList.each{ it ->
                    criterion.add(it)
                }
                cr.add(criterion)

            }
        }
        return cr
    }


    private validateFilter(def theFilters) {
        // Ensure all of the operations are supported
        try {
            theFilters.each { it ->
                ListFilterManager.operators.valueOf(it.operator)
            }
        } catch (IllegalArgumentException ile) {
            throw new ApplicationException(this.getClass().getName(), ile)
        }
    }

    private def getRestriction(def map) {
        // Find a definition for our field
        def fieldDefinition = filterDefinition.find { it -> it.field == map.field }
        def value = map.value
        if (fieldDefinition && fieldDefinition.preProcessor ) {
            value = fieldDefinition.preProcessor(value)
        }
        if (map.operator == "eq") {
            return Restrictions.eq( map.field, value)
        }
        else if (map.operator == "st") {
            return Restrictions.like( map.field, value + "%")
        }
        else if (map.operator == "co") {
            return Restrictions.like( map.field, "%" + value + "%")
        }
        else if (map.operator == "ne") {
            return Restrictions.ne(map.field, value)
        }
    }

    static def capitalize = { it ->
        return it.toUpperCase()
    }
}
