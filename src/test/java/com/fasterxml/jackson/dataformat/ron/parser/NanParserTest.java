package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.dataformat.ron.NanTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class NanParserTest extends NanTest {

    private static RONParser newParser(String ron) throws IOException {
        Reader reader = new StringReader(ron);
        return new RONFactory().createParser(reader);
    }

    @Override
    public void testFloat() throws IOException {
        try (RONParser parser = newParser("NaN")) {
            assertEquals(Float.NaN, parser.nextFloatValue(-1), 0.0001);
        }
    }

    @Override
    public void testDouble() throws IOException {
        try (RONParser parser = newParser("NaN")) {
            assertEquals(Double.NaN, parser.nextDoubleValue(-1), 0.0001);
        }
    }
}
