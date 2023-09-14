package com.salmalteam.salmal.domain.auth;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("logoutAccessToken")
public class LogoutAccessToken extends Token{
    private LogoutAccessToken(final String id, final Long expiry){
        super(id, expiry);
    }
    public static LogoutAccessToken of(final String accessToken, final Long expiry){
        return new LogoutAccessToken(accessToken, expiry);
    }
}
