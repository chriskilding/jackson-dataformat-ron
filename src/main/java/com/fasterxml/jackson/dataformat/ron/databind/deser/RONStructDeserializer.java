package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.dataformat.ron.parser.RONParser;

import java.io.IOException;

public class RONStructDeserializer extends RONDeserializer<Object> {
    @Override
    public Object deserialize(RONParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return null;
    }
}
