package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    /**
     * Example POJO based on https://schema.org/Book
     */
    static class Book {

        public boolean abridged;
        public int numberOfPages;

        Book(boolean abridged, int numberOfPages) {
            this.abridged = abridged;
            this.numberOfPages = numberOfPages;
        }
    }
}
