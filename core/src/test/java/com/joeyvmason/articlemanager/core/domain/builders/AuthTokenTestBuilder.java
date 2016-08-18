package com.joeyvmason.articlemanager.core.domain.builders;

import com.joeyvmason.articlemanager.core.domain.auth.AuthToken;
import com.joeyvmason.articlemanager.core.domain.users.User;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.UUID;

public class AuthTokenTestBuilder {

    private User user;
    private String accessToken = UUID.randomUUID().toString();
    private String refreshToken = UUID.randomUUID().toString();
    private Date expiration = DateTime.now().plusDays(1).toDate();

    public static AuthTokenTestBuilder validAuthToken() {
        return new AuthTokenTestBuilder();
    }

    public AuthTokenTestBuilder withExpiration(DateTime dateTime) {
        this.expiration = dateTime == null ? null : dateTime.toDate();
        return this;
    }

    public AuthTokenTestBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public AuthTokenTestBuilder withAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public AuthTokenTestBuilder withRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public AuthToken build() {
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(accessToken);
        authToken.setExpiration(expiration);
        authToken.setUser(user);
        return authToken;
    }

}
