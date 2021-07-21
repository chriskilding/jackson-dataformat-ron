package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import com.fasterxml.jackson.dataformat.ron.databind.examples.*;
import com.fasterxml.jackson.dataformat.ron.databind.examples.animal.Cat;
import com.fasterxml.jackson.dataformat.ron.databind.examples.animal.Dog;
import com.fasterxml.jackson.dataformat.ron.databind.examples.animal.Jellyfish;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class StructReaderTest extends ContainerTest {

    @Override
    public void testEmpty() throws IOException {
        String ron = "()";
        Jellyfish struct = new RONMapper().readValue(ron, Jellyfish.class);
        assertEquals(new Jellyfish(), struct);
    }

    @Override
    public void testOne() throws IOException {
        String ron = "(barks:2)";
        Dog struct = new RONMapper().readValue(ron, Dog.class);
        assertEquals(new Dog(2), struct);
    }

    @Override
    public void testMultiple() throws IOException {
        String ron = "(happy:true,meows:2)";
        Cat cat = new RONMapper().readValue(ron, Cat.class);
        assertEquals(new Cat(true, 2), cat);
    }

    @Test
    public void testNested() throws IOException {
        String ron = "(a:1,b:(c:2))";
        Nested struct = new RONMapper().readValue(ron, Nested.class);
        assertEquals(new Nested(1, new B(2)), struct);
    }

    @Test
    public void testMissingFieldsNotDeserialized() throws IOException {
        String ron = "(a:1)";
        Nested struct = new RONMapper().readValue(ron, Nested.class);
        assertEquals(new Nested(1, null), struct);
    }

}
