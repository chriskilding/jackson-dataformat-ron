package com.fasterxml.jackson.dataformat.ron.databind.deser.transformers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;

import java.math.BigDecimal;

class BigDecimalDeserializer implements Deserializer {
    @Override
    public boolean canApply(JavaType javaType) {
        return javaType.isTypeOrSubTypeOf(BigDecimal.class);
    }

    @Override
    public Object apply(RONParser.ValueContext ctx) {
        final String num = ctx.NUMBER().getText();
        return new BigDecimal(num);
    }
}
