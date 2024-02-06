package com.salmalteam.salmal.auth.application;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.salmalteam.salmal.auth.entity.Role;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class FixedTokenProvider implements TokenProvider {

	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private final String ID = "id";
	private final long accessTokenExpiry;
	private final Key secretKey;

	public FixedTokenProvider(long accessTokenExpiry, Key secretKey) {
		this.accessTokenExpiry = accessTokenExpiry;
		this.secretKey = secretKey;
	}

	@Override
	public String provide(Map<String, Object> payload) {
		final Date nowDate = new Date();
		final Date endDate = new Date(nowDate.getTime() + accessTokenExpiry);

		return Jwts.builder()
			.setSubject(ACCESS_TOKEN_SUBJECT)
			.setIssuedAt(nowDate)
			.setExpiration(endDate)
			.setClaims(payload)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public String provideWithIdClaim(Long id) {
		HashMap<String, Object> payload = new HashMap<>();
		payload.put("id", id);
		payload.put("role", Role.MEMBER);
		return provide(payload);
	}
}