package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.dataformat.ron.FloatTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class FloatParserTest extends FloatTest {

    private static RONParser newParser(String ron) throws IOException {
        Reader reader = new StringReader(ron);
        return new RONFactory().createParser(reader);
    }

    @Override
    public void testFloat() throws IOException {
        try (RONParser parser = newParser("1.23")) {
            assertEquals(1.23f, parser.nextFloatValue(-1), 0.0001);
        }
    }

    @Test
    public void testFloatDefaultValue() throws IOException {
        try (RONParser parser = newParser("true")) {
            assertEquals(-1, parser.nextFloatValue(-1), 0.0001);
        }
    }

    @Override
    public void testBigDecimal() throws IOException {
        try (RONParser parser = newParser("1.23")) {
            assertEquals(BigDecimal.valueOf(1.23), parser.nextBigDecimalValue());
        }
    }

    @Override
    public void testDouble() throws IOException {
        try (RONParser parser = newParser("1.23")) {
            assertEquals(1.23d, parser.nextDoubleValue(-1), 0.0001);
        }
    }

    @Test
    public void testDoubleDefaultValue() throws IOException {
        try (RONParser parser = newParser("true")) {
            assertEquals(-1, parser.nextDoubleValue(-1), 0.0001);
        }
    }
}
