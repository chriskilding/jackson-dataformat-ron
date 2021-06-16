package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

// TODO figure out what to do here, maybe wire up the serializers
public class RONWriter extends ObjectWriter {
    protected RONWriter(ObjectWriter base, JsonFactory f) {
        super(base, f);
    }

    @Override
    protected DefaultSerializerProvider _serializerProvider() {
        return super._serializerProvider();
    }
}
