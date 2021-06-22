package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.ron.databind.ser.RONStructSerializer;
import com.fasterxml.jackson.dataformat.ron.generator.RONWriteFeature;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StructWriterTest {

    @Test
    public void testWriteStruct() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        Book book = new Book(true, 1);
        String ron = mapper.writeValueAsString(book);
        assertEquals("(abridged:true,numberOfPages:1)", ron);
    }

    @Test
    public void testWriteNamedStruct() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        Book book = new Book(true, 1);
        String ron = mapper.writeValueAsString(book);
        assertEquals("Book(abridged:true,numberOfPages:1)", ron);
    }
}
