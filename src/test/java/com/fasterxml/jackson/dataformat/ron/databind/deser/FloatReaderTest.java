package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.ron.FloatTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class FloatReaderTest extends FloatTest {

    @Override
    public void testBigDecimal() throws JsonProcessingException {
        BigDecimal bd = new RONMapper().readValue("1.23", BigDecimal.class);
        assertEquals(BigDecimal.valueOf(1.23), bd);
    }

    @Override
    public void testDouble() throws JsonProcessingException {
        double d = new RONMapper().readValue("1.23", Double.class);
        assertEquals(1.23, d, 0.0001);
    }

    @Override
    public void testFloat() throws JsonProcessingException {
        float f = new RONMapper().readValue("1.23", Float.class);
        assertEquals(1.23f, f, 0.0001f);
    }
}
