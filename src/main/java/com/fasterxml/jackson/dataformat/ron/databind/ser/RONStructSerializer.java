package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.dataformat.ron.generator.RONGenerator;

import java.io.IOException;

/**
 * Serialize RON structs. (Adapted from Jackson's BeanSerializer and BeanSerializerBase.)
 */
class RONStructSerializer extends RONSerializer<Object> {

    private final BeanPropertyWriter[] properties;

    public RONStructSerializer(BeanPropertyWriter[] properties) {
        this.properties = properties;
    }

    @Override
    public final void serialize(Object bean, RONGenerator gen, SerializerProvider provider) throws IOException {
        // TODO proper Jackson name lookup
        final String name = bean.getClass().getSimpleName();
        gen.writeStartStruct(name);

        serializeFields(bean, gen, provider);

        gen.writeEndStruct();
    }

    private void serializeFields(Object bean, RONGenerator gen, SerializerProvider provider) throws IOException {
        for (BeanPropertyWriter prop: this.properties) {
                if (prop != null) { // can have nulls in filtered list
                    try {
                        prop.serializeAsField(bean, gen, provider);
                    } catch (Exception e) {
                        String name = prop.getName();
                        throw new JsonGenerationException("Could not serialize field '" + name + "'", gen);
                    }
                }
            }

    }
}
