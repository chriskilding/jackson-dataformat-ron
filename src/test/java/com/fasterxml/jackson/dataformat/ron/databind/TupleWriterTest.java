package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.ron.databind.ser.RONTupleSerializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TupleWriterTest {

    @Test
    public void testWriteTuple() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        MyBook book = new MyBook(true, 1);
        String ron = mapper.writeValueAsString(book);
        assertEquals("(true,1)", ron);
    }

//    @JsonSerialize(using = RONTupleSerializer.class)
    private static class MyBook extends Book {
        MyBook(boolean abridged, int numberOfPages) {
            super(abridged, numberOfPages);
        }
    }
}
