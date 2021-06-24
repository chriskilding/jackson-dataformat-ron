package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

public class ArrayReaderTest extends ContainerTest {

    @Override
    public void testEmpty() throws IOException {
        String ron = "[]";
        int[] array = new RONMapper().readValue(ron, int[].class);
        assertArrayEquals(new int[] {}, array);
    }

    @Override
    public void testOne() throws IOException {
        String ron = "[1]";
        int[] array = new RONMapper().readValue(ron, int[].class);
        assertArrayEquals(new int[] {1}, array);
    }

    @Override
    public void testMultiple() throws IOException {
        String ron = "[1,2]";
        int[] array = new RONMapper().readValue(ron, int[].class);
        assertArrayEquals(new int[] {1, 2}, array);
    }
}
