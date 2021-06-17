package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

public class RONGenerator extends GeneratorBase implements RONEnumGenerator, RONStructGenerator, RONTupleGenerator {

    private static final char DOUBLE_QUOTE = '\"';

    protected final Writer _writer;

    /**
     * Object that keeps track of the current contextual state
     * of the generator.
     */
    protected RONWriteContext _writeContext;

    final protected IOContext _ioContext;

    public RONGenerator(int features, ObjectCodec codec, Writer writer, IOContext ioContext) {
        super(features, codec);
        this._writer = writer;
        this._ioContext = ioContext;
        _writeContext = RONWriteContext.createRootContext();
    }

    @Override
    public Object getOutputTarget() {
        return _writer;
    }

    /**
     * RON supports true primitives (not wrapped in strings).
     */
    @Override
    public boolean canWriteFormattedNumbers() {
        return true;
    }

    /**
     * RON structs can have optional type names.
     */
    @Override
    public boolean canWriteTypeId() {
        return true;
    }

    @Override
    public boolean canWriteObjectId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canWriteBinaryNatively() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canOmitFields() {
        throw new UnsupportedOperationException();
    }

    /**
     * The RON format currently does not do schemas.
     */
    @Override
    public boolean canUseSchema(FormatSchema schema) {
        return false;
    }

    @Override
    public void close() throws IOException {
        super.close();

        if (_writer != null) {
            if (_ioContext.isResourceManaged() || isEnabled(StreamWriteFeature.AUTO_CLOSE_TARGET)) {
                _writer.close();
            } else if (isEnabled(StreamWriteFeature.FLUSH_PASSED_TO_STREAM)) {
                // If we can't close it, we should at least flush
                _writer.flush();
            }
        }
        // Internal buffer(s) generator has can now be released as well
        _releaseBuffers();
    }

    @Override
    public void writeStartArray() throws IOException {
        _verifyValueWrite("start an array");
        _writeContext = _writeContext.createChildArrayContext();
        _writer.write('[');
    }

    @Override
    public void writeEndArray() throws IOException {
        if (!_writeContext.inAnArray()) {
            _reportError("Current context not Array but "+_writeContext.typeDesc());
        }
        _writer.write(']');
        _writeContext = _writeContext.clearAndGetParent();
    }

    @Override
    public void writeStartObject() throws IOException {
        _verifyValueWrite("start an object");
        _writeContext = _writeContext.createChildObjectContext();
        _writer.write('{');
    }

    @Override
    public void writeEndObject() throws IOException {
        if (!_writeContext.inAnObject()) {
            _reportError("Current context not Object but "+_writeContext.typeDesc());
        }
        _writer.write('}');
        _writeContext = _writeContext.clearAndGetParent();
    }

    @Override
    public void writeStartTuple() throws IOException {
        _verifyValueWrite("start a tuple");
        _writeContext = _writeContext.createChildTupleContext();
        _writer.write('(');
    }

    @Override
    public void writeEndTuple() throws IOException {
        if (!_writeContext.inTuple()) {
            _reportError("Current context not Tuple but "+_writeContext.typeDesc());
        }
        _writer.write(')');
        _writeContext = _writeContext.clearAndGetParent();
    }

    @Override
    public void writeStartStruct() throws IOException {
        _verifyValueWrite("start a struct");
        _writeContext = _writeContext.createChildStructContext();
        _writer.write('(');
    }

    @Override
    public void writeStartStruct(String name) throws IOException {
        _verifyValueWrite("start a named struct");
        _writeContext = _writeContext.createChildStructContext();
        // FIXME escape it properly
        _writer.write(name);
        _writer.write('(');
    }

    @Override
    public void writeEndStruct() throws IOException {
        if (!_writeContext.inStruct()) {
            _reportError("Current context not Struct but "+_writeContext.typeDesc());
        }
        _writer.write(')');
        _writeContext = _writeContext.clearAndGetParent();
    }

    @Override
    public void writeStartEnum(String name) throws IOException {
        _verifyValueWrite("start an enum");
        _writeContext = _writeContext.createChildEnumContext();
        // FIXME escape it properly
        _writer.write(name);
        _writer.write('(');
    }

    @Override
    public void writeEndEnum() throws IOException {
        if (!_writeContext.inEnum()) {
            _reportError("Current context not Enum but "+_writeContext.typeDesc());
        }
        _writer.write(')');
        _writeContext = _writeContext.clearAndGetParent();
    }

