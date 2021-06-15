package com.fasterxml.jackson.dataformat.ron;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.NumberOutput;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

public class RONGenerator extends GeneratorBase implements RONEnumGenerator, RONStructGenerator, RONTupleGenerator {

    protected final Writer _writer;

    /**
     * Object that keeps track of the current contextual state
     * of the generator.
     */
    protected RONWriteContext _writeContext;

    /**
     * Intermediate buffer in which contents are buffered before
     * being written using {@link #_writer}.
     */
    protected char[] _outputBuffer;

    /**
     * Pointer to the first buffered character to output
     */
    protected int _outputHead;

    /**
     * Pointer to the position right beyond the last character to output
     * (end marker; may point to position right beyond the end of the buffer)
     */
    protected int _outputTail;

    /**
     * End marker of the output buffer; one past the last valid position
     * within the buffer.
     */
    protected int _outputEnd;

    /**
     * Intermediate buffer in which characters of a String are copied
     * before being encoded.
     *
     * @since 2.10
     */
    protected char[] _copyBuffer;

    final protected IOContext _ioContext;

    /**
     * Character used for quoting JSON Object property names
     * and String values.
     */
    protected final char _quoteChar = JsonFactory.DEFAULT_QUOTE_CHAR;

    /**
     * Separator to use, if any, between root-level values.
     *
     * @since 2.1
     */
    protected final SerializableString _rootValueSeparator
            = new SerializedString(" ");

