package com.fasterxml.jackson.dataformat.ron.databind.examples.subdir;

import com.fasterxml.jackson.dataformat.ron.databind.examples.Animal;

public class Dog implements Animal {

    public float aboies;

    public Dog() {
        // default constructor
    }

    public Dog(float aboies) {
        this.aboies = aboies;
    }

    @Override
    public String sound() {
        return "aboie";
    }
}
