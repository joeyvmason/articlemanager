package com.joeyvmason.articlemanager.core.domain;

public class InternalServerErrorException extends RuntimeException {
    private static final long serialVersionUID = 1421773495828685482L;

    public InternalServerErrorException(Exception e) {
        super(e);
    }

    public InternalServerErrorException(String message) {
        super(message);
    }
}
