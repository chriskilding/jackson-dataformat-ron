package com.fasterxml.jackson.dataformat.ron.parser;

/**
 * Enumeration for basic token types used for returning results of parsing RON content. (The counterpart of JsonToken.)
 */
public enum RONToken {
    NOT_AVAILABLE,

    START_STRUCT,
    END_STRUCT,

    START_OBJECT,
    END_OBJECT,

    START_ENUM,
    END_ENUM,

    START_TUPLE,
    END_TUPLE,

    START_ARRAY,
    END_ARRAY,

    FIELD_NAME,

    STRING,
    INTEGER,
    FLOAT,
    TRUE,
    FALSE,

    COMMA,

}
