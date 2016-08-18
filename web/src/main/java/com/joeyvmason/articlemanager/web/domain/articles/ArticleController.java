package com.joeyvmason.articlemanager.web.domain.articles;

import com.google.common.base.Objects;
import com.joeyvmason.articlemanager.web.application.auth.Authorized;
import com.joeyvmason.articlemanager.web.application.auth.LoggedInUser;
import com.joeyvmason.articlemanager.core.domain.BadRequestException;
import com.joeyvmason.articlemanager.core.domain.ForbiddenException;
import com.joeyvmason.articlemanager.core.domain.articles.Article;
import com.joeyvmason.articlemanager.core.domain.articles.ArticleRepository;
import com.joeyvmason.articlemanager.core.domain.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/articles")
@Authorized
public class ArticleController  {
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Article findById(@LoggedInUser User loggedInUser, @PathVariable String id) {
        logger.info("Finding Article by ID({})", id);
        Article article = articleRepository.findOne(id);

        if (article == null) {
            throw new BadRequestException(String.format("Invalid Article: %s", id));
        }

        if (!Objects.equal(loggedInUser, article.getOwner())) {
            throw new ForbiddenException(loggedInUser);
        }

        return article;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@LoggedInUser User loggedInUser, @PathVariable String id) {
        logger.info("Deleting Article by ID({})", id);
        Article article = articleRepository.findOne(id);

        if (article == null) {
            throw new BadRequestException(String.format("Invalid Article: %s", id));
        }

        if (!Objects.equal(loggedInUser, article.getOwner())) {
            throw new ForbiddenException(loggedInUser);
        }

        articleRepository.delete(article);
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<Article> findAll(@LoggedInUser User loggedInUser) {
        logger.info("Finding all Articles with Owner({})", loggedInUser);
        return articleRepository.findByOwner(loggedInUser);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Article create(@LoggedInUser User loggedInUser, @RequestBody ArticleForm articleForm) {
        logger.info("Creating new Article with Owner({})", loggedInUser);
        return articleRepository.save(articleForm.toEntity(loggedInUser));
    }
}
