package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.dataformat.ron.PackageVersion;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import com.fasterxml.jackson.dataformat.ron.databind.ser.RONSerializerFactory;
import com.fasterxml.jackson.dataformat.ron.generator.RONWriteFeature;

import java.util.Collection;

public class RONMapper extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    public static class Builder extends MapperBuilder<RONMapper, Builder> {
        public Builder(RONMapper m) {
            super(m);
        }
    }

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

        // Default serializer factory is stateless, can just assign
        _serializerFactory = RONSerializerFactory.instance;
    }

    /**
     * Copy constructor
     */
    protected RONMapper(RONMapper src) {
        super(src);
    }

    @SuppressWarnings("unchecked")
    public static RONMapper.Builder builder() {
        return new Builder(new RONMapper());
    }

    public static Builder builder(RONFactory streamFactory) {
        return new Builder(new RONMapper(streamFactory));
    }

    @Override
    public RONMapper copy() {
        _checkInvalidCopy(RONMapper.class);
        return new RONMapper(this);
    }

    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }

    @Override
    public RONFactory getFactory() {
        return (RONFactory) _jsonFactory;
    }

    public RONMapper enable(RONWriteFeature f) {
        _serializationConfig = _serializationConfig.with(f);
        return this;
    }

    private enum ThingType {
        ARRAY, BOOLEAN, ENUM, NUMBER, STRING, STRUCT, TUPLE
    }

    private ThingType _determineType(Class<?> propType) {
        // very first thing: arrays
        if (propType.isArray()) {
            return ThingType.ARRAY;
        }

        if (propType.isEnum()) {
            return ThingType.ENUM;
        }

        // First let's check certain cases that ought to be just presented as Strings...
        if (propType == String.class
                || propType == Character.TYPE
                || propType == Character.class) {
            return ThingType.STRING;
        }
        if (propType == Boolean.class
                || propType == Boolean.TYPE) {
            return ThingType.BOOLEAN;
        }

        // all primitive types are good for NUMBER, since 'char', 'boolean' handled above
        if (propType.isPrimitive()) {
            return ThingType.NUMBER;
        }
        if (Number.class.isAssignableFrom(propType)) {
            return ThingType.NUMBER;
        }
        if (Collection.class.isAssignableFrom(propType)) { // since 2.5
            return ThingType.ARRAY;
        }
        // but in general we will just do what we can:
        return null; // ThingType.NUMBER_OR_STRING;
    }


}
