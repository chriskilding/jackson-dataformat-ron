package com.fasterxml.jackson.dataformat.ron.databind.examples.animal;

import com.fasterxml.jackson.annotation.JsonSubTypes;

/**
 * A textbook polymorphic class-subclass example.
 */
@JsonSubTypes({
    @JsonSubTypes.Type(Cat.class),
    @JsonSubTypes.Type(Dog.class),
    @JsonSubTypes.Type(Jellyfish.class)
})
public interface Animal {
    String sound();
}
