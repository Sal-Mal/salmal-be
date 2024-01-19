package com.salmalteam.salmal.auth.application;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salmalteam.salmal.auth.dto.request.LoginRequest;
import com.salmalteam.salmal.auth.dto.request.LogoutRequest;
import com.salmalteam.salmal.auth.dto.request.SignUpRequest;
import com.salmalteam.salmal.auth.dto.response.LoginResponse;
import com.salmalteam.salmal.auth.dto.response.TokenAvailableResponse;
import com.salmalteam.salmal.auth.dto.response.TokenResponse;
import com.salmalteam.salmal.auth.entity.LogoutAccessToken;
import com.salmalteam.salmal.auth.entity.RefreshToken;
import com.salmalteam.salmal.auth.entity.TokenRepository;
import com.salmalteam.salmal.auth.exception.AuthException;
import com.salmalteam.salmal.auth.exception.AuthExceptionType;
import com.salmalteam.salmal.member.application.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {

	private final TokenProvider jwtProvider;
	private final TokenProvider refreshTokenProvider;
	private final TokenRepository tokenRepository;
	private final MemberService memberService;
	private final TokenValidator tokenValidator;
	private final long refreshTokenExpiry;

	public AuthService(TokenProvider jwtProvider, TokenProvider refreshTokenProvider,
		TokenRepository tokenRepository, MemberService memberService, TokenValidator tokenValidator,
		@Value("${jwt.refresh-token-expiry}") long refreshTokenExpiry) {
		this.jwtProvider = jwtProvider;
		this.refreshTokenProvider = refreshTokenProvider;
		this.tokenRepository = tokenRepository;
		this.memberService = memberService;
		this.tokenValidator = tokenValidator;
		this.refreshTokenExpiry = refreshTokenExpiry;
	}

	@Transactional
	public LoginResponse login(final LoginRequest loginRequest) {
		final Long memberId = memberService.findMemberIdByProviderId(loginRequest.getProviderId());
		return generateTokenById(memberId);
	}

	@Transactional
	public LoginResponse signUp(final String provider, final SignUpRequest signUpRequest) {
		final Long memberId = memberService.save(provider, signUpRequest);
		return generateTokenById(memberId);
	}

	//TODO 리프레시 토큰 만료시간 덜어내기
	private LoginResponse generateTokenById(final Long memberId) {
		Map<String, Object> payload = createPayloadWithIdClaim(memberId);

		String accessToken = jwtProvider.provide(payload);
		String refreshToken = refreshTokenProvider.provide(payload);
		tokenRepository.saveRefreshToken(RefreshToken.of(refreshToken));
		return LoginResponse.of(accessToken, refreshToken);
	}

	@Transactional
	public void logout(final String accessToken, final LogoutRequest logoutRequest) {
		final String refreshToken = logoutRequest.getRefreshToken();
		tokenRepository.saveLogoutAccessToken(LogoutAccessToken.of(accessToken));
		tokenRepository.deleteRefreshTokenById(refreshToken);
	}

	//TODO 리프래시 토큰발급 개선
	@Transactional
	public TokenResponse reissueAccessToken(final String refreshToken) {
		validateRefreshToken(refreshToken);
		validateRefreshTokenExists(refreshToken);
		final String accessToken = jwtProvider.provide(createPayloadWithIdClaim(1L));
		return TokenResponse.from(accessToken);
	}

	private Map<String, Object> createPayloadWithIdClaim(Long memberId) {
		Map<String, Object> payload = new HashMap<>();
		payload.put("id", memberId);
		return payload;
	}

	private void validateRefreshToken(final String refreshToken) {
		if (!tokenValidator.isValidRefreshToken(refreshToken)) {
			throw new AuthException(AuthExceptionType.NOT_VALID_REFRESH_TOKEN);
		}
	}

	private void validateRefreshTokenExists(final String refreshToken) {
		if (!tokenRepository.existsRefreshTokenById(refreshToken)) {
			throw new AuthException(AuthExceptionType.NOT_FOUND_REFRESH_TOKEN);
		}
	}

	public TokenAvailableResponse validateToken(String accessToken) {
		return new TokenAvailableResponse(tokenValidator.isValidAccessToken(accessToken));
	}
}
