package com.boot.common.converter

import com.boot.common.util.TestObject
import spock.lang.Specification

class ConverterSpec extends Specification {
    def 'non-null props are copied'() {
        given:
        TestObject target = new TestObject(
                prop1: 'target prop1',
                prop2: 'target prop2'
        )

        TestObject source = new TestObject(
                prop1: 'source prop1',
                prop2: null
        )

        when:
        TestObject result = Converter.copyProperties(target, source) as TestObject

        then:
        // source prop had value, so should copy over
        result.prop1 == source.prop1
        // source prop was null, so shouldn't copy over
        result.prop2 == target.prop2
    }
}