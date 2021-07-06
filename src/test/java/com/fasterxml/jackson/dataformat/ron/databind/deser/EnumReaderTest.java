package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import com.fasterxml.jackson.dataformat.ron.databind.examples.Example;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EnumReaderTest {

    @Test
    public void testReadEnum() throws IOException {
        String ron = "Foo";
        Example ex = new RONMapper().readValue(ron, Example.class);
        assertEquals(Example.Foo, ex);
    }

}
