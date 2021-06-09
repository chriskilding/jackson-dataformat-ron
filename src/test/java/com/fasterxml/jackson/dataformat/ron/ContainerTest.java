package com.fasterxml.jackson.dataformat.ron;

import org.junit.Test;

import java.io.IOException;

public abstract class ContainerTest {

    @Test
    public abstract void testEmpty() throws IOException;

    @Test
    public abstract void testOne() throws IOException;

    @Test
    public abstract void testMultiple() throws IOException;
}
