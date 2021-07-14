package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.dataformat.ron.NanTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class NanReaderTest extends NanTest {
    @Override
    public void testFloat() throws IOException {
        float i = new RONMapper().readValue("NaN", Float.class);
        assertEquals(Float.NaN, i, 0.0001);
    }

    @Override
    public void testDouble() throws IOException {
        double i = new RONMapper().readValue("NaN", Double.class);
        assertEquals(Double.NaN, i, 0.0001);
    }
}
