package com.fasterxml.jackson.dataformat.ron.databind.examples;

import java.util.Objects;

public class Dog implements Animal {
    public int barks;

    public Dog() {
        // default constructor
    }

    public Dog(int barks) {
        this.barks = barks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog dog = (Dog) o;
        return barks == dog.barks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(barks);
    }

    @Override
    public String sound() {
        return "bark";
    }
}
