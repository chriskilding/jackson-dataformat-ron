package com.fasterxml.jackson.dataformat.ron.generator;

import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.dataformat.ron.RONContextType;

public class RONWriteContext extends JsonStreamContext {

    /**
     * Indicates logical type of context.
     */
    protected RONContextType _type;

    /**
     * Parent context for this context; null for root context.
     */
    protected final RONWriteContext _parent;

    /**
     * Marker used to indicate that we just received a name, and
     * now expect a value
     */
    protected boolean _gotName;

    protected RONWriteContext _child = null;

    /**
     * Name of the field of which value is to be parsed; only
     * used for OBJECT contexts
     */
    protected String _currentName;

    RONWriteContext(RONContextType type, RONWriteContext parent)
    {
        super();
        _type = type;
        _parent = parent;
        _index = -1;
        _gotName = false;
    }

    static RONWriteContext createRootContext() {
        return new RONWriteContext(RONContextType.ROOT, null);
    }

    public RONWriteContext createChildArrayContext() {
        RONWriteContext ctxt = _child;
        if (ctxt == null) {
            _child = ctxt = new RONWriteContext(RONContextType.ARRAY, this);
            return ctxt;
        }
        ctxt.reset(RONContextType.ARRAY);
        return ctxt;
    }

    private void reset(RONContextType type) {
        _type = type;
        _currentName = null;
        _index = -1;
    }

    public RONWriteContext createChildTupleContext() {
        RONWriteContext ctxt = _child;
        if (ctxt == null) {
            _child = ctxt = new RONWriteContext(RONContextType.TUPLE, this);
            return ctxt;
        }
        ctxt.reset(RONContextType.TUPLE);
        return ctxt;
    }

    public RONWriteContext createChildEnumContext() {
        RONWriteContext ctxt = _child;
        if (ctxt == null) {
            _child = ctxt = new RONWriteContext(RONContextType.ENUM, this);
            return ctxt;
        }
        ctxt.reset(RONContextType.ENUM);
        return ctxt;
    }

    public RONWriteContext createChildStructContext() {
        RONWriteContext ctxt = _child;
        if (ctxt == null) {
            _child = ctxt = new RONWriteContext(RONContextType.STRUCT, this);
            return ctxt;
        }
        ctxt.reset(RONContextType.STRUCT);
        return ctxt;
    }

    public RONWriteContext createChildObjectContext() {
        RONWriteContext ctxt = _child;
        if (ctxt == null) {
            _child = ctxt = new RONWriteContext(RONContextType.OBJECT, this);
            return ctxt;
        }
        ctxt.reset(RONContextType.OBJECT);
        return ctxt;
    }

    public RONWriteContext clearAndGetParent() {
//        _currentValue = null;
        // could also clear the current name, but seems cheap enough to leave?
        return _parent;
    }

    @Override
    public JsonStreamContext getParent() {
        return _parent;
    }

    @Override
    public String getCurrentName() {
        return _currentName;
    }

    public boolean writeName(String name) {
        if (_gotName) {
            return false;
        }
        _gotName = true;
        _currentName = name;
        return true;
    }

    public boolean writeValue() {
        // Most likely, object:
        if (_type == RONContextType.OBJECT || _type == RONContextType.STRUCT) {
            if (!_gotName) {
                return false;
            }
            _gotName = false;
        }
        // Array fine, and must allow root context for Object values too so...
        ++_index;
        return true;
    }

    // FIXME cannot @Override inArray(); must ask jackson-core to remove final modifier
    public boolean inAnArray() {
        return _type == RONContextType.ARRAY;
    }

    // FIXME cannot @Override inObject(); must ask jackson-core to remove final modifier
    public boolean inAnObject() {
        return _type == RONContextType.OBJECT;
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
}
