package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.dataformat.ron.generator.RONGenerator;
import com.fasterxml.jackson.dataformat.ron.parser.RONParser;

import java.io.IOException;

public abstract class RONDeserializer<T> extends JsonDeserializer<T> {

    /**
     * Deserialize the value using the RONParser.
     */
    public abstract T deserialize(RONParser p, DeserializationContext ctxt) throws IOException;

    /**
     * Check if the parser is a RONParser, downcast it, then delegate to the relevant method in this class.
     */
    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (!(p instanceof RONParser)) {
            throw new JsonParseException(p, "Parser was not an instance of RONParser - cannot continue deserialization");
        }
        final RONParser parser = (RONParser) p;
        return this.deserialize(parser, ctxt);
    }
}
