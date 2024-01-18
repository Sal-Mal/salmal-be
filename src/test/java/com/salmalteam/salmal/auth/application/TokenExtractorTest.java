package com.salmalteam.salmal.auth.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.salmalteam.salmal.auth.entity.MemberPayLoad;
import com.salmalteam.salmal.auth.exception.AuthException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

class TokenExtractorTest {

	TokenProvider tokenProvider;
	TokenExtractor tokenExtractor;

	@BeforeEach
	void setUp() {
		SecretKey secretKey = Keys.hmacShaKeyFor("testSecretKeytestSecretKeytestSecretKeytestSecretKey".getBytes());
		tokenProvider = new FixedTokenProvider(100000L, 100000L, secretKey);
		tokenExtractor = new TokenExtractor();
	}

	@Test
	@DisplayName("인증헤더를 받아 토큰을 추출해야 한다.")
	void extract() {
		//given
		String accessToken = tokenProvider.createAccessToken(10L);

		//when
		String token = tokenExtractor.extractByHeader("Bearer " + accessToken)
			.get();

		//then
		assertThat(token).isNotBlank();
	}

	@Test
	@DisplayName("인증헤더가 존재하지 않으면(비어있으면) empty 한 optional 을 반환한다.")
	void extract_empty_header() throws Exception {
		//when
		Optional<String> token = tokenExtractor.extractByHeader(null);

		//then
		Assertions.assertThat(token.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("인증헤더에 존재하는 인증 타입이 Bearer 가 아니면 예외가 발생한다.")
	void extract_invalid_auth_type() throws Exception {
		//given
		String accessToken = tokenProvider.createAccessToken(10L);

		//expect
		assertThatThrownBy(() -> tokenExtractor.extractByHeader("Basic " + accessToken))
			.isInstanceOf(AuthException.class)
			.hasMessage("유효하지 않은 접근 토큰 요청 -> [ type = UNAUTHORIZED ] [ code = 4004 ] ");
	}

	static class FixedTokenProvider implements TokenProvider {

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

}