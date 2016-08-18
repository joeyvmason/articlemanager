package com.joeyvmason.articlemanager.web.application.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@Configuration
public class AuthenticationConfig {

    @Autowired
    private AuthenticationService authenticationService;

    @Bean
    public AuthorizedAspect authorizedAspect() {
        return new AuthorizedAspect(authenticationService);
    }

}
