package com.joeyvmason.articlemanager.web.application;

import com.joeyvmason.articlemanager.web.application.auth.AuthenticationService;
import com.joeyvmason.articlemanager.core.domain.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    private final AuthenticationService authenticationService;

    public LoggingInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse response, Object handler)
            throws Exception {
        User loggedInUser = authenticationService.getLoggedInUser(req);
        String loggedInUserId = loggedInUser != null ? loggedInUser.getId() : null;
        logger.info("adr:{}; user:{}; req:\"{} {} {}\";", req.getRemoteAddr(), loggedInUserId, req.getMethod(), req.getRequestURL(), req.getProtocol());
        return true;
    }

    @Override
    public void postHandle(	HttpServletRequest request, HttpServletResponse response,
                               Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        logger.info("Request completed with Status({})", response.getStatus());
    }
}
