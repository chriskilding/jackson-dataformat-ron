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
