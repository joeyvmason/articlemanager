package com.joeyvmason.articlemanager.web.application.auth;

import com.joeyvmason.articlemanager.core.domain.auth.AuthToken;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


public class LoggedInAuthTokenArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthenticationService authenticationService;
    
    public LoggedInAuthTokenArgumentResolver(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return (AuthToken.class.isAssignableFrom(methodParameter.getParameterType()) && methodParameter.getParameterAnnotation(LoggedInAuthToken.class) != null);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        return authenticationService.getLoggedInAuthToken(nativeWebRequest);
    }
}
