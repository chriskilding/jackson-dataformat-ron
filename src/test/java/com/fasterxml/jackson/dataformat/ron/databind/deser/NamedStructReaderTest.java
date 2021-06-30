package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import com.fasterxml.jackson.dataformat.ron.databind.examples.*;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class NamedStructReaderTest extends ContainerTest {

    @Ignore("Behavior not yet known for unit structs")
    @Override
    public void testEmpty() throws IOException {
        String ron = "Empty()";
        Empty struct = new RONMapper().readValue(ron, Empty.class);
        assertEquals(new Empty(), struct);
    }

    @Override
    public void testOne() throws IOException {
        String ron = "One(foo:1)";
        One struct = new RONMapper().readValue(ron, One.class);
        assertEquals(new One(1), struct);
    }

    @Override
    public void testMultiple() throws IOException {
        String ron = "Book(abridged:true,numberOfPages:1)";
        Book struct = new RONMapper().readValue(ron, Book.class);
        assertEquals(new Book(true, 1), struct);
    }

    @Test
    public void testPolymorphic() throws IOException {
        Animal dog = new RONMapper().readValue("Dog(barks:1)", Animal.class);
        assertEquals(new Dog(1), dog);

        Animal cat = new RONMapper().readValue("Cat(meow:true)", Animal.class);
        assertEquals(new Cat(true), cat);
    }

    /**
     * Show what happens with JSON and regular ObjectMapper
     */
    @Test
    public void testReferencePolymorphicBehavior() throws IOException {
        Animal dog = new ObjectMapper().readValue("{\"@type\":\"Dog\",\"barks\":1}", Animal.class);
        assertEquals(new Dog(1), dog);

        Animal cat = new ObjectMapper().readValue("{\"@type\":\"Cat\",\"meow\":true}", Animal.class);
        assertEquals(new Cat(true), cat);
    }

    public static class One {
        public int foo;

        One() {
            // no-op
        }

        One(int foo) {
            this.foo = foo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            One one = (One) o;
            return foo == one.foo;
        }

        @Override
        public int hashCode() {
            return Objects.hash(foo);
        }
    }

}
