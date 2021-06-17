package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.ron.generator.RONGenerator;

import java.io.IOException;

public abstract class RONSerializer<T> {
    public abstract void serialize(T value, RONGenerator gen, SerializerProvider serializers)
            throws IOException;
}
