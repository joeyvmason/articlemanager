package com.joeyvmason.articlemanager.web.application.auth;

import com.google.common.base.Preconditions;
import com.joeyvmason.articlemanager.core.domain.auth.AuthToken;
import com.joeyvmason.articlemanager.core.domain.auth.AuthTokenRepository;
import com.joeyvmason.articlemanager.core.domain.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@SuppressWarnings("unchecked")
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final AuthTokenRepository authTokenRepository;

    @Autowired
    public AuthenticationServiceImpl(AuthTokenRepository authTokenRepository) {
        this.authTokenRepository = authTokenRepository;
    }

    @Override
    public User getLoggedInUser(NativeWebRequest request) {
        return getLoggedInUser(new HttpRequestWrapper(request));
    }

    @Override
    public User getLoggedInUser(HttpServletRequest request) {
        return getLoggedInUser(new HttpRequestWrapper(request));
    }

    @Override
    public AuthToken getLoggedInAuthToken(NativeWebRequest request) {
        return getLoggedInAuthToken(new HttpRequestWrapper(request));
    }

    @Override
    public AuthToken getLoggedInAuthToken(HttpServletRequest request) {
        return getLoggedInAuthToken(new HttpRequestWrapper(request));
    }

    private User getLoggedInUser(HttpRequestWrapper httpRequestWrapper) {
        init(httpRequestWrapper);
        return (User) httpRequestWrapper.getAttribute(LOGGED_IN_USER);
    }

    private AuthToken getLoggedInAuthToken(HttpRequestWrapper httpRequestWrapper) {
        init(httpRequestWrapper);
        return (AuthToken) httpRequestWrapper.getAttribute(LOGGED_IN_AUTH_TOKEN);
    }

    private void init(HttpRequestWrapper httpRequestWrapper) {
        // TODO: This is often being hit several times for the same request. Need to only allow first thread to execute this code. Force the others to wait.

        AuthToken authToken = (AuthToken) httpRequestWrapper.getAttribute(LOGGED_IN_AUTH_TOKEN);
        if (authToken != null) {
            // data has already been initialized
            logger.trace("Data has already been initialized");
            return;
        }

        String accessToken = httpRequestWrapper.getHeader(X_AUTH_TOKEN);
        if (accessToken == null) {
            // no token provided
            logger.trace("No token provided");
            return;
        }

        authToken = authTokenRepository.findByAccessToken(accessToken);
        if (authToken == null) {
            // invalid token
            logger.trace("Token is invalid");
            return;
        }

        if (new Date().after(authToken.getExpiration())) {
            // expired token
            logger.trace("Token is expired");
            return;
        }

        // set auth token
        logger.trace("Setting AuthToken({})", authToken.getId());
        httpRequestWrapper.setAttribute(LOGGED_IN_AUTH_TOKEN, authToken);

        // retrieve and set user
        if (authToken.getUser() != null) {
            User user = authToken.getUser();

            logger.trace("Setting LoggedInUser({})", user.getId());
            httpRequestWrapper.setAttribute(LOGGED_IN_USER, user);
        }
    }

    private static class HttpRequestWrapper {
        private NativeWebRequest nativeWebRequest;
        private HttpServletRequest httpServletRequest;

        public HttpRequestWrapper(NativeWebRequest nativeWebRequest) {
            Preconditions.checkNotNull(nativeWebRequest);
            this.nativeWebRequest = nativeWebRequest;
        }

        public HttpRequestWrapper(HttpServletRequest httpServletRequest) {
            Preconditions.checkNotNull(httpServletRequest);
            this.httpServletRequest = httpServletRequest;
        }

        public Object getAttribute(String key) {
            if (nativeWebRequest != null) {
                return nativeWebRequest.getAttribute(key, RequestAttributes.SCOPE_REQUEST);
            } else {
                return httpServletRequest.getAttribute(key);
            }
        }

        public void setAttribute(String key, Object object) {
            if (nativeWebRequest != null) {
                nativeWebRequest.setAttribute(key, object, RequestAttributes.SCOPE_REQUEST);
            } else {
                httpServletRequest.setAttribute(key, object);
            }
        }

        public String getHeader(String key) {
            if (nativeWebRequest != null) {
                return nativeWebRequest.getHeader(key);
            } else {
                return httpServletRequest.getHeader(key);
            }
        }
    }
}