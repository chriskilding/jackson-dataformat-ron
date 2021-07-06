package com.fasterxml.jackson.dataformat.ron.databind.examples.animal;

public class Jellyfish implements Animal {

    // no fields

    /**
     * No vocalisation
     */
    @Override
    public String sound() {
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }
}
