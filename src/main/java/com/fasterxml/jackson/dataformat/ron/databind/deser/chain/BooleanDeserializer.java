package com.fasterxml.jackson.dataformat.ron.databind.deser.chain;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONLexer;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;

class BooleanDeserializer implements Deserializer {
    @Override
    public boolean canApply(JavaType javaType) {
        return javaType.isTypeOrSubTypeOf(Boolean.class) || javaType.isTypeOrSubTypeOf(boolean.class);
    }

    @Override
    public Object apply(RONParser.ValueContext ctx) {
        switch (ctx.start.getType()) {
            case RONLexer.TRUE:
                return true;
            case RONLexer.FALSE:
                return false;
            default:
                throw new IllegalArgumentException("Cannot parse the value as a boolean");
        }
    }
}
