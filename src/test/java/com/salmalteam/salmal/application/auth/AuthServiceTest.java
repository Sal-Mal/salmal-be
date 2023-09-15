package com.salmalteam.salmal.application.auth;

import com.salmalteam.salmal.application.member.MemberService;
import com.salmalteam.salmal.domain.auth.RefreshToken;
import com.salmalteam.salmal.domain.auth.TokenRepository;
import com.salmalteam.salmal.dto.request.auth.LoginRequest;
import com.salmalteam.salmal.dto.request.auth.SignUpRequest;
import com.salmalteam.salmal.dto.response.auth.LoginResponse;
import com.salmalteam.salmal.dto.response.auth.TokenResponse;
import com.salmalteam.salmal.exception.auth.AuthException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;
    @Mock
    TokenProvider tokenProvider;
    @Mock
    TokenRepository tokenRepository;
    @Mock
    MemberService memberService;

    @Nested
    class 로그인_테스트{

        @Test
        void 기가입자인_경우_정상적으로_접근_토큰과_재발급_토큰을_발급한다(){
            // given
            final Long memberId = 1L;
            final String accessToken = "accessToken";
            final String refreshToken = "refreshToken";
            final Long refreshTokenExpiry = 10000L;
            final String providerId = "providerId";
            final LoginRequest loginRequest = new LoginRequest("providerId");

            given(memberService.findMemberIdByProviderId(eq(providerId))).willReturn(memberId);
            given(tokenProvider.createAccessToken(eq(memberId))).willReturn(accessToken);
            given(tokenProvider.createRefreshToken(eq(memberId))).willReturn(refreshToken);
            given(tokenProvider.getTokenExpiry(eq(refreshToken))).willReturn(refreshTokenExpiry);

            // when
            final LoginResponse loginResponse = authService.login(loginRequest);

            assertAll(
                    () -> assertThat(loginResponse).isEqualTo(LoginResponse.of(accessToken, refreshToken)),
                    () ->  verify(tokenRepository, times(1)).saveRefreshToken(any(RefreshToken.class))
            );

        }

    }

    @Nested
    class 회원_가입_테스트{
        @Test
        void 접근_토큰과_재발급_토큰을_발급한다(){

            // given
            final Long memberId = 1L;
            final String nickName = "닉네임";
            final Boolean marketingInformationConsent = false;
            final String accessToken = "accessToken";
            final String refreshToken = "refreshToken";
            final Long refreshTokenExpiry = 10000L;
            final String providerId = "providerId";
            final SignUpRequest signUpRequest = new SignUpRequest(providerId, nickName, marketingInformationConsent);

            given(memberService.save(eq(providerId), any())).willReturn(memberId);
            given(tokenProvider.createAccessToken(eq(memberId))).willReturn(accessToken);
            given(tokenProvider.createRefreshToken(eq(memberId))).willReturn(refreshToken);
            given(tokenProvider.getTokenExpiry(eq(refreshToken))).willReturn(refreshTokenExpiry);

            // when
            final LoginResponse loginResponse = authService.signUp(providerId, signUpRequest);

            // then
            assertAll(
                    () -> assertThat(loginResponse).isEqualTo(LoginResponse.of(accessToken, refreshToken)),
                    () ->  verify(tokenRepository, times(1)).saveRefreshToken(any(RefreshToken.class))
            );
        }
    }

    @Nested
    class 토큰_재발급_테스트{
        final Long memberId = 1L;
        final String refreshToken = "refreshToken";
        final String accessToken = "accessToken";
        @Test
        void 재발급_토큰을_통해서_접근_토큰을_재발급_한다(){

            // given
            given(tokenProvider.isValidRefreshToken(eq(refreshToken))).willReturn(true);
            given(tokenRepository.existsRefreshTokenById(eq(refreshToken))).willReturn(true);
            given(tokenProvider.getMemberIdFromToken(eq(refreshToken))).willReturn(memberId);
            given(tokenProvider.createAccessToken(eq(memberId))).willReturn(accessToken);

            // when
            final TokenResponse tokenResponse = authService.reissueAccessToken(refreshToken);

            // then
            assertAll(
                    () -> assertThat(tokenResponse).isEqualTo(TokenResponse.from(accessToken))
            );
        }

        @Test
        void 재발급_토큰이_유효하지_않으면_예외를_발생시킨다(){

            // given
            given(tokenProvider.isValidRefreshToken(any())).willReturn(false);

            // when & then
            assertThatThrownBy(() -> authService.reissueAccessToken(refreshToken))
                    .isInstanceOf(AuthException.class);
        }

        @Test
        void 재발급_토큰이_존재하지_않으면_예외를_발생시킨다(){

            // given
            given(tokenProvider.isValidRefreshToken(any())).willReturn(true);
            given(tokenRepository.existsRefreshTokenById(any())).willReturn(false);

            // when & then
            assertThatThrownBy(() -> authService.reissueAccessToken(refreshToken))
                    .isInstanceOf(AuthException.class);
        }
    }
}