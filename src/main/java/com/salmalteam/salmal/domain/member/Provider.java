package com.salmalteam.salmal.domain.member;

import com.salmalteam.salmal.exception.auth.AuthException;
import com.salmalteam.salmal.exception.auth.AuthExceptionType;

import java.util.Arrays;

public enum Provider {
    KAKAO,
    APPLE
    ;

    public static Provider from(final String provider){
        return Arrays.stream(Provider.values())
                .filter(it -> it.name().equalsIgnoreCase(provider))
                .findAny()
                .orElseThrow(() -> new AuthException(AuthExceptionType.NOT_SUPPORTED_LOGIN_TYPE));
    }
}
