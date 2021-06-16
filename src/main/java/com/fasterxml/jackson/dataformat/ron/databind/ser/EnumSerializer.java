package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.fasterxml.jackson.dataformat.ron.RONGenerator;

import java.io.IOException;

/**
 * Serialize RON enums.
 */
public class EnumSerializer extends RONSerializer<Enum<?>> {

    protected final EnumValues _values;

    protected final Boolean _serializeAsIndex;

    public EnumSerializer(EnumValues v, Boolean serializeAsIndex) {
        _values = v;
        _serializeAsIndex = serializeAsIndex;
    }

    @Override
    public final void serialize(Enum<?> en, RONGenerator gen, SerializerProvider serializers)
            throws IOException {

        final String name = _values.serializedValueFor(en).getValue();

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
