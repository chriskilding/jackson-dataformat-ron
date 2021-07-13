package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import com.fasterxml.jackson.dataformat.ron.ScalarTest;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class ScalarParserTest extends ScalarTest {

    @Override
    public void testString() throws IOException {
        Reader ron = new StringReader("\"hello\"");

        try (JsonParser parser = new RONFactory().createParser(ron)) {
            assertEquals("hello", parser.nextTextValue());
        }
    }

    @Override
    public void testInt() throws IOException {
        Reader ron = new StringReader("123");

        try (JsonParser parser = new RONFactory().createParser(ron)) {
            assertEquals(123, parser.nextIntValue(-1));
        }
    }

    @Override
    public void testLong() throws IOException {
        Reader ron = new StringReader("123");

        try (JsonParser parser = new RONFactory().createParser(ron)) {
            assertEquals(123, parser.nextLongValue(-1));
        }
    }

    @Override
    public void testFloat() throws IOException {
        Reader ron = new StringReader("1.23");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(1.23f, parser.nextFloatValue(-1), 0.0001);
        }
    }

    @Override
    public void testBigInteger() throws IOException {
        Reader ron = new StringReader("123");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(BigInteger.valueOf(123), parser.nextBigIntegerValue());
        }
    }

    @Override
    public void testBigDecimal() throws IOException {
        Reader ron = new StringReader("1.23");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(BigDecimal.valueOf(1.23), parser.nextBigDecimalValue());
        }
    }

    @Override
    public void testDouble() throws IOException {
        Reader ron = new StringReader("1.23");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(1.23d, parser.nextDoubleValue(-1), 0.0001);
        }
    }

    @Override
    public void testTrue() throws IOException {
        final StringReader ron = new StringReader("true");

        try (JsonParser parser = new RONFactory().createParser(ron)) {
            assertTrue(parser.nextBooleanValue());
        }
    }

    @Override
    public void testFalse() throws IOException {
        final StringReader ron = new StringReader("false");

        try (JsonParser parser = new RONFactory().createParser(ron)) {
            assertFalse(parser.nextBooleanValue());
        }
    }

    @Override
    public void testInf() throws IOException {
        Reader ron = new StringReader("inf");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(Float.POSITIVE_INFINITY, parser.nextFloatValue(-1), 0.0001);
        }
    }

    @Override
    public void testMinusInf() throws IOException {
        Reader ron = new StringReader("-inf");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(Float.NEGATIVE_INFINITY, parser.nextFloatValue(-1), 0.0001);
        }
    }

    @Override
    public void testNan() throws IOException {
        Reader ron = new StringReader("NaN");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(Float.NaN, parser.nextFloatValue(-1), 0.0001);
        }
    }

    /**
     * RON does not have null support. Instead the token 'null' will be parsed like any other arbitrary string token.
     */
    @Test
    public void testNull() throws IOException {
        final StringReader ron = new StringReader("null");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals("null", parser.nextIdentifier());
        }
    }
}
