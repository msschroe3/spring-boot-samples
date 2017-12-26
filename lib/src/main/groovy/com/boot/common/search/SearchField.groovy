package com.boot.common.search

import org.springframework.validation.BindException

/**
 * When performing a search there are two ways you can specify the search value and operation.
 *
 * 1) Explicit SearchField properties - the constructor won't be hit in this case
 *
 *      http://domain.com/api/v1/puppies?puppies.name=TAZ&puppies.op=CT
 *
 * 2) Single property, which utilizes SearchField constructor
 *
 *      http://domain.com/api/v1/puppies?name=TAZ,CT
 *
 *      The value passed in (TAZ,CT) will be split into value and operation and assigned
 *      to the class properties. If anything goes wrong in the binding, a BindException is thrown.
 */
class SearchField {

    SearchField(String search) {
        if (!search) {
            throw new BindException("SearchField", "val")
        }

        // grab the search val and search operation from the string
        def (val, op) = search.tokenize(',')

        this.val = val
        this.op = op ? op as SearchOperation : SearchOperation.EQ
    }

    /**
     * Value to use along with the search operation
     */
    String val

    /**
     * Search operation to perform on the field with the provided value
     */
    SearchOperation op = SearchOperation.EQ
}
