package com.salmalteam.salmal.domain.member;

import com.salmalteam.salmal.member.entity.NickName;
import com.salmalteam.salmal.member.exception.MemberException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class NickNameTest {

    @ParameterizedTest
    @CsvSource(value = {"닉", "네", "닉네임닉네임닉네임닉네임닉네임닉네임닉네임닉네임"})
    void 닉네임의_길이가_2미만_또는_20초과일_경우_예외가_발생한다(final String nickName){

        // when & then
        assertThatThrownBy(() -> NickName.from(nickName)).isInstanceOf(MemberException.class);
    }
}