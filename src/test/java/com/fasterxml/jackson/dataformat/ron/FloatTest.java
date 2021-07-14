package com.fasterxml.jackson.dataformat.ron;

import org.junit.Test;

import java.io.IOException;

/**
 * Test the conversion of RON float values to/from Java float types.
 */
public abstract class FloatTest {

    @Test
    public abstract void testDouble() throws IOException;

    @Test
    public abstract void testFloat() throws IOException;

    @Test
    public abstract void testBigDecimal() throws IOException;
}
