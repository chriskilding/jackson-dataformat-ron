package com.fasterxml.jackson.dataformat.ron.parser;

import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.dataformat.ron.RONContextType;

public class RONReadContext extends JsonStreamContext {

    /**
     * Indicates logical type of context.
     */
    protected RONContextType _type;

    @Override
    public JsonStreamContext getParent() {
        return null;
    }

    @Override
    public String getCurrentName() {
        return null;
    }

    public RONReadContext createChildArrayContext() {
        throw new UnsupportedOperationException();
    }

    public RONReadContext createChildObjectContext() {
        throw new UnsupportedOperationException();
    }

    public RONReadContext createChildStructContext() {
        throw new UnsupportedOperationException();
    }

    public RONReadContext createChildStringContext() {
        throw new UnsupportedOperationException();
    }

    // FIXME cannot @Override inArray(); must ask jackson-core to remove final modifier
    public boolean inAnArray() {
        return _type == RONContextType.ARRAY;
    }

    // FIXME cannot @Override inObject(); must ask jackson-core to remove final modifier
    public boolean inAnObject() {
        return _type == RONContextType.OBJECT;
    }

    // FIXME cannot @Override inRoot(); must ask jackson-core to remove final modifier
    public boolean inTheRoot() {
        return _type == RONContextType.ROOT;
    }

    public boolean inEnum() {
        return _type == RONContextType.ENUM;
    }

    public boolean inTuple() {
        return _type == RONContextType.TUPLE;
    }

    public boolean inStruct() {
        return _type == RONContextType.STRUCT;
    }

    public boolean inString() {
        throw new UnsupportedOperationException();
    }
}
