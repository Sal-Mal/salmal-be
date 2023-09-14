package com.salmalteam.salmal.domain.member;

import com.salmalteam.salmal.exception.auth.AuthException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ProviderTest {

    @ParameterizedTest
    @CsvSource(value = {"apple", "kakao", "KaKao", "KAKAO", "APPLE"})
    void 지원하는_소셜로그인은_대소문자_모두_수용한다(final String provider){
        Provider.from(provider);
    }

    @ParameterizedTest
    @CsvSource(value = {"naver", "google", "github"})
    void 지원하는_소셜_로그인_제공자_이외의_경우_예외가_발생한다(final String provider){

        // when & then
        assertThatThrownBy(() -> Provider.from(provider))
                .isInstanceOf(AuthException.class);
    }
}