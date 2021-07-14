package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.dataformat.ron.IntTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class IntWriterTest extends IntTest {
    @Override
    public void testInt() throws IOException {
        String ron = new RONMapper().writeValueAsString(123);
        assertEquals("123", ron);
    }

    @Override
    public void testLong() throws IOException {
        String ron = new RONMapper().writeValueAsString(123L);
        assertEquals("123", ron);
    }

    @Override
    public void testBigInteger() throws IOException {
        String ron = new RONMapper().writeValueAsString(BigInteger.valueOf(123));
        assertEquals("123", ron);
    }
}
