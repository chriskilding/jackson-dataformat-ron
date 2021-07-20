package com.fasterxml.jackson.dataformat.ron.databind.deser.transformers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;

class IntegerDeserializer implements Deserializer {
    @Override
    public boolean canApply(JavaType javaType) {
        return javaType.isTypeOrSubTypeOf(Integer.class) || javaType.isTypeOrSubTypeOf(int.class);
    }

    @Override
    public Object apply(RONParser.ValueContext ctx) {
        final String num = ctx.NUMBER().getText();
        return Integer.parseInt(num);
    }
}
