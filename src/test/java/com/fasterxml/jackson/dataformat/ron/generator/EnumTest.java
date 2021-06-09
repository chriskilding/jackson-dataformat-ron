package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumTest extends ContainerTest {

    @Override
    public void testEmpty() {
        assertEquals("Foo", "");
    }

    @Override
    public void testOne() {
        assertEquals("Foo(1)", "");
    }

    @Override
    public void testMultiple() {
        assertEquals("Foo(1,2)", "");
    }

    public enum Example {
        Foo, Bar, Baz
    }
}
