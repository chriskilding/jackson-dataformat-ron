package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tell Jackson how to serialize an object as RON. The clarification is necessary because RON's set of types is larger than the set of types Java supports.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface RONFormat {

    RONFormat.Shape shape() default RONFormat.Shape.ANY;

    enum Shape {
        ANY,
        SCALAR,

        ARRAY,
        MAP,
        STRUCT,
        ENUM,
        TUPLE
    }
}
