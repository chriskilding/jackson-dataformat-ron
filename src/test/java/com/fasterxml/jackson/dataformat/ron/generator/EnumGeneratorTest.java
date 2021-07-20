package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.EnumTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class EnumGeneratorTest extends EnumTest {

    @Override
    public void testSimple() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeEnum("Foo");
        }
        assertEquals("Foo", w.toString());
    }

    @Override
    public void testComplex() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeStartEnum("Foo");
            generator.writeNumber(1);
            generator.writeNumber(2);
            generator.writeEndEnum();
        }
        assertEquals("Foo(1,2)", w.toString());
    }
}
