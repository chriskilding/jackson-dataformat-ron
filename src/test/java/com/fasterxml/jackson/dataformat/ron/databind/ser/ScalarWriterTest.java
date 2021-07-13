package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.dataformat.ron.ScalarTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

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
    public void testBigInteger() throws IOException {
        String ron = new RONMapper().writeValueAsString(BigInteger.valueOf(123));
        assertEquals("123", ron);
    }

    @Override
    public void testBigDecimal() throws IOException {
        String ron = new RONMapper().writeValueAsString(BigDecimal.valueOf(1.23));
        assertEquals("1.23", ron);
    }

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
    public void testString() throws IOException {
        String ron = new RONMapper().writeValueAsString("foo");
        assertEquals("\"foo\"", ron);
    }

    @Override
    public void testInf() throws IOException {
        String ron = new RONMapper().writeValueAsString(Float.POSITIVE_INFINITY);
        assertEquals("inf", ron);
    }

    @Override
    public void testMinusInf() throws IOException {
        String ron = new RONMapper().writeValueAsString(Float.NEGATIVE_INFINITY);
        assertEquals("-inf", ron);
    }

    @Override
    public void testNan() throws IOException {
        String ron = new RONMapper().writeValueAsString(Float.NaN);
        assertEquals("NaN", ron);
    }
}
