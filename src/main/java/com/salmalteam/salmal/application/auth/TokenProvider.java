package com.salmalteam.salmal.application.auth;

public interface TokenProvider {
    String createAccessToken(Long id);
    String createRefreshToken(Long id);
    Long getTokenExpiry(String token);
    boolean isValidRefreshToken(String refreshToken);
    Long getMemberIdFromToken(String token);
}
