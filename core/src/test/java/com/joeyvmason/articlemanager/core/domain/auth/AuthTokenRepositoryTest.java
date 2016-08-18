package com.joeyvmason.articlemanager.core.domain.auth;

import com.joeyvmason.articlemanager.core.BaseIntegrationTest;
import com.joeyvmason.articlemanager.core.domain.builders.AuthTokenTestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;


import static org.fest.assertions.api.Assertions.assertThat;

public class AuthTokenRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Test
    public void shouldSaveAndRetrieveAuthToken() {
        //given
        AuthToken authToken = AuthTokenTestBuilder.validAuthToken().build();

        //when
        authTokenRepository.save(authToken);

        //then
        AuthToken authTokenFromDB = authTokenRepository.findOne(authToken.getId());
        assertThat(authTokenFromDB).isEqualTo(authToken);
        assertThat(authTokenFromDB.getAccessToken()).isEqualTo(authToken.getAccessToken());
        assertThat(authTokenFromDB.getExpiration()).isEqualTo(authToken.getExpiration());
    }

    @Test
    public void shouldRetrieveByAccessToken() {
        //given
        AuthToken authToken = AuthTokenTestBuilder.validAuthToken().build();

        //when
        authTokenRepository.save(authToken);

        //then
        AuthToken authTokenFromDB = authTokenRepository.findByAccessToken(authToken.getAccessToken());
        assertThat(authTokenFromDB).isEqualTo(authToken);
    }

    @Test
    public void shouldDeleteAuthToken() {
        //given
        AuthToken authToken = AuthTokenTestBuilder.validAuthToken().build();

        //when
        authTokenRepository.save(authToken);

        //then
        authTokenRepository.delete(authToken);
        assertThat(authTokenRepository.findOne(authToken.getId())).isNull();
    }

}