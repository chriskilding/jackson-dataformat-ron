package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Java Collections have slightly different parsing to arrays.
 */
public class CollectionReaderTest extends ContainerTest {

    public static final CollectionType LIST_INTEGER = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);

    @Override
    public void testEmpty() throws IOException {
        String ron = "[]";
        List<Integer> coll = new RONMapper().readValue(ron, LIST_INTEGER);
        assertTrue(coll.isEmpty());
    }

    @Override
    public void testOne() throws IOException {
        String ron = "[1]";
        List<Integer> coll = new RONMapper().readValue(ron, LIST_INTEGER);
        assertTrue(coll.contains(1));
    }

    @Override
    public void testMultiple() throws IOException {
        String ron = "[1,2]";
        List<Integer> coll = new RONMapper().readValue(ron, LIST_INTEGER);
        assertTrue(coll.contains(1));
        assertTrue(coll.contains(2));
    }

}
