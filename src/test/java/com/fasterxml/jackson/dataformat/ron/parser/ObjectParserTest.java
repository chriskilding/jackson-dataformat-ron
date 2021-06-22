package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class ObjectParserTest extends ContainerTest {

    @Override
    public void testEmpty() throws IOException {
        Reader ron = new StringReader("{}");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        }
    }

    @Override
    public void testOne() throws IOException {
        Reader ron = new StringReader("{\"familyName\":\"Joe\"}");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertEquals("familyName", parser.nextFieldName());
            assertEquals("Joe", parser.nextTextValue());
            assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        }
    }

    @Override
    public void testMultiple() throws IOException {
        Reader ron = new StringReader("{\"familyName\":\"Joe\",\"givenName\":\"Bloggs\"}");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertEquals("familyName", parser.nextFieldName());
            assertEquals("Joe", parser.nextTextValue());
            assertEquals("givenName", parser.nextFieldName());
            assertEquals("Bloggs", parser.nextTextValue());
            assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        }
    }

    @Test
    public void testTrailingComma() throws IOException {
        Reader ron = new StringReader("{\"familyName\":\"Joe\",}");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertEquals("familyName", parser.nextFieldName());
            assertEquals("Joe", parser.nextTextValue());
            assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        }
    }

}
