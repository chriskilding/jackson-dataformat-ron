package com.fasterxml.jackson.dataformat.ron.util;

import java.lang.reflect.Field;

public final class Fields {

    private Fields() {

    }

    public static void setSuppressingAccessChecks(Field field, Object obj, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(obj, value);
        field.setAccessible(false);
    }

    public static String print(Field[] fields) {
        final StringBuilder sb = new StringBuilder();

        sb.append("(");
        for (Field field: fields) {
            sb.append(field.getName());
            sb.append(",");
        }
        sb.append(")");

        return sb.toString();
    }
}
