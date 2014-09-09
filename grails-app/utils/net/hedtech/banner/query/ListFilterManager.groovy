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
    /**
     * The operators. They are equals, not equals, less than, greater than, contains, starts with, exists, does not exist,
     * true, false
     */
    enum operators  { eq, ne, lt, gt, co, st, has, hasnot, t, f}

    /**
     * The field types. They are:
     *      freeform: The value can be anything and a free form text field is provided.
     *      operatoronly: No value field is provided as the operator has all of the information needed.
     *      validation: A validation object is provided that provides a list of values to choose from.
     */
    enum type { freeform, operatoronly, validation}

    /**
     * The filter is generated by query builder from an input that is described in saveFilter.
     * It is a list of maps. Each map has the following elements:
     *      field: The field for the filter
     *      value: The value for this field.
     *      operator: The operator for this filter element. It should match one of the operators above.
     */
    def filter

    /**
     * This defines what is available to the filter. It is an array of map values that contain:
     *      field: The field to use. This is itself is a map of the following:
     *          code: The field that is used in the criterion
     *          description: The description displayed to the UI
     *      type: matches on of the type's described in our enum. This is used for the UI to build the value field. If
     *          type is validation, then validationObject must be included in the definition. If type is operatoronly,
     *          then value is not allowed in the filter.
     *      validateDomain: The domain object to used to get the validation objects for the field.
     *      operators: [] An array of operators that match the above enumeration
     *      preProcessor: A closure used to process the data for the field. There are some avaiable as part of this class.
     *      specialProcessor: A closure used to generate the restriction for this field.
     */
    def filterDefinition

    def ListFilterManager(def filterDefinitionToUse) {
        filterDefinition = filterDefinitionToUse
    }

    /**
     * Takes a filter map and parses it, then store it. The filters map looks like this:
     *      filter[x][field] - The field for the given filter, where x is an integer 0-n
     *      filter[x][operator] - The operator for the filter
     *      filter[x][value] - The value for the filter.
     * @param theFilter
     */
    void saveFilter(def theFilter) {
        def filterToSave = QueryBuilder.createFilters(theFilter)
        validateFilter(filterToSave)
        filter = filterToSave.asList()
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


    void deleteAttributeFromFilter(def attribute) {
        for (def i=0;i<filter?.size(); i++) {
            if (filter[i].field == attribute) {
                filter.remove(i)
                break;
            }
        }
    }

    void deleteFilter() {
        filter = null
    }


    private validateFilter(def theFilters) {
        try {
            theFilters.each { it ->
                // Ensure all of our operators are valid.
                ListFilterManager.operators.valueOf(it.operator)

                // now get the configuration for the filter. IF the configuration states that the filter should
                // be operatoronly and there is a value, then we hafve a problem
                def filterDef = filterDefinition.find{ fd -> fd.field.code == it.field}
                if (filterDef) {
                    if (filterDef.type) {

                        // Ensure type if valid
                        ListFilterManager.type.valueOf(filterDef.type)

                        if (filterDef.type == ListFilterManager.type.operatoronly.toString()) {
                            // If our definition has a type of operatoronly, no value is allowed in the filter
                            if (it.value) {
                                throw new IllegalArgumentException("Value exists for operatoronly operation: " + it)
                            }
                        }
                        else if (filterDef.type == ListFilterManager.type.validation.toString()) {
                            if (!filterDef.validationDomain) {
                                throw new IllegalArgumentException("Missing domain for validation object: " + it)
                            }
                        }
                    }
                }
                else {
                    // No def for our filter
                    throw new IllegalArgumentException("Missing filter definition for filter: " + it)
                }
            }
        } catch (IllegalArgumentException ile) {
            throw new ApplicationException(this.getClass().getName(), ile)
        }
    }

    /**
     * Map has the following:
     *  field: The field to operator on
     *  operator: The operator to apply
     *  value: The value of the filter item
     *
     * @param map See above
     * @return A hibernate restriction
     */
    private def getRestriction(def map) {
        // Find a definition for our field
        def fieldDefinition = filterDefinition.find { it -> it.field.code == map.field }
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
            else if (map.operator == "t") {
                if (fieldDefinition.trueFalseProcessor) {
                    value = fieldDefinition.trueFalseProcessor("t")
                }
                else {
                    value = ListFilterManager.defaultTrueFalseGenerator("t")
                }
                return Restrictions.eq(map.field, value)
            }
            else if (map.operator == "f") {
                if (fieldDefinition.trueFalseProcessor) {
                    value = fieldDefinition.trueFalseProcessor("f")
                }
                else {
                    value = ListFilterManager.defaultTrueFalseGenerator("f")
                }
                return Restrictions.eq(map.field, value)
            }
        }
    }

    static def capitalize = { it ->
        return it.toUpperCase()
    }


    static def defaultMapGenerator = {it ->
        return [description: it.description, code: it.code]
    }


    static def defaultTrueFalseGenerator = { it ->
        if (it == "t") {
            return true
        }
        else {
            return false
        }

    }
}
