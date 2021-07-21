package com.fasterxml.jackson.dataformat.ron.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FieldsTest {

    public static class Foo {
        public int a;
        public boolean b;
    }

    @Test
    public void testPrint() {
       String f = Fields.print(Foo.class.getFields());

       assertEquals("(a,b,)", f);
   }
}
