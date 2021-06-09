package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class ArrayTest extends ContainerTest {
    @Override
    public void empty() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeStartArray();
            generator.writeEndArray();
        }
        assertEquals("[]", w.toString());
    }

    @Override
    public void one() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeStartArray();
            generator.writeNumber(1);
            generator.writeEndArray();
        }
        assertEquals("[1]", w.toString());
    }

    @Override
    public void multiple() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeStartArray();
            generator.writeNumber(1);
            generator.writeNumber(2);
            generator.writeNumber(3);
            generator.writeEndArray();
        }
        assertEquals("[1,2,3]", w.toString());
    }
}
