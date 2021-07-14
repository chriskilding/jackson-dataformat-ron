package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.dataformat.ron.ScalarTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ScalarWriterTest extends ScalarTest {
    @Override
    public void testTrue() throws IOException {
        String ron = new RONMapper().writeValueAsString(true);
        assertEquals("true", ron);
    }

    @Override
    public void testFalse() throws IOException {
        String ron = new RONMapper().writeValueAsString(false);
        assertEquals("false", ron);
    }

    @Override
    public void testString() throws IOException {
        String ron = new RONMapper().writeValueAsString("foo");
        assertEquals("\"foo\"", ron);
    }

}
