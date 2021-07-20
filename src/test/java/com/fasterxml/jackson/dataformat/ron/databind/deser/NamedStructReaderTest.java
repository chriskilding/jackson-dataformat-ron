package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import com.fasterxml.jackson.dataformat.ron.databind.examples.animal.Animal;
import com.fasterxml.jackson.dataformat.ron.databind.examples.animal.Cat;
import com.fasterxml.jackson.dataformat.ron.databind.examples.animal.Dog;
import com.fasterxml.jackson.dataformat.ron.databind.examples.animal.Jellyfish;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class NamedStructReaderTest extends ContainerTest {

    @Override
    public void testEmpty() throws IOException {
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

    @Test(expected = JsonMappingException.class)
    public void testUnknownStructName() throws IOException {
        String ron = "Foo(a:1)";
        Animal animal = new RONMapper().readValue(ron, Animal.class);
    }
}
