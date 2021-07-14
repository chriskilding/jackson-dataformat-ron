package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.FloatTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class FloatGeneratorTest extends FloatTest {

    @Override
    public void testFloat() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(1.23f);
        }
        assertEquals("1.23", w.toString());
    }

    @Override
    public void testDouble() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(1.23d);
        }
        assertEquals("1.23", w.toString());
    }

    @Override
    public void testBigDecimal() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNumber(BigDecimal.valueOf(1.234));
        }
        assertEquals("1.234", w.toString());
    }
}
