package com.joeyvmason.articlemanager.core.domain;


import com.joeyvmason.articlemanager.core.domain.users.User;

public interface Form<ENTITY> {

    public ENTITY toEntity(User loggedInUser);

    public ENTITY update(ENTITY existing, User loggedInUser);
}
