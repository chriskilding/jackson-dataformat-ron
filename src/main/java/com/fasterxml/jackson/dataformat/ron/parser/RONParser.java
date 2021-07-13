package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.ParserBase;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONLexer;
import com.fasterxml.jackson.dataformat.ron.util.Strings;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;

public class RONParser extends ParserBase {

    protected Reader _reader;

    private RONLexer lexer;

    protected ObjectCodec _objectCodec;

    /**
     * Method called when input comes as a {@link java.io.Reader}, and buffer allocation
     * can be done using default mechanism.
     */
    public RONParser(IOContext ctxt, int features, Reader r, ObjectCodec codec) {
        super(ctxt, features);
        _reader = r;
        _objectCodec = codec;
    }

    @Override
    public ObjectCodec getCodec() {
        return _objectCodec;
    }

    @Override
    public void setCodec(ObjectCodec c) {
        _objectCodec = c;
    }

    @Override
    public Reader getInputSource() {
        return _reader;
    }

    @Override
    protected void _closeInput() throws IOException {
        /* 25-Nov-2008, tatus: As per [JACKSON-16] we are not to call close()
         *   on the underlying Reader, unless we "own" it, or auto-closing
         *   feature is enabled.
         *   One downside is that when using our optimized
         *   Reader (granted, we only do that for UTF-32...) this
         *   means that buffer recycling won't work correctly.
         */
        if (_reader != null) {
            if (_ioContext.isResourceManaged() || isEnabled(Feature.AUTO_CLOSE_SOURCE)) {
                _reader.close();
            }
            _reader = null;
        }
    }

    @Override
    public final String getText() {
        // not used
        return null;
    }

    @Override
    public final char[] getTextCharacters() {
        // not used
        return null;
    }

    @Override
    public final int getTextLength() {
        // not used
        return 0;
    }

    @Override
    public final int getTextOffset() {
        // not used
        return 0;
    }

    /**
     * An alternative to nextToken() that can return the full range of RON tokens.
     */
    public RONToken nextRONToken() throws IOException {
        Token token = nextLexerToken();

        return toRONToken(token);
    }

    /**
     * Low-level nextToken method.
     */
    private Token nextLexerToken() throws IOException {
        Reader reader = this._reader;

        // TODO close lexer?
        if (lexer == null) {
            CharStream charStream = CharStreams.fromReader(reader);
            lexer = new RONLexer(charStream);
        }

        Token token = lexer.nextToken();
        String tokenText = token.getText();
        char t = tokenText.charAt(0);

        switch (t) {
            case ':':
            case ',':
                // consume and keep going
                // TODO: rewrite to avoid recursion
                return nextLexerToken();
        }

        return token;
    }

    private static RONToken toRONToken(Token token) {
        switch (token.getType()) {
            case RONLexer.TRUE:
                return RONToken.TRUE;
            case RONLexer.FALSE:
                return RONToken.FALSE;
            case RONLexer.START_ARRAY:
                return RONToken.START_ARRAY;
            case RONLexer.END_ARRAY:
                return RONToken.END_ARRAY;
            case RONLexer.START_MAP:
                return RONToken.START_MAP;
            case RONLexer.END_MAP:
                return RONToken.END_MAP;
            case RONLexer.START_TUPLE:
                return RONToken.START_TUPLE;
            case RONLexer.END_TUPLE:
                return RONToken.END_TUPLE;
            case RONLexer.STRING:
                return RONToken.STRING;
            default:
                // FIXME add the rest
                return null;
        }
    }

    private static JsonToken toJsonToken(Token token) {
        switch (token.getType()) {
            case RONLexer.TRUE:
                return JsonToken.VALUE_TRUE;
            case RONLexer.FALSE:
                return JsonToken.VALUE_FALSE;
            case RONLexer.INF:
            case RONLexer.MINUS_INF:
            case RONLexer.NAN:
                return JsonToken.VALUE_NUMBER_FLOAT;
            case RONLexer.STRING:
                return JsonToken.VALUE_STRING;
            case RONLexer.START_ARRAY:
                return JsonToken.START_ARRAY;
            case RONLexer.END_ARRAY:
                return JsonToken.END_ARRAY;
            case RONLexer.START_MAP:
                return JsonToken.START_OBJECT;
            case RONLexer.END_MAP:
                return JsonToken.END_OBJECT;
                // FIXME add the rest
            default:
                return null;
        }
    }

