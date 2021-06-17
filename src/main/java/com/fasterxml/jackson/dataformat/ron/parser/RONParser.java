package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.ParserBase;
import com.fasterxml.jackson.core.io.IOContext;

import java.io.IOException;
import java.io.Reader;

public class RONParser extends ParserBase {

    protected final Reader reader;

    /**
     * Shadows supertype variable of same name
     */
    protected RONToken _nextToken;

    /**
     * Shadows supertype variable of same name
     */
    protected RONToken _currToken;

    /**
     * Shadows supertype variable of same name
     */
    protected RONReadContext _parsingContext;

    public RONParser(IOContext ctxt, int features, Reader reader) {
        super(ctxt, features);
        this.reader = reader;
    }

    @Override
    protected void _closeInput() throws IOException {

    }

    @Override
    public ObjectCodec getCodec() {
        return null;
    }

    @Override
    public void setCodec(ObjectCodec objectCodec) {

    }

    /**
     * Delegates to nextRONToken() and translates from the result.
     * <p>An error will occur if the parser encounters a RON token that has no equivalent in JSON.
     */
    @Override
    public JsonToken nextToken() throws IOException {
        RONToken t = this.nextRONToken();

        if (t == null) {
             return null;
        }

        switch (t) {
            case START_ARRAY:
                return JsonToken.START_ARRAY;
            case END_ARRAY:
                return JsonToken.END_ARRAY;
            case START_OBJECT:
                return JsonToken.START_OBJECT;
            case END_OBJECT:
                return JsonToken.END_OBJECT;
            case TRUE:
                return JsonToken.VALUE_TRUE;
            case FALSE:
                return JsonToken.VALUE_FALSE;
            case STRING:
                return JsonToken.VALUE_STRING;
            case FLOAT:
                return JsonToken.VALUE_NUMBER_FLOAT;
            case INTEGER:
                return JsonToken.VALUE_NUMBER_INT;
            case NOT_AVAILABLE:
                return JsonToken.NOT_AVAILABLE;
            case FIELD_NAME:
                return JsonToken.FIELD_NAME;
            default:
                throw new UnsupportedOperationException("Encountered a RON token that has no equivalent in JSON");
        }
    }

    public RONToken nextRONToken() throws IOException {

        int i = _skipWSOrEnd();
        if (i < 0) {
            // end of input - should actually close/release things
            close();
            return (_currToken = null);
        }

        RONToken t;

        switch (i) {
            case '"':
                if (!_parsingContext.inString()) {
                    _parsingContext = _parsingContext.createChildStringContext();
                }
                t = RONToken.STRING;
                break;
            case '[':
                if (!_parsingContext.inAnArray()) {
                    _parsingContext = _parsingContext.createChildArrayContext();
                }
                t = RONToken.START_ARRAY;
                break;
            case '{':
                if (!_parsingContext.inAnObject()) {
                    _parsingContext = _parsingContext.createChildObjectContext();
                }
                t = RONToken.START_OBJECT;
                break;
            case '(':
                if (!_parsingContext.inStruct()) {
                    _parsingContext = _parsingContext.createChildStructContext();
                }
                t = RONToken.START_STRUCT; // FIXME or tuple, or an enum
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                t = RONToken.INTEGER;
                break;
            default:
                t = null;
                break;
        }

        return t;
    }

    private int _skipWSOrEnd() {
        return 0;
    }

    @Override
    public String getText() throws IOException {
        return null;
    }

    @Override
    public char[] getTextCharacters() throws IOException {
        return new char[0];
    }

    @Override
    public int getTextLength() throws IOException {
        return 0;
    }

    @Override
    public int getTextOffset() throws IOException {
        return 0;
    }


}
