package com.fasterxml.jackson.dataformat.ron;

import org.junit.Test;

import java.io.IOException;

/**
 * Test conversion of RON ints to/from Java integer types.
 */
public abstract class IntTest {

    @Test
    public abstract void testInt() throws IOException;

    @Test
    public abstract void testLong() throws IOException;

    @Test
    public abstract void testBigInteger() throws IOException;
}
