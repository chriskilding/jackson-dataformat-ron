package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.fasterxml.jackson.dataformat.ron.generator.RONGenerator;

import java.io.IOException;

/**
 * Serialize RON enums.
 */
class RONEnumSerializer extends RONSerializer<Enum<?>> {

    /**
     * This map contains pre-resolved values (since there are ways
     * to customize actual String constants to use) to use as
     * serializations.
     */
    protected final EnumValues _values;

    public RONEnumSerializer(EnumValues v) {
        _values = v;
    }

    @SuppressWarnings("unchecked")
    public static RONEnumSerializer construct(Class<?> enumClass, SerializationConfig config) {
        EnumValues v = EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass);
        return new RONEnumSerializer(v);
    }

    @Override
    public final void serialize(Enum<?> en, RONGenerator gen, SerializerProvider serializers)
            throws IOException {

        gen.writeStartEnum(_values.serializedValueFor(en).getValue());
        // TODO write child fields
        gen.writeEndEnum();
    }
}
