package com.fasterxml.jackson.dataformat.ron.databind.deser;

public class VisitorException extends RuntimeException {
    public VisitorException(String msg) {
        super(msg);
    }

    public VisitorException(String msg, Throwable e) {
        super(msg, e);
    }
}
