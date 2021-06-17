package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.fasterxml.jackson.dataformat.ron.generator.RONGenerator;

import java.io.IOException;

/**
 * Serialize RON enums.
 */
public class EnumSerializer extends RONSerializer<Enum<?>> {

    protected final EnumValues _values;

    public EnumSerializer(EnumValues v) {
        _values = v;
    }

    @Override
    public final void serialize(Enum<?> en, RONGenerator gen, SerializerProvider serializers)
            throws IOException {

        // TODO proper name lookup
        final String name = en.getDeclaringClass().getName();

        boolean hasChildFields = false;
        if (hasChildFields) {
            gen.writeStartEnum(name);
            // TODO write child fields
            gen.writeEndEnum();
        } else {
            gen.writeEnum(name);
        }
    }
}
