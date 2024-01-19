package com.salmalteam.salmal.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.salmalteam.salmal.auth.application.AuthPayloadGenerator;
import com.salmalteam.salmal.auth.application.TokenProvider;
import com.salmalteam.salmal.auth.application.TokenValidator;
import com.salmalteam.salmal.auth.entity.TokenRepository;
import com.salmalteam.salmal.auth.infrastructure.JwtProvider;
import com.salmalteam.salmal.auth.infrastructure.RefreshTokenProvider;

@Configuration
public class AuthConfig {
	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";

	private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

	private final String secretKey;
	private final TokenRepository tokenRepository;

	private final long accessTokenExpireMillis;
	private final long refreshTokenExpireMillis;

	public AuthConfig(
		TokenRepository tokenRepository, @Value("${jwt.access-token-expiry}") long accessTokenExpireMillis,
		@Value("${jwt.refresh-token-expiry}") long refreshTokenExpireMillis,
		@Value("${jwt.secret-key}") String secretKey) {
		this.tokenRepository = tokenRepository;
		this.accessTokenExpireMillis = accessTokenExpireMillis;
		this.refreshTokenExpireMillis = refreshTokenExpireMillis;
		this.secretKey = secretKey;
	}

	@Bean
	public TokenProvider jwtProvider() {
		return new JwtProvider(secretKey, ACCESS_TOKEN_SUBJECT, accessTokenExpireMillis);
	}

	@Bean
	public TokenProvider refreshTokenProvider() {
		return new RefreshTokenProvider(secretKey, REFRESH_TOKEN_SUBJECT, refreshTokenExpireMillis);
	}

	@Bean
	public TokenValidator tokenValidator() {
		return new TokenValidator(secretKey);
	}

	@Bean
	public AuthPayloadGenerator authPayloadGenerator() {
		return new AuthPayloadGenerator(tokenRepository, secretKey);
	}
}


