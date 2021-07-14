package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.dataformat.ron.IntTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class IntParserTest extends IntTest {

    private static RONParser newParser(String ron) throws IOException {
        Reader reader = new StringReader(ron);
        return new RONFactory().createParser(reader);
    }

    @Override
    public void testInt() throws IOException {
        try (JsonParser parser = newParser("123")) {
            assertEquals(123, parser.nextIntValue(-1));
        }
    }

    @Test
    public void testIntDefaultValue() throws IOException {
        try (JsonParser parser = newParser("true")) {
            assertEquals(-1, parser.nextIntValue(-1));
        }
    }

    @Override
    public void testLong() throws IOException {
        try (JsonParser parser = newParser("123")) {
            assertEquals(123, parser.nextLongValue(-1));
        }
    }

    @Test
    public void testLongDefaultValue() throws IOException {
        try (JsonParser parser = newParser("true")) {
            assertEquals(-1, parser.nextLongValue(-1));
        }
    }

    @Override
    public void testBigInteger() throws IOException {
        try (RONParser parser = newParser("123")) {
            assertEquals(BigInteger.valueOf(123), parser.nextBigIntegerValue());
        }
    }
}
