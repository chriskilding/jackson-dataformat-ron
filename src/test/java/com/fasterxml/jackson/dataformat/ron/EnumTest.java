package com.fasterxml.jackson.dataformat.ron;

import org.junit.Test;

import java.io.IOException;

public abstract class EnumTest {

    /**
     * Test simple enums.
     */
    @Test
    public abstract void testSimple() throws IOException;

    /**
     * Test complex enums with child fields.
     */
    @Test
    public abstract void testComplex() throws IOException;
}
