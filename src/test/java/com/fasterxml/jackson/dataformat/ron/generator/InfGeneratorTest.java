package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.InfTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class InfGeneratorTest extends InfTest {

    @Override
    public void testFloat() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(Float.POSITIVE_INFINITY);
        }
        assertEquals("inf", w.toString());
    }

    @Override
    public void testDouble() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(Double.POSITIVE_INFINITY);
        }
        assertEquals("inf", w.toString());
    }
}
