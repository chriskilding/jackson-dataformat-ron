package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;

import static org.junit.Assert.assertEquals;

public class NamedStructTest extends ContainerTest {
    @Override
    public void testEmpty() {
        assertEquals("Example()", "");
    }

    @Override
    public void testOne() {
        assertEquals("Example(foo:1)", "");
    }

    @Override
    public void testMultiple() {
        assertEquals("Example(foo:1,bar:2)", "");
    }
}
