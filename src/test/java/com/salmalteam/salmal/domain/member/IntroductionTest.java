package com.salmalteam.salmal.domain.member;

import com.salmalteam.salmal.domain.member.entity.Introduction;
import com.salmalteam.salmal.domain.member.exception.MemberException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class IntroductionTest {

    @Test
    void 자기소개의_길이가_30초과일_경우_예외가_발생한다(){

        final String introduction = "자기소개글".repeat(10);
        // when & then
        assertThatThrownBy(() -> Introduction.from(introduction))
                .isInstanceOf(MemberException.class);
    }
}