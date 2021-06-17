package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class StructParserTest extends ContainerTest {

    @Override
    public void testEmpty() throws IOException {
        Reader ron = new StringReader("()");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(RONToken.START_STRUCT, parser.nextToken());
            assertEquals(RONToken.END_STRUCT, parser.nextToken());
        }
    }

    @Override
    public void testOne() throws IOException {
        Reader ron = new StringReader("(foo:1)");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(RONToken.START_STRUCT, parser.nextToken());
            assertEquals("foo", parser.nextFieldName());
            assertEquals(1, parser.nextIntValue(-1));
            assertEquals(RONToken.END_STRUCT, parser.nextToken());
        }
    }

    @Override
    public void testMultiple() throws IOException {
        Reader ron = new StringReader("(foo:1,bar:2)");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(RONToken.START_STRUCT, parser.nextToken());
            assertEquals("foo", parser.nextFieldName());
            assertEquals(1, parser.nextIntValue(-1));
            assertEquals("bar", parser.nextFieldName());
            assertEquals(2, parser.nextIntValue(-1));
            assertEquals(RONToken.END_STRUCT, parser.nextToken());
        }
    }
}