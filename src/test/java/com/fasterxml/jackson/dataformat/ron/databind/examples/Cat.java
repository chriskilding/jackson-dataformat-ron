package com.fasterxml.jackson.dataformat.ron.databind.examples;

import com.fasterxml.jackson.dataformat.ron.databind.deser.NamedStructReaderTest;

import java.util.Objects;

public class Cat implements Animal {

    public boolean meow;

    public Cat(boolean meow) {
        this.meow = meow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cat cat = (Cat) o;
        return meow == cat.meow;
    }

    @Override
    public int hashCode() {
        return Objects.hash(meow);
    }

    @Override
    public String sound() {
        return "meow";
    }
}
