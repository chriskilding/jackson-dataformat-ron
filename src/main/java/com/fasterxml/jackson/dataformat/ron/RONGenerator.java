package com.fasterxml.jackson.dataformat.ron;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.GeneratorBase;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class RONGenerator extends GeneratorBase {

    protected RONGenerator(int features, ObjectCodec codec) {
        super(features, codec);
    }

    @Override
    public void writeStartArray() throws IOException {

    }

    @Override
    public void writeEndArray() throws IOException {

    }

    @Override
    public void writeStartObject() throws IOException {

    }

    @Override
    public void writeEndObject() throws IOException {

    }

    /**
     * Write the '(' which is the start of a tuple or struct.
     */
    public void writeStartTuple() throws IOException {

    }

    /**
     * Write the ')' which is the end of a tuple or struct.
     */
    public void writeEndTuple() throws IOException {

    }

    @Override
    public void writeFieldName(String s) throws IOException {

    }

    @Override
    public void writeString(String s) throws IOException {

    }

    @Override
    public void writeString(char[] chars, int i, int i1) throws IOException {

    }

    @Override
    public void writeRawUTF8String(byte[] bytes, int i, int i1) throws IOException {
        _reportUnsupportedOperation();
    }

    @Override
    public void writeUTF8String(byte[] bytes, int i, int i1) throws IOException {

    }

    @Override
    public void writeRaw(String s) throws IOException {

    }

    @Override
    public void writeRaw(String s, int i, int i1) throws IOException {

    }

    @Override
    public void writeRaw(char[] chars, int i, int i1) throws IOException {

    }

    @Override
    public void writeRaw(char c) throws IOException {

    }

    @Override
    public void writeBinary(Base64Variant base64Variant, byte[] bytes, int i, int i1) throws IOException {

    }

    @Override
    public void writeNumber(int i) throws IOException {

    }

    @Override
    public void writeNumber(long l) throws IOException {

    }

    @Override
    public void writeNumber(BigInteger bigInteger) throws IOException {

    }

    @Override
    public void writeNumber(double v) throws IOException {

    }

    @Override
    public void writeNumber(float v) throws IOException {

    }

    @Override
    public void writeNumber(BigDecimal bigDecimal) throws IOException {

    }

    @Override
    public void writeNumber(String s) throws IOException {

    }

    @Override
    public void writeBoolean(boolean b) throws IOException {

    }

    @Override
    public void writeNull() throws IOException {

    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    protected void _releaseBuffers() {

    }

    @Override
    protected void _verifyValueWrite(String s) throws IOException {

    }

    protected void _reportUnsupportedOperation() {
        throw new UnsupportedOperationException("Operation not supported by generator of type "+getClass().getName());
    }
}
