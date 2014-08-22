/*******************************************************************************
 Copyright 2009-2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.query

import net.hedtech.banner.exceptions.ApplicationException
import org.hibernate.criterion.Criterion
import org.hibernate.criterion.Restrictions

/**
 * Considerations:
 *  On things like searchLastName, an operator might need to be applied to the data, such as making it all capitalized.
 */
class ListFilterManager {
    enum operators  { eq, ne, lt, gt, co, st, has, hasnot, t, f}
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

    Criterion getCriterionObject() {
        Criterion cr
        def restrictionsList = []
        filter.each { it ->
                restrictionsList << getRestriction(it)
        }

        if (restrictionsList.size() > 0) {
            if (restrictionsList.size() == 1) {
                cr = restrictionsList[0]
            }
            else {
                // AND them all together
                cr = Restrictions.conjunction()
                restrictionsList.each{ it ->
                    cr.add(it)
                }
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

    /**
     * Map has the following:
     *  field: The field to operator on
     *  operator: The operator to apply
     *
     * @param map See above
     * @return A hibernate restriction
     */
    private def getRestriction(def map) {
        // Find a definition for our field
        def fieldDefinition = filterDefinition.find { it -> it.field == map.field }
        def value = map.value
        if (fieldDefinition && fieldDefinition.preProcessor ) {
            value = fieldDefinition.preProcessor(value)
        }

        if (fieldDefinition && fieldDefinition.specialProcessor) {
            return fieldDefinition.specialProcessor(map)
        }
        else {
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
    }

    static def capitalize = { it ->
        return it.toUpperCase()
    }
}
