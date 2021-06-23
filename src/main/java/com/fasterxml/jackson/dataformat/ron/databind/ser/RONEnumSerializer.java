package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
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

        final String name = _values.serializedValueFor(en).getValue();
        gen.writeEnum(name);
        // TODO write child fields - but note, the standard ObjectMapper cannot do this for enums either
    }
}
