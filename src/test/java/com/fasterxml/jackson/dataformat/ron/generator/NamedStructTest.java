package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;

import static org.junit.Assert.assertEquals;

public class NamedStructTest extends ContainerTest {
    @Override
    public void empty() {
        assertEquals("Example()", "");
    }

    @Override
    public void one() {
        assertEquals("Example(foo:1)", "");
    }

    @Override
    public void multiple() {
        assertEquals("Example(foo:1,bar:2)", "");
    }
}
