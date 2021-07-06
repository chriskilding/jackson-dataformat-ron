package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Cat;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Dog;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Jellyfish;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EnumReaderTest extends ContainerTest {

    enum Example {
        Foo, Bar
    }

    /**
     * A simple enum with no child fields can be read easily enough
     */
    @Test
    public void testSimple() throws IOException {
        String ron = "Foo";
        Example ex = new RONMapper().readValue(ron, Example.class);
        assertEquals(Example.Foo, ex);
    }

    @Override
    public void testEmpty() throws IOException {
        String ron = "Jellyfish()";
        Jellyfish animal = new RONMapper().readValue(ron, Jellyfish.class);
        assertEquals(new Jellyfish(), animal);
    }

    @Override
    public void testOne() throws IOException {
        String ron = "Dog(2)";
        Dog animal = new RONMapper().readValue(ron, Dog.class);
        assertEquals(new Dog(2), animal);
    }

    @Override
    public void testMultiple() throws IOException {
        String ron = "Cat(true,2)";
        Cat animal = new RONMapper().readValue(ron, Cat.class);
        assertEquals(new Cat(true, 2), animal);
    }

}
