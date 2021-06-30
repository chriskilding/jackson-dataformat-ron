package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import com.fasterxml.jackson.dataformat.ron.databind.examples.B;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Book;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Empty;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Nested;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class StructReaderTest extends ContainerTest {

    @Ignore("Behavior not yet known for unit structs")
    @Override
    public void testEmpty() throws IOException {
        String ron = "()";
        Empty struct = new RONMapper().readValue(ron, Empty.class);
        assertEquals(new Empty(), struct);
    }

    @Override
    public void testOne() throws IOException {
        String ron = "(foo:1)";
        One struct = new RONMapper().readValue(ron, One.class);
        assertEquals(new One(1), struct);
    }

    @Override
    public void testMultiple() throws IOException {
        String ron = "(abridged:true,numberOfPages:1)";
        Book struct = new RONMapper().readValue(ron, Book.class);
        assertEquals(new Book(true, 1), struct);
    }

    @Test
    public void testNested() throws IOException {
        String ron = "(a:1,b:(c:2))";
        Nested struct = new RONMapper().readValue(ron, Nested.class);
        assertEquals(new Nested(1, new B(2)), struct);
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
