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
        assertEquals("(abridged:true,numberOfPages:1)", ron);
    }

    @Test
    public void testWriteNamedStruct() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        Book book = new Book(true, 1);
        String ron = mapper.writeValueAsString(book);
        assertEquals("Book(abridged:true,numberOfPages:1)", ron);
    }

    /**
     * Example POJO based on https://schema.org/Book
     */
    static class Book {

        private boolean abridged;
        private int numberOfPages;

        Book(boolean abridged, int numberOfPages) {
            this.abridged = abridged;
            this.numberOfPages = numberOfPages;
        }

        public boolean isAbridged() {
            return abridged;
        }

        public void setAbridged(boolean abridged) {
            this.abridged = abridged;
        }

        public int getNumberOfPages() {
            return numberOfPages;
        }

        public void setNumberOfPages(int numberOfPages) {
            this.numberOfPages = numberOfPages;
        }
    }
}
