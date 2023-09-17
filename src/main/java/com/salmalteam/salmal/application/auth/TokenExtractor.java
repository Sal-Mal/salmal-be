package com.salmalteam.salmal.application.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenExtractor {

    private final TokenProvider tokenProvider;

    public String extractAccessTokenFromHeader(final String authorizationHeader){
        final String tokenType = tokenProvider.getTokenType();
        return authorizationHeader.substring(tokenType.length());
    }
}
