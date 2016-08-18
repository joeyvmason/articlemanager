package com.joeyvmason.articlemanager.web.application.auth;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggedInAuthToken {
}
