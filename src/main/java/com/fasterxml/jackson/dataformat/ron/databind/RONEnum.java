package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the class should be mapped to/from RON enums rather than structs.
 *
 * The annotation is applied at the class level to ensure that if something is deserialized from a RON enum, it will be serialized back to a RON enum in future. (A method- or field-level annotation would risk breaking this property.)
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface RONEnum {
}
