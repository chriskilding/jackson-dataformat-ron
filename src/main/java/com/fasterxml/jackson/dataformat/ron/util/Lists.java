package com.fasterxml.jackson.dataformat.ron.util;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Utility functions for lists.
 */
public final class Lists {
    private Lists() {}


    /**
     * Unlike list.toArray(), this converts the List to an array of the desired element type.
     */
    public static Object toArray(List<?> list, Class<?> elementType) {
        final Object arr = Array.newInstance(elementType, list.size());

        for (int i = 0; i < list.size(); ++i) {
            final Object element = list.get(i);
            Array.set(arr, i, element);
        }

        return arr;
    }
}
