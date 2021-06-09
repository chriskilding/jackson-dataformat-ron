package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;

import static org.junit.Assert.assertEquals;

public class TupleTest extends ContainerTest {
    @Override
    public void testEmpty() {
        assertEquals("()", "");
    }

    @Override
    public void testOne() {
        assertEquals("(1)", "");
    }

    @Override
    public void testMultiple() {
        assertEquals("(1,2)", "");
    }
}
