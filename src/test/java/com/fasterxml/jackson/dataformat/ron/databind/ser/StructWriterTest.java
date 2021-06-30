package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Book;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StructWriterTest {

    @Test
    public void testWriteStruct() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        Book book = new Book(true, 1);
        String ron = mapper.writeValueAsString(book);
        assertEquals("Book(abridged:true,numberOfPages:1)", ron);
    }

}
