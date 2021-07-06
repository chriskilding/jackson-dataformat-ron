package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import com.fasterxml.jackson.dataformat.ron.databind.examples.*;
import com.fasterxml.jackson.dataformat.ron.databind.examples.animal.Cat;
import com.fasterxml.jackson.dataformat.ron.databind.examples.animal.Dog;
import com.fasterxml.jackson.dataformat.ron.databind.examples.animal.Jellyfish;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class StructWriterTest extends ContainerTest {

    @Override
    public void testEmpty() throws IOException {
        Jellyfish animal = new Jellyfish();
        String ron = new RONMapper().writeValueAsString(animal);
        assertEquals("Jellyfish()", ron);
    }

    @Override
    public void testOne() throws IOException {
        Dog animal = new Dog(2);
        String ron = new RONMapper().writeValueAsString(animal);
        assertEquals("Dog(barks:2)", ron);
    }

    @Override
    public void testMultiple() throws JsonProcessingException {
        Cat animal = new Cat(true, 2);
        String ron = new RONMapper().writeValueAsString(animal);
        assertEquals("Cat(happy:true,meows:2)", ron);
    }

    @Test
    public void testNested() throws IOException {
        Nested n = new Nested(1, new B(2));
        String ron = new RONMapper().writeValueAsString(n);
        assertEquals("Nested(a:1,b:B(c:2))", ron);
    }
}
