package com.salmalteam.salmal.auth.entity;

import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken")
public class RefreshToken extends Token {
	private static final long EXPIRE_MILLS = 1209600033;

	private RefreshToken(final String id, final Long expiry) {
		super(id, expiry);
	}

	public static RefreshToken of(final String refreshToken) {
		return new RefreshToken(refreshToken, EXPIRE_MILLS);
	}
}
