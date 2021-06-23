package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.ron.generator.RONGenerator;

import java.io.IOException;

/**
 * RON equivalent of the Jackson StdSerializer base class.
 * @param <T>
 */
abstract class RONSerializer<T> extends JsonSerializer<T> {

    /**
     * Check if the generator is a RONGenerator, downcast it, then delegate to the relevant method in this class.
     */
    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (!(gen instanceof RONGenerator)) {
            throw new JsonGenerationException("Generator was not an instance of RONGenerator - cannot continue serialization", gen);
        }
        RONGenerator generator = (RONGenerator) gen;
        this.serialize(value, generator, serializers);
    }

    /**
     * Serialize the value using the RONGenerator.
     */
    public abstract void serialize(T value, RONGenerator gen, SerializerProvider serializers)
            throws IOException;
}
