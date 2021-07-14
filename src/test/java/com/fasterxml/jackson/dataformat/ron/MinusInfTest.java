package com.fasterxml.jackson.dataformat.ron;

import org.junit.Test;

import java.io.IOException;

public abstract class MinusInfTest {

    /**
     * Test converting float to/from '-inf'
     */
    @Test
    public abstract void testFloat() throws IOException;

    /**
     * Test converting double to/from '-inf'
     */
    @Test
    public abstract void testDouble() throws IOException;
}
