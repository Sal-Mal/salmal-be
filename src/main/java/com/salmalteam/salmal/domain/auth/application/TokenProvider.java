package com.salmalteam.salmal.domain.auth.application;

import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;

public interface TokenProvider {
    String createAccessToken(Long id);
    String createRefreshToken(Long id);
    Long getTokenExpiry(String token);
    boolean isValidRefreshToken(String refreshToken);
    boolean isValidAccessToken(String accessToken);
    Long getMemberIdFromToken(String token);
    String getTokenType();
    MemberPayLoad getPayLoad(String accessToken);
}
