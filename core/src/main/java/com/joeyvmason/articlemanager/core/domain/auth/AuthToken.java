package com.joeyvmason.articlemanager.core.domain.auth;

import com.joeyvmason.articlemanager.core.domain.AuditableEntity;
import com.joeyvmason.articlemanager.core.domain.users.User;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;
import java.util.UUID;

public class AuthToken extends AuditableEntity {

    @DBRef
    private User user;

    private String accessToken;
    private Date expiration;

    public AuthToken() {
        this.accessToken = UUID.randomUUID().toString();
        this.expiration = DateTime.now().plusWeeks(1).toDate();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
