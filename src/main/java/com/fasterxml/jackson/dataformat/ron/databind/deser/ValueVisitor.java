package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.dataformat.ron.util.Strings;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONLexer;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Helper for the BeanDeserializer.
 */
class ValueVisitor {

    String visitString(RONParser.ValueContext ctx) {
        final String str = ctx.STRING().getText();
        return Strings.removeEnclosingQuotes(str);
    }

    double visitDouble(RONParser.ValueContext ctx) {
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

    float visitFloat(RONParser.ValueContext ctx) {
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

    int visitInt(RONParser.ValueContext ctx) {
        final String num = ctx.NUMBER().getText();
        return Integer.parseInt(num);
    }

    long visitLong(RONParser.ValueContext ctx) {
        final String num = ctx.NUMBER().getText();
        return Long.parseLong(num);
    }

    BigInteger visitBigInteger(RONParser.ValueContext ctx) {
        final String num = ctx.NUMBER().getText();
        return new BigInteger(num);
    }

    BigDecimal visitBigDecimal(RONParser.ValueContext ctx) {
        final String num = ctx.NUMBER().getText();
        return new BigDecimal(num);
    }

    boolean visitBoolean(RONParser.ValueContext ctx) {
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
