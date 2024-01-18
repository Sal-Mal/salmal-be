package com.salmalteam.salmal.auth.application;

import java.security.Key;
import java.util.Date;

import com.salmalteam.salmal.auth.entity.MemberPayLoad;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class FixedTokenProvider implements TokenProvider {

	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
	private final String ID = "id";
	private final long accessTokenExpiry;
	private final long refreshTokenExpiry;
	private final Key secretKey;

	public FixedTokenProvider(long accessTokenExpiry, long refreshTokenExpiry, Key secretKey) {
		this.accessTokenExpiry = accessTokenExpiry;
		this.refreshTokenExpiry = refreshTokenExpiry;
		this.secretKey = secretKey;
	}

	@Override
	public String createAccessToken(Long id) {
		final Date nowDate = new Date();
		final Date endDate = new Date(nowDate.getTime() + accessTokenExpiry);

		return Jwts.builder()
			.setSubject(ACCESS_TOKEN_SUBJECT)
			.setIssuedAt(nowDate)
			.setExpiration(endDate)
			.claim(ID, id)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	@Override
	public String createRefreshToken(Long id) {
		return null;
	}

	@Override
	public Long getTokenExpiry(String token) {
		return null;
	}

	@Override
	public boolean isValidRefreshToken(String refreshToken) {
		return false;
	}

	@Override
	public boolean isValidAccessToken(String accessToken) {
		return false;
	}

	@Override
	public Long getMemberIdFromToken(String token) {
		return null;
	}

	@Override
	public String getTokenType() {
		return null;
	}

	@Override
	public MemberPayLoad getPayLoad(String accessToken) {
		return null;
	}
}