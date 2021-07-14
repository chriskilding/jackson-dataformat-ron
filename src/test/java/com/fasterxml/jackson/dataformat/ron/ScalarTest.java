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
    public abstract void testString() throws IOException;
}
