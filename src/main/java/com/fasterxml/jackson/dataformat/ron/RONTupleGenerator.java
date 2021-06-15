package com.fasterxml.jackson.dataformat.ron;

import java.io.IOException;

public interface RONTupleGenerator {
    /**
     * Write the '(' which is the start of a tuple.
     */
    void writeStartTuple() throws IOException;

    /**
     * Write the ')' which is the end of a tuple.
     */
    void writeEndTuple() throws IOException;
}
