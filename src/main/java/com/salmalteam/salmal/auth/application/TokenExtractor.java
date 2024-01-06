package com.salmalteam.salmal.auth.application;

import org.springframework.stereotype.Component;

import com.salmalteam.salmal.auth.infrastructure.TokenProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenExtractor {

    private final TokenProvider tokenProvider;

    public String extractAccessTokenFromHeader(final String authorizationHeader){
        final String tokenType = tokenProvider.getTokenType();
        return authorizationHeader.substring(tokenType.length());
    }
}
