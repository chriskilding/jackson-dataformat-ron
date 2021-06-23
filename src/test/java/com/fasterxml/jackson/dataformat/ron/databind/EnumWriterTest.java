package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnumWriterTest {

    @Test
    public void testWriteEnum() throws JsonProcessingException {
        Foo foo = Foo.Bar;
        String ron = new RONMapper().writeValueAsString(foo);
        assertEquals("Bar", ron);
    }

    @Ignore("The normal ObjectMapper cannot serialize enum child fields either")
    public void testWriteEnumWithMultipleParameters() throws JsonProcessingException {
        Coordinate coord = Coordinate.Bar;
        String ron = new RONMapper().writeValueAsString(coord);
        assertEquals("Bar(10, 20)", ron);
    }

    enum Foo {
        Bar
    }

    enum Coordinate {

        Bar(10, 20);

        final int x;
        final int y;

        Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
