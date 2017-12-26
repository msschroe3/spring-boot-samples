package com.boot.common.search

/**
 * Abbreviations of supported Search Operations
 */
enum SearchOperation {
    // contains
    CT,
    // starts with
    SW,
    // ends with
    EW,
    // equals
    EQ,
    // less than
    LT,
    // less than or equal to
    LTE,
    // greater than
    GT,
    // greater than or equal to
    GTE,
    // less than date
    LTD,
    // greater than date
    GTD,
    // less than or equal to date
    LTED,
    // greater than or equal to date
    GTED,
    // is null
    N,
    // is not null
    NN
}