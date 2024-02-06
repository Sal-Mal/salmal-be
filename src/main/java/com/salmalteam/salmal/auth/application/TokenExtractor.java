package com.salmalteam.salmal.auth.application;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.salmalteam.salmal.auth.exception.AuthException;
import com.salmalteam.salmal.auth.exception.AuthExceptionType;

@Component
public class TokenExtractor {

	private static final String TOKEN_TYPE = "Bearer";

	public Optional<String> extractByHeader(final String authorizationHeader) {
		if (authorizationHeader == null) {
			return Optional.empty();
		}
		validateTokenType(authorizationHeader);
		return Optional.of(authorizationHeader.substring(TOKEN_TYPE.length()));
	}

	private void validateTokenType(String authorizationHeader) {
		if (!authorizationHeader.startsWith(TOKEN_TYPE)) {
			throw new AuthException(AuthExceptionType.NOT_VALID_ACCESS_TOKEN);
		}
	}
}
