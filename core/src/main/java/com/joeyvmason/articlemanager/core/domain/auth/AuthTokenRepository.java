package com.joeyvmason.articlemanager.core.domain.auth;

import com.joeyvmason.articlemanager.core.domain.BaseRepository;

import java.util.List;

public interface AuthTokenRepository extends BaseRepository<AuthToken> {

    AuthToken findByAccessToken(String accessToken);

    List<AuthToken> findByUserId(String userId);

}
