package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.ron.generator.RONGenerator;

import java.io.IOException;

/**
 * Serialize RON structs.
 */
public class StructSerializer extends RONSerializer<Object> {

    @Override
    public void serialize(Object value, RONGenerator gen, SerializerProvider serializers) throws IOException {
        // TODO adapt impl from BeanSerializer

        gen.writeStartStruct();

        // FIXME write fields...

        gen.writeEndStruct();
    }
}
