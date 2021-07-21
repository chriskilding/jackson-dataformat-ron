package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.ron.EnumTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Book;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Example;
import com.fasterxml.jackson.dataformat.ron.databind.examples.animal.Cat;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EnumReaderTest extends EnumTest {

    @Override
    public void testSimple() throws IOException {
        String ron = "Foo";
        Example ex = new RONMapper().readValue(ron, Example.class);
        assertEquals(Example.Foo, ex);
    }

    @Override
    public void testComplex() throws IOException {
        String ron = "Book(true,2)";
        Book book = new RONMapper().readValue(ron, Book.class);
        assertEquals(new Book(true, 2), book);
    }

    @Test(expected = JsonMappingException.class)
    public void testCannotMapEnumWithoutAnnotation() throws IOException {
        String ron = "Cat(true,2)";
        // attempt to read RON enum to a POJO without the correct annotation
        new RONMapper().readValue(ron, Cat.class);
    }

}
