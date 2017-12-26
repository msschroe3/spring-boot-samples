package com.boot.common.converter

import org.codehaus.groovy.runtime.InvokerHelper

class Converter {
    /**
     * Will copy over any non-null properties from the source to the target.
     * Properties must be named the same in order to be copied.
     * @param target object that will receive source's properties and be returned
     * @param source object that will have properties copied from
     * @return target with source's non-null properties
     */
    static def copyProperties(def target, def source) {
        InvokerHelper.setProperties(target, source.properties.findAll { it.value != null })
        return target
    }
}
