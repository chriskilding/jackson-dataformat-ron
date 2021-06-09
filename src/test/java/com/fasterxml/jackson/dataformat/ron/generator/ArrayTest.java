package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class ArrayTest extends ContainerTest {
    @Override
    public void testEmpty() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeStartArray();
            generator.writeEndArray();
        }
        assertEquals("[]", w.toString());
    }

    @Override
    public void testOne() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeStartArray();
            generator.writeNumber(1);
            generator.writeEndArray();
        }
        assertEquals("[1]", w.toString());
    }

    @Override
    public void testMultiple() throws IOException {
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

    @Test
    public void testHeterogenous() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeStartArray();
            generator.writeNumber(1);
            generator.writeBoolean(true);
            generator.writeEndArray();
        }
        assertEquals("[1,true]", w.toString());
    }
}
