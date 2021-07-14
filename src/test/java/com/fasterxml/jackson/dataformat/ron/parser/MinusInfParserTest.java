package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.dataformat.ron.MinusInfTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class MinusInfParserTest extends MinusInfTest {

    private static RONParser newParser(String ron) throws IOException {
        Reader reader = new StringReader(ron);
        return new RONFactory().createParser(reader);
    }

    @Override
    public void testFloat() throws IOException {
        try (RONParser parser = newParser("-inf")) {
            assertEquals(Float.NEGATIVE_INFINITY, parser.nextFloatValue(-1), 0.0001);
        }
    }

    @Override
    public void testDouble() throws IOException {
        try (RONParser parser = newParser("-inf")) {
            assertEquals(Double.NEGATIVE_INFINITY, parser.nextDoubleValue(-1), 0.0001);
        }
    }
}
