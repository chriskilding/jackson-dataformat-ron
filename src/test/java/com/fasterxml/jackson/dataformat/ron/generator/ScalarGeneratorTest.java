package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.RONFactory;
import com.fasterxml.jackson.dataformat.ron.ScalarTest;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class ScalarGeneratorTest extends ScalarTest {

    @Override
    public void testTrue() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeBoolean(true);
        }
        assertEquals("true", w.toString());
    }

    @Override
    public void testFalse() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeBoolean(false);
        }
        assertEquals("false", w.toString());
    }

    @Override
    public void testString() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeString("bar");
        }
        assertEquals(q("bar"), w.toString());
    }

    @Test
    public void testStringWithQuote() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeString("foo'");
        }
        assertEquals(q("foo'"), w.toString());
    }

    @Test
    public void testStringWithDoubleQuote() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeString("foo\"");
        }
        assertEquals(q("foo\\\""), w.toString());
    }

    /**
     * RON does not support null values.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNull() throws IOException {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeNull();
        }
    }

    /**
     * Quoted string. Wraps the provided string with double quotes.
     */
    private static String q(String str) {
        return "\"" + str + "\"";
    }
}
