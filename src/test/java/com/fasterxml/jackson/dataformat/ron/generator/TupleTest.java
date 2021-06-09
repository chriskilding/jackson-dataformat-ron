package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;

import static org.junit.Assert.assertEquals;

public class TupleTest extends ContainerTest {
    @Override
    public void empty() {
        assertEquals("()", "");
    }

    @Override
    public void one() {
        assertEquals("(1)", "");
    }

    @Override
    public void multiple() {
        assertEquals("(1,2)", "");
    }
}
