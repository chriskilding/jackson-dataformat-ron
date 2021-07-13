package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import com.fasterxml.jackson.dataformat.ron.databind.examples.animal.Cat;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

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

    @Test
    public void testNestedRonEntities() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        Cat[] array = {new Cat(true, 2)};
        String ron = mapper.writeValueAsString(array);
        assertEquals("[Cat(happy:true,meows:2)]", ron);
    }
}
