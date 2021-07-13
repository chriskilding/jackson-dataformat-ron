package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParserNextTokenTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "[", JsonToken.START_ARRAY },
                { "]", JsonToken.END_ARRAY },
                { "{", JsonToken.START_OBJECT },
                { "}", JsonToken.END_OBJECT },
                { "true", JsonToken.VALUE_TRUE },
                { "false", JsonToken.VALUE_FALSE },
                { "inf", JsonToken.VALUE_NUMBER_FLOAT },
                { "-inf", JsonToken.VALUE_NUMBER_FLOAT },
                { "NaN", JsonToken.VALUE_NUMBER_FLOAT },
                { "\"foo\"", JsonToken.VALUE_STRING },
                { "123", JsonToken.VALUE_NUMBER_INT },
                { "", null }
        });
    }

    public ParserNextTokenTest(String input, JsonToken expected) {
        this.input = input;
        this.expected = expected;
    }

    private final String input;
    private final JsonToken expected;

    private static RONParser newParser(String ron) throws IOException {
        Reader reader = new StringReader(ron);
        return new RONFactory().createParser(reader);
    }

    @Test
    public void testNextToken() throws IOException {
        try (JsonParser parser = newParser(this.input)) {
            assertEquals(this.expected, parser.nextToken());
        }
    }
}
