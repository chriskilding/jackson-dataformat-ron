package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnumWriterTest {

    @Test
    public void testWriteEnum() throws JsonProcessingException {
        RONMapper mapper = new RONMapper();
        Foo foo = Foo.Bar;
        String ron = mapper.writeValueAsString(foo);
        assertEquals("Bar", ron);
    }

    public enum Foo {
        Bar, Baz
    }
}
