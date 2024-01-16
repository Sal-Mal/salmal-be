package com.salmalteam.salmal.auth.application;

import java.nio.charset.StandardCharsets;

import com.salmalteam.salmal.auth.entity.MemberPayLoad;
import com.salmalteam.salmal.auth.entity.Role;
import com.salmalteam.salmal.auth.entity.TokenRepository;
import com.salmalteam.salmal.auth.exception.AuthException;
import com.salmalteam.salmal.auth.exception.AuthExceptionType;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class AuthPayloadExtractor {

	private static final String ID_KEY = "id";
	private static final String ROLE_KEY = "role";
	private final TokenRepository tokenRepository;
	private final JwtParser jwtParser;

	public AuthPayloadExtractor(TokenRepository tokenRepository, String secretKey) {
		this.tokenRepository = tokenRepository;
		jwtParser = Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
			.build();
	}

	public MemberPayLoad extractByToken(String token) {
		validateNotLoggedOutAccessToken(token);
		try {
			Long id = parseBody(token, ID_KEY, Long.class);
			String role = parseBody(token, ROLE_KEY, String.class);
			return MemberPayLoad.of(id, Role.of(role));
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	private void validateNotLoggedOutAccessToken(final String accessToken) {
		if (tokenRepository.existsLogoutAccessTokenById(accessToken)) {
			throw new AuthException(AuthExceptionType.LOGGED_OUT_ACCESS_TOKEN);
		}
	}

	private <T> T parseBody(String token, String id, Class<T> type) {
		return jwtParser.parseClaimsJwt(token)
			.getBody()
			.get(id, type);
	}
}
