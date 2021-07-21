package com.fasterxml.jackson.dataformat.ron.databind.deser.chain;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;

class LongDeserializer implements Deserializer {
    @Override
    public boolean canApply(JavaType javaType) {
        return javaType.isTypeOrSubTypeOf(Long.class) || javaType.isTypeOrSubTypeOf(long.class);
    }

    @Override
    public Object apply(RONParser.ValueContext ctx) {
        final String num = ctx.NUMBER().getText();
        return Long.parseLong(num);
    }
}
