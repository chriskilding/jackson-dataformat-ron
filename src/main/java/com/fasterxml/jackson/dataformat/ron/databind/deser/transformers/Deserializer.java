package com.fasterxml.jackson.dataformat.ron.databind.deser.transformers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;

public interface Deserializer {
    boolean canApply(JavaType javaType);

    Object apply(RONParser.ValueContext ctx);
}
