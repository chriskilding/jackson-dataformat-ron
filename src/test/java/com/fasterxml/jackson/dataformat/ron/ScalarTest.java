package com.fasterxml.jackson.dataformat.ron;

import org.junit.Test;

import java.io.IOException;

/**
 * Ensure all RON scalar value options, and their conversions into Java types, are exercised.
 */
public abstract class ScalarTest {

    @Test
    public abstract void testTrue() throws IOException;

    @Test
    public abstract void testFalse() throws IOException;

    @Test
    public abstract void testDouble() throws IOException;

    @Test
    public abstract void testFloat() throws IOException;

    @Test
    public abstract void testBigInteger() throws IOException;

    @Test
    public abstract void testBigDecimal() throws IOException;

    @Test
    public abstract void testInf() throws IOException;

    @Test
    public abstract void testMinusInf() throws IOException;

    @Test
    public abstract void testNan() throws IOException;

    @Test
    public abstract void testInt() throws IOException;

    @Test
    public abstract void testLong() throws IOException;

    @Test
    public abstract void testString() throws IOException;
}
