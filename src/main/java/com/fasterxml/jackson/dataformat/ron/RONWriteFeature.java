package com.fasterxml.jackson.dataformat.ron;

import com.fasterxml.jackson.core.FormatFeature;

public enum RONWriteFeature implements FormatFeature {

    /**
     * Whether to write trailing commas after the last field in a structure.
     */
    TRAILING_COMMAS(false);

    private final boolean _defaultState;
    private final int _mask;

    RONWriteFeature(boolean defaultState) {
        _defaultState = defaultState;
        _mask = (1 << ordinal());
    }

    @Override
    public boolean enabledByDefault() { return _defaultState; }


    @Override
    public int getMask() { return _mask; }

    @Override
    public boolean enabledIn(int flags) { return (flags & _mask) != 0; }
}
