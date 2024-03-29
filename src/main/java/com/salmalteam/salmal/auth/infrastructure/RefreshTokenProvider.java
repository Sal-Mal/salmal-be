package com.salmalteam.salmal.auth.infrastructure;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

import com.salmalteam.salmal.auth.application.TokenProvider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class RefreshTokenProvider implements TokenProvider {

	private final Key secretKey;
	private final Long expireMillis;
	private final String subject;

	public RefreshTokenProvider(String secretKey, String subject, Long expireMillis) {
		this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.expireMillis = expireMillis;
		this.subject = subject;
	}

	@Override
	public String provide(Map<String, Object> payload) {
		final Date nowDate = new Date();
		final Date endDate = new Date(nowDate.getTime() + expireMillis);

		return Jwts.builder()
			.setClaims(payload)
			.setSubject(subject)
			.setIssuedAt(nowDate)
			.setExpiration(endDate)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}
}
