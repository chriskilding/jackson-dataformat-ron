package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.dataformat.ron.generator.RONGenerator;

import java.io.IOException;

/**
 * Serialize RON tuples.
 */
public class RONTupleSerializer extends RONSerializer<Object[]> {
    public RONTupleSerializer() {

    }

    @Override
    public void serialize(Object[] value, RONGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartTuple();
        serializeContents(value, gen, serializers);
        gen.writeEndTuple();
    }

    private void serializeContents(Object[] value, RONGenerator gen, SerializerProvider provider) {
//        final int len = value.length;
//        if (len == 0) {
//            return;
//        }
//        if (_elementSerializer != null) {
//            serializeContentsUsing(value, gen, provider, _elementSerializer);
//            return;
//        }
//        if (_valueTypeSerializer != null) {
//            serializeTypedContents(value, gen, provider);
//            return;
//        }
//        int i = 0;
//        Object elem = null;
//        try {
//            PropertySerializerMap serializers = _dynamicSerializers;
//            for (; i < len; ++i) {
//                elem = value[i];
//                if (elem == null) {
//                    provider.defaultSerializeNull(gen);
//                    continue;
//                }
//                Class<?> cc = elem.getClass();
//                JsonSerializer<Object> serializer = serializers.serializerFor(cc);
//                if (serializer == null) {
//                    if (_elementType.hasGenericTypes()) {
//                        serializer = _findAndAddDynamic(serializers,
//                                provider.constructSpecializedType(_elementType, cc), provider);
//                    } else {
//                        serializer = _findAndAddDynamic(serializers, cc, provider);
//                    }
//                }
//                serializer.serialize(elem, gen, provider);
//            }
//        } catch (Exception e) {
//            wrapAndThrow(provider, e, elem, i);
//        }
    }
}
