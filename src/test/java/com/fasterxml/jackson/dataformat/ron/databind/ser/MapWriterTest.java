package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MapWriterTest {

    @Test
    public void testWriteObject() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        Map<String, Object> book = new HashMap<>();
        book.put("abridged", true);
        book.put("numberOfPages", 1);
        String ron = mapper.writeValueAsString(book);
        assertEquals("{\"numberOfPages\":1,\"abridged\":true}", ron);
    }

}
