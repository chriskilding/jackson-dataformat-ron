package com.fasterxml.jackson.dataformat.ron.databind.examples;

import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonSubTypes({
    @JsonSubTypes.Type(value = Cat.class),
    @JsonSubTypes.Type(value = Dog.class),
})
public interface Animal {
    String sound();
}