    protected RONGenerator(int features, ObjectCodec codec, Writer writer, IOContext ioContext) {
        super(features, codec);
        this._writer = writer;
        this._ioContext = ioContext;
        _writeContext = RONWriteContext.createRootContext();
        _outputBuffer = ioContext.allocConcatBuffer();
        _outputEnd = _outputBuffer.length;
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
    public void writeStartArray() throws IOException {
        _verifyValueWrite("start an array");
        _writeContext = _writeContext.createChildArrayContext();
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '[';
    }

    protected void _flushBuffer() throws IOException
    {
        int len = _outputTail - _outputHead;
        if (len > 0) {
            int offset = _outputHead;
            _outputTail = _outputHead = 0;
            _writer.write(_outputBuffer, offset, len);
        }
    }

    @Override
    public void writeEndArray() throws IOException {
        if (!_writeContext.inArray()) {
            _reportError("Current context not Array but "+_writeContext.typeDesc());
        }
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = ']';
        _writeContext = _writeContext.clearAndGetParent();
    }

    @Override
    public void writeStartObject() throws IOException {
        _verifyValueWrite("start an object");
        _writeContext = _writeContext.createChildObjectContext();
        if (_cfgPrettyPrinter != null) {
            _cfgPrettyPrinter.writeStartObject(this);
        } else {
            if (_outputTail >= _outputEnd) {
                _flushBuffer();
            }
            _outputBuffer[_outputTail++] = '{';
        }
    }

    @Override
    public void writeEndObject() throws IOException {
        if (!_writeContext.inObject()) {
            _reportError("Current context not Object but "+_writeContext.typeDesc());
        }
        if (_cfgPrettyPrinter != null) {
            _cfgPrettyPrinter.writeEndObject(this, _writeContext.getEntryCount());
        } else {
            if (_outputTail >= _outputEnd) {
                _flushBuffer();
            }
            _outputBuffer[_outputTail++] = '}';
        }
        _writeContext = _writeContext.clearAndGetParent();
    }

    @Override
    public void writeStartTuple() throws IOException {
        _verifyValueWrite("start a tuple");
        _writeContext = _writeContext.createChildTupleContext();
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '(';
    }

    @Override
    public void writeEndTuple() throws IOException {
        if (!_writeContext.inTuple()) {
            _reportError("Current context not Tuple but "+_writeContext.typeDesc());
        }
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = ')';
        _writeContext = _writeContext.clearAndGetParent();
    }

    @Override
    public void writeStartStruct() throws IOException {
        _verifyValueWrite("start a struct");
        _writeContext = _writeContext.createChildStructContext();
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = '(';
    }

    @Override
    public void writeStartStruct(String name) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeEndStruct() throws IOException {
        if (!_writeContext.inStruct()) {
            _reportError("Current context not Struct but "+_writeContext.typeDesc());
        }
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = ')';
        _writeContext = _writeContext.clearAndGetParent();
    }

    @Override
    public void writeStartEnum(String name) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeEndEnum() throws IOException {
        if (!_writeContext.inEnum()) {
            _reportError("Current context not Enum but "+_writeContext.typeDesc());
        }
        if (_outputTail >= _outputEnd) {
            _flushBuffer();
        }
        _outputBuffer[_outputTail++] = ')';
        _writeContext = _writeContext.clearAndGetParent();
    }

    @Override
    public void writeEnum(String name) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeFieldName(String s) throws IOException {

    }

    @Override
    public void writeString(String s) throws IOException {
        _verifyValueWrite(WRITE_STRING);

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
        _reportUnsupportedOperation();
    }

    @Override
    public void writeNumber(int i) throws IOException {
        _verifyValueWrite(WRITE_NUMBER);
        // up to 10 digits and possible minus sign
        if ((_outputTail + 11) >= _outputEnd) {
            _flushBuffer();
        }
        _outputTail = NumberOutput.outputInt(i, _outputBuffer, _outputTail);
    }

    @Override
    public void writeNumber(long l) throws IOException {
        _verifyValueWrite(WRITE_NUMBER);
        if ((_outputTail + 21) >= _outputEnd) {
            // up to 20 digits, minus sign
            _flushBuffer();
        }
        _outputTail = NumberOutput.outputLong(l, _outputBuffer, _outputTail);
    }

    @Override
    public void writeNumber(BigInteger bigInteger) throws IOException {

    }

    @Override
    public void writeNumber(double v) throws IOException {
        // What is the max length for doubles? 40 chars?
        _verifyValueWrite(WRITE_NUMBER);
        writeRaw(String.valueOf(v));
    }

    @Override
    public void writeNumber(float v) throws IOException {
        // What is the max length for floats?
        _verifyValueWrite(WRITE_NUMBER);
        writeRaw(String.valueOf(v));
    }

    @Override
    public void writeNumber(BigDecimal bigDecimal) throws IOException {

    }

    @Override
    public void writeNumber(String s) throws IOException {

    }

    @Override
    public void writeBoolean(boolean b) throws IOException {
        _verifyValueWrite(WRITE_BOOLEAN);
        if ((_outputTail + 5) >= _outputEnd) {
            _flushBuffer();
        }
        int ptr = _outputTail;
        char[] buf = _outputBuffer;
        if (b) {
            buf[ptr] = 't';
            buf[++ptr] = 'r';
            buf[++ptr] = 'u';
            buf[++ptr] = 'e';
        } else {
            buf[ptr] = 'f';
            buf[++ptr] = 'a';
            buf[++ptr] = 'l';
            buf[++ptr] = 's';
            buf[++ptr] = 'e';
        }
        _outputTail = ptr+1;
    }

    @Override
    public void writeNull() throws IOException {

    }

    @Override
    public void flush() throws IOException {
        _flushBuffer();
        if (_writer != null) {
            if (isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
                _writer.flush();
            }
        }
    }

    @Override
    protected void _releaseBuffers() {
        char[] buf = _outputBuffer;
        if (buf != null) {
            _outputBuffer = null;
            _ioContext.releaseConcatBuffer(buf);
        }
        buf = _copyBuffer;
        if (buf != null) {
            _copyBuffer = null;
            _ioContext.releaseNameCopyBuffer(buf);
        }
    }

    @Override
    protected void _verifyValueWrite(String s) throws IOException {
        // FIXME implement
    }

    @Override
    protected void _reportUnsupportedOperation() {
        throw new UnsupportedOperationException("Operation not supported by generator of type "+getClass().getName());
    }
}