    /**
     * This method will only work if the RON input is strictly JSON-compatible.
     */
    @Override
    public final JsonToken nextToken() throws IOException {
        Token token = nextLexerToken();

        return toJsonToken(token);
    }

    @Override
    public String nextFieldName() throws IOException {
        // TODO distinguish field names from string values
        return this.nextTextValue();
    }

    @Override
    public final String nextTextValue() throws IOException {
        Token token = nextLexerToken();

        if (token.getType() == RONLexer.STRING) {
            return Strings.removeEnclosingQuotes(token.getText());
        }

        return null;
    }

    @Override
    public final int nextIntValue(int defaultValue) throws IOException {
        Token token = nextLexerToken();

        if (token.getType() == RONLexer.NUMBER) {
            String num = token.getText();
            return Integer.parseInt(num);
        }

        return defaultValue;
    }

    @Override
    public final long nextLongValue(long defaultValue) throws IOException {
        Token token = nextLexerToken();

        if (token.getType() == RONLexer.NUMBER) {
            String num = token.getText();
            return Long.parseLong(num);
        }

        return defaultValue;
    }

    @Override
    public final Boolean nextBooleanValue() throws IOException {
        final Token token = nextLexerToken();

        switch (token.getType()) {
            case RONLexer.TRUE:
                return Boolean.TRUE;
            case RONLexer.FALSE:
                return Boolean.FALSE;
            default:
                return null;
        }
    }

    /**
     * Get the next RON identifier (struct name, struct key, enum name).
     */
    public String nextIdentifier() throws IOException {
        Token token = this.nextLexerToken();

        if (token.getType() == RONLexer.IDENTIFIER) {
            return token.getText();
        } else {
            _reportError("The next token was not an identifier: ", token.getText());
            return null;
        }
    }

    /**
     * This doesn't exist on JsonParser yet but we include it for completeness.
     */
    public float nextFloatValue(float defaultValue) throws IOException {
        Token token = this.nextLexerToken();

        switch (token.getType()) {
            case RONLexer.INF:
                return Float.POSITIVE_INFINITY;
            case RONLexer.MINUS_INF:
                return Float.NEGATIVE_INFINITY;
            case RONLexer.NAN:
                return Float.NaN;
            case RONLexer.NUMBER:
                return Float.parseFloat(token.getText());
        }

        return defaultValue;
    }

    /**
     * This doesn't exist on JsonParser yet but we include it for completeness.
     */
    public double nextDoubleValue(double defaultValue) throws IOException {
        Token token = this.nextLexerToken();

        switch (token.getType()) {
            case RONLexer.INF:
                return Double.POSITIVE_INFINITY;
            case RONLexer.MINUS_INF:
                return Double.NEGATIVE_INFINITY;
            case RONLexer.NAN:
                return Double.NaN;
            case RONLexer.NUMBER:
                return Double.parseDouble(token.getText());
        }

        return defaultValue;
    }

    /**
     * This doesn't exist on JsonParser yet but we include it for completeness.
     */
    public BigInteger nextBigIntegerValue() throws IOException {
        final Token token = this.nextLexerToken();

        if (token.getType() == RONLexer.NUMBER) {
            String num = token.getText();
            return new BigInteger(num);
        }

        return null;
    }

    /**
     * This doesn't exist on JsonParser yet but we include it for completeness.
     */
    public BigDecimal nextBigDecimalValue() throws IOException {
        final Token token = this.nextLexerToken();

        if (token.getType() == RONLexer.NUMBER) {
            String num = token.getText();
            return new BigDecimal(num);
        }

        return null;
    }
}
