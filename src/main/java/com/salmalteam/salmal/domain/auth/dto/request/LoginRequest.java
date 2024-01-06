package com.salmalteam.salmal.domain.auth.dto.request;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequest {

    private String providerId;

    public LoginRequest(final String providerId){
        this.providerId = providerId;
    }

}
