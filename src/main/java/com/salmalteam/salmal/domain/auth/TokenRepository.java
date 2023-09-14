package com.salmalteam.salmal.domain.auth;

public interface TokenRepository {
    void saveRefreshToken(RefreshToken refreshToken);
}
