package com.fasterxml.jackson.dataformat.ron.databind.deser.chain;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;

import java.math.BigInteger;

class BigIntegerDeserializer implements Deserializer {
    @Override
    public boolean canApply(JavaType javaType) {
        return javaType.isTypeOrSubTypeOf(BigInteger.class);
    }

    @Override
    public Object apply(RONParser.ValueContext ctx) {
        final String num = ctx.NUMBER().getText();
        return new BigInteger(num);
    }
}
