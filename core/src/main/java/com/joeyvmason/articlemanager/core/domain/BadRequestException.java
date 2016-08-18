package com.joeyvmason.articlemanager.core.domain;

public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 7607967372651013186L;

    public BadRequestException() {
        super("Bad request");
    }

    public BadRequestException(String message) {
        super(message);
    }

}
