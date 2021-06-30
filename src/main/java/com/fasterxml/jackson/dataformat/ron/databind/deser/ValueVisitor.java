package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.dataformat.ron.antlr4.RONLexer;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ValueVisitor {

    public String visitString(RONParser.ValueContext ctx) {
        final String str = ctx.STRING().getText();
        return removeEnclosingQuotes(str);
    }

    public double visitDouble(RONParser.ValueContext ctx) {
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

    public float visitFloat(RONParser.ValueContext ctx) {
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

    public int visitInt(RONParser.ValueContext ctx) {
        final String num = ctx.NUMBER().getText();
        return Integer.parseInt(num);
    }

    public BigInteger visitBigInteger(RONParser.ValueContext ctx) {
        final String num = ctx.NUMBER().getText();
        return new BigInteger(num);
    }

    public BigDecimal visitBigDecimal(RONParser.ValueContext ctx) {
        final String num = ctx.NUMBER().getText();
        return new BigDecimal(num);
    }

    public boolean visitBoolean(RONParser.ValueContext ctx) {
        switch (ctx.start.getType()) {
            case RONLexer.TRUE:
                return true;
            case RONLexer.FALSE:
                return false;
            default:
                throw new IllegalArgumentException("Cannot parse the value as a boolean");
        }
    }

    private static String removeEnclosingQuotes(String myString) {
        return myString.substring(1, myString.length()-1);
    }
}
