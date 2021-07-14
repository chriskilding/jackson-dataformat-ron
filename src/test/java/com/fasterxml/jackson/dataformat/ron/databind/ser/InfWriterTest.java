package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.dataformat.ron.InfTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class InfWriterTest extends InfTest {
    @Override
    public void testFloat() throws IOException {
        String ron = new RONMapper().writeValueAsString(Float.POSITIVE_INFINITY);
        assertEquals("inf", ron);
    }

    @Override
    public void testDouble() throws IOException {
        String ron = new RONMapper().writeValueAsString(Double.POSITIVE_INFINITY);
        assertEquals("inf", ron);
    }
}
