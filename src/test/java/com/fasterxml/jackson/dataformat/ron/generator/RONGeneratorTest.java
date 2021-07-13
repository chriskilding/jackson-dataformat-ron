package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.RONFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RONGeneratorTest {

    private static RONGenerator newGenerator() throws IOException {
        return new RONFactory().createGenerator(new StringWriter());
    }

    /**
     * The RON format currently does not do schemas.
     */
    @Test
    public void shouldNotUseSchemas() throws IOException {
        try (RONGenerator generator = newGenerator()) {
            assertFalse(generator.canUseSchema(null));
        }
    }

    /**
     * RON structs can have optional type names.
     */
    @Test
    public void shouldWriteTypeNames() throws IOException {
        try (RONGenerator generator = newGenerator()) {
            assertTrue(generator.canWriteTypeId());
        }
    }

    @Test(expected = IOException.class)
    public void testEndArrayMismatch() throws IOException {
        try (RONGenerator generator = newGenerator()) {
            generator.writeStartTuple();
            generator.writeEndArray();
        }
    }

    @Test(expected = IOException.class)
    public void testEndStructMismatch() throws IOException {
        try (RONGenerator generator = newGenerator()) {
            generator.writeStartArray();
            generator.writeEndStruct();
        }
    }

    @Test(expected = IOException.class)
    public void testEndObjectMismatch() throws IOException {
        try (RONGenerator generator = newGenerator()) {
            generator.writeStartArray();
            generator.writeEndObject();
        }
    }

    @Test(expected = IOException.class)
    public void testEndTupleMismatch() throws IOException {
        try (RONGenerator generator = newGenerator()) {
            generator.writeStartArray();
            generator.writeEndTuple();
        }
    }

    @Test(expected = IOException.class)
    public void testEndEnumMismatch() throws IOException {
        try (RONGenerator generator = newGenerator()) {
            generator.writeStartArray();
            generator.writeEndEnum();
        }
    }
}
