package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import com.fasterxml.jackson.dataformat.ron.databind.examples.*;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class NamedStructReaderTest extends ContainerTest {

    @Override
    public void testEmpty() throws IOException {
        // FIXME: deserializer currently treats this as an enum
        String ron = "Jellyfish()";
        Jellyfish struct = new RONMapper().readValue(ron, Jellyfish.class);
        assertEquals(new Jellyfish(), struct);
    }

    @Override
    public void testOne() throws IOException {
        final String ron = "Dog(barks:1)";
        Animal dog = new RONMapper().readValue(ron, Animal.class);
        assertEquals(new Dog(1), dog);
    }

    @Override
    public void testMultiple() throws IOException {
        String ron = "Cat(happy:true,meows:2)";
        Animal cat = new RONMapper().readValue(ron, Cat.class);
        assertEquals(new Cat(true, 2), cat);
    }
}
