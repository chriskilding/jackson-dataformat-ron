package com.fasterxml.jackson.dataformat.ron.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.lang.reflect.Field;

public final class JavaTypes {

    private JavaTypes() {

    }

    public static JavaType fromField(Field field) {
        final Class<?> klass = field.getType();
        return TypeFactory.defaultInstance().constructFromCanonical(klass.getCanonicalName());
    }
}
