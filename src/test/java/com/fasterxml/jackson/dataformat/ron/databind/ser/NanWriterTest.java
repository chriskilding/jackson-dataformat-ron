package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.dataformat.ron.NanTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class NanWriterTest extends NanTest {
    @Override
    public void testFloat() throws IOException {
        String ron = new RONMapper().writeValueAsString(Float.NaN);
        assertEquals("NaN", ron);
    }

    @Override
    public void testDouble() throws IOException {
        String ron = new RONMapper().writeValueAsString(Double.NaN);
        assertEquals("NaN", ron);
    }
}
