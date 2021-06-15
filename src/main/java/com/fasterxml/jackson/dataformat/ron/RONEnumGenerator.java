package com.fasterxml.jackson.dataformat.ron;

import java.io.IOException;

public interface RONEnumGenerator {
    /**
     * Write the start of an enum that has child fields.
     */
    void writeStartEnum(String name) throws IOException;

    /**
     * Write the end of an enum.
     */
    void writeEndEnum() throws IOException;

    /**
     * Write an enum.
     */
    void writeEnum(String name) throws IOException;
}
