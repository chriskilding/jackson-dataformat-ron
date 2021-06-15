package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import com.fasterxml.jackson.dataformat.ron.RONGenerator;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class EnumTest extends ContainerTest {

    @Override
    public void testEmpty() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeStartEnum("Foo");
            generator.writeEndEnum();
        }
        assertEquals("Foo()", w.toString());
    }

    @Override
    public void testOne() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeStartEnum("Foo");
            generator.writeNumber(1);
            generator.writeEndEnum();
        }
        assertEquals("Foo(1)", w.toString());
    }

    @Override
    public void testMultiple() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeStartEnum("Foo");
            generator.writeNumber(1);
            generator.writeNumber(2);
            generator.writeEndEnum();
        }
        assertEquals("Foo(1,2)", w.toString());
    }

    public enum Example {
        Foo, Bar, Baz
    }
}
