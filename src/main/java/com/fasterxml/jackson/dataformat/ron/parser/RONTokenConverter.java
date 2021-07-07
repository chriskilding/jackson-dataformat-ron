package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.core.JsonToken;

public class RONTokenConverter {

    public static JsonToken toJsonToken(RONToken t) {
        if (t == null) {
            return null;
        }

        switch (t) {
            case START_ARRAY:
                return JsonToken.START_ARRAY;
            case END_ARRAY:
                return JsonToken.END_ARRAY;
            case START_MAP:
                return JsonToken.START_OBJECT;
            case END_MAP:
                return JsonToken.END_OBJECT;
            case TRUE:
                return JsonToken.VALUE_TRUE;
            case FALSE:
                return JsonToken.VALUE_FALSE;
            case STRING:
                return JsonToken.VALUE_STRING;
            case FLOAT:
                return JsonToken.VALUE_NUMBER_FLOAT;
            case INTEGER:
                return JsonToken.VALUE_NUMBER_INT;
            case NOT_AVAILABLE:
                return JsonToken.NOT_AVAILABLE;
            case FIELD_NAME:
                return JsonToken.FIELD_NAME;
            default:
                throw new UnsupportedOperationException("Encountered a RON token that has no equivalent in JSON");
        }
    }
}
