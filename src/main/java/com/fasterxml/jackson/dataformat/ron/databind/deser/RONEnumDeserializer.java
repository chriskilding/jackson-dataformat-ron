package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.util.CompactStringObjectMap;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.dataformat.ron.parser.RONParser;

import java.io.IOException;

public class RONEnumDeserializer extends RONDeserializer<Object> {

    private final EnumResolver byNameResolver;

    private final CompactStringObjectMap lookupByName;

    public RONEnumDeserializer(EnumResolver byNameResolver) {
        this.byNameResolver = byNameResolver;
        this.lookupByName = byNameResolver.constructLookup();
    }

    @Override
    public Object deserialize(RONParser p, DeserializationContext ctxt) throws IOException {
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            return lookupByName.find(p.getText());
        }

        return ctxt.handleUnexpectedToken(enumClass(), p.currentToken(), p, "Enum value could not be deserialized");
    }

    private Class<?> enumClass() {
        return handledType();
    }
}
