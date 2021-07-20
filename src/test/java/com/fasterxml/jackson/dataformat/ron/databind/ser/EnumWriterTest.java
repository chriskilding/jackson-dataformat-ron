package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.ron.EnumTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Book;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Example;

import static org.junit.Assert.assertEquals;

public class EnumWriterTest extends EnumTest {

    @Override
    public void testSimple() throws JsonProcessingException {
        Example example = Example.Bar;
        String ron = new RONMapper().writeValueAsString(example);
        assertEquals("Bar", ron);
    }

    @Override
    public void testComplex() throws JsonProcessingException {
        Book book = new Book(true, 2);
        String ron = new RONMapper().writeValueAsString(book);
        assertEquals("Book(true,2)", ron);
    }
}
