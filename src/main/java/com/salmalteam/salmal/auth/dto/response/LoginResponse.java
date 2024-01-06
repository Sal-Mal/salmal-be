package com.salmalteam.salmal.auth.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = {"accessToken", "refreshToken"})
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private LoginResponse(final String accessToken, final String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponse of(final String accessToken, final String refreshToken){
        return new LoginResponse(accessToken, refreshToken);
    }
}
