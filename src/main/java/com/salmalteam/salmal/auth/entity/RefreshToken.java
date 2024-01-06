package com.salmalteam.salmal.auth.entity;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("refreshToken")
public class RefreshToken extends Token{
    private RefreshToken(final String id, final Long expiry){
        super(id, expiry);
    }

    public static RefreshToken of(final String refreshToken, final Long expiry){
        return new RefreshToken(refreshToken, expiry);
    }
}
