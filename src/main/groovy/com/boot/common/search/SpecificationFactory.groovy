package com.boot.common.search

import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Component

import java.time.format.DateTimeParseException

import static java.time.ZonedDateTime.now
import static java.time.ZonedDateTime.parse

/**
 * This factory handles creating Specifications (used by JPA) from an Object
 * that contains one to many SearchField properties.
 */
@Component
class SpecificationFactory {
    /**
     * This method will loop through the properties on the searchRequest object
     * and create a Specification for any of the properties that are of type SearchField.
     * It will use the SearchField data (val & op) to determine which Specification to build.
     * @param searchRequest Object with one to many SearchField properties
     * @return Specifications instance that can be passed to a JPA repository extending JpaSpecificationExecutor
     */
    Specifications buildSpecifications(def searchRequest) {
        Specifications specs

        // loop through props on SearchRequest and create Specification for each SearchField
        searchRequest?.properties?.each { k,v ->
            if (v instanceof SearchField) {
                Specification spec = buildSpecification(k as String, v.val, v.op)

                // if specs hasn't been used yet, the first clause has to be a where
                if (!specs) {
                    specs = Specifications.where(spec)

                // all additional clauses get and'd onto the initial where clause
                } else {
                    specs = specs.and(spec)
                }
            }
        }

        return specs
    }

    /**
     * Based on the SearchOperation, the appropriate Specification will be built.
     * @param attribute name of the domain attribute
     * @param val value being searched
     * @param operation operation to perform (equals, contains, starts with, less than, etc)
     * @return Specification
     */
    Specification buildSpecification(String attribute, String val, SearchOperation operation) {
        switch (operation) {
            case SearchOperation.SW:
                return startsWith(attribute, val)
            case SearchOperation.EW:
                return endsWith(attribute, val)
            case SearchOperation.CT:
                return containsLike(attribute, val)
            case SearchOperation.EQ:
                return isEqual(attribute, val)
            case SearchOperation.LT:
                return lessThan(attribute, val)
            case SearchOperation.LTE:
                return lessThanOrEqualTo(attribute, val)
            case SearchOperation.GT:
                return greaterThan(attribute, val)
            case SearchOperation.GTE:
                return greaterThanOrEqualTo(attribute, val)
            case SearchOperation.LTD:
                return lessThanDate(attribute, val)
            case SearchOperation.LTED:
                return lessThanOrEqualToDate(attribute, val)
            case SearchOperation.GTD:
                return greaterThanDate(attribute, val)
            case SearchOperation.GTED:
                return greaterThanOrEqualToDate(attribute, val)
            case SearchOperation.N:
                return isNull(attribute)
            case SearchOperation.NN:
                return isNotNull(attribute)
            default:
                throw new IllegalArgumentException("Unsupported Search Operation: $operation")
        }
    }

    /**
     * Mixed Type Operations
     */

    private static Specification isEqual(String attribute, String value) {
        return { root, query, cb -> cb.equal(root.get(attribute), castValue(value)) }
    }

    /**
     * String Specific Operations
     */

    private static Specification startsWith(String attribute, String value) {
        return { root, query, cb -> cb.like(root.get(attribute), "$value%") }
    }

    private static Specification endsWith(String attribute, String value) {
        return { root, query, cb -> cb.like(root.get(attribute), "%$value") }
    }

    private static Specification containsLike(String attribute, String value) {
        return { root, query, cb -> cb.like(root.get(attribute), "%$value%") }
    }

    /**
     * Number Specific Operations (Only Integer & Double as of now)
     */

    private static Specification lessThan(String attribute, String value) {
        return { root, query, cb -> cb.lessThan(root.get(attribute), castNumber(value)) }
    }

    private static Specification lessThanOrEqualTo(String attribute, String value) {
        return { root, query, cb -> cb.lessThanOrEqualTo(root.get(attribute), castNumber(value)) }
    }

    private static Specification greaterThan(String attribute, String value) {
        return { root, query, cb -> cb.greaterThan(root.get(attribute), castNumber(value)) }
    }

    private static Specification greaterThanOrEqualTo(String attribute, String value) {
        return { root, query, cb -> cb.greaterThanOrEqualTo(root.get(attribute), castNumber(value)) }
    }

    /**
     * Date Specific Operations
     */

    private static Specification lessThanDate(String attribute, String value) {
        return { root, query, cb -> cb.lessThan(root.get(attribute), parseDate(value)) }
    }

    private static Specification lessThanOrEqualToDate(String attribute, String value) {
        return { root, query, cb -> cb.lessThanOrEqualTo(root.get(attribute), parseDate(value)) }
    }

    private static Specification greaterThanDate(String attribute, String value) {
        return { root, query, cb -> cb.greaterThan(root.get(attribute), parseDate(value)) }
    }

    private static Specification greaterThanOrEqualToDate(String attribute, String value) {
        return { root, query, cb -> cb.greaterThanOrEqualTo(root.get(attribute), parseDate(value)) }
    }

    /**
     * Null Operations
     */

    private static Specification isNull(String attribute) {
        return { root, query, cb -> cb.isNull(root.get(attribute)) }
    }

    private static Specification isNotNull(String attribute) {
        return { root, query, cb -> cb.isNotNull(root.get(attribute)) }
    }

    /**
     * Takes a String and attempts to cast it to the appropriate Number type (Integer or Double)
     * @param value String value
     * @return casted Number version of the value
     */
    private static def castNumber(String value) {
        if (value.isInteger()) {
            return value as Integer
        } else if (value.isDouble()) {
            return value as Double
        }

        throw new IllegalArgumentException("Unsupported Type for Numeric Search Operation")
    }

    /**
     * Takes a String and attempts to cast it to the appropriate type
     * @param value String value
     * @return casted version of the value
     */
    private static def castValue(String value) {
        if (value.isInteger()) {
            return value as Integer
        } else if (value.isDouble()) {
            return value as Double
        }

        return value
    }

    private static final NOW = "now"

    /**
     * Attempts to parse ZonedDateTime. If 'now' is provided, ZonedDateTime.now() is used as the value.
     * @param date date in String format
     * @return parsed ZonedDateTime object
     */
    private static parseDate(String date) {
        try {
            return (date == NOW) ? now() : parse(date)
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid Date Provided: $date")
        }
    }
}
