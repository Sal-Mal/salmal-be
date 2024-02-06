package com.salmalteam.salmal.domain.auth;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;

import com.salmalteam.salmal.auth.entity.LogoutAccessToken;
import com.salmalteam.salmal.auth.entity.RefreshToken;
import com.salmalteam.salmal.auth.entity.TokenRepository;
import com.salmalteam.salmal.auth.entity.LogoutAccessTokenRepository;
import com.salmalteam.salmal.auth.entity.RefreshTokenRepository;
import com.salmalteam.salmal.auth.infrastructure.TokenRepositoryImpl;

@DataRedisTest
class TokenRepositoryTest {
	@Autowired
	LogoutAccessTokenRepository logoutAccessTokenRepository;

	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	TokenRepository tokenRepository;

	private static final String TOKEN = "A".repeat(30);

	@BeforeEach
	void setUp() {
		tokenRepository = new TokenRepositoryImpl(refreshTokenRepository, logoutAccessTokenRepository);
		refreshTokenRepository.deleteAll();
		logoutAccessTokenRepository.deleteAll();
	}

	@Test
	void 재발급_토큰_저장() {
		// given
		RefreshToken refreshToken = RefreshToken.of(TOKEN);
		// when
		tokenRepository.saveRefreshToken(refreshToken);
		// then
		Assertions.assertThat(tokenRepository.existsRefreshTokenById(TOKEN)).isTrue();
	}

	@Test
	void 재발급_토큰_삭제() {
		// given
		tokenRepository.saveRefreshToken(RefreshToken.of(TOKEN));
		// when
		tokenRepository.deleteRefreshTokenById(TOKEN);
		// then
		Assertions.assertThat(tokenRepository.existsRefreshTokenById(TOKEN)).isFalse();
	}

	@Test
	void 로그아웃_접근_토큰_저장() {
		// given
		LogoutAccessToken logoutAccessToken = LogoutAccessToken.of(TOKEN);
		// when
		tokenRepository.saveLogoutAccessToken(logoutAccessToken);
		// then
		Assertions.assertThat(tokenRepository.existsLogoutAccessTokenById(TOKEN)).isTrue();
	}
}