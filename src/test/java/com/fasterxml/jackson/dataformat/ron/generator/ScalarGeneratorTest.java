package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.RONFactory;
import com.fasterxml.jackson.dataformat.ron.ScalarTest;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class ScalarGeneratorTest extends ScalarTest {

    @Override
    public void testInt() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(123);
        }
        assertEquals("123", w.toString());
    }

    @Override
    public void testLong() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(123L);
        }
        assertEquals("123", w.toString());
    }

    @Override
    public void testTrue() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeBoolean(true);
        }
        assertEquals("true", w.toString());
    }

    @Override
    public void testFalse() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeBoolean(false);
        }
        assertEquals("false", w.toString());
    }

    @Override
    public void testFloat() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(1.23f);
        }
        assertEquals("1.23", w.toString());
    }

    @Override
    public void testInf() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(Float.POSITIVE_INFINITY);
        }
        assertEquals("inf", w.toString());
    }

    @Override
    public void testMinusInf() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(Float.NEGATIVE_INFINITY);
        }
        assertEquals("-inf", w.toString());
    }

    @Override
    public void testNan() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(Float.NaN);
        }
        assertEquals("NaN", w.toString());
    }

    @Override
    public void testDouble() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(1.23d);
        }
        assertEquals("1.23", w.toString());
    }

    @Override
    public void testBigInteger() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(BigInteger.ONE);
        }
        assertEquals("1", w.toString());
    }

    @Override
    public void testBigDecimal() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(BigDecimal.valueOf(1.234));
        }
        assertEquals("1.234", w.toString());
    }

    @Override
    public void testString() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeString("bar");
        }
        assertEquals(q("bar"), w.toString());
    }

    @Test
    public void testStringWithQuote() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeString("foo'");
        }
        assertEquals(q("foo'"), w.toString());
    }

    @Test
    public void testStringWithDoubleQuote() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeString("foo\"");
        }
        assertEquals(q("foo\\\""), w.toString());
    }

    /**
     * RON does not support null values.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNull() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNull();
        }
    }

    /**
     * Quoted string. Wraps the provided string with double quotes.
     */
    private static String q(String str) {
        return "\"" + str + "\"";
    }
}
