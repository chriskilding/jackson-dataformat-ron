package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.ron.ScalarTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import static org.junit.Assert.*;

public class ScalarReaderTest extends ScalarTest {

    @Override
    public void testString() throws JsonProcessingException {
        String str = new RONMapper().readValue("\"foobar\"", String.class);
        assertEquals("foobar", str);
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

}
