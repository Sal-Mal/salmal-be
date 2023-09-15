package com.salmalteam.salmal.dto.response.auth;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = {"accessToken"})
public class TokenResponse {
    private String accessToken;
    private TokenResponse(final String accessToken){
        this.accessToken = accessToken;
    }

    public static TokenResponse from(final String accessToken){
        return new TokenResponse(accessToken);
    }
}
