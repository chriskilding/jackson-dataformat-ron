package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;

import static org.junit.Assert.assertEquals;

public class StructTest extends ContainerTest {
    @Override
    public void testEmpty() {
        assertEquals("()", "");
    }

    @Override
    public void testOne() {
        assertEquals("(foo:1)", "");
    }

    @Override
    public void testMultiple() {
        assertEquals("(foo:1,bar:2)", "");
    }
}