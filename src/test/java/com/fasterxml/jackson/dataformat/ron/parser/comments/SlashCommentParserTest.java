package com.fasterxml.jackson.dataformat.ron.parser.comments;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import com.fasterxml.jackson.dataformat.ron.parser.RONParser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Ensure the RON parser ignores slash-style comments.
 */
public class SlashCommentParserTest extends CommentParserTest {

    @Override
    public void testIgnoresBlockComments() throws IOException {
        Reader ron = new StringReader("// comment");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertNull(parser.nextToken());
        }
    }

    @Override
    public void testIgnoresMultilineBlockComments() throws IOException {
        String comment =
                "// comment\n" +
                "// further comment\n";
        Reader ron = new StringReader(comment);

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertNull(parser.nextToken());
        }
    }

    @Override
    public void testIgnoresInlineComments() throws IOException {
        Reader ron = new StringReader("[] // comment");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            assertEquals(JsonToken.START_ARRAY, parser.nextToken());
            assertEquals(JsonToken.END_ARRAY, parser.nextToken());
            assertNull(parser.nextToken());
        }
    }

}
