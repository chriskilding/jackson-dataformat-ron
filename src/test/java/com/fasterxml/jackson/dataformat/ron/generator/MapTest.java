package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class MapTest extends ContainerTest {
    @Override
    public void empty() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeStartObject();
            generator.writeEndObject();
        }
        assertEquals("{}", w.toString());
    }

    @Override
    public void one() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeStartObject();
            generator.writeFieldName("foo");
            generator.writeNumber(1);
            generator.writeEndObject();
        }
        assertEquals("{\"foo\":1}", w.toString());
    }

    @Override
    public void multiple() throws IOException {
        StringWriter w = new StringWriter();
        try (JsonGenerator generator = new RONMapper().createGenerator(w)) {
            generator.writeStartObject();
            generator.writeFieldName("foo");
            generator.writeNumber(1);
            generator.writeFieldName("bar");
            generator.writeNumber(2);
            generator.writeEndObject();
        }
        assertEquals("{\"foo\":1,\"bar\":2}", w.toString());
    }
}
