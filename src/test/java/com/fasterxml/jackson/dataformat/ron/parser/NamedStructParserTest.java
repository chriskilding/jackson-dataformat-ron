package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class NamedStructParserTest extends ContainerTest {

    @Override
    public void testEmpty() throws IOException {
        Reader ron = new StringReader("Book()");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals("Book", parser.nextIdentifier());
            assertEquals(RONToken.START_TUPLE, parser.nextRONToken());
            assertEquals(RONToken.END_TUPLE, parser.nextRONToken());
        }
    }

    @Override
    public void testOne() throws IOException {
        Reader ron = new StringReader("Book(foo:1)");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals("Book", parser.nextIdentifier());
            assertEquals(RONToken.START_TUPLE, parser.nextRONToken());
            assertEquals("foo", parser.nextIdentifier());
            assertEquals(1, parser.nextIntValue(-1));
            assertEquals(RONToken.END_TUPLE, parser.nextRONToken());
        }
    }

    @Override
    public void testMultiple() throws IOException {
        Reader ron = new StringReader("Book(foo:1,bar:2)");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals("Book", parser.nextIdentifier());
            assertEquals(RONToken.START_TUPLE, parser.nextRONToken());
            assertEquals("foo", parser.nextIdentifier());
            assertEquals(1, parser.nextIntValue(-1));
            assertEquals("bar", parser.nextIdentifier());
            assertEquals(2, parser.nextIntValue(-1));
            assertEquals(RONToken.END_TUPLE, parser.nextRONToken());
        }
    }
}
