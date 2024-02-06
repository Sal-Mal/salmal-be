package com.salmalteam.salmal.auth.infrastructure;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.salmalteam.salmal.support.TokenValidator;

class RefreshTokenProviderTest {
	private RefreshTokenProvider refreshTokenProvider;
	private final String SECRET_KEY = "K".repeat(32);

	private TokenValidator tokenValidator;

	private final String subject = "refreshToken";

	@BeforeEach
	void setUp() {
		Long ACCESS_TOKEN_EXPIRY = 100000L;
		refreshTokenProvider = new RefreshTokenProvider(SECRET_KEY, subject, ACCESS_TOKEN_EXPIRY);
		tokenValidator = new TokenValidator(SECRET_KEY);
	}

	@Nested
	class 접근_토큰_생성_테스트 {
		@Test
		void 전달받은_ID_를_페이로드에_넣어_접근토큰을_생성_한다() {
			//given
			HashMap<String, Object> payload = new HashMap<>();
			payload.put("id", 500L);
			payload.put("name", "재현");

			// when
			String actual = refreshTokenProvider.provide(payload);

			//then
			assertThat(actual).isNotNull();
			assertThat(tokenValidator.isValidate(actual)).isTrue();
			assertThat(tokenValidator.hasClaims(actual, "id", 500L, Long.class)).isTrue();
			assertThat(tokenValidator.hasClaims(actual, "name", "재현", String.class)).isTrue();
			assertThat(tokenValidator.hasClaims(actual, "sub", subject, String.class)).isTrue();

		}
	}

	@Nested
	class 재발급_토큰_생성_테스트 {
		@Test
		void 전달받은_ID_를_페이로드에_넣어_재발급토큰을_생성한다() {
			// given
			final Long id = 32L;
			Map<String, Object> payload = createPayloadWithIdClaim(id);

			// when
			final String refreshToken = refreshTokenProvider.provide(payload);
			// then
			assertThat(refreshToken).isNotNull();
		}
	}

	private Map<String, Object> createPayloadWithIdClaim(Long id) {
		HashMap<String, Object> payload = new HashMap<>();
		payload.put("id", id);
		return payload;
	}
}