package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.dataformat.ron.EnumTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumParserTest extends EnumTest {

    @Override
    public void testSimple() throws IOException {
        Reader ron = new StringReader("Foo");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals("Foo", parser.nextIdentifier());
        }
    }

    @Override
    public void testComplex() throws IOException {
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
