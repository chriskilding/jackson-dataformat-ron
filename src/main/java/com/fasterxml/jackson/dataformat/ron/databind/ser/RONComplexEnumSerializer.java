package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.dataformat.ron.generator.RONGenerator;

import java.io.IOException;

public class RONComplexEnumSerializer extends RONSerializer<Object> {

    private final BeanPropertyWriter[] properties;

    public RONComplexEnumSerializer(BeanPropertyWriter[] properties) {
        this.properties = properties;
    }

    @Override
    public void serialize(Object enumeration, RONGenerator gen, SerializerProvider serializers) throws IOException {
        final String name = enumeration.getClass().getSimpleName();

        gen.writeStartEnum(name);
        serializeElements(enumeration, gen, serializers);
        gen.writeEndEnum();
    }

    private void serializeElements(Object enumeration, RONGenerator gen, SerializerProvider provider) throws IOException {
        for (BeanPropertyWriter prop: this.properties) {
            if (prop != null) { // can have nulls in filtered list
                try {
                    prop.serializeAsElement(enumeration, gen, provider);
                } catch (Exception e) {
                    String name = prop.getName();
                    throw new JsonGenerationException("Could not serialize field '" + name + "'", gen);
                }
            }
        }

    }
}
