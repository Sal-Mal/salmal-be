package com.salmalteam.salmal.domain.auth;

public interface TokenRepository {
    void saveRefreshToken(RefreshToken refreshToken);
    void saveLogoutAccessToken(LogoutAccessToken logoutAccessToken);
    void deleteRefreshTokenById(String refreshToken);
    boolean existsLogoutAccessTokenById(String token);
    boolean existsRefreshTokenById(String token);
}
