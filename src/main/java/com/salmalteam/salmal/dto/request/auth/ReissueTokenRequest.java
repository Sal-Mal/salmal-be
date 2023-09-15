package com.salmalteam.salmal.dto.request.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReissueTokenRequest {
    private String refreshToken;
    public ReissueTokenRequest(final String refreshToken){
        this.refreshToken = refreshToken;
    }
}
