package com.fasterxml.jackson.dataformat.ron.util;

import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ConstructorsTest {

    @Test
    public void testGetDefaultConstructor() {
        final Constructor<?> ctor = Constructors.getDefaultConstructor(Empty.class);

        assertNotNull(ctor);
    }

    @Test
    public void testGetDefaultConstructorNull() {
        final Constructor<?> ctor = Constructors.getDefaultConstructor(NoDefaultConstructor.class);

        assertNull(ctor);
    }

    static class Empty {
        // implicit default constructor
    }

    static class NoDefaultConstructor {

        private final int foo;

        public NoDefaultConstructor(int foo) {
            this.foo = foo;
        }
    }
}
