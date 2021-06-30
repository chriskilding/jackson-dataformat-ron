package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class ScalarReaderTest {

    @Test
    public void testString() throws JsonProcessingException {
        String str = new RONMapper().readValue("\"foobar\"", String.class);
        assertEquals("foobar", str);
    }

    @Test
    public void testInt() throws JsonProcessingException {
        int i = new RONMapper().readValue("123", Integer.class);
        assertEquals(123, i);
    }

    @Test
    public void testBigInteger() throws JsonProcessingException {
        BigInteger bi = new RONMapper().readValue("123", BigInteger.class);
        assertEquals(BigInteger.valueOf(123), bi);
    }

    @Test
    public void testBigDecimal() throws JsonProcessingException {
        BigDecimal bd = new RONMapper().readValue("1.23", BigDecimal.class);
        assertEquals(BigDecimal.valueOf(1.23), bd);
    }

    @Test
    public void testDouble() throws JsonProcessingException {
        double d = new RONMapper().readValue("1.23", Double.class);
        assertDoubleEquals(1.23, d);
    }

    @Test
    public void testFloat() throws JsonProcessingException {
        float f = new RONMapper().readValue("1.23", Float.class);
        assertEquals(1.23f, f, 0.001f);
    }

    @Test
    public void testInf() throws JsonProcessingException {
        double i = new RONMapper().readValue("inf", Double.class);
        assertDoubleEquals(Double.POSITIVE_INFINITY, i);
    }

    @Test
    public void testMinusInf() throws JsonProcessingException {
        double i = new RONMapper().readValue("-inf", Double.class);
        assertDoubleEquals(Double.NEGATIVE_INFINITY, i);
    }

    @Test
    public void testNaN() throws JsonProcessingException {
        double i = new RONMapper().readValue("NaN", Double.class);
        assertDoubleEquals(Double.NaN, i);
    }

    @Test
    public void testTrue() throws JsonProcessingException {
        boolean b = new RONMapper().readValue("true", Boolean.class);
        assertTrue(b);
    }

    @Test
    public void testFalse() throws JsonProcessingException {
        boolean b = new RONMapper().readValue("false", Boolean.class);
        assertFalse(b);
    }

    private static void assertDoubleEquals(double expected, double actual) {
        assertEquals(expected, actual, 0.0001);
    }
}
