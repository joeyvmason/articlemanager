package com.joeyvmason.articlemanager.core.domain;


public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 7880775588770243286L;

    public NotFoundException() {
        super("Resource not found");
    }

    public NotFoundException(String message) {
        super(message);
    }

}

