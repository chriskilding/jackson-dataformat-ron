package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.IntTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class IntGeneratorTest extends IntTest {
    @Override
    public void testInt() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(123);
        }
        assertEquals("123", w.toString());
    }

    @Override
    public void testLong() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(123L);
        }
        assertEquals("123", w.toString());
    }

    @Override
    public void testBigInteger() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(BigInteger.ONE);
        }
        assertEquals("1", w.toString());
    }
}
