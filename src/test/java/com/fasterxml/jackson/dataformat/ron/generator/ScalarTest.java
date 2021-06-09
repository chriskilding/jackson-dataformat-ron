package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ScalarTest {

    @Test
    public void testInt() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeNumber(123);
        }
        assertEquals("123", w.toString());
    }

    @Test
    public void testBoolean() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeBoolean(true);
        }
        assertEquals("true", w.toString());
    }

    @Test
    public void testFloat() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeNumber(1.23);
        }
        assertEquals("1.23", w.toString());
    }

    @Test
    public void testBinary() throws IOException {
        fail("Behavior not yet known");
    }

    @Test
    public void testNull() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeNull();
        }
        fail("Behavior not yet known");
    }

    @Test
    public void testString() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeString("bar");
        }
        assertEquals("\"bar\"", w.toString());
    }

    @Test
    public void testStringWithQuote() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeString("foo'");
        }
        assertEquals("\"foo'\"", w.toString());
    }

    @Test
    public void testStringWithDoubleQuote() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeString("foo\"");
        }
        assertEquals("\"foo\\\"", w.toString());
    }

    @Test
    public void testStringWithUnicodeControlChar() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeString("foo\u0001");
        }
        assertEquals("\"foo\\u0001\"", w.toString());
    }

    @Test
    public void testStringWithControlChar() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeString("foo\b");
        }
        assertEquals("\"foo\\b\"", w.toString());
    }
}
