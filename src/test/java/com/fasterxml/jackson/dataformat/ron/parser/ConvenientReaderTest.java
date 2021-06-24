package com.fasterxml.jackson.dataformat.ron.parser;

import org.junit.Test;

import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertTrue;

public class ConvenientReaderTest {

    private static ConvenientReader readerFor(String str) {
        final Reader reader = new StringReader(str);
        return new ConvenientReader(new PushbackReader(reader));
    }

    @Test
    public void testSkipWhitespace() throws Exception {
        try (ConvenientReader reader = readerFor("   }")) {
            reader.skipWhitespace();
            assertTrue(reader.consume("}"));
        }
    }

    @Test
    public void testSkipWhitespaceNewline() throws Exception {
        try (ConvenientReader reader = readerFor("   \n}")) {
            reader.skipWhitespace();
            assertTrue(reader.consume("}"));
        }
    }
}
