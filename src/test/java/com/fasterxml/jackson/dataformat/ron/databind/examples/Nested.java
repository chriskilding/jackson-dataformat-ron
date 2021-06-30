package com.fasterxml.jackson.dataformat.ron.databind.examples;

import java.util.Objects;

public class Nested {

    public int a;
    public B b;

    public Nested() {
        // no-op
    }

    public Nested(int a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nested nested = (Nested) o;
        return a == nested.a && Objects.equals(b, nested.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
