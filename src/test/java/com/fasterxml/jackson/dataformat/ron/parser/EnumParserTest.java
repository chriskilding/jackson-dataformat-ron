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

        fail("Unclear what the behavior should be");
    }

    @Override
    public void testOne() throws IOException {
        Reader ron = new StringReader("Foo(1)");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(RONToken.START_ENUM, parser.nextToken());
            assertEquals(1, parser.nextIntValue(-1));
            assertEquals(RONToken.END_ENUM, parser.nextToken());
        }
    }

    @Override
    public void testMultiple() throws IOException {
        Reader ron = new StringReader("Foo(1,true)");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(RONToken.START_ENUM, parser.nextToken());
            assertEquals(1, parser.nextIntValue(-1));
            assertTrue(parser.nextBooleanValue());
            assertEquals(RONToken.END_ENUM, parser.nextToken());
        }
    }

    @Test
    public void testSimple() throws IOException {
        Reader ron = new StringReader("Foo");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(RONToken.ENUM, parser.nextToken());
        }
    }

    @Test
    public void testTrailingComma() throws IOException {
        Reader ron = new StringReader("Foo(1,)");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(RONToken.START_ENUM, parser.nextToken());
            assertEquals(1, parser.nextIntValue(-1));
            assertEquals(RONToken.END_ENUM, parser.nextToken());
        }
    }
}