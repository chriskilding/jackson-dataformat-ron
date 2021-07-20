package com.fasterxml.jackson.dataformat.ron.databind.deser.transformers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONLexer;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;

class FloatDeserializer implements Deserializer {
    @Override
    public boolean canApply(JavaType javaType) {
        return javaType.isTypeOrSubTypeOf(Float.class) || javaType.isTypeOrSubTypeOf(float.class);
    }

    @Override
    public Object apply(RONParser.ValueContext ctx) {
        switch (ctx.start.getType()) {
            case RONLexer.INF:
                return Float.POSITIVE_INFINITY;
            case RONLexer.MINUS_INF:
                return Float.NEGATIVE_INFINITY;
            case RONLexer.NAN:
                return Float.NaN;
            default:
                final String num = ctx.NUMBER().getText();
                return Float.parseFloat(num);
        }
    }
}
