package com.salmalteam.salmal.application.auth;

import com.salmalteam.salmal.application.member.MemberService;
import com.salmalteam.salmal.domain.auth.RefreshToken;
import com.salmalteam.salmal.domain.auth.TokenRepository;
import com.salmalteam.salmal.dto.request.LoginRequest;
import com.salmalteam.salmal.dto.request.SignUpRequest;
import com.salmalteam.salmal.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final MemberService memberService;

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

    private LoginResponse generateTokenById(final Long memberId) {
        final String accessToken = tokenProvider.createAccessToken(memberId);
        final String refreshToken = tokenProvider.createRefreshToken(memberId);
        final Long refreshTokenExpiry = tokenProvider.getTokenExpiry(refreshToken);
        tokenRepository.saveRefreshToken(RefreshToken.of(refreshToken, refreshTokenExpiry));

        return LoginResponse.of(accessToken, refreshToken);
    }

}
