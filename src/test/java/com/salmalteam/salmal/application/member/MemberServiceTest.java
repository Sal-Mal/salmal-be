package com.salmalteam.salmal.application.member;

import com.salmalteam.salmal.domain.member.MemberRepository;
import com.salmalteam.salmal.dto.request.auth.SignUpRequest;
import com.salmalteam.salmal.exception.member.MemberException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;
    @Mock
    MemberRepository memberRepository;

    @Nested
    class 회원_저장_테스트{
        @Test
        void 이미_존재하는_닉네임의_경우_예외가_발생한다(){
            // given
            final String provider = "KAKAO";
            final String providerId = "providerId";
            final String nickName = "닉네임";
            final Boolean marketingInformationConsent = true;
            final SignUpRequest signUpRequest = new SignUpRequest(providerId, nickName, marketingInformationConsent);
            given(memberRepository.existsByNickName(any())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> memberService.save(provider, signUpRequest)).isInstanceOf(MemberException.class);
        }
    }

    @Nested
    class providerId_로_회원_조회_테스트{
        @Test
        void 회원이_존재하지_않으면_예외가_발생한다(){

            // given
            final String providerId = "providerId";
            given(memberRepository.findByProviderId(eq(providerId))).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberService.findMemberIdByProviderId(providerId)).isInstanceOf(MemberException.class);
        }
    }
}