package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NamedStructGeneratorTest extends ContainerTest {
    @Override
    public void testEmpty() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeStartStruct("Example");
            generator.writeEndStruct();
        }
        assertEquals("Example()", w.toString());
    }

    @Override
    public void testOne() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeStartStruct("Example");
            generator.writeFieldName("foo");
            generator.writeNumber(1);
            generator.writeEndStruct();
        }
        assertEquals("Example(foo:1)", w.toString());
    }

    @Override
    public void testMultiple() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeStartStruct("Example");
            generator.writeFieldName("foo");
            generator.writeNumber(1);
            generator.writeFieldName("bar");
            generator.writeNumber(2);
            generator.writeEndStruct();
        }
        assertEquals("Example(foo:1,bar:2)", w.toString());
    }
}
