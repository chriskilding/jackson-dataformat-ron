package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.dataformat.ron.PackageVersion;
import com.fasterxml.jackson.dataformat.ron.RONFactory;
import com.fasterxml.jackson.dataformat.ron.databind.ser.RONSerializerFactory;

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

        // Default serializer factory is stateless, can just assign
        _serializerFactory = RONSerializerFactory.instance;
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
    public Version version() {
        return PackageVersion.VERSION;
    }

    @Override
    public RONFactory getFactory() {
        return (RONFactory) _jsonFactory;
    }

}
