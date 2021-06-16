package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.RONFactory;
import com.fasterxml.jackson.dataformat.ron.RONGenerator;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ScalarTest {

    @Test
    public void testInt() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(123);
        }
        assertEquals("123", w.toString());
    }

    @Test
    public void testBoolean() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeBoolean(true);
        }
        assertEquals("true", w.toString());
    }

    @Test
    public void testFloat() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(1.23);
        }
        assertEquals("1.23", w.toString());
    }

    @Test
    public void testBigInteger() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(BigInteger.ONE);
        }
        assertEquals("1", w.toString());
    }

    @Test
    public void testBigDecimal() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(BigDecimal.valueOf(1.234));
        }
        assertEquals("1.234", w.toString());
    }

    @Test
    public void testBinary() throws IOException {
        fail("Behavior not yet known");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNull() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNull();
        }
    }

    @Test
    public void testString() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeString("bar");
        }
        assertEquals("\"bar\"", w.toString());
    }

    @Test
    public void testStringWithQuote() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeString("foo'");
        }
        assertEquals("\"foo'\"", w.toString());
    }

    @Test
    public void testStringWithDoubleQuote() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeString("foo\"");
        }
        assertEquals("\"foo\\\"", w.toString());
    }

    @Test
    public void testStringWithUnicodeControlChar() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeString("foo\u0001");
        }
        assertEquals("\"foo\\u0001\"", w.toString());
    }

    @Test
    public void testStringWithControlChar() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeString("foo\b");
        }
        assertEquals("\"foo\\b\"", w.toString());
    }
}
