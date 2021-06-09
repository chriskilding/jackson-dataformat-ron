package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumTest extends ContainerTest {

    @Override
    public void empty() {
        assertEquals("Foo", "");
    }

    @Override
    public void one() {
        assertEquals("Foo(1)", "");
    }

    @Override
    public void multiple() {
        assertEquals("Foo(1,2)", "");
    }
}
