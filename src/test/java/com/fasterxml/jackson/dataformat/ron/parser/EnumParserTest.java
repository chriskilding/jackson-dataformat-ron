package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class EnumParserTest extends ContainerTest {

    @Override
    public void testEmpty() throws IOException {
        Reader ron = new StringReader("Foo()");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals("Foo", parser.nextIdentifier());
            assertEquals(RONToken.START_TUPLE, parser.nextRONToken());
            assertEquals(RONToken.END_TUPLE, parser.nextRONToken());
        }
    }

    @Override
    public void testOne() throws IOException {
        Reader ron = new StringReader("Foo(1)");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals("Foo", parser.nextIdentifier());
            assertEquals(RONToken.START_TUPLE, parser.nextRONToken());
            assertEquals(1, parser.nextIntValue(-1));
            assertEquals(RONToken.END_TUPLE, parser.nextRONToken());
        }
    }

    @Override
    public void testMultiple() throws IOException {
        Reader ron = new StringReader("Foo(1,true)");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals("Foo", parser.nextIdentifier());
            assertEquals(RONToken.START_TUPLE, parser.nextRONToken());
            assertEquals(1, parser.nextIntValue(-1));
            assertTrue(parser.nextBooleanValue());
            assertEquals(RONToken.END_TUPLE, parser.nextRONToken());
        }
    }

    @Test
    public void testSimple() throws IOException {
        Reader ron = new StringReader("Foo");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals("Foo", parser.nextIdentifier());
        }
    }

    @Test
    public void testTrailingComma() throws IOException {
        Reader ron = new StringReader("Foo(1,)");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals("Foo", parser.nextIdentifier());
            assertEquals(RONToken.START_TUPLE, parser.nextRONToken());
            assertEquals(1, parser.nextIntValue(-1));
            assertEquals(RONToken.END_TUPLE, parser.nextRONToken());
        }
    }
}
