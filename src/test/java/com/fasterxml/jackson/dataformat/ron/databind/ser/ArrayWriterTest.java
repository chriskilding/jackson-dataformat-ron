package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ArrayWriterTest extends ContainerTest {

    @Override
    public void testEmpty() throws IOException {
        RONMapper mapper = new RONMapper();
        int[] array = {};
        String ron = mapper.writeValueAsString(array);
        assertEquals("[]", ron);
    }

    @Override
    public void testOne() throws IOException {
        RONMapper mapper = new RONMapper();
        int[] array = {1};
        String ron = mapper.writeValueAsString(array);
        assertEquals("[1]", ron);
    }

    @Override
    public void testMultiple() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        int[] array = {1, 2};
        String ron = mapper.writeValueAsString(array);
        assertEquals("[1,2]", ron);
    }
}
