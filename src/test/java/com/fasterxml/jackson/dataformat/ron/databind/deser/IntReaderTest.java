package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.ron.IntTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class IntReaderTest extends IntTest {

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
}
