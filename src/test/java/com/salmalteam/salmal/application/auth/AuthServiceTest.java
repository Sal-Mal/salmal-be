package com.salmalteam.salmal.application.auth;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.salmalteam.salmal.auth.application.AuthService;
import com.salmalteam.salmal.auth.dto.request.LoginRequest;
import com.salmalteam.salmal.auth.dto.request.SignUpRequest;
import com.salmalteam.salmal.auth.dto.response.LoginResponse;
import com.salmalteam.salmal.auth.dto.response.TokenResponse;
import com.salmalteam.salmal.auth.entity.RefreshToken;
import com.salmalteam.salmal.auth.entity.Role;
import com.salmalteam.salmal.auth.entity.TokenRepository;
import com.salmalteam.salmal.auth.exception.AuthException;
import com.salmalteam.salmal.auth.infrastructure.JwtProvider;
import com.salmalteam.salmal.auth.infrastructure.RefreshTokenProvider;
import com.salmalteam.salmal.member.application.MemberService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	AuthService authService;
	@Mock
	JwtProvider jwtProvider;
	@Mock
	RefreshTokenProvider refreshTokenProvider;
	@Mock
	TokenRepository tokenRepository;
	@Mock
	MemberService memberService;

	@BeforeEach
	void setUp() {
		authService = new AuthService(jwtProvider, refreshTokenProvider, tokenRepository, memberService);
	}

	@Nested
	class 로그인_테스트 {

		@Test
		void 기가입자인_경우_정상적으로_접근_토큰과_재발급_토큰을_발급한다() {
			// given
			final Long memberId = 1L;
			final String accessToken = "accessToken";
			final String refreshToken = "refreshToken";
			final String providerId = "providerId";
			final Map<String, Object> payload = new HashMap<>();
			payload.put("id", memberId);
			payload.put("role", Role.MEMBER);
			final LoginRequest loginRequest = new LoginRequest("providerId");

			given(memberService.findMemberIdByProviderId(eq(providerId))).willReturn(memberId);
			given(jwtProvider.provide(any())).willReturn(accessToken);
			given(refreshTokenProvider.provide(eq(payload))).willReturn(refreshToken);

			// when
			final LoginResponse loginResponse = authService.login(loginRequest);

			assertAll(
				() -> assertThat(loginResponse).isEqualTo(LoginResponse.of(accessToken, refreshToken)),
				() -> verify(tokenRepository, times(1)).saveRefreshToken(any(RefreshToken.class))
			);
		}

		@Nested
		class 회원_가입_테스트 {
			@Test
			void 접근_토큰과_재발급_토큰을_발급한다() {

				// given
				final Long memberId = 1L;
				final String nickName = "닉네임";
				final Boolean marketingInformationConsent = false;
				final String accessToken = "accessToken";
				final String refreshToken = "refreshToken";
				final String providerId = "providerId";
				final Map<String, Object> payload = new HashMap<>();
				payload.put("id", memberId);
				payload.put("role", Role.MEMBER);
				final SignUpRequest signUpRequest = new SignUpRequest(providerId, nickName,
					marketingInformationConsent);

				given(memberService.save(eq(providerId), any())).willReturn(memberId);
				given(jwtProvider.provide(eq(payload))).willReturn(accessToken);
				given(refreshTokenProvider.provide(eq(payload))).willReturn(refreshToken);

				// when
				final LoginResponse loginResponse = authService.signUp(providerId, signUpRequest);

				// then
				assertAll(
					() -> assertThat(loginResponse).isEqualTo(LoginResponse.of(accessToken, refreshToken)),
					() -> verify(tokenRepository, times(1)).saveRefreshToken(any(RefreshToken.class))
				);
			}

			@Nested
			class 토큰_재발급_테스트 {
				final String refreshToken = "refreshToken";
				final String accessToken = "accessToken";

				@Test
				void 재발급_토큰을_통해서_접근_토큰을_재발급_한다() {
					// given
					HashMap<String, Object> payload = new HashMap<>();
					long memberId = 1000L;
					payload.put("id", memberId);
					given(tokenRepository.existsRefreshTokenById(any())).willReturn(true);
					given(jwtProvider.provide(any())).willReturn(accessToken);

					// when
					final TokenResponse tokenResponse = authService.reissueAccessToken(memberId, refreshToken);

					// then
					assertAll(
						() -> assertThat(tokenResponse).isEqualTo(TokenResponse.from(accessToken))
					);
				}

				@Test
				void 재발급_토큰이_존재하지_않으면_예외를_발생시킨다() {
					// given
					given(tokenRepository.existsRefreshTokenById(any())).willReturn(false);

					// when & then
					assertThatThrownBy(() -> authService.reissueAccessToken(12345612352L, refreshToken))
						.isInstanceOf(AuthException.class);
				}
			}
		}
	}
}