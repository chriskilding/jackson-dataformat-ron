package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Example;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnumWriterTest {

    /**
     * Simple Java enums can be serialized directly.
     */
    @Test
    public void testWriteEnum() throws JsonProcessingException {
        Example example = Example.Bar;
        String ron = new RONMapper().writeValueAsString(example);
        assertEquals("Bar", ron);
    }

}
