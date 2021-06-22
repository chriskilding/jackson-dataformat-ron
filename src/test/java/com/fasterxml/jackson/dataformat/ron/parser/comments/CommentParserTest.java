package com.fasterxml.jackson.dataformat.ron.parser.comments;

import org.junit.Test;

import java.io.IOException;

/**
 * Ensure that the parser ignores RON comments.
 */
public abstract class CommentParserTest {

    @Test
    public abstract void testIgnoresInlineComments() throws IOException;

    @Test
    public abstract void testIgnoresBlockComments() throws IOException;

    @Test
    public abstract void testIgnoresMultilineBlockComments() throws IOException;

}
