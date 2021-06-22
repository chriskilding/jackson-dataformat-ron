package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.dataformat.ron.generator.RONGenerator;

import java.io.IOException;

/**
 * Serialize RON structs. Adapted from Jackson's BeanSerializer.
 */
public class RONStructSerializer extends RONSerializer<Object> {

    /**
     * Writers used for outputting actual property values
     */
    private final BeanPropertyWriter[] _props;

    /**
     * Feature flag: whether to serialize the struct with its name.
     */
    private final boolean namedStruct;

    public RONStructSerializer(BeanPropertyWriter[] properties, boolean namedStruct) {
        _props = properties;
        this.namedStruct = namedStruct;
    }

    @Override
    public void serialize(Object value, RONGenerator gen, SerializerProvider serializers) throws IOException {
        // TODO proper Jackson name lookup
        String name = value.getClass().getName();

        if (namedStruct) {
            gen.writeStartStruct(name);
        } else {
            gen.writeStartStruct();
        }
        serializeFields(value, gen, serializers);
        gen.writeEndStruct();
    }

    private void serializeFields(Object value, RONGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            for (BeanPropertyWriter prop: _props) {
                if (prop != null) { // can have nulls in filtered list
                    prop.serializeAsField(value, gen, serializers);
                }
            }
        } catch (Exception e) {
            throw new JsonMappingException(gen, "Could not serialize RON struct", e);
        }
    }
}
