package com.fasterxml.jackson.dataformat.ron;

import java.io.IOException;

public interface RONStructGenerator {
    /**
     * Write the start of a struct.
     */
    void writeStartStruct() throws IOException;

    /**
     * Write the start of a named struct.
     */
    void writeStartStruct(String name) throws IOException;

    /**
     * Write the end of a struct.
     */
    void writeEndStruct() throws IOException;
}
