package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.dataformat.ron.PackageVersion;
import com.fasterxml.jackson.dataformat.ron.RONFactory;

public class RONMapper extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    /**
     * Base implementation for "Vanilla" {@link ObjectMapper}, used with
     * RON backend.
     *
     */
    public static class Builder extends MapperBuilder<RONMapper, Builder>
    {
        public Builder(RONMapper m) {
            super(m);
        }
    }

    /*
    /**********************************************************
    /* Life-cycle
    /**********************************************************
     */

    public RONMapper() {
        this(new RONFactory());
    }

    public RONMapper(RONFactory f) {
        super(f);
    }

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
    public RONMapper copy()
    {
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
