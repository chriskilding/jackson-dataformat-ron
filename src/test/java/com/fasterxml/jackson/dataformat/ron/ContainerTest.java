package com.fasterxml.jackson.dataformat.ron;

import org.junit.Test;

import java.io.IOException;

public abstract class ContainerTest {

    @Test
    public abstract void empty() throws IOException;

    @Test
    public abstract void one() throws IOException;

    @Test
    public abstract void multiple() throws IOException;
}
