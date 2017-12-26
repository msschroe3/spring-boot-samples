package com.boot.common.search

import org.springframework.validation.BindException
import spock.lang.Specification
import spock.lang.Unroll

class SearchFieldSpec extends Specification {
    @Unroll
    def 'constructor parses arg correctly - #desc'() {
        when:
        SearchField field = new SearchField(search)

        then:
        field.val == 'hello'
        field.op == SearchOperation.EQ

        where:
        desc                         | search
        'just val (default op used)' | 'hello'
        'val and op'                 | 'hello,EQ'
    }

    @Unroll
    def 'when val is #desc, constructor throws BindException'() {
        when:
        new SearchField(val)

        then:
        thrown BindException

        where:
        desc           | val
        'null'         | null
        'empty string' | ''
    }
}
