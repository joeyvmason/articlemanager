package com.joeyvmason.articlemanager.web.application.auth;


import com.joeyvmason.articlemanager.core.domain.users.User;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authorized {
    User.Role[] withRoles() default {};
}
