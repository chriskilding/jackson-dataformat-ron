package com.fasterxml.jackson.dataformat.ron.databind.examples;

public class Empty {
    // no fields

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }
}
