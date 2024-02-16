package com.salmalteam.salmal.auth.application;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salmalteam.salmal.auth.dto.request.LoginRequest;
import com.salmalteam.salmal.auth.dto.request.LogoutRequest;
import com.salmalteam.salmal.auth.dto.request.SignUpRequest;
import com.salmalteam.salmal.auth.dto.response.LoginResponse;
import com.salmalteam.salmal.auth.dto.response.TokenResponse;
import com.salmalteam.salmal.auth.entity.LogoutAccessToken;
import com.salmalteam.salmal.auth.entity.RefreshToken;
import com.salmalteam.salmal.auth.entity.Role;
import com.salmalteam.salmal.auth.entity.TokenRepository;
import com.salmalteam.salmal.auth.exception.AuthException;
import com.salmalteam.salmal.auth.exception.AuthExceptionType;
import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.member.entity.MemberRepository;
import com.salmalteam.salmal.member.entity.Status;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final TokenProvider jwtProvider;
	private final TokenProvider refreshTokenProvider;
	private final TokenRepository tokenRepository;
	private final MemberService memberService;

	private final MemberRepository memberRepository;

	@Transactional
	public LoginResponse login(final LoginRequest loginRequest) {
		final Long memberId = memberService.findMemberIdByProviderId(loginRequest.getProviderId());
		return generateTokenById(memberId);
	}

	@Transactional
	public LoginResponse signUp(final String provider, final SignUpRequest signUpRequest) {
		if (checkRejoin(signUpRequest.getProviderId())) {
			final Long memberId = memberService.rejoin(provider,signUpRequest.getProviderId(),signUpRequest.getNickName(),signUpRequest.getMarketingInformationConsent());
			return generateTokenById(memberId);
		}
		final Long memberId = memberService.save(provider, signUpRequest);
		return generateTokenById(memberId);
	}

	private boolean checkRejoin(String providerId) {
		return memberRepository.findByProviderId(providerId)
			.map(m-> m.getStatus().equals(Status.REMOVED))
			.orElse(false);
	}

	//TODO 리프래시 토큰 저장타입 개선
	private LoginResponse generateTokenById(final Long memberId) {
		Map<String, Object> payload = createPayloadWithIdClaim(memberId);
		String accessToken = jwtProvider.provide(payload);
		String refreshToken = refreshTokenProvider.provide(payload);
		tokenRepository.saveRefreshToken(RefreshToken.of(refreshToken));
		return LoginResponse.of(accessToken, refreshToken);
	}

	//TODO logout 로직 개선
	@Transactional
	public void logout(final String accessToken, final LogoutRequest logoutRequest) {
		final String refreshToken = logoutRequest.getRefreshToken();
		tokenRepository.saveLogoutAccessToken(LogoutAccessToken.of(accessToken));
		tokenRepository.deleteRefreshTokenById(refreshToken);
	}

	//TODO 리프래시 토큰발급 개선 (저장 타입 개선, 리프레시토큰 인증헤더 사용)
	@Transactional
	public TokenResponse reissueAccessToken(Long memberId, final String refreshToken) {
		validateRefreshTokenExists(refreshToken);
		final String accessToken = jwtProvider.provide(createPayloadWithIdClaim(memberId));
		return TokenResponse.from(accessToken);
	}

	private Map<String, Object> createPayloadWithIdClaim(Long memberId) {
		Map<String, Object> payload = new HashMap<>();
		payload.put("id", memberId);
		payload.put("role", Role.MEMBER);
		return payload;
	}

	private void validateRefreshTokenExists(final String refreshToken) {
		if (!tokenRepository.existsRefreshTokenById(refreshToken)) {
			throw new AuthException(AuthExceptionType.NOT_FOUND_REFRESH_TOKEN);
		}
	}
}
