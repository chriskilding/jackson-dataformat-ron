package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;

import static org.junit.Assert.assertEquals;

public class StructTest extends ContainerTest {
    @Override
    public void empty() {
        assertEquals("()", "");
    }

    @Override
    public void one() {
        assertEquals("(foo:1)", "");
    }

    @Override
    public void multiple() {
        assertEquals("(foo:1,bar:2)", "");
    }
}
