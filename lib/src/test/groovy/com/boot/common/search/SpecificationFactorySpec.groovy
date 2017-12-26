package com.boot.common.search

import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.boot.common.util.TestSearchRequest
import org.springframework.data.jpa.domain.Specifications
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Path
import javax.persistence.criteria.Root
import java.time.ZonedDateTime

class SpecificationFactorySpec extends Specification {
    @Subject
    SpecificationFactory factory

    @Shared
    CriteriaBuilder cb

    @Shared
    CriteriaQuery cq

    @Shared
    Root root

    @Shared
    Path path

    def setup() {
        cb = Mock()
        cq = Mock()
        root = Mock()
        path = Mock()
    }

    def "buildSpecifications"() {
        given:
        def request = new TestSearchRequest([
                'field1': new SearchField('hello world,EQ'),
                'field2': new SearchField('1.5,GT'),
                'field3': new SearchField('ok,SW')
        ])

        when:
        Specifications result = factory.buildSpecifications(request)

        then:
        result
    }

    def "buildSpecifications with one field"() {
        given:
        def request = new TestSearchRequest([
                'field1': new SearchField('hello world,EQ')
        ])

        when:
        Specifications result = factory.buildSpecifications(request)

        then:
        result
    }

    @Shared
    String attr = 'field-name'

    @Shared
    String stringVal = 'value'

    @Shared
    String intVal = '3'

    @Shared
    String doubleVal = '3.5'

    @Shared
    String nowVal = 'now'

    @Shared
    String dateVal = ZonedDateTime.now().toString()


    @Unroll
    def "buildSpecification - isEqual with #dataType"() {
        when:
        factory
                .buildSpecification(attr, value, SearchOperation.EQ)
                .toPredicate(root, cq, cb)

        then:
        1 * root.get(attr) >> path
        1 * cb.equal(path, castVal)

        where:
        dataType  | value     | castVal
        'String'  | stringVal | stringVal
        'Integer' | intVal    | intVal as Integer
        'Double'  | doubleVal | doubleVal as Double
    }

    @Unroll
    def "buildSpecification: #opName"() {
        when:
        factory
                .buildSpecification(attr, stringVal, opEnum)
                .toPredicate(root, cq, cb)

        then:
        1 * root.get(attr) >> path
        1 * cb.like(path, pattern)

        where:
        opName         | opEnum                                       | pattern
        'startsWith'   | SearchOperation.SW | "$stringVal%"
        'endsWith'     | SearchOperation.EW | "%$stringVal"
        'containsList' | SearchOperation.CT | "%$stringVal%"
    }

    @Unroll
    def "buildSpecification: lessThan with #type"() {
        when:
        factory
                .buildSpecification(attr, val, SearchOperation.LT)
                .toPredicate(root, cq, cb)

        then:
        1 * root.get(attr) >> path
        1 * cb.lessThan(path, castVal)

        where:
        type      | val       | castVal
        'Integer' | intVal    | intVal as Integer
        'Double'  | doubleVal | doubleVal as Double
    }

    @Unroll
    def "buildSpecification: lessThanOrEqualTo with #type"() {
        when:
        factory
                .buildSpecification(attr, val, SearchOperation.LTE)
                .toPredicate(root, cq, cb)

        then:
        1 * root.get(attr) >> path
        1 * cb.lessThanOrEqualTo(path, castVal)

        where:
        type      | val       | castVal
        'Integer' | intVal    | intVal as Integer
        'Double'  | doubleVal | doubleVal as Double
    }

    @Unroll
    def "buildSpecification: greaterThan with #type"() {
        when:
        factory
                .buildSpecification(attr, val, SearchOperation.GT)
                .toPredicate(root, cq, cb)

        then:
        1 * root.get(attr) >> path
        1 * cb.greaterThan(path, castVal)

        where:
        type      | val       | castVal
        'Integer' | intVal    | intVal as Integer
        'Double'  | doubleVal | doubleVal as Double
    }

    @Unroll
    def "buildSpecification: greaterThanOrEqualTo with #type"() {
        when:
        factory
                .buildSpecification(attr, val, SearchOperation.GTE)
                .toPredicate(root, cq, cb)

        then:
        1 * root.get(attr) >> path
        1 * cb.greaterThanOrEqualTo(path, castVal)

        where:
        type      | val       | castVal
        'Integer' | intVal    | intVal as Integer
        'Double'  | doubleVal | doubleVal as Double
    }

    @Unroll
    def "buildSpecification: lessThanDate with #type"() {
        when:
        factory
                .buildSpecification(attr, val, SearchOperation.LTD)
                .toPredicate(root, cq, cb)

        then:
        1 * root.get(attr) >> path
        1 * cb.lessThan(path, _ as ZonedDateTime)

        where:
        type   | val
        'now'  | nowVal
        'Date' | dateVal
    }

    @Unroll
    def "buildSpecification: lessThanOrEqualToDate with #type"() {
        when:
        factory
                .buildSpecification(attr, val, SearchOperation.LTED)
                .toPredicate(root, cq, cb)

        then:
        1 * root.get(attr) >> path
        1 * cb.lessThanOrEqualTo(path, _ as ZonedDateTime)

        where:
        type   | val
        'now'  | nowVal
        'Date' | dateVal
    }

    @Unroll
    def "buildSpecification: greaterThanDate with #type"() {
        when:
        factory
                .buildSpecification(attr, val, SearchOperation.GTD)
                .toPredicate(root, cq, cb)

        then:
        1 * root.get(attr) >> path
        1 * cb.greaterThan(path, _ as ZonedDateTime)

        where:
        type   | val
        'now'  | nowVal
        'Date' | dateVal
    }

    @Unroll
    def "buildSpecification: greaterThanOrEqualToDate with #type"() {
        when:
        factory
                .buildSpecification(attr, val, SearchOperation.GTED)
                .toPredicate(root, cq, cb)

        then:
        1 * root.get(attr) >> path
        1 * cb.greaterThanOrEqualTo(path, _ as ZonedDateTime)

        where:
        type   | val
        'now'  | nowVal
        'Date' | dateVal
    }

    def "buildSpecification: isNull"() {
        when:
        factory
                .buildSpecification(attr, stringVal, SearchOperation.N)
                .toPredicate(root, cq, cb)

        then:
        1 * root.get(attr) >> path
        1 * cb.isNull(path)
    }

    def "buildSpecification: isNotNull"() {
        when:
        factory
                .buildSpecification(attr, stringVal, SearchOperation.NN)
                .toPredicate(root, cq, cb)

        then:
        1 * root.get(attr) >> path
        1 * cb.isNotNull(path)
    }

    // cannot test this case since all possible SearchOperation values are supported
    // def "buildSpecification throws exception for unsupported SearchOperation"()

    @Unroll
    def "castNumber throws exception when #type is provided"() {
        when:
        factory
                .buildSpecification(attr, val, SearchOperation.LT)
                .toPredicate(root, cq, cb)

        then:
        1 * root.get(attr) >> path
        0 * cb.lessThan(path, _)

        thrown IllegalArgumentException

        where:
        type     | val
        'String' | stringVal
        'Date'   | dateVal
    }

    @Unroll
    def "parseDate throws exception when #type is provided"() {
        when:
        factory
                .buildSpecification(attr, val, SearchOperation.LTED)
                .toPredicate(root, cq, cb)

        then:
        1 * root.get(attr) >> path
        0 * cb.lessThan(path, _)

        thrown IllegalArgumentException

        where:
        type      | val
        'String'  | stringVal
        'Integer' | intVal
        'Double'  | doubleVal
    }

}