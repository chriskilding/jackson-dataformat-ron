package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.ron.generator.RONGenerator;

import java.io.IOException;

/**
 * Serialize RON tuples.
 */
public class TupleSerializer extends RONSerializer<Object> {
    @Override
    public void serialize(Object value, RONGenerator gen, SerializerProvider serializers) throws IOException {
        // TODO adapt impl from BeanSerializer
        // TODO how will we know whether to serialize as a struct or as a tuple?
        //    ...is this even doable in Java? Which has no native concept of tuples... apart from Collection<Object>?

        gen.writeStartTuple();
        // FIXME do this...
        gen.writeEndTuple();
    }
}
