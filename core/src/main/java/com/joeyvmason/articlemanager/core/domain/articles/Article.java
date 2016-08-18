package com.joeyvmason.articlemanager.core.domain.articles;

import com.joeyvmason.articlemanager.core.domain.AuditableEntity;
import com.joeyvmason.articlemanager.core.domain.users.User;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Article extends AuditableEntity {

    private String title;
    private String body;

    @DBRef
    private User owner;

    public Article() {}

    public Article(User owner, String title, String body) {
        this.owner = owner;
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
