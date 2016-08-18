package com.joeyvmason.articlemanager.web.application.auth;

import com.joeyvmason.articlemanager.core.domain.users.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


public class LoggedInUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticationService authenticationService;

    public LoggedInUserArgumentResolver(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return (User.class.isAssignableFrom(methodParameter.getParameterType()) && methodParameter.getParameterAnnotation(LoggedInUser.class) != null);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        return authenticationService.getLoggedInUser(nativeWebRequest);
    }
}
