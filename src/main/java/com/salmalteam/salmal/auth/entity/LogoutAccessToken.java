package com.salmalteam.salmal.auth.entity;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("logoutAccessToken")
public class LogoutAccessToken extends Token {

	private static final long EXPIRE_MILLS = 10800000;

	private LogoutAccessToken(final String id, final Long expiry) {
		super(id, expiry);
	}

	public static LogoutAccessToken of(final String accessToken) {
		return new LogoutAccessToken(accessToken, EXPIRE_MILLS);
	}
}
