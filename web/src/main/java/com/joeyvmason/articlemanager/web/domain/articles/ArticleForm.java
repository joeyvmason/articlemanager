package com.joeyvmason.articlemanager.web.domain.articles;

import com.joeyvmason.articlemanager.core.domain.Form;
import com.joeyvmason.articlemanager.core.domain.articles.Article;
import com.joeyvmason.articlemanager.core.domain.users.User;

public class ArticleForm implements Form<Article> {

    private String title;
    private String body;

    public ArticleForm() {}

    public ArticleForm(String title, String body) {
        this.title = title;
        this.body = body;
    }

    @Override
    public Article toEntity(User loggedInUser) {
        return new Article(loggedInUser, title, body);
    }

    @Override
    public Article update(Article existing, User loggedInUser) {
        existing.setTitle(title);
        existing.setBody(body);
        return existing;
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
}
