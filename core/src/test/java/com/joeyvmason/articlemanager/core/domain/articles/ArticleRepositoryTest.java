package com.joeyvmason.articlemanager.core.domain.articles;

import com.joeyvmason.articlemanager.core.BaseIntegrationTest;
import com.joeyvmason.articlemanager.core.domain.builders.ArticleTestBuilder;
import com.joeyvmason.articlemanager.core.domain.builders.UserTestBuilder;
import com.joeyvmason.articlemanager.core.domain.users.User;
import com.joeyvmason.articlemanager.core.domain.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;


public class ArticleRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldFindAndRetrieveArticle() {
        //given
        Article article = ArticleTestBuilder.valid().build();

        //when
        article = articleRepository.save(article);

        //then
        Article articleFromDB = articleRepository.findOne(article.getId());
        assertThat(articleFromDB).isEqualTo(article);
    }

    @Test
    public void shouldFindByOwner() {
        //given
        User user1 = userRepository.save(UserTestBuilder.valid().build());
        User user2 = userRepository.save(UserTestBuilder.valid().build());

        //when
        Article article1 = articleRepository.save(ArticleTestBuilder.valid().withOwner(user1).build());
        Article article2 = articleRepository.save(ArticleTestBuilder.valid().withOwner(user2).build());

        //then
        assertThat(articleRepository.findByOwner(user1)).containsExactly(article1);
        assertThat(articleRepository.findByOwner(user2)).containsExactly(article2);
    }

}