package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class ObjectGeneratorTest extends ContainerTest {
    @Override
    public void testEmpty() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeStartObject();
            generator.writeEndObject();
        }
        assertEquals("{}", w.toString());
    }

    @Override
    public void testOne() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeStartObject();
            generator.writeFieldName("foo");
            generator.writeNumber(1);
            generator.writeEndObject();
        }
        assertEquals("{\"foo\":1}", w.toString());
    }

    @Override
    public void testMultiple() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeStartObject();
            generator.writeFieldName("foo");
            generator.writeNumber(1);
            generator.writeFieldName("bar");
            generator.writeNumber(2);
            generator.writeEndObject();
        }
        assertEquals("{\"foo\":1,\"bar\":2}", w.toString());
    }

    @Test
    public void testComplexFieldName() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeStartObject();
            generator.writeFieldName("foo bar");
            generator.writeNumber(1);
            generator.writeEndObject();
        }
        assertEquals("{\"foo bar\":1}", w.toString());
    }
}
