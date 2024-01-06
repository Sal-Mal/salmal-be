package com.salmalteam.salmal.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salmalteam.salmal.auth.dto.response.TokenAvailableResponse;
import com.salmalteam.salmal.auth.entity.LogoutAccessToken;
import com.salmalteam.salmal.auth.entity.TokenRepository;
import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.auth.entity.RefreshToken;
import com.salmalteam.salmal.auth.dto.request.LoginRequest;
import com.salmalteam.salmal.auth.dto.request.LogoutRequest;
import com.salmalteam.salmal.auth.dto.request.SignUpRequest;
import com.salmalteam.salmal.auth.dto.response.LoginResponse;
import com.salmalteam.salmal.auth.dto.response.TokenResponse;
import com.salmalteam.salmal.auth.exception.AuthException;
import com.salmalteam.salmal.auth.exception.AuthExceptionType;
import com.salmalteam.salmal.auth.infrastructure.TokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    @Transactional
    public void logout(final String accessToken, final LogoutRequest logoutRequest){
        final String refreshToken = logoutRequest.getRefreshToken();
        final Long accessTokenExpiry = tokenProvider.getTokenExpiry(accessToken);

        tokenRepository.saveLogoutAccessToken(LogoutAccessToken.of(accessToken, accessTokenExpiry));
        tokenRepository.deleteRefreshTokenById(refreshToken);
    }

    @Transactional
    public TokenResponse reissueAccessToken(final String refreshToken){
        validateRefreshToken(refreshToken);
        validateRefreshTokenExists(refreshToken);
        final Long memberId = tokenProvider.getMemberIdFromToken(refreshToken);
        final String accessToken = tokenProvider.createAccessToken(memberId);
        return TokenResponse.from(accessToken);
    }

    private void validateRefreshToken(final String refreshToken) {
        if(!tokenProvider.isValidRefreshToken(refreshToken)){
            throw new AuthException(AuthExceptionType.NOT_VALID_REFRESH_TOKEN);
        }
    }

    private void validateRefreshTokenExists(final String refreshToken){
        if(!tokenRepository.existsRefreshTokenById(refreshToken)){
            throw new AuthException(AuthExceptionType.NOT_FOUND_REFRESH_TOKEN);
        }
    }

    public TokenAvailableResponse validateToken(String accessToken) {
        return new TokenAvailableResponse(tokenProvider.isValidAccessToken(accessToken));
    }
}
