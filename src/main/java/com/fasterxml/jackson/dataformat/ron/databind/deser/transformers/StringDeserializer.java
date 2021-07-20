package com.fasterxml.jackson.dataformat.ron.databind.deser.transformers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;
import com.fasterxml.jackson.dataformat.ron.util.Strings;

class StringDeserializer implements Deserializer {
    @Override
    public boolean canApply(JavaType javaType) {
        return javaType.isTypeOrSubTypeOf(String.class);
    }

    @Override
    public Object apply(RONParser.ValueContext ctx) {
        final String str = ctx.STRING().getText();
        return Strings.removeEnclosingQuotes(str);
    }
}
