package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.dataformat.ron.InfTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MinusInfReaderTest extends InfTest {
    @Override
    public void testFloat() throws IOException {
        float inf = new RONMapper().readValue("-inf", Float.class);
        assertEquals(Float.NEGATIVE_INFINITY, inf, 0.0001);
    }

    @Override
    public void testDouble() throws IOException {
        double inf = new RONMapper().readValue("-inf", Double.class);
        assertEquals(Double.NEGATIVE_INFINITY, inf, 0.0001);
    }
}
