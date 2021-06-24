package com.fasterxml.jackson.dataformat.ron.parser;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

/**
 * Wrapper for the reader with convenience methods, in the style of Rust's `bytes`.
 */
public class ConvenientReader extends Reader {

    protected final static int INT_TAB = '\t';
    protected final static int INT_LF = '\n';
    protected final static int INT_CR = '\r';
    protected final static int INT_SPACE = 0x0020;

    private final PushbackReader reader;

    public ConvenientReader(PushbackReader reader) {
        this.reader = reader;
    }

    /**
     * Walk forward in the stream to skip whitespace.
     */
    void skipWhitespace() throws IOException {
        while (reader.ready()) {
            final int c = reader.read();
            switch (c) {
                case INT_TAB:
                case INT_CR:
                case INT_LF:
                case INT_SPACE:
                    continue;
                default:
                    reader.unread(c);
                    return;
            }
        }
    }

    /**
     * Walk forward in the stream, matching and consuming the specified string.
     *
     * @return whether the string was consumed in its entirety
     */
    boolean consume(String str) throws IOException {
        for (char expected: str.toCharArray()) {
            final char actual = (char) reader.read();
            if (expected != actual) {
                return false;
            }
        }

        return true;
    }

    /**
     * Extract the next character in the stream as a char.
     */
    char readChar() throws IOException {
        return (char) reader.read();
    }

    /**
     * Extract the next characters in the stream as an int.
     */
    int readInt() {
        return 0;
    }

    /**
     * Extract the next characters in the stream as a float.
     */
    float readFloat() {
        return 0;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return reader.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }
}
