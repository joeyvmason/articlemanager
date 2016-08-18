package com.joeyvmason.articlemanager.core.domain;


import com.joeyvmason.articlemanager.core.domain.users.User;

public class ForbiddenException extends RuntimeException {
    private static final long serialVersionUID = -5038657029649050722L;

    private User user;

    public ForbiddenException(User user) {
        super("Access to requested resource is forbidden");
        this.user = user;
    }

    public ForbiddenException(User user, String message) {
        super(message);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
