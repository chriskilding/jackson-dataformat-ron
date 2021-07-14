package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.NanTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NanGeneratorTest extends NanTest {
    @Override
    public void testFloat() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(Float.NaN);
        }
        assertEquals("NaN", w.toString());
    }

    @Override
    public void testDouble() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(Double.NaN);
        }
        assertEquals("NaN", w.toString());
    }
}
