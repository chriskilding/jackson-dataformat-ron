package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnumWriterTest {

    @Test
    public void testWriteEnum() throws JsonProcessingException {
        Foo foo = Foo.Bar;
        String ron = new RONMapper().writeValueAsString(foo);
        assertEquals("Bar", ron);
    }

    @Test
    public void testWriteEnumWithParameter() throws JsonProcessingException {
        FooWithParameter foo = FooWithParameter.Bar;
        String ron = new RONMapper().writeValueAsString(foo);
        assertEquals("Bar(1)", ron);
    }

    @Test
    public void testWriteEnumWithMultipleParameters() throws JsonProcessingException {
        Coordinate coord = Coordinate.Bar;
        String ron = new RONMapper().writeValueAsString(coord);
        assertEquals("Bar(10, 20)", ron);
    }

    enum Foo {
        Bar
    }

    enum FooWithParameter {

        Bar(1);

        final int i;

        FooWithParameter(int i) {
            this.i = i;
        }
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
