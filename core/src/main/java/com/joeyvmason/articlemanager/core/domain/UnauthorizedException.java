package com.joeyvmason.articlemanager.core.domain;

public class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = -936887092298006910L;
    
    public UnauthorizedException() {
        super("Login required");
    }
    
    public UnauthorizedException(String message) {
        super(message);
    }
}
