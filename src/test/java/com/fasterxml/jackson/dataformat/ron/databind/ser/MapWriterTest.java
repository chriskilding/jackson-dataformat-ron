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

public class MapWriterTest extends ContainerTest {

    @Override
    public void testEmpty() throws IOException {
        RONMapper mapper = new RONMapper();
        Map<String, Object> book = new LinkedHashMap<>();
        String ron = mapper.writeValueAsString(book);
        assertEquals("{}", ron);
    }

    @Override
    public void testOne() throws IOException {
        RONMapper mapper = new RONMapper();
        Map<String, Object> book = new LinkedHashMap<>();
        book.put("numberOfPages", 1);
        String ron = mapper.writeValueAsString(book);
        assertEquals("{\"numberOfPages\":1}", ron);
    }

    @Override
    public void testMultiple() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        Map<String, Object> book = new LinkedHashMap<>();
        book.put("abridged", true);
        book.put("numberOfPages", 1);
        String ron = mapper.writeValueAsString(book);
        assertEquals("{\"abridged\":true,\"numberOfPages\":1}", ron);
    }

    @Test
    public void testNestedRonEntities() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        Map<String, Cat> map = new LinkedHashMap<>();
        map.put("cat", new Cat(true, 2));
        String ron = mapper.writeValueAsString(map);
        assertEquals("{\"cat\":Cat(happy:true,meows:2)}", ron);
    }
}
