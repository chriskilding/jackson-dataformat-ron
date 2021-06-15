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
        throw new UnsupportedOperationException();
    }

    public RONWriteContext createChildEnumContext() {
        throw new UnsupportedOperationException();
    }

    public RONWriteContext createChildStructContext() {
        throw new UnsupportedOperationException();
    }

    public RONWriteContext createChildObjectContext() {
        throw new UnsupportedOperationException();
    }

    public RONWriteContext clearAndGetParent() {
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

    // FIXME this is currently not possible; must ask jackson-core to remove final modifier
    //    @Override
    //    public boolean inArray() {}

    // FIXME this is currently not possible; must ask jackson-core to remove final modifier
    //    @Override
    //    public boolean inObject() {}

    public boolean inEnum() {
        throw new UnsupportedOperationException();
    }

    public boolean inTuple() {
        throw new UnsupportedOperationException();
    }

    public boolean inStruct() {
        throw new UnsupportedOperationException();
    }
}
