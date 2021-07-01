package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.dataformat.ron.PackageVersion;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONLexer;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;
import com.fasterxml.jackson.dataformat.ron.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.dataformat.ron.databind.deser.RONDeserializerFactory;
import com.fasterxml.jackson.dataformat.ron.databind.ser.RONSerializerFactory;
import org.antlr.v4.runtime.*;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.channels.ReadableByteChannel;

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

        _deserializationContext = new DefaultDeserializationContext.Impl(RONDeserializerFactory.instance);
    }

    /**
     * Copy constructor
     */
    protected RONMapper(RONMapper src) {
        super(src);
    }

    @Override
    public RONMapper copy() {
        _checkInvalidCopy(RONMapper.class);
        return new RONMapper(this);
    }

    @Override
    protected Object _readMapAndClose(JsonParser p0, JavaType valueType)
            throws IOException {
        // Delegate to regular JSON parser for the JSON subset of RON
        if (valueType.isArrayType() || valueType.isMapLikeType() || valueType.isCollectionLikeType()) {
            return super._readMapAndClose(p0, valueType);
        }

        try (com.fasterxml.jackson.dataformat.ron.parser.RONParser p = (com.fasterxml.jackson.dataformat.ron.parser.RONParser) p0) {
            final DeserializationConfig cfg = getDeserializationConfig();
            final DefaultDeserializationContext ctxt = createDeserializationContext(p, cfg);

            Reader reader = (Reader) p.getInputSource();

            if (reader == null) {
                throw new JsonParseException(p, "Did not find a Reader in the JsonParser to use");
            }

            CharStream charStream = CharStreams.fromReader(reader);

            RONLexer lexer = new RONLexer(charStream);
            TokenStream tokens = new CommonTokenStream(lexer);

            RONParser parser = new RONParser(tokens);

            final BeanDeserializer bd = new BeanDeserializer(valueType, cfg);
//            JsonToken t = _initForReading(p, valueType);
//            if (t == JsonToken.VALUE_NULL) {
//                 Ask JsonDeserializer what 'null value' to use:
//                result = _findRootDeserializer(ctxt, valueType).getNullValue(ctxt);
//            } else if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
//                result = null;
//            } else {
            return bd.visitRoot(parser.root());
//                ctxt.checkUnresolvedObjectId();
//            }
        }
    }

    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }

    @Override
    public RONFactory getFactory() {
        return (RONFactory) _jsonFactory;
    }

}