    @Override
    public void writeEnum(String name) throws IOException {
        if (name == null) {
            writeNull();
            return;
        }
        _verifyValueWrite("write Enum value");
        // FIXME proper escaping
        _writer.write(name);
    }

    @Override
    public void writeFieldName(String s) throws IOException {
        if (!_writeContext.writeName(s)) {
            _reportError("Cannot write a field name, expecting a value");
        }

        if (_writeContext.hasCurrentIndex()) {
            writeRaw(',');
        }

        if (_writeContext.inStruct()) {
            // (foo:"bar")
            writeRaw(s);
        } else if (_writeContext.inAnObject()) {
            // {"foo":"bar"}
            writeQuotedString(s);
        }
    }

    @Override
    public void writeString(String s) throws IOException {
        if (s == null) {
            writeNull();
            return;
        }
        _verifyValueWrite("write String value");
        writeQuotedString(s);
    }

    private void writeQuotedString(String s) throws IOException {
        writeRaw(DOUBLE_QUOTE);
        // FIXME proper escaping
        writeRaw(s);
        writeRaw(DOUBLE_QUOTE);
    }

    @Override
    public void writeString(char[] chars, int i, int i1) {

    }

    @Override
    public void writeRawUTF8String(byte[] bytes, int i, int i1) {
        _reportUnsupportedOperation();
    }

    @Override
    public void writeUTF8String(byte[] bytes, int i, int i1) {

    }

    @Override
    public void writeRaw(String s) throws IOException {
        _writer.write(s);
    }

    @Override
    public void writeRaw(String s, int offset, int len) {

    }

    @Override
    public void writeRaw(char[] chars, int offset, int len) {

    }

    @Override
    public void writeRaw(char c) throws IOException {
        _writer.write(c);
    }

    @Override
    public void writeBinary(Base64Variant base64Variant, byte[] bytes, int offset, int len) {

    }

    @Override
    public void writeNumber(int i) throws IOException {
        _verifyValueWrite(WRITE_NUMBER);
        writeRaw(String.valueOf(i));
    }

    @Override
    public void writeNumber(long l) throws IOException {
        _verifyValueWrite(WRITE_NUMBER);
        writeRaw(String.valueOf(l));
    }

    @Override
    public void writeNumber(BigInteger bigInteger) throws IOException {
        if (bigInteger == null) {
            writeNull();
            return;
        }
        _verifyValueWrite("write number");
        writeRaw(String.valueOf(bigInteger));
    }

    @Override
    public void writeNumber(double v) throws IOException {
        _verifyValueWrite(WRITE_NUMBER);
        writeRaw(String.valueOf(v));
    }

    @Override
    public void writeNumber(float v) throws IOException {
        _verifyValueWrite(WRITE_NUMBER);
        writeRaw(String.valueOf(v));
    }

    @Override
    public void writeNumber(BigDecimal bigDecimal) throws IOException {
        if (bigDecimal == null) {
            writeNull();
            return;
        }
        _verifyValueWrite("write number");
        writeRaw(String.valueOf(bigDecimal));
    }

    @Override
    public void writeNumber(String s) {

    }

    @Override
    public void writeBoolean(boolean b) throws IOException {
        _verifyValueWrite(WRITE_BOOLEAN);
        _writer.write(String.valueOf(b));
    }

    /**
     * RON does not support null values.
     */
    @Override
    public void writeNull() {
        _reportUnsupportedOperation();
    }

    @Override
    public void flush() throws IOException {
        if (_writer != null) {
            if (isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
                _writer.flush();
            }
        }
    }

    @Override
    protected void _releaseBuffers() {
        // no-op
    }

    @Override
    protected void _verifyValueWrite(String typeMsg) throws IOException {
        // first, check that name/value cadence works
        if (!_writeContext.writeValue()) {
            _reportError("Cannot "+typeMsg+", expecting a field name");
        }

        if (_writeContext.inAnArray() || _writeContext.inTuple() || _writeContext.inEnum()) {
            if (_writeContext.getCurrentIndex() != 0) {
                // TODO change behavior depending on RONWriteFeature.TRAILING_COMMAS
                writeRaw(",");
            }
        } else if (_writeContext.inStruct() || _writeContext.inAnObject()) {
            writeRaw(":");
        }
    }

    @Override
    protected void _reportUnsupportedOperation() {
        throw new UnsupportedOperationException("Operation not supported by generator of type "+getClass().getName());
    }
}
