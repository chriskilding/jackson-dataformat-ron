package com.fasterxml.jackson.dataformat.ron;

import com.fasterxml.jackson.core.JsonStreamContext;

public class RONWriteContext extends JsonStreamContext {

    public enum ContextType {
        ROOT, ARRAY, OBJECT, STRUCT, TUPLE, ENUM
    }

    /**
     * Indicates logical type of context.
     */
    protected ContextType _type;

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

    RONWriteContext(ContextType type, RONWriteContext parent)
    {
        super();
        _type = type;
        _parent = parent;
        _index = -1;
        _gotName = false;
    }

    static RONWriteContext createRootContext() {
        return new RONWriteContext(ContextType.ROOT, null);
    }

    public RONWriteContext createChildArrayContext() {
        RONWriteContext ctxt = _child;
        if (ctxt == null) {
            _child = ctxt = new RONWriteContext(ContextType.ARRAY, this);
            return ctxt;
        }
        ctxt.reset(ContextType.ARRAY);
        return ctxt;
    }

    private void reset(ContextType type) {
        _type = type;
        _currentName = null;
        _index = -1;
    }

    public RONWriteContext createChildTupleContext() {
        RONWriteContext ctxt = _child;
        if (ctxt == null) {
            _child = ctxt = new RONWriteContext(ContextType.TUPLE, this);
            return ctxt;
        }
        ctxt.reset(ContextType.TUPLE);
        return ctxt;
    }

    public RONWriteContext createChildEnumContext() {
        RONWriteContext ctxt = _child;
        if (ctxt == null) {
            _child = ctxt = new RONWriteContext(ContextType.ENUM, this);
            return ctxt;
        }
        ctxt.reset(ContextType.ENUM);
        return ctxt;
    }

    public RONWriteContext createChildStructContext() {
        RONWriteContext ctxt = _child;
        if (ctxt == null) {
            _child = ctxt = new RONWriteContext(ContextType.STRUCT, this);
            return ctxt;
        }
        ctxt.reset(ContextType.STRUCT);
        return ctxt;
    }

    public RONWriteContext createChildObjectContext() {
        RONWriteContext ctxt = _child;
        if (ctxt == null) {
            _child = ctxt = new RONWriteContext(ContextType.OBJECT, this);
            return ctxt;
        }
        ctxt.reset(ContextType.OBJECT);
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
        if (_type == ContextType.OBJECT || _type == ContextType.STRUCT) {
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
        return _type == ContextType.ARRAY;
    }

    // FIXME cannot @Override inObject(); must ask jackson-core to remove final modifier
    public boolean inAnObject() {
        return _type == ContextType.OBJECT;
    }

    // FIXME cannot @Override inRoot(); must ask jackson-core to remove final modifier
    public boolean inTheRoot() {
        return _type == ContextType.ROOT;
    }

    public boolean inEnum() {
        return _type == ContextType.ENUM;
    }

    public boolean inTuple() {
        return _type == ContextType.TUPLE;
    }

    public boolean inStruct() {
        return _type == ContextType.STRUCT;
    }
}
