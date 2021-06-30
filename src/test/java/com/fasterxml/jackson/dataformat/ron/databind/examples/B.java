package com.fasterxml.jackson.dataformat.ron.databind.examples;

import java.util.Objects;

public class B {
    public int c;

    public B() {
        // no-op
    }

    public B(int c) {
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        B b = (B) o;
        return c == b.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(c);
    }
}
