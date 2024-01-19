package com.salmalteam.salmal.auth.application;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class TokenValidator {

	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

	private final JwtParser jwtParser;

	public TokenValidator(String secretKey) {
		jwtParser = Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
			.build();
	}

	public boolean isValidRefreshToken(final String refreshToken) {
		try {
			final Claims claims = getClaims(refreshToken);
			return isRefreshToken(claims) && isNotExpired(claims);
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public boolean isValidAccessToken(final String accessToken) {
		try {
			final Claims claims = getClaims(accessToken);
			return isAccessToken(claims) && isNotExpired(claims);
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	private Claims getClaims(String token) {
		return jwtParser.parseClaimsJws(token)
			.getBody();
	}

	private boolean isAccessToken(final Claims claims) {
		return claims.getSubject().equals(ACCESS_TOKEN_SUBJECT);
	}

	private boolean isRefreshToken(final Claims claims) {
		return claims.getSubject().equals(REFRESH_TOKEN_SUBJECT);
	}

	private boolean isNotExpired(final Claims claims) {
		return claims.getExpiration().after(new Date());
	}
}
