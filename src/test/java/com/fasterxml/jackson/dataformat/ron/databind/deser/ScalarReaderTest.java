package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.ron.ScalarTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class ScalarReaderTest extends ScalarTest {

    @Override
    public void testString() throws JsonProcessingException {
        String str = new RONMapper().readValue("\"foobar\"", String.class);
        assertEquals("foobar", str);
    }

    @Override
    public void testInt() throws JsonProcessingException {
        int i = new RONMapper().readValue("123", Integer.class);
        assertEquals(123, i);
    }

    @Override
    public void testLong() throws JsonProcessingException {
        long l = new RONMapper().readValue("123", Long.class);
        assertEquals(123, l);
    }

    @Override
    public void testBigInteger() throws JsonProcessingException {
        BigInteger bi = new RONMapper().readValue("123", BigInteger.class);
        assertEquals(BigInteger.valueOf(123), bi);
    }

    @Override
    public void testBigDecimal() throws JsonProcessingException {
        BigDecimal bd = new RONMapper().readValue("1.23", BigDecimal.class);
        assertEquals(BigDecimal.valueOf(1.23), bd);
    }

    @Override
    public void testDouble() throws JsonProcessingException {
        double d = new RONMapper().readValue("1.23", Double.class);
        assertDoubleEquals(1.23, d);
    }

    @Override
    public void testFloat() throws JsonProcessingException {
        float f = new RONMapper().readValue("1.23", Float.class);
        assertEquals(1.23f, f, 0.001f);
    }

    @Override
    public void testInf() throws JsonProcessingException {
        double inf = new RONMapper().readValue("inf", Double.class);
        assertDoubleEquals(Double.POSITIVE_INFINITY, inf);
    }

    @Override
    public void testMinusInf() throws JsonProcessingException {
        double i = new RONMapper().readValue("-inf", Double.class);
        assertDoubleEquals(Double.NEGATIVE_INFINITY, i);
    }

    @Override
    public void testNan() throws JsonProcessingException {
        double i = new RONMapper().readValue("NaN", Double.class);
        assertDoubleEquals(Double.NaN, i);
    }

    @Override
    public void testTrue() throws JsonProcessingException {
        boolean t = new RONMapper().readValue("true", Boolean.class);
        assertTrue(t);
    }

    @Override
    public void testFalse() throws JsonProcessingException {
        boolean f = new RONMapper().readValue("false", Boolean.class);
        assertFalse(f);
    }

    private static void assertDoubleEquals(double expected, double actual) {
        assertEquals(expected, actual, 0.0001);
    }
}
