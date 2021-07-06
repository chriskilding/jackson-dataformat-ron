package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
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

    @Test
    public void testMultiple() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        Map<String, Object> book = new LinkedHashMap<>();
        book.put("abridged", true);
        book.put("numberOfPages", 1);
        String ron = mapper.writeValueAsString(book);
        assertEquals("{\"abridged\":true,\"numberOfPages\":1}", ron);
    }

}
