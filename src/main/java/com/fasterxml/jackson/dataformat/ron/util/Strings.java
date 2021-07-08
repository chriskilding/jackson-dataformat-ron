package com.fasterxml.jackson.dataformat.ron.util;

/**
 * Utility functions for strings.
 */
public final class Strings {

    private Strings() {}

    public static String removeEnclosingQuotes(String myString) {
        return myString.substring(1, myString.length()-1);
    }
}
