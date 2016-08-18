package com.joeyvmason.articlemanager.core.domain.builders;

import com.joeyvmason.articlemanager.core.domain.articles.Article;
import com.joeyvmason.articlemanager.core.domain.users.User;

public class ArticleTestBuilder {

    private User owner;
    private String title;
    private String body;

    public Article build() {
        return new Article(owner, title, body);
    }

    public static ArticleTestBuilder valid() {
        return new ArticleTestBuilder();
    }

    public ArticleTestBuilder withOwner(User owner) {
        this.owner = owner;
        return this;
    }
}
