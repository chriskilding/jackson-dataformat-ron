package com.fasterxml.jackson.dataformat.ron.databind.examples;

import java.util.Objects;

public class Cat implements Animal {

    public boolean happy;
    public int meows;

    public Cat() {
        // default constructor
    }

    public Cat(boolean happy, int meows) {
        this.happy = happy;
        this.meows = meows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cat cat = (Cat) o;
        return happy == cat.happy && meows == cat.meows;
    }

    @Override
    public int hashCode() {
        return Objects.hash(happy, meows);
    }

    @Override
    public String sound() {
        return "meow";
    }
}
