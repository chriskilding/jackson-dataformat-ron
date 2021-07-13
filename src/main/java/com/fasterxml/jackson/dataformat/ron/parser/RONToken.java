package com.fasterxml.jackson.dataformat.ron.parser;

/**
 * Enumeration for basic token types used for returning results of parsing RON content. (The counterpart of JsonToken.)
 */
public enum RONToken {
    NOT_AVAILABLE,

    START_MAP,
    END_MAP,

    ENUM,

    START_TUPLE,
    END_TUPLE,

    START_ARRAY,
    END_ARRAY,

    FIELD_NAME,

    STRING,
    NUMBER,
    TRUE,
    FALSE,
    INF,
    MINUS_INF,
    NAN
}
