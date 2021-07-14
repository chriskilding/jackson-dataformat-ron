package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.dataformat.ron.FloatTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class FloatWriterTest extends FloatTest {

    @Override
    public void testDouble() throws IOException {
        String ron = new RONMapper().writeValueAsString(1.23d);
        assertEquals("1.23", ron);
    }

    @Override
    public void testFloat() throws IOException {
        String ron = new RONMapper().writeValueAsString(1.23f);
        assertEquals("1.23", ron);
    }

    @Override
    public void testBigDecimal() throws IOException {
        String ron = new RONMapper().writeValueAsString(BigDecimal.valueOf(1.23));
        assertEquals("1.23", ron);
    }
}
