package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class ScalarParserTest {

    @Test
    public void testString() throws IOException {
        Reader ron = new StringReader("\"hello\"");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals("hello", parser.nextTextValue());
        }
    }

    @Test
    public void testInt() throws IOException {
        Reader ron = new StringReader("123");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(123, parser.nextIntValue(-1));
        }
    }

    @Test
    public void testFloat() throws IOException {
        Reader ron = new StringReader("1.23");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            // there is no parser.nextFloatValue()
            assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        }
    }

    @Test
    public void testBoolean() throws IOException {
        try (RONParser parser = new RONFactory().createParser(new StringReader("true"))) {
            assertTrue(parser.nextBooleanValue());
        }

        try (RONParser parser = new RONFactory().createParser(new StringReader("false"))) {
            assertFalse(parser.nextBooleanValue());
        }
    }
}