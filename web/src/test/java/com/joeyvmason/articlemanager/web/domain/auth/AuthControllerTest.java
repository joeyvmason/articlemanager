package com.joeyvmason.articlemanager.web.domain.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joeyvmason.articlemanager.web.BaseMvcIntegrationTest;
import com.joeyvmason.articlemanager.core.domain.auth.AuthToken;
import com.joeyvmason.articlemanager.core.domain.auth.AuthTokenRepository;
import com.joeyvmason.articlemanager.web.application.auth.AuthenticationService;
import com.joeyvmason.articlemanager.core.domain.builders.AuthTokenTestBuilder;
import com.joeyvmason.articlemanager.core.domain.builders.UserTestBuilder;
import com.joeyvmason.articlemanager.core.domain.users.User;
import com.joeyvmason.articlemanager.core.domain.users.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.testng.annotations.Test;


import static org.fest.assertions.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends BaseMvcIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(AuthControllerTest.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldNotSignUpWithDuplicateEmailAddress() throws Exception {
        //given
        User user = userRepository.save(UserTestBuilder.valid().build());

        //when
        AuthController.LoginForm login = new AuthController.LoginForm(user.getEmailAddress(), RandomStringUtils.randomAlphanumeric(10));

        String jsonBody = objectMapper.writeValueAsString(login);
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldSignUp() throws Exception {
        String emailAddress = String.format("foo+%s@bar.com", RandomStringUtils.randomAlphabetic(10));
        String password = RandomStringUtils.randomAlphanumeric(10);

        //when
        AuthController.LoginForm login = new AuthController.LoginForm(emailAddress, password);

        String jsonBody = objectMapper.writeValueAsString(login);
        ResultActions result = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
        ).andExpect(status().isOk());

        //then
        String jsonResponse = result.andReturn().getResponse().getContentAsString();
        AuthToken authToken = objectMapper.readValue(jsonResponse, AuthToken.class);

        AuthToken authTokenFromDB = authTokenRepository.findByAccessToken(authToken.getAccessToken());
        User user = authTokenFromDB.getUser();
        assertThat(user.getEmailAddress()).isEqualTo(emailAddress);
        assertThat(user.getShaPassword()).isEqualTo(User.getSha512(password));
    }

    @Test
    public void shouldFindMyself() throws Exception {
        User user = userRepository.save(UserTestBuilder.valid().build());
        AuthToken authToken = authTokenRepository.save(AuthTokenTestBuilder.validAuthToken().withUser(user).build());

        ResultActions result = mockMvc.perform(get("/auth/me")
                .header(AuthenticationService.X_AUTH_TOKEN, authToken.getAccessToken())
        ).andExpect(status().isOk());

        String jsonResponse = result.andReturn().getResponse().getContentAsString();
        logger.info("JSON: {}", jsonResponse);
//        User userFromResponse = objectMapper.readValue(jsonResponse, User.class);

//        assertThat(userFromResponse).isEqualTo(user);
    }

    @Test
    public void shouldNotFindMyselfIfNotLoggedIn() throws Exception {
        mockMvc.perform(get("/auth/me")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldLogInWithValidCredentials() throws Exception {
        String password = "foobar99";
        User user = userRepository.save(UserTestBuilder.valid().withPassword(password).build());

        AuthController.LoginForm login = new AuthController.LoginForm(user.getEmailAddress(), password);

        String jsonBody = objectMapper.writeValueAsString(login);
        ResultActions result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
        ).andExpect(status().isOk());

        String jsonResponse = result.andReturn().getResponse().getContentAsString();
        AuthToken authToken = objectMapper.readValue(jsonResponse, AuthToken.class);

        AuthToken authTokenFromDB = authTokenRepository.findByAccessToken(authToken.getAccessToken());
        assertThat(authTokenFromDB.getUser()).isEqualTo(user);
    }

    @Test
    public void shouldNotLogInWithBadCredentials() throws Exception {
        AuthController.LoginForm login = new AuthController.LoginForm("invalid@foo.com", "foobar99");
        String jsonBody = objectMapper.writeValueAsString(login);
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldLogOut() throws Exception {
        //given
        User user = userRepository.save(UserTestBuilder.valid().build());

        AuthToken authToken = authTokenRepository.save(AuthTokenTestBuilder.validAuthToken().withUser(user).build());
        logger.info("Created new AuthToken({}) with AccessToken({})", authToken.getId(), authToken.getAccessToken());

        //when
        mockMvc.perform(post("/auth/logout")
                .header(AuthenticationService.X_AUTH_TOKEN, authToken.getAccessToken())
        ).andExpect(status().isOk());

        //then
        assertThat(authTokenRepository.findOne(authToken.getId())).isNull();
    }


}