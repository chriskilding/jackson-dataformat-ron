package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import org.junit.Ignore;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class StructReaderTest extends ContainerTest {
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
        String ron = "(foo:1,bar:true)";
        Multiple struct = new RONMapper().readValue(ron, Multiple.class);
        assertEquals(new Multiple(1, true), struct);
    }

    static class Empty {
        // no fields
    }

    static class One {
        public int foo;

        One(int foo) {
            this.foo = foo;
        }
    }

    static class Multiple {

        public int foo;
        public boolean bar;

        public Multiple(int foo, boolean bar) {
            this.foo = foo;
            this.bar = bar;
        }
    }
}
