package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class ArrayParserTest extends ContainerTest {

    @Override
    public void testEmpty() throws IOException {
        Reader ron = new StringReader("[]");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(JsonToken.START_ARRAY, parser.nextToken());
            assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        }
    }

    @Override
    public void testOne() throws IOException {
        Reader ron = new StringReader("[1]");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(JsonToken.START_ARRAY, parser.nextToken());
            assertEquals(1, parser.nextIntValue(-1));
            assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        }
    }

    @Override
    public void testMultiple() throws IOException {
        Reader ron = new StringReader("[1,2,3]");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(JsonToken.START_ARRAY, parser.nextToken());
            assertEquals(1, parser.nextIntValue(-1));
            assertEquals(2, parser.nextIntValue(-1));
            assertEquals(3, parser.nextIntValue(-1));
            assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        }
    }
}
