package com.salmalteam.salmal.dto.request.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogoutRequest {
    private String refreshToken;
    public LogoutRequest(final String refreshToken){
        this.refreshToken = refreshToken;
    }
}
