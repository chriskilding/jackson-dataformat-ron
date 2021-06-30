package com.fasterxml.jackson.dataformat.ron.databind.examples;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Cat.class),
    @JsonSubTypes.Type(value = Dog.class),
//    @JsonSubTypes.Type(value = com.fasterxml.jackson.dataformat.ron.databind.examples.subdir.Dog.class)
    // If 2 classes in this list have the same name, the last one in this list takes precedence.
    // It looks as if this is a situation that Jackson does not want you to get into. It really expects that subtypes have different names.
})
public interface Animal {
    String sound();
}
