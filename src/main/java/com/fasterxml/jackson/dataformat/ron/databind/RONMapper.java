package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.ron.PackageVersion;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONLexer;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;
import com.fasterxml.jackson.dataformat.ron.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.dataformat.ron.databind.ser.RONSerializerFactory;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import java.io.IOException;
import java.io.Reader;

public class RONMapper extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    /**
     * Convenience constructor
     */
    public RONMapper() {
        this(new RONFactory());
    }


    /**
     * Main constructor
     */
    public RONMapper(RONFactory f) {
        super(f);

        // Default de/serializer factories are stateless, can just assign
        _serializerFactory = RONSerializerFactory.instance;
    }

    @Override
    protected Object _readMapAndClose(JsonParser p0, JavaType valueType)
            throws IOException {
        try (com.fasterxml.jackson.dataformat.ron.parser.RONParser p = (com.fasterxml.jackson.dataformat.ron.parser.RONParser) p0) {
            final DeserializationConfig cfg = getDeserializationConfig();

            Reader reader = p.getInputSource();

            if (reader == null) {
                throw new JsonParseException(p, "Did not find a Reader in the JsonParser to use");
            }

            CharStream charStream = CharStreams.fromReader(reader);

            RONLexer lexer = new RONLexer(charStream);
            TokenStream tokens = new CommonTokenStream(lexer);

            RONParser parser = new RONParser(tokens);

            final BeanDeserializer bd = new BeanDeserializer(valueType, cfg);

            return bd.visitRoot(parser.root());
        }
    }

    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }
}
