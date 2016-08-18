package com.joeyvmason.articlemanager.web.domain.articles;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.joeyvmason.articlemanager.web.BaseMvcIntegrationTest;
import com.joeyvmason.articlemanager.core.domain.articles.Article;
import com.joeyvmason.articlemanager.core.domain.articles.ArticleRepository;
import com.joeyvmason.articlemanager.core.domain.auth.AuthToken;
import com.joeyvmason.articlemanager.core.domain.auth.AuthTokenRepository;
import com.joeyvmason.articlemanager.web.application.auth.AuthenticationService;
import com.joeyvmason.articlemanager.core.domain.builders.ArticleTestBuilder;
import com.joeyvmason.articlemanager.core.domain.builders.AuthTokenTestBuilder;
import com.joeyvmason.articlemanager.core.domain.builders.UserTestBuilder;
import com.joeyvmason.articlemanager.core.domain.users.User;
import com.joeyvmason.articlemanager.core.domain.users.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.fest.assertions.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.Test;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArticleControllerTest extends BaseMvcIntegrationTest {

    private static CollectionType COLLECTION_TYPE = TypeFactory.defaultInstance().constructCollectionType(List.class, Article.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldFindById() throws Exception {
        //given
        User user1 = userRepository.save(UserTestBuilder.valid().build());
        User user2 = userRepository.save(UserTestBuilder.valid().build());
        AuthToken authToken = authTokenRepository.save(AuthTokenTestBuilder.validAuthToken().withUser(user1).build());
        Article article1 = articleRepository.save(ArticleTestBuilder.valid().withOwner(user1).build());
        Article article2 = articleRepository.save(ArticleTestBuilder.valid().withOwner(user2).build());

        //when
        MvcResult response = mockMvc.perform(get(String.format("/articles/%s", article1.getId()))
                .header(AuthenticationService.X_AUTH_TOKEN, authToken.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        //then
        String jsonResponse = response.getResponse().getContentAsString();
        Article article = objectMapper.readValue(jsonResponse, Article.class);
        assertThat(article).isEqualTo(article1);
    }

    @Test
    public void shouldForbidFindById() throws Exception {
        //given
        User user1 = userRepository.save(UserTestBuilder.valid().build());
        User user2 = userRepository.save(UserTestBuilder.valid().build());
        AuthToken authToken = authTokenRepository.save(AuthTokenTestBuilder.validAuthToken().withUser(user1).build());
        Article article1 = articleRepository.save(ArticleTestBuilder.valid().withOwner(user1).build());
        Article article2 = articleRepository.save(ArticleTestBuilder.valid().withOwner(user2).build());

        //when
        mockMvc.perform(get(String.format("/articles/%s", article2.getId()))
                .header(AuthenticationService.X_AUTH_TOKEN, authToken.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void shouldUnauthorizeFindById() throws Exception {
        //when
        mockMvc.perform(get("/articles/foo")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldCreateArticle() throws Exception {
        //given
        User user = userRepository.save(UserTestBuilder.valid().build());
        AuthToken authToken = authTokenRepository.save(AuthTokenTestBuilder.validAuthToken().withUser(user).build());

        //when
        ArticleForm articleForm = new ArticleForm(RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphabetic(10));
        String jsonBody = objectMapper.writeValueAsString(articleForm);
        MvcResult response = mockMvc.perform(post("/articles")
                .header(AuthenticationService.X_AUTH_TOKEN, authToken.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
        ).andExpect(status().isOk()).andReturn();

        //then
        String jsonResponse = response.getResponse().getContentAsString();
        Article article = objectMapper.readValue(jsonResponse, Article.class);

        Article articleFromDB = articleRepository.findOne(article.getId());
        assertThat(articleFromDB.getBody()).isEqualTo(articleForm.getBody());
        assertThat(articleFromDB.getTitle()).isEqualTo(articleForm.getTitle());
    }

    @Test
    public void shouldUnauthorizeCreateArticle() throws Exception {
        //when
        String jsonBody = objectMapper.writeValueAsString(new ArticleForm());
        mockMvc.perform(post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldFindAll() throws Exception {
        //given
        User user = userRepository.save(UserTestBuilder.valid().build());
        AuthToken authToken = authTokenRepository.save(AuthTokenTestBuilder.validAuthToken().withUser(user).build());
        Article article1 = articleRepository.save(ArticleTestBuilder.valid().withOwner(user).build());
        Article article2 = articleRepository.save(ArticleTestBuilder.valid().withOwner(user).build());
        Article article3 = articleRepository.save(ArticleTestBuilder.valid().build());

        //when
        MvcResult response = mockMvc.perform(get("/articles")
                .header(AuthenticationService.X_AUTH_TOKEN, authToken.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        //then
        String jsonResponse = response.getResponse().getContentAsString();
        List<Article> articles = objectMapper.readValue(jsonResponse, COLLECTION_TYPE);
        Assertions.assertThat(articles).containsOnly(article1, article2);
    }

    @Test
    public void shouldUnauthorizeFindAllArticles() throws Exception {
        //when
        mockMvc.perform(get("/articles")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

}