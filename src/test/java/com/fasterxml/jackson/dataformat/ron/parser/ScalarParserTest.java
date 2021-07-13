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

    private static RONParser newParser(String ron) throws IOException {
        Reader reader = new StringReader(ron);
        return new RONFactory().createParser(reader);
    }

    @Override
    public void testString() throws IOException {
        try (JsonParser parser = newParser("\"hello\"")) {
            assertEquals("hello", parser.nextTextValue());
        }
    }

    @Override
    public void testInt() throws IOException {
        try (JsonParser parser = newParser("123")) {
            assertEquals(123, parser.nextIntValue(-1));
        }
    }

    @Test
    public void testIntDefaultValue() throws IOException {
        try (JsonParser parser = newParser("true")) {
            assertEquals(-1, parser.nextIntValue(-1));
        }
    }

    @Override
    public void testLong() throws IOException {
        try (JsonParser parser = newParser("123")) {
            assertEquals(123, parser.nextLongValue(-1));
        }
    }

    @Test
    public void testLongDefaultValue() throws IOException {
        try (JsonParser parser = newParser("true")) {
            assertEquals(-1, parser.nextLongValue(-1));
        }
    }

    @Override
    public void testFloat() throws IOException {
        try (RONParser parser = newParser("1.23")) {
            assertEquals(1.23f, parser.nextFloatValue(-1), 0.0001);
        }
    }

    @Test
    public void testFloatDefaultValue() throws IOException {
        try (RONParser parser = newParser("true")) {
            assertEquals(-1, parser.nextFloatValue(-1), 0.0001);
        }
    }

    @Override
    public void testBigInteger() throws IOException {
        try (RONParser parser = newParser("123")) {
            assertEquals(BigInteger.valueOf(123), parser.nextBigIntegerValue());
        }
    }

    @Override
    public void testBigDecimal() throws IOException {
        try (RONParser parser = newParser("1.23")) {
            assertEquals(BigDecimal.valueOf(1.23), parser.nextBigDecimalValue());
        }
    }

    @Override
    public void testDouble() throws IOException {
        try (RONParser parser = newParser("1.23")) {
            assertEquals(1.23d, parser.nextDoubleValue(-1), 0.0001);
        }
    }

    @Test
    public void testDoubleDefaultValue() throws IOException {
        try (RONParser parser = newParser("true")) {
            assertEquals(-1, parser.nextDoubleValue(-1), 0.0001);
        }
    }

    @Override
    public void testTrue() throws IOException {
        try (JsonParser parser = newParser("true")) {
            assertTrue(parser.nextBooleanValue());
        }
    }

    @Override
    public void testFalse() throws IOException {
        try (JsonParser parser = newParser("false")) {
            assertFalse(parser.nextBooleanValue());
        }
    }

    @Override
    public void testInf() throws IOException {
        try (RONParser parser = newParser("inf")) {
            assertEquals(Float.POSITIVE_INFINITY, parser.nextFloatValue(-1), 0.0001);
        }
    }

    @Override
    public void testMinusInf() throws IOException {
        try (RONParser parser = newParser("-inf")) {
            assertEquals(Float.NEGATIVE_INFINITY, parser.nextFloatValue(-1), 0.0001);
        }
    }

    @Override
    public void testNan() throws IOException {
        try (RONParser parser = newParser("NaN")) {
            assertEquals(Float.NaN, parser.nextFloatValue(-1), 0.0001);
        }
    }

    /**
     * RON does not have null support. Instead the token 'null' will be parsed like any other arbitrary string token.
     */
    @Test
    public void testNull() throws IOException {
        try (RONParser parser = newParser("null")) {
            assertEquals("null", parser.nextIdentifier());
        }
    }
}
