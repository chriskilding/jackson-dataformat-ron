package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayWriterTest {
    @Test
    public void testWriteArray() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        int[] array = {1, 2};
        String ron = mapper.writeValueAsString(array);
        assertEquals("[1,2]", ron);
    }
}
