package com.fasterxml.jackson.dataformat.ron.databind.deser.chain;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONLexer;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;

class DoubleDeserializer implements Deserializer {
    @Override
    public boolean canApply(JavaType javaType) {
        return javaType.isTypeOrSubTypeOf(Double.class) || javaType.isTypeOrSubTypeOf(double.class);
    }

    @Override
    public Object apply(RONParser.ValueContext ctx) {
        switch (ctx.start.getType()) {
            case RONLexer.INF:
                return Double.POSITIVE_INFINITY;
            case RONLexer.MINUS_INF:
                return Double.NEGATIVE_INFINITY;
            case RONLexer.NAN:
                return Double.NaN;
            default:
                final String num = ctx.NUMBER().getText();
                return Double.parseDouble(num);
        }
    }
}
