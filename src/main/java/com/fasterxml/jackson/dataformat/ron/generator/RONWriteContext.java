package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.dataformat.ron.RONContextType;

class RONWriteContext extends JsonStreamContext {

    /**
     * Indicates logical type of context.
     */
    private final RONContextType type;

    /**
     * Parent context for this context; null for root context.
     */
    private final RONWriteContext parent;

    /**
     * Marker used to indicate that we just received a name, and
     * now expect a value
     */
    private boolean gotName;

    /**
     * Name of the field of which value is to be parsed; only
     * used for OBJECT contexts
     */
    private String currentName;

    RONWriteContext(RONContextType type, RONWriteContext parent) {
        super();
        _index = -1;
        this.type = type;
        this.parent = parent;
        this.gotName = false;
    }

    static RONWriteContext createRootContext() {
        return new RONWriteContext(RONContextType.ROOT, null);
    }

    public RONWriteContext createChildArrayContext() {
        return new RONWriteContext(RONContextType.ARRAY, this);
    }

    public RONWriteContext createChildTupleContext() {
        return new RONWriteContext(RONContextType.TUPLE, this);
    }

    public RONWriteContext createChildEnumContext() {
        return new RONWriteContext(RONContextType.ENUM, this);
    }

    public RONWriteContext createChildStructContext() {
        return new RONWriteContext(RONContextType.STRUCT, this);
    }

    public RONWriteContext createChildObjectContext() {
        return new RONWriteContext(RONContextType.OBJECT, this);
    }

    public RONWriteContext clearAndGetParent() {
        currentName = null;
        return this.getParent();
    }

    @Override
    public RONWriteContext getParent() {
        return parent;
    }

    @Override
    public String getCurrentName() {
        return currentName;
    }

    public boolean writeName(String name) {
        if (gotName) {
            return false;
        }
        gotName = true;
        currentName = name;
        return true;
    }

    public boolean writeValue() {
        if (type == RONContextType.OBJECT || type == RONContextType.STRUCT) {
            if (!gotName) {
                return false;
            }
            gotName = false;
        }
        // Array fine, and must allow root context for Object values too so...
        ++_index;
        return true;
    }

    // TODO cannot @Override inArray(); must ask jackson-core to remove final modifier
    public boolean inAnArray() {
        return type == RONContextType.ARRAY;
    }

    // TODO cannot @Override inObject(); must ask jackson-core to remove final modifier
    public boolean inAnObject() {
        return type == RONContextType.OBJECT;
    }

    public boolean inEnum() {
        return type == RONContextType.ENUM;
    }

    public boolean inTuple() {
        return type == RONContextType.TUPLE;
    }

    public boolean inStruct() {
        return type == RONContextType.STRUCT;
    }
}
