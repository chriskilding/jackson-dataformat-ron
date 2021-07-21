package com.fasterxml.jackson.dataformat.ron.util;

import java.lang.reflect.Constructor;

/**
 * Utility functions for constructors.
 */
public final class Constructors {

    private Constructors() {}

    /**
     * @return the default constructor for the klass, or null if there isn't one
     */
    public static Constructor<?> getDefaultConstructor(Class<?> klass) {
        for (Constructor<?> ctor: klass.getConstructors()) {
            if (isDefaultConstructor(ctor)) {
                return ctor;
            }
        }

        return null;
    }

    private static boolean isDefaultConstructor(Constructor<?> ctor) {
        return (ctor.getParameterTypes().length == 0);
    }
}
