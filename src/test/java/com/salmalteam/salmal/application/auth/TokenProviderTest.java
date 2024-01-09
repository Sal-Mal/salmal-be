package com.salmalteam.salmal.application.auth;

import com.salmalteam.salmal.auth.application.TokenProvider;
import com.salmalteam.salmal.auth.infrastructure.JwtProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenProviderTest {

    private TokenProvider tokenProvider;
    private final String SECRET_KEY = "K".repeat(32);
    private final Long ACCESS_TOKEN_EXPIRY = 100000L;
    private final Long REFRESH_TOKEN_EXPIRY = 100000000L;
    @BeforeEach
    void setUp(){
        tokenProvider = new JwtProvider(ACCESS_TOKEN_EXPIRY, REFRESH_TOKEN_EXPIRY, SECRET_KEY);
    }

    @Nested
    class 접근_토큰_생성_테스트{
        @Test
        void 전달받은_ID_를_페이로드에_넣어_접근토큰을_생성_한다(){
            // given
            final Long id = 32L;

            // when
            final String accessToken = tokenProvider.createAccessToken(id);

            // then
            assertThat(accessToken).isNotNull();
        }
    }

    @Nested
    class 재발급_토큰_생성_테스트{
        @Test
        void 전달받은_ID_를_페이로드에_넣어_재발급토큰을_생성한다(){
            // given
            final Long id = 32L;
            // when
            final String refreshToken = tokenProvider.createRefreshToken(id);
            // then
            assertThat(refreshToken).isNotNull();
        }
    }

    @Nested
    class 토큰_유효기간_추출_테스트{

        @Test
        void 매개변수로_토큰을_전달받으면_해당_토큰의_유효기간을_반환한다(){

            // given
            final Long id = 32L;
            final String accessToken = tokenProvider.createAccessToken(id);
            final String refreshToken = tokenProvider.createRefreshToken(id);

            // when
            final Long accessTokenExpiry = tokenProvider.getTokenExpiry(accessToken);
            final Long refreshTokenExpiry = tokenProvider.getTokenExpiry(refreshToken);

            // then
            assertAll(
                    () ->  assertTrue(accessTokenExpiry >= ACCESS_TOKEN_EXPIRY - 10000 && accessTokenExpiry <= ACCESS_TOKEN_EXPIRY + 10000),
                    () ->  assertTrue(refreshTokenExpiry >= REFRESH_TOKEN_EXPIRY - 10000 && accessTokenExpiry <= REFRESH_TOKEN_EXPIRY + 10000)
            );
        }
    }
}
