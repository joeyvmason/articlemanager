package com.joeyvmason.articlemanager.core.domain.articles;

import com.joeyvmason.articlemanager.core.domain.BaseRepository;
import com.joeyvmason.articlemanager.core.domain.users.User;

import java.util.List;

public interface ArticleRepository extends BaseRepository<Article> {
    List<Article> findByOwner(User loggedInUser);
}
