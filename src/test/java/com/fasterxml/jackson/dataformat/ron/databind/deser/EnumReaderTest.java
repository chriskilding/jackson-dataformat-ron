package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EnumReaderTest {

    enum Example {
        Foo, Bar
    }

    @Test
    public void testFoo() throws IOException {
        String ron = "Foo";
        Example ex = new RONMapper().readValue(ron, Example.class);
        assertEquals(Example.Foo, ex);
    }

    @Test
    public void testBar() throws IOException {
        String ron = "Bar";
        Example ex = new RONMapper().readValue(ron, Example.class);
        assertEquals(Example.Bar, ex);
    }

}
