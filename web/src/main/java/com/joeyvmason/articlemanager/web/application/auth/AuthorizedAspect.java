package com.joeyvmason.articlemanager.web.application.auth;

import com.joeyvmason.articlemanager.core.domain.ForbiddenException;
import com.joeyvmason.articlemanager.core.domain.UnauthorizedException;
import com.joeyvmason.articlemanager.core.domain.users.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
public class AuthorizedAspect {
    private final static Logger logger = LoggerFactory.getLogger(AuthorizedAspect.class);
    private final AuthenticationService authenticationService;

    public AuthorizedAspect(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Before("publicMethod() && (classAnnotatedWithAuthorized() || methodAnnotatedWithAuthorized())")
    public void authorize(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        User loggedInUser = authenticationService.getLoggedInUser(request);
        if (loggedInUser == null) {
            throw new UnauthorizedException();
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Authorized authorized = methodSignature.getMethod().getAnnotation(Authorized.class);
        if (authorized == null) {
            authorized = methodSignature.getMethod().getDeclaringClass().getAnnotation(Authorized.class);
        }

        if (!hasAtLeastOneRole(loggedInUser, authorized.withRoles())) {
            throw new ForbiddenException(loggedInUser);
        }
    }

    @Pointcut("within(@com.joeyvmason.articlemanager.web.application.auth.Authorized *)")
    public void classAnnotatedWithAuthorized() {
    }

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {
    }

    @Pointcut("@annotation(com.joeyvmason.articlemanager.web.application.auth.Authorized)")
    public void methodAnnotatedWithAuthorized() {
    }

    private boolean hasAtLeastOneRole(User user, User.Role[] roles) {
        if (roles.length == 0) {
            return true;
        }

        for (User.Role role : roles) {
            if (user.hasRole(role)) {
                return true;
            }
        }

        return false;
    }
}

