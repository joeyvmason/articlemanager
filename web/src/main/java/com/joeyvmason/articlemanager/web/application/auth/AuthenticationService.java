package com.joeyvmason.articlemanager.web.application.auth;

import com.joeyvmason.articlemanager.core.domain.auth.AuthToken;
import com.joeyvmason.articlemanager.core.domain.users.User;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    String X_AUTH_TOKEN = "X-Auth-Token";
    String LOGGED_IN_USER = "loggedInUser";
    String LOGGED_IN_AUTH_TOKEN = "loggedInAuthToken";

    User getLoggedInUser(NativeWebRequest nativeWebRequest);

    AuthToken getLoggedInAuthToken(NativeWebRequest nativeWebRequest);

    User getLoggedInUser(HttpServletRequest request);

    AuthToken getLoggedInAuthToken(HttpServletRequest request);
}
