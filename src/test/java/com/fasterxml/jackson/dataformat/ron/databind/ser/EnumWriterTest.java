package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONFormat;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Animal;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Cat;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Dog;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Jellyfish;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class EnumWriterTest extends ContainerTest {

    /**
     * Classes can be serialized in multiple ways. Serialize as enum.
     */
    @RONFormat(shape = RONFormat.Shape.ENUM)
    private Animal animal;

    /**
     * Simple Java enums can be serialized directly.
     */
    @Test
    public void testSimpleEnum() throws JsonProcessingException {
        Foo foo = Foo.Bar;
        String ron = new RONMapper().writeValueAsString(foo);
        assertEquals("Bar", ron);
    }

    @Override
    public void testEmpty() throws JsonProcessingException {
        this.animal = new Jellyfish();
        String ron = new RONMapper().writeValueAsString(animal);
        assertEquals("Jellyfish()", ron);
    }

    @Override
    public void testOne() throws JsonProcessingException {
        this.animal = new Dog(2);
        String ron = new RONMapper().writeValueAsString(animal);
        assertEquals("Dog(2)", ron);
    }

    @Override
    public void testMultiple() throws IOException {
        this.animal = new Cat(true, 2);
        String ron = new RONMapper().writeValueAsString(animal);
        assertEquals("Cat(true,2)", ron);
    }

    enum Foo {
        Bar
    }

}
